package com.base.spring.utils;

import com.alibaba.fastjson.JSON;
import com.base.spring.domain.TreeNodeEntity;
import com.base.spring.domain.TreeNodeType;
import com.base.spring.dto.FueluxTreeJsonNode;
import com.base.spring.dto.FueluxTreeJsonNodeAttr;
import com.base.spring.dto.FueluxTreeNodeType;
import com.google.common.collect.Lists;
import org.apache.commons.lang3.StringUtils;

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
public class Test2 {

    /*
    public static void main(String[] args) {

        Test2 t = new Test2();
        //  t.testSubList();
        //t.testSortList();
          t.testFueluxTreeNode();
    }
    */

    private void testFueluxTreeNode(){

        FueluxTreeJsonNode node = new FueluxTreeJsonNode("name", FueluxTreeNodeType.folder);
        FueluxTreeJsonNodeAttr attr = new FueluxTreeJsonNodeAttr(12l,true);
        attr.setCssClass("css");
        attr.setDataIcon("data icon");
        node.setAttr(attr);

        String str = JSON.toJSONString(node);

        System.out.println(str);

        System.out.println(str.replaceAll("\"dataIcon\":","\"data-icon\":"));
    }

    private void testSubList() {

        List<String> list = Lists.newArrayList("1", "2", "2");

        System.out.println(list.subList(0, 2));
        System.out.println(list.subList(1, 2));
        System.out.println(list.subList(2, 2));


    }

    private void testSortList() {

        int add = 9;

        List<TreeNodeEntity> list = Lists.newArrayList();

        TreeNodeEntity parent = new TreeNodeEntity(TreeNodeType.Menu, "root", 0, 0, false, null);

        list.add(new TreeNodeEntity(TreeNodeType.Menu, "name1", 0, 0, false, parent));
        list.add(new TreeNodeEntity(TreeNodeType.Menu, "name1", 0, 1, false, parent));
        list.add(new TreeNodeEntity(TreeNodeType.Menu, "name3", 0, 3, false, parent));
        list.add(new TreeNodeEntity(TreeNodeType.Menu, "name2", 0, 2, false, parent));
        list.add(new TreeNodeEntity(TreeNodeType.Menu, "name5", 0, 5, false, parent));
        list.add(new TreeNodeEntity(TreeNodeType.Menu, "name6", 0, 6, false, parent));


        System.out.println(StringUtils.center(" orignal ", 80, "*"));

        for (TreeNodeEntity entity : list)
            System.out.println(String.format("%s  |  %s | %d", entity.getName(), entity.getIndex(), list.indexOf(entity)));


        sortTreeNode(list);

        System.out.println(StringUtils.center(" modify by sort ", 80, "*"));


        for (TreeNodeEntity entity : list)
            System.out.println(String.format("%s  |  %s | %d", entity.getName(), entity.getIndex(), list.indexOf(entity)));

        TreeNodeEntity add1 = new TreeNodeEntity(TreeNodeType.Menu, "add1", 0, add, false, parent);

        sortTreeNode(list, add1, add);

        System.out.println(StringUtils.center("  sort sub", 80, "*"));

        for (TreeNodeEntity entity : list)
            System.out.println(String.format("%s  |  %s | %d", entity.getName(), entity.getIndex(), list.indexOf(entity)));


    }

    /**
     * list 中的 TreeNodeEntity 按照 index 属性排序后，重新设置 TreeNodeEntity 的 index 的值为 TreeNodeEntity 在 list 中的位置。
     *
     * @param list
     */
    private void sortTreeNode(List<TreeNodeEntity> list) {
        // Sorting 便于利用 list 的 indexOf 方法
        Collections.sort(list, new Comparator<TreeNodeEntity>() {
            @Override
            public int compare(TreeNodeEntity node1, TreeNodeEntity node2) {
                return Integer.compare(node1.getIndex(), node2.getIndex());
            }
        });

        for (TreeNodeEntity entity : list)
            entity.setIndex(list.indexOf(entity));

    }

    /**
     * 添加单个元素到指定位置，并按照元素在 list 中的位置，重新设置元素的 index 属性
     * @param list
     * @param nodeNew 待添加的元素
     * @param index 添加元素到 list 的 index 位置
     */
    private void sortTreeNode(List<TreeNodeEntity> list, TreeNodeEntity nodeNew, int index) {

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
