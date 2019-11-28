package com.job.controller.common;

import com.alipay.api.AlipayApiException;
import com.job.common.utils.AlipayUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * @author keith
 * @version 1.0
 * @date 2019/11/18
 */
@RestController
@RequestMapping(value = "/zfb/notify")
@Api(tags = {"支付宝支付回调地址"})
public class ZfbController {

    private final AlipayUtils alipayUtils;

    public ZfbController(AlipayUtils alipayUtils) {
        this.alipayUtils = alipayUtils;
    }

    @GetMapping
    @ApiOperation(value = "支付宝支付回调通知")
    public void appNotify(HttpServletRequest request) throws IOException, AlipayApiException {
        alipayUtils.notify(request);
    }
}
