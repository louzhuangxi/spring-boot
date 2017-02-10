package com.example.mybatis.generate.domain;

public class BaseTree {
    private Long id;

    private Boolean enabled;

    private String operator;

    private String remark;

    private Boolean valid;

    private String css;

    private Integer index;

    private Boolean isParent;

    private String name;

    private String target;

    private Integer type;

    private String url;

    private Long parentId;

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

    public String getCss() {
        return css;
    }

    public void setCss(String css) {
        this.css = css == null ? null : css.trim();
    }

    public Integer getIndex() {
        return index;
    }

    public void setIndex(Integer index) {
        this.index = index;
    }

    public Boolean getIsParent() {
        return isParent;
    }

    public void setIsParent(Boolean isParent) {
        this.isParent = isParent;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    public String getTarget() {
        return target;
    }

    public void setTarget(String target) {
        this.target = target == null ? null : target.trim();
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url == null ? null : url.trim();
    }

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
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