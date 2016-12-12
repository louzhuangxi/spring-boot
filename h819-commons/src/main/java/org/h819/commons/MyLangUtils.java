package org.h819.commons;

import com.rits.cloning.Cloner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;

/**
 * Description : TODO()
 * User: h819
 * Date: 14-4-10
 * Time: 下午5:44
 * To change this template use File | Settings | File Templates.
 */
public class MyLangUtils {
    private static Logger logger = LoggerFactory.getLogger(MyLangUtils.class);

    //防止被初始化
    private MyLangUtils() {}

    /**
     * 此函数求循环次数。类似“分针开始位于12点的位置，90分钟的时间段，分针经过几次12点的位置？（起始时在12点的位置不计）”
     *
     * @param allCount    总时间
     * @param LIMIT_COUNT 一圈需要的时间
     */

    public static int getRecurrent(int allCount, int LIMIT_COUNT) {

        int temp1 = allCount / LIMIT_COUNT;
        int temp2 = allCount % LIMIT_COUNT;
        int count = 0;

        // System.out.println(temp1);
        // System.out.println(temp2);

        // 不满 60 分钟，算一圈
        if (temp1 == 0) {
            if (temp2 != 0) {
                count = 1;
            } else {// 此时 总时间数为 0;
                count = 0;
            }
        } else { // 大于等于60 分钟

            if (temp2 != 0) {
                // 非 60 的倍数
                count = temp1 + 1;
            } else {
                // 60 的倍数;
                count = temp1;
            }
        }

        return count;
    }

    /**
     * 主动抛出异常，并打印信息。抛出异常后，程序停止运行
     *
     * @param message
     */
    public static void throwException(String message) {
        try {
            throw new Exception(message);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * 将旧的15位身份证件号，转成18位， 同时进行身份证信息的简单校验 如果是18位，则只进行校验
     *
     * @param orgIDCardNo
     * @return
     * @throws java.text.ParseException
     */
    public static String converse15DigitTo18Digit(String orgIDCardNo) throws ParseException {

        if (orgIDCardNo == null) {
            throw new IllegalArgumentException("身份证号为null");
        }
        String buffer = orgIDCardNo.trim();
        int numberLength = buffer.length();

        // 校验身份证位数，如果不是15位或18位，则抛出“IllgalArgumentException非法的证件号码”异常
        if (numberLength != 15 && numberLength != 18) {
            throw new ParseException("不合格的身份证号", 0);
        }

        // 校验身份证的日期参数
        if (numberLength == 15) {
            // 校验15位身份证的日期参数
            String birthDateStr = buffer.substring(6, 12);
            try {
                new SimpleDateFormat("yyMMdd").parse(birthDateStr);
            } catch (ParseException e) {
                e.printStackTrace();
                throw new ParseException("不合格的身份证号", 7);
            }
            // 对15位的证件转换18位的前缀部分
            buffer = buffer.substring(0, 6) + "19" + buffer.substring(6, 15);

        } else {
            // 校验18位身份证的日期参数
            String birthDateStr = buffer.substring(6, 14);
            try {
                new SimpleDateFormat("yyyyMMdd").parse(birthDateStr);
            } catch (ParseException e) {
                e.printStackTrace();
                throw new ParseException("不合格的身份证号", 7);
            }
            // 截取前17位，对18位证件进行尾部编码重计算
            buffer = buffer.substring(0, 17);
        }

        // 计算18位证的校验位
        char[] charArray = buffer.toCharArray();
        int factor = 0;
        for (int i = 18; i >= 2; i--) {
            factor += (((int) Math.pow(2, (i - 1))) % 11)
                    * ((charArray[18 - i] & 0xff) - 48);
        }
        factor %= 11;
        switch (factor) {
            case 0:
                buffer += "1";
                break;
            case 1:
                buffer += "0";
                break;
            case 2:
                buffer += "X";
                break;
            case 3:
                buffer += "9";
                break;
            case 4:
                buffer += "8";
                break;
            case 5:
                buffer += "7";
                break;
            case 6:
                buffer += "6";
                break;
            case 7:
                buffer += "5";
                break;
            case 8:
                buffer += "4";
                break;
            case 9:
                buffer += "3";
                break;
            case 10:
                buffer += "2";
                break;
        }

        // 如果是18位的身份证，要同原始的证件代码进行校验
        if (numberLength == 18 && !buffer.equals(orgIDCardNo.trim())) {
            throw new ParseException("不合格的身份证号", 18);
        }
        return buffer;
    }

    /*
     * 知识点：java 中：如果一个对象能被 clone ，需要该对象实现 Cloneable 接口
     */

    /**
     * clone : 内存中完全独立的一个对象，和被 clone 对象相同，操作其中一个，对另外一个无影响
     * -
     * 被 clone 对象不需要实现 Cloneable 接口，可以任意克隆对象。
     * -
     * 利用 https://code.google.com/p/cloning/ 插件
     * This method makes a "deep clone" of any object it is given.
     *
     * @param object 被克隆对象
     * @return 完全独立的被克隆对象
     */
    //org.apache.commons.lang3.SerializationUtils.clone() 只能深度克隆继承了 Serializable 接口的对象
    public static <T> T deepClone(T object) {
        Cloner cloner = new Cloner();
        return cloner.deepClone(object);
    }

    /**
     * @param args
     */
    public static void main(String[] args) {
        // TODO Auto-generated method stub
        // MyLangUtils.throwException(" 主动抛出异常");
        try {

            System.out.println(MyCharsetUtils.detectEncoding(new File("D:\\01\\gntl.csv")));

        } catch (IOException e) {

            e.printStackTrace();
        }

        // 身份证验证
        try {
            // 18 位
            System.out.println(MyLangUtils
                    .converse15DigitTo18Digit("230103197404180351"));
            // 15 位
            System.out.println(MyLangUtils
                    .converse15DigitTo18Digit("210101830101065"));

        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}