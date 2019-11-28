package com.job.common.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.job.common.statuscode.ExceptionMessage;
import com.job.common.statuscode.ServerResponse;
import com.job.entity.*;
import com.job.mapper.UserInfoMapper;
import com.job.mapper.UserOrderMapper;
import com.job.service.UserMoneyService;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.ParseException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.web.client.RestTemplate;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.bind.annotation.adapters.HexBinaryAdapter;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.*;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.*;

/**
 * 微信工具类
 *
 * @author keith
 * @version 1.0
 * @date 2019/11/15
 */
@Slf4j
@Configuration
@Transactional(rollbackFor = Exception.class)
public class WxUtils {

    private final RestTemplate restTemplate;

    private final UserInfoMapper userInfoMapper;

    private final UserOrderMapper userOrderMapper;

    private final UserMoneyService userMoneyService;

    /**
     * 开发平台APPID
     */
    @Value("${wx.appId}")
    private String APPID;
    /**
     * 开发平台APPSECRET
     */
    @Value("${wx.appKey}")
    private String APPSECRET;

    /**
     * 商户号
     * 1491103722
     * 1482664972
     */
    private static final String MACID = "1482664972";

    /**
     * API密钥
     */
    private static final String APIKEY = "f4fa414a8e644bfa96c312f268b045e4";

    @Value("${wx.notifyUrl}")
    private String notifyUrl;

    public WxUtils(RestTemplate restTemplate, UserInfoMapper userInfoMapper, UserOrderMapper userOrderMapper, UserMoneyService userMoneyService) {
        this.restTemplate = restTemplate;
        this.userInfoMapper = userInfoMapper;
        this.userOrderMapper = userOrderMapper;
        this.userMoneyService = userMoneyService;
    }

    /**
     * 授权登录
     *
     * @param code 授权码
     * @return
     */
    public ServerResponse authorization(String code) {
        String tokenUrl = "https://api.weixin.qq.com/sns/oauth2/access_token?appid=" + APPID + "&secret="
                + APPSECRET + "&code=" + code + "&grant_type=authorization_code";
        try {
            Map<?, ?> tokenMap = doGet(tokenUrl);
            String openid = tokenMap.get("openid").toString();
            String token = tokenMap.get("access_token").toString();
            String infoUrl = "https://api.weixin.qq.com/sns/userinfo?access_token=" + token + "&openid=" + openid
                    + "&lang=zh_CN";
            Map<?, ?> infoMap = doGet(infoUrl);
            //成功获取授权,以下部分为业务逻辑处理了，
            if (infoMap != null && openid != null) {
                UserInfo userInfo = userInfoMapper.findByOpenId(openid);
                if (userInfo == null) {
                    UserInfo info = new UserInfo();
                    info.setOpenid(openid);
                    info.setNickname(infoMap.get("nickname").toString());
                    info.setHeadimgurl(infoMap.get("headimgurl").toString());
                    info.setSex(Integer.valueOf(infoMap.get("sex").toString()));
                    info.setProvince(infoMap.get("province").toString());
                    info.setCity(infoMap.get("city").toString());
                    info.setCountry(infoMap.get("country").toString());
                    userInfoMapper.insertSelective(info);
                    return ServerResponse.createBySuccess(info);
                } else {
                    userInfo.setOpenid(openid);
                    userInfo.setNickname(infoMap.get("nickname").toString());
                    userInfo.setHeadimgurl(infoMap.get("headimgurl").toString());
                    userInfo.setSex(Integer.valueOf(infoMap.get("sex").toString()));
                    userInfo.setProvince(infoMap.get("province").toString());
                    userInfo.setCountry(infoMap.get("country").toString());
                    userInfo.setCity(infoMap.get("city").toString());
                    userInfoMapper.updateByPrimaryKeySelective(userInfo);
                    return ServerResponse.createBySuccess(userInfo);
                }
            } else {
                return ServerResponse.createByErrorMessage("授权登录失败");
            }
        } catch (IOException e) {
            e.printStackTrace();
            return ServerResponse.createByErrorMessage("授权登录失败");
        }
    }

    /**
     * APP预支付
     *
     * @param userOrder 商品信息
     * @param request   请求参数
     * @return
     */
    public ServerResponse appPay(UserOrder userOrder, HttpServletRequest request) throws IOException {
        //金额转化分
        int money = userOrder.getMoney().multiply(new BigDecimal(100)).setScale(0, BigDecimal.ROUND_HALF_UP).intValue();
        // 设置客户端的ip地址
        String spbillCreateIp = getIpAddress(request);
        //随机数
        String nonceStr = 1 + RandomUtil.randomStr(15);
        // 微信app支付十个必须要传入的参数
        Map<String, Object> params = new HashMap<>(10);
        // 应用ID
        params.put("appid", APPID);
        // 商户号
        params.put("mch_id", MACID);
        // 随机字符串
        params.put("nonce_str", nonceStr);
        // 商品描述
        params.put("body", userOrder.getOrderDesc());
        // 商户订单号
        params.put("out_trade_no", userOrder.getOrderNum());
        // 总金额(分)
        params.put("total_fee", money);
        // 终端IP
        params.put("spbill_create_ip", spbillCreateIp);
        // 通知/回调地址
        params.put("notify_url", notifyUrl);
        // 交易类型:JS_API=公众号支付、NATIVE=扫码支付、APP=app支付
        params.put("trade_type", "APP");
        // 签名
        params.put("sign", sign(params, APIKEY));
        String xmlData = mapToXml(params);
        String payUrl = "https://api.mch.weixin.qq.com/pay/unifiedorder";
        String wxRetXmlData = doPost(payUrl, xmlData);
        Map wxRetMapData = xmlToMap(wxRetXmlData);
        Assert.notNull(wxRetMapData, ExceptionMessage.XML_DATA_INCORRECTNESS.getMessage());
        log.info("weChat pre pay result data: {}", wxRetMapData);
        if ("SUCCESS".equals(wxRetMapData.get("result_code"))) {
            if ("SUCCESS".equals(wxRetMapData.get("return_code"))) {
                Map<String, Object> result = new HashMap<>(10);
                result.put("appid", APPID);
                result.put("partnerid", MACID);
                result.put("prepayid", wxRetMapData.get("prepay_id").toString());
                result.put("noncestr", nonceStr);
                result.put("timestamp", RandomUtil.getTimestamp());
                result.put("package", "Sign=WXPay");
                // 对返回给App端的数据进行签名
                result.put("sign", sign(result, APIKEY));
                System.out.println("appPay:" + result);
                return ServerResponse.createBySuccess(result);
            } else {
                return ServerResponse.createByErrorMessage(wxRetMapData.get("err_code_des").toString());
            }
        } else {
            return ServerResponse.createByErrorMessage(wxRetMapData.get("return_msg").toString());
        }
    }

    /**
     * 提现（商家付款到微信）
     *
     * @return
     * @throws ParseException
     */
    public ServerResponse<String> cashOut(CashOutOrder cashOutOrder, HttpServletRequest request) throws ParseException {
        //金额转化分
        int money = cashOutOrder.getTotalFee().multiply(new BigDecimal(100)).setScale(0, BigDecimal.ROUND_HALF_UP).intValue();
        String spbillCreateIp = getIpAddress(request);
        //随机数
        String nonceStr = 1 + RandomUtil.randomStr(15);
        //等待确认转账金额,ip,openid的来源
        Map<String, Object> params = new HashMap<>();
        // 参数：开始生成第一次签名
        params.put("mch_appid", APPID);
        params.put("mchid", MACID);
        params.put("partner_trade_no", cashOutOrder.getTradeNo());
        params.put("nonce_str", nonceStr);
        params.put("openid", cashOutOrder.getOpenid());
        //是否校验用户姓名 NO_CHECK：不校验真实姓名  FORCE_CHECK：强校验真实姓名
        params.put("check_name", "NO_CHECK");
        params.put("amount",money);
        params.put("spbill_create_ip", spbillCreateIp);
        params.put("desc", cashOutOrder.getRemarks());
        String sign = sign(params, APIKEY);
        params.put("sign", sign);
        String xmlData = mapToXml(params);
        String cashOutUrl = "https://api.mch.weixin.qq.com/mmpaymkttransfers/promotion/transfers";
        try {
            String wxRetXmlData = doPost(cashOutUrl, xmlData);
            Map wxRetMapData = xmlToMap(wxRetXmlData);
            assert wxRetMapData != null;
            if ("SUCCESS".equals(wxRetMapData.get("result_code"))) {
                if ("SUCCESS".equals(wxRetMapData.get("return_code"))) {
                    return ServerResponse.createBySuccess(wxRetMapData.get("partner_trade_no").toString());
                } else {
                    return ServerResponse.createByErrorMessage(wxRetMapData.get("err_code_des").toString());
                }
            } else {
                return ServerResponse.createByErrorMessage(wxRetMapData.get("return_msg").toString());
            }
        } catch (Exception e) {
            log.error("提现发生异常");
            e.printStackTrace();
            return ServerResponse.createByError();
        }
    }

    /**
     * 支付回调，返回微信通知
     *
     * @param request
     * @param response
     * @return
     * @throws IOException
     */
    public void notify(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String wxRetXml = getRequestData(request);
        Map<String, String> wxRetMap = xmlToMap(wxRetXml);
        Assert.notNull(wxRetMap, ExceptionMessage.XML_DATA_INCORRECTNESS.getMessage());
        //当返回的return_code为SUCCESS则回调成功
        if ("SUCCESS".equalsIgnoreCase(wxRetMap.get("return_code"))) {
            // 通知微信收到回调
            String resXml = "<xml><return_code><![CDATA[SUCCESS]]></return_code><return_msg><![CDATA[OK]]></return_msg></xml>";
            BufferedOutputStream out = new BufferedOutputStream(response.getOutputStream());
            out.write(resXml.getBytes());
            out.flush();
            out.close();
            //更新订单状态
            UserOrder userOrder = userOrderMapper.selectByOrderNum(wxRetMap.get("out_trade_no"));
            userOrder.setFinishTime(new Date());
            if ("SUCCESS".equalsIgnoreCase(wxRetMap.get("result_code"))) {
                userOrder.setOrderStatus(2);
                userMoneyService.paySuccess(userOrder);
            } else {
                userOrder.setOrderStatus(3);
            }
            //更新订单信息
            userOrderMapper.updateByPrimaryKey(userOrder);
            log.info("notify success");
        } else {
            log.error("notify failed");
        }
    }

    /**
     * get方式请求
     *
     * @param url
     * @return
     * @throws IOException
     */
    private Map<?, ?> doGet(String url) throws IOException {
        return new ObjectMapper().readValue(new String(Objects.requireNonNull(restTemplate.getForEntity(url, String.class).getBody()).getBytes("ISO8859-1"), StandardCharsets.UTF_8), Map.class);
    }

    /**
     * get方式请求
     *
     * @param url
     * @return
     * @throws IOException
     */
    private String doPost(String url, String params) throws IOException {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        HttpEntity<String> request = new HttpEntity<>(params, headers);
        return new String(Objects.requireNonNull(restTemplate.postForEntity(url, request, String.class).getBody()).getBytes("ISO8859-1"), StandardCharsets.UTF_8);
    }

    /**
     * Map格式转换为xml字符串
     *
     * @param params
     * @return
     */
    public static String mapToXml(Map<String, Object> params) {
        StringBuilder sb = new StringBuilder();
        Set<Map.Entry<String, Object>> es = params.entrySet();
        Iterator<Map.Entry<String, Object>> it = es.iterator();
        sb.append("<xml>");
        while (it.hasNext()) {
            Map.Entry<String, Object> entry = it.next();
            String k = entry.getKey();
            Object v = entry.getValue();
            sb.append("<").append(k).append(">").append(v).append("</").append(k).append(">");
        }
        sb.append("</xml>");
        return sb.toString();
    }

    /**
     * XML格式字符串转换为Map
     *
     * @param xmlStr
     * @return
     */
    public static Map<String, String> xmlToMap(String xmlStr) {
        try (InputStream inputStream = new ByteArrayInputStream(xmlStr.getBytes(StandardCharsets.UTF_8))) {
            Map<String, String> data = new HashMap<>();
            DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
            Document doc = documentBuilder.parse(inputStream);
            doc.getDocumentElement().normalize();
            NodeList nodeList = doc.getDocumentElement().getChildNodes();
            for (int idx = 0; idx < nodeList.getLength(); ++idx) {
                Node node = nodeList.item(idx);
                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    Element element = (Element) node;
                    data.put(element.getNodeName(), element.getTextContent());
                }
            }
            return data;
        } catch (Exception ex) {
            log.warn("xml convert to map failed message: {}", ex.getMessage());
            return null;
        }
    }

    /**
     * 获得request的ip
     *
     * @param request
     * @return
     */
    public static String getIpAddress(HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For");
        if (Objects.nonNull(ip) && !"unKnown".equalsIgnoreCase(ip)) {
            // 多次反向代理后会有多个ip值，第一个ip才是真实ip
            int index = ip.indexOf(",");
            if (index != -1) {
                return ip.substring(0, index);
            } else {
                return ip;
            }
        }
        ip = request.getHeader("X-Real-IP");
        if (Objects.nonNull(ip) && !"unKnown".equalsIgnoreCase(ip)) {
            return ip;
        }
        return request.getRemoteAddr();
    }

    /**
     * 支付参数生成签名
     *
     * @param params
     * @param apiKey
     * @return
     */
    public static String sign(Map<String, Object> params, String apiKey) {
        StringBuilder sb = new StringBuilder();
        Set<Map.Entry<String, Object>> set = new TreeMap<>(params).entrySet();
        for (Map.Entry<String, Object> entry : set) {
            String k = entry.getKey();
            Object v = entry.getValue();
            sb.append(k).append("=").append(v).append("&");
        }
        sb.append("key=").append(apiKey);
        return Objects.requireNonNull(MD5(sb.toString())).toUpperCase();
    }

    /**
     * 生成md5摘要
     *
     * @param content
     * @return
     */
    public static String MD5(String content) {
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("MD5");
            messageDigest.update(content.getBytes(StandardCharsets.UTF_8));
            byte[] hashCode = messageDigest.digest();
            return new HexBinaryAdapter().marshal(hashCode).toLowerCase();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 读取 request 中的数据
     *
     * @param request
     * @return
     */
    public static String getRequestData(HttpServletRequest request) {
        StringBuilder sb = new StringBuilder();
        BufferedReader br = null;
        String line;
        try {
            // 接收request数据流，并指定编码格式接收
            br = new BufferedReader(new InputStreamReader(request.getInputStream(), StandardCharsets.UTF_8));
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return sb.toString();
    }

    /**
     * 返回微信错误码描述
     *
     * @param errCode 错误码
     * @return
     */
    private String errorDesc(String errCode) {
        if ("NO_AUTH".equals(errCode)) {
            return "没有该接口权限";
        } else if ("AMOUNT_LIMIT".equals(errCode)) {
            return "金额超限";
        } else if ("PARAM_ERROR".equals(errCode)) {
            return "参数错误";
        } else if ("OPENID_ERROR".equals(errCode)) {
            return "Openid错误";
        } else if ("SEND_FAILED".equals(errCode)) {
            return "付款错误";
        } else if ("NOTENOUGH".equals(errCode)) {
            return "余额不足";
        } else if ("SYSTEMERROR".equals(errCode)) {
            return "系统繁忙，请稍后再试。";
        } else if ("NAME_MISMATCH".equals(errCode)) {
            return "姓名校验出错";
        } else if ("SIGN_ERROR".equals(errCode)) {
            return "签名错误";
        } else if ("XML_ERROR".equals(errCode)) {
            return "Post内容出错";
        } else if ("FATAL_ERROR".equals(errCode)) {
            return "两次请求参数不一致";
        } else if ("FREQ_LIMIT".equals(errCode)) {
            return "超过频率限制，请稍后再试。";
        } else if ("MONEY_LIMIT".equals(errCode)) {
            return "已经达到今日付款总额上限/已达到付款给此用户额度上限";
        } else if ("CA_ERROR".equals(errCode)) {
            return "商户API证书校验出错";
        } else if ("V2_ACCOUNT_SIMPLE_BAN".equals(errCode)) {
            return "无法给非实名用户付款";
        } else if ("PARAM_IS_NOT_UTF8".equals(errCode)) {
            return "请求参数中包含非utf8编码字符";
        } else if ("SENDNUM_LIMIT".equals(errCode)) {
            return "该用户今日付款次数超过限制,如有需要请进入【微信支付商户平台-产品中心-企业付款到零钱-产品设置】进行修改";
        } else if ("RECV_ACCOUNT_NOT_ALLOWED".equals(errCode)) {
            return "收款账户不在收款账户列表";
        } else if ("PAY_CHANNEL_NOT_ALLOWED".equals(errCode)) {
            return "本商户号未配置API发起能力 ";
        } else {
            return "未知错误";
        }
    }
}
