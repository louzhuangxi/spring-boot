package org.examples.spring.service;

/**
 * Description : TODO(spring mail example)
 * User: h819
 * Date: 2015/2/9
 * Time: 15:55
 * To change this template use File | Settings | File Templates.
 */

import org.apache.commons.beanutils.DynaBean;
import org.apache.commons.beanutils.LazyDynaBean;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.tools.generic.DateTool;
import org.apache.velocity.tools.generic.NumberTool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.velocity.VelocityEngineUtils;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

@Service
@Transactional(readOnly = true)
public class SpringMailService {

    @Autowired
    private JavaMailSenderImpl mailSender;

    @Autowired
    private VelocityEngine velocityEngine;

    /**
     * @param to      接收者邮件地址
     * @param subject 邮件主题
     * @param msg     邮件内容
     */
    public void sendMailTxT(String from ,String[] to, String subject, String msg) {


        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
        simpleMailMessage.setTo(to);
        simpleMailMessage.setSubject(subject);
        simpleMailMessage.setText(msg);
        simpleMailMessage.setFrom(from);
        mailSender.send(simpleMailMessage);
    }

    /**
     * Velocity 模板发送邮件 html 格式
     *
     * @param to
     * @param subject
     * @throws javax.mail.MessagingException
     */
    public void sendMailVelocity(String from ,String[] to, String subject) throws MessagingException {

        //如果不是 html 格式，修改为  SimpleMailMessage
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, StandardCharsets.UTF_8.toString());

        /**
         *邮件内容
         */
        helper.setFrom(from);
        helper.setTo(to);
        helper.setSubject(subject);

        //模板内容
        Map<String, Object> model = new HashMap<String, Object>();
        model.put("firstName", "Yashwant");
        model.put("lastName", "Chavan");
        model.put("location", "china");
        //创建动态 bean
        DynaBean dynaBean = new LazyDynaBean();
        dynaBean.set("name", "It is name"); //simple
        dynaBean.set("gender", new Integer(1));  //simple
        //设置 bean 属性

        // Velocity 工具类，实例可以直接放入 map ,在模板文件中直接使用
        // 如日期格式化 $dateTool.format("yyyy-MM-dd",$info.issueTime)
        DateTool dateTool = new DateTool();//日期工具
        NumberTool numberTool = new NumberTool();//数字工具
        model.put("dateTool",dateTool);
        model.put("numberTool",numberTool);

        model.put("bean", dynaBean);
        String text = VelocityEngineUtils.mergeTemplateIntoString(velocityEngine, "./templates/velocity_template_email-newsletter.vm", StandardCharsets.UTF_8.toString(), model);
        helper.setText(text, true);

        mailSender.send(message);
    }

    /**
     * 发送附件
     *
     * @param to
     * @param subject
     * @throws javax.mail.MessagingException
     */
    public void sendMailWithAttachment(String from ,String[] to, String subject) throws MessagingException {

        MimeMessage message = mailSender.createMimeMessage();
        // use the true flag to indicate you need a multipart message
        MimeMessageHelper helper = new MimeMessageHelper(message, true, StandardCharsets.UTF_8.toString());
        helper.addAttachment("show_name.jpg", new FileSystemResource(new File("d:/email.jpg")));

        /**
         *邮件内容
         */
        helper.setFrom(from);
        helper.setTo(to);
        helper.setSubject(subject);
        helper.setText("my text", true);


        mailSender.send(message);
    }
}
