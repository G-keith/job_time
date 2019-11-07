package com.job.controller;

import com.job.common.statuscode.ServerResponse;
import com.job.entity.vo.JobDto;
import com.job.service.HomePageService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author keith
 * @version 1.0
 * @date 2019/11/4
 */
@Slf4j
@RestController
@Api(tags = "首页接口")
@RequestMapping(value = "/home")
public class HomePageController {

    private final HomePageService homePageService;

    public HomePageController(HomePageService homePageService) {
        this.homePageService = homePageService;
    }

    @GetMapping("/img")
    @ApiOperation(value = "轮播图查询")
    public ServerResponse findAll(){
        return homePageService.findAll();
    }

    @GetMapping("/recommend")
    @ApiOperation(value = "推荐任务列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "pageNo", value = "第几页", dataType = "int", defaultValue = "1"),
            @ApiImplicitParam(name = "pageSize", value = "每页几条", dataType = "int", defaultValue = "5"),
    })
    public ServerResponse findJob(Integer pageNo,Integer pageSize){
        return homePageService.findRecommend(pageNo, pageSize);
    }

    @GetMapping("/sign_in")
    @ApiOperation(value = "用户当天是否签到过")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userId", value = "用户id", dataType = "int" ,required = true),
    })
    public ServerResponse isSignIn(Integer userId){
        return homePageService.isSignIn(userId);
    }

    @PostMapping("/sign_in")
    @ApiOperation(value = "插入用户签到记录")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userId", value = "用户id", dataType = "int" ,required = true),
    })
    public ServerResponse insertSignIn(Integer userId){
        return homePageService.insertSignIn(userId);
    }
}
