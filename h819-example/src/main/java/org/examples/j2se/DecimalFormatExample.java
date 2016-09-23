package org.examples.j2se;

import java.text.DecimalFormat;

/**
 * Description : TODO(演示数字格式化)
 * User: h819
 * Date: 14-4-28
 * Time: 下午2:28
 * To change this template use File | Settings | File Templates.
 */
public class DecimalFormatExample {

    public static void main(String[] args) {
        DecimalFormat df = new DecimalFormat();
        System.out.println("");
        System.out.println(" DecimalFormat 格式化数据，可以进行四舍五入");
        System.out.println("");

        double data = 1203.405607809;
        System.out.println(" == 格式化之前：" + data);
        System.out.println("");

        String pattern = "0.0";//1203.4
        df.applyPattern(pattern);
        System.out.println(" 1):  采用 " + pattern + " 模式格式化后：" + df.format(data));
        System.out.println("");

        pattern = "0";//1203.4
        df.applyPattern(pattern);
        System.out.println(" 1.1):  采用 " + pattern + " 模式格式化后：" + df.format(data));
        System.out.println("");

        // 可以在模式后加上自己想要的任何字符，比如单位
        pattern = "00000000.000kg";//00001203.406kg
        df.applyPattern(pattern);
        System.out.println(" 2): 采用 " + pattern + " 模式格式化后：" + df.format(data));
        System.out.println("");

        //#表示如果存在就显示字符，如果不存在就不显示，只能用在模式的两头
        pattern = "##000.000kg";//1203.406kg
        df.applyPattern(pattern);
        System.out.println(" 3): 采用 " + pattern + " 模式格式化后：" + df.format(data));
        System.out.println("");

        //-表示输出为负数，必须放在最前面
        pattern = "-000.000";//-1203.406
        df.applyPattern(pattern);
        System.out.println(" 4): 采用 " + pattern + " 模式格式化后：" + df.format(data));
        System.out.println("");

        //,是分组分隔符 ：输出结果12,03.41
        pattern = "-0,00.0#";//-12,03.41
        df.applyPattern(pattern);
        System.out.println(" 5): 采用 " + pattern + " 模式格式化后：" + df.format(data));
        System.out.println("");

        //E表示输出为指数，”E“之前的字符串是底数的格式，之后的是指数的格式。
        pattern = "0.00E000";//1.20E003
        df.applyPattern(pattern);
        System.out.println(" 6): 采用 " + pattern + " 模式格式化后：" + df.format(data));
        System.out.println("");

        //%表示乘以100并显示为百分数，要放在最后
        pattern = "0.00%";//120340.56%
        df.applyPattern(pattern);
        System.out.println(" 7): 采用 " + pattern + " 模式格式化后：" + df.format(data));
        System.out.println("");

        //"u2030"表示乘以1000并显示为千分数，要放在最后
        pattern = "0.00u2030";//203405.61‰
        df.applyPattern(pattern);
        System.out.println(" 8)采用 " + pattern + " 模式格式化后：" + df.format(data));
        System.out.println("");


        //"u00A4"货币符号，要放在两端*****1203.41￥
        pattern = "0.00u00A4";//1203.41￥
        df.applyPattern(pattern);
        System.out.println(" 9): 采用 " + pattern + " 模式格式化后：" + df.format(data));
        System.out.println("");

        //'用于在前缀或或后缀中为特殊字符加引号，要创建单引号本身，请连续使用两个单引号："# o''clock"。
        pattern = "'#'#";//#1203

        // pattern = "'#'" ;//#1203
        df.applyPattern(pattern);
        System.out.println(" 10): 采用 " + pattern + " 模式格式化后：" + df.format(data));
        System.out.println("");
        pattern = "# o''clock";//1203 o'clock
        df.applyPattern(pattern);
        System.out.println(" 11): 采用 " + pattern + " 模式格式化后：" + df.format(data));
        System.out.println("");

        //''放在中间或后面单引号就显示在最后，放在最前面单引号就显示在最前
        // pattern = "# o''clock.000" ;//1203.406 o'clock
        // pattern = "# .000o''clock";//1203.406 o'clock
        // pattern = "# .000''";//1203.406 '
        // pattern = "# .''000";//1203.406 '
        pattern = "''# .000";//'1203.406
        df.applyPattern(pattern);
        System.out.println(" 12): 采用 " + pattern + " 模式格式化后：" + df.format(data));
    }
}
