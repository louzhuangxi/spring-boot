package com.base.spring.utils;

import com.alibaba.fastjson.JSON;
import com.base.spring.domain.TreeEntity;
import com.base.spring.vo.FueluxTreeJsonNode;
import com.base.spring.vo.FueluxTreeNodeType;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Description : TODO(TreeNodeEntity -> FueluxTreeJsonNode 转换工具，注意非事物状态下，递归方法，返回值的写法)
 * Description : TODO(同 ztree ，FueluxTree 本身不提供节点排序功能，按照返回的节点顺序排序，所以需要待转换的 TreeNodeEntity 事先排好序)
 * User: h819
 * Date: 2016/2/3
 * Time: 13:38
 * To change this template use File | Settings | File Templates.
 */
public class FueluxTreeUtils {


    /**
     * 如果构造，初始化 fluelux tree 的时候，默认选中某些节点 ?
     */

    /**
     * 静态调用
     */
    private FueluxTreeUtils() {
    }


    /**
     * 把 TreeNodeEntity 类型转换为 FueluxTreeJsonNode 类型 json string
     *
     * @param treeNodeEntity 待转换的对象
     */
    public static String getFueluxTreeJson(TreeEntity treeNodeEntity) {
        String str = JSON.toJSONString(toFueluxTree(treeNodeEntity));
        return str.replaceAll("\"dataIcon\":", "\"data-icon\":");  // 替换为 Fuelux Tree 规定的格式

    }

    /**
     * 把 TreeNodeEntity 类型转换为 FueluxTreeJsonNode 类型 json string
     *
     * @param treeNodeEntities 待转换的对象
     */
    public static String getFueluxTreeJson(Collection<TreeEntity> treeNodeEntities) {
        String str = JSON.toJSONString(toFueluxTree(treeNodeEntities));
        return str.replaceAll("\"dataIcon\":", "\"data-icon\":");   // 替换为 Fuelux Tree 规定的格式

    }

    /**
     * 把 TreeNodeEntity 类型转换为 FueluxTreeJsonNode 类型
     * fuelux tree 解析的 json 字符串，都是集合类型，所以单个对象，也包装为集合类型
     *
     * @param treeNodeEntity 待转换的对象
     */
    private static List<FueluxTreeJsonNode> toFueluxTree(TreeEntity treeNodeEntity) {
        if (treeNodeEntity == null)
            return new ArrayList<>(0);

        List list = new ArrayList<>(1);
        list.add(treeNodeEntity);

        return toFueluxTree(list);

    }

    /**
     * 把 TreeNodeEntity 类型转换为 FueluxTreeJsonNode 类型
     *
     * @param treeNodeEntities 待转换的对象
     */
    private static List<FueluxTreeJsonNode> toFueluxTree(Collection<TreeEntity> treeNodeEntities) {
        if (treeNodeEntities == null || treeNodeEntities.isEmpty())
            return new ArrayList<>(0);

        List<FueluxTreeJsonNode> FueluxTreeNodes = new ArrayList<>(treeNodeEntities.size());

        for (TreeEntity treeNode : treeNodeEntities)
            if (treeNode.isParentNode())  // 节点默认不选中
                FueluxTreeNodes.add(new FueluxTreeJsonNode(treeNode.getName(), FueluxTreeNodeType.folder, treeNode.getId(), false));
            else
                FueluxTreeNodes.add(new FueluxTreeJsonNode(treeNode.getName(), FueluxTreeNodeType.item, treeNode.getId(), true));

        return FueluxTreeNodes;

    }

}
