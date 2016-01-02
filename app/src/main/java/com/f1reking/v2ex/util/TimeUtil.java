package com.f1reking.v2ex.util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * 时间工具类
 * Created by F1ReKing on 2016/1/2.
 */
public class TimeUtil {

    public static String format(String pattern,long milliseconds){
        return (new SimpleDateFormat(pattern)).format(new Date(milliseconds));
    }

    /**
     * 将以毫秒为单位的时间转化为“小时：分钟：秒”的格式（不足1小时的没有小时部分）。
     * 适用于显示一段视频的时长（有些视频时长是大于1小时，有些是不足1小时的）
     */
    public static String formatHMS(long millis){
        final long millisOfOneHour = TimeUnit.HOURS.toMillis(1);
        if (millis < millisOfOneHour){
            return String.format("%1$M:%1$tS",millis);
        }else{
            return String.format("%1$d:%2$TM:%2$TS",millis / millisOfOneHour,millis % millisOfOneHour);
        }
    }
}
