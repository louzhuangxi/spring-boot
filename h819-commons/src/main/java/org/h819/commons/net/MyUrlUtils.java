package org.h819.commons.net;

import org.apache.commons.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.UnsupportedEncodingException;
import java.net.*;
import java.util.Properties;

/**
 * Description : TODO(Net Utils ，单例模式，通过 getInstance 方法调用)
 * User: h819
 * Date: 14-4-22
 * Time: 上午11:16
 * To change this template use File | Settings | File Templates.
 */
public class MyUrlUtils {

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
    private static Logger logger = LoggerFactory.getLogger(MyUrlUtils.class);

    public static void main(String[] arg) {

        //测试
        String s1 = "http://news.163.com";
        String s2 = "http://newss.163.com";
        String s3 = "newss.163.com";
        String s4 = "http://www.hlanda.gov.cn/html/index/page/000100030003_1.html";
        String url1 = "http://www.capital-std.com/innerApp";




        // System.out.println(s1 + " " + MyNetUtils.isURLAvailable(s1,3));
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
     * check whether a web page exists or not.
     * <p/>
     * 判断网址是否可访问
     *
     * @param url
     * @param timeout 判定超时时长，单位：毫秒
     * @return
     */
    public static boolean isURLAvailable(String url, int timeout) {
        try {
            return isURLAvailable(new URL(url), timeout);
        } catch (MalformedURLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * url 是否可访问
     *
     * @param url     url
     * @param timeout 判定超时时长，单位：毫秒
     * @return
     */
    public static boolean isURLAvailable(URL url, int timeout) {
        try {
            HttpURLConnection.setFollowRedirects(false);
            // note : you may also need
            //        HttpURLConnection.setInstanceFollowRedirects(false)
            HttpURLConnection con =
                    (HttpURLConnection) url.openConnection();
            con.setRequestMethod("HEAD");
            con.setConnectTimeout(timeout);
            return (con.getResponseCode() == HttpURLConnection.HTTP_OK);
        } catch (Exception e) {
            //  e.printStackTrace();
            return false;
        }
    }


    /**
     * check whether a web page exists or not by proxy
     * <p/>
     * 判断网址是否可访问，通过代理
     *
     * @param URLName
     * @return
     */
    public static boolean existsUrlByProxy(String URLName) {
        try {

            Properties systemSettings = System.getProperties();
            systemSettings.put("proxySet", "true");
            systemSettings.put("http.proxyHost", "proxy.mycompany.local");
            systemSettings.put("http.proxyPort", "80");

            URL u = new URL(URLName);
            HttpURLConnection con = (HttpURLConnection) u.openConnection();

            String encodedUserPwd =
                    Base64.encodeBase64String("domain\\username:password".getBytes());
            con.setRequestProperty
                    ("Proxy-Authorization", "Basic " + encodedUserPwd);
            con.setRequestMethod("HEAD");
            System.out.println
                    (con.getResponseCode() + " : " + con.getResponseMessage());
            return (con.getResponseCode() == HttpURLConnection.HTTP_OK);
        } catch (Exception e) {
            //  e.printStackTrace();
            return false;
        }
    }

    /**
     * 编码URL
     *
     * @param url     URL
     * @param charset 编码
     * @return 编码后的URL
     */
    public static String encode(String url, String charset) {
        try {
            return URLEncoder.encode(url, charset);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 解码URL
     *
     * @param url     URL
     * @param charset 编码
     * @return 解码后的URL
     */
    public static String decode(String url, String charset) {
        try {
            return URLDecoder.decode(url, charset);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }
}


