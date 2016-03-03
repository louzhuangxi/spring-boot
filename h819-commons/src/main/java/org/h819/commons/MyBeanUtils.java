package org.h819.commons;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.collections.ComparatorUtils;
import org.apache.commons.collections.comparators.ComparableComparator;
import org.apache.commons.collections.comparators.ComparatorChain;
import org.apache.commons.collections.map.ListOrderedMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.util.*;

/**
 * Description : TODO()
 * User: h819
 * Date: 14-1-13
 * Time: 下午4:06
 * To change this template use File | Settings | File Templates.
 */
public class MyBeanUtils {


    private static Logger logger = LoggerFactory.getLogger(MyBeanUtils.class);

    /**
     * 根据给定的条件，把 list 中的 javabean 排序。
     * 用到了 commons beanutils 和  commons.collections
     *
     * @param list           待排序的 list
     * @param listOrderedMap 排序条件。
     *                       这是一个有序的 list ，排序条件按照加入到 list 的bean的属性(map 的 key)的先后顺序排序。
     *                       listOrderedMap 的 key 为待排序的bean的属性名称，值为是否按该属性的正序排序，true 为正序，false 为逆序。
     *                       使用方法见本类的 main 函数例子，使用时注意不要写错 bean 的属性名称。
     * @param <V>            list 中的 bean 类型
     */
    public static <V> void sortListBeans(List<V> list, ListOrderedMap listOrderedMap) {

//        ListOrderedMap properties = new ListOrderedMap();
//        properties.put("hello", "hello1");
//        properties.put("12", "lsff");
//        properties.put("abs", true);
        int num = listOrderedMap.size();

        ArrayList sortFields = new ArrayList();

        for (int i = 0; i < num; i++) {
            //  System.out.println("key =" + listOrderedMap.get(i) + " , value=" + listOrderedMap.getValue(i));
            Comparator mycmp = ComparableComparator.getInstance();

            mycmp = ComparatorUtils.nullLowComparator(mycmp);  //允许null

            if ((Boolean) listOrderedMap.getValue(i) == false)
                mycmp = ComparatorUtils.reversedComparator(mycmp); //逆序

            Comparator cmp = new BeanComparator((String) listOrderedMap.get(i), mycmp);
            sortFields.add(cmp);
        }

        ComparatorChain multiSort = new ComparatorChain(sortFields);
        Collections.sort(list, multiSort);
    }


    /**
     * bean to map
     *
     * @param bean java bean 类型的值对象
     * @return
     */

    //Apache Commons bean : Map<String, String> objectAsMap = BeanUtils.describe(fieldNoteBook);
    //BeanUtils 的 describe 的方法就不可以 ，它的返回值类型为 <String, String> 。
    // 我现在需要的是 <String, Object> ，等 BeanUtils 升级后是否改变这个参数(目前 1.9.2 版本)
    //但他默认包含 class 属性，这里自己实现，以加深理解
    public static Map<String, Object> toMap(Object bean) {

        if (bean == null) {
            return null;
        }

        Map<String, Object> map = new HashMap<String, Object>();
        try {
            BeanInfo beanInfo = Introspector.getBeanInfo(bean.getClass());
            PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
            for (PropertyDescriptor property : propertyDescriptors) {
                String key = property.getName();
                // 过滤class属性，否则输出结果会有 class 属性
                if (!key.equals("class")) {
                    // 得到property对应的getter方法
                    Method getter = property.getReadMethod();
                    Object value = getter.invoke(bean);
                    map.put(key, value);
                }
            }
        } catch (Exception e) {
            System.out.println("transBean2Map Error " + e);
        }
        return map;
    }

    public static void main(String[] args) {

//        ListOrderedMap properties = new ListOrderedMap();
//        // properties.put("name", true);
//        properties.put("name", false);
//        //properties.put("passwd", false);
//
//
//        BeanEntity beanEntity1 = new BeanEntity();
//        beanEntity1.setName("jiang");
//        beanEntity1.setPasswd("jiang password");
//
//        BeanEntity beanEntity2 = new BeanEntity();
//        beanEntity2.setName("hui");
//        beanEntity2.setPasswd("hui password");
//
//        BeanEntity beanEntity3 = new BeanEntity();
//        beanEntity3.setName("123");
//        beanEntity3.setPasswd("123 password");
//
//        ArrayList<BeanEntity> alist = new ArrayList();
//        alist.add(beanEntity1);
//        alist.add(beanEntity2);
//        alist.add(beanEntity3);
//
//        sortListBeans(alist, properties);
//
//        for (BeanEntity bean : alist) {
//            System.out.println("name =" + bean.getName() + ", passwd =" + bean.getPasswd());
//        }


    }

}
