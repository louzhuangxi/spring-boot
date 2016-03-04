package org.h819.commons;

/**
 * Description : TODO()
 * User: h819
 * Date: 2015/3/23
 * Time: 17:39
 * To change this template use File | Settings | File Templates.
 */

import org.apache.commons.net.smtp.SMTPClient;
import org.apache.commons.net.smtp.SMTPReply;
import org.xbill.DNS.Lookup;
import org.xbill.DNS.Record;
import org.xbill.DNS.Type;

import java.io.IOException;

public class MyEmailUtils {

    /**
     * 利用 dnsjava 和 commons.net ，验证 email 地址是否合法
     *
     * @param emailAddress
     * @return
     */
    public static boolean validateEmailAddress(String emailAddress) {

        if (!emailAddress.matches("[\\w\\.\\-]+@([\\w\\-]+\\.)+[\\w\\-]+")) {
            System.err.println("Format error");
            return false;
        }

        String log = "";
        String host = "";
        String hostName = emailAddress.split("@")[1];
        Record[] result = null;
        SMTPClient client = new SMTPClient();

        boolean mxServerAvilabe = false;

        try {
            // 根据域名，查找 MX 记录，是否存在。
            Lookup lookup = new Lookup(hostName, Type.MX);
            lookup.run();

//            for (Record r : lookup.getAnswers())
//                System.out.println(r);

            if (lookup.getResult() != Lookup.SUCCESSFUL) {
                log += "找不到MX记录\n";
                return false;
            } else {
                result = lookup.getAnswers();
            }

            // 连接到邮箱服务器，用 SMTPClient 探测 MX 的域名，是否能访问。
            for (int i = 0; i < result.length; i++) {
                host = result[i].getAdditionalName().toString();

                client.connect(host);

                if (!SMTPReply.isPositiveCompletion(client.getReplyCode())) {

                    client.disconnect();
                    log += "SMTP server " + host + " refused connection.";
                    continue;

                } else {
                    log += "MX record about " + hostName + " exists.\n";
                    log += "Connection succeeded to " + host + "\n";
                    mxServerAvilabe = true;
                    break;
                }
            }

            return mxServerAvilabe;

            // 下面代码是模拟登陆，但 outlook.com 邮箱不允许本机登陆，所以下面代码不能运行。
//            log += client.getReplyString();
//            // HELO cyou-inc.com
//            client.login("cyou-inc.com");
//            log += ">HELO cyou-inc.com\n";
//            log += "=" + client.getReplyString();
//
//            // MAIL FROM: <zhaojinglun@cyou-inc.com>
//            client.setSender("zhaojinglun@cyou-inc.com");
//            log += ">MAIL FROM: <zhaojinglun@cyou-inc.com>\n";
//            log += "=" + client.getReplyString();
//
//            // RCPT TO: <$emailAddress>
//            client.addRecipient(emailAddress);
//            log += ">RCPT TO: <" + emailAddress + ">\n";
//            log += "=" + client.getReplyString();
//
//            if (250 == client.getReplyCode()) {
//                return true;
//            }

        } catch (Exception e) {

            e.printStackTrace();

        } finally {

            try {

                client.disconnect();

            } catch (IOException e) {
            }
            // 打印日志
            System.err.println(log);
        }

        return false;
    }

    public static void main(String[] args) {
//        System.err.println("Outcome: "
//                + MyEmailUtils.validateEmailAddress("1729866861@qq.com"));
        System.err.println("Outcome: "
                + MyEmailUtils.validateEmailAddress("h81900@outlook.com")); //
    }
}