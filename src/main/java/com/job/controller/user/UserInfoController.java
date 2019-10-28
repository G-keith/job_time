package com.job.controller.user;

import com.job.common.utils.MD5Util;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author keith
 * @version 1.0
 * @date 2019/10/28
 */
@Slf4j
@RestController
@Api(tags = "登录，密码修改等接口")
@RequestMapping(value = "user")
public class UserInfoController {

    @GetMapping(value = "password")
    @ApiOperation(value = "生成密码（测试用）")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "password",value = "明文密码",dataType = "string",required = true)
    })
    public String password(String password){
        return MD5Util.md5EncodeUtf8(password);
    }
}
