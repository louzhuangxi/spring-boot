package org.examples.j2se.collection;

import org.examples.j2se.domain.Person;
import org.h819.commons.MyStringUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Description : TODO()
 * User: h819
 * Date: 2016/2/14
 * Time: 16:51
 * To change this template use File | Settings | File Templates.
 */
public class SortSubList {

    /* */


    public static void main(String[] args) {

        SortSubList test = new SortSubList();
        test.testSortList();

    }

    /**
     * 演示如何添加一个元素到指定位置，并把修改该元素的位置属性
     *
     * 注意递归包含关系时的写法 ，详见 TreeNodeEntity.java
     */
    private void testSortList() {

        int add = 1;

        List<Person> list = new ArrayList<Person>();

        list.add(new Person("name1", 0, 0));
        list.add(new Person("name1", 0, 1));
        list.add(new Person("name3", 0, 3));
        list.add(new Person("name2", 0, 2));
        list.add(new Person("name5", 0, 5));
        list.add(new Person("name6", 0, 6));


        System.out.println(MyStringUtils.center(" orignal ", 80, "*"));

        for (Person entity : list)
            System.out.println(String.format("%s  |  %s | %d", entity.getName(), entity.getIndex(), list.indexOf(entity)));


        sortTreeNode(list);

        System.out.println(MyStringUtils.center(" modify by sort ", 80, "*"));


        for (Person entity : list)
            System.out.println(String.format("%s  |  %s | %d", entity.getName(), entity.getIndex(), list.indexOf(entity)));

        Person add1 = new Person("add1", 0, add);

        sortTreeNode(list, add1, add);

        System.out.println(MyStringUtils.center("  sort sub", 80, "*"));

        for (Person entity : list)
            System.out.println(String.format("%s  |  %s | %d", entity.getName(), entity.getIndex(), list.indexOf(entity)));


    }

    /**
     * list 中的 TreeNodeEntity 按照 index 属性排序后，重新设置 TreeNodeEntity 的 index 的值为 TreeNodeEntity 在 list 中的位置。
     *
     * @param list
     */
    private void sortTreeNode(List<Person> list) {
        // Sorting 便于利用 list 的 indexOf 方法
        Collections.sort(list, new Comparator<Person>() {
            @Override
            public int compare(Person node1, Person node2) {
                return Integer.compare(node1.getIndex(), node2.getIndex());
            }
        });

        for (Person entity : list)
            entity.setIndex(list.indexOf(entity));

    }

    /**
     * 添加单个元素到指定位置，并按照元素在 list 中的位置，重新设置元素的 index 属性
     *
     * @param list
     * @param nodeNew 待添加的元素
     * @param index   添加元素到 list 的 index 位置
     */
    private void sortTreeNode(List<Person> list, Person nodeNew, int index) {

        if (index < 0) {             //加在第一位
            index = 0;
            nodeNew.setIndex(index);
        }
        if (index > list.size()) {      //加在最后
            index = list.size();
            nodeNew.setIndex(index);
        }

        list.add(index, nodeNew);

        while (index + 1 < list.size()) {
            list.get(index + 1).setIndex(index + 1);
            index++;

        }


    }
}
