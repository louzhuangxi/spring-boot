package org.h819.commons;

import org.h819.commons.exe.ExecParameter;

import java.io.*;
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
public class MyBackup {


    public static void main(String[] args) {
        MyBackup backup = new MyBackup();
        backup.mysqlBack1("127.0.0.1", 3306, "root", "root");
        // backup.mysqlBack2();

    }

    /**
     * 使用前需要先关闭警告信息
     *
     * @param hostIp
     * @param port
     * @param username
     * @param password
     */
    private void mysqlBack1(String hostIp, int port, String username, String password, File backup, String dbName) {
        List<ExecParameter> list = new ArrayList<ExecParameter>();
//        list.add(new ExecParameter("-h", hostIp)); // 只有 key ，没有 value
//        list.add(new ExecParameter("-P", String.valueOf(port)));
        list.add(new ExecParameter("--user=" + username, MyConstants.ExecEmptyValue));
        list.add(new ExecParameter("--password=" + password, MyConstants.ExecEmptyValue));// --password[=password], -p[password] -p 是中间没有空格
        list.add(new ExecParameter("--lock-all-tables", MyConstants.ExecEmptyValue));
        list.add(new ExecParameter("--quick", MyConstants.ExecEmptyValue));

        list.add(new ExecParameter("-r", backup.getAbsolutePath()));
        list.add(new ExecParameter(dbName, MyConstants.ExecEmptyValue));
        MyExecUtils.exec(Paths.get("E:\\program\\mysql-5.7.11-winx64\\bin\\mysqldump.exe"), list, 0);

    }

    private void mysqlBack2() {
        File fbackup = new File("C:/backup.sql");
        // execute mysqldump command
        String[] command = new String[]{"cmd.exe", "/c", "E:\\program\\mysql-5.7.11-winx64\\bin\\mysqldump.exe --quick --lock-tables -u root -proot -r d:\\ztree.sql", "ztree"};
        System.out.println(command);
        final Process process;
        try {
            process = Runtime.getRuntime().exec(command);
            // write process output line by line to file
            if (process != null) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            try (BufferedReader reader = new BufferedReader(new InputStreamReader(new DataInputStream(process.getInputStream())));
                                 BufferedWriter writer = new BufferedWriter(new FileWriter(fbackup))) {
                                String line;
                                while ((line = reader.readLine()) != null) {
                                    writer.write(line);
                                    writer.newLine();
                                }
                            }
                        } catch (Exception ex) {
                            // handle or log exception ...
                        }
                    }
                }).start();
            }
            if (process != null && process.waitFor() == 0) {
                System.out.println("Backup created successfully");
            } else {
                System.out.println("Could not create the backup");
            }

        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }
}
