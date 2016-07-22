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
//@Table(name = "example_address3")
//@NamedEntityGraph(name = "examples.entity.onetoone.Address3Entity",//唯一id ,jpa 2.1属性
//        attributeNodes = {@NamedAttributeNode("person")})
public class Address3Entity {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "person_id", nullable = false)
    private Person3Entity person;


    public Address3Entity() {

    }

    public Address3Entity(String name) {

        this.name = name;
    }


    /**
     * 添加关联关系
     */
    public void addPerson(Person3Entity person) {
        if (person != null) {
            this.setPerson(person);
            person.setAddress(this); //建立关联
        }
    }

    /**
     * 注意解除关联的方法
     */
    public void removePerson(Person3Entity person) {
        if (person != null) {
            this.setPerson(null);
            person.setAddress(null);  //解除关联
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

    public Person3Entity getPerson() {
        return person;
    }

    public void setPerson(Person3Entity person) {
        this.person = person;
    }
}
