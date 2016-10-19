package org.examples.spring.domain.onetomany.foreignkey.bidirectional;

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
@Table(name = "example_child5")
@NamedEntityGraph(name = "examples.entity.onetomany.ChildEntity5",//唯一id ,jpa 2.1属性
        attributeNodes = {@NamedAttributeNode("parent")})
@Getter
@Setter
@AllArgsConstructor
public class ChildEntity5 {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id", nullable = false) //在一的一方 parent 表增加 parent_id 字段，指定 parent 的 id数值是 parent 的 id
            ParentEntity5 parent;
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    public ChildEntity5() {

    }

}
