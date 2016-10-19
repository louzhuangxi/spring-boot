package org.examples.spring.domain;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.h819.web.spring.jpa.entitybase.AbstractMySQLEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Description : TODO(用户信息)
 * User: h819
 * Date: 14-7-7
 * Time: 下午3:18
 * To change this template use File | Settings | File Templates.
 */
@Entity
@Table(name = "example_user")

@Getter
@Setter
@AllArgsConstructor
@Deprecated // 用 spring boot web 中的
public class UserEntity extends AbstractMySQLEntity {

    @Column(name = "name")
    private String name;

    /**
     * 用 spring boot web 中的
     */
}
