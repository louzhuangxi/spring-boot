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
package org.h819.web.spring.jpa.entitybase;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Base class to derive entity classes from.
 * <p/>
 * oracle 中创建 entity id ，和 Sequence 名称相关，如果默认的 Sequence 名字 SEQ_ID_APP 不合适，可以把 id 直接拷贝到 entity 中，不用基础此类
 * <p/>
 * 基类统一定义id的属性名称、数据类型、列名映射及生成策略.
 * Oracle需要每个Entity独立定义id的SEQUCENCE时，不继承于本类而改为实现一个Idable的接口。
 * (摘自 spring side)
 *
 * @author Oliver Gierke
 */

// @MappedSuperclass  : JPA 基类的标识，表示类本身不受 Spring 管理的实体类，不会在数据库中建表，而是会由其他实体类进行扩展后建表
@MappedSuperclass
public abstract class AbstractOracleEntity implements Serializable {


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

    /**
     * 如果没有 @GeneratedValue ，仅有 @Id ，则主键应该由应用程序指定
     */

    @Id
    @Column(name = "id", updatable = false, nullable = false)
    @SequenceGenerator(name = "SEQ_ID_GENERATOR", sequenceName = "SEQ_ID_APP", initialValue = 1, allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_ID_GENERATOR")
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

        AbstractOracleEntity that = (AbstractOracleEntity) obj;

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
