package org.examples.Test;

import org.examples.j2se.Lombok.Lombok;

/**
 * Description : TODO()
 * User: h819
 * Date: 2017/8/30
 * Time: 10:45
 * To change this template use File | Settings | File Templates.
 */
public class Test {


    public static void main(String[] args) {

        //        bean.setAge(1);
//        bean.setIndex(100);
//        bean.setName("jiang");
        Lombok bean = Lombok.builder().age(1).name("jiang").build();
        System.out.println(bean.toString());

    }
}
