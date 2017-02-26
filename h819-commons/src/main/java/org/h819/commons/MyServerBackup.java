package org.h819.commons;

import org.h819.commons.exe.ExecParameter;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 * Description : TODO()
 * User: h819
 * Date: 2017-01-20
 * Time: 20:58
 * To change this template use File | Settings | File Templates.
 */
public class MyServerBackup {


    public static void main(String[] args) throws IOException {
        MyServerBackup backup = new MyServerBackup();
        String cmdPath = "C:\\Program Files\\MySQL\\MySQL Server 5.7\\bin\\mysqldump.exe";
        //  backup.mysqlBack1("127.0.0.1", 3306, "root", "root",new File("d:\\backup.sql"),""); // 家里机器
        //  backup.mysqlBack1("127.0.0.1", 3306, "root", "123456",new File("d:\\backup.sql"),"ztree"); //单位机器
        backup.mysqlDumpBackUp(cmdPath, "127.0.0.1", 3306, "root", "123456", new File("d:\\backup.sql"), "ztree");

    }

    /**
     * 使用前需要先关闭警告信息
     * -
     * mysqldump: [Warning] Using a password on the command line interface can be insecure.
     *
     * @param hostIp
     * @param port
     * @param username
     * @param password
     */
    @Deprecated // 用 Runtime ，此方法不好使
    private void mysqlBack1(String hostIp, int port, String username, String password, File backup, String dbName) {
        List<ExecParameter> list = new ArrayList<ExecParameter>();
//        list.add(new ExecParameter("-h", hostIp)); // 只有 key ，没有 value
//        list.add(new ExecParameter("-P", String.valueOf(port)));
        list.add(new ExecParameter("--password=" + password, MyConstants.ExecEmptyValue));// --password[=password], -p[password] -p 是中间没有空格
        list.add(new ExecParameter("--lock-all-tables", MyConstants.ExecEmptyValue));
        list.add(new ExecParameter("--quick", MyConstants.ExecEmptyValue));
        list.add(new ExecParameter("-r", backup.getAbsolutePath()));
        list.add(new ExecParameter(dbName, MyConstants.ExecEmptyValue));
        // MyExecUtils.exec(Paths.get("E:\\program\\mysql-5.7.11-winx64\\bin\\mysqldump.exe"), list, 0); // 家里机器
        MyExecUtils.exec(Paths.get("C:\\Program Files\\MySQL\\MySQL Server 5.7\\bin\\mysqldump.exe"), list, 0);  //单位机器


    }

    public void mysqlDumpBackUp(String mysqlDumpCmdPath, String hostIp, int port, String username, String password, File backup, String dbName) throws IOException {

        if (!Files.exists(Paths.get(mysqlDumpCmdPath)))
            throw new IOException(mysqlDumpCmdPath + " not exist");

        StringBuilder cmdBuilder = new StringBuilder();
        cmdBuilder.append(MyExecUtils.formatRuntimeCmdPath(mysqlDumpCmdPath)).append(" ");
        cmdBuilder.append("-h").append(" ").append(hostIp).append(" ");
        cmdBuilder.append("-P").append(" ").append(String.valueOf(port)).append(" ");
        cmdBuilder.append("--quick").append(" ");
        cmdBuilder.append("--lock-tables").append(" ");
        cmdBuilder.append("-u").append(" ").append(username).append(" ");
        cmdBuilder.append("-p").append(password).append(" ");
        cmdBuilder.append("-r").append(" ").append(backup.getAbsoluteFile()).append(" ");
        String[] command = new String[]{"cmd.exe", "/c", cmdBuilder.toString(), dbName};

//        String[] command = new String[]{"cmd.exe", "/c",
//                "C:\\mysqldump.exe --quick --lock-tables -u root -p123456 -r d:\\ztree.sql", "ztrees"};
        //  System.out.println(Arrays.asList(command));
        final Process process;

        try {
            process = Runtime.getRuntime().exec(command);
            // write process output line by line to file
            if (process != null && process.waitFor() == 0) {
                //normally terminated, a way to read the output
                InputStream inputStream = process.getInputStream();
                byte[] buffer = new byte[inputStream.available()];
                inputStream.read(buffer);
                System.out.println(new String(buffer));
            } else {
                // abnormally terminated, there was some problem
                //a way to read the error during the execution of the command
                InputStream errorStream = process.getErrorStream();
                byte[] buffer = new byte[errorStream.available()];
                errorStream.read(buffer);
                System.err.println(new String(buffer));
            }

        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }
}
