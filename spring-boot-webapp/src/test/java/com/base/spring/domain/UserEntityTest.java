package com.base.spring.domain;

import com.alibaba.fastjson.JSON;
import com.base.SpringBootWebappApplication;
import com.base.spring.repository.UserRepository;
import com.base.spring.utils.BCryptPassWordUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.transaction.annotation.Transactional;

/**
 * Description : TODO()
 * User: h819
 * Date: 2015/12/28
 * Time: 13:48
 * To change this template use File | Settings | File Templates.
 */

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = SpringBootWebappApplication.class)
@Rollback(false)
@Transactional
@WebAppConfiguration
public class UserEntityTest {

    @Autowired
    UserRepository userRepository;


    @Test
   // @WithMockUser
    public void testFind() {
       System.out.println(JSON.toJSONString(userRepository.findAll()));
    }

    @Test
    public void testSave() {

        UserEntity admin = new UserEntity();
        admin.setUserName("admin");
        admin.setLoginName("admin");
        admin.setPassword(BCryptPassWordUtils.encode("password"));
        admin.setEmail("1@qq.com");

        userRepository.save(admin);
      //  userRepository.save(new UserEntity("user","user","123456","user@email.com"));

    }

    @Test
    public void testUpdate() {

        UserEntity admin = userRepository.findOneByUserName("admin").get();
        admin.setEmail("1@qq.com");
        userRepository.save(admin);
        //  userRepository.save(new UserEntity("user","user","123456","user@email.com"));

    }


}