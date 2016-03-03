package org.examples.spring.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

import javax.mail.MessagingException;

//使用 Spring-Test 框架
@RunWith(SpringJUnit4ClassRunner.class)
//需要加载的spring配置文件的地址
@ContextConfiguration(locations = {"classpath:applicationContext.xml"})
//测试时，加入事务属性
@TransactionConfiguration(transactionManager = "transactionManagerMySQL", defaultRollback = false)
@Transactional
public class SpringMailServiceTest {
    @Autowired
    SpringMailService mailService;
    private String to = "172986686@qq.com";
    private String from = "84579197@qq.com";

    @Test
    public void testSendMailTxT() throws Exception {
        String subject = "spring mail test - txt";
        String msg = "it is txt content";
        mailService.sendMailTxT(from, new String[]{to, "h81900@outlook.com"}, subject, msg);

    }

    @Test
    public void testSendMailVelocity() throws Exception {
        String subject = "spring mail test - velocity";
        String msg = "it is velocity content";
        // mailService.sendMailTxT( new String[]{to}, subject, msg);
        try {
            mailService.sendMailVelocity(from, new String[]{to, "h81900@outlook.com"}, subject);
        } catch (MessagingException e) {
            e.printStackTrace();
        }

    }

    @Test
    public void testSendMailWithAttachment() throws Exception {
        String subject = "spring mail test - WithAttachment";
        String msg = "it is WithAttachment content";
        // mailService.sendMailTxT( new String[]{to}, subject, msg);
        try {
            mailService.sendMailWithAttachment(from, new String[]{to}, subject);

        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }
}
