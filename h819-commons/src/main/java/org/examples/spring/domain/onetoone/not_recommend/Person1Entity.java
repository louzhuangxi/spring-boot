package org.examples.spring.domain.onetoone.not_recommend;

import javax.persistence.*;

/**
 * Description : TODO()
 * User: h819
 * Date: 2014/11/18
 * Time: 16:55
 * To change this template use File | Settings | File Templates.
 */
//@Entity
//@Table(name = "example_person1")
//@NamedEntityGraph(name = "examples.entity.onetoone.Person3Entity",//唯一id ,jpa 2.1属性
//        attributeNodes = {@NamedAttributeNode("address")})
public class Person1Entity {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "address_id") //单向：仅在发出端设置， 在发出端 person1 表中，会增加 address1 的 id，Address1 表中无变化
    private Address1Entity address;


    public Person1Entity() {

    }

    public Person1Entity(String name) {

        this.name = name;
    }


    /**
     * 添加关联关系
     */
    public void addAddress(Address1Entity address) {
        if (address != null) {
            this.setAddress(address);//建立关联
        }
    }

    /**
     * 注意解除关联的方法(单向的不需要)
     * 不需要 person 参数
     */
    public void removeAddress() {

        this.setAddress(null);//解除关联

    }

    public Address1Entity getAddress() {
        return address;
    }

    public void setAddress(Address1Entity address) {
        this.address = address;
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
