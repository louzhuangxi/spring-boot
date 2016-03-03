package com.base.spring.service.user;

import com.base.spring.domain.UserEntity;
import com.base.spring.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.Optional;

@Service
@Transactional(readOnly = true) //在事务中(带有 @Transactional)的 fetch = FetchType.LAZY 才可以自动加载
public class UserService {

    private static final Logger logger = LoggerFactory.getLogger(UserService.class);

    @Autowired
    private UserRepository userRepository;

    public Optional<UserEntity> getUserById(long id) {
        logger.debug("Getting user={}", id);
        return Optional.ofNullable(userRepository.findOne(id));
    }

    public Optional<UserEntity> getUserByEmail(String email) {
        logger.debug("Getting user by email={}", email.replaceFirst("@.*", "@***"));

        return userRepository.findOneByEmail(email);

        //打印测试，此处调用也可以强制级联加载关联对象
//        logger.info("role size : " + entity.get().getRoles().size());
//
//        for (RoleEntity entity1 : entity.get().getRoles())
//            logger.info("privi size : " + entity1.getPrivileges().size());

//        return entity;
    }


    public Collection<UserEntity> getAllUsers() {
        logger.debug("Getting all users");
        return userRepository.findAll(new Sort("email"));
    }


    @Transactional(readOnly = false)
    public UserEntity createUser(String loginName, String password, String email) {
        return userRepository.save(new UserEntity(loginName, password, email));
    }

    /*
    public UserEntity create(UserEntity form) {
        UserEntity user = new UserEntity();
        user.setEmail(form.getEmail());
        user.setPassword((new BCryptPasswordEncoder().encode(form.getPassword())));
        user.setRoles(form.getRoles());
        return userRepository.save(user);


    }   */
}