package org.h819.commons.net.jftp.connection;

import org.h819.commons.net.jftp.client.Client;
import org.h819.commons.net.jftp.client.ClientFactory;
import org.h819.commons.net.jftp.client.auth.UserCredentials;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

/**
 * Description : TODO()
 * User: h819
 * Date: 2015/6/2
 * Time: 16:43
 * To change this template use File | Settings | File Templates.
 */
public class FtpConnectionTest {

    Client client;

    Connection connection;

    @Before
    public void setUp() throws Exception {


        String sftphostname = "129.9.100.14";
        int sftpport = 22;
        String sftpusername = "root";  // root
        String sftppassword = "82797984";  // 82797984


        String ftphostname = "129.9.100.16";
        int ftpport = 21;
        String ftpusername = "tree";  // root
        String ftppassword = "tree";  // 82797984

        client = new ClientFactory().createClient(ClientFactory.Protocol.FTP);
        // or new FtpClient(); new SftpClient(); new FtpsClient()
        client.setHost(ftphostname);
        client.setPort(ftpport);
        client.setCredentials(new UserCredentials(ftpusername, ftppassword));
//
        connection = client.connect();


    }

    @After
    public void tearDown() throws Exception {

        client.disconnect();

    }

    @Test
    public void testChangeDirectory() throws Exception {

        connection.changeDirectory("/");

    }

    @Test
    public void testListFiles() throws Exception {

        List<JFTPFile> list = connection.listFiles("/data/");

        for (JFTPFile file : list)
            System.out.println(file.getName());

    }

    @Test
    public void testListFiles1() throws Exception {

    }

    @Test
    public void testGetWorkingDirectory() throws Exception {

    }

    @Test
    public void testDownloadFile() throws Exception {

        //connection.downloadFile("/data/101.html", "d:\\000\\1", true);
        connection.downloadFile("/2.csv", "d:\\000\\1", true);

    }

    @Test
    public void testUploadFile() throws Exception {

        connection.uploadFile("d:\\101.html", "/1/3", true);

    }

    @Test
    public void testCalculateDirectoryInfo() throws Exception {

    }


    @Test
    public void testForceRemoveDirectory() throws Exception {

        connection.removeFileOrDirectory("/1/sf");

    }


    @Test
    public void testUploadDirectory() throws Exception {

        connection.uploadDirectory("d:\\12", "/1/3", true);

    }

    @Test
    public void testGetFileSize() throws Exception {

    }

    @Test
    public void testDownloadDirectory() throws Exception {

        connection.downloadDirectory("/1", "d:\\12", true);

    }

    @Test
    public void testCheckDirectoryExists() throws Exception {

    }

    @Test
    public void testCheckFileExists() throws Exception {

        System.out.println(" check file exist :" + connection.existsFile("/data/101.html"));

    }

    @Test
    public void testCalculateDirectoryInfo1() throws Exception {

    }

    @Test
    public void testrecursiveCreateDirectory() throws Exception {

        // connection.("/2/2/2/1");

    }

    @Test
    public void testisSync() throws Exception {

        //读取时间差
        // System.out.println("本地时间和服务器端时间差 ："+ connection.getTimeDiff());
        System.out.println("判断文件是否发生变化 ："+ connection.isSync("/2.csv","D:\\ftp\\2.csv"));



    }
}