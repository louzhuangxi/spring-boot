package com.base.spring.service.security;


import com.base.spring.custom.security.SecurityUser;
import com.base.spring.repository.RoleRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;

@Service(value = "currentUserService") // 变为 spring 容器管理的 bean 对象的时候，默认名字为类名，首字母小写。此处明确指出，便于理解
public class CurrentUserService {

    private static final Logger LOGGER = LoggerFactory.getLogger(CurrentUserService.class);

    @Autowired
    RoleRepository roleRepository;
//    @Autowired
//    PrivilegeRepository privilegeRepository;

    /**
     * 判断用户是否有相关权限，本方法判断依据是
     * 用户自己，或者系统管理员，可以查看用户基本信息，用于 UserController.getUserPage 方法验证
     *
     * @param securityUser
     * @param userId
     * @return
     */
    public boolean canAccessUser(SecurityUser securityUser, Long userId) {
        LOGGER.debug("Checking if user={} has access to user={}", securityUser, userId);
        return securityUser != null && (Arrays.asList(securityUser.getRoles()).contains("ADMIN") || securityUser.getId().equals(userId));
    }

}
