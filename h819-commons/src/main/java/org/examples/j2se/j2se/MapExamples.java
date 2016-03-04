package org.examples.j2se.j2se;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Description : TODO()
 * User: h819
 * Date: 14-4-9
 * Time: 下午4:38
 * To change this template use File | Settings | File Templates.
 */
public class MapExamples {


    /**
     * map 循环例子
     */
    private void mapList() {

        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        //list.add()...
        for (Map<String, Object> s : list) {
            for (Map.Entry<String, Object> entry : s.entrySet()) {
                System.out.println("Key = " + entry.getKey() + ", Value = " + entry.getValue());
            }
            System.out.println(StringUtils.center("break", 80, "="));
        }
    }
}
