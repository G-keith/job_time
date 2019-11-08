package com.job.service;

import com.job.common.statuscode.ServerResponse;
import com.job.mapper.CoverMapMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author keith
 * @version 1.0
 * @date 2019/11/7
 */
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class CoverMapService {

    @Autowired
    private CoverMapMapper coverMapMapper;

    /**
     * 查询所有轮播图
     * @return
     */
    public ServerResponse findCoverMap(){
        return ServerResponse.createBySuccess(coverMapMapper.findCoverMap());
    }

    /**
     * 插入轮播图
     * @param img
     * @param details
     * @return
     */
    public ServerResponse insertCoverMap(String img,String details){
        int result=coverMapMapper.insertCoverMap(img, details);
        if(result>0){
            return ServerResponse.createBySuccess();
        }else{
            return ServerResponse.createByError();
        }
    }

    /**
     * 删除轮播图
     * @param imgId
     * @return
     */
    public ServerResponse deleteCoverMap(Integer imgId){
        int result=coverMapMapper.deleteCoverMap(imgId);
        if(result>0){
            return ServerResponse.createBySuccess();
        }else{
            return ServerResponse.createByError();
        }
    }

    /**
     * 更新轮播图
     * @param imgId
     * @param img
     * @param details
     * @return
     */
    public ServerResponse updateCoverMap(Integer imgId,String img,String details){
        int result=coverMapMapper.updateCoverMap(imgId, img, details);
        if(result>0){
            return ServerResponse.createBySuccess();
        }else{
            return ServerResponse.createByError();
        }
    }
}
