package com.base.spring.domain;

import com.base.spring.utils.BCryptPassWordUtils;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Description : TODO(用户信息)
 * User: h819
 * Date: 14-7-7
 * Time: 下午3:18
 * To change this template use File | Settings | File Templates.
 */
// 可参考 org.springframework.security.core.userdetails.User
@Entity
@Table(name = "base_user")
@Getter
@Setter
@EntityListeners(AuditingEntityListener.class) // 该 entity 启用 auditing
public class UserEntity extends BaseEntity {

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
    private String loginName;

    //密码，必要时，加密处理


    @Column(name = "password", nullable = false)
    private String password;

    // 不持久化到数据库，也不显示在Restful接口的属性.
    @Transient
    @JsonIgnore


    private String passwordRepeated;


    //用户真正姓名


    @Column(name = "user_name", nullable = false)
    private String userName;


    //Indicates whether the user's account has expired.
    @Column(name = "account_NonExpired", columnDefinition = "boolean default false")
    private boolean accountNonExpired;


    //Indicates whether the user is locked or unlocked.
    @Column(name = "account_NonLocked", columnDefinition = "boolean default false")
    private boolean isAccountNonLocked;


    //Indicates whether the user's account has expired.
    @Column(name = "credentials_nonExpired", columnDefinition = "boolean default false")
    private boolean isCredentialsNonExpired;

    /**
     * 该用户是否已经通过邮件验证
     */


    @Column(name = "email_valid", columnDefinition = "boolean default false")
    private boolean emailValid;

    /**
     * 该用户是否接收 email 邮件通知
     */


    @Column(name = "receiveEmailInfo", columnDefinition = "boolean default true")
    private boolean receiveEmailInfo;


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


    // 给用户发送确认邮件时，附带 token 参数，用来唯一定位用户。用 UUID.randomUUID().toString() 生成，保证了唯一性
    // 该参数不宜用 id 或其他的参数。
    @Column(name = "token")
    private String token;


    @ManyToMany(mappedBy = "users", targetEntity = GroupEntity.class)
    private List<GroupEntity> group = new ArrayList<>();


    @ManyToMany(fetch = FetchType.LAZY, targetEntity = RoleEntity.class) // 单向多对多，只在发出方设置，接收方不做设置
    @JoinTable(name = "base_ref_users_roles", //指定关联表名
            joinColumns = {@JoinColumn(name = "user_id", referencedColumnName = "id")},////生成的中间表的字段，对应关系的发出端(主表) id
            inverseJoinColumns = {@JoinColumn(name = "role_id", referencedColumnName = "id")}, //生成的中间表的字段，对应关系的接收端(从表) id
            uniqueConstraints = {@UniqueConstraint(columnNames = {"user_id", "role_id"})}) // 唯一性约束，是从表的联合字段
    @Fetch(FetchMode.SUBSELECT)
    @BatchSize(size = 100)//roles 过多的情况下应用。
    private List<RoleEntity> roles = new ArrayList<RoleEntity>(); //set 可以过滤重复元素

    /**
     * JPA spec 需要无参的构造方法，用户不能直接使用。
     * 如果想要生成 Entity ，用其他有参数的构造方法。
     */
    public UserEntity() {

        // no-args constructor required by JPA spec
        // this one is protected since it shouldn't be used directly
    }


    /**
     * 创建用户，密码加密
     *
     * @param loginName
     * @param password
     * @param email
     */
    public UserEntity(String loginName, String password, String email) {

        this.loginName = loginName;
        this.loginName = loginName;
        this.password = BCryptPassWordUtils.encode(password);
        this.email = email;
    }


    /**
     * 自定义方法，添加学生
     * 注意建立关联的方法(单向的不需要)
     * 属性 student.teachers设置为 public ，否则无法直接获取。直接设置其属性，get 方法不方便直接操作 ????
     *
     * @param role
     */
    public void addRole(RoleEntity role) {

        if (!this.roles.contains(role)) {
            this.roles.add(role);
            role.getUsers().add(this);
        }

    }

    /**
     * 自定义方法，删除学生
     * 注意删除关联的方法(单向的不需要)
     * 属性 student.teachers 设置为 public ，否则无法直接获取。直接设置其属性，get 方法不方便直接操作 ???/
     *
     * @param student
     */
    public void removeRole(RoleEntity student) {

        if (this.roles.contains(student)) {
            this.roles.remove(student);
            student.getUsers().remove(this);

        }

    }

    /**
     * 获取字符串形式的 Role
     *
     * @return
     */
    public String[] getStringRoles() {

        List<String> roles = new ArrayList<String>();

        for (RoleEntity roleEntity : getRoles())
            roles.add(roleEntity.getName());

        return roles.toArray(new String[roles.size()]);
    }
}
