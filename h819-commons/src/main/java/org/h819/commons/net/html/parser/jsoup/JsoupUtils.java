package org.h819.commons.net.html.parser.jsoup;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Description : TODO()
 * User: h819
 * Date: 2016/10/31
 * Time: 16:15
 * To change this template use File | Settings | File Templates.
 */
public class JsoupUtils {

    private JsoupUtils() {
    }

    /**
     * Document 是 Element 子类，也可以使用
     */

    /**
     * 需要 cookies 才能访问
     *
     * @param url             将要访问的网址
     * @param indexCookiesUrl 获取 cookies 的首页网址
     * @param method
     * @return
     */
    public static Connection getDefaultConnectionWithCookies(String url, Connection.Method method, String indexCookiesUrl) {
        // 首先通过获取 Jsoup  cookies ，之后再进行 url 访问 ，不能先 url ，之后再 cookiesUrl
        Map cookies = getIndexCookies(indexCookiesUrl);
        return Jsoup.connect(url).timeout(JsoupContants.timeout_10second).userAgent(JsoupContants.Chrome_54).method(method).cookies(cookies).ignoreHttpErrors(true).ignoreContentType(true);
    }

    public static Connection getDefaultConnectionWithCookies(String url, Connection.Method method, Map indexCookies) {
        return Jsoup.connect(url).timeout(JsoupContants.timeout_10second).userAgent(JsoupContants.Chrome_54).method(method).cookies(indexCookies).ignoreHttpErrors(true).ignoreContentType(true);
    }

    /**
     * 不需要 cookies 访问
     *
     * @param url
     * @param method
     * @return
     */
    public static Connection getDefaultConnection(String url, Connection.Method method) {
        return Jsoup.connect(url).timeout(JsoupContants.timeout_10second).userAgent(JsoupContants.Chrome_54).method(method).ignoreHttpErrors(true).ignoreContentType(true);
    }

    /**
     * 根据属性，获得其值
     *
     * @param element
     * @param attribute
     * @return
     */
    public static String getValueByAttribute(Element element, String attribute) {
        return element.attr(attribute);

    }

    public static Map getCookies(Connection.Response response) {
        return response.cookies();
    }

    /**
     * 访问首页，获取 cookies . 有的网站进行了反爬虫策略，没有访问首页的，视为爬虫
     *
     * @param url 首页 url
     * @return
     */
    private static Map getIndexCookies(String url) {
        Map map = new HashMap();
        try {
            map = Jsoup.connect(url).timeout(JsoupContants.timeout_10second).userAgent(JsoupContants.Chrome_54).method(Connection.Method.GET).execute().cookies();
        } catch (IOException e) {
            // e.printStackTrace();
        } finally {
            return map;
        }
    }
}
