package com.base.spring.domain;

import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Description : TODO(用户组，也可以理解为部门等逻辑结构，总之是为了给用户分组，便于授权)
 * User: h819
 * Date: 2015/10/16
 * Time: 15:15
 * To change this template use File | Settings | File Templates.
 */
@Entity
@Table(name = "base_group") //group 为 mysql 关键字，不能用作字段名
public class GroupEntity extends BaseEntity {


    @ManyToMany(fetch = FetchType.LAZY, targetEntity = UserEntity.class)// 如果是单向多对多，只在发出方设置，接收方不做设置
    @JoinTable(name = "base_ref_user_group", //指定关联表名
            joinColumns = {@JoinColumn(name = "group_id", referencedColumnName = "id")},////生成的中间表的字段，对应关系的发出端(主表) id
            inverseJoinColumns = {@JoinColumn(name = "user_id", referencedColumnName = "id")}, //生成的中间表的字段，对应关系的接收端(从表) id
            uniqueConstraints = {@UniqueConstraint(columnNames = {"group_id", "user_id"})}) // 唯一性约束，是从表的联合字段
    @Fetch(FetchMode.SUBSELECT)
    @BatchSize(size = 100)//roles 过多的情况下应用。
    private List<UserEntity> users = new ArrayList<UserEntity>();

    @ManyToMany(fetch = FetchType.LAZY, targetEntity = RoleEntity.class)// 如果是单向多对多，只在发出方设置，接收方不做设置
    @JoinTable(name = "base_ref_group_roles", //指定关联表名
            joinColumns = {@JoinColumn(name = "group_id", referencedColumnName = "id")},////生成的中间表的字段，对应关系的发出端(主表) id
            inverseJoinColumns = {@JoinColumn(name = "role_id", referencedColumnName = "id")}, //生成的中间表的字段，对应关系的接收端(从表) id
            uniqueConstraints = {@UniqueConstraint(columnNames = {"role_id", "group_id"})}) // 唯一性约束，是从表的联合字段
    @Fetch(FetchMode.SUBSELECT)
    @BatchSize(size = 100)//roles 过多的情况下应用。
    private List<RoleEntity> roles = new ArrayList<RoleEntity>();


    //昵称
    @Column(name = "name", unique = true)
    private String name;

    /**
     * JPA spec 需要无参的构造方法，用户不能直接使用。
     * 如果想要生成 Entity ，用其他有参数的构造方法。
     */
    protected GroupEntity() {
        // no-args constructor required by JPA spec
        // this one is protected since it shouldn't be used directly
    }

    public GroupEntity(final String name) {
        this.name = name;
    }


    /**
     * 自定义方法，添加权限
     * 注意建立关联的方法(单向的不需要)
     * 属性 student.teachers设置为 public ，否则无法直接获取。直接设置其属性，get 方法不方便直接操作 ????
     *
     * @param role
     */
    public void addRole(RoleEntity role) {

        if (!this.roles.contains(role)) {
            this.roles.add(role);
            role.getGroup().add(this);
        }

    }

    /**
     * 自定义方法，删除学生
     * 注意删除关联的方法(单向的不需要)
     * 属性 student.teachers 设置为 public ，否则无法直接获取。直接设置其属性，get 方法不方便直接操作 ???/
     *
     * @param role
     */
    public void removeRole(RoleEntity role) {

        if (this.roles.contains(role)) {
            this.roles.remove(role);
            role.getGroup().remove(this);

        }

    }


    /**
     * 自定义方法，添加权限
     * 注意建立关联的方法(单向的不需要)
     * 属性 student.teachers设置为 public ，否则无法直接获取。直接设置其属性，get 方法不方便直接操作 ????
     *
     * @param user
     */
    public void addUser(UserEntity user) {

        if (!this.users.contains(user)) {
            this.users.add(user);
            user.getGroup().add(this);
        }

    }

    /**
     * 自定义方法，删除学生
     * 注意删除关联的方法(单向的不需要)
     * 属性 student.teachers 设置为 public ，否则无法直接获取。直接设置其属性，get 方法不方便直接操作 ???/
     *
     * @param user
     */
    public void removeUser(UserEntity user) {

        if (this.users.contains(user)) {
            this.users.remove(user);
            user.getGroup().remove(this);

        }

    }

    public List<UserEntity> getUsers() {
        return users;
    }

    public void setUsers(List<UserEntity> users) {
        this.users = users;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<RoleEntity> getRoles() {
        return roles;
    }

    public void setRoles(List<RoleEntity> roles) {
        this.roles = roles;
    }
}
