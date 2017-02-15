package com.example.mybatis.generate.domain;

public class BaseRole {
    private Long id;

    private Boolean enabled;

    private String operator;

    private String remark;

    private Boolean valid;

    private String name;

    private byte[] createdDate;

    private byte[] modifiedDate;

    private byte[] expireDate;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator == null ? null : operator.trim();
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark == null ? null : remark.trim();
    }

    public Boolean getValid() {
        return valid;
    }

    public void setValid(Boolean valid) {
        this.valid = valid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    public byte[] getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(byte[] createdDate) {
        this.createdDate = createdDate;
    }

    public byte[] getModifiedDate() {
        return modifiedDate;
    }

    public void setModifiedDate(byte[] modifiedDate) {
        this.modifiedDate = modifiedDate;
    }

    public byte[] getExpireDate() {
        return expireDate;
    }

    public void setExpireDate(byte[] expireDate) {
        this.expireDate = expireDate;
    }
}