package org.examples.spring.domain.onetomany.jointable;

import javax.persistence.*;

/**
 * Description : TODO()
 * User: h819
 * Date: 2014/11/18
 * Time: 16:55
 * To change this template use File | Settings | File Templates.
 */
@Entity
@Table(name = "example_child4")
@NamedEntityGraph(name = "examples.entity.onetomany.ChildEntity4",//唯一id ,jpa 2.1属性
        attributeNodes = {@NamedAttributeNode("parent")})
public class ChildEntity4 {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    // @JoinColumn(name = "parent_id", nullable = false) //在 child 标准增加 parent_id 字段，指定 parent 的 id数值是 parent 的 id
    @JoinTable//通过中间表（自动生成中间表），只在 parent 中设置
            (
                    name = "example_ref_parent4_child4", //指定关联表名
                    joinColumns = {@JoinColumn(name = "child_id", referencedColumnName = "id")},//生成的中间表的字段，对应关系的发出端(JoinTable 关键字所在的类即是发出端) id
                    inverseJoinColumns = {@JoinColumn(name = "parent_id", referencedColumnName = "id")},//生成的中间表的字段，对应关系的接收端(从表) id
                    uniqueConstraints = {@UniqueConstraint(columnNames = {"parent_id", "child_id"})// 唯一性约束，是从表的联合字段
                    }
            )
    private ParentEntity4 parent;

    public ChildEntity4() {

    }

    /**
     * 增加一个 ChildEntity1 ，不必通过set list
     *
     * @param parent
     */
    public void addParent(ParentEntity4 parent) {
        if (parent != null)
            this.setParent(parent);
    }

    /**
     * 注意解除关联的方法(单向的不需要)
     * 不需要 parent 参数
     */

    public void removeParent() {
        if (parent != null)
            this.setParent(null);
    }

    public ParentEntity4 getParent() {
        return parent;
    }

    public void setParent(ParentEntity4 parent) {
        this.parent = parent;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
