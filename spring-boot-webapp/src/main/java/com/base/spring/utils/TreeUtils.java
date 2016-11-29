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


    /**
     * 应用包含和不包含的时候需要注意：
     * 1.
     * includes 和 excludes 不能同时应用
     * includes 和 excludes 都是返回的新的 TreeEntity 无法连续进行 contains 计算，后一次判断无法进行
     * 2.
     * 可以判断其他的元素是否包含，此时可以 includes 和 excludes 可以同时应用
     */

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
     * 把 TreeNodeEntity 集合类型转换为 ZTreeJsonNode 类型，并用一个临时的节点返回
     *
     * @param zTreeNodeName 临时节点的名字
     * @param treeEntity
     * @param roleEntity
     * @return
     */
    public static ZTreeNode convertToZTreeNode(String zTreeNodeName, List<TreeEntity> treeEntity, RoleEntity roleEntity) {
        ZTreeNode parentTemp = new ZTreeNode(0, zTreeNodeName, "url", true, true, false); //创建一个临时的变量，用于保存转换结果。
        for (TreeEntity tree : treeEntity) {
            parentTemp.addChild(convertToZTreeNode(tree, roleEntity));
        }
        return parentTemp; // parentTemp 只有一个子元素，为待转换的 treeNode
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
     * @param filterIncludes   过滤器，源节点及其子节点，仅包含集合中有的节点
     * @return
     */
    public static TreeEntity createCopyTreeEntityByFilterIncludes(TreeEntity sourceTreeEntity, Collection<TreeEntity> filterIncludes) {
        TreeEntity parentTemp = new TreeEntity();  // 临时变量 , 该操作不能放在事务中进行，因为 new 操作会自动创建表记录
        createCopyTreeEntityByFilterIncludes(parentTemp, sourceTreeEntity, filterIncludes); // 临时变量赋值
        if (!parentTemp.getChildren().isEmpty())
            return parentTemp.getChildren().get(0); //临时 parentNew 节点，只有一个子节点
        return null;
    }

    /**
     * 不包含
     *
     * @param sourceTreeEntity 待重新组装的节点
     * @param filterExcludes   过滤器，源节点及其子节点，不包含集合中有的节点
     * @return
     */
    public static TreeEntity createCopyTreeEntityByFilterExcludes(TreeEntity sourceTreeEntity, Collection<TreeEntity> filterExcludes) {
        TreeEntity parentTemp = new TreeEntity();  // 临时变量 , 该操作不能放在事务中进行，因为 new 操作会自动创建表记录
        createCopyTreeEntityByFilterExcludes(parentTemp, sourceTreeEntity, filterExcludes); // 临时变量赋值
        if (!parentTemp.getChildren().isEmpty())
            return parentTemp.getChildren().get(0); //临时 parentNew 节点，只有一个子节点
        return null;
    }

    /**
     * 不包含
     * 仅为了和 createCopyTreeEntityByFilterIncludes 一起应用时，createCopyTreeEntityByFilterIncludes 生成了新的对象，而无法判断时使用
     *
     * @param sourceTreeEntity   待重新组装的节点
     * @param filterNameExcludes 过滤器，源节点及其子节点的 name 属性，不包含集合中有的节点
     * @return
     */
    public static TreeEntity createCopyTreeEntityByFilterNameExcludes(TreeEntity sourceTreeEntity, Collection<String> filterNameExcludes) {
        TreeEntity parentTemp = new TreeEntity();  // 临时变量 , 该操作不能放在事务中进行，因为 new 操作会自动创建表记录
        createCopyTreeEntityByFilterNameExcludes(parentTemp, sourceTreeEntity, filterNameExcludes); // 临时变量赋值
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
    public static TreeEntity createCopyTreeEntity(TreeEntity sourceTreeEntity) {
        return createCopyTreeEntityByFilterIncludes(sourceTreeEntity, null);
    }


    /**
     * 把 TreeNodeEntity 类型转换为 ZTreeNode 类型，把其值用 parent 带回
     *
     * @param zTreeNodeParentTemp 通过此参数，保存转换后的结果。因为利用了递归，可以通过参数传回转换后的结果
     * @param treeEntity          待转换的对象
     * @param roleEntity          如果拥有此权限，树节点设置为选中状态
     */
    private static void convertToZTreeNode(TreeEntity treeEntity, ZTreeNode zTreeNodeParentTemp, RoleEntity roleEntity) {
        if (treeEntity == null || zTreeNodeParentTemp == null)
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

        zTreeNodeParentTemp.addChild(zTreeNode);

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
     * @param filterIncludes   过滤掉源节点及其子节点中，不包含在集合中的节点
     */
    private static void createCopyTreeEntityByFilterIncludes(TreeEntity parentTemp, TreeEntity sourceTreeEntity, Collection<TreeEntity> filterIncludes) {
        //生成 copyNode 节点本身

        if (sourceTreeEntity == null)
            return;


        if (filterIncludes != null)
            if (!filterIncludes.contains(sourceTreeEntity))
                return;

        TreeEntity copy = new TreeEntity(sourceTreeEntity.getType(), sourceTreeEntity.getName(), sourceTreeEntity.getIndex(), sourceTreeEntity.isParentNode(), parentTemp);
        copy.setUrl(sourceTreeEntity.getUrl());
        copy.setCss(sourceTreeEntity.getCss());
        parentTemp.addChildToLastIndex(copy); // 添加到所有子节点的尾
        parentTemp.setParentNode(true); // 包含子节点，是父节点

        //生成 currentNode 节点的子节点
        if (!sourceTreeEntity.getChildren().isEmpty()) {
            for (TreeEntity child : sourceTreeEntity.getChildren())
                createCopyTreeEntityByFilterIncludes(copy, child, filterIncludes); // 递归
        }

    }

    /**
     * 不包含，和 createCopyTreeEntityByFilterIncludes 相反
     *
     * @param parentTemp
     * @param sourceTreeEntity
     * @param filterExcludes   节点本身（含子节点）去除
     */
    private static void createCopyTreeEntityByFilterExcludes(TreeEntity parentTemp, TreeEntity sourceTreeEntity, Collection<TreeEntity> filterExcludes) {
        //生成 copyNode 节点本身
        if (sourceTreeEntity == null)
            return;

        if (filterExcludes != null)
            if (filterExcludes.contains(sourceTreeEntity)) {
                //System.out.println("contains excludes");
                return;
            }

        TreeEntity copy = new TreeEntity(sourceTreeEntity.getType(), sourceTreeEntity.getName(), sourceTreeEntity.getIndex(), sourceTreeEntity.isParentNode(), parentTemp);
        copy.setUrl(sourceTreeEntity.getUrl());
        copy.setCss(sourceTreeEntity.getCss());
        parentTemp.addChildToLastIndex(copy); // 添加到所有子节点的尾
        parentTemp.setParentNode(true); // 包含子节点，是父节点

        //生成 currentNode 节点的子节点
        if (!sourceTreeEntity.getChildren().isEmpty()) {
            for (TreeEntity child : sourceTreeEntity.getChildren())
                createCopyTreeEntityByFilterExcludes(copy, child, filterExcludes); // 递归
        }
    }

    /**
     * 不包含
     * 仅为了和 createCopyTreeEntityByFilterIncludes 一起应用时，createCopyTreeEntityByFilterIncludes 生成了新的对象，而无法判断时使用
     *
     * @param parentTemp
     * @param sourceTreeEntity
     * @param filterNameExcludes
     */
    private static void createCopyTreeEntityByFilterNameExcludes(TreeEntity parentTemp, TreeEntity sourceTreeEntity, Collection<String> filterNameExcludes) {
        //生成 copyNode 节点本身
        if (sourceTreeEntity == null)
            return;

        if (filterNameExcludes != null)
            if (filterNameExcludes.contains(sourceTreeEntity.getName())) {
                //System.out.println("contains excludes");
                return;
            }

        TreeEntity copy = new TreeEntity(sourceTreeEntity.getType(), sourceTreeEntity.getName(), sourceTreeEntity.getIndex(), sourceTreeEntity.isParentNode(), parentTemp);
        copy.setUrl(sourceTreeEntity.getUrl());
        copy.setCss(sourceTreeEntity.getCss());
        parentTemp.addChildToLastIndex(copy); // 添加到所有子节点的尾
        parentTemp.setParentNode(true); // 包含子节点，是父节点

        //生成 currentNode 节点的子节点
        if (!sourceTreeEntity.getChildren().isEmpty()) {
            for (TreeEntity child : sourceTreeEntity.getChildren())
                createCopyTreeEntityByFilterNameExcludes(copy, child, filterNameExcludes); // 递归
        }
    }

}
