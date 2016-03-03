package org.examples.app;

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
@TransactionConfiguration(transactionManager = "transactionManagerOracle", defaultRollback = false)
@Transactional
public class ShenZhenClientAppTest {



    @Test
    public void testCreatTiluTXTFromExcelFiles() throws Exception {

    }

    @Test
    public void testInsertToOracle() throws Exception {

    }

    @Test
    public void testInsertXGDToOracle() throws Exception {

    }
}