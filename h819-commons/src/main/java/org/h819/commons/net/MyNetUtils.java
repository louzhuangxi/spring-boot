package org.h819.commons.net;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 * Description : TODO(Net Utils ，单例模式，通过 getInstance 方法调用)
 * User: h819
 * Date: 14-4-22
 * Time: 上午11:16
 * To change this template use File | Settings | File Templates.
 */
@Slf4j
public class MyNetUtils {

    /**
     * 非法IP地址常量。
     *
     * @since 0.12
     */
    public static final String INVALID_IP = "0.0.0.0";
    /**
     * 未知主机名常量。
     *
     * @since 0.12
     */
    public static final String UNKNOWN_HOST = "";
    //private static final Logger log = LoggerFactory.getLogger(MyNetUtils.class);

    public static void main(String[] arg) {

        //测试
        String s1 = "http://news.163.com";
        String s2 = "http://newss.163.com";
        String s3 = "newss.163.com";
        String s4 = "http://www.hlanda.gov.cn/html/index/page/000100030003_1.html";
              String url1 = "http://www.capital-std.com/innerApp";


       // System.out.println(s1 + " " + MyNetUtils.isURLAvailable(s1,3));
        System.out.println(s1 + " " + MyNetUtils.isNetServiceAvailable("129.9.100.14",1521,3));
//        System.out.println(s2 + " " + MyNetUtils.isURLAvailable(s2));
//        System.out.println(s3 + " " + MyNetUtils.isURLAvailable(s3));
 //       System.out.println(s4 + " available " + MyNetUtils.isURLAvailable(s4,3));

//        try {
//            System.out.println(MyNetUtils.getRealUrl(url1));
//        } catch (IOException e) {
//            e.printStackTrace();
//        }


    }

    /**
     * 根据主机名得到IP地址字符串。
     *
     * @param hostName 要查找地址的主机名
     * @return 对应主机的IP地址，主机名未知或者非法时返回INVALID_IP。
     */
    public static String getByName(String hostName) {
        try {
            InetAddress inet = InetAddress.getByName(hostName);
            return inet.getHostAddress();
        } catch (UnknownHostException e) {
            return INVALID_IP;
        }
    }

    /**
     * 获取客户端 ip
     *
     * @param request
     * @return
     */

//    public static String getIpAddrByRequest(HttpServletRequest request) {
//
//        String ip = request.getHeader("x-forwarded-for");
//
//        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
//            ip = request.getHeader("Proxy-Client-IP");
//        }
//        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
//            ip = request.getHeader("WL-Proxy-Client-IP");
//        }
//        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
//            ip = request.getRemoteAddr();
//        }
//        return ip;
//
//        /**
//         * jsp 中，根据ip地址跳转
//         * */
//        // String ipD = ip.split("\\.")[0];//获取 ip 地址第一位
//        //
//        // if(ipD.equals("129")){
//        // response.sendRedirect("http://129.9.100.10/innerApp");
//        // }else{
//        // response.sendRedirect("http://202.106.162.203/innerApp");
//        // }
//
//    }

    /**
     * 根据IP地址得到主机名。
     *
     * @param ip 要查找主界面的IP地址
     * @return 对应IP的主机名，IP地址未知时返回UNKNOWN_HOST，IP地址未知也可能是网络问题造成的。
     */
    public static String getHostName(String ip) {
        try {
            InetAddress inet = InetAddress.getByName(ip);
            return inet.getHostName();
        } catch (UnknownHostException e) {
            return UNKNOWN_HOST;
        }
    }

    /**
     * 仅检测服务是否可用，不验证用户名和密码
     *
     * @param host    The Host name or ip
     * @param port    The port number
     * @param timeout 判定超时时长，单位：秒
     * @return
     */
    public static boolean isNetServiceAvailable(String host, int port, int timeout) {

        boolean isOpen = true;
        Socket soc = null;
        try {

            soc = new Socket();
            soc.connect(new InetSocketAddress(host, port), timeout * 1000);
        } catch (IOException e) {
            log.info("connect timed out");
            isOpen = false;
            // e.printStackTrace();
        } finally {
            if (soc != null) {
                try {
                    soc.close();
                } catch (IOException ignored) {
                }
            }
        }

        return isOpen;
    }
}


