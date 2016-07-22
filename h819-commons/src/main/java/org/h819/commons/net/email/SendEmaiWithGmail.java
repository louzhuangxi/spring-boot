package org.h819.commons.net.email;

import org.apache.commons.lang.text.StrTokenizer;
import org.apache.commons.mail.Email;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.HtmlEmail;
import org.apache.commons.mail.SimpleEmail;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Date;


// 查看gamil 邮件的另一个开源库
// http://code.google.com/p/gmail4j/

/**
 * 
 * 用第三方的邮件服务器发送邮件，一般都会有限制，如连续发送的时间间隔、邮件个数等 主要是邮件服务器提供商防止恶意的垃圾邮件发送
 * 所以如果是发送大量的邮件，应该自己搭建邮件服务器。
 * 
 * 用于收取Gmail邮件
 * 
 * 应用 commons email 发送 email 该类只能发送邮件 commons email commons email 实际上为 java mail
 * 的再次包装 http://commons.apache.org/email/userguide.html
 * 
 * 此次编写，commons mail 版本为 1.1
 * 
 * 应用的时候，需要加载sun的mail.jar 下载地址:http://java.sun.com/products/javamail/ 默认的 tomcat
 * 下面没有
 * 
 * 概念解释
 * 
 * cc 抄送：邮件抄送给相关的人，邮件中显示被抄送人地址 bcc 密送：邮件抄送给相关的人，邮件中不显示被抄送人地址
 * 
 * 
 * 参考 http://www.javaeye.com/topic/34614
 * http://yanghaiskys.javaeye.com/blog/259483
 * http://weishuwei.javaeye.com/blog/95582
 * 
 * 这几种格式的邮件，主要部分相同，感觉能独立出来做出一个公用的部分，但是由于基础知识不牢固，没有抽象出来 惭愧
 * 
 * 其他应用如果用到，就照着这个类写吧
 * 
 * 尝试过用 tom 的 smtp，延迟太严重，有的邮件竟然延迟了 半个月甚至更多. 另外 sohu,sina 的免费邮箱都有问题，不要采用
 * 
 * 
 * amazon 免费发送邮件
 * 
 * http://www.infoq.com/cn/news/2011/01/Amazon-SES
 * http://docs.amazonwebservices.com/ses/2010-12-01/APIReference/
 * 
 * 
 */
public class SendEmaiWithGmail {

	/**
	 * 用 commons 发送简单邮件 用 test@gmail.com (登录密码为 pass)发送邮件
	 * 
	 * @author jianghui
	 * @param userName
	 *            gmail 用户名 test
	 * @param password
	 *            gmail 密码 pass
	 * @param subject
	 *            邮件标题
	 * @param simpleEmailBody
	 *            邮件内容
	 * @param from
	 *            邮件显示的发信人.实际的发信地址为 gamil 邮箱地址
	 * @param to
	 *            收件人邮件地址
	 * @param cc
	 *            抄送人邮件地址,多个地址用分号";"隔开,如果没有，则为空字符串""
	 * @param bcc
	 *            密送人邮件地址,多个地址用分号";"隔开,如果没有，则为空字符串""
	 * @throws EmailException
	 */
	public void sendsimpleEmail(String userName, String password,
			String subject, String simpleEmailBody, String from, String to,
			String cc, String bcc) throws EmailException {

		// 创建SimpleEmail对象
		SimpleEmail email = new SimpleEmail();

		// 显示调试信息用于IED中输出
		email.setDebug(true);

		// 设置发送电子邮件的邮件服务器
		email.setHostName("smtp.gmail.com");

		// 邮件服务器是否使用ssl加密方式gmail就是，163就不是)
		email.setSSL(Boolean.TRUE);

		// 设置smtp端口号(需要查看邮件服务器的说明ssl加密之后端口号是不一样的)
		email.setSmtpPort(465);

		// 设置发送人的账号/密码
		email.setAuthentication(userName, password);

		// 显示的发信人地址,实际地址为gmail的地址
		email.setFrom(from);
		// 设置发件人的地址/称呼
		// email.setFrom("test@test.com", "发送人");

		// 收信人地址
		email.addTo(to);
		// 设置收件人的账号/称呼)
		// email.addTo("orientplaza@sohu.com", "收件人");

		// 多个抄送地址
		StrTokenizer stokenCC = new StrTokenizer(cc.trim(), ";");
		// 开始逐个抄送地址
		for (int i = 0; i < stokenCC.getTokenArray().length; i++) {
			email.addCc((String) stokenCC.getTokenArray()[i]);
		}

		// 多个密送送地址
		StrTokenizer stokenBCC = new StrTokenizer(bcc.trim(), ";");
		// 开始逐个抄送地址
		for (int i = 0; i < stokenBCC.getTokenArray().length; i++) {
			email.addBcc((String) stokenBCC.getTokenArray()[i]);
		}

		// Set the charset of the message.
		email.setCharset("UTF-8");

		email.setSentDate(new Date());

		// 设置标题,但是不能设置编码,commons 邮件的缺陷
		email.setSubject(subject);

		// 设置邮件正文
		email.setMsg(simpleEmailBody);

		// 就是send发送
		email.send();

		// 这个是我自己写的。
		System.out.println("The SimpleEmail send sucessful!");

	}

	public void sendsimpleEmail2(Email email, String userName, String password,
			String subject, String simpleEmailBody, String from, String to,
			String cc, String bcc) throws EmailException {

		// 创建SimpleEmail对象

		// 显示调试信息用于IED中输出
		email.setDebug(true);

		// 设置发送电子邮件的邮件服务器
		email.setHostName("smtp.gmail.com");

		// 邮件服务器是否使用ssl加密方式gmail就是，163就不是)
		email.setSSL(Boolean.TRUE);

		// 设置smtp端口号(需要查看邮件服务器的说明ssl加密之后端口号是不一样的)
		email.setSmtpPort(465);

		// 设置发送人的账号/密码
		email.setAuthentication(userName, password);

		// 显示的发信人地址,实际地址为gmail的地址
		email.setFrom(from);
		// 设置发件人的地址/称呼
		// email.setFrom("test@test.com", "发送人");

		// 收信人地址
		email.addTo(to);
		// 设置收件人的账号/称呼)
		// email.addTo("orientplaza@sohu.com", "收件人");

		// 多个抄送地址
		StrTokenizer stokenCC = new StrTokenizer(cc.trim(), ";");
		// 开始逐个抄送地址
		for (int i = 0; i < stokenCC.getTokenArray().length; i++) {
			email.addCc((String) stokenCC.getTokenArray()[i]);
		}

		// 多个密送送地址
		StrTokenizer stokenBCC = new StrTokenizer(bcc.trim(), ";");
		// 开始逐个抄送地址
		for (int i = 0; i < stokenBCC.getTokenArray().length; i++) {
			email.addBcc((String) stokenBCC.getTokenArray()[i]);
		}

		// Set the charset of the message.
		email.setCharset("UTF-8");

		email.setSentDate(new Date());

		// 设置标题,但是不能设置编码,commons 邮件的缺陷
		email.setSubject(subject);

		// 设置邮件正文
		email.setMsg(simpleEmailBody);

		// 就是send发送
		email.send();

		// 这个是我自己写的。
		System.out.println("The SimpleEmail send sucessful!");

	}

	/**
	 * 发送 html 格式的邮件
	 * 
	 * @param userName
	 * @param password
	 * @param subject
	 * @param from
	 * @param to
	 * @param cc
	 * @param bcc
	 * @throws EmailException
	 * @throws java.net.MalformedURLException
	 */
	public void sendHTMLEmail(String userName, String password, String subject,
			String from, String to, String cc, String bcc)
			throws EmailException, MalformedURLException {

		// 创建SimpleEmail对象
		HtmlEmail email = new HtmlEmail();

		// 显示调试信息用于IED中输出
		email.setDebug(true);

		// 设置发送电子邮件的邮件服务器
		email.setHostName("smtp.gmail.com");

		// 邮件服务器是否使用ssl加密方式gmail就是，163就不是)
		email.setSSL(Boolean.TRUE);

		// 设置smtp端口号(需要查看邮件服务器的说明ssl加密之后端口号是不一样的)
		email.setSmtpPort(465);

		// 设置发送人的账号/密码
		email.setAuthentication(userName, password);

		// 显示的发信人地址,实际地址为gmail的地址
		email.setFrom(from);
		// 设置发件人的地址/称呼
		// email.setFrom("test@test.com", "发送人");

		// 收信人地址
		email.addTo(to);
		// 设置收件人的账号/称呼)
		// email.addTo("orientplaza@sohu.com", "收件人");

		// 多个抄送地址
		StrTokenizer stokenCC = new StrTokenizer(cc.trim(), ";");
		// 开始逐个抄送地址
		for (int i = 0; i < stokenCC.getTokenArray().length; i++) {
			email.addCc((String) stokenCC.getTokenArray()[i]);
		}

		// 多个密送送地址
		StrTokenizer stokenBCC = new StrTokenizer(bcc.trim(), ";");
		// 开始逐个抄送地址
		for (int i = 0; i < stokenBCC.getTokenArray().length; i++) {
			email.addBcc((String) stokenBCC.getTokenArray()[i]);
		}

		// Set the charset of the message.
		email.setCharset("UTF-8");

		email.setSentDate(new Date());

		// 设置标题,但是不能设置编码,commons 邮件的缺陷
		email.setSubject(subject);

		// === 以上同 simpleEmail

		// ===html mail 内容
		StringBuffer msg = new StringBuffer();
		msg.append("<html><body>");
		// embed the image and get the content id

		// 远程图片
		URL url = new URL("http://www.apache.org/images/asf_logo_wide.gif");
		String cid = email.embed(url, "Apache logo");
		msg.append("<img src=\"cid:").append(cid).append("\">");

		// 本地图片
		File img = new File("d:/java.gif");
		msg.append("<img src=cid:").append(email.embed(img)).append(">");
		msg.append("</body></html>");

		// === html mail 内容

		// ==== // set the html message
		email.setHtmlMsg(msg.toString());
		// set the alternative message
		email.setTextMsg("Your email client does not support HTML messages");
		// send the email
		email.send();
		System.out.println("The HtmlEmail send sucessful!");

	}

	// 参见 commons mail 的 user guide
	public void sendMultiPartEmail() {

	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

		SendEmaiWithGmail gt = new SendEmaiWithGmail();

		// SendEmaiWithGmail gth = new
		// SendEmaiWithGmail(gth.getSampleHtmlEmailBody());

		String userName = "hellorpc";
		String password = "jh091740";
		String subject = "测试邮件,荷兰文 OriëntQ Group";
		String body = "测试邮件内容,荷兰文 OriëntQ Group";
		String from = "hello@smtp.com";
		String cc = "system112@tom.com;javabackout@sina.com";
		String to = "orientplaza@sohu.com";
		String bcc = "backupapollo@gmail.com";

		// try {
		// gt.sendsimpleEmail(userName, password, subject, body, from, to,
		// cc,bcc);
		//			
		// } catch (EmailException e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// }

	}

}
