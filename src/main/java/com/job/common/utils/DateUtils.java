package com.job.common.utils;

import cn.hutool.core.date.DateUtil;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * @author keith
 * @version 1.0
 * @date 2019/11/5
 */
public class DateUtils {

    /**
     * 指定日期 加days 天/月/年
     * @param curDate 日期
     * @param days 添加的天数、月数、年数，如果为负数，则表示获取指定日期之前的日期
     * @param addType 1：天   2：月   3：年 4:小时
     * @return String 日期格式的字符创
     */
    public static Date getDate_add(Date curDate, int days, int addType) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(curDate);
        if(addType == 3) {
            //年份添加days
            calendar.add(Calendar.YEAR, days);
        } else if(addType == 2) {
            //月份添加days
            calendar.add(Calendar.MONTH, days);
        } else if(addType == 1) {
            //日 添加days
            calendar.add(Calendar.DAY_OF_MONTH, days);
        } else if(addType == 4){
            //小时
            calendar.add(Calendar.HOUR_OF_DAY, days);
        }
        return calendar.getTime();
    }

    /**
     * 时间字符串转时间
     * @param time
     * @return
     */
    public static Date stringToDate(String time){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        try {
            return sdf.parse(time);
        } catch (ParseException e) {
            e.printStackTrace();
             throw new RuntimeException();
        }
    }

    /**
     * 获取指定格式的日期
     * @return String 指定格式的当前日期
     * @return String 日期格式的字符串或者星期几
     */
    public static String getDate(Date date) {
        return DateUtil.format(date,"yyyy-MM-dd HH:mm:ss");
    }
}
