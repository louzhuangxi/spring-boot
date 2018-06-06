package com.example.demo.domain;

import lombok.*;
import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.springframework.util.Assert;

import javax.persistence.*;
import java.util.*;

/**
 * Description : TODO(树状结构, type 不同，就代表不同类型的树)
 * User: h819
 * Date: 14-7-7
 * Time: 下午3:18
 * To change this template use File | Settings | File Templates.
 */
@Data
@Entity
@Table(name = "base_tree")
@NamedEntityGraphs({
        @NamedEntityGraph(name = "tree.parent", attributeNodes = {@NamedAttributeNode("parent")}),//级联 parent
        @NamedEntityGraph(name = "tree.children", attributeNodes = {@NamedAttributeNode("children")}), // 级联 children
        @NamedEntityGraph(name = "tree.parent.children", attributeNodes = {@NamedAttributeNode("parent"), @NamedAttributeNode("children")})})
// 二者都级联
public class TreeEntity extends BaseEntity {

    //private static final log log = LoggerFactory.getLogger(TreeEntity.class);

    /**
     * 父组织
     * 树状结构，root 节点（根节点），没有父节点，为 null
     */

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    private TreeEntity parent;

    /**
     * 子组织
     * jpa 中，orphanRemoval = true ，才可以删除子.
     */

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "parent", orphanRemoval = true)
    @Fetch(FetchMode.SUBSELECT)
    //如果不用此句，默认是 FetchMode.SELECT ,查询每个被关联 child ，会发送一个查询语句，供发送 n+1个。FetchMode.SUBSELECT 通过子查询一次完成，对于 child 过多的情况下，应用。
    //n+1问题，需要根据实际情况调试
    @BatchSize(size = 100)//child 过多的情况下应用。
    @OrderBy("index ASC")
    //用到了可以调整排序，所以不用 set ，用 list。
    private List<TreeEntity> children = new ArrayList<>();  //初始化，否则在没有初始化时进行操作会发生异常。

    /**
     * 节点名称
     */
    @Column(name = "name", nullable = false)
    private String name;
    /**
     * 菜单 url ，点击叶节点的时候，导航到的 url ，这个对菜单有用处。
     */
    @Column(name = "url")
    private String url;
    /**
     * url target
     * "_blank", "_self" 或 其他指定窗口名称
     */
    @Column(name = "target")
    private String target;
    /**
     * 菜单样式表 class 名称
     */
    @Column(name = "css")
    private String css;
    /**
     * 在同级中的排序。
     * 需要用机制保证排序是连续的，并且从 0 开始。
     * 如果这样，每次增加、删除或者移动，都需要把所有的节点进行重新排序。
     * 也可以不连续，但容易有点混乱，所以还是牺牲点性能，保证连续吧
     * order,index 为 mysql 关键字，不能用作字段名
     */
    @Column(name = "index_", nullable = false)
    private int index;
    /**
     * 是否为父节点，包含子节点，即为父节点 , true 时会显示为文件夹图标。
     * 添加节点时，如果没有子节点，不会显示为文件夹图标，即使是想添加父节点，也会显示为叶节点图表。
     */

    // @getter,@setter 自动生成的方法，没有 get 和 setter 会变成  isParent() 和 setParent(boolean parent)
    // 此时和有的反序列化工具如 fasterjson 对应不上 ，这是反序列化工具的 bug 么？
    //只好自己实现 getter 和 setter 方法，会覆盖 @Getter   @Setter ，但为了清晰，最好去掉
    @Column(name = "isParent", columnDefinition = "boolean default true")
    private boolean isParentNode;
    /**
     * 菜单类型 : 必须
     */

    @Column(name = "type", nullable = false)
    private TreeType type;
    /**
     * 树状结构的层级,根节点 level = 0，依次递增
     * 不加 @Getter , @Setter 不自动生成
     */
    @Transient  // 不在数据库中建立字段
    @Setter(AccessLevel.NONE) // 不创建 Setter  方法，反序列化会有问题么?
    private int level;
    /**
     * JPA spec 需要无参的构造方法，用户不能直接使用。
     * 如果想要生成 Entity ，用其他有参数的构造方法。
     * DTOUtils 中使用 public 方法生成空对象
     */
    public TreeEntity() {
        // no-args constructor required by JPA spec
        // this one is protected since it shouldn't be used directly
    }

    /**
     * 菜单创建，只提供这一个构造函数，强制要求录入必需的数据项
     * 菜单需要初始化
     *
     * @param type         节点类型
     * @param name         名称
     * @param index        节点的排序序号 index
     * @param isParentNode 本身是否是父节点。
     *                     由于使用了 dtoUtils 设定转换深度（children 可能不在转换层次内，不会转换，会有 children = null 情况，此时无法从 children 是否有值来判断本身是否父节点），
     *                     所以 isParent 还是放在构造函数内，直接作为属性设置，可以直接判断。
     * @param parent       父菜单
     */

    public TreeEntity(TreeType type, String name, int index, boolean isParentNode, TreeEntity parent) {

        if (!name.contains("root_")) // 初始化根节点时，需要设置 parent 为 null (InitializeService.java) ，其他情况 parent 不允许 为 null
            Assert.notNull(parent, "parent can not be null.");

        this.name = name;
        this.type = type;
        this.index = index;
        this.isParentNode = isParentNode;
        this.parent = parent;

    }

    /**
     * 添加指定节点到当前节点所有子节点队列的尾部，即添加的节点 index 最大
     *
     * @param child
     */
    public void addChildToLastIndex(TreeEntity child) {

        if (child == null || children.contains(child))
            return;
        child.setIndex(children.size()); // list 的 index 从 0 开始 , children.size() 为最后的序号 +1
        children.add(child);
        child.setParent(this);

    }

    /**
     * 可以用于：
     * 0. 增加子节点作为当前节点的子节点，并设置子节点的顺位 index
     * -
     * 根据当前父节点不同，子节点可以用于节点移动 ：
     * 子节点
     * 1. 在当前父节点下，子节点间同级移动；
     * 2. 移动到新的父节点下
     *
     * @param child
     * @param index
     */
    public void addChildToIndex(TreeEntity child, int index) {

        if (child == null)
            return;

        // FastJsonPropertyPreFilter filter = new FastJsonPropertyPreFilter();
//        filter.addExcludes(TreeNodeEntity.class, "children", "roles");

        //排序前
//        System.out.println(StringUtils.center(" original ", 80, "*"));
//        MyJsonUtils.prettyPrint(children, filter);


        if (children.contains(child)) // true 同级间添加 , false 为添加到新的父节点。
            children.remove(child); // 重复的，删除后重新添加，不能直接修改 index ，可能重复

        sortTreeByIndex(children); //按照元素在 list 的位置信息，重新设置原来子节点的 index 属性，便于重新排序  ，否则 while 逻辑不对

        //排序后
//        System.out.println(StringUtils.center(" modify by sort ", 80, "*"));
//        MyJsonUtils.prettyPrint(children, filter);


        children.add(index, child);
        while (index < children.size()) { //只变换 index 后面的元素位置，index 之前，是排序好的
            children.get(index).setIndex(index);
            index++;
        }

        child.setParent(this);

    }

    /**
     * 清空子
     */
    public void clearChildren() {
        children.clear();
    }


    /**
     * Returns if this node is the root node in the tree or not.
     *
     * @return <code>true</code> if this node is the root of the tree;
     * <code>false</code> if it has a parent.
     */
    public boolean isRoot() {
        if (this.parent == null) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 树状结构的层级,根节点 level = 0，依次递增
     *
     * @return
     */
    public int getLevel() {
        getLevelInit(this);
        return level;
    }

    /**
     * 递归计算层级，根节点为 0 级
     *
     * @param entity
     * @return
     */
    private void getLevelInit(TreeEntity entity) {

        if (entity.getParent() == null) {
            return;
        } else {
            level++;
            getLevelInit(entity.getParent());
        }
    }


    /**
     * list 中的 TreeNodeEntity 按照 index 属性排序后，重新设置 TreeNodeEntity 的 index 的值为 TreeNodeEntity 在 list 中的位置。
     *
     * @param children
     */

    private void sortTreeByIndex(List<TreeEntity> children) {
        // Sorting 便于利用 list 的 indexOf 方法
        Collections.sort(children, new Comparator<TreeEntity>() {
            @Override
            public int compare(TreeEntity child1, TreeEntity child2) {
                return Integer.compare(child1.getIndex(), child2.getIndex());
            }
        });

        for (TreeEntity entity : children)
            entity.setIndex(children.indexOf(entity));

    }
}
