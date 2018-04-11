package org.example.ztree.utils;


import org.example.ztree.domain.TreeNodeEntity;
import org.example.ztree.dto.ZTreeJsonNode;

import java.util.List;

/**
 * Description : TODO(TreeNodeEntity -> ZTreeJsonNode 转换工具，注意非事物状态下，递归方法，返回值的写法)
 * Description : TODO(ztree 本身不提供节点排序功能，按照返回的节点顺序排序，所以需要待转换的 TreeNodeEntity 事先排好序)
 * User: h819
 * Date: 2016/2/3
 * Time: 13:38
 * To change this template use File | Settings | File Templates.
 */
public class ZTreeUtils {

    private ZTreeUtils() {
    }

    /**
     * 把 TreeNodeEntity 类型转换为 ZTreeJsonNode 类型
     *
     * @param treeNode 待转换的对象
     * @return
     */
    public static ZTreeJsonNode getJsonData(TreeNodeEntity treeNode) {

        ZTreeJsonNode parentTemp = new ZTreeJsonNode("temp"); //创建一个临时的变量，用于保存转换结果。
        convertToJsonData(parentTemp, treeNode);
        return parentTemp.getChildren().get(0); // parentTemp 只有一个子元素，为待转换的 treeNode
    }

    /**
     * 把 TreeNodeEntity 类型转换为 ZTreeJsonNode 类型，并返回其子节点
     *
     * @param treeNode 待转换的对象
     * @return
     */
    public static List<ZTreeJsonNode> getJsonDataChildren(TreeNodeEntity treeNode) {
        return getJsonData(treeNode).getChildren();
    }


    /**
     * 根据已有的节点(可以有子节点)，创建一个相同的新的节点。可以递归创建
     * <p>
     * 该方法不能象 getJsonData() 方法一样，通过临时创建的 父对象返回，因为即使是临时创建 new ，在事务中也会创建表记录。
     *
     * @param currentNode 已有节点，是数据库里一条数据，可以有子节点。在数据库中创建一个新的记录，如果有子节点，可以递归创建。在事务环境中，new 操作会自动创建表记录
     * @param parent      已有节点的父节点，最终的结果是，parent 中新生成了一个新的和 currentNode 一样的子节点。parent 参数需要外部提供。
     */
    public static void createCopyNode(TreeNodeEntity parent, TreeNodeEntity currentNode) {

        //生成 currentNode 节点本身，在数据库中对应一条新的记录
        TreeNodeEntity newTree = new TreeNodeEntity(currentNode.getType(), currentNode.getName(), currentNode.getIndex(), currentNode.isParentNode(), parent);
        newTree.setUrl(currentNode.getUrl());
        newTree.setCss(currentNode.getCss());
        parent.addChildToLastIndex(newTree); // 添加到所有子节点的尾部
        parent.setParentNode(true);

        //生成 currentNode 节点的子节点
        if (!currentNode.getChildren().isEmpty()) {
            for (TreeNodeEntity child : currentNode.getChildren())
                createCopyNode(newTree, child); // 递归
        }

    }

    /**
     * 把 TreeNodeEntity 类型转换为 ZTreeJsonNode 类型，把其值用 parent 带回
     *
     * @param zTreeJsonNodeParentTemp 通过此参数，保存转换后的结果。因为利用了递归，可以通过参数传回转换后的结果
     * @param treeNodeEntity          待转换的对象
     */
    private static void convertToJsonData(ZTreeJsonNode zTreeJsonNodeParentTemp, TreeNodeEntity treeNodeEntity) {
        if (treeNodeEntity == null || zTreeJsonNodeParentTemp == null)
            return;

        long id = treeNodeEntity.getId();
        long pId = 0;
        if (!treeNodeEntity.isRoot())
            pId = treeNodeEntity.getParent().getId();

        //  System.out.println(String.format("%d",pId));

        String name = treeNodeEntity.getName();
        String url = treeNodeEntity.getUrl();
        boolean open = false;

        //展开有子节点的父节点
        if (!treeNodeEntity.getChildren().isEmpty())
            open = true;

        boolean isParent = treeNodeEntity.isParentNode();

        ZTreeJsonNode zTreeNode = new ZTreeJsonNode(id, name, url, open, isParent);

        zTreeJsonNodeParentTemp.addChild(zTreeNode);

        if (!treeNodeEntity.getChildren().isEmpty())
            for (TreeNodeEntity child : treeNodeEntity.getChildren())
                convertToJsonData(zTreeNode, child);

    }

}
