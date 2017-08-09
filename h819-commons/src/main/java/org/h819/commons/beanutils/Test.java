package org.h819.commons.beanutils;

import org.apache.commons.beanutils.PropertyUtils;
import org.h819.commons.MyBeanUtils;
import org.date.bean.CityBean;

import java.lang.reflect.InvocationTargetException;
import java.util.Map;

/**
 * Description : TODO()
 * User: h819
 * Date: 2016/11/21
 * Time: 15:55
 * To change this template use File | Settings | File Templates.
 */
public class Test {

    public static void main(String[] ags) throws IllegalAccessException, NoSuchMethodException, InvocationTargetException {
        CityBean bean = new CityBean();
        bean.setCity("Beijing");



        System.out.println(MyBeanUtils.beanToMap(bean));
        Map map = PropertyUtils.describe(bean);
        map.remove("class");
        System.out.println(map);



    }
}
