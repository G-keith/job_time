package com.job.service;

import com.github.qcloudsms.SmsSingleSender;
import com.github.qcloudsms.SmsSingleSenderResult;
import com.job.common.statuscode.ServerResponse;
import com.job.entity.user.UserInfo;
import com.job.mapper.user.UserInfoMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    private final UserInfoMapper userInfoMapper;

    public UserInfoService(UserInfoMapper userInfoMapper) {
        this.userInfoMapper = userInfoMapper;
    }

    /**
     * 根据账号密码查询用户是否存在
     *
     * @param userInfo 账号密码
     * @return 0存在，1不存在
     */
    public ServerResponse signIn(UserInfo userInfo) {
        //查询手机号是否存在
            int result = userInfoMapper.findByPassword(userInfo);
            if (result > 0) {
                return ServerResponse.createBySuccess();
            } else {
                return ServerResponse.createByErrorMessage("账号或者密码错误");
            }
    }

    /**
     * 检验手机号是否存在
     * @param phone 手机号码
     * @return 0不存在，1存在
     */
    public ServerResponse checkPhone(String phone) {
        int result = userInfoMapper.findByPhone(phone);
        if (result > 0) {
            return ServerResponse.createByErrorMessage("手机号已存在");
        } else {
            return ServerResponse.createBySuccess();
        }
    }

    /**
     * 检验昵称是否存在
     * @param nickName 昵称
     * @return 0不存在，不存在
     */
    public ServerResponse checkNickName(String nickName) {
        int result = userInfoMapper.findByNickName(nickName);
        if (result > 0) {
            return ServerResponse.createByErrorMessage("昵称已存在");
        } else {
            return ServerResponse.createBySuccess();
        }
    }

    /**
     * 插入用户信息
     *
     * @param userInfo 用户信息
     * @return 0成功，1失败
     */
    public ServerResponse insertUserInfo(UserInfo userInfo) {
        int result = userInfoMapper.insertSelective(userInfo);
        if (result > 0) {
            //更新验证码为已使用
            userInfoMapper.updateCode(userInfo.getPhone());
            return ServerResponse.createBySuccess();
        } else {
            return ServerResponse.createByErrorMessage("注册失败");
        }
    }

    /**
     * 更新密码
     *
     * @param password 密码
     * @param phone    手机号
     * @return 0失败，1成功
     */
    public ServerResponse updatePassword(String password, String phone) {
        int result = userInfoMapper.updateByPhone(phone, password);
        if (result > 0) {
            return ServerResponse.createBySuccess();
        } else {
            return ServerResponse.createByErrorMessage("注册失败");
        }
    }

    /**
     * 发送验证码并存放到数据库中去
     * @param phone 手机号
     * @return 验证码
     */
    public ServerResponse insertCode(String phone){
        String code= getCode();
        //判断是否发送成功
        int res=userInfoMapper.insertCode(phone, code);
        if(res>0){
            //存放到数据库中去
            boolean result=sendCode(phone,code);
            if(result){
                return ServerResponse.createBySuccess(code);
            }else{
                return ServerResponse.createByErrorMessage("发送失败");
            }
        }else{
            return ServerResponse.createByErrorMessage("发送失败");
        }
    }

    /**
     * 检验验证码是否正确
     * @param phone 手机号
     * @param code 验证码
     * @return 0正确，1不正确
     */
    public ServerResponse checkCode(String phone,String code){
        String result=userInfoMapper.findCodeByPhone(phone);
        if(code.equals(result)){
            return ServerResponse.createBySuccess();
        }else{
            return ServerResponse.createByErrorMessage("验证码不正确");
        }
    }

    /**
     * 发送短信验证码
     *
     * @param phone 手机号
     * @return false 失败，true 成功
     */
    private boolean sendCode(String phone, String code) {
        boolean res=false;
        String[] params = {code};
        SmsSingleSender ssender = new SmsSingleSender(appId, appKey);
        try {
            SmsSingleSenderResult result = ssender.sendWithParam("86", phone,
                    templateId, params, smsSign, "", "");
            System.out.println(result);
            res = true;
        }
        catch (Exception e) {
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
}
