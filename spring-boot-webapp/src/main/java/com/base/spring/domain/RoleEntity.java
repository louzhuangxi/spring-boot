package com.base.spring.domain;

import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

/**
 * Description : TODO(角色表)
 * User: h819
 * Date: 2015/10/16
 * Time: 15:15
 * To change this template use File | Settings | File Templates.
 */
@Entity
@Table(name = "base_role")
public class RoleEntity extends BaseEntity {


    @ManyToMany(mappedBy = "roles", targetEntity = GroupEntity.class)
    private Set<GroupEntity> group = new HashSet<GroupEntity>();

    @ManyToMany(mappedBy = "roles", targetEntity = UserEntity.class)
    private Set<UserEntity> users = new HashSet<UserEntity>();

    @ManyToMany(fetch = FetchType.LAZY, targetEntity = PrivilegeEntity.class)// 单向多对多，只在发出方设置，接收方不做设置
    @JoinTable(name = "base_ref_roles_privileges", //指定关联表名
            joinColumns = {@JoinColumn(name = "role_id", referencedColumnName = "id")},////生成的中间表的字段，对应关系的发出端(主表) id
            inverseJoinColumns = {@JoinColumn(name = "privilege_id", referencedColumnName = "id")}, //生成的中间表的字段，对应关系的接收端(从表) id
            uniqueConstraints = {@UniqueConstraint(columnNames = {"role_id", "privilege_id"})}) // 唯一性约束，是从表的联合字段
    @Fetch(FetchMode.SUBSELECT)
    @BatchSize(size = 100)//roles 过多的情况下应用。
    private Set<PrivilegeEntity> privileges = new HashSet<PrivilegeEntity>();

    //昵称
    @Column(name = "name",unique = true)
    private String name;

    /**
     * JPA spec 需要无参的构造方法，用户不能直接使用。
     * 如果想要生成 Entity ，用其他有参数的构造方法。
     */
    protected RoleEntity() {
        // no-args constructor required by JPA spec
        // this one is protected since it shouldn't be used directly
    }

    public RoleEntity(final String name) {
        super();
        this.name = name;
    }


    /**
     * 自定义方法，添加学生
     * 注意建立关联的方法(单向的不需要)
     * 属性 student.teachers设置为 public ，否则无法直接获取。直接设置其属性，get 方法不方便直接操作 ????
     *
     * @param privilege
     */
    public void addPrivilege(PrivilegeEntity privilege) {

        if (!this.privileges.contains(privilege)) {
            this.privileges.add(privilege);
            privilege.getRoles().add(this);
        }

    }

    /**
     * 自定义方法，删除学生
     * 注意删除关联的方法(单向的不需要)
     * 属性 student.teachers 设置为 public ，否则无法直接获取。直接设置其属性，get 方法不方便直接操作 ???/
     *
     * @param privilege
     */
    public void removePrivilege(PrivilegeEntity privilege) {

        if (this.privileges.contains(privilege)) {
            this.privileges.remove(privilege);
            privilege.getRoles().remove(this);

        }

    }

    /**
     * 自定义方法，添加学生
     * 注意建立关联的方法(单向的不需要)
     * 属性 student.teachers设置为 public ，否则无法直接获取。直接设置其属性，get 方法不方便直接操作 ????
     *
     * @param user
     */
    public void addUser(UserEntity user) {

        if (!this.users.contains(user)) {
            this.users.add(user);
            user.getRoles().add(this);
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
            user.getRoles().remove(this);

        }

    }

    /**
     * 自定义方法，添加学生
     * 注意建立关联的方法(单向的不需要)
     * 属性 student.teachers设置为 public ，否则无法直接获取。直接设置其属性，get 方法不方便直接操作 ????
     *
     * @param group
     */
    public void addGroup(GroupEntity group) {

        if (!this.group.contains(group)) {
            this.group.add(group);
            group.getRoles().add(this);
        }

    }

    /**
     * 自定义方法，删除学生
     * 注意删除关联的方法(单向的不需要)
     * 属性 student.teachers 设置为 public ，否则无法直接获取。直接设置其属性，get 方法不方便直接操作 ???/
     *
     * @param group
     */
    public void removeGroup(GroupEntity group) {

        if (this.group.contains(group)) {
            this.group.remove(group);
            group.getRoles().remove(this);

        }

    }

    public Set<UserEntity> getUsers() {
        return users;
    }

    public void setUsers(Set<UserEntity> users) {
        this.users = users;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<PrivilegeEntity> getPrivileges() {
        return privileges;
    }

    public void setPrivileges(Set<PrivilegeEntity> privileges) {
        this.privileges = privileges;
    }

    public Set<GroupEntity> getGroup() {
        return group;
    }

    public void setGroup(Set<GroupEntity> group) {
        this.group = group;
    }
}
