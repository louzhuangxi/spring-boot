package com.example.demo.utils;

import com.example.demo.domain.TreeEntity;
import com.example.demo.vo.JsTreeNode;
import com.example.demo.vo.JsTreeState;

/**
 * Description : TODO(TreeNodeEntity -> ZTreeNode 转换工具，注意非事物状态下，递归方法，返回值的写法)
 * Description : TODO(ztree 本身不提供节点排序功能，按照返回的节点顺序排序，所以需要待转换的 TreeEntity 事先排好序)
 * Description : TODO(在本类中， new TreeEntity 数据库中不会生成新的 Entity 对象)
 * User: h819
 * Date: 2016/2/3
 * Time: 13:38
 * To change this template use File | Settings | File Templates.
 */
public class TreeUtils {

    public static JsTreeNode convertToJsTreeNode(TreeEntity treeEntity) {

        JsTreeNode node = new JsTreeNode();
        node.setId(treeEntity.getId());
        node.setIcon(treeEntity.getCss());
        node.setState(new JsTreeState());
        node.setText(treeEntity.getName());
        node.setParent(treeEntity.getParent().getId());
        return node;
    }

}
