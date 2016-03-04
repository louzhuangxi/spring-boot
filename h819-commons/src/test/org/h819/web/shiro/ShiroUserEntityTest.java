package org.h819.web.shiro;


import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

//使用 Spring-Test 框架
@RunWith(SpringJUnit4ClassRunner.class)
//需要加载的spring配置文件的地址
@ContextConfiguration(locations = {"classpath:applicationContext.xml"})
//测试时，加入事务属性
@TransactionConfiguration(transactionManager = "transactionManagerMySQL", defaultRollback = false)
@Transactional
public class ShiroUserEntityTest {

    @Test
    public void testShiro() throws Exception {

    }

}