package com.job.entity;

import lombok.Data;

import java.util.Date;

/**
 * @author keith
 * @version 1.0
 * @date 2019/12/9
 */
@Data
public class UserChat {
    private Integer newsId;

    private Integer userId;

    private Integer targetId;

    private Integer newsNum;

    private String newsContent;

    private Date newsTime;
}