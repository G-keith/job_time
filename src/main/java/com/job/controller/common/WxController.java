package com.job.controller.common;

import com.job.common.utils.WxUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author keith
 * @version 1.0
 * @date 2019/11/18
 */
@RestController
@RequestMapping(value = "/wx/notify")
@Api(tags = {"微信支付回调地址"})
public class WxController {

    @Autowired
    private WxUtils wxUtils;

    @PostMapping
    @ApiOperation(value = "微信支付回调通知")
    public void appNotify(HttpServletRequest request, HttpServletResponse response) throws IOException {
        System.out.println("回调接口controller");
       wxUtils.notify(request, response);
    }
}
