package org.h819.commons;


import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;

/**
 * @author H819
 * @version V1.0
 * @Description: TODO(时间、日期工具类。由于 date 在 java1.8变化较大，在本工具类中，仍然然沿用早期版本的 date 类，仅实现几个常用方法。)
 * @Description: TODO(日后 java 1.8 应用规模大起来之后，再修改本类。)
 * @Description: TODO(日期格式化，用 DateFormatUtils 类)
 * @Title: DateUtils.java
 * @date 2014-8-9
 */

 //http://blog.progs.be/542/date-to-java-time

//日期格式化 http://www.ocpsoft.org/prettytime/
@Deprecated
public class MyDateUtils  {

    // ====== 正则表达式(为了字符串合并的时候不产生歧义，每个子字符串都用括号()括起来)=====
    /* 判断是否是正确的年份,必须以 19,20 开头，并且是四位 */
    public static String yearPatternReg = "((19|20)[0-9]{2})";
    /* 判断日期格式 yyyy-MM-dd */
    public static String datePatternReg = "((19|20)[0-9]{2}-(0[1-9]|1[012])-(0[1-9]|[12][0-9]|3[01]))";
    /* 判断时间格式 HH:mm:dd */
    public static String time24PatternReg = "((0[0-9]|1[0-9]|2[0-4]):(0[0-9]|[1-5][0-9]|60):(0[0-9]|[1-5][0-9]|60))";
    /* 判断日期格式 yyyy-MM-dd HH:mm:dd */
    public static String dateTime24PatternReg = datePatternReg + " " + time24PatternReg;
    // ===== 字符串 =====
    // year
    public static String yearPattern = "yyyy";
    // date
    // public static String datePattern = "yyyy-MM-dd";
    // date
    public static String datePattern = "yyyy-MM-dd";
    // 24 小时制
    public static String timePattern24 = "HH:mm:ss";
    // 24 小时制
    public static String dateTime24Pattern = datePattern + " " + timePattern24;
    // 12 小时制
    public static String timePattern12 = "hh:mm:ss";
    // 12 小时制
    public static String dateTime12Pattern = datePattern + " " + timePattern12;
    private static final Logger logger = LoggerFactory.getLogger(MyDateUtils.class);


    /**
     * 通过静态方法调用
     */
    public MyDateUtils() {

    }


    /**
     * @param args
     */
    public static void main(String[] args) {
        // TODO Auto-generated method stub

//        MyDateUtils.examples();
//
//        System.out.println(MyDateUtils.getCurrentDate(MyDateUtils.dateTime24Pattern).toString());
//        System.out.println(MyDateUtils.getCurrentDateString(MyDateUtils.dateTime24Pattern));
//        System.out.println(new java.sql.Date(MyDateUtils.getCurrentDate(MyDateUtils.dateTime24Pattern).getTime()));

       // System.out.println(MyDateUtils.getToday(""));

    }



    /**
     * 获取当前日期
     *
     * @param parsePatterns 日期格式
     * @return 当前日期字符串
     */

    public static Date getCurrentDate(String parsePatterns) {

       // return LocalDate.now();


        try {

            return DateUtils.parseDateStrictly(getCurrentDateString(parsePatterns), parsePatterns);

        } catch (ParseException e) {

            return null;
        }
    }





    /**
     * 获取当前日期字符串
     *
     * @param parsePatterns 日期格式
     * @return 当前日期字符串
     */

    public static String getCurrentDateString(String parsePatterns) {
        return DateFormatUtils.format(new Date(), parsePatterns);
    }

    /**
     * java.util.Date 转换为 java.sql.date
     *
     * @return Date
     */
    public static java.sql.Date convertJavaDate2SqlDate(java.util.Date date) {

        return new java.sql.Date(date.getTime());

    }


    /**
     * 判断给定日期字符串格式是否满足正则表达式
     *
     * @param dateStr    给的的日期字符串
     * @param patternReg 该字符串必须为正则表达式
     * @return
     */
    public static boolean isDatePatternByReg(String dateStr, String patternReg) {

        return dateStr.matches(patternReg);

    }

    /**
     * 补足不完整的两位的年号
     *
     * @param year 给定的年代，缺少 19 或者 20
     * @return 补充完整的四位年代号
     */
    public static String fillYear2To4(String year) {
        String realYear = year.trim();
        // 年为非数字
        if (!StringUtils.isNumeric(realYear))
            return null;
        // log.info("year :" + year);
        if (realYear.length() == 2) {// 补足两位年代
            try {
                int yearCode = Integer.valueOf(realYear);
                // log.info("yearCode :" + yearCode);
                // === 00 - 30 默认为 20 开头
                if (0 < yearCode && yearCode < 10)
                    // 如果年号为07,那么转换为整数的时候，会去掉 0,故加上此句
                    realYear = "200" + Integer.toString(yearCode);
                else if (10 <= yearCode && yearCode <= 30) // 判断到 2030 年足矣
                    realYear = "20" + Integer.toString(yearCode);
                    // === 30 - 99 默认为 19 开头
                else if (30 < yearCode && yearCode <= 99)
                    realYear = "19" + Integer.toString(yearCode);
                else {
                    // log.info("wrong year :" + yearCode);
                    return null;
                }
            } catch (Exception ex) {// 年号错误
                ex.printStackTrace();
                return null;
            }
        } else if (realYear.length() == 4) {
            if (!realYear.matches(yearPatternReg)) {// 不是正确的四位年号;
                // log.info(Year + " : is not right year");
                return null;
            }
        } else {
            // log.info(Year + " : is not right year");
            return null;
        }
        return realYear;
    }

    /**
     * 判断是否为润年
     *
     * @param year
     * @return
     */
    public static boolean isLeapYear(int year) {
        return (year % 4 == 0 && year % 100 != 0) || (year % 400 == 0);
    }

    /**
     * @param c
     * @return String
     * @Title:getWeekDay
     * @Description: 判断是星期几.
     */
    public static String getWeekDay(Calendar c) {
        if (c == null) {
            return "星期一";
        }
        switch (c.get(Calendar.DAY_OF_WEEK)) {
            case Calendar.MONDAY:
                return "星期一";
            case Calendar.TUESDAY:
                return "星期二";
            case Calendar.WEDNESDAY:
                return "星期三";
            case Calendar.THURSDAY:
                return "星期四";
            case Calendar.FRIDAY:
                return "星期五";
            case Calendar.SATURDAY:
                return "星期六";
            default:
                return "星期日";
        }
    }


    /**
     * 演示各种 api 方法 ，在这里寻找是否有相应的方法
     */
    private static void examples() {

    }

}
