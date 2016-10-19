package org.examples.spring.domain.onetomany.foreignkey.unidirectional.manytoone;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

/**
 * Description : TODO()
 * User: h819
 * Date: 2014/11/18
 * Time: 16:54
 * To change this template use File | Settings | File Templates.
 */
//@Entity
//@Table(name = "example_parent3")
@Getter
@Setter
@AllArgsConstructor
public class ParentEntity3 {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;


    public ParentEntity3() {
    }

     public ParentEntity3(String name) {
        this.name = name;
    }


}