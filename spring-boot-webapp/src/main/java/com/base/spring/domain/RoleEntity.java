package com.base.spring.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.*;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 * Description : TODO(角色表)
 * User: h819
 * Date: 2015/10/16
 * Time: 15:15
 * To change this template use File | Settings | File Templates.
 */


//定义一个角色：对哪些树节点有哪些权限，这些节点的权限相同；
// 如果出现不同的节点不同的权限，那么定义为不同的角色。
@Entity
@Table(name = "base_role")
@Getter
@Setter
@ToString
@AllArgsConstructor
public class RoleEntity extends BaseEntity {


    @ManyToMany(fetch = FetchType.LAZY, targetEntity = GroupEntity.class)// 如果是单向多对多，只在发出方设置，接收方不做设置
    @JoinTable(name = "base_ref_group_roles", //指定关联表名
            joinColumns = {@JoinColumn(name = "role_id", referencedColumnName = "id")},////生成的中间表的字段，对应关系的发出端(主表) id
            inverseJoinColumns = {@JoinColumn(name = "group_id", referencedColumnName = "id")}, //生成的中间表的字段，对应关系的接收端(从表) id
            uniqueConstraints = {@UniqueConstraint(columnNames = {"role_id", "group_id"})}) // 唯一性约束，是从表的联合字段
    private Set<GroupEntity> groups = new HashSet<>();


    @ManyToMany(fetch = FetchType.LAZY, targetEntity = UserEntity.class) // 单向多对多，只在发出方设置，接收方不做设置
    @JoinTable(name = "base_ref_users_roles", //指定关联表名
            joinColumns = {@JoinColumn(name = "role_id", referencedColumnName = "id")},////生成的中间表的字段，对应关系的发出端(主表) id
            inverseJoinColumns = {@JoinColumn(name = "user_id", referencedColumnName = "id")}, //生成的中间表的字段，对应关系的接收端(从表) id
            uniqueConstraints = {@UniqueConstraint(columnNames = {"user_id", "role_id"})}) // 唯一性约束，是从表的联合字段
    private Set<UserEntity> users = new HashSet<>();

//    @ManyToMany(fetch = FetchType.LAZY, targetEntity = PrivilegeEntity.class)// 单向多对多，只在发出方设置，接收方不做设置
//    @JoinTable(name = "base_ref_roles_privileges", //指定关联表名
//            joinColumns = {@JoinColumn(name = "role_id", referencedColumnName = "id")},////生成的中间表的字段，对应关系的发出端(主表) id
//            inverseJoinColumns = {@JoinColumn(name = "privilege_id", referencedColumnName = "id")}, //生成的中间表的字段，对应关系的接收端(从表) id
//            uniqueConstraints = {@UniqueConstraint(columnNames = {"role_id", "privilege_id"})}) // 唯一性约束，是从表的联合字段
//    @Fetch(FetchMode.SUBSELECT)
//    @BatchSize(size = 100)//roles 过多的情况下应用。
//    private List<PrivilegeEntity> privileges = new ArrayList<>();


    @ManyToMany(fetch = FetchType.LAZY, targetEntity = TreeNodeEntity.class)// 单向多对多，只在发出方设置，接收方不做设置
    @JoinTable(name = "base_ref_roles_treenode", //指定关联表名
            joinColumns = {@JoinColumn(name = "role_id", referencedColumnName = "id")},////生成的中间表的字段，对应关系的发出端(主表) id
            inverseJoinColumns = {@JoinColumn(name = "treenode_id", referencedColumnName = "id")}, //生成的中间表的字段，对应关系的接收端(从表) id
            uniqueConstraints = {@UniqueConstraint(columnNames = {"role_id", "treenode_id"})}) // 唯一性约束，是从表的联合字段
    @Fetch(FetchMode.SUBSELECT)
    @BatchSize(size = 100)//roles 过多的情况下应用。
    private Set<TreeNodeEntity> treeNodes = new HashSet<>();

    //昵称

    @Column(name = "name", unique = true)
    private String name;

    /**
     * JPA spec 需要无参的构造方法，用户不能直接使用。
     * 如果想要生成 Entity ，用其他有参数的构造方法。
     */
    public RoleEntity() {
        // no-args constructor required by JPA spec
        // this one is protected since it shouldn't be used directly
    }

    public RoleEntity(final String name) {
        super();
        this.name = name;
    }


    /**
     * 添加单个
     *
     * @param treeNode
     */
    public void addTreeNode(TreeNodeEntity treeNode) {
        if (treeNode != null)
            this.treeNodes.add(treeNode);

    }

    /**
     * 添加多个
     *
     * @param treeNodes
     */
    public void addTreeNodes(Collection<TreeNodeEntity> treeNodes) {
        if (treeNodes != null && !treeNodes.isEmpty())
            this.treeNodes.addAll(treeNodes);
    }

    /**
     * 自定义方法，删除
     *
     * @param treeNode
     */
    public void removeTreeNode(TreeNodeEntity treeNode) {
        if (treeNode != null)
            this.treeNodes.remove(treeNode);
    }

    /**
     * 删除多个
     *
     * @param treeNodes
     */
    public void removeTreeNodes(Collection<TreeNodeEntity> treeNodes) {
        if (treeNodes != null && !treeNodes.isEmpty())
            this.treeNodes.removeAll(treeNodes);
    }

    /**
     * 清空子
     */
    public void clearTreeNodes() {
        this.treeNodes.clear();
    }

    public void addUser(UserEntity user) {
        if (user != null)
            this.users.add(user);
    }

    public void addUsers(Collection<UserEntity> users) {
        if (users != null && !users.isEmpty())
            this.users.addAll(users);

    }

    public void removeUser(UserEntity user) {
        if (user != null)
            this.users.remove(user);
    }

    public void removeUsers(Collection<UserEntity> users) {
        if (users != null && !users.isEmpty())
            this.users.removeAll(users);
    }

    public void clearUsers() {
        this.users.clear();
    }

    public void addGroup(GroupEntity group) {
        if (group != null)
            this.groups.add(group);
    }

    public void addGroups(Collection<GroupEntity> groups) {
        if (groups != null && !groups.isEmpty())
            this.getGroups().addAll(groups);

    }


    public void removeGroup(GroupEntity group) {
        if (group != null)
            this.groups.remove(group);
    }

    public void removeGroups(Collection<GroupEntity> groups) {
        if (groups != null && !groups.isEmpty())
            this.groups.removeAll(groups);
    }

    public void clearGroups() {
        this.groups.clear();
    }
}
