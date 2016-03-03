package org.examples.spring.domain;

import org.h819.web.spring.jpa.entitybase.AbstractMySQLEntity;
import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Description : TODO(树状结构，不用这个，参考 TreeNodeEntity)
 * User: h819
 * Date: 14-7-7
 * Time: 下午3:18
 * To change this template use File | Settings | File Templates.
 */
@Entity
@Table(name = "example_tree", uniqueConstraints = {@UniqueConstraint
        (columnNames = {"name", "code"})})
@NamedEntityGraphs({
        @NamedEntityGraph(name = "examples.entity.tree.parent", attributeNodes = {@NamedAttributeNode("parent")}),//级联 parent
        @NamedEntityGraph(name = "examples.entity.tree.children", attributeNodes = {@NamedAttributeNode("children")}), // 级联 children
        @NamedEntityGraph(name = "examples.entity.tree.parent.children", attributeNodes = {@NamedAttributeNode("parent"), @NamedAttributeNode("children")})}) //级联 parent , children
//有相互关联关系的类：jackjson 转换为 json 结构的 String 的时候，可以避免进入死循环。但实际项目测试发现，如果本类还有其他复杂的关联关系，还是不行。只能用过滤属性的办法进行过滤。
@Deprecated // 用 spring boot web 中的
public class TreeEntity extends AbstractMySQLEntity {
    /**
     * 组织名称
     */
    @Column(name = "name")
    private String name;

    /**
     * 组织编码
     */
    @Column(name = "code")
    private String code;

    /**
     * 树状结构的层级
     */
    @Column(name = "level")
    private int level;


    /**
     * 在同级中的排序
     */
    @Column(name = "order")
    private int order;

    /**
     * 父组织
     * 树状结构，root 节点（根节点），没有父节点，为 null
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    private TreeEntity parent;

    /**
     * 子组织
     */
    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "parent")
    @Fetch(FetchMode.SUBSELECT)
    //如果不用此句，默认是 FetchMode.SELECT ,查询每个被关联 child ，会发送一个查询语句，供发送 n+1个。FetchMode.SUBSELECT 通过子查询一次完成，对于 child 过多的情况下，应用。
    //n+1问题，需要根据实际情况调试
    @BatchSize(size = 100)//child 过多的情况下应用。
    private List<TreeEntity> children = new ArrayList<TreeEntity>();

    public TreeEntity() {
    }

    /**
     * @param name
     * @param code
     * @param level
     */
    public TreeEntity(String name, String code, int level) {
        this.name = name;
        this.code = code;
        this.level = level;
    }

    /**
     * 添加子
     *
     * @param e
     */
    public void addChild(TreeEntity e) {
        if(e!=null) {
            children.add(e);
            e.setParent(this);
        }
    }

    /**
     * 添加子
     *
     * @param e
     */
    public void removeChild(TreeEntity e) {
        if(e!=null) {
            children.remove(e);
            e.setParent(null);
        }
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public TreeEntity getParent() {
        return parent;
    }

    public void setParent(TreeEntity parent) {
        this.parent = parent;
    }

    public List<TreeEntity> getChildren() {
        return children;
    }

    public void setChildren(List<TreeEntity> children) {
        this.children = children;
    }

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }
}
