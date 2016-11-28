package org.h819.commons;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import org.h819.commons.json.FastJsonPropertyPreFilter;

import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;

/**
 * Description : TODO(扩展 com.alibaba.fastjson.JSON 类)
 * User: h819
 * Date: 14-4-24
 * Time: 下午2:00
 * To change this template use File | Settings | File Templates.
 */
//SerializerFeature 属性
//http://46aae4d1e2371e4aa769798941cef698.devproxy.yunshipei.com/u010246789/article/details/52539576
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
     * @param bean
     */
    public static void prettyPrint(Object bean) {
        prettyPrint(bean, null, Charset.defaultCharset());
    }

    /**
     * @param bean
     * @param charset
     */
    public static void prettyPrint(Object bean, Charset charset) {
        prettyPrint(bean, null, charset);
    }

    //   FastJsonPropertyPreFilter preFilter = new FastJsonPropertyPreFilter();
    //    preFilter.addExcludes(InfoEntity.class, "refUserInfoEntities"); //在整个转换过程中，无论哪个级联层次，只要遇到 InfoEntity 类，那么他的 refUserInfoEntities 属性就不进行转换
//     preFilter.addExcludes(ProvinceEntity.class, "parent", "children"); //多个属性


    public static void prettyPrint(Object bean, FastJsonPropertyPreFilter preFilter) {
        prettyPrint(bean, preFilter, Charset.defaultCharset());
    }

    /**
     * 打印 bean object 到 console
     * -
     * 利用 SerializerFeature.PrettyFormat ，格式化输出输出 json 字符串 ，属性过滤器用 fastJson 实现
     * -
     * 如果不在 hibernate 的事务环境下，级联属性是不能输出的，此时要把所有的级联属性过滤掉，才可以不进入死循环
     *
     * @param bean      待打印的对象
     * @param preFilter 过滤器
     * @param charset   输出到 console 的字符串的编码，有时候在 maven 环境中会出现乱码，此参数应修改为和 maven 一致
     *                  发现 fastjson 有此问题，用此方法修正
     */
    public static void prettyPrint(Object bean, FastJsonPropertyPreFilter preFilter, Charset charset) {
        JSON.DEFFAULT_DATE_FORMAT = "yyyy-MM-dd HH:mm:ss.SSS";

        SerializerFeature[] features = {
                SerializerFeature.DisableCircularReferenceDetect,
                SerializerFeature.WriteDateUseDateFormat,
                SerializerFeature.PrettyFormat};

        try {
            PrintStream out = new PrintStream(System.out, true, charset.name());
            if (preFilter == null)
                out.println(JSON.toJSONString(bean, features));
            else
                out.println(JSON.toJSONString(bean, preFilter, features));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }


    }

}
