package org.test;

import com.alibaba.fastjson.JSON;
import org.h819.commons.MyStringUtils;
import org.h819.web.spring.jdbc.OrderBean;

import java.io.IOException;
import java.util.List;

/**
 * Description : TODO()
 * User: h819
 * Date: 2017/8/14
 * Time: 11:05
 * To change this template use File | Settings | File Templates.
 */
public class Test4 {

    public static void main(String[] args) throws IOException {

        Test4 t = new Test4();
        OrderBean order1 = new OrderBean("A", OrderBean.Direction.DESC);
        OrderBean order2 = new OrderBean("b", OrderBean.Direction.ASC);
        OrderBean order3 = new OrderBean("c", OrderBean.Direction.ASC);
        OrderBean[] orders = {order1, order2, order3};

        //t.testOrder(orders);
        t.testFastJson(orders);

    }

    private void testOrder(OrderBean... order) {

//   return " order by  " + Arrays.toString(sortParameters).replace("[", "").replace("]", "") + " " + sort;
        //  System.out.println(Arrays.toString(order));
        StringBuilder builder = new StringBuilder(order.length);
        builder.append(" order by ");
        for (OrderBean o : order)
            builder.append(o.getProperty()).append(" ").append(o.getDirection()).append(", ");
        int last = builder.lastIndexOf(",");
        builder.deleteCharAt(last);
        builder.append(" ");
        System.out.println(builder.toString());
    }

    private void testFastJson(OrderBean order) {
        String str = JSON.toJSONString(order);
        System.out.println(str);

        System.out.println(MyStringUtils.center(80, "*"));

        OrderBean order1 = JSON.parseObject(str, OrderBean.class);
        System.out.println(order1.getProperty() + "," + order1.getDirection());
    }

    private void testFastJson(OrderBean[] order) {
        String str = JSON.toJSONString(order);
        System.out.println(str);

        System.out.println(MyStringUtils.center(80, "*"));


        List<OrderBean> order1 = JSON.parseArray(str, OrderBean.class);
        for (OrderBean or : order1)
            System.out.println(or.getProperty() + "," + or.getDirection());
    }

}
