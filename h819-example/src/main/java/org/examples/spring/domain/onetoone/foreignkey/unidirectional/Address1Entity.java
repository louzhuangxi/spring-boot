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
//@Table(name = "example_address1")
@Getter
@Setter
@AllArgsConstructor
public class Address1Entity {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    /**
     * 没有 Person1Entity 标记，只能单向
     */

    public Address1Entity() {

    }

    public Address1Entity(String name) {

        this.name = name;
    }

}
