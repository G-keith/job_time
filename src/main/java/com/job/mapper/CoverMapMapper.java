package com.job.mapper;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * @author keith
 * @version 1.0
 * @date 2019/11/7
 */
@Repository
public interface CoverMapMapper {


    /**
     * 查询所有轮播图
     * @return
     */
    List<Map<String,Object>> findCoverMap();

    /**
     * 插入轮播图
     * @param img
     * @param details
     * @return
     */
    int insertCoverMap(@Param("img") String img,@Param("details") String details);

    /**
     * 删除轮播图
     * @return
     */
    int deleteCoverMap(@Param("imgId") Integer imgId);

    /**
     * 更新轮播图
     * @param img
     * @param details
     * @param imgId
     * @return
     */
    int updateCoverMap(@Param("imgId") Integer imgId,@Param("img") String img,@Param("details") String details);
}
