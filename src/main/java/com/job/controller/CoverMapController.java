package com.job.controller;

import com.job.common.statuscode.ServerResponse;
import com.job.service.CoverMapService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author keith
 * @version 1.0
 * @date 2019/11/8
 */
@Slf4j
@RestController
@Api(tags = "后台轮播图接口")
@RequestMapping(value = "/coverMap")
public class CoverMapController {

    @Autowired
    private CoverMapService coverMapService;

    @GetMapping("/all")
    @ApiOperation(value = "查询所有轮播图")
    public ServerResponse findAll(){
        return coverMapService.findCoverMap();
    }

    @PostMapping
    @ApiOperation(value = "插入轮播图")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "img", value = "图片地址", dataType = "string",required = true),
            @ApiImplicitParam(name = "details", value = "图片介绍", dataType = "string",required = true),
    })
    public ServerResponse insertCoverMap(String img,String details){
        return coverMapService.insertCoverMap(img, details);
    }

    @DeleteMapping
    @ApiOperation(value = "删除轮播图")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "imageId", value = "主键id", dataType = "int",required = true),
    })
    public ServerResponse deleteCoverMap(Integer imageId){
        return coverMapService.deleteCoverMap(imageId);
    }

    @PutMapping
    @ApiOperation(value = "更新轮播图")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "imageId", value = "主键id", dataType = "int",required = true),
            @ApiImplicitParam(name = "img", value = "图片地址", dataType = "string",required = true),
            @ApiImplicitParam(name = "details", value = "图片介绍", dataType = "string",required = true),
    })
    public ServerResponse updateCoverMap(Integer imageId,String img,String details){
        return coverMapService.updateCoverMap(imageId, img, details);
    }
}
