package org.examples.spring.domain.onetomany.not_recommend;

import com.google.common.collect.Sets;
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
public class ParentEntity1 {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;


    @OneToMany(fetch = FetchType.LAZY, orphanRemoval = true)
    @JoinColumn(name = "parent_id ")
    @Fetch(FetchMode.SUBSELECT)
    //如果不用此句，默认是 FetchMode.SELECT ,查询每个被关联 child ，会发送一个查询语句，供发送 n+1个。FetchMode.SUBSELECT 通过子查询一次完成，对于 child 过多的情况下，应用。
    //n+1问题，需要根据实际情况调试
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


    /**
     * 增加一个 ChildEntity1 ，不必通过set list
     *
     * @param child
     */
    public void addChild(ChildEntity1 child) {
        if (child != null)
            this.children.add(child);
    }

    /**
     * 注意解除关联的方法(单向的不需要)
     *
     * @param child
     */

    public void removeChild(ChildEntity1 child) {
        if (child != null)
            this.children.remove(child);
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

    public Set<ChildEntity1> getChildren() {
        return children;
    }

    public void setChildren(Set<ChildEntity1> children) {
        this.children = children;
    }
}