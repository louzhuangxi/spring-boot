package org.examples.spring.domain.onetomany.jointable.unidirectional.manytoone;

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
@Table(name = "example_child4")
@NamedEntityGraph(name = "examples.entity.onetomany.ChildEntity4",//唯一id ,jpa 2.1属性
        attributeNodes = {@NamedAttributeNode("parent")})
@Getter
@Setter
@AllArgsConstructor
public class ChildEntity4 {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinTable//通过中间表（自动生成中间表），只在 child 中设置
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
     * 增加几个增删关联关系表中数据的方法
     *
     * 1. 双向都是发出端的，双方都可以维护关联关系表中数据
     *
     * 2. 单向是发出端的，仅发出端可以维护关联关系表中数据
     * */

    /**
     * add , remove , clear 方法同 many to many TeacherEntity1
     */

}
