package org.examples.spring.domain.onetomany.jointable.bidirectional;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

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
@Getter
@Setter
@AllArgsConstructor
public class ChildEntity6 {

    /**
     * 方式一，方式二 区别，见 many to many , StudentEntity1.class
     */

    /**
     * 方式一：parent 是主表
     *  @ManyToMany(mappedBy = "children", targetEntity = ParentEntity6.class)
     *  private ParentEntity6 parent;
     */

    /**
     * 方式二 ： 双方都是主表
     */
    @ManyToOne(fetch = FetchType.LAZY, targetEntity = ParentEntity6.class)
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
}
