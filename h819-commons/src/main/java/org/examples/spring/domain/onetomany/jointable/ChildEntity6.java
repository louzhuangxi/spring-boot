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
@Table(name = "example_child6")
@NamedEntityGraph(name = "examples.entity.onetomany.ChildEntity6",//唯一id ,jpa 2.1属性
        attributeNodes = {@NamedAttributeNode("parent")})
public class ChildEntity6 {

    //解释见 ParentEntity6
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinTable//通过中间表（自动生成中间表）
            (name = "example_ref_parent6_child6", //指定关联表名
                    joinColumns = {@JoinColumn(name = "child_id", referencedColumnName = "id")},//生成的中间表的字段，对应关系的发出端(JoinTable 关键字所在的类即是发出端) id
                    inverseJoinColumns = {@JoinColumn(name = "parent_id", referencedColumnName = "id")},//生成的中间表的字段，对应关系的接收端(从表) id
                    uniqueConstraints = {@UniqueConstraint(columnNames = {"parent_id", "child_id"})})// 唯一性约束，是从表的联合字段
    private ParentEntity6 parent;

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "name")
    private String name;

    public ChildEntity6() {

    }

    /**
     * 增加一个 ChildEntity5 ，不必通过 list
     * 注意建立关联的方法(单向的不需要)
     *
     * @param parent
     */
    public void addParent(ParentEntity6 parent) {
        if (parent != null) {
            this.parent = parent;
            parent.addChild(this);
        }
    }

    /**
     * 注意解除关联的方法(单向的不需要)
     *
     * @param parent
     */

    public void removeParent(ParentEntity6 parent) {
        if (parent != null) {
            this.parent = null;
            parent.removeChild(this);
        }
    }


    public ParentEntity6 getParent() {
        return parent;
    }

    public void setParent(ParentEntity6 parent) {
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
