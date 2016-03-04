package org.h819.commons;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.SystemUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.nio.charset.StandardCharsets;
import java.util.Properties;

import static java.lang.System.out;

//重新参考 commons-lang 中
//localUtils,SystemUtils
// out.println(SystemUtils.IS_OS_WINDOWS); 可以直接打印本机的操作系统类型

public class MySystemUtils extends SystemUtils {

	// jdk 1.6
	// http://java.sun.com/javase/6/docs/api/java/lang/System.html#getProperties()
	// 参考系统变量调用

	// Key <-> Description of Associated Value
	// java.version <-> Java Runtime Environment version

	// java.vendor <-> Java Runtime Environment vendor

	// java.vendor.url <-> Java vendor URL

	// java.home <-> Java installation directory

	// java.vm.specification.version <-> Java Virtual Machine specification
	// version

	// java.vm.specification.vendor <-> Java Virtual Machine specification
	// vendor

	// java.vm.specification.name <-> Java Virtual Machine specification name

	// java.vm.version <-> Java Virtual Machine implementation version

	// java.vm.vendor <-> Java Virtual Machine implementation vendor

	// java.vm.name <-> Java Virtual Machine implementation name

	// java.specification.version <-> Java Runtime Environment specification
	// version

	// java.specification.vendor <-> Java Runtime Environment specification
	// vendor

	// java.specification.name <-> Java Runtime Environment specification name

	// java.class.version <-> Java class format version number

	// java.class.path <-> Java class path

	// java.library.path <-> List of paths to search when loading libraries

	// java.io.tmpdir <-> Default temp file path

	// java.compiler <-> Name of JIT compiler to use

	// java.ext.dirs <-> Path of extension directory or directories

	// os.name <-> Operating system name

	// os.arch <-> Operating system architecture

	// os.version <-> Operating system version

	// file.separator <-> File separator ("/" on UNIX)

	// path.separator <-> Path separator (":" on UNIX)

	// line.separator <-> Line separator ("\n" on UNIX)

	// user.name <-> User's account name

	// user.home <-> User's home directory

	// user.dir <-> User's current working directory

	public static String getProperty(String key) {
		return System.getProperty(key);
	}

	/**
	 * 获取操作系统变量 有时间的时候，用 org.apache.commons.lang.SystemUtils 改写
	 * 
	 * @return
	 * @throws Exception
	 */
	// 返回当前系统变量的函数，结果放在一个Properties里边，这里只针对win2k以上的，其它系统可以自己改进
	public static Properties getOSEnv() throws Exception {
		Properties prop = new Properties();
		String OS = System.getProperty("os.name").toLowerCase();
		Process p = null;
		if (OS.indexOf("windows") > -1) {
			p = Runtime.getRuntime().exec("cmd /c set"); // 其它的操作系统可以自行处理，
		}
		BufferedReader br = new BufferedReader(new InputStreamReader(
				p.getInputStream()));
		String line;
		while ((line = br.readLine()) != null) {
			int i = line.indexOf("=");
			if (i > -1) {
				String key = line.substring(0, i);
				String value = line.substring(i + 1);
				prop.setProperty(key, value);
			}
		}
		return prop;
	}

	/**
	 * 获取系统 Java 环境变量
	 * 
	 * @return
	 */
	public static Properties getJavaEnv() {
		// 通过获得系统属性构造属性类 prop
		Properties prop = new Properties(System.getProperties());
		return prop;

		// 在标准输出中输出系统属性的内容
		// prop.list(System.out);

	}

	/**
	 * 获取电脑主机名
	 * 
	 * @return
	 */
	public static String getComputerName() {
		String hostName = "";
		try {

			// ip：apollonotebook/192.168.1.2
			String address = InetAddress.getLocalHost().toString();
			// System.out.println(address);
			hostName = StringUtils.substringBeforeLast(address, "/");
			// System.out.println(hostName);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return hostName;
	}

	/**
	 * 获取IP地址
	 * 
	 * @return
	 */
	public static String getIpAddress() {
		String ip = "";
		try {
			// ip：apollonotebook/192.168.1.2
			String address = InetAddress.getLocalHost().toString();
			// System.out.println(address);
			ip = StringUtils.substringAfterLast(address, "/");
			// System.out.println(ip);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ip;
	}

	/**
	 * 计算程序运行时间的例子
	 */
	private void getRunTime() {
		// 伪代码
		long startTime = System.currentTimeMillis(); // 获取开始时间
		// doSomeThing(); //测试的代码段
		long endTime = System.currentTimeMillis(); // 获取结束时间
		System.out.println("程序运行时间： " + (endTime - startTime) + "ms");
	}

	/**
	 * 运行 windows 上面的exe命令
	 * 
	 * @param exeCommandPath
	 *            exe 文件所在位置
	 * @throws java.io.IOException
	 */
	private void runWindowsCMDExample(String exeCommandPath) {

		// exeCommandPath = "E:\\Program Files\\Notepad++\\notepad++.exe";
		out.println(exeCommandPath);

		// ${in} -o ${out} -f -T 9 -t -s storeallcharacters -s

		File f = new File("d:\\t.pdf");

		String filePath = f.getPath();// 要打开的文件路径名，可以包含空格。

		String filePath2 = f.getParent() + FilenameUtils.getBaseName(filePath)
				+ ".swf";

		String cmdStr0[] = { "E:\\docviewer\\swftools0.9.1\\pdf2swf.exe", " ",
				filePath, " ","-o", " ", filePath2, " ","-f", " ","-T"," ","9" };

		String cmdStr[] = { "E:\\docviewer\\swftools0.9.1\\pdf2swf.exe", " ",
				filePath, "-o", filePath2, "-f", "-T", "9" };

		
		
		
		String ss = "";
		for (String s : cmdStr0)

		{
			ss = ss + s;

		}
		System.out.println(ss);

		
		ss = "cmd.exe /c"+" "+StringUtils.replace(ss, " ", "\" \"");
		
		System.out.println(ss);
		
		try {

			// rt.exec(exeCommandPath);

			Runtime.getRuntime().exec(ss);

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * 运行 windows 上面的exe命令
	 * 
	 * @param exeCommandPath
	 *            exe 文件所在位置
	 * @throws java.io.IOException
	 */
	private void runWindowsCMDExample0(String exeCommandPath) {

		Runtime run = Runtime.getRuntime();
		String[] cmd = new String[5];
		cmd[0] = "cmd";
		cmd[1] = "/c";
		cmd[2] = "start";
		cmd[3] = " ";
		cmd[4] = "E:\\docviewer\\swftools0.9.1\\pdf2swf.exe";
		
		
		
		
		
		
		try {
			run.exec(cmd);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	/**

	 * @return 系统字符集编码

	 */
	public static String systemCharset() {
		String charset = System.getProperty("file.encoding");
		if(MyStringUtils.isBlank(charset)) {
			charset = StandardCharsets.UTF_8.toString();
		}
		return charset;
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

		try {
			MySystemUtils sp = new MySystemUtils();
			// Properties p = sp.getOSEnv();
			// System.out.println(p); // 注意大小写
			// System.out.println(p.getProperty("CATALINA_HOME")); // 注意大小写
			// System.out.println(p.getProperty("TEMP")); // 注意大小写
			// sp.getComputerName();
			// sp.getIpAddress();

			// out.println(System.getProperty("os.name"));
			// out.println(SystemUtils.IS_OS_WINDOWS);

			sp.runWindowsCMDExample(null);

		} catch (Exception e) {
			System.out.println(e);
		}

	}

}
