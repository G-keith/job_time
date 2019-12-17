package com.job.service;

import cn.hutool.json.JSON;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.qcloudsms.SmsSingleSender;
import com.github.qcloudsms.SmsSingleSenderResult;
import com.job.common.page.PageVO;
import com.job.common.statuscode.ServerResponse;
import com.job.common.utils.AlipayUtils;
import com.job.common.utils.MD5Util;
import com.job.common.utils.RandomUtil;
import com.job.common.utils.WxUtils;
import com.job.entity.*;
import com.job.mapper.UserInfoMapper;
import com.job.mapper.UserMoneyMapper;
import com.job.mapper.UserOrderMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.Date;
import java.util.Map;
import java.util.Random;

/**
 * @author keith
 * @version 1.0
 * @date 2019/10/28
 */
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class UserInfoService {

    @Value("${appId}")
    private int appId;

    @Value("${appKey}")
    private String appKey;

    @Value("${templateId}")
    private int templateId;

    @Value("${smsSign}")
    private String smsSign;

    @Value("${wx.appId}")
    private String wxAppId;

    @Value("${wx.appKey}")
    private String wxAppKey;

    @Autowired
    private RestTemplate restTemplate;

    private final UserInfoMapper userInfoMapper;

    private final UserMoneyMapper userMoneyMapper;

    private final WxUtils wxUtils;

    private final AlipayUtils alipayUtils;

    private final UserOrderMapper userOrderMapper;

    public UserInfoService(UserInfoMapper userInfoMapper, UserMoneyMapper userMoneyMapper, WxUtils wxUtils, AlipayUtils alipayUtils, UserOrderMapper userOrderMapper) {
        this.userInfoMapper = userInfoMapper;
        this.userMoneyMapper = userMoneyMapper;
        this.wxUtils = wxUtils;
        this.alipayUtils = alipayUtils;
        this.userOrderMapper = userOrderMapper;
    }

    /**
     * 手机号登录
     *
     * @param phone 账号密码
     * @return 0存在，1不存在
     */
    public ServerResponse signIn(String phone, String password) {
        //查询用户信息
        UserInfo result = userInfoMapper.findPassword(phone, password);
        if (result != null) {
            if (result.getStatus() == 1) {
                return ServerResponse.createByErrorCodeMessage(2, "用户为黑名单，不可登录");
            } else {
                return ServerResponse.createBySuccess(result);
            }
        } else {
            return ServerResponse.createByErrorMessage("账户或者密码失败");
        }
    }

    /**
     * 注册
     *
     * @param userInfo
     * @return
     */
    public ServerResponse register(UserInfo userInfo) {
        UserInfo user = userInfoMapper.findByPhone(userInfo.getPhone());
        if (user != null) {
            return ServerResponse.createByErrorCodeMessage(2, "手机号已经存在");
        }
        userInfo.setUID(getUid());
        UserInfo info = userInfoMapper.findByOpenId(userInfo.getOpenid());
        int result;
        if (info == null) {
            result = userInfoMapper.insertSelective(userInfo);
            //注册时插入用户账户信息
            userMoneyMapper.insertMoney(userInfo.getUserId());
        } else {
            info.setOpenid(userInfo.getOpenid());
            info.setHeadimgurl(userInfo.getHeadimgurl());
            info.setNickname(userInfo.getNickname());
            info.setSex(userInfo.getSex());
            info.setCountry(userInfo.getCountry());
            info.setProvince(userInfo.getProvince());
            info.setCity(userInfo.getCity());
            info.setPassword(userInfo.getPassword());
            info.setPhone(userInfo.getPhone());
            result = userInfoMapper.updateByPrimaryKeySelective(info);
        }
        if (result > 0) {
            return ServerResponse.createBySuccess();
        } else {
            return ServerResponse.createByError();
        }
    }

    /**
     * 网页注册
     *
     * @param userInfo
     * @return
     */
    public ServerResponse webRegister(UserInfo userInfo) {
        UserInfo user = userInfoMapper.findByPhone(userInfo.getPhone());
        if (user != null) {
            return ServerResponse.createByErrorCodeMessage(2, "手机号已经存在");
        }
        userInfo.setUID(getUid());
        int result = userInfoMapper.insertSelective(userInfo);
        //注册时插入用户账户信息
        userMoneyMapper.insertMoney(userInfo.getUserId());
        if (result > 0) {
            return ServerResponse.createBySuccess();
        } else {
            return ServerResponse.createByError();
        }
    }

    /**
     * 绑定微信
     * @param userInfo
     * @return
     */
    public ServerResponse bindWx(UserInfo userInfo){
        //查询用户信息
        UserInfo result = userInfoMapper.findPassword(userInfo.getPhone(), userInfo.getPassword());
        if(result!=null){
            result.setOpenid(userInfo.getOpenid());
            result.setHeadimgurl(userInfo.getHeadimgurl());
            result.setNickname(userInfo.getNickname());
            result.setSex(userInfo.getSex());
            result.setProvince(userInfo.getProvince());
            result.setCountry(userInfo.getCountry());
            result.setCity(userInfo.getCity());
            userInfoMapper.updateByPrimaryKeySelective(result);
            return ServerResponse.createBySuccess(result);
        }else{
            return ServerResponse.createByErrorMessage("账户或者密码不对");
        }
    }

    /**
     * 手机号是否存在
     *
     * @param phone
     * @return
     */
    public ServerResponse isExistPhone(String phone) {
        UserInfo userInfo = userInfoMapper.findByPhone(phone);
        if (userInfo == null) {
            return ServerResponse.createBySuccessMessage("手机号不存在");
        } else {
            return ServerResponse.createByErrorMessage("手机号已存在");
        }
    }

    /**
     * 获取微信信息
     *
     * @param code
     * @return
     */
    public ServerResponse wxInfo(String code) {
        try {
            return ServerResponse.createBySuccess(wxUtils.authorization(code));
        } catch (IOException e) {
            e.printStackTrace();
            return ServerResponse.createByErrorMessage("获取信息失败");
        }
    }

    /**
     * 插入邀请码
     *
     * @param uid
     * @param userId
     * @return
     */
    public ServerResponse insertUid(String uid, Integer userId) {
        UserInfo userInfo = userInfoMapper.findByUserId(userId);
        userInfo.setUpUID(uid);
        int result = userInfoMapper.updateByPrimaryKeySelective(userInfo);
        if (result > 0) {
            return ServerResponse.createBySuccess();
        } else {
            return ServerResponse.createByError();
        }

    }

    /**
     * @throws
     * @title weChatLogin
     * @description 微信授权登录
     * @author Kuangzc
     * @updateTime 2019-9-12 16:00:51
     */
    public ServerResponse weChatLogin(String code) throws IOException {
        Map<?, ?> infoMap = wxUtils.authorization(code);
        if (infoMap == null) {
            return ServerResponse.createByErrorMessage("微信授权失败");
        }
        UserInfo userInfo = userInfoMapper.findByOpenId(infoMap.get("openid").toString());
        if (userInfo != null) {
            userInfoMapper.updateByPrimaryKeySelective(userInfo);
            return ServerResponse.createBySuccess(userInfo);
        } else {
            UserInfo info = new UserInfo();
            info.setOpenid(infoMap.get("openid").toString());
            info.setNickname(infoMap.get("nickname").toString());
            info.setHeadimgurl(infoMap.get("headimgurl").toString());
            info.setSex(Integer.valueOf(infoMap.get("sex").toString()));
            info.setProvince(infoMap.get("province").toString());
            info.setCity(infoMap.get("city").toString());
            info.setCountry(infoMap.get("country").toString());
            info.setUID(getUid());
            userInfoMapper.insertSelective(info);
            //注册时插入用户账户信息
            userMoneyMapper.insertMoney(info.getUserId());
            return ServerResponse.createBySuccess(info);
        }
    }


    /**
     * 发送验证码并存放到数据库中去
     *
     * @param phone 手机号
     * @return 验证码
     */
    public ServerResponse insertCode(String phone) {
        String code = getCode();
        //判断是否发送成功
        int res = userInfoMapper.insertCode(phone, code);
        if (res > 0) {
            //存放到数据库中去
            boolean result = sendCode(phone, code);
            if (result) {
                return ServerResponse.createBySuccess(code);
            } else {
                return ServerResponse.createByErrorMessage("发送失败");
            }
        } else {
            return ServerResponse.createByErrorMessage("发送失败");
        }
    }

    /**
     * 检验验证码是否正确
     *
     * @param phone 手机号
     * @param code  验证码
     * @return 0正确，1不正确
     */
    public ServerResponse checkCode(String phone, String code) {
        String result = userInfoMapper.findCodeByPhone(phone);
        if (code.equals(result)) {
            return ServerResponse.createBySuccess();
        } else {
            return ServerResponse.createByErrorMessage("验证码不正确");
        }
    }

    /**
     * 查询所有用户信息
     *
     * @param pageNo
     * @param pageSize
     * @return
     */
    public ServerResponse findAll(Integer pageNo, Integer pageSize, String phone, Integer status) {
        Page<UserInfo> page = PageHelper.startPage(pageNo, pageSize);
        userInfoMapper.findAll(phone, status);
        return ServerResponse.createBySuccess(PageVO.build(page));
    }

    /**
     * 查询所有用户信息
     *
     * @param pageNo
     * @param pageSize
     * @return
     */
    public ServerResponse findBlacklist(Integer pageNo, Integer pageSize, String phone) {
        Page<UserInfo> page = PageHelper.startPage(pageNo, pageSize);
        userInfoMapper.findAll(phone, 1);
        return ServerResponse.createBySuccess(PageVO.build(page));
    }

    /**
     * 更新用户信息
     *
     * @param userInfo 用户信息
     * @return
     */
    public ServerResponse updateUserInfo(UserInfo userInfo, Integer adminId) {
        int result = userInfoMapper.updateByPrimaryKeySelective(userInfo);
        Map<String, Object> map = userInfoMapper.selectById(adminId);
        if (result > 0) {
            userInfo = userInfoMapper.findByUserId(userInfo.getUserId());
            String content;
            if (userInfo.getPhone() != null) {
                content = "管理员" + map.get("account").toString() + "修改了手机号为" + userInfo.getPhone() + "的个人信息";
            } else {
                content = "管理员" + map.get("account").toString() + "修改了用户呢称为" + userInfo.getPhone() + "的个人信息";
            }
            userInfoMapper.insertFoot(adminId, userInfo.getUserId(), content);
            return ServerResponse.createBySuccess();
        } else {
            return ServerResponse.createByError();
        }
    }

    /**
     * 更新用户信息
     *
     * @param userInfo 用户信息
     * @return
     */
    public ServerResponse updateBlack(UserInfo userInfo, Integer adminId) {
        int result = userInfoMapper.updateByPrimaryKeySelective(userInfo);
        Map<String, Object> map = userInfoMapper.selectById(adminId);
        if (result > 0) {
            userInfo = userInfoMapper.findByUserId(userInfo.getUserId());
            String content;
            if (userInfo.getPhone() != null) {
                content = "管理员" + map.get("account").toString() + "将手机号为" + userInfo.getPhone() + "加入了黑名单";
            } else {
                content = "管理员" + map.get("account").toString() + "将用户呢称为" + userInfo.getPhone() + "加入了黑名单";
            }
            userInfoMapper.insertFoot(adminId, userInfo.getUserId(), content);
            return ServerResponse.createBySuccess();
        } else {
            return ServerResponse.createByError();
        }
    }

    /**
     * 修改管理员账户信息
     *
     * @param account
     * @param password
     * @return
     */
    public ServerResponse modifyAdminInfo(String account, String password, Integer adminId) {
        int res = userInfoMapper.selectInfo(account, adminId);
        if (res > 0) {
            return ServerResponse.createByErrorCodeMessage(2, "账户已存在");
        }
        int result = userInfoMapper.updateInfo(account, password, adminId);
        if (result > 0) {
            return ServerResponse.createBySuccess();
        } else {
            return ServerResponse.createByError();
        }
    }

    /**
     * 后台登录
     *
     * @param account
     * @param password
     * @return
     */
    public ServerResponse loginAdminInfo(String account, String password) {
        Map<String, Object> map = userInfoMapper.loginAdminInfo(account, password);
        if (map != null && map.size() > 0) {
            return ServerResponse.createBySuccess(map);
        } else {
            return ServerResponse.createByError();
        }
    }

    /**
     * 充值
     *
     * @param userId
     * @param money
     * @param request
     * @return
     * @throws IOException
     */
    public ServerResponse wxRecharge(Integer userId, BigDecimal money, Integer type, HttpServletRequest request, Integer mold) throws IOException {
        UserOrder userOrder = getOrder(userId, money, type, mold);
        ServerResponse response = wxUtils.appPay(userOrder, request);
        //预支付成功成功
        if (response.getStatus() == 1) {
            Object result = response.getData();
            JSONObject jsonObject = JSONUtil.parseObj(result);
            String prepayid = jsonObject.get("prepayid").toString();
            userOrder.setPrepayid(prepayid);
            userOrder.setCommitTime(new Date());
            userOrderMapper.insertSelective(userOrder);
        }
        return response;
    }

    /**
     * 支付宝充值
     *
     * @param userId
     * @param money
     * @return
     */
    public ServerResponse zfbRecharge(Integer userId, BigDecimal money, Integer type, Integer mold) {
        UserOrder userOrder = getOrder(userId, money, type, mold);
        ServerResponse response = alipayUtils.alipay(userOrder);
        if (response.getStatus() == 1) {
            userOrder.setCommitTime(new Date());
            userOrderMapper.insertSelective(userOrder);
        }
        return response;
    }

    /**
     * 组装用户订单信息
     *
     * @param userId
     * @param money
     * @param type
     * @return
     */
    private UserOrder getOrder(Integer userId, BigDecimal money, Integer type, Integer mold) {
        UserOrder userOrder = new UserOrder();
        userOrder.setUserId(userId);
        userOrder.setOrderType(type);
        if (type == 1) {
            userOrder.setOrderMold(mold);
            userOrder.setOrderDesc("小蜜蜂-会员充值");
        } else {
            userOrder.setOrderDesc("小蜜蜂-充值");
        }
        userOrder.setOrderNum(RandomUtil.getTimestamp() + RandomUtil.randomStr(3));
        userOrder.setMoney(money);
        return userOrder;
    }

    /**
     * 更新用户头像
     *
     * @param headimgurl
     * @param userId
     * @return
     */
    public ServerResponse headimgurl(String headimgurl, Integer userId) {
        UserInfo userInfo = new UserInfo();
        userInfo.setUserId(userId);
        userInfo.setHeadimgurl(headimgurl);
        int result = userInfoMapper.updateByPrimaryKeySelective(userInfo);
        if (result > 0) {
            return ServerResponse.createBySuccess();
        } else {
            return ServerResponse.createByError();
        }
    }

    /**
     * 查询客服列表
     * @return
     */
    public ServerResponse findCustomer(){
        return ServerResponse.createBySuccess(userInfoMapper.findCustomer());
    }

    public ServerResponse updateMoney(UserMoney userMoney) {
        int result = userMoneyMapper.updateMoney(userMoney);
        if (result > 0) {
            //插入明细
            UserMoneyDetails userMoneyDetails = new UserMoneyDetails();
            userMoneyDetails.setUserId(userMoney.getUserId());
            if (userMoney.getBalance() != null) {
                userMoneyDetails.setType(2);
                userMoneyDetails.setMoney(userMoney.getBalance());
                userMoneyDetails.setIntroduce("后台手动修改账户余额");
                userMoneyDetails.setTradeTime(new Date());
                userMoneyMapper.insertMoneyDetails(userMoneyDetails);
            }
            if (userMoney.getRepaidBalance() != null) {
                userMoneyDetails.setType(3);
                userMoneyDetails.setMoney(userMoney.getRepaidBalance());
                userMoneyDetails.setIntroduce("后台手动修改账户余额");
                userMoneyDetails.setTradeTime(new Date());
                userMoneyMapper.insertMoneyDetails(userMoneyDetails);
            }
            if (userMoney.getBond() != null) {
                userMoneyDetails.setType(4);
                userMoneyDetails.setMoney(userMoney.getBond());
                userMoneyDetails.setIntroduce("后台手动修改账户余额");
                userMoneyDetails.setTradeTime(new Date());
                userMoneyMapper.insertMoneyDetails(userMoneyDetails);
            }
            return ServerResponse.createBySuccess();
        } else {
            return ServerResponse.createByError();
        }
    }

    /**
     * 查询所有账号
     *
     * @return
     */
    public ServerResponse selectAll() {
        return ServerResponse.createBySuccess(userInfoMapper.selectAll());
    }

    /**
     * 插入账户信息
     *
     * @param account
     * @param password
     * @return
     */
    public ServerResponse insertInfo(String account, String password, String phone) {
        int res = userInfoMapper.selectInfo(account, -1);
        UserInfo userInfo = userInfoMapper.findByPhone(phone);
        if (res > 0) {
            return ServerResponse.createByErrorCodeMessage(2, "账户已存在");
        }
        int result = 0;
        if (userInfo != null) {
            return ServerResponse.createByErrorCodeMessage(2, "账户已存在");
        } else {
//            UserInfo user = new UserInfo();
//            user.setPhone(phone);
//            user.setUID(getUid());
//            user.setPassword(MD5Util.md5EncodeUtf8(password));
//            user.setIsAdmin(1);
//            userInfoMapper.insertSelective(user);
//            //注册时插入用户账户信息
//            userMoneyMapper.insertMoney(user.getUserId());
            result = userInfoMapper.insertInfo(account, password);
        }
        if (result > 0) {
            return ServerResponse.createBySuccess();
        } else {
            return ServerResponse.createByError();
        }
    }

    /**
     * 删除用户账号
     *
     * @param adminId
     * @return
     */
    public ServerResponse deleteInfo(Integer adminId) {
        int result = userInfoMapper.deleteInfo(adminId);
        if (result > 0) {
            return ServerResponse.createBySuccess();
        } else {
            return ServerResponse.createByError();
        }
    }

    /**
     * 发送短信验证码
     *
     * @param phone 手机号
     * @return false 失败，true 成功
     */
    private boolean sendCode(String phone, String code) {
        boolean res = false;
        String[] params = {code};
        SmsSingleSender ssender = new SmsSingleSender(appId, appKey);
        try {
            SmsSingleSenderResult result = ssender.sendWithParam("86", phone,
                    templateId, params, smsSign, "", "");
            System.out.println(result);
            res = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return res;
    }

    /**
     * 查询用户足迹信息
     *
     * @param pageNo
     * @param pageSize
     * @param account
     * @param phone
     * @param nickName
     * @return
     */
    public ServerResponse selectFoot(Integer pageNo, Integer pageSize, String account, String phone, String nickName) {
        Page<AdminFoot> page = PageHelper.startPage(pageNo, pageSize);
        userInfoMapper.selectFoot(account, phone, nickName);
        return ServerResponse.createBySuccess(PageVO.build(page));
    }

    /**
     * 产生验证码
     *
     * @return 6位数验证码
     */
    private static String getCode() {
        StringBuilder str = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < 6; i++) {
            str.append(random.nextInt(10));
        }
        return str.toString();
    }

    /**
     * 产生uid
     *
     * @return 8位数UID
     */
    private String getUid() {
        StringBuilder str = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < 8; i++) {
            str.append(random.nextInt(10));
        }
        if (userInfoMapper.UidIsExist(str.toString()) > 0) {
            return getUid();
        } else {
            return str.toString();
        }
    }
}
