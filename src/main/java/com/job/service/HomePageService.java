package com.job.service;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.job.common.page.PageVO;
import com.job.common.statuscode.ServerResponse;
import com.job.entity.UserMoney;
import com.job.entity.vo.JobDto;
import com.job.entity.vo.JobVo;
import com.job.mapper.HomePageMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author keith
 * @version 1.0
 * @date 2019/11/4
 */
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class HomePageService {

    @Autowired
    private HomePageMapper homePageMapper;

    /**
     * 查询所有首页轮播图
     * @return 首页轮播图
     */
    public ServerResponse findAll(){
        return ServerResponse.createBySuccess(homePageMapper.findAll());
    }

    /**
     * 查询推荐任务列表信息
     * @param pageNo     第几页
     * @param pageSize   每页几条
     * @return  任务推荐任务列表信息
     */
    public ServerResponse findRecommend(Integer pageNo,Integer pageSize){
        Page<JobVo> page = PageHelper.startPage(pageNo, pageSize);
        homePageMapper.findRecommend();
        return ServerResponse.createBySuccess(PageVO.build(page));
    }

    /**
     * 用户当天是否签到过
     * @param userId 用户id
     * @return 1没有，2有
     */
    public ServerResponse isSignIn(Integer userId){
        int result=homePageMapper.isSignIn(userId);
        if(result==1){
            return ServerResponse.createBySuccess("已经签到过",1);
        }else{
            return ServerResponse.createBySuccess("没有签到过",2);
        }
    }

    /**
     * 插入用户签到信息
     * @param userId 用户id
     * @return 0成功，1失败
     */
    public ServerResponse insertSignIn(Integer userId){
        int result=homePageMapper.insertSignIn(userId);
        if(result>0){
            //增加账户余额
            UserMoney userMoney=homePageMapper.selectByUserId(userId);
            if(userMoney!=null){
                if(userMoney.getBalance()!=null){
                    userMoney.setBalance(userMoney.getBalance().add(homePageMapper.selectSignInMoney().get("money")));
                }else{
                    userMoney.setBalance(homePageMapper.selectSignInMoney().get("money"));
                }
            }else{
                UserMoney money=new UserMoney();
                money.setUserId(userId);
                money.setBalance(homePageMapper.selectSignInMoney().get("money"));
                homePageMapper.insertUserMoney(money);
            }
            return ServerResponse.createBySuccess();
        }else{
            return ServerResponse.createByError();
        }
    }
}
