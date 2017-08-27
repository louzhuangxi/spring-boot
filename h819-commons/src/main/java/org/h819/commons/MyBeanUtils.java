package org.h819.commons;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.ConvertUtils;
import org.apache.commons.beanutils.Converter;
import org.apache.commons.beanutils.converters.SqlDateConverter;
import org.apache.commons.beanutils.converters.SqlTimestampConverter;
import org.apache.commons.collections.ComparatorUtils;
import org.apache.commons.collections.comparators.ComparableComparator;
import org.apache.commons.collections.comparators.ComparatorChain;
import org.apache.commons.collections.map.ListOrderedMap;
import org.date.bean.UserBean;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Description : TODO()
 * User: h819
 * Date: 14-1-13
 * Time: 下午4:06
 * To change this template use File | Settings | File Templates.
 */
public class MyBeanUtils {

    /**
     * 根据给定的条件，把 list 中的 javabean 排序。
     * 用到了 commons beanutils 和  commons.collections
     *
     * @param list           待排序的 list
     * @param listOrderedMap 排序条件。
     *                       这是一个有序的 list ，排序条件按照加入到 list 的 bean 的属性(map 的 key)的先后顺序排序。
     *                       listOrderedMap 的 key 为待排序的 bean 的属性名称，值为是否按该属性的正序排序，true 为正序，false 为逆序。
     *                       使用方法见本类的 testSortListBeans() 方法例子，使用时注意不要写错 bean 的属性名称。
     * @param <T>            list 中的 bean 类型
     */
    public static <T> void sortListBeans(List<T> list, ListOrderedMap listOrderedMap) {

        int num = listOrderedMap.size();
        ArrayList sortFields = new ArrayList();

        for (int i = 0; i < num; i++) {
            //  System.out.println("key =" + listOrderedMap.get(i) + " , value=" + listOrderedMap.getValue(i));
            Comparator comp = ComparableComparator.getInstance();

            comp = ComparatorUtils.nullLowComparator(comp);  //允许null

            if ((Boolean) listOrderedMap.getValue(i) == false)
                comp = ComparatorUtils.reversedComparator(comp); //逆序

            Comparator cmp = new BeanComparator((String) listOrderedMap.get(i), comp);
            sortFields.add(cmp);
        }

        ComparatorChain multiSort = new ComparatorChain(sortFields);
        Collections.sort(list, multiSort);
    }

    /**
     * 浅拷贝，bean to map
     * -
     * 浅拷贝，如果涉及到 bean 属性是复杂属性，看参考 DtoUtils 转换为 map 的方法
     *
     * @param bean java bean 类型的值对象
     * @return
     */
    public static Map<String, Object> beanToMap(Object bean) {

        if (bean == null) {
            return null;
        }

        Map<String, Object> map = new HashMap();
        try {
            BeanInfo beanInfo = Introspector.getBeanInfo(bean.getClass());
            PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
            for (PropertyDescriptor property : propertyDescriptors) {
                String key = property.getName();
                // 过滤class属性，否则输出结果会有 class 属性
                if (!key.equals("class")) {
                    // 得到property对应的getter方法
                    Object value = property.getReadMethod().invoke(bean);
                    map.put(key, value);
                }
            }
        } catch (Exception e) {
            System.out.println("beanToMap Error " + e);
        }
        return map;


        /**
         *  同 Apache Commons bean , PropertyUtils.describe
         *  但他默认包含 class 属性,去掉
         */

//        try {
//            Map map2 = PropertyUtils.describe(bean);
//            map2.remove("class");
//            return map2;
//        } catch (Exception e) {
//            e.printStackTrace();
//            return null;
//        }
    }

    /**
     * 转换单个 map to bean
     *
     * @param map       待转换的 map
     * @param beanClass 满足 bean 格式，且需要有无参的构造方法; bean 属性的名字应该和 map 的 key 相同
     * @param <T>
     * @return
     */
    public static <T> T mapToBean(Map<String, Object> map, Class<T> beanClass) {

        T o = null;
        try {
            o = beanClass.newInstance();
            mapToBean(map, o);
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }

        return o;
    }

    /**
     * 转换多个 map to bean
     *
     * @param listMap   待转换的 map
     * @param beanClass 满足 bean 格式，且需要有无参的构造方法; bean 属性的名字应该和 map 的 key 相同
     * @param <T>
     * @return
     */
    public static <T> List<T> mapToBeans(Collection<Map<String, Object>> listMap, Class<T> beanClass) {

        List<T> list = new ArrayList(listMap.size());
        try {

            for (Map<String, Object> map : listMap) {
                T o = beanClass.newInstance();
                mapToBean(map, o);
                list.add(o);
            }
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }

        return list;
    }

    /**
     * map to bean
     * 转换过程中，由于属性的类型不同，需要分别转换。
     * java 反射机制，转换过程中属性的类型默认都是 String 类型，否则会抛出异常，而 BeanUtils 项目，做了大量转换工作，比 java 反射机制好用
     * BeanUtils 的 populate 方法，对 Date 属性转换，支持不好，需要自己编写转换器
     *
     * @param map  待转换的 map
     * @param bean 满足 bean 格式，且需要有无参的构造方法; bean 属性的名字应该和 map 的 key 相同
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     */
    private static void mapToBean(Map<String, Object> map, Object bean) throws IllegalAccessException, InvocationTargetException {

        //注册几个转换器
        ConvertUtils.register(new SqlDateConverter(null), java.sql.Date.class);
        ConvertUtils.register(new SqlTimestampConverter(null), java.sql.Timestamp.class);
        //注册一个类型转换器  解决 common-beanutils 为 Date 类型赋值问题
        ConvertUtils.register(new Converter() {
            //  @Override
            public Object convert(Class type, Object value) { // type : 目前所遇到的数据类型。  value :目前参数的值。
                // System.out.println(String.format("value = %s", value));

                if (value == null || value.equals("") || value.equals("null"))
                    return null;
                Date date = null;
                try {
                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    date = dateFormat.parse((String) value);
                } catch (Exception e) {

                    e.printStackTrace();
                }

                return date;
            }

        }, Date.class);

        BeanUtils.populate(bean, map);
    }

    /**
     * 拷贝 source 中的属性到 target 中
     *
     * @param source
     * @param target
     * @param properties
     */
    public static void copyBeanProperties(final Object source, final Object target, final Iterable<String> properties) {

        final BeanWrapper src = new BeanWrapperImpl(source);
        final BeanWrapper trg = new BeanWrapperImpl(target);

        for (final String propertyName : properties) {
            trg.setPropertyValue(propertyName, src.getPropertyValue(propertyName));
        }

    }

    public static void main(String[] args) {

    }

    private void testSortListBeans() {

        ListOrderedMap properties = new ListOrderedMap();
        // properties.put("name", true);
        properties.put("name", false);
        //properties.put("passwd", false);


        UserBean beanEntity1 = new UserBean();
        beanEntity1.setName("jiang");
        beanEntity1.setPasswd("jiang password");

        UserBean beanEntity2 = new UserBean();
        beanEntity2.setName("hui");
        beanEntity2.setPasswd("hui password");

        UserBean beanEntity3 = new UserBean();
        beanEntity3.setName("123");
        beanEntity3.setPasswd("123 password");

        ArrayList<UserBean> alist = new ArrayList();
        alist.add(beanEntity1);
        alist.add(beanEntity2);
        alist.add(beanEntity3);

        sortListBeans(alist, properties);

        for (UserBean bean : alist) {
            System.out.println("name =" + bean.getName() + ", passwd =" + bean.getPasswd());
        }

    }
}
