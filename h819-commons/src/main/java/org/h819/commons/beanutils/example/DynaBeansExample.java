package org.h819.commons.beanutils.example;


import org.apache.commons.beanutils.DynaBean;
import org.apache.commons.beanutils.LazyDynaBean;
import org.apache.commons.beanutils.LazyDynaMap;
import org.h819.commons.beanutils.bean.UserEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Map;


/**
 * <p/>
 * 构造任意属性的 bean，做为 VO 到前端，便于使用 jstl
 * <p/>
 * 解决了定义好的 bean 不能调整属性，而把所有的属性都传递到前端的问题
 *
 * 性能提高，尤其是bean包含大量的前端用不到的数据的问题。
 * <p/>
 * 另外一个例子见 com.h819.web.commons.beanutils.example.BeanUtilsExample.java
 */
@Controller
public class DynaBeansExample {

    @RequestMapping(value = "/website/test.html")
    public String testDynaBeanController(Model model) {

        //创建动态 bean
        DynaBean dynaBean = new LazyDynaBean();

        //字符串属性，通过属性名 name 调用
        dynaBean.set("name", "Peter Collinson"); //simple

        //数字属性 ,通过属性名 gender 调用
        dynaBean.set("gender", new Integer(1));  //simple

        //bean 属性,通过属性名 bean 调用
        UserEntity beanExample = new UserEntity();
        beanExample.setName("name string."); //simple
        dynaBean.set("bean", beanExample);

        //map 属性，通过 customer 和 title 调用
        dynaBean.set("customer", "title", "Mr");      // mapped
        dynaBean.set("customer", "surname", "Smith"); // mapped

        //可以指定属性顺序,通过 address 和 顺序号调用
        /**
         * 此方式无法通过 jstl 调用，不使用
         * */
        dynaBean.set("address", 0, "addressLine1");     // indexed
        dynaBean.set("address", 1, "addressLine2");     // indexed
        dynaBean.set("address", 2, "addressLine3");     // indexed

        //其他用法
        /**
         * 可以转换成  map ， 用法同 LazyDynaBean
         */
        LazyDynaMap dynaMapBean = new LazyDynaMap();
        dynaMapBean.set("name", "Peter Collinson"); //simple;
        Map myMap = dynaMapBean.getMap();   // retrieve the Map


        //传递到前端
        model.addAttribute("test", dynaBean);
        return "2760/jsp/test";

        /**
         * 前端调用
         */
        //页面 jstl 读取，不需要，用引入任何类，仅引入 jstl 标签即可使用
        //注意，使用 map 这个关键字
        //http://commons.apache.org/proper/commons-beanutils/apidocs/org/apache/commons/beanutils/BasicDynaBean.html
        //官方 doc 中，getMap() 方法解释处有说明

        /**
           调用代码 : test 为 bean 名称，如果是字符串 $ {map.name} 即可
         $ {test.map.name} <br />
         $ {test.map.gender} <br />
         $ {test.map.bean.name} <br />
         $ {test.map.customer.title} <br />
         $ {test.map.customer.surname} <br />

         */

        //http://www.programgo.com/article/85353617047/
    }

}