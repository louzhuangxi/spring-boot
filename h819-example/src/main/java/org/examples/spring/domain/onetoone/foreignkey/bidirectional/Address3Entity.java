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
//@Table(name = "example_address3")
//@NamedEntityGraph(name = "examples.entity.onetoone.Address3Entity",//唯一id ,jpa 2.1属性
//        attributeNodes = {@NamedAttributeNode("person")})
@Getter
@Setter
@AllArgsConstructor
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

}
