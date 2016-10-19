package org.examples.spring.domain.onetoone.foreignkey.bidirectional;

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
//@Entity
//@Table(name = "example_person3")
//@NamedEntityGraph(name = "examples.entity.onetoone.Person3Entity",//唯一id ,jpa 2.1属性
//        attributeNodes = {@NamedAttributeNode("address")})
@Getter
@Setter
@AllArgsConstructor
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

}
