package org.h819.commons.net.jftp;

import org.h819.commons.net.jftp.client.Client;
import org.h819.commons.net.jftp.client.ClientFactory;
import org.h819.commons.net.jftp.client.auth.UserCredentials;
import org.h819.commons.net.jftp.connection.Connection;
import org.h819.commons.net.jftp.connection.JFTPFile;

import java.util.List;

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
        int ftpport = 21;
        String ftphostname16 = "129.9.100.16"; //

        String ftpusername16 = "tree";  // root
        String ftppassword16 = "tree";  // 82797984

        String ftphostname = "129.9.100.10"; //
        String ftpusername = "read";  // root
        String ftppassword = "readpdf";  // 82797984

          String ftpusernameCanWrite = "capital";  // root
          String ftppasswordCanWrite = "capitalpass";


        //创建连接 16
        Client client = new ClientFactory().createClient(ClientFactory.Protocol.FTP);
        // or new FtpClient(); new SftpClient(); new FtpsClient()
        client.setHost(ftphostname16);
        client.setPort(ftpport);
        client.setCredentials(new UserCredentials(ftpusername16, ftppassword16));


        //创建连接 10
//        Client client = new ClientFactory().createClient(ClientFactory.Protocol.FTP);
//        // or new FtpClient(); new SftpClient(); new FtpsClient()
//        client.setHost(ftphostname);
//        client.setPort(ftpport);
//        client.setCredentials(new UserCredentials(ftpusernameCanWrite, ftppasswordCanWrite));

      //  String file ="\\DB\\DB13\\DB%1300%B31%9-1990.PDF";
        String file ="/DB/DB13\\DB13%T%1081.20-2009.PDF";

        /**
         * 当有连续操作的时候，要使用一个连接，最后断开
         */
        Connection connection = client.connect();


        /**
         * exist
         */

        System.out.println("working directory : "+connection.getWorkingDirectory());
      //  System.out.println(connection.existsFile(file));

        /**
         * 下载
         */

       // connection.downloadFile("/GN2/2014.02/DB11!T~1047-2013.pdf", "D:\\ftpFiles\\test.pdf", false);

        /**
         * 列表
         */

        List<JFTPFile> remoteFiles = connection.listFiles("/data/");

        for (JFTPFile jfile : remoteFiles)
            System.out.println(String.format("%s -> %s -> %s -> %d", jfile.getAbsolutePath(),jfile.getName() ,jfile.getLastModified(), jfile.getSize()));

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

        //断开连接，如果一次性下载多个文件，应都下载完成之后在断开连接
        client.disconnect();
    }
}
