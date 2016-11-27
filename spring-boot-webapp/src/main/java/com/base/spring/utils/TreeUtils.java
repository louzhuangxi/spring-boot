package com.base.spring.utils;

import com.base.spring.domain.RoleEntity;
import com.base.spring.domain.TreeEntity;
import com.base.spring.vo.ZTreeNode;

import java.util.Collection;
import java.util.List;

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


    private TreeUtils() {
    }

    /**
     * 把 TreeNodeEntity 类型转换为 ZTreeJsonNode 类型
     *
     * @param treeEntity 待转换的对象
     * @return
     */
    public static ZTreeNode convertToZTreeNode(TreeEntity treeEntity, RoleEntity roleEntity) {

        ZTreeNode parentTemp = new ZTreeNode(0, "name", "url", false, false, false); //创建一个临时的变量，用于保存转换结果。
        convertToZTreeNode(treeEntity, parentTemp, roleEntity);
        return parentTemp.getChildren().get(0); // parentTemp 只有一个子元素，为待转换的 treeNode
    }

    /**
     * 把 TreeNodeEntity 类型转换为 ZTreeJsonNode 类型，并返回其子节点
     *
     * @param treeNode 待转换的对象
     * @return
     */
    public static List<ZTreeNode> getZTreeNodeChildren(TreeEntity treeNode, RoleEntity roleEntity) {
        return convertToZTreeNode(treeNode, roleEntity).getChildren();
    }


    /**
     * 创建已有节点的拷贝，生成新的节点，该节点及其子节点，仅包含集合中有的节点
     *
     * @param sourceTreeEntity 待重新组装的节点
     * @param filterCollection 过滤器，源节点及其子节点，仅包含集合中有的节点
     * @return
     */
    public static TreeEntity getFilterCopyTreeEntityInCollection(TreeEntity sourceTreeEntity, Collection<TreeEntity> filterCollection) {
        TreeEntity parentTemp = new TreeEntity();  // 临时变量 , 该操作不能放在事务中进行，因为 new 操作会自动创建表记录
        createCopyTreeEntity(parentTemp, sourceTreeEntity, filterCollection); // 临时变量赋值
        if (!parentTemp.getChildren().isEmpty())
            return parentTemp.getChildren().get(0); //临时 parentNew 节点，只有一个子节点
        return null;
    }

    /**
     * 创建已有节点的拷贝，生成新的节点
     *
     * @param sourceTreeEntity
     * @return
     */
    public static TreeEntity getCopyTreeEntity(TreeEntity sourceTreeEntity) {
        return getFilterCopyTreeEntityInCollection(sourceTreeEntity, null);
    }


    /**
     * 把 TreeNodeEntity 类型转换为 ZTreeNode 类型，把其值用 parent 带回
     *
     * @param zTreeJsonNodeParentTemp 通过此参数，保存转换后的结果。因为利用了递归，可以通过参数传回转换后的结果
     * @param treeEntity              待转换的对象
     */
    private static void convertToZTreeNode(TreeEntity treeEntity, ZTreeNode zTreeJsonNodeParentTemp, RoleEntity roleEntity) {
        if (treeEntity == null || zTreeJsonNodeParentTemp == null)
            return;

        long id = treeEntity.getId();
//        long pId = 0;
//        if (!treeNodeEntity.isRoot())
//            pId = treeNodeEntity.getParent().getId();

        //  System.out.println(String.format("%d",pId));

        String name = treeEntity.getName();
        String url = treeEntity.getUrl();
        boolean open = false;
        boolean checked = false;

        //是否展开有子节点的父节点
        if (!treeEntity.getChildren().isEmpty())
            open = true;

        //是否属于指定的 RoleEntity
        if (roleEntity != null)
            if (treeEntity.getRoles().contains(roleEntity))
                checked = true;

        ZTreeNode zTreeNode = new ZTreeNode(id, name, url, open, treeEntity.isParentNode(), checked);

        zTreeJsonNodeParentTemp.addChild(zTreeNode);

        if (!treeEntity.getChildren().isEmpty())
            for (TreeEntity child : treeEntity.getChildren())
                convertToZTreeNode(child, zTreeNode, roleEntity);

    }


    /**
     * 创建已有节点的拷贝，生成新的节点 :
     * -
     * 根据已有的节点(该节点可以有子节点，可以递归创建)，创建一个相同的新的节点（和已有节点完全相同），作为临时变量 parentTemp 的一个子节点
     * -
     * 该操作不能放在事务中进行，因为 new 操作会自动创建表记录
     *
     * @param parentTemp       临时变量 parentTemp ，他的子节点就是需要创建的已有节点的拷贝，仅作为递归时携带变量的临时变量
     * @param sourceTreeEntity 已有节点，是数据库里一条数据，可以有子节点。
     * @param filterCollection 仅包含的节点的集合     * @param filterCollection 过滤器，源节点及其子节点，仅包含集合中有的节点
     */
    private static void createCopyTreeEntity(TreeEntity parentTemp, TreeEntity sourceTreeEntity, Collection<TreeEntity> filterCollection) {
        //生成 copyNode 节点本身

        if (filterCollection != null && !filterCollection.isEmpty())
            if (!filterCollection.contains(sourceTreeEntity))
                return;

        TreeEntity copy = new TreeEntity(sourceTreeEntity.getType(), sourceTreeEntity.getName(), sourceTreeEntity.getIndex(), sourceTreeEntity.isParentNode(), parentTemp);
        copy.setUrl(sourceTreeEntity.getUrl());
        copy.setCss(sourceTreeEntity.getCss());
        parentTemp.addChildToLastIndex(copy); // 添加到所有子节点的尾
        parentTemp.setParentNode(true); // 包含子节点，是父节点

        //生成 currentNode 节点的子节点
        if (!sourceTreeEntity.getChildren().isEmpty()) {
            for (TreeEntity child : sourceTreeEntity.getChildren())
                createCopyTreeEntity(copy, child, filterCollection); // 递归
        }

    }

}
