package org.examples.spring.domain.onetomany.not_recommend;

import javax.persistence.*;

/**
 * Description : TODO()
 * User: h819
 * Date: 2014/11/18
 * Time: 16:55
 * To change this template use File | Settings | File Templates.
 */
//@Entity
//@Table(name = "example_child3")
//@NamedEntityGraph(name = "examples.entity.onetomany.ChildEntity3",//唯一id ,jpa 2.1属性
//        attributeNodes = {@NamedAttributeNode("parent")})
public class ChildEntity3 {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id", nullable = false) //会在 parent 表中增加 parent_id 字段，指定 parent 的 id数值是 parent 的 id
    private ParentEntity3 parent;

    public ChildEntity3() {

    }

    /**
     * 增加一个 ChildEntity1 ，不必通过set list
     *
     * @param parent
     */
    public void addParent(ParentEntity3 parent) {
        if (parent != null)
            this.setParent(parent);
    }

    /**
     * 注意解除关联的方法(单向的不需要)
     *
     * 不需要 parent 参数
     */

    public void removeParent() {
        if (parent != null)
            this.setParent(null);
    }



    public ParentEntity3 getParent() {
        return parent;
    }

    public void setParent(ParentEntity3 parent) {
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
