package org.h819.commons;

import com.alibaba.fastjson.JSON;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.apache.commons.io.FileUtils;
import org.h819.commons.json.FastJsonPropertyPreFilter;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Description : TODO(包装 ObjectMapper 类)
 * User: h819
 * Date: 14-4-24
 * Time: 下午2:00
 * To change this template use File | Settings | File Templates.
 * <p>
 * 对于复杂对象的序列化，应该用 java bean 相互嵌套来表示，不要用 List<List<Map>> 等这样的基础 Collection ，不容易反序列化
 */

/**
 * 与 fast json 的区别是，无法使用属性过滤
 * 所以在有双向级联的对象中，不要使用
 * 
 */
public class MyJacksonUtils {

    https://github.com/alibaba/fastjson/wiki/%E5%9C%A8-Spring-%E4%B8%AD%E9%9B%86%E6%88%90-Fastjson

    private static ObjectMapper objectMapper;

    static {
        objectMapper = new ObjectMapper();
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        objectMapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS")); //转换时，date 的格式
        objectMapper.enable(SerializationFeature.INDENT_OUTPUT);  // pretty print
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        objectMapper.configure(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY, false);
        objectMapper.enableDefaultTyping();

    }

    /**
     * 满足 Bean 格式的对象的序列化和反序列化
     */

    // 序列化：任意对象 ，Object -> String  , object 可以是一个bean，也可以是 bean 对象的集合
    // 可以用来测试查看对象的属性
    // String jsonString = JSON.getJSONString(obj);

    // 反序列化 String -> Object
    // VO vo = JSON.parseObject("...", VO.class);  //单个对象字符串
    // List<VO> vo = JSON.parseArray("...", VO.class);  //多个对象字符串，如 list 有多个对象，序列化为字符串之后，进行反序列化
    private MyJacksonUtils() {
    }

    /**
     * @param bean 可以是单个 Bean ，也可以是  Bean 对象的集合  Collection<Object> beans
     */
    public static void prettyPrint(Object bean) {
        prettyPrint(bean, Charset.defaultCharset());
    }


    /**
     * 打印 bean object 到 console
     * -
     * 利用 SerializerFeature.PrettyFormat ，格式化输出输出 json 字符串 ，属性过滤器用 fastJson 实现
     * -
     * 如果不在 hibernate 的事务环境下，级联属性是不能输出的，此时要把所有的级联属性过滤掉，才可以不进入死循环
     *
     * @param bean         待打印的对象，可以是对象的集合
     * @param printCharset 输出到 console 的字符串的编码，有时候在 maven 环境中会出现乱码，此参数应修改为和 maven 一致
     *                     发现 fastjson 有此问题，用此方法修正
     */
    public static void prettyPrint(Object bean, Charset printCharset) {
        try {
            PrintStream out = new PrintStream(System.out, true, printCharset.name());
            out.println(toPrettyJSONString(bean));
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


    public static void prettyWrite(File file, Object bean, Charset fileCharset) {

        prettyWrite(file, bean, null, fileCharset);
    }

    public static void prettyWrite(File file, Object bean, FastJsonPropertyPreFilter preFilter) {
        prettyWrite(file, bean, preFilter, Charset.defaultCharset());
    }


    public static void prettyWrite(File file, Object bean, FastJsonPropertyPreFilter preFilter, Charset fileCharset) {

        try {
            FileUtils.write(file, toPrettyJSONString(bean, preFilter), fileCharset);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public static String toPrettyJSONString(Object bean) {
        try {
            objectMapper.writeValueAsString(bean);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

    }


    public static <T> T parseObject(String jsonString, Class<T> clazz) {
        return JSON.parseObject(jsonString, clazz);
    }


    public static <T> T parseObject(File jsonStringFile, Class<T> clazz) throws IOException {
        return parseObject(jsonStringFile, Charset.defaultCharset(), clazz);

    }

    /**
     * @param jsonStringFile 包含 jsonString 的文件, 用 FileUtils 读取， jdk Files.read 方法有问题，不能读全所有行
     * @param clazz
     * @param fileCharset
     * @param <T>
     * @return
     * @throws IOException
     */
    public static <T> T parseObject(File jsonStringFile, Charset fileCharset, Class<T> clazz) throws IOException {
        return parseObject(FileUtils.readFileToString(jsonStringFile, fileCharset), clazz);

    }

    public static <T> List<T> parseArray(String jsonString, Class<T> clazz) {
        return JSON.parseArray(jsonString, clazz);

    }


    public static <T> List<T> parseArray(File jsonStringFile, Class<T> clazz) throws IOException {
        return parseArray(jsonStringFile, Charset.defaultCharset(), clazz);

    }

    /**
     * @param jsonStringFile 用 FileUtils 读取， jdk Files.read 方法有问题，不能读全所有行
     * @param clazz
     * @param fileCharset
     * @param <T>
     * @return
     * @throws IOException
     */
    public static <T> List<T> parseArray(File jsonStringFile, Charset fileCharset, Class<T> clazz) throws IOException {
        return parseArray(FileUtils.readFileToString(jsonStringFile, fileCharset), clazz);

    }

    public static void main(String[] args) {
        System.out.println(JSON.toJSONString("hell world."));
        System.out.println(toPrettyJSONString(new Date()));
    }

}
