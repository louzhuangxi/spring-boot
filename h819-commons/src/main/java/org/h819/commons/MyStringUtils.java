package org.h819.commons;

import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Description : TODO(扩展 org.apache.commons.lang3.StringUtils 类)
 * User: h819
 * Date: 14-4-24
 * Time: 下午2:00
 * To change this template use File | Settings | File Templates.
 */
public class MyStringUtils {
    // On UNIX systems, it returns "\n"; on Microsoft Windows systems it returns "\r\n".
    public static final String linebreak = System.lineSeparator();

    public static final String SPACE = " ";
    public static final String DOT = ".";
    public static final String SLASH = "/";
    public static final String BACKSLASH = "\\";
    public static final String EMPTY = "";
    public static final String CRLF = "\r\n";
    public static final String NEWLINE = "\n";
    public static final String UNDERLINE = "_";
    public static final String COMMA = ",";

    public static final String HTML_NBSP = "&nbsp;";
    public static final String HTML_AMP = "&amp";
    public static final String HTML_QUOTE = "&quot;";
    public static final String HTML_LT = "&lt;";
    public static final String HTML_GT = "&gt;";
    public static final String EMPTY_JSON = "{}";


    /**
     * 判断中文的正则表达式
     */
    public static final String CHINESE_CHAR_REG_STR = "[\\u4E00-\\u9FA5]+";

    public final static String[] CHINESE_NUMBERS = {"零", "一", "二", "三", "四",
            "五", "六", "七", "八", "九"};
    public final static String[] CHINESE_NUMBER_UNITS = {"", "十", "百", "千",
            "万", "十万", "百万", "千万", "亿"};


    /**
     * 判断一个字符是否包含在ASCII 表中，ASCII 表中字符的整数值在0~127之间。 可以简单的认为，不在 ASCII 表中，认为是汉字。
     *
     * @param value
     * @return
     */
    public static boolean isASCII(char value) {
        return value * 1 <= 127 && value * 1 >= 0 ? true : false;
    }

    /**
     * 判断有几个中文字符
     *
     * @param str
     * @return
     */
    public static int getChineseCount(String str) {
        Pattern p = Pattern.compile(CHINESE_CHAR_REG_STR);
        Matcher m = p.matcher(str);
        int count = 0;
        while (m.find()) {
            for (int i = 0; i <= m.groupCount(); i++) {
                count = count + 1;
            }
        }
        return count;
    }

    /**
     * 去掉字符串中的中文
     *
     * @param str      待处理字符串
     * @param opposite 去除中文,true ;保留非中文,false.
     * @return 处理结果
     */
    public static String removeChineseString(String str, boolean opposite) {

        String resultStr = "";

        for (int i = 0; i < str.length(); i++) {
            String tempStr = org.apache.commons.lang.StringUtils.substring(str,
                    i, i + 1);
            if (tempStr.matches(CHINESE_CHAR_REG_STR)) {
                if (!opposite)
                    resultStr = resultStr + tempStr;
            } else if (opposite)
                resultStr = resultStr + tempStr;
        }

        return resultStr.trim();
    }

    /**
     * <p>
     * 空格有两个：一个 int 值为 32,另外 int 值为 160(网页拷贝下来的居多).
     * </p>
     * <p>
     * 一般的java程序只处理了32，没有处理 160， (如 jdk,commons.lang 就没有处理 160) 本函数去掉 首尾空格 32
     * 和160.
     * </p>
     * 注意其他的空格处理情况
     *
     * @param str 待处理字符串
     * @return 去掉空格后的字符串
     */
    public static String trim(String str) {
        // 去掉中文空格
        str = StringUtils.replace(str, "　", " ");
        // 去掉 char 160
        str = StringUtils.replaceChars(str, (char) 160, (char) 32);
        return StringUtils.trim(str);
    }

    /**
     * 利用正则表达式， 去掉空格、制表符、换页符、换行符等空白字符
     * -
     * On UNIX systems, it returns "\n"; on Microsoft Windows systems it returns "\r\n".
     *
     * @param str
     * @return
     */
    public static String removeReturn(String str) {

        if (str == null)
            return "";
        /* \s 比 \n\r 范围更广 */
        // return str.replaceAll("\\s", ""); // replaceAll 参数为正则表达式
        //--
        //On UNIX systems, it returns "\n"; on Microsoft Windows systems it returns "\r\n".
        return str.replaceAll(linebreak, "");
    }

    /**
     * 利用正则表达式， 替换连续的字符为指定字符串
     * + ：重复一次或更多次
     *
     * @param str
     * @param searchChar
     * @param replaceChar
     * @return
     */
    public static String replaceRepeat(String str, String searchChar, String replaceChar) {

        if (str == null)
            return "";
        return str.replaceAll(searchChar + "+", replaceChar); // replaceAll 参数为正则表达式
    }

    /**
     * 转义HTML用于安全过滤
     *
     * @param html
     * @return
     */
    public static String escapeHtml(String html) {
        return StringEscapeUtils.escapeHtml4(html);
    }

    /**
     * 清理字符串，清理出某些不可见字符
     *
     * @param txt
     * @return {String}
     */
    public static String cleanChars(String txt) {
        return txt.replaceAll("[ 　	`·•�\\f\\t\\v]", "");
    }

    /**
     * 多行数据，根据某行特征，分割为多个行集合
     * <p/>
     * 例子：挑出每个添加剂数据
     * startString = "添加剂"
     * ===============
     * 添加剂名称   银玻璃
     * CAS号
     * 塑料        PE,PP,PS,AS,ABS,PA,PET,
     * PC,PVDC:2.25
     * 0.05 (SML，以银计)
     * ===============
     * 添加剂名称   银锌玻璃
     * CAS号
     * 塑料        PE,PP,PS,AS,ABS,PA,PET,
     * PC,PVDC:3.0
     * 0.05 (SML，以银计)；25.0 (SML，
     * 以锌计)
     * ===============
     * 添加剂名称   银锌沸石
     * CAS号
     * 塑料        PE,PP,PS,AS,ABS,PA,PET,
     * PC,PVDC:3.0
     * 0.05 (SML，以银计)；25.0 (SML，
     * 以锌计)
     * ===============
     * <p/>
     * 特征行开始字符串 ：  添加剂名称
     * <p/>
     * 分割为多个添加剂
     *
     * @param stringList  待分割的行
     * @param startString 特征行开始字符串
     * @return
     */
    public static List<List<String>> splitByStartString(List<String> stringList, String startString) {

        List<List<String>> lists = new ArrayList<List<String>>();
        int start = -1;
        int end = -1;

        for (String s : stringList) {
            s = s.trim();
            if (s == null || s.equals(""))
                continue;

            if (s.startsWith(startString)) {
                if (start == -1 && end == -1) {  //找到第一个
                    start = stringList.indexOf(s);
                    continue;
                }

                if (start != -1 && end == -1)
                    end = stringList.indexOf(s);
            }

            if (start != -1 && end != -1) {
                List<String> subList = stringList.subList(start, end);
                lists.add(subList);

                start = end;
                end = -1;
            }

        }

        //最后一个加入到list中
        lists.add(stringList.subList(start, stringList.size()));
        return lists;
    }

    /**
     * 获得经过转义的，符合 sql 语法的值，主要是去掉了单引号
     * <p/>
     * sql 语句中，双引号相当于单引号的转义。
     * <p/>
     * "Hello' China's "  单引号转义为双引号   "Hello'' China''s "
     *
     * @param str 原 sql 值
     * @return
     */
    public static String escapeSql(String str) {
        if (str == null) {
            return null;
        }
        return StringUtils.replace(str, "'", "''");
    }

    /**
     * 指定数量的字符
     *
     * @param size
     * @param padChar
     * @return
     */
    public static String center(int size, char padChar) {
        return StringUtils.center("", size, padChar);
    }

    /**
     * parse html to plain text
     *
     * @param htmlStr
     * @return
     */
    public static String htmlToPlainText(String htmlStr) {
        return Jsoup.parse(htmlStr).text();
    }


    /**
     * 删除指定分隔符内的字符串
     *
     * @param str
     * @param open
     * @param close
     * @return
     */
    public static String removeSubstringBetween(String str, String open, String close) {

        return str.replaceAll(open + ".+" + close, "");  // 利用正则表达式
        //return removePattern(str, open + ".+" + close); //也可以
    }


    /**
     * 找到字符串中最后一个数字的位置
     * -
     * String s1 = "abc1de"; 1,  -> 3
     * String s2 = "abc1de6"; 6, -> 6
     * String s3 = "77abc1de8";8,  -> 8
     * String s4 = "7abc1de88"; 88,  -> 7
     *
     * @param seq
     * @return
     */
    public static int indexOfLastNumeric(CharSequence seq) {

        //(\d+)(?!.*\d)
        Pattern p = Pattern.compile("(\\d+)(?!.*\\d)");
        Matcher m = p.matcher(seq);
        if (m.find())
            return m.start();
        else return -1;

//        if(m.find()){
//            System.out.println(m.toString());
//            System.out.println(m.group() + " " + m.start() + " " + m.end());
//        }
    }

    /**
     * 字符串最后的数字(字符串形式)
     * -
     * String s1 = "abc1de"; -> 1
     * String s2 = "abc1de6"; -> 6
     * String s3 = "77abc1de8";-> 8
     * String s4 = "7abc1de88";  -> 88
     *
     * @param seq
     * @return
     */
    public static String substringOfLastNumeric(CharSequence seq) {

        //(\d+)(?!.*\d)
        Pattern p = Pattern.compile("(\\d+)(?!.*\\d)");
        Matcher m = p.matcher(seq);
        if (m.find())
            return m.group();
        else return null;

    }

    /**
     * 找到字符串中第一个数字的位置
     * -
     * String s1 = "abc1de"; 1,  -> 3
     * String s2 = "abc1de6"; 6, -> 6
     * String s3 = "77abc1de8";8,  -> 8
     * String s4 = "7abc1de88"; 88,  -> 7
     *
     * @param seq
     * @return
     */
    public static int indexOfFirstNumeric(CharSequence seq) {

        //(\d+)(?!.*\d)
        Pattern p = Pattern.compile("(\\d+)");
        Matcher m = p.matcher(seq);
        if (m.find())
            return m.start();
        else return -1;

//        if(m.find()){
//            System.out.println(m.toString());
//            System.out.println(m.group() + " " + m.start() + " " + m.end());
//        }
    }


    /**
     * 字符串中第一个的数字(字符串形式)
     * -
     * String s1 = "abc1de"; -> 1
     * String s2 = "abc1de6"; -> 6
     * String s3 = "77abc1de8";-> 8
     * String s4 = "7abc1de88";  -> 88
     *
     * @param seq
     * @return 如果不含数字，返回 null
     */
    public static String substringOfFirstNumeric(CharSequence seq) {

        Pattern p = Pattern.compile("(\\d+)");
        Matcher m = p.matcher(seq);
        if (m.find())
            return m.group();
        else return null;

    }

    public static void main(String[] args) {

        String s1 = "abc1de";
        String s2 = "abc1de6";
        String s3 = "77abc1de8";
        String s4 = "7abc1de88";
        String s;

        //  System.out.println(m.group() + " " + m.start() + " " + m.end());
        //Returns the start index of the previous match.
        //Returns the offset after the last character matched.
        //    MyStringUtils.indexOfFirstNumeric(s1);     //1 3 4
        s = s3;
        System.out.println(String.format("%s , %d , %s", s, MyStringUtils.indexOfLastNumeric(s), MyStringUtils.substringOfLastNumeric(s)));
        System.out.println(String.format("%s , %d , %s", s, MyStringUtils.indexOfFirstNumeric(s), MyStringUtils.substringOfFirstNumeric(s)));  //6 6 7
        // MyStringUtils.indexOfFirstNumeric(s3);  //8 8 9
        // MyStringUtils.indexOfFirstNumeric(s4);  //88 7 9
    }

    /**
     * 字符串编码转换的实现方法
     *
     * @param str        待转换编码的字符串
     * @param newCharset 目标编码
     * @return
     * @throws java.io.UnsupportedEncodingException
     */
    public String changeCharset(String str, String newCharset)
            throws UnsupportedEncodingException {
        if (str != null) {
            // 用默认字符编码解码字符串。
            byte[] bs = str.getBytes();
            // 用新的字符编码生成字符串
            return new String(bs, newCharset);
        }
        return null;
    }

    /**
     * 字符串编码转换的实现方法
     *
     * @param str        待转换编码的字符串
     * @param oldCharset 原编码
     * @param newCharset 目标编码
     * @return
     * @throws java.io.UnsupportedEncodingException
     */
    public String changeCharset(String str, String oldCharset, String newCharset)
            throws UnsupportedEncodingException {
        if (str != null) {
            // 用旧的字符编码解码字符串。解码可能会出现异常。
            byte[] bs = str.getBytes(oldCharset);
            // 用新的字符编码生成字符串
            return new String(bs, newCharset);
        }
        return null;
    }

    /**
     * 该方法未验证
     *
     * @param c
     * @return
     */
    public boolean isChineseChar(char c) {
        Character.UnicodeBlock ub = Character.UnicodeBlock.of(c);
        if (ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS
                || ub == Character.UnicodeBlock.CJK_COMPATIBILITY_IDEOGRAPHS
                || ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_A
                || ub == Character.UnicodeBlock.GENERAL_PUNCTUATION
                || ub == Character.UnicodeBlock.CJK_SYMBOLS_AND_PUNCTUATION
                || ub == Character.UnicodeBlock.HALFWIDTH_AND_FULLWIDTH_FORMS) {
            return true;
        }
        return false;
    }
}
