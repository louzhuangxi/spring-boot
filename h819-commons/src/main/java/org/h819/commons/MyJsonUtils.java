package org.h819.commons;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import org.h819.commons.json.FastJsonPropertyPreFilter;

/**
 * Description : TODO(扩展 com.alibaba.fastjson.JSON 类)
 * User: h819
 * Date: 14-4-24
 * Time: 下午2:00
 * To change this template use File | Settings | File Templates.
 */
public class MyJsonUtils {


    // 序列化：任意对象 ，Object -> String  , object 可以是一个bean，也可以是 bean 对象的集合
    // 可以用来测试查看对象的属性
    // String jsonString = JSON.toJSONString(obj);


    // 反序列化 String -> Object
    // VO vo = JSON.parseObject("...", VO.class);  //单个对象字符串
    // List<VO> vo = JSON.parseArray("...", VO.class);  //多个对象字符串，如 list 有多个对象，序列化为字符串之后，进行反序列化

    private MyJsonUtils() {
    }

    /**
     * 过滤不需要输出的属性，用法见 FastJsonPropertyPreFilter
     * -
     * 如果不在 hibernate 的事务环境下，级联属性是不能输出的，此时要把所有的级联属性过滤掉，才可以不进入死循环
     *
     * @param bean
     * @param preFilter
     * @return
     */
    public static String toJSONString(Object bean, FastJsonPropertyPreFilter preFilter) {
        return JSON.toJSONString(bean, preFilter, SerializerFeature.DisableCircularReferenceDetect, SerializerFeature.WriteDateUseDateFormat);
    }


    /**
     * 利用 SerializerFeature.PrettyFormat ，格式化输出输出任何 Bean 对象
     *
     * @param bean
     */
    public static void prettyPrint(Object bean) {
        System.out.print(JSON.toJSONString(bean, SerializerFeature.DisableCircularReferenceDetect, SerializerFeature.WriteDateUseDateFormat, SerializerFeature.PrettyFormat));
    }


    /**
     * 利用 SerializerFeature.PrettyFormat ，格式化输出输出 json 字符串 ，属性过滤器用 fastJson 实现
     * -
     * 如果不在 hibernate 的事务环境下，级联属性是不能输出的，此时要把所有的级联属性过滤掉，才可以不进入死循环
     *
     * @param bean
     */
    public static void prettyPrint(Object bean, FastJsonPropertyPreFilter preFilter) {
        System.out.print(JSON.toJSONString(bean, preFilter, SerializerFeature.DisableCircularReferenceDetect, SerializerFeature.WriteDateUseDateFormat, SerializerFeature.PrettyFormat));
    }
}
