package org.examples.spring.domain;


import com.fasterxml.jackson.annotation.JsonFormat;
import org.h819.commons.MyDateUtils;
import org.h819.web.spring.jpa.entitybase.AbstractMySQLEntity;

import javax.persistence.*;
import java.util.Date;

/**
 * Description : TODO(用户信息)
 * User: h819
 * Date: 14-7-7
 * Time: 下午3:18
 * To change this template use File | Settings | File Templates.
 */
@Entity
@Table(name = "example_user")
public class UserEntity extends AbstractMySQLEntity {

    /**
     * 用户注册后，是否激活。激活可以通过邮件
     * 1. 唯一的 token
     * 2. 有效期为 24 小时
     */
    //有效期，24 小时
    private static final int EXPIRE_DAY_AMOUNT = 24;

    //昵称
    @Column(name = "nick_name")
    private String nickName;

    //登录名
    @Column(name = "login_name", unique = true, nullable = false)
    private String loginname;
    //密码，必要时，加密处理
    @Column(name = "password", nullable = false)
    private String password;

    //用户真正姓名
    @Column(name = "name", nullable = false)
    private String name;

    //出生日期

    //性别

    @Column(name = "address")
    private String address;

    @Column(name = "telephone")
    private String telephone;

    @Column(name = "mobile")
    private String mobile;

    @Column(name = "fax")
    private String fax;

    @Column(name = "postcode")
    private String postcode;

    //重要信息，用于找回密码，唯一
    @Column(name = "email", unique = true)
    private String email;

    @Column(name = "company")
    private String company;

    //社交工具
    @Column(name = "qq")
    private String qq;

    @Column(name = "weixin")
    private String weixin;

    /**
     * 该用户是否通过邮件验证
     */
    @Column(name = "valid", columnDefinition = "boolean default false")
    private boolean valid;
    /**
     * 该用户是否接收 email 邮件通知
     */
    @Column(name = "receive_email", columnDefinition = "boolean default true")
    private boolean receiveEmailInfo;
    /**
     * 信息更新时间
     */
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "update_time")
    // 设定JSON序列化时的日期格式
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+08:00")
    private Date updateTime;
    /**
     * 信息创建时间
     */
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "create_time")
    // 设定JSON序列化时的日期格式
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+08:00")
    private Date createTime;
    // 给用户发送确认邮件时，附带 token 参数，用来唯一定位用户。用 UUID.randomUUID().toString() 生成，保证了唯一性
    // 该参数不宜用 id 或其他的参数。
    @Column(name = "token")
    private String token;
    @Column(name = "expire_time")
    @Temporal(TemporalType.TIMESTAMP)
    private Date expireDate;
    /**
     * 记录操作者
     */
    @Column(name = "operator")
    private String operator;
    /**
     * 备注
     */
    @Column(name = "remark")
    private String remark;


    public UserEntity() {
    }

    public static int getEXPIRE_DAY_AMOUNT() {
        return EXPIRE_DAY_AMOUNT;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getLoginname() {
        return loginname;
    }

    public void setLoginname(String loginname) {
        this.loginname = loginname;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getFax() {
        return fax;
    }

    public void setFax(String fax) {
        this.fax = fax;
    }

    public String getPostcode() {
        return postcode;
    }

    public void setPostcode(String postcode) {
        this.postcode = postcode;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getQq() {
        return qq;
    }

    public void setQq(String qq) {
        this.qq = qq;
    }

    public String getWeixin() {
        return weixin;
    }

    public void setWeixin(String weixin) {
        this.weixin = weixin;
    }

    public boolean isValid() {
        return valid;
    }

    public void setValid(boolean valid) {
        this.valid = valid;
    }

    public boolean isReceiveEmailInfo() {
        return receiveEmailInfo;
    }

    public void setReceiveEmailInfo(boolean receiveEmailInfo) {
        this.receiveEmailInfo = receiveEmailInfo;
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
        this.createTime = createTime;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Date getExpireDate() {
        return expireDate;
    }

    /**
     * 根据当前日期，设置过期时间
     *
     * @param hours 从当前日期开始计算，过期时间
     */
    public void setExpireDate(int hours) {
        this.expireDate = MyDateUtils.addHours(new Date(), hours);
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
}
