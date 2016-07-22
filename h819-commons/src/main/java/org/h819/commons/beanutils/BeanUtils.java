package org.h819.commons.beanutils;

import org.apache.commons.beanutils.BeanUtilsBean;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Array;
import java.lang.reflect.InvocationTargetException;
import java.sql.Timestamp;
import java.util.Collection;
import java.util.Date;
import java.util.LinkedList;
import java.util.Map;
import java.util.Map.Entry;

/**
 * Description : TODO()
 * User: h819
 * Date: 2015/5/13
 * Time: 12:56
 * To change this template use File | Settings | File Templates.
 */
public class BeanUtils {


    public static void copyBeanProperties(final Object source, final Object target, final Iterable<String> properties) {

        final BeanWrapper src = new BeanWrapperImpl(source);
        final BeanWrapper trg = new BeanWrapperImpl(target);

        for (final String propertyName : properties) {
            trg.setPropertyValue(propertyName, src.getPropertyValue(propertyName));
        }

    }

    /**
     * 可以脱离 hibernate 存在，对一个bean进行深度复制，所有的属性节点全部会被复制，级联层次问题没有解决，会出现死循环问题 . 日后有时间，按照 DTOUtils类进行改写
     *
     * @param source
     * @return
     * @throws InstantiationException
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     * @see [类、类#方法、类#成员]
     */
    @SuppressWarnings({"unchecked", "rawtypes"})
    public static <T> T deepCopyBean(T source) {

        if (source == null) {
            return null;
        }

        try {
            if (source instanceof Collection) {
                return (T) deepCopyCollection((Collection) source);
            }
            if (source.getClass().isArray()) {
                return (T) deepCopyArray(source);
            }
            if (source instanceof Map) {
                return (T) deepCopyMap((Map) source);
            }
            if (source instanceof Date) {
                return (T) new Date(((Date) source).getTime());
            }
            if (source instanceof Timestamp) {
                return (T) new Timestamp(((Timestamp) source).getTime());
            }
            // 基本类型直接返回原值
            if (source.getClass().isPrimitive() || source instanceof String || source instanceof Boolean
                    || Number.class.isAssignableFrom(source.getClass())) {
                return source;
            }
            if (source instanceof StringBuilder) {
                return (T) new StringBuilder(source.toString());
            }
            if (source instanceof StringBuffer) {
                return (T) new StringBuffer(source.toString());
            }
            Object dest = source.getClass().newInstance();
            BeanUtilsBean bean = BeanUtilsBean.getInstance();
            PropertyDescriptor[] origDescriptors = bean.getPropertyUtils().getPropertyDescriptors(source);
            for (int i = 0; i < origDescriptors.length; i++) {
                String name = origDescriptors[i].getName();
                if ("class".equals(name)) {
                    continue;
                }

                if (bean.getPropertyUtils().isReadable(source, name) && bean.getPropertyUtils().isWriteable(dest, name)) {
                    try {
                        Object value = deepCopyBean(bean.getPropertyUtils().getSimpleProperty(source, name));
                        bean.getPropertyUtils().setSimpleProperty(dest, name, value);
                    } catch (NoSuchMethodException e) {
                    }
                }
            }
            return (T) dest;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    private static Collection deepCopyCollection(Collection source)
            throws InstantiationException, IllegalAccessException {
        Collection dest = new LinkedList();
        for (Object o : source) {
            dest.add(deepCopyBean(o));
        }
        return dest;
    }

    private static Object deepCopyArray(Object source)
            throws InstantiationException, IllegalAccessException, ArrayIndexOutOfBoundsException, IllegalArgumentException {
        int length = Array.getLength(source);
        Object dest = Array.newInstance(source.getClass().getComponentType(), length);
        if (length == 0) {
            return dest;
        }
        for (int i = 0; i < length; i++) {
            Array.set(dest, i, deepCopyBean(Array.get(source, i)));
        }
        return dest;
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    private static Map deepCopyMap(Map source)
            throws InstantiationException, IllegalAccessException {
        Map dest = source.getClass().newInstance();
        for (Object o : source.entrySet()) {
            Entry e = (Entry) o;
            dest.put(deepCopyBean(e.getKey()), deepCopyBean(e.getValue()));
        }
        return dest;
    }

}
