package org.examples.spring.domain.onetomany.jointable.unidirectional.onetomany;

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
@Table(name = "example_child2")
@Getter
@Setter
@AllArgsConstructor
public class ChildEntity2 {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;


    public ChildEntity2() {

    }

    public ChildEntity2(String name) {

        this.name = name;
    }
}
