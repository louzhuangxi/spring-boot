package org.h819.web.jdbc;

import java.util.Map;

/**
 * Description : TODO()
 * User: h819
 * Date: 2018/4/11
 * Time: 10:15
 * To change this template use File | Settings | File Templates.
 */
public class TestDbUtils {
    private static String show = "show databases";
    private static String dataSorceA = "a";
    private static String dataSorceB = "b";

    public static void main(String args[]) {
        TestDbUtils test = new TestDbUtils();
        test.test();

    }

    /**
     * 多数据源测试
     */
    private static void testMultiDataSource() {

    }

    private void test() {
        for (Map<String, Object> map : MyDbUtils.getListMap(show))
            loopMap(map);
    }

    private void loopMap(Map<String, Object> map) {
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            String key = entry.getKey();
            Object value = entry.getValue();
            System.out.println("key = " + key + " , value = " + value);// ...
        }
    }

    private void testEmptyArrays() {
        int[] ints = new int[0];
        System.out.println(ints.length);
        System.out.println(ints);

    }

}
