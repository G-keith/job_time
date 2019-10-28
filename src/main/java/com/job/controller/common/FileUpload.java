package com.job.controller.common;

import cn.hutool.core.date.DateUtil;
import com.job.common.statuscode.ServerResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 文件上传控制类
 * @author ziv
 * @date 2019-04-08
 */
@Slf4j
@RestController
@RequestMapping(value = "upload")
@Api(tags = {"文件上传接口"})
public class FileUpload {

    @Value("${fileSavePath}")
    private String fileSavePath;

    @Value("${resourcePath}")
    private String resourcePath;


    @PostMapping(value = "singleImg", headers="content-type=multipart/form-data")
    @ApiOperation(value = "单张图片上传接口")
    public ServerResponse singleImg(@ApiParam(value = "file", required = true)MultipartFile file) {
        ServerResponse response;
        try {
            String readPath = saveFile(file);
            response = ServerResponse.createBySuccess(readPath);
        } catch (IOException e) {
            log.error(e.getMessage());
            response = ServerResponse.createByErrorMessage(e.getMessage());
        }
        return response;
    }

    @PostMapping(value = "multipleFile", headers="content-type=multipart/form-data")
    @ApiOperation(value = "多文件上传接口")
    public ServerResponse singleFile(@ApiParam(value = "file", required = true)MultipartFile[] files) {
        ServerResponse response;
        List<String> list=new ArrayList<>();
        try {
            for (MultipartFile file : files) {
                list.add(saveFile(file));
            }
            String fileStr = String.join("|", list);
            response = ServerResponse.createBySuccess(fileStr);
        } catch (IOException e) {
            log.error(e.getMessage());
            response = ServerResponse.createByErrorMessage(e.getMessage());
        }
        return response;
    }

    /**
     * 保存文件
     * @param file 文件
     * @return BasicFile
     * @throws IOException
     */
        private String saveFile(MultipartFile file) throws IOException {
        // 日期文件夹
        String dataPackage = DateUtil.format(new Date(), "yyyy-MM-dd");
        // 文件名称
        String fileOriginName = String.valueOf(System.currentTimeMillis())+"_"+file.getOriginalFilename();
        // 文件类型
        String fileType = fileOriginName.substring(fileOriginName.lastIndexOf(".") + 1);
        // 文件路径
        String filePath =  "/"+ dataPackage + "/" + fileType + "/" + fileOriginName;
        // 服务器文件存储路径
        String savePath = fileSavePath + filePath;
        // 文件访问路径
        String readPath = resourcePath + filePath;
        File destFile = new File(savePath);
        // 创建目录
        destFile.getParentFile().mkdirs();
        // 保存文件
        file.transferTo(destFile);
        return readPath;
    }
}
