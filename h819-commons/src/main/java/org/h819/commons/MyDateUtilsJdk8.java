package org.h819.commons;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoUnit;
import java.util.Date;

/**
 * 概念
 */

// java.time 中的类，是线程安全的，并且可以作为静态方法使用
//
// 瞬时时间（Instant）
// 持续时间（duration）
// 日期（date）,时间（time）
// 时区（time-zone）
// 时间段（Period）

/**
 * 关键类
 */

//        Instant : 它代表的是时间戳
//        LocalDate : 不包含具体时间的日期，比如2014-01-14。它可以用来存储生日，周年纪念日，入职日期等。
//        LocalTime : 它代表的是不含日期的时间
//        LocalDateTime : 它包含了日期及时间，不过还是没有偏移信息或者说时区。
//        ZonedDateTime : 这是一个包含时区的完整的日期时间，偏移量是以UTC/格林威治时间为基准的。
//        MonthDay : 这个类由月日组合，不包含年信息
//https://github.com/JeffLi1993/java-core-learning-example/blob/master/src/main/java/org/javacore/time/TimeUtil.java
public class MyDateUtilsJdk8 {


    /**
     * 获取默认时间格式: yyyy-MM-dd HH:mm:ss
     */
    private static final DateTimeFormatter Default_DateTime_Formatter = DateFormat.DateTime_Pattern_With_millisecond_Line.formatter;

    private MyDateUtilsJdk8() {
        // no construct function
    }

    /**
     * @param args
     */
    public static void main(String[] args) {
        // TODO Auto-generated method stub

        //测试
//        System.out.println(DateUtilsJdk8.getLocalDate(new Date()));
//        System.out.println(DateUtilsJdk8.getLocalTime(new Date()));
//        System.out.println(DateUtilsJdk8.asLocalDateTime(new Date()));


    }

    /**
     * 转换 java.util.Date -> java.time.LocalDate ，用于老系统向新系统转换，新系统中，不用 Date
     *
     * @param date
     * @return
     */
    public static LocalDate asLocalDate(Date date) {

        return asLocalDateTime(date).toLocalDate();

    }

    public static Date asDate(LocalDate date) {
        Instant instant = date.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant();
        return Date.from(instant);

    }

    /**
     * 转换 java.util.Date -> java.time.LocalTime ，用于老系统向新系统转换，新系统中，不用 Date
     *
     * @param date
     * @return
     */
    public static LocalTime asLocalTime(Date date) {

        return asLocalDateTime(date).toLocalTime();

    }

    /**
     * 转换 java.util.Date -> java.time.LocalDateTime ，用于老系统向新系统转换，新系统中，不用 Date
     *
     * @param date
     * @return
     */
    public static LocalDateTime asLocalDateTime(Date date) {

        return LocalDateTime.ofInstant(date.toInstant(), ZoneId.systemDefault());

    }

    public static Date asDate(LocalDateTime date) {
        Instant instant = date.atZone(ZoneId.systemDefault()).toInstant();
        return Date.from(instant);
    }

    /**
     * @param date
     * @return
     */
    public static Date astDate(LocalTime date) {

        //  LocalTime actually can't be converted to a Date, because it only contains the time partition of DateTime.
        // Like 11:00. But no day is known. You have to supply it manually:
        // 获取当前日的的事件
        Instant instant = date.atDate(LocalDate.of(LocalDate.now().getYear(), LocalDate.now().getMonth(), LocalDate.now().getDayOfMonth())).atZone(ZoneId.systemDefault()).toInstant();
        return Date.from(instant);

    }

    /**
     * 根据毫秒创建 LocalDateTime
     *
     * @param milliseconds
     * @return
     */
    public static LocalDateTime asLocalDateTime(long milliseconds) {
        return LocalDateTime.ofInstant(Instant.ofEpochMilli(milliseconds), ZoneId.systemDefault());
    }


    /**
     * @param localDateStr
     * @return
     */
    public static LocalDate parseLocalDate(String localDateStr, DateFormat format) {
        return LocalDate.parse(localDateStr, format.formatter);
    }

    public static LocalTime parseLocalTime(String localTimeStr, DateFormat format) {
        return LocalTime.parse(localTimeStr, format.formatter);
    }


    /**
     * String 转时间
     *
     * @param localDateTimeStr
     * @param format           时间格式
     * @return
     */
    public static LocalDateTime parseLocalDateTime(String localDateTimeStr, DateFormat format) {
        return LocalDateTime.parse(localDateTimeStr, format.formatter);
    }


    public static String parseLocalDate(LocalDate localDate, DateFormat format) {
        return format.formatter.format(localDate);
    }

    /**
     * 时间转 String
     *
     * @param localTime
     * @return
     */
    public static String parseLocalTime(LocalTime localTime, DateFormat format) {
        return format.formatter.format(localTime);
    }

    /**
     * 时间转 String
     *
     * @param localDateTime
     * @param format        时间格式
     * @return
     */
    public static String parseLocalDateTime(LocalDateTime localDateTime, DateFormat format) {
        return format.formatter.format(localDateTime);
    }


    public static String getCurrentLocalDate(DateFormat format) {
        return format.formatter.format(LocalDate.now());
    }

    public static String getCurrentLocaltime(DateFormat format) {
        return format.formatter.format(LocalTime.now());
    }

    /**
     * 获取当前时间
     *
     * @param format 时间格式
     * @return
     */
    public static String getCurrentLocalDatetime(DateFormat format) {
        return format.formatter.format(LocalDateTime.now());
    }

    public static LocalDateTime getCurrentLocalDatetime() {
        return LocalDateTime.now();
    }

    public static LocalDate getCurrentLocalDate() {
        return LocalDate.now();
    }

    public static LocalTime getCurrentLocalTime() {
        return LocalTime.now();
    }


    /**
     * 演示用法
     */
    private void examples() {

        /**
         *Java 8中日期与时间API的几个关键点
         */

//        看完了这些例子后，我相信你已经对Java 8这套新的时间日期API有了一定的了解了。现在我们来回顾下关于这个新的API的一些关键的要素。
//
//        它提供了javax.time.ZoneId用来处理时区。

//        它提供了LocalDate与LocalTime类
//        Java 8中新的时间与日期API中的所有类都是不可变且线程安全的，这与之前的Date与Calendar API中的恰好相反，那里面像java.util.Date以及SimpleDateFormat这些关键的类都不是线程安全的。
//        新的时间与日期API中很重要的一点是它定义清楚了基本的时间与日期的概念，比方说，瞬时时间，持续时间，日期，时间，时区以及时间段。它们都是基于ISO日历体系的。
//
//        每个Java开发人员都应该至少了解这套新的API中的这五个类：
//        Instant 它代表的是时间戳，比如2014-01-14T02:20:13.592Z，这可以从java.time.Clock类中获取，像这样： Instant current = Clock.system(ZoneId.of(“Asia/Tokyo”)).instant();
//        LocalDate 它表示的是不带时间的日期，比如2014-01-14。它可以用来存储生日，周年纪念日，入职日期等。
//        LocalTime – 它表示的是不带日期的时间
//        LocalDateTime – 它包含了时间与日期，不过没有带时区的偏移量
//        ZonedDateTime – 这是一个带时区的完整时间，它根据UTC/格林威治时间来进行时区调整
//        这个库的主包是java.time，里面包含了代表日期，时间，瞬时以及持续时间的类。它有两个子package，一个是java.time.foramt，这个是什么用途就很明显了，还有一个是java.time.temporal，它能从更低层面对各个字段进行访问。
//        时区指的是地球上共享同一标准时间的地区。每个时区都有一个唯一标识符，同时还有一个地区/城市(Asia/Tokyo)的格式以及从格林威治时间开始的一个偏移时间。比如说，东京的偏移时间就是+09:00。
//        OffsetDateTime类实际上包含了LocalDateTime与ZoneOffset。它用来表示一个包含格林威治时间偏移量（+/-小时：分，比如+06:00或者 -08：00）的完整的日期（年月日）及时间（时分秒，纳秒）。
//        DateTimeFormatter类用于在Java中进行日期的格式化与解析。与SimpleDateFormat不同，它是不可变且线程安全的，如果需要的话，可以赋值给一个静态变量。DateTimeFormatter类提供了许多预定义的格式器，你也可以自定义自己想要的格式。当然了，根据约定，它还有一个parse()方法是用于将字符串转换成日期的，如果转换期间出现任何错误，它会抛出DateTimeParseException异常。类似的，DateFormatter类也有一个用于格式化日期的format()方法，它出错的话则会抛出DateTimeException异常。
//        再说一句，“MMM d yyyy”与“MMm dd yyyy”这两个日期格式也略有不同，前者能识别出”Jan 2 2014″与”Jan 14 2014″这两个串，而后者如果传进来的是”Jan 2 2014″则会报错，因为它期望月份处传进来的是两个字符。为了解决这个问题，在天为个位数的情况下，你得在前面补0，比如”Jan 2 2014″应该改为”Jan 02 2014″。
//        关于Java 8这个新的时间日期API就讲到这了。这几个简短的示例 对于理解这套新的API中的一些新增类已经足够了。由于它是基于实际任务来讲解的，因此后面再遇到Java中要对时间与日期进行处理的工作时，就不用再四处寻找了。我们学习了如何创建与修改日期实例。我们还了解了纯日期，日期加时间，日期加时区的区别，知道如何比较两个日期，如何找到某天到指定日期比如说下一个生日，周年纪念日或者保险日还有多少天。我们还学习了如何在Java 8中用线程安全的方式对日期进行解析及格式化，而无需再使用线程本地变量或者第三方库这种取巧的方式。新的API能胜任任何与时间日期相关的任务。

        /**
         *获取当前时间
         */
        LocalDate today = LocalDate.now();
        System.out.println("Today's Local date : " + today);

        int year = today.getYear();
        int month = today.getMonthValue();
        int day = today.getDayOfMonth();
        System.out.printf("Year : %d Month : %d day : %d \t %n", year, month, day);

        /**
         获取指定时间
         */
        LocalDate dateOfBirth = LocalDate.of(2010, 01, 14);
        System.out.println("Your Date of birth is : " + dateOfBirth);

        //LocalDate重写了equals方法来进行日期的比较
        LocalDate date1 = LocalDate.of(2014, 01, 14);
        if (date1.equals(today)) {
            System.out.printf("Today %s and date1 %s are same date %n", today, date1);
        }

        /**
         *  比较
         *
         */

        //仅比较月份和日期是否相等，如判断某日是否相同，每年是循环的
        MonthDay birthday = MonthDay.of(dateOfBirth.getMonth(), dateOfBirth.getDayOfMonth());
        MonthDay currentMonthDay = MonthDay.from(today);
        if (currentMonthDay.equals(birthday)) {
            System.out.println("Many Many happy returns of the day !!");
        } else {
            System.out.println("Sorry, today is not your birthday");
        }

        /**
         *如何增加时间里面的小时数
         */
        //
        LocalTime time = LocalTime.now();
        LocalTime newTime = time.plusHours(2); // adding two hours
        System.out.println("Time after 2 hours : " + newTime);


        // 获取1周后的日期
        LocalDate nextWeek = today.plus(1, ChronoUnit.WEEKS);  //ChronoUnit则用来表示这个时间单位
        System.out.println("Today is : " + today);
        System.out.println("Date after 1 week : " + nextWeek);

        //一年前后的日期
        LocalDate previousYear = today.minus(1, ChronoUnit.YEARS);  // 前
        System.out.println("Date before 1 year : " + previousYear);
        LocalDate nextYear = today.plus(1, ChronoUnit.YEARS);    //后
        System.out.println("Date after 1 year : " + nextYear);

        /**
         *时钟
         */

        //Java 8中自带了一个Clock类，你可以用它来获取某个时区下当前的瞬时时间，日期或者时间。
        // 可以用Clock来替代System.currentTimeInMillis()与 TimeZone.getDefault()方法。
        //对不同时区的日期进行处理的话这是相当方便的，可以根据不同的时区来创建时钟

        //日期和时钟比较
//         private Clock clock; // dependency inject ...
//             
//            public void process(LocalDate eventDate) {
//                 
//                    if(eventDate.isBefore(LocalDate.now(clock)) {
//                            ...
//                        }
//                }
        // Returns the current time based on your system clock and set to UTC.
        Clock clock = Clock.systemUTC();
        System.out.println("Clock : " + clock);

        // Returns time based on system clock zone Clock defaultClock =
        Clock.systemDefaultZone();
        System.out.println("Clock : " + clock);

        /**
         * 判断某个日期是在另一个日期的前面还是后面
         */

        LocalDate tomorrow = LocalDate.of(2014, 1, 15);
        if (tomorrow.isAfter(today)) {
            System.out.println("Tomorrow comes after today");
        }
        LocalDate yesterday = today.minus(1, ChronoUnit.DAYS);
        if (yesterday.isBefore(today)) {
            System.out.println("Yesterday is day before today");
        }


        /**
         * 时区
         */

        // Java 8不仅将日期和时间进行了分离，同时还有时区。
        // 比如ZonId代表的是某个特定的时区，而ZonedDateTime代表的是带时区的时间。
        //可以将本地时间转换成另一个时区中的对应时间
        LocalDateTime localtDateAndTime = LocalDateTime.now();
        ZoneId america = ZoneId.of("America/New_York");
        ZonedDateTime dateAndTimeInNewYork = ZonedDateTime.of(localtDateAndTime, america);
        System.out.println("Current date and time in a particular timezone : " + dateAndTimeInNewYork);

        /**
         * 示例13 如何表示固定的日期，比如信用卡过期时间
         */

        //正如MonthDay表示的是某个重复出现的日子的，YearMonth又是另一个组合，它代表的是像信用卡还款日，定期存款到期日，options到期日这类的日期。
        // 你可以用这个类来找出那个月有多少天，lengthOfMonth()这个方法返回的是这个YearMonth实例有多少天，这对于检查2月到底是28天还是29天可是非常有用的。
        YearMonth currentYearMonth = YearMonth.now();
        System.out.printf("Days in month year %s: %d%n", currentYearMonth, currentYearMonth.lengthOfMonth());
        YearMonth creditCardExpiry = YearMonth.of(2018, Month.FEBRUARY);
        System.out.printf("Your credit card expires on %s %n", creditCardExpiry);

        /**
         * 示例14 如何在Java 8中检查闰年
         */

        if (today.isLeapYear()) {
            System.out.println("This year is Leap year");
        } else {
            System.out.println("2014 is not a Leap year");
        }

        /**
         * 示例15 两个日期之间包含多少天，多少个月
         */
        //还有一个常见的任务就是计算两个给定的日期之间包含多少天，多少周或者多少年。
        // 你可以用java.time.Period类来完成这个功能。在下面这个例子中，我们将计算当前日期与将来的一个日期之前一共隔着几个月。

        LocalDate java8Release = LocalDate.of(2014, Month.MARCH, 14);
        Period periodToNextJavaRelease =
                Period.between(today, java8Release);
        System.out.println("Months left between today and Java 8 release : " + periodToNextJavaRelease.getMonths());


        /**
         * 示例16 带时区偏移量的日期与时间
         */
        //在Java 8里面，你可以用ZoneOffset类来代表某个时区，比如印度是GMT或者UTC5：30，你可以使用它的静态方法ZoneOffset.of()方法来获取对应的时区。
        // 只要获取到了这个偏移量，你就可以拿LocalDateTime和这个偏移量创建出一个OffsetDateTime。
        //还有一点就是，OffSetDateTime主要是给机器来理解的，如果是给人看的，可以使用ZoneDateTime类。
        LocalDateTime datetime = LocalDateTime.of(2014, Month.JANUARY, 14, 19, 30);
        ZoneOffset offset = ZoneOffset.of("+05:30");
        OffsetDateTime date = OffsetDateTime.of(datetime, offset);
        System.out.println("Date and Time with timezone offset in Java : " + date);

        /**
         * 示例17 在Java 8中如何获取当前时间戳
         */
        Instant timestamp = Instant.now();
        System.out.println("What is value of this instant " + timestamp);

        /**
         * 示例18 如何在Java 8中使用预定义的格式器来对日期进行解析/格式化
         */
        //它还自带了一些预定义好的格式器，包含了常用的日期格式。比如说，本例 中我们就用了预定义的BASICISODATE格式，它会将2014年2月14日格式化成20140114。
        String dayAfterTommorrow = "20140116";
        LocalDate formatted = LocalDate.parse(dayAfterTommorrow, DateTimeFormatter.BASIC_ISO_DATE);
        System.out.printf("Date generated from String %s is %s %n", dayAfterTommorrow, formatted);


        /**
         * 示例19 如何在Java中使用自定义的格式器来解析日期
         * 字符串解析为日期
         */
        //变量名称和原来一样
        //y : 年
        //M :月
        //d ：日
        //h : 小时
        //m : 分
        //S : 秒
        String goodFriday = "Apr 18 2014";
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMM dd yyyy");
            LocalDate holiday = LocalDate.parse(goodFriday, formatter);
            System.out.printf("Successfully parsed String %s, date is %s%n", goodFriday, holiday);
        } catch (DateTimeParseException ex) {
            System.out.printf("%s is not parsable!%n", goodFriday);
            ex.printStackTrace();
        }

        /**
         * 示例20 如何在Java 8中对日期进行格式化，转换成字符串
         * 日期，格式化为字符串
         */

        LocalDateTime arrivalDate = LocalDateTime.now();
        try {
            DateTimeFormatter format = DateTimeFormatter.ofPattern("MMM dd yyyy hh:mm a");
            String landing = arrivalDate.format(format);
            System.out.printf("Arriving at : %s %n", landing);
        } catch (DateTimeException ex) {
            System.out.printf("%s can't be formatted!%n", arrivalDate);
            ex.printStackTrace();
        }
    }

    /**
     * 时间格式
     */
    public enum DateFormat {

        /**
         * 日期格式
         */
        Date_Pattern_Line("yyyy-MM-dd"),
        Date_Pattern_Slash("yyyy/MM/dd"),
        Date_Pattern_Double_Slash("yyyy\\MM\\dd"),
        Date_Pattern_None("yyyyMMdd"),

        /**
         * 时间格式
         */
        Time_Pattern_Slash("HH:mm:ss"),
        Time_Pattern_With_millisecond_Double_Slash("HH:mm:ss.SSS"),

        /**
         * 日期时间格式
         */
        DateTime_Pattern_Line("yyyy-MM-dd HH:mm:ss"),
        DateTime_Pattern_Slash("yyyy/MM/dd HH:mm:ss"),
        DateTime_Pattern_Double_Slash("yyyy\\MM\\dd HH:mm:ss"),
        DateTime_Pattern_None("yyyyMMdd HH:mm:ss"),

        /**
         * 日期时间格式 带毫秒
         */
        DateTime_Pattern_With_millisecond_Line("yyyy-MM-dd HH:mm:ss.SSS"),
        DateTime_Pattern_With_millisecond_Slash("yyyy/MM/dd HH:mm:ss.SSS"),
        DateTime_Pattern_With_millisecond_Double_Slash("yyyy\\MM\\dd HH:mm:ss.SSS"),
        DateTime_Pattern_With_millisecond_None("yyyyMMdd HH:mm:ss.SSS");

        private transient DateTimeFormatter formatter;

        DateFormat(String pattern) {
            // formatter = DateTimeFormatter.ofPattern(pattern);
            formatter =
                    new DateTimeFormatterBuilder().appendPattern(pattern)
                            //  .parseDefaulting(ChronoField.NANO_OF_DAY, 0)
                            .toFormatter()
                            .withZone(ZoneId.systemDefault());
        }
    }

}
