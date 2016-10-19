package org.examples.spring.domain.onetoone.foreignkey.unidirectional;

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
//@Table(name = "example_person1")
//@NamedEntityGraph(name = "examples.entity.onetoone.Person3Entity",//唯一id ,jpa 2.1属性
//        attributeNodes = {@NamedAttributeNode("address")})
@Getter
@Setter
@AllArgsConstructor
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
}
