package org.examples.spring.domain.onetomany.jointable;

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
@Entity
@Table(name = "example_parent6")
@NamedEntityGraph(name = "examples.entity.onetomany.ParentEntity6",//唯一id ,jpa 2.1属性
        attributeNodes = {@NamedAttributeNode("children")})
public class ParentEntity6 {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    // 双向 one to many, 关系表方式，parent 和 child 两个表都无变化，会自动创建多方(child)指定的中间表。关系表方式自动创建，而不需要真正的实体类。如果需要对关系添加属性，而需要有真正的实体类，此情形同 many to many 的 one to many + 关系表的实现方式。
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "parent", orphanRemoval = true)
    // mappedBy = "parent" ，parent 为 child 类中的属性
    @Fetch(FetchMode.SUBSELECT)
    //如果不用此句，默认是 FetchMode.SELECT ,查询每个被关联 child ，会发送一个查询语句，供发送 n+1个。FetchMode.SUBSELECT 通过子查询一次完成，对于 child 过多的情况下，应用。
    //n+1问题，需要根据实际情况调试
    @BatchSize(size = 100)//child 过多的情况下应用。
    private Set<ChildEntity6> children = Sets.newHashSet(); //set 可以过滤重复元素
    // Getters and Setters

    public ParentEntity6() {
    }

    public ParentEntity6(String name) {
        this.name = name;
    }


    /**
     * 增加一个 ChildEntity5 ，不必通过 list
     * 注意建立关联的方法(单向的不需要)
     *
     * @param child
     */
    public void addChild(ChildEntity6 child) {
        if (child != null) {
            this.children.add(child);
            child.setParent(this);
        }
    }

    /**
     * 注意解除关联的方法(单向的不需要)
     *
     * @param child
     */

    public void removeChild(ChildEntity6 child) {
        if (child != null) {
            child.setParent(null);
            this.children.remove(child);
        }
    }

    public Set<ChildEntity6> getChildren() {
        return children;
    }

    public void setChildren(Set<ChildEntity6> children) {
        this.children = children;
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