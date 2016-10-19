package org.examples.spring.domain.onetomany.foreignkey.unidirectional.onetomany;

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
//@Table(name = "example_child1")
@Getter
@Setter
@AllArgsConstructor
public class ChildEntity1 {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;


    public ChildEntity1() {

    }

    public ChildEntity1(String name) {

        this.name = name;
    }

}
