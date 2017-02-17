package org.h819.commons;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import org.apache.commons.io.FileUtils;
import org.h819.commons.json.FastJsonPropertyPreFilter;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.util.Collection;
import java.util.List;

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

    /**
     * 满足 Bean 格式的对象的序列化和反序列化
     */

    // 序列化：任意对象 ，Object -> String  , object 可以是一个bean，也可以是 bean 对象的集合
    // 可以用来测试查看对象的属性
    // String jsonString = JSON.getJSONString(obj);

    // 反序列化 String -> Object
    // VO vo = JSON.parseObject("...", VO.class);  //单个对象字符串
    // List<VO> vo = JSON.parseArray("...", VO.class);  //多个对象字符串，如 list 有多个对象，序列化为字符串之后，进行反序列化
    private MyJsonUtils() {
    }

    /**
     * 使用和单个 Bean 对象，和 Bean 对象的集合
     * 多写一个集合参数，便于理解，实际上不写也行
     */

    /**
     * @param bean
     */
    public static void prettyPrint(Object bean) {
        prettyPrint(bean, null, Charset.defaultCharset());
    }

    /**
     * 多写一个集合参数的方法
     *
     * @param beans
     */
    public static void prettyPrint(Collection<Object> beans) {
        prettyPrint(beans);
    }

    /**
     * @param bean
     * @param charset
     */
    public static void prettyPrint(Object bean, Charset charset) {
        prettyPrint(bean, null, charset);
    }

    /**
     * 多写一个集合参数的方法
     *
     * @param beans
     */
    public static void prettyPrint(Collection<Object> beans, Charset charset) {
        prettyPrint(beans, charset);
    }

    public static void prettyPrint(Object bean, FastJsonPropertyPreFilter preFilter) {
        prettyPrint(bean, preFilter, Charset.defaultCharset());
    }

    /**
     * 多写一个集合参数的方法
     *
     * @param beans
     */
    public static void prettyPrint(Collection<Object> beans, FastJsonPropertyPreFilter preFilter) {
        prettyPrint(beans, preFilter);
    }

    /**
     * 打印 bean object 到 console
     * -
     * 利用 SerializerFeature.PrettyFormat ，格式化输出输出 json 字符串 ，属性过滤器用 fastJson 实现
     * -
     * 如果不在 hibernate 的事务环境下，级联属性是不能输出的，此时要把所有的级联属性过滤掉，才可以不进入死循环
     *
     * @param bean      待打印的对象，可以是对象的集合
     * @param preFilter 过滤器
     * @param charset   输出到 console 的字符串的编码，有时候在 maven 环境中会出现乱码，此参数应修改为和 maven 一致
     *                  发现 fastjson 有此问题，用此方法修正
     */
    public static void prettyPrint(Object bean, FastJsonPropertyPreFilter preFilter, Charset charset) {
        try {
            PrintStream out = new PrintStream(System.out, true, charset.name());
            out.println(getJSONString(bean, preFilter));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    public static void prettyPrint(Collection<Object> beans, FastJsonPropertyPreFilter preFilter, Charset charset) {
        prettyPrint(beans, preFilter, charset);
    }

    /**
     * 输出到文件
     *
     * @param file
     * @param bean
     */
    public static void prettyWrite(File file, Object bean) {
        prettyWrite(file, bean, null, Charset.defaultCharset());
    }

    /**
     * 多写一个集合参数的方法
     *
     * @param file
     * @param beans
     */
    public static void prettyWrite(File file, Collection<Object> beans) {
        prettyWrite(file, beans);
    }

    public static void prettyWrite(File file, Object bean, Charset charset) {

        prettyWrite(file, bean, null, charset);
    }

    /**
     * 多写一个集合参数的方法
     *
     * @param file
     * @param beans
     * @param charset
     */
    public static void prettyWrite(File file, Collection<Object> beans, Charset charset) {
        prettyWrite(file, beans, charset);
    }

    public static void prettyWrite(File file, Object bean, FastJsonPropertyPreFilter preFilter) {
        prettyWrite(file, bean, preFilter, Charset.defaultCharset());
    }

    /**
     * 多写一个集合参数的方法
     *
     * @param file
     * @param beans
     * @param preFilter
     */
    public static void prettyWrite(File file, Collection<Object> beans, FastJsonPropertyPreFilter preFilter) {

        prettyWrite(file, beans, preFilter);
    }

    public static void prettyWrite(File file, Object bean, FastJsonPropertyPreFilter preFilter, Charset charset) {

        try {
            FileUtils.write(file, getJSONString(bean, preFilter), charset);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 多写一个集合参数的方法
     *
     * @param file
     * @param beans
     * @param preFilter
     * @param charset
     */
    public static void prettyWrite(File file, Collection<Object> beans, FastJsonPropertyPreFilter preFilter, Charset charset) {
        prettyWrite(file, beans, preFilter, charset);
    }

    private static String getJSONString(Object bean, FastJsonPropertyPreFilter preFilter) {

        JSON.DEFFAULT_DATE_FORMAT = "yyyy-MM-dd HH:mm:ss.SSS";

        SerializerFeature[] features = {
                SerializerFeature.DisableCircularReferenceDetect,
                SerializerFeature.WriteDateUseDateFormat,
                SerializerFeature.PrettyFormat};

        if (preFilter == null)
            return JSON.toJSONString(bean, features);
        else
            return JSON.toJSONString(bean, preFilter, features);

    }

    public static String toJSONString(Object bean, FastJsonPropertyPreFilter preFilter) {
        JSON.DEFFAULT_DATE_FORMAT = "yyyy-MM-dd HH:mm:ss.SSS";
        SerializerFeature[] features = {
                SerializerFeature.DisableCircularReferenceDetect,
                SerializerFeature.WriteDateUseDateFormat
        };

        if (preFilter == null)
            return JSON.toJSONString(bean, features);
        else
            return JSON.toJSONString(bean, preFilter, features);

    }

    public static String toPrettyJSONString(Object bean, FastJsonPropertyPreFilter preFilter) {

        return getJSONString(bean, preFilter);
    }

    public static <T> T parse(String text, Class<T> clazz) {
        return JSON.parseObject(text, clazz);
    }

    /**
     * @param jsonText 包含 jsonText 的文件
     * @param clazz
     * @param charset
     * @param <T>
     * @return
     * @throws IOException
     */
    public static <T> T parse(File jsonText, Class<T> clazz, Charset charset) throws IOException {
        return parse(FileUtils.readFileToString(jsonText, charset), clazz);

    }

    public static <T> T parse(File jsonText, Class<T> clazz) throws IOException {
        return parse(jsonText, clazz, Charset.defaultCharset());

    }

    public static <T> List<T> parseArray(String jsonText, Class<T> clazz) {
        return JSON.parseArray(jsonText, clazz);

    }

    /**
     * @param jsonText 用 FileUtils 读取， jdk Files.read 方法会有问题
     * @param clazz
     * @param charset
     * @param <T>
     * @return
     * @throws IOException
     */
    public static <T> List<T> parseArray(File jsonText, Class<T> clazz, Charset charset) throws IOException {
        return parseArray(FileUtils.readFileToString(jsonText, charset), clazz);

    }

    public static <T> List<T> parseArray(File jsonText, Class<T> clazz) throws IOException {
        return parseArray(jsonText, clazz, Charset.defaultCharset());

    }

}
