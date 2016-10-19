package org.examples.app;

/**
 * Description : TODO(spring Application 如何使用 bean )
 * User: h819
 * Date: 2015/8/11
 * Time: 9:15
 * To change this template use File | Settings | File Templates.
 */

import org.examples.spring.domain.UserEntity;
import org.examples.spring.repository.UserEntityRepository;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.core.io.ClassPathResource;

import java.util.List;


public class Application {
    public static void main(String[] args) {
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(new ClassPathResource("resources/applicationContext.xml").getPath());
        UserEntityRepository userRepository = context.getBean(UserEntityRepository.class);//获取 bean 的方法
        UserEntity user = new UserEntity("");

        user.setName("jianghui");
        userRepository.save(user);

        List<UserEntity> personList = userRepository.findAll();
        System.out.println("Person List : ");
        for (UserEntity u : personList) {
            System.out.println(u);
        }

        System.out.println("user with Id 1 is " + userRepository.findOne(1l));

        context.close();

    }
}
