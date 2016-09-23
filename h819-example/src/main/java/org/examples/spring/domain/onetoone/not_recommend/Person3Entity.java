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
//@Table(name = "example_person3")
//@NamedEntityGraph(name = "examples.entity.onetoone.Person3Entity",//唯一id ,jpa 2.1属性
//        attributeNodes = {@NamedAttributeNode("address")})
public class Person3Entity {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    @OneToOne(fetch = FetchType.LAZY, mappedBy = "person", orphanRemoval = true) //会在 address2 表中，增加 person2 的 id
    private Address3Entity address;

    public Person3Entity() {

    }

    public Person3Entity(String name) {

        this.name = name;
    }

    /**
     * @return
     */
    public Address3Entity getAddress() {
        return address;
    }

    /**
     * @param address
     */
    public void setAddress(Address3Entity address) {
        this.address = address;
    }

    /**
     * 添加关联关系
     */
    public void addAddress(Address3Entity address) {
        if (address != null) {
            this.setAddress(address);
            address.setPerson(this); //建立关联
        }
    }

    /**
     * 注意解除关联的方法(单向的不需要)
     */
    public void removeAddress(Address3Entity address) {
        if (address != null) {
            this.setAddress(null);
            address.setPerson(null);  //解除关联
        }
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
