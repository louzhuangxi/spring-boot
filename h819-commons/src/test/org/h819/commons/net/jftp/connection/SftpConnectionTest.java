package org.h819.commons.net.jftp.connection;

import org.h819.commons.net.jftp.client.Client;
import org.h819.commons.net.jftp.client.ClientFactory;
import org.h819.commons.net.jftp.client.auth.UserCredentials;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * Description : TODO()
 * User: h819
 * Date: 2015/6/4
 * Time: 10:30
 * To change this template use File | Settings | File Templates.
 */
public class SftpConnectionTest {


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

        client = new ClientFactory().createClient(ClientFactory.Protocol.SFTP);
        // or new FtpClient(); new SftpClient(); new FtpsClient()
        client.setHost(sftphostname);
        client.setPort(sftpport);
        client.setCredentials(new UserCredentials(sftpusername, sftppassword));
//
        connection = client.connect();

    }

    @After
    public void tearDown() throws Exception {

        client.disconnect();

    }

    @Test
    public void testListFiles() throws Exception {

    }

    @Test
    public void testDownloadFile() throws Exception {

        connection.downloadFile("/etc/passwd", "d://8/3",true);

    }

    @Test
    public void testDownloadDirectory() throws Exception {

        connection.downloadDirectory("/etc/lp/", "d://10/5",true);

    }

    @Test
    public void testUploadFile() throws Exception {
        connection.uploadFile("D:\\10\\5\\Systems", "/1/2",true);
    }

    @Test
    public void testUploadDirectory() throws Exception {

        connection.uploadDirectory("D:\\00\\spring-boot-sample-data-jpa", "/1/",true);

    }

    @Test
    public void testDirectoryInfo() throws Exception {

    }

    @Test
    public void testRemoveFileOrDirectory() throws Exception {

        connection.removeFileOrDirectory("/1/src/main/java");

    }

    @Test
    public void testExistFile() throws Exception {

        // System.out.println("sftp file exist  :" + connection.existFile("/etc/lp/logs"));
        System.out.println("sftp file exist  :" + connection.existsFile("/etc/passwd"));

    }

    @Test
    public void testExistDirectory() throws Exception {

        System.out.println("sftp directory exist  :" + connection.existsDirectory("/etc/passwd"));

    }

    @Test
    public void testCreateDirectory() throws Exception {

        //  connection.createDirectory("/2/3/");

    }
}