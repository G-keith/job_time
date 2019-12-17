package com.job.controller;

import com.job.common.statuscode.ServerResponse;
import com.job.common.utils.AlipayUtils;
import com.job.common.utils.MD5Util;
import com.job.entity.CashOutOrder;
import com.job.entity.UserInfo;
import com.job.entity.UserOrder;
import com.job.service.UserInfoService;
import com.job.task.TimerTask;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.math.BigDecimal;

/**
 * @author keith
 * @version 1.0
 * @date 2019/10/28
 */
@Slf4j
@RestController
@Api(tags = "手机端账号信息接口")
@RequestMapping(value = "/user")
public class UserInfoController {

    private final UserInfoService userInfoService;

    private final AlipayUtils alipayUtils;
    private final TimerTask timerTask;

    public UserInfoController(UserInfoService userInfoService, AlipayUtils alipayUtils, TimerTask timerTask) {
        this.userInfoService = userInfoService;
        this.alipayUtils = alipayUtils;
        this.timerTask = timerTask;
    }

    @GetMapping("/enroll")
    @ApiOperation(value = "手机号密码登录")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "phone", value = "手机号", dataType = "string", required = true),
            @ApiImplicitParam(name = "password", value = "密码", dataType = "string", required = true),
    })
    public ServerResponse signIn(String phone, String password) {
        return userInfoService.signIn(phone, MD5Util.md5EncodeUtf8(password));
    }

    @GetMapping("/wxInfo")
    @ApiOperation(value = "获取微信信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "code", value = "用户换取access_token的code，仅在ErrCode为0时有效", required = true, dataType = "String"),
    })
    public ServerResponse wxInfo(String code) {
        return userInfoService.wxInfo(code);
    }

    @PostMapping("/register")
    @ApiOperation(value = "注册")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "phone", value = "手机号", dataType = "string", required = true),
            @ApiImplicitParam(name = "password", value = "密码", dataType = "string", required = true),
            @ApiImplicitParam(name = "openid", value = "openId", dataType = "string", required = true),
            @ApiImplicitParam(name = "nickname", value = "昵称", dataType = "string", required = true),
            @ApiImplicitParam(name = "sex", value = "性别", dataType ="int", required = true),
            @ApiImplicitParam(name = "province", value = "省份", dataType = "string"),
            @ApiImplicitParam(name = "city", value = "城市", dataType = "string"),
            @ApiImplicitParam(name = "country", value = "国家", dataType = "string"),
            @ApiImplicitParam(name = "headimgurl", value = "头像", dataType = "string", required = true),
            @ApiImplicitParam(name = "upUID", value = "邀请码", dataType = "string"),
    })
    public ServerResponse register(String upUID, String phone, String password, String openid, String nickname, Integer sex, String province, String city, String country, String headimgurl) {
        UserInfo userInfo = new UserInfo();
        userInfo.setPhone(phone);
        userInfo.setPassword(MD5Util.md5EncodeUtf8(password));
        userInfo.setOpenid(openid);
        userInfo.setNickname(nickname);
        userInfo.setSex(sex);
        userInfo.setProvince(province);
        userInfo.setCountry(country);
        userInfo.setHeadimgurl(headimgurl);
        userInfo.setCity(city);
        if (upUID != null) {
            userInfo.setUpUID(upUID);
        }
        return userInfoService.register(userInfo);
    }

    @PostMapping("/webRegister")
    @ApiOperation(value = "网页注册")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "phone", value = "手机号", dataType = "string", required = true),
            @ApiImplicitParam(name = "password", value = "密码", dataType = "string", required = true),
            @ApiImplicitParam(name = "upUID", value = "邀请码", dataType = "string"),
    })
    public ServerResponse webRegister(String upUID, String phone, String password) {
        UserInfo userInfo = new UserInfo();
        userInfo.setPhone(phone);
        userInfo.setPassword(MD5Util.md5EncodeUtf8(password));
        userInfo.setUpUID(upUID);
        return userInfoService.webRegister(userInfo);
    }

    @PostMapping("/bindWx")
    @ApiOperation(value = "绑定微信")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "phone", value = "手机号", dataType = "string", required = true),
            @ApiImplicitParam(name = "password", value = "密码", dataType = "string", required = true),
            @ApiImplicitParam(name = "openid", value = "openId", dataType = "string", required = true),
            @ApiImplicitParam(name = "nickname", value = "昵称", dataType = "string", required = true),
            @ApiImplicitParam(name = "sex", value = "性别", dataType ="int", required = true),
            @ApiImplicitParam(name = "province", value = "省份", dataType = "string"),
            @ApiImplicitParam(name = "city", value = "城市", dataType = "string"),
            @ApiImplicitParam(name = "country", value = "国家", dataType = "string"),
            @ApiImplicitParam(name = "headimgurl", value = "头像", dataType = "string", required = true),
    })
    public ServerResponse bindWx(String phone, String password, String openid, String nickname, Integer sex, String province, String city, String country, String headimgurl){
        UserInfo userInfo = new UserInfo();
        userInfo.setPhone(phone);
        userInfo.setPassword(MD5Util.md5EncodeUtf8(password));
        userInfo.setOpenid(openid);
        userInfo.setNickname(nickname);
        userInfo.setSex(sex);
        userInfo.setCountry(country);
        userInfo.setProvince(province);
        userInfo.setHeadimgurl(headimgurl);
        userInfo.setCity(city);
        return userInfoService.bindWx(userInfo);
    }

    @GetMapping("/isExistPhone")
    @ApiOperation(value = "手机号是否存在")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "phone", value = "手机号", dataType = "string", required = true),
    })
    public ServerResponse isExistPhone(String phone){
        return userInfoService.isExistPhone(phone);
    }

    @PutMapping("/uid")
    @ApiOperation(value = "插入邀请码")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userId", value = "用户id", dataType = "string", required = true),
            @ApiImplicitParam(name = "uid", value = "邀请人uid", dataType = "string", required = true),
    })
    public ServerResponse insertUid(String uid, Integer userId) {
        return userInfoService.insertUid(uid, userId);
    }

    @GetMapping("/weChatLogin")
    @ApiOperation(value = "微信登录")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "code", value = "用户换取access_token的code，仅在ErrCode为0时有效", required = true, dataType = "String"),
    })
    public ServerResponse weChatLogin(String code) throws IOException {
        return userInfoService.weChatLogin(code);
    }

//    @GetMapping("code")
//    @ApiOperation("获取验证码")
//    @ApiImplicitParams({
//            @ApiImplicitParam(name = "phone", value = "手机号", dataType = "string", required = true),
//    })
//    public ServerResponse getCode(String phone) {
//        return userInfoService.insertCode(phone);
//    }

//    @GetMapping("checkCode")
//    @ApiOperation("检验验证码是否正确")
//    @ApiImplicitParams({
//            @ApiImplicitParam(name = "phone", value = "手机号", dataType = "string", required = true),
//            @ApiImplicitParam(name = "code", value = "验证码", dataType = "string", required = true),
//    })
//    public ServerResponse checkCode(String phone, String code) {
//        return userInfoService.checkCode(phone, code);
//    }

    @GetMapping("/black")
    @ApiOperation(value = "获取黑名单用户")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "pageNo", value = "第几页", dataType = "int", defaultValue = "1"),
            @ApiImplicitParam(name = "pageSize", value = "每页几条", dataType = "int", defaultValue = "10"),
            @ApiImplicitParam(name = "phone", value = "手机号", dataType = "string"),
    })
    public ServerResponse findBlacklist(Integer pageNo, Integer pageSize, String phone) {
        return userInfoService.findBlacklist(pageNo, pageSize, phone);
    }

    @GetMapping("/wxRecharge")
    @ApiOperation(value = "获取微信充值预支付")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userId", value = "用户id", dataType = "int", required = true),
            @ApiImplicitParam(name = "money", value = "金额", dataType = "int", required = true),
            @ApiImplicitParam(name = "type", value = "类型（1.代表会员充值，2代表账户充值）", dataType = "int", required = true),
            @ApiImplicitParam(name = "mold", value = "1.周充值，2.月充值，3.季充值，4.年充值", dataType = "int", required = true),
    })
    public ServerResponse wxRecharge(Integer userId, BigDecimal money, Integer type, Integer mold, HttpServletRequest request) throws IOException {
        return userInfoService.wxRecharge(userId, money, type, request, mold);
    }

    @GetMapping("/zfbRecharge")
    @ApiOperation(value = "获取支付宝充值信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userId", value = "用户id", dataType = "int", required = true),
            @ApiImplicitParam(name = "money", value = "金额", dataType = "int", required = true),
            @ApiImplicitParam(name = "type", value = "类型（1.代表会员充值，2代表账户充值）", dataType = "int", required = true),
            @ApiImplicitParam(name = "mold", value = "1.周充值，2.月充值，3.季充值，4.年充值", dataType = "int", required = true),

    })
    public ServerResponse zfbRecharge(Integer userId, BigDecimal money, Integer type, Integer mold) {
        return userInfoService.zfbRecharge(userId, money, type, mold);
    }

    @PutMapping("/headimgurl")
    @ApiOperation(value = "更新头像")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userId", value = "用户id", dataType = "int", required = true),
            @ApiImplicitParam(name = "headimgurl", value = "头像路径", dataType = "string", required = true),
    })
    public ServerResponse headimgurl(String headimgurl, Integer userId) {
        return userInfoService.headimgurl(headimgurl, userId);
    }

    @GetMapping("/customer")
    @ApiOperation(value = "查询客服列表")
    public ServerResponse findCustomer(){
        return userInfoService.findCustomer();
    }

    @GetMapping("/testReport")
    public void testReport(){
        timerTask.reportCommit();
    }
}
