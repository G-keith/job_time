package com.job.service;

import com.job.common.statuscode.ServerResponse;
import com.job.entity.vo.InviteVo;
import com.job.mapper.ExtendMapper;
import com.job.mapper.HomePageMapper;
import com.job.mapper.UserMoneyMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author keith
 * @version 1.0
 * @date 2019/11/5
 */
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class ExtendService {

    private final ExtendMapper extendMapper;

    private final HomePageMapper homePageMapper;

    private final UserMoneyMapper userMoneyMapper;

    public ExtendService(ExtendMapper extendMapper, HomePageMapper homePageMapper, UserMoneyMapper userMoneyMapper) {
        this.extendMapper = extendMapper;
        this.homePageMapper = homePageMapper;
        this.userMoneyMapper = userMoneyMapper;
    }

    /**
     * 查询用户邀请信息
     * @param userId 用户id
     * @return 用户邀请信息
     */
    public ServerResponse selectUser(Integer userId){
        InviteVo inviteVo=extendMapper.selectInvite(userId);
        if(inviteVo!=null){
            inviteVo.setTotalMoney(userMoneyMapper.countInvite(userId));
        }
        return ServerResponse.createBySuccess(inviteVo);
    }

    /**
     * 统计每个月邀请排行榜
     * @return  每个月邀请排行榜
     */
    public ServerResponse countInvite(){
        List<InviteVo> inviteVoList=extendMapper.countInvite();
        inviteVoList.forEach(e-> e.setTotalMoney(userMoneyMapper.countInvite(e.getUserId())));
        return ServerResponse.createBySuccess(inviteVoList);
    }
}
