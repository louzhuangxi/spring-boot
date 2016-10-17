package com.base.spring.domain;

import lombok.AllArgsConstructor;
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
 * Description : TODO(用户组，也可以理解为部门等逻辑结构，总之是为了给用户分组，便于授权)
 * User: h819
 * Date: 2015/10/16
 * Time: 15:15
 * To change this template use File | Settings | File Templates.
 */
@Entity
@Table(name = "base_group") //group 为 mysql 关键字，不能用作字段名
@Getter
@Setter
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class) // 该 entity 启用 auditing
public class GroupEntity extends BaseEntity {


    @ManyToMany(fetch = FetchType.LAZY, targetEntity = UserEntity.class)// 双向多对多，发出方、接收方都设置
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
    public GroupEntity() {
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
            role.getGroups().add(this);
        }

    }

    public void addRoles(List<RoleEntity> roles) {

        this.setRoles(roles);

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
            role.getGroups().remove(this);

        }
    }

    public void removeRoles(List<RoleEntity> roles) {

        for (RoleEntity entity : roles)
            removeRole(entity);
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
            user.getGroups().add(this);
        }

    }

    public void addUsers(List<UserEntity> users) {

      this.setUsers(users);

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
            user.getGroups().remove(this);

        }
    }

    public void removeUsers(List<UserEntity> users) {
        for (UserEntity userEntity : users)
            removeUser(userEntity);
    }

    /**
     * 清空 user
     */
    public void clearUsers() {
        this.users.clear();
    }

    /**
     * 清空 role
     */
    public void clearRoles() {
        this.roles.clear();
    }

}
