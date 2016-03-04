package org.h819.commons.beanutils.example;


import org.apache.commons.beanutils.*;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.h819.commons.beanutils.bean.CityBean;
import org.h819.commons.beanutils.bean.MyBean;
import org.h819.commons.beanutils.bean.NameBean;
import org.h819.commons.beanutils.bean.PersonBean;

import java.lang.reflect.InvocationTargetException;
import java.sql.Date;
import java.util.*;

/**
 * Description : TODO(beanutils 例子大全)
 * User: h819
 * Date: 14-1-13
 * Time: 下午5:48
 * To change this template use File | Settings | File Templates.
 */
public class BeanutilsExample {
    /**
     * @param args
     */
    public static void main(String[] args) {

        //将一个已经存在的bean包装成一个dynabean, 便于使用
        NameBean nameBean = new NameBean();
        nameBean.setName("shihuan");
        DynaBean wrapper = new WrapDynaBean(nameBean);
        String firstName = (String) wrapper.get("name");
        System.out.println(StringUtils.center("将一个bean包装成一个dynabean, 便于使用",100 ,"="));
        System.out.println(firstName);

        //将Map中的数据，填入到bean中
        Map map = new HashMap();
        map.put("name", "大家好!");
        try {
            BeanUtils.populate(nameBean, map);
            System.out.println(StringUtils.center("将Map中的数据，填入到bean中",100 ,"="));
            System.out.println(nameBean.getName());
        } catch (IllegalAccessException e1) {
            e1.printStackTrace();
        } catch (InvocationTargetException e1) {
            e1.printStackTrace();
        }


        //类型转换
        ConvertUtils.convert("2010-4-30", Date.class);  //转化为时间类
        ConvertUtils.convert("20.02", Double.class);  //转化为double
        System.out.println(StringUtils.center("类型转换",100 ,"="));
        System.out.println(ConvertUtils.convert("2011-8-28", Date.class));
        System.out.println(ConvertUtils.convert("28.09", Double.class));

        //将整个集合的bean的name属性设置为批量修改集合中的bean的name属性

        List dtoCollection = new ArrayList();
        dtoCollection.add(nameBean);
        BeanPropertyValueChangeClosure closure = new BeanPropertyValueChangeClosure("name","替换完成后的值") ;
        CollectionUtils.forAllDo(dtoCollection, closure);
        System.out.println(StringUtils.center("将整个集合的bean的name属性设置为批量修改集合中的bean的name属性",100 ,"="));
        System.out.println(nameBean.getName());

        //过滤整个集合，查找name的值是prepare的所有beans
        List dtoCollectionFilter = new ArrayList();
        nameBean.setName("prepare");
        dtoCollectionFilter.add(nameBean);
        BeanPropertyValueEqualsPredicate predicate = new BeanPropertyValueEqualsPredicate("name", "prepare");
        CollectionUtils.filter(dtoCollectionFilter, predicate);
        System.out.println(StringUtils.center("过滤整个集合，查找name的值是prepare的所有beans",100 ,"="));
        System.out.println(dtoCollectionFilter.size());

        //获取每个bean的person属性中的地址属性中的城市信息，输出到集合中
        MyBean mybean1 = new MyBean();
        PersonBean person1 = new PersonBean();
        mybean1.setPerson(person1);
        CityBean address1 = new CityBean();
        person1.setAddress(address1);
        mybean1.getPerson().getAddress().setCity("锦州");

        MyBean mybean2 = new MyBean();
        PersonBean person2 = new PersonBean();
        mybean2.setPerson(person2);
        CityBean address2 = new CityBean();
        person2.setAddress(address2);
        mybean2.getPerson().getAddress().setCity("沈阳");


        List myCollection = new ArrayList();
        myCollection.add(mybean1);
        myCollection.add(mybean2);
        BeanToPropertyValueTransformer transformer = new BeanToPropertyValueTransformer("person.address.city");
        Collection resultCity = CollectionUtils.collect(myCollection, transformer);
        System.out.println(StringUtils.center("获取每个bean的person属性中的地址属性中的城市信息，输出到集合中",100 ,"="));
        for (Iterator iterCity = resultCity.iterator(); iterCity.hasNext(); ) {
            String citys = (String) iterCity.next();

            System.out.println(citys);
        }


        //dynabean是一种动态的Bean,他的使用非常像Map的操作
        DynaBean car = new LazyDynaBean();
        car.set("carNo", 1);
        car.set("owner", "张三");
        System.out.println(car.get("carNo") + " -- " + car.get("owner"));

        DynaBean carMap = new LazyDynaMap();
        carMap.set("carNo", 2);
        carMap.set("owner", "历史");
        System.out.println(StringUtils.center("dynabean是一种动态的Bean,他的使用非常像Map的操作",100 ,"="));
        System.out.println(carMap.get("carNo") + " -- " + carMap.get("owner"));

        List carList = new LazyDynaList();
        carList.add(3);
        carList.add("王五");
        System.out.println(carList.get(0).toString() + " -- " + carList.get(1).toString());

        BasicDynaClass bdClass = new BasicDynaClass();

        try {
            DynaBean dynaBean = bdClass.newInstance();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        }


    }
}
