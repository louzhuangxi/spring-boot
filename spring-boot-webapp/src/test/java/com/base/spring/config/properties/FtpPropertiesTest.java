package com.base.spring.config.properties;

import org.h819.commons.MyJsonUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

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
    private FtpPropertyBean ftpProperties;

    @Test
    public void testConfigurationProperties()
    {
        MyJsonUtils.prettyPrint(ftpProperties);
    }
}