package org.examples.spring.domain.onetomany.jointable.bidirectional;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

/**
 * Description : TODO()
 * User: h819
 * Date: 2014/11/18
 * Time: 16:54
 * To change this template use File | Settings | File Templates.
 */
@Entity
@Table(name = "example_parent6")
@NamedEntityGraph(name = "examples.entity.onetomany.ParentEntity6",//唯一id ,jpa 2.1属性
        attributeNodes = {@NamedAttributeNode("children")})
@Getter
@Setter
@AllArgsConstructor
public class ParentEntity6 {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    // 双向 one to many, 关系表方式，parent 和 child 两个表都无变化，会自动创建多方(child)指定的中间表。关系表方式自动创建，而不需要真正的实体类。如果需要对关系添加属性，而需要有真正的实体类，此情形同 many to many 的 one to many + 关系表的实现方式。
    @OneToMany(fetch = FetchType.LAZY, targetEntity = ChildEntity6.class)
    @JoinTable//通过中间表
            (name = "example_ref_parent6_child6", //指定关联表名
                    joinColumns = {@JoinColumn(name = "parent_id", referencedColumnName = "id")},//生成的中间表的字段，对应关系的发出端(JoinTable 关键字所在的类即是发出端) id
                    inverseJoinColumns = {@JoinColumn(name = "child_id", referencedColumnName = "id")},//生成的中间表的字段，对应关系的接收端(从表) id
                    uniqueConstraints = {@UniqueConstraint(columnNames = {"parent_id", "child_id"})})// 唯一性约束，是从表的联合字段
    @Fetch(FetchMode.SUBSELECT) // n+1 问题见 one to many ParentEntity2.class
    @BatchSize(size = 100)//child 过多的情况下应用。
    private Set<ChildEntity6> children = new HashSet<>(); //set 可以过滤重复元素
    // Getters and Setters

    public ParentEntity6() {
    }

    public ParentEntity6(String name) {
        this.name = name;
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