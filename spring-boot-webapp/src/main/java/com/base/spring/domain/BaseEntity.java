package com.base.spring.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import java.time.LocalDateTime;

/**
 * Description : TODO(一个基础 domain ，大部分的实体都有此相关属性，故总结出来，供其它实体继承)
 * User: h819
 * Date: 2015/12/25
 * Time: 15:05
 * To change this template use File | Settings | File Templates.
 */
// @MappedSuperclass  : JPA 基类的标识，表示类本身不受 Spring 管理的实体类，不会在数据库中建表，而是会由其他实体类进行扩展后建表
@MappedSuperclass
@Getter
@Setter
public abstract class BaseEntity extends AbstractMySQLEntity {

    /**
     * 本记录过期时间
     */

    @Column(name = "expire_date")
    // @Temporal(TemporalType.TIMESTAMP)  LocalDateTime 不用
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+08:00")
    private LocalDateTime expireDate;

    /**
     * 记录操作者，应该是该用户的id
     */

    @Column(name = "operator")
    private String operator;
    /**
     * 备注
     */

    @Column(name = "remark")
    private String remark;


    /**
     * 本记录是否通过验证
     */

    @Column(name = "valid", columnDefinition = "boolean default true")
    private boolean valid;

    /**
     * 本记录是否启用，如果为 false ，则本记录不可以
     */

    @Column(name = "enabled", columnDefinition = "boolean default true")
    private boolean enabled;

    /**
     * JPA spec 需要无参的构造方法，用户不能直接使用。
     * 如果想要生成 Entity ，用其他有参数的构造方法。
     */
    protected BaseEntity() {
        // no-args constructor required by JPA spec
        // this one is protected since it shouldn't be used directly
    }
}
