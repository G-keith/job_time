package com.job.mapper;

import com.job.entity.UserChat;
import com.job.entity.vo.UserChatRecordVo;
import com.job.entity.vo.UserChatVo;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author keith
 * @version 1.0
 * @date 2019/12/9
 */
@Repository
public interface UserChatMapper {
    /**
     * 删除聊天列表
     * @param newsId
     * @return
     */
    int deleteByPrimaryKey(Integer newsId);

    /**
     * 插入聊天列表
     * @param record
     * @return
     */
    int insertSelective(UserChat record);

    /**
     * 更新聊天列表
     * @param record
     * @return
     */
    int updateByPrimaryKeySelective(UserChat record);

    /**
     * 查询列表是否存在
     * @param userId
     * @param targetId
     * @return
     */
    UserChat selectByUserId(@Param("userId")Integer userId,@Param("targetId")Integer targetId);

    /**
     * 插入聊天记录
     * @param userId
     * @param targetId
     * @param newsContent
     * @return
     */
    int insertRecord(@Param("userId")Integer userId,@Param("targetId")Integer targetId,@Param("newsContent") String newsContent);

    /**
     * 查询聊天记录
     * @param userId
     * @param targetId
     * @return
     */
    List<UserChatRecordVo> selectRecord(@Param("userId")Integer userId,@Param("targetId")Integer targetId);

    /**
     * 查询消息列表
     * @param userId 用户id
     * @return
     */
    List<UserChatVo> selectNews(@Param("userId")Integer userId);

}