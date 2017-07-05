package com.base.spring.config.properties;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Map;

/**
 * Description : TODO()
 * User: h819
 * Date: 2017/1/22
 * Time: 17:01
 * To change this template use File | Settings | File Templates.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class FtpPropertiesTest {
    @Autowired
    private FtpProperty ftpProperties;

    @Test
    public void testConfigurationProperties() {

        System.out.println(ftpProperties.getUrl());
        System.out.println(ftpProperties.getPort());
        for (FtpProperty.User user : ftpProperties.getUsers())
            System.out.println(user.getName() + "," + user.getPassword());

        for (Map.Entry<String, String> entry : ftpProperties.getInfos().entrySet()) {
            System.out.println("Key : " + entry.getKey() + " ,  Value : " + entry.getValue());
        }

    }
}