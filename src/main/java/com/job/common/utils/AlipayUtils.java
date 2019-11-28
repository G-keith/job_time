package com.job.common.utils;

import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.domain.AlipayFundTransToaccountTransferModel;
import com.alipay.api.domain.AlipayTradeAppPayModel;
import com.alipay.api.internal.util.AlipaySignature;
import com.alipay.api.request.AlipayFundTransToaccountTransferRequest;
import com.alipay.api.request.AlipayTradeAppPayRequest;
import com.alipay.api.response.AlipayFundTransToaccountTransferResponse;
import com.alipay.api.response.AlipayTradeAppPayResponse;
import com.job.common.statuscode.ServerResponse;
import com.job.entity.*;
import com.job.mapper.UserOrderMapper;
import com.job.service.UserMoneyService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 支付宝工具类
 *
 * @author keith
 * @version 1.0
 * @date 2019/11/17
 */
@Slf4j
@Configuration
@Transactional(rollbackFor = Exception.class)
public class AlipayUtils {

    /**
     * 1.商户appid,使用商户自己的appid即可
     */
    @Value("${zfb.appId}")
    private String APPID;
    /**
     * 私钥 pkcs8格式的，与在支付宝存储的公钥对应
     */
    @Value("${zfb.appPrivateKey}")
    private String APP_PRIVATE_KEY;
    /**
     * 3.支付宝公钥，支付宝生成的公钥，切勿与商户公钥混淆
     */
    @Value("${zfb.alipayPublicKey}")
    private String ALIPAY_PUBLIC_KEY;
    /**
     * 4.服务器异步通知页面路径 需http://或者https://格式的完整路径，必须外网可以正常访问，可以使用natapp进行外网映射
     */
    @Value("${zfb.notifyUrl}")
    private String notifyUrl;
    /**
     * 页面跳转同步通知页面路径 需http://或者https://格式的完整路径，必须外网可以正常访问 商户可以自定义同步跳转地址
     */
    @Value("${zfb.returnUrl}")
    private String returnUrl;

    private final UserOrderMapper userOrderMapper;

    private final UserMoneyService userMoneyService;

    /**
     * 请求支付宝的网关地址,此处为沙箱测试地址，正式环境替换即可
     */
    @Value("${zfb.url}")
    private String url;

    /**
     * 加密类型
     */
    private static String SIGNTYPE = "RSA2";

    /**
     * 编码
     */
    private static String CHARSET = "UTF-8";

    /**
     * 返回格式
     */
    private static String FORMAT = "json";

    public AlipayUtils(UserOrderMapper userOrderMapper, UserMoneyService userMoneyService) {
        this.userOrderMapper = userOrderMapper;
        this.userMoneyService = userMoneyService;
    }


    /**
     * 支付宝支付
     *
     * @param userOrder
     * @return
     */
    public ServerResponse alipay(UserOrder userOrder) {
        //实例化客户端
        AlipayClient alipayClient = new DefaultAlipayClient(url, APPID, APP_PRIVATE_KEY, FORMAT, CHARSET, ALIPAY_PUBLIC_KEY, SIGNTYPE);
        //实例化具体API对应的request类,类名称和接口名称对应,当前调用接口名称：alipay.trade.app.pay
        AlipayTradeAppPayRequest request = new AlipayTradeAppPayRequest();
        //SDK已经封装掉了公共参数，这里只需要传入业务参数。以下方法为sdk的model入参方式(model和biz_content同时存在的情况下取biz_content)。
        AlipayTradeAppPayModel model = new AlipayTradeAppPayModel();
        //商品标题
        model.setSubject(userOrder.getOrderDesc());
        //商家订单的唯一编号
        model.setOutTradeNo(userOrder.getOrderNum());
        //超时关闭该订单时间
        model.setTimeoutExpress("90m");
        //订单总金额
        model.setTotalAmount(userOrder.getMoney().toString());
        //销售产品码，商家和支付宝签约的产品码，为固定值QUICK_MSECURITY_PAY
        model.setProductCode("QUICK_MSECURITY_PAY");
        request.setBizModel(model);
        //回调地址
        request.setNotifyUrl(notifyUrl);
        String orderString = "";
        try {
            //这里和普通的接口调用不同，使用的是sdkExecute
            AlipayTradeAppPayResponse response = alipayClient.sdkExecute(request);
            //orderString 可以直接给客户端请求，无需再做处理。
            orderString = response.getBody();
            System.out.println(orderString);
            if (orderString != null && !"".equals(orderString)) {
                return ServerResponse.createBySuccess(orderString);
            } else {
                return ServerResponse.createByErrorMessage("订单生成失败");
            }
        } catch (AlipayApiException e) {
            e.printStackTrace();
            return ServerResponse.createByErrorMessage("订单生成失败");
        }
    }

    public void notify(HttpServletRequest request) throws AlipayApiException, UnsupportedEncodingException {
        Map<String, String> params = new HashMap<>();
        Map requestParams = request.getParameterMap();
        System.out.println(requestParams);
        for (Object o : requestParams.keySet()) {
            String name = (String) o;
            String[] values = (String[]) requestParams.get(name);
            String valueStr = "";
            for (int i = 0; i < values.length; i++) {
                valueStr = (i == values.length - 1) ? valueStr + values[i]
                        : valueStr + values[i] + ",";
            }
            //乱码解决，这段代码在出现乱码时使用。
            valueStr = new String(valueStr.getBytes(StandardCharsets.ISO_8859_1), StandardCharsets.UTF_8);
            params.put(name, valueStr);
        }
        log.info("ssss" + params.toString());
        //切记alipaypublickey是支付宝的公钥，请去open.alipay.com对应应用下查看。
        boolean flag = AlipaySignature.rsaCheckV1(params, ALIPAY_PUBLIC_KEY, CHARSET, SIGNTYPE);
        log.info("支付标志" + flag);

        //更新订单状态
        UserOrder userOrder = userOrderMapper.selectByOrderNum(requestParams.get("out_trade_no").toString());
        userOrder.setFinishTime(new Date());
        if (flag) {
            userOrder.setOrderStatus(2);
            userMoneyService.paySuccess(userOrder);
        } else {
            userOrder.setOrderStatus(3);
        }
        //更新订单信息
        userOrderMapper.updateByPrimaryKey(userOrder);
    }

    public ServerResponse cashOut(CashOutOrder cashOutOrder) {
        //填写自己创建的app的对应参数
        AlipayClient alipayClient = new DefaultAlipayClient(url, APPID, APP_PRIVATE_KEY, FORMAT, CHARSET, ALIPAY_PUBLIC_KEY, SIGNTYPE);
        AlipayFundTransToaccountTransferRequest transferAccountsRequest = new AlipayFundTransToaccountTransferRequest();
        AlipayFundTransToaccountTransferModel model = new AlipayFundTransToaccountTransferModel();
        model.setOutBizNo(cashOutOrder.getTradeNo());
        model.setPayeeType("ALIPAY_LOGONID");
        model.setPayeeAccount(cashOutOrder.getZfbAccount());
        model.setAmount(cashOutOrder.getTotalFee().toString());
        model.setPayerShowName(cashOutOrder.getZfbName());
        model.setRemark(cashOutOrder.getRemarks());
        transferAccountsRequest.setBizModel(model);
        try {
            AlipayFundTransToaccountTransferResponse response = alipayClient.execute(transferAccountsRequest);
            if (response.isSuccess()) {
                System.out.println(response.getBody());
                return ServerResponse.createBySuccess(response.getBody());
            } else {
                return ServerResponse.createByErrorMessage("调用失败");
            }
        } catch (AlipayApiException e) {
            e.printStackTrace();
            return ServerResponse.createByError();
        }
    }
}
