package com.base.spring.custom;

import com.base.spring.domain.UserEntity;
import org.springframework.security.core.authority.AuthorityUtils;

/**
 * 重新包装 org.springframework.security.core.userdetails.User 类型
 * 作用：
 * 1. 继承 org.springframework.security.core.userdetails.User ，可以作为 loadUserByUsername 的返回值
 * 2. 重新包装后的 domain, 可包含三种属性:
 * 2.1  org.springframework.security.core.userdetails.User 中的属性
 * 2.2  UserEntity  中的属性
 * 2.3  常用的自定义属性
 * 三种属性，传递到 model 中，方便逻辑处理
 */
public class SecurityUser extends org.springframework.security.core.userdetails.User {

    private UserEntity user;
    private String[] roles;

    //通过角色，获取的是 PrivilegeEntity 中的 name , 如果 name 标识的 url 满足  ?????
    public SecurityUser(UserEntity userEntity) {
        //重新根据 UserEntity 进行包装
        super(userEntity.getUserName(), userEntity.getPassword(), AuthorityUtils.createAuthorityList(userEntity.getStringRoles()));

        // 其他变量赋值
        this.user = userEntity;
        roles = userEntity.getStringRoles();
    }

    /**
     * 直接获取自定义的 UserEntity
     *
     * @return
     */
    public UserEntity getUser() {
        return user;
    }


    /**
     * 如果 CurrentUser 在 model 中名字为 currentUser
     * 该方法的返回值获取 : currentUser.id
     *
     * @return
     */
    public Long getId() {
        return user.getId();
    }


    /**
     * 如果 CurrentUser 在 model 中名字为 currentUser
     * 该方法的返回值获取 : currentUser.role
     * 可以显式定义 roles 变量后赋值
     *
     * @return
     */
    public String[] getRoles() {
        // return user.getStringRoles();
        return roles;
    }

}
