/*
 * Copyright 2012 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.demo.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * Base class to derive entity classes from.
 * -
 * MySQL 创建 id
 * -
 * 基类统一定义id的属性名称、数据类型、列名映射及生成策略.
 * Oracle需要每个Entity独立定义id的SEQUCENCE时，不继承于本类而改为实现一个Idable的接口。
 * (摘自 spring side)
 *
 * @author Oliver Gierke
 */

// @MappedSuperclass  :
// 1. JPA 基类的标识，表示类本身不是一个完整的实体类，不会在数据库中建表。它的属性会创建在其子类所创建的表中
// 2. 不能再标注@Entity或@Table注解
// 3. 常用于创建公共属性
@Getter
@Setter
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class) // 该 entity 启用 auditing ，在 @CreatedDate 等标记的类上设置
public abstract class AbstractMySQLEntity implements Serializable {


    /**
     * JPA提供的四种标准用法为TABLE，SEQUENCE，IDENTITY，AUTO
     * TABLE:使用一个特定的数据库表格来保存主键
     * SEQUENCE:根据地层数据库的序列来生成主键，条件是数据库支持序列，主要是Oracle
     * IDENTITY:主键由数据库自动生成(主要是自动增长型，针对 MySQL)
     * AUTO:主键由程序控制
     */

    /**
     *  oracle 中设置方法
     *
     *  如果序列 SEQ_USER_ID 不存在，会报异常
     // name :为 SequenceGenerator 起一个名字
     // sequenceName 对应的数据库序列名字 (sequenceName 和 name 可以相同)
     // initialValue 初始值
     // allocationSize ：增长步长为 1
     @SequenceGenerator(name = "USER_ID_GENERATOR", sequenceName = "SEQ_USER_ID", initialValue = 1, allocationSize = 1)
     // strategy ：固定为 GenerationType.SEQUENCE
     // generator ： @SequenceGenerator 的 name
     @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "USER_ID_GENERATOR")
     */


    /**
     * 如果不指定表字段，会自动映射为 变量名
     * 并且在加载运行时，发现没有该表字段，会自动添加创建（如果表，Sequence没有也会创建）。
     * 对应ORACLE，创建的的类型对应关系：
     * String  VARCHAR2(255 CHAR)
     * Long    NUMBER(19)
     * Integer NUMBER(10)
     * java.sql.Date   DATE
     * java.sql.Time   DATE
     * java.util.Date  TIMESTAMP(6)
     * java.sql.Timestamp  TIMESTAMP(6)
     */

    //auditing 使用方法
    //1. 开启 @EnableJpaAuditing
    //2. 在 Entity 上标记 @EntityListeners(AuditingEntityListener.class)
    // ------
    // 在 entity 创建和修改时，会自动记录两个时间，不用程序控制，会自动维护

    //LocalDateTime 和 mysql 字段类型对应时:
    // mysql 的字段类型都没有 millisecond 属性，所以只能对应成 mysql 的 tinyblob 类型，不能直接查看
    @Column(name = "created_date")
    @CreatedDate
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+08:00")
    LocalDateTime createdDate;


    @Column(name = "last_modified_date")
    @LastModifiedDate
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+08:00")
    LocalDateTime lastModifiedDate;


    // 自定义获取用户名的方法 见 JpaConfig
    //创建者
    @Column(name = "created_by", nullable = false)
    @CreatedBy
    private String createdByUser;

    //最后修改人
    @Column(name = "last_modified_by", nullable = false)
    @LastModifiedBy
    private String lastModifiedByUser;


    /**
     * Mysql , H2 中 ID 的设置方法
     * 如果没有 @GeneratedValue ，仅有 @Id ，则主键应该由应用程序指定
     */

    @Id
    @Column(name = "id", updatable = false, nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Returns the identifier of the entity.
     *
     * @return the id
     */
    public Long getId() {
        return id;
    }

    /**
     * jpa 会自行设置 id，为 Entity 的唯一主键，一般不需要改动
     * 没有 setter 方法还不行，在反序列化时(string to object )，还需要用到 setter 方法
     *
     * @param id
     */
    public void setId(Long id) {
        this.id = id;
    }

    /*
         * 简单认定，如果 id 相同，即为同一个对象。如果需要更改此测量，在基础此类的子类中 override 此方法
         * @see java.lang.Object#equals(java.lang.Object)
         */
    @Override
    public boolean equals(Object obj) {

        if (this == obj) {
            return true;
        }

        if (this.id == null || obj == null || !(this.getClass().equals(obj.getClass()))) {
            return false;
        }

        AbstractMySQLEntity that = (AbstractMySQLEntity) obj;

        return this.id.equals(that.getId());
    }

    /*
     * (non-Javadoc)
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        return id == null ? 0 : id.hashCode();
    }
}
