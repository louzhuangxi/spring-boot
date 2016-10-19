package org.examples.spring.domain.onetoone.jointable.bidirectional;

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
@Table(name = "example_address4")
@NamedEntityGraph(name = "examples.entity.onetoone.Address4Entity",//唯一id ,jpa 2.1属性
        attributeNodes = {@NamedAttributeNode("person")})
@Getter
@Setter
@AllArgsConstructor
public class Address4Entity {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;


    @OneToOne(fetch = FetchType.LAZY, mappedBy = "address", orphanRemoval = true)
    private Person4Entity person;


    public Address4Entity() {

    }

    public Address4Entity(String name) {

        this.name = name;
    }
}
