package org.examples.newfeatures.jpa21.entity;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Description : TODO()
 * User: h819
 * Date: 2015/1/14
 * Time: 13:24
 * To change this template use File | Settings | File Templates.
 */
@Entity
@Table(name = "example_jpa21_product")
public class Product implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", updatable = false, nullable = false)
    private Long id = null;
    @Version
    @Column(name = "version")
    private int version = 0;

    @Column
    private String name;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
