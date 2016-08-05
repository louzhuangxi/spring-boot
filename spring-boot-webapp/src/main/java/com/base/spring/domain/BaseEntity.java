package com.base.spring.domain;

import com.fasterxml.jackson.annotation.JsonFormat;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.util.Date;

/**
 * Description : TODO(一个基础 domain ，大部分的实体都有此相关属性，故总结出来，供其它实体继承)
 * User: h819
 * Date: 2015/12/25
 * Time: 15:05
 * To change this template use File | Settings | File Templates.
 */
// LocalDateTime hibernate 4 还不支持，spring boot 1.3.0 是 hibernate 4 的，等到了 hibernate 5 ，此处的 Data 换为 LocalDateTime
// @MappedSuperclass  : JPA 基类的标识，表示类本身不受 Spring 管理的实体类，不会在数据库中建表，而是会由其他实体类进行扩展后建表
@MappedSuperclass
public class BaseEntity extends AbstractMySQLEntity {
    /**
     * 本记录更新时间
     */
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "update_time")
    // 设定JSON序列化时的日期格式
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+08:00")
    private Date updateTime;
    /**
     * 本记录创建时间
     */
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "create_time")
    // 设定JSON序列化时的日期格式
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+08:00")
    private Date createTime;

    /**
     * 本记录过期时间
     */
    @Column(name = "expire_time")
    @Temporal(TemporalType.TIMESTAMP)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+08:00")
    private Date expireDate;


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

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        if (createTime == null)
            this.createTime = new Date();

        else this.createTime = createTime;
    }

    public Date getExpireDate() {
        return expireDate;
    }

    public void setExpireDate(Date expireDate) {
        this.expireDate = expireDate;
    }

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public boolean getValid() {
        return valid;
    }

    public void setValid(boolean valid) {
        this.valid = valid;
    }

    public boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }
}
