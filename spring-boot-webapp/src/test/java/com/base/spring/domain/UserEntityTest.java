package com.base.spring.domain;

import com.alibaba.fastjson.JSON;
import com.base.spring.repository.UserRepository;
import com.base.spring.utils.BCryptPassWordUtils;
import org.h819.commons.MyJsonUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.data.domain.ExampleMatcher.GenericPropertyMatchers.endsWith;
import static org.springframework.data.domain.ExampleMatcher.GenericPropertyMatchers.startsWith;

/**
 * Description : TODO()
 * User: h819
 * Date: 2015/12/28
 * Time: 13:48
 * To change this template use File | Settings | File Templates.
 */

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
@Transactional
@Rollback(false)
public class UserEntityTest {

    @Autowired
    UserRepository userRepository;


    @Test
    // @WithMockUser
    public void testFind() {
        System.out.println(JSON.toJSONString(userRepository.findAll()));
    }

    @Test
    public void testUpdate() {

        UserEntity admin = userRepository.findByLoginName("admin").get();
        admin.setEmail("1@qq.com");
        userRepository.save(admin);
        //  userRepository.save(new UserEntity("user","user","123456","user@email.com"));

    }

    @Test
    public void testExample() {

        UserEntity admin = new UserEntity();
        admin.setUserName("example");
        admin.setLoginName("example");
        admin.setPassword(BCryptPassWordUtils.encode("example"));
        admin.setEmail("1@qq.com");

        ExampleMatcher matcher = ExampleMatcher.matching()
                .withMatcher("userName", endsWith())
                .withMatcher("loginName", startsWith().ignoreCase());

        Example<UserEntity> example = Example.of(admin, matcher);

        MyJsonUtils.prettyPrint(userRepository.findAll(example));
        System.out.println(userRepository.count(example));

    }


}