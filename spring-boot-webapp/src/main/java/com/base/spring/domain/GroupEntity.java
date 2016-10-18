package com.base.spring.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

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
    private Set<UserEntity> users = new HashSet<UserEntity>();


    @ManyToMany(fetch = FetchType.LAZY, targetEntity = RoleEntity.class)// 如果是单向多对多，只在发出方设置，接收方不做设置
    @JoinTable(name = "base_ref_group_roles", //指定关联表名
            joinColumns = {@JoinColumn(name = "group_id", referencedColumnName = "id")},////生成的中间表的字段，对应关系的发出端(主表) id
            inverseJoinColumns = {@JoinColumn(name = "role_id", referencedColumnName = "id")}, //生成的中间表的字段，对应关系的接收端(从表) id
            uniqueConstraints = {@UniqueConstraint(columnNames = {"role_id", "group_id"})}) // 唯一性约束，是从表的联合字段
    @Fetch(FetchMode.SUBSELECT)
    @BatchSize(size = 100)//roles 过多的情况下应用。
    private Set<RoleEntity> roles = new HashSet<RoleEntity>();


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
     * @param role
     */
    public void addRole(RoleEntity role) {
        if (role != null)
            this.roles.add(role);
    }

    public void addRoles(Collection<RoleEntity> roles) {
        if (roles != null && !roles.isEmpty())
            this.roles.addAll(roles);
    }

    /**
     * 自定义方法，删除学生
     *
     * @param role
     */
    public void removeRole(RoleEntity role) {
        if (role != null)
            this.roles.remove(role);
    }

    public void removeRoles(Collection<RoleEntity> roles) {
        if (roles != null && !roles.isEmpty())
            this.roles.removeAll(roles);
    }

    /**
     * 清空 role
     */
    public void clearRoles() {
        this.roles.clear();
    }


    /**
     * 自定义方法，添加权限
     *
     * @param user
     */
    public void addUser(UserEntity user) {
        if (user != null)
            this.users.add(user);
    }

    public void addUsers(Collection<UserEntity> users) {
        if (users != null && !users.isEmpty())
            this.users.addAll(users);

    }

    /**
     * 自定义方法，删除学生
     *
     * @param user
     */
    public void removeUser(UserEntity user) {
        if (user != null)
            this.users.remove(user);
    }

    public void removeUsers(Collection<UserEntity> users) {
        if (users != null && !users.isEmpty())
            this.users.removeAll(users);
    }

    /**
     * 清空 user
     */
    public void clearUsers() {
        this.users.clear();
    }


}
