package org.examples.spring.domain.onetomany.foreignkey.unidirectional.onetomany;

import com.google.common.collect.Sets;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.*;
import java.util.Set;

/**
 * Description : TODO()
 * User: h819
 * Date: 2014/11/18
 * Time: 16:54
 * To change this template use File | Settings | File Templates.
 */
//@Entity
//@Table(name = "example_parent1")
//@NamedEntityGraph(name = "examples.entity.onetomany.ParentEntity1",//唯一id ,jpa 2.1属性
//        attributeNodes = {@NamedAttributeNode("children")})
@Getter
@Setter
@AllArgsConstructor
public class ParentEntity1 {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;


    @OneToMany(fetch = FetchType.LAZY, orphanRemoval = true)
    @JoinColumn(name = "parent_id ")
    @Fetch(FetchMode.SUBSELECT)  // n+1 问题见 one to many ParentEntity2.class
    @BatchSize(size = 100)//child 过多的情况下应用。
    // 仅在 Parent 中设置，会在 child 表中，增加 parent_id 字段, child 表，不做任何标记。
    // 如果漏掉这个注解，持久化厂商会创建一个专门的关联关系表，变成表关联方式
    private Set<ChildEntity1> children = Sets.newHashSet(); //set 可以过滤重复元素
    // Getters and Setters

    public ParentEntity1() {
    }

    public ParentEntity1(String name) {
        this.name = name;
    }

}