package org.h819.commons;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * 记住，一定要用Double和double！ 看看 commons 中有无可用的方法 保留两位小数，参考 MyDateUtils 类中的写法
 *
 * @author jianghui
 */

// 各种数字之间的转换
// 保留小数位数?????
// http://blog.sina.com.cn/s/blog_3c6ecea90100cbr8.html~type=v5_one&label=rela_prevarticle
public class MyMathUtils {

    // 默认除法运算精度
    private static final int DEF_DIV_SCALE = 10;

    // 这个类不能实例化

    private MyMathUtils() {

    }

    /**
     * 提供精确的加法运算。
     *
     * @param v1 被加数
     * @param v2 加数
     * @return 两个参数的和
     */

    public static double add(double v1, double v2) {

        BigDecimal b1 = new BigDecimal(Double.toString(v1));

        BigDecimal b2 = new BigDecimal(Double.toString(v2));

        return b1.add(b2).doubleValue();

    }

    /**
     * 提供精确的减法运算。
     *
     * @param v1 被减数
     * @param v2 减数
     * @return 两个参数的差
     */

    public static double sub(double v1, double v2) {

        BigDecimal b1 = new BigDecimal(Double.toString(v1));

        BigDecimal b2 = new BigDecimal(Double.toString(v2));

        return b1.subtract(b2).doubleValue();

    }

    /**
     * 提供精确的乘法运算。
     *
     * @param v1 被乘数
     * @param v2 乘数
     * @return 两个参数的积
     */

    public static double mul(double v1, double v2) {

        BigDecimal b1 = new BigDecimal(Double.toString(v1));

        BigDecimal b2 = new BigDecimal(Double.toString(v2));

        return b1.multiply(b2).doubleValue();

    }

    /**
     * 提供（相对）精确的除法运算，当发生除不尽的情况时，精确到
     * <p>
     * 小数点以后10位，以后的数字四舍五入。
     *
     * @param v1 被除数
     * @param v2 除数
     * @return 两个参数的商
     */

    public static double div(double v1, double v2) {

        return div(v1, v2, DEF_DIV_SCALE);

    }

    /**
     * 提供（相对）精确的除法运算。当发生除不尽的情况时，由scale参数指
     * <p>
     * 定精度，以后的数字四舍五入。
     *
     * @param v1    被除数
     * @param v2    除数
     * @param scale 表示表示需要精确到小数点以后几位。
     * @return 两个参数的商
     */

    public static double div(double v1, double v2, int scale) {

        if (scale < 0) {
            throw new IllegalArgumentException(
                    "The scale must be a positive integer or zero");
        }
        BigDecimal b1 = new BigDecimal(Double.toString(v1));
        BigDecimal b2 = new BigDecimal(Double.toString(v2));
        return b1.divide(b2, scale, BigDecimal.ROUND_HALF_UP).doubleValue();

    }

    /**
     * 提供精确的小数位四舍五入处理。
     *
     * @param v     需要四舍五入的数字
     * @param scale 小数点后保留几位
     * @return 四舍五入后的结果
     */

    public static double round(double v, int scale) {

        if (scale < 0) {
            throw new IllegalArgumentException(
                    "The scale must be a positive integer or zero");
        }
        BigDecimal b = new BigDecimal(Double.toString(v));

        BigDecimal one = new BigDecimal("1");

        return b.divide(one, scale, BigDecimal.ROUND_HALF_UP).doubleValue();
    }

    /**
     * 判断给定的数是否是奇数
     *
     * @param number
     * @return true 奇数；false 偶数
     */
    public static boolean isOddNumber(int number) {

        return number % 2 != 0;

    }


    /**
     * 将一个整数 number，平均分成 partition 份，返回每份的起始和结束数，例如
     * 5846 平均分成 10 份，每份的起始和结束数是
     * ------
     * [1,585],
     * [586,1170],
     * [1171,1755],
     * [1756,2340],
     * [2341,2925],
     * [2926,3510],
     * [3511,4095],
     * [4096,4680],
     * [4681,5265],
     * [5266,5846] // 最后一组的结果，会比其他的结果集少(整除的时候，相同)
     * ------
     * 用于计算平均分配多线程任务
     *
     * @param number
     * @param part
     * @return
     */
    public static List<Integer[]> averageNumberToPart(int number, int part) {

        int all = number;
        int count = part;//平局分成十分
        int average = all / count; // 平均数
        int r = all % count; // 余数
        //  System.out.println(r);
        if (r != 0)
            if (r < count) // 余数小于平均数，平均数+1
                average = average + 1;
        //   System.out.println(average);
        int i = 0;
        List<Integer[]> list = new ArrayList<>(part);
        while (i < all) {
            int start = i + 1; // 起始
            int end = i + average; // 结束
            if (end >= all)
                end = all;
            System.out.println(start + "~" + end);
            list.add(new Integer[]{start, end});
            i = i + average;
        }
        return list;
    }

    /**
     * @param args
     */
    public static void main(String[] args) {
        // TODO Auto-generated method stub
        int number = 5846; //
        int part = 10;    // 分成 10 份

        //  MyMathUtils.averageNumberToPart(number, partition);
        MyFastJsonUtils.prettyPrint(MyMathUtils.averageNumberToPart(4000, part));
    }
}
