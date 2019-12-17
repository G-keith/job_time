package com.job.service;

import ch.qos.logback.core.joran.event.SaxEventRecorder;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.job.common.page.PageVO;
import com.job.common.statuscode.ServerResponse;
import com.job.entity.JobType;
import com.job.entity.UserChat;
import com.job.entity.vo.UserChatRecordVo;
import com.job.entity.vo.UserChatVo;
import com.job.mapper.UserChatMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

/**
 * @author keith
 * @version 1.0
 * @date 2019/12/9
 */
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class UserChatService {

    private final UserChatMapper userChatMapper;

    public UserChatService(UserChatMapper userChatMapper) {
        this.userChatMapper = userChatMapper;
    }

    /**
     * 插入聊天记录
     * @param userId
     * @param targetId
     * @param newsContent
     * @return
     */
    public ServerResponse insetChatRecord(Integer userId,Integer targetId,String newsContent){
        int result=userChatMapper.insertRecord(userId, targetId, newsContent);
        if(result>0){
            chatList(userId, targetId, newsContent);
            return ServerResponse.createBySuccess();
        }else{
            return ServerResponse.createByError();
        }
    }

    /**
     * 查询聊天记录
     * @param pageNo
     * @param pageSize
     * @param userId
     * @param targetId
     * @return
     */
    public ServerResponse selectChatRecord(Integer pageNo, Integer pageSize,Integer userId,Integer targetId){
        Page<UserChatRecordVo> page = PageHelper.startPage(pageNo, pageSize);
        userChatMapper.selectRecord(userId, targetId);
        updateNewsNum(userId, targetId);
        return ServerResponse.createBySuccess(PageVO.build(page));
    }

    /**
     * 查询消息列表
     * @param pageNo
     * @param pageSize
     * @param userId
     * @return
     */
    public ServerResponse selectNews(Integer pageNo, Integer pageSize,Integer userId){
        Page<UserChatVo> page = PageHelper.startPage(pageNo, pageSize);
        userChatMapper.selectNews(userId);
        return ServerResponse.createBySuccess(PageVO.build(page));
    }

    public void chatList(Integer userId, Integer targetId, String newsContent){
        //插入或者更新聊天列表
        UserChat userChat=userChatMapper.selectByUserId(userId,targetId);
        if(userChat!=null){
            userChat.setNewsContent(newsContent);
            userChat.setNewsTime(new Date());
            userChat.setNewsNum(0);
            userChatMapper.updateByPrimaryKeySelective(userChat);
            //更新接收者列表
            UserChat targetChat=userChatMapper.selectByUserId(targetId,userId);
            targetChat.setNewsContent(newsContent);
            targetChat.setNewsTime(new Date());
            targetChat.setNewsNum(targetChat.getNewsNum()+1);
            userChatMapper.updateByPrimaryKeySelective(targetChat);
        }else{
            UserChat user=new UserChat();
            user.setUserId(userId);
            user.setTargetId(targetId);
            user.setNewsTime(new Date());
            user.setNewsNum(0);
            user.setNewsContent(newsContent);
            userChatMapper.insertSelective(user);
            //插入接收者列表
            UserChat target=new UserChat();
            target.setUserId(targetId);
            target.setTargetId(userId);
            target.setNewsTime(new Date());
            target.setNewsNum(1);
            target.setNewsContent(newsContent);
            userChatMapper.insertSelective(target);
        }
    }

    private void updateNewsNum(Integer userId,Integer targetId){
        UserChat userChat=userChatMapper.selectByUserId(userId,targetId);
        if(userChat!=null){
            userChat.setNewsNum(0);
            userChatMapper.updateByPrimaryKeySelective(userChat);
        }
    }
}
