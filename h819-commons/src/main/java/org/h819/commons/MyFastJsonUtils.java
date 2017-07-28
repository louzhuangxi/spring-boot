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

/**
 * 对于复杂对象的序列化，应该用 java bean 相互嵌套来表示，不要用 List<List<Map>> 等这样的基础 Collection ，不容易反序列化
 */
public class MyFastJsonUtils {

    /**
     * 满足 Bean 格式的对象的序列化和反序列化
     */

    // 序列化：任意对象 ，Object -> String  , object 可以是一个bean，也可以是 bean 对象的集合
    // 可以用来测试查看对象的属性
    // String jsonString = JSON.getJSONString(obj);

    // 反序列化 String -> Object
    // VO vo = JSON.parseObject("...", VO.class);  //单个对象字符串
    // List<VO> vo = JSON.parseArray("...", VO.class);  //多个对象字符串，如 list 有多个对象，序列化为字符串之后，进行反序列化
    private MyFastJsonUtils() {
    }


    /**
     * @param bean 可以是单个 Bean ，也可以是  Bean 对象的集合  Collection<Object> beans
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

    /**
     * 输出到文件
     *
     * @param file json 字符串输出到的文件
     * @param bean 可以是单个 Bean ，也可以是  Bean 对象的集合  Collection<Object> beans
     */
    public static void prettyWrite(File file, Object bean) {
        prettyWrite(file, bean, null, Charset.defaultCharset());
    }


    public static void prettyWrite(File file, Object bean, Charset charset) {

        prettyWrite(file, bean, null, charset);
    }

    public static void prettyWrite(File file, Object bean, FastJsonPropertyPreFilter preFilter) {
        prettyWrite(file, bean, preFilter, Charset.defaultCharset());
    }


    public static void prettyWrite(File file, Object bean, FastJsonPropertyPreFilter preFilter, Charset charset) {

        try {
            FileUtils.write(file, getJSONString(bean, preFilter), charset);
        } catch (IOException e) {
            e.printStackTrace();
        }
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


    public static String toPrettyJSONString(Object bean, FastJsonPropertyPreFilter preFilter) {

        return getJSONString(bean, preFilter);
    }

    public static <T> T parse(String jsonText, Class<T> clazz) {
        return JSON.parseObject(jsonText, clazz);
    }


    public static <T> T parse(File jsonText, Class<T> clazz) throws IOException {
        return parse(jsonText, Charset.defaultCharset(), clazz);

    }

    /**
     * @param jsonText 包含 jsonText 的文件
     * @param clazz
     * @param charset
     * @param <T>
     * @return
     * @throws IOException
     */
    public static <T> T parse(File jsonText, Charset charset, Class<T> clazz) throws IOException {
        return parse(FileUtils.readFileToString(jsonText, charset), clazz);

    }

    public static <T> List<T> parseArray(String jsonText, Class<T> clazz) {
        return JSON.parseArray(jsonText, clazz);

    }


    public static <T> List<T> parseArray(File jsonText, Class<T> clazz) throws IOException {
        return parseArray(jsonText, Charset.defaultCharset(), clazz);

    }

    /**
     * @param jsonText 用 FileUtils 读取， jdk Files.read 方法有问题
     * @param clazz
     * @param charset
     * @param <T>
     * @return
     * @throws IOException
     */
    public static <T> List<T> parseArray(File jsonText, Charset charset, Class<T> clazz) throws IOException {
        return parseArray(FileUtils.readFileToString(jsonText, charset), clazz);

    }

}
