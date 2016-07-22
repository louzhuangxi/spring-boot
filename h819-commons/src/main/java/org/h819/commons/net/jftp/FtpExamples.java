package org.h819.commons.net.jftp;

import org.h819.commons.net.jftp.client.Client;
import org.h819.commons.net.jftp.client.ClientFactory;
import org.h819.commons.net.jftp.client.auth.UserCredentials;
import org.h819.commons.net.jftp.connection.Connection;

/**
 * Description : TODO( jftp 使用方法演示)
 * User: h819
 * Date: 2015/6/2
 * Time: 16:39
 * To change this template use File | Settings | File Templates.
 */
public class FtpExamples {

    public static void main(String[] arg) {

        /**
         * sftp
         */

        String sftp_hostname = "129.9.100.14";
        int sftp_port = 22;
        String sftp_username = "root";  // root
        String sftp_password = "82797984";  // 82797984

        /**
         * ftp
         */
//        String ftphostname = "129.9.100.16"; //
//        int ftpport = 21;
//        String ftpusername = "tree";  // root
//        String ftppassword = "tree";  // 82797984

        String ftphostname = "129.9.100.10"; //
        int ftpport = 21;
        String ftpusername = "read";  // root
        String ftppassword = "readpdf";  // 82797984


        //创建连接
        Client client = new ClientFactory().createClient(ClientFactory.Protocol.FTP);
        // or new FtpClient(); new SftpClient(); new FtpsClient()
        client.setHost(ftphostname);
        client.setPort(ftpport);
        client.setCredentials(new UserCredentials(ftpusername, ftppassword));

        //如果连接比较耗时，并且有连续连接的操作，可以加入判断，省去反复连接的操作。
        Connection connection = null;
        if (!client.isconnect())
            connection = client.connect();
        connection.changeDirectory("/");


        /**
         * 下载
         */

        connection.downloadFile("/GN2/2014.02/DB11!T~1047-2013.pdf","D:\\ftpFiles\\test.pdf",false);

        /**
         * 列表
         */

//        List<JFTPFile> remoteFiles = connection.listFiles();
//        for (JFTPFile file : remoteFiles)
//            System.out.println(file.getName() + " -> " + file.getLastModified() + " -> " + file.getSize());

        /**
         * 续传
         */
        //
        // connection.downloadFile("/1.pdf", "d:\\ftp\\", true);
        // connection.uploadFile("D:\\ftp\\2.pdf","/",true);

        /**
         * 读取时间差
         */
        //
       // System.out.println("本地时间和服务器端时间差 ："+ connection.getTimeDiff());
       //  System.out.println("判断文件是否发生变化 ："+ connection.isSync("/2.csv","D:\\ftp\\2.csv"));


        //断开连接
        client.disconnect();
    }
}
