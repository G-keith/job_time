package com.job.mapper;

import com.job.entity.vo.InviteVo;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author keith
 * @version 1.0
 * @date 2019/11/5
 */
@Repository
public interface ExtendMapper {

    /**
     * 查询用户邀请信息
     * @param userId 用户id
     * @return 0失败，1成功
     */
    InviteVo selectInvite(Integer userId);

    /**
     * 查询本月邀请信息
     * @return 邀请信息
     */
    List<InviteVo> countInvite();
}
