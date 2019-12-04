package com.job.mapper;

import org.springframework.stereotype.Repository;

/**
 * @author keith
 * @version 1.0
 * @date 2019/11/28
 */
@Repository
public interface UserAgentMapper {

    /**
     * 插入经纪人信息
     * @param content
     * @return
     */
    int insertAgent(String content);

    /**
     * 删除经纪人信息
     * @return
     */
    int deleteAgent();

    /**
     * 查询经纪人信息
     * @return
     */
    String findAgent();
}
