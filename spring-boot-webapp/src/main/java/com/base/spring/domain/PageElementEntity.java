package com.base.spring.domain;

import javax.persistence.*;

/**
 * Description : TODO(页面某个具体元素，如下载按钮，图片 等)
 * User: h819
 * Date: 2015/10/16
 * Time: 15:15
 * To change this template use File | Settings | File Templates.
 */
@Entity
@Table(name = "base_page_element")
public class PageElementEntity extends BaseEntity {


    /**
     * 名称
     */
    @Column(name = "name", unique = true)
    private String name;

    /**
     * 编码，自动生成唯一值
     */
    @Column(name = "code", nullable = false)
    private String code;

    @OneToOne(fetch = FetchType.LAZY, mappedBy = "pageElement", orphanRemoval = true)
    private PrivilegeEntity privilege;

    /**
     * JPA spec 需要无参的构造方法，用户不能直接使用。
     * 如果想要生成 Entity ，用其他有参数的构造方法。
     */
    protected PageElementEntity() {
        // no-args constructor required by JPA spec
        // this one is protected since it shouldn't be used directly
    }

    public PageElementEntity(final String name) {
        super();
        this.name = name;
        this.code = java.util.UUID.randomUUID().toString();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    /**
     * 随机字符串
     */
    public void setCode() {
        this.code = java.util.UUID.randomUUID().toString();
    }

    public PrivilegeEntity getPrivilege() {
        return privilege;
    }

    public void setPrivilege(PrivilegeEntity privilege) {
        this.privilege = privilege;
    }
}
