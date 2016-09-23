package org.examples.spring.domain.onetomany.not_recommend;

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

    /**
     * 增加一个 ChildEntity5 ，不必通过 list
     * 注意建立关联的方法(单向的不需要)
     *
     * @param parent
     */
    public void addParent(ParentEntity5 parent) {
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

    public void removeParent(ParentEntity5 parent) {
        if (parent != null) {
            this.parent = null;
            parent.removeChild(this);
        }
    }


    public ParentEntity5 getParent() {
        return parent;
    }

    public void setParent(ParentEntity5 parent) {
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
