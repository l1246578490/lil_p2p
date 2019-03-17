package com.dj.p2p.utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateUtil {


    /**
     * 根据时间往后加N小时
     * @return
     */
    public static Date addDateHour(Date date, Integer amount){
        Calendar rightNow = Calendar.getInstance();
        rightNow.setTime(date);
        rightNow.add(Calendar.HOUR,amount);
        return rightNow.getTime();
    }

    /**
     * 根据当前时间往后加N小时
     * @return
     */
    public static Date addDateHour(Integer amount){
        Calendar rightNow = Calendar.getInstance();
        rightNow.setTime(new Date());
        rightNow.add(Calendar.HOUR_OF_DAY,amount);
        return rightNow.getTime();
    }


    /**
     * 根据时间往后加N天
     * @return
     */
    public static Date addDateDay(Date date, Integer amount){
        Calendar rightNow = Calendar.getInstance();
        rightNow.setTime(date);
        rightNow.add(Calendar.DAY_OF_YEAR,amount);
        return rightNow.getTime();
    }

    /**
     * 根据时间往后加N月
     * @return
     */
    public static Date addDateMonth(Date date, Integer month){
        Calendar rightNow = Calendar.getInstance();
        rightNow.setTime(date);
        rightNow.add(Calendar.MONTH,month);
        return rightNow.getTime();
    }


    /**
     * 根据当前时间往后加N天
     * @return
     */
    public static Date addDateDay(Integer amount){
        Calendar rightNow = Calendar.getInstance();
        rightNow.setTime(new Date());
        rightNow.add(Calendar.DAY_OF_YEAR,amount);
        return rightNow.getTime();
    }
    /**
     * 根据当前时间往后加N分钟
     * @return
     */
    public static Date addDateMINUTE(Integer amount){

        Calendar rightNow = Calendar.getInstance();
        rightNow.setTime(new Date());
        rightNow.add(Calendar.MINUTE,amount);
        return rightNow.getTime();
    }

    /**
     * 根据时间往后加N天
     * @return
     */
    public static Date addDateMINUTE(Date date, Integer amount){
        if(null == date){
            return null;
        }
        Calendar rightNow = Calendar.getInstance();
        rightNow.setTime(date);
        rightNow.add(Calendar.MINUTE,amount);
        return rightNow.getTime();
    }

    /**
     * 比较时间大小  date1>date2-->true 否则 false
     * @param date1
     * @param date2
     * @return
     */
    public static Boolean compare(Date date1, Date date2){
        if(null == date1 && null == date2){
            return null;
        }
        if(date1.getTime() >= date2.getTime()){
            return true;
        }
        return false;
    }

    /**
     * 获取明天0点毫秒值
     * @return
     */
    public static long getTomorrowZeroSeconds() {
        long current = System.currentTimeMillis();// 当前时间毫秒数
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_MONTH, 1);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        long tomorrowzero = calendar.getTimeInMillis();
        long tomorrowzeroSeconds = (tomorrowzero - current) / 1000;
        return tomorrowzeroSeconds;
    }
}
