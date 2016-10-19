package org.examples.spring.domain.onetomany.foreignkey.bidirectional;

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
@Entity
@Table(name = "example_parent5")
@NamedEntityGraph(name = "examples.entity.onetomany.ParentEntity5",//唯一id ,jpa 2.1属性
        attributeNodes = {@NamedAttributeNode("children")})
@Getter
@Setter
@AllArgsConstructor
public class ParentEntity5 {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "parent", orphanRemoval = true)
    // mappedBy = "parent" ，parent 为 child 类中的属性
    @Fetch(FetchMode.SUBSELECT) // n+1 问题见 one to many ParentEntity2.class
    @BatchSize(size = 100)//child 过多的情况下应用。
    private Set<ChildEntity5> children = Sets.newHashSet(); //set 可以过滤重复元素
    // Getters and Setters

    public ParentEntity5() {
    }

    public ParentEntity5(String name) {
        this.name = name;
    }

}