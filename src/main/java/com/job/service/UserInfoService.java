package com.job.service;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.qcloudsms.SmsSingleSender;
import com.github.qcloudsms.SmsSingleSenderResult;
import com.job.common.page.PageVO;
import com.job.common.statuscode.ServerResponse;
import com.job.common.utils.WxUtils;
import com.job.entity.UserInfo;
import com.job.mapper.UserInfoMapper;
import com.job.mapper.UserMoneyMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
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

    public UserInfoService(UserInfoMapper userInfoMapper, UserMoneyMapper userMoneyMapper, WxUtils wxUtils) {
        this.userInfoMapper = userInfoMapper;
        this.userMoneyMapper = userMoneyMapper;
        this.wxUtils = wxUtils;
    }

    /**
     * 登录
     * @param phone 账号密码
     * @return 0存在，1不存在
     */
    public ServerResponse signIn(String phone,String uid) {
        //查询用户信息
        UserInfo result = userInfoMapper.findByPhone(phone);
        if (result != null) {
            if(result.getStatus()==1){
                return ServerResponse.createByErrorCodeMessage(2,"用户未黑名单，不可登录");
            }else{
                return ServerResponse.createBySuccess(result);
            }
        } else {
            UserInfo userInfo=new UserInfo();
            userInfo.setPhone(phone);
            userInfo.setUID(getUid());
            if (uid != null && !"".equals(uid)) {
                userInfo.setUpUID(uid);
            }
            userInfoMapper.insertPhone(userInfo);
            //注册时插入用户账户信息
            userMoneyMapper.insertMoney(userInfo.getUserId());
            return ServerResponse.createBySuccess(userInfo);
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
        return wxUtils.authorization(code);
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
     * @param pageNo
     * @param pageSize
     * @return
     */
    public ServerResponse findAll(Integer pageNo,Integer pageSize,String phone,Integer status){
        Page<UserInfo> page = PageHelper.startPage(pageNo, pageSize);
        userInfoMapper.findAll(phone,status);
        return ServerResponse.createBySuccess(PageVO.build(page));
    }

    /**
     * 查询所有用户信息
     * @param pageNo
     * @param pageSize
     * @return
     */
    public ServerResponse findBlacklist(Integer pageNo,Integer pageSize,String phone){
        Page<UserInfo> page = PageHelper.startPage(pageNo, pageSize);
        userInfoMapper.findAll(phone,1);
        return ServerResponse.createBySuccess(PageVO.build(page));
    }

    /**
     * 更新用户信息
     * @param userInfo 用户信息
     * @return
     */
    public ServerResponse updateUserInfo(UserInfo userInfo){
        int result=userInfoMapper.updateByPrimaryKeySelective(userInfo);
        if(result>0){
            return ServerResponse.createBySuccess();
        }else{
            return ServerResponse.createByError();
        }
    }

    public ServerResponse openOrRenew(){
        return null;
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
        if(userInfoMapper.UidIsExist(str.toString())>0){
            return getUid();
        }else{
            return str.toString();
        }
    }
}
