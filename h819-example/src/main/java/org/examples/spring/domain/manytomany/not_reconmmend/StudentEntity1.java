package org.examples.spring.domain.manytomany.not_reconmmend;

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
//@Table(name = "example_student1")
@Getter
@Setter
@AllArgsConstructor
public class StudentEntity1 {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    public StudentEntity1() {

    }

    public StudentEntity1(String name) {
        this.name = name;
    }
}
