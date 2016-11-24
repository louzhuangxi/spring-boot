package org.h819.commons.json;

import com.alibaba.fastjson.serializer.JSONSerializer;
import com.alibaba.fastjson.serializer.PropertyPreFilter;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Description : TODO(fastjson 序列化对象，设置某个类的属性过滤器，excludes 优先于 includes .)
 * User: h819
 * Date: 2015/4/14
 * Time: 10:01
 * To change this template use File | Settings | File Templates.
 */
//com.alibaba.fastjson.serializer.SimplePropertyPreFilter 也可以达到本类的效果，但不如本类易用，使用本类
//https://github.com/alibaba/fastjson/wiki/%E4%BD%BF%E7%94%A8SimplePropertyPreFilter%E8%BF%87%E6%BB%A4%E5%B1%9E%E6%80%A7
//使用方法，见 example 方法。
public class FastJsonPropertyPreFilter implements PropertyPreFilter {

    private Map<Class<?>, String[]> includes = new HashMap<>();
    private Map<Class<?>, String[]> excludes = new HashMap<>();

    public FastJsonPropertyPreFilter() {
    }

    /**
     * 和 excludes 相反
     *
     * @param clasz
     * @param includes
     */
    public void addIncludes(Class clasz, String... includes) {
        this.includes.put(clasz, includes);

    }

    /**
     * 设置不进行序列化的属性。
     *
     * @param clasz    准备序列化的对象类别，该类别可以位于级联层次的任意层次，只要属性类别为此类别，那么其属性就会被忽略。用于 hibernate 的关联关系中，可以避免深度级联而陷于死循环。
     *                 excludes 优先于 includes
     * @param excludes 不进行序列化的属性
     */
    public void addExcludes(Class clasz, String... excludes) {
        this.excludes.put(clasz, excludes);
    }

    @Override
    public boolean apply(JSONSerializer serializer, Object source, String name) {

        //对象为空。直接放行
        if (source == null) {
            return true;
        }
        // 获取当前需要序列化的对象的类对象
        Class<?> clazz = source.getClass();

        // 无需序列的对象、寻找需要过滤的对象，可以提高查找层级
        // 找到不需要的序列化的类型
        for (Map.Entry<Class<?>, String[]> item : this.excludes.entrySet()) {
            // isAssignableFrom()，用来判断类型间是否有继承关系
            if (item.getKey().isAssignableFrom(clazz)) {
                List<String> list = Arrays.asList(item.getValue());
                if (list.contains(name))
                    return false;
            }
        }

        //   System.out.println(clazz.getName() + " , " + name);

        // 需要序列的对象集合为空 表示全部需要序列化
        if (this.includes.isEmpty()) {
            return true;
        }

        // 需要序列的对象
        for (Map.Entry<Class<?>, String[]> item : this.includes.entrySet()) {
            // isAssignableFrom()，用来判断类型间是否有继承关系
            if (item.getKey().isAssignableFrom(clazz)) {
                List<String> list = Arrays.asList(item.getValue());
                if (list.contains(name))
                    return true;
            }
        }

        return false;
    }

    /**
     * 使用方法
     * 在 object 转换为 jsonString 的时候，被转换对象( object ) 中的属性可能是对象之间的相互关联，有的是父子关系，是递归管理 ...
     * 无论在哪个级联层次的对象中，只要包含指定对象的指定属性，就可以被忽略。
     * 作用：1. 减少 json 数据输出
     *       2. 斩断双向管理的对象，转换为 jsonStirng 的递归死循环。
     */
    private void example() {

//        FastJsonPropertyPreFilter preFilter = new FastJsonPropertyPreFilter();
//        preFilter.addExcludes(InfoEntity.class, "refUserInfoEntities"); //在整个转换过程中，无论哪个级联层次，只要遇到 InfoEntity 类，那么他的 refUserInfoEntities 属性就不进行转换
//        preFilter.addExcludes(ProvinceEntity.class, "parent", "children"); //多个属性
//        preFilter.addExcludes(WebSiteEntity.class, "webSiteColumns");
//        preFilter.addExcludes(WebSiteColumnEntity.class, "snatchUrls");
//        return JSON.toJSONString(response, preFilter, SerializerFeature.DisableCircularReferenceDetect);
    }
}