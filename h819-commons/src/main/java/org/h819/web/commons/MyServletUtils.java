package org.h819.web.commons;

import org.apache.commons.lang3.StringUtils;
import org.springframework.util.Assert;

import javax.servlet.http.HttpServletRequest;
import java.io.FileNotFoundException;

/**
 * @author H819
 * @version V1.0
 * @Title: ServletUtils.java
 * @Description: TODO(web 应用, HttpServletRequest 工具类)
 * @date 2016-2-16
 */

// http://www.blogjava.net/meil/archive/2006/10/10/73908.html
//import org.springframework.web.util.WebUtils;
public class MyServletUtils {

    /**
     * 静态调用
     */
    private MyServletUtils() {
    }


    /** example :
     *  应用的 url : http://localhost:8888/base/
     *  浏览器请求 url : http://localhost:8888/base/menu/ajax/index.html?page=index&tag=menu
     * ---
     * request.getScheme() = http
     * request.getServerName() = localhost
     * request.getServerPort() = 8888
     * request.getContextPath() = base
     * request.getServletPath() = /menu/ajax/index.html
     * request.getRequestURI() = /base/menu/ajax/index.html
     * request.getRequestURL() = http://localhost:8888/base/menu/ajax/index.html
     * request.getLocalAddr() = 0:0:0:0:0:0:0:1
     * request.getLocalAddr() = 0:0:0:0:0:0:0:1
     * request.getQueryString() = page=index&tag=menu
     * */

    /**
     * 获取应用的 url, 将返回 http://localhost:8888/base/
     *
     * @param request
     * @return
     */
    public static String getAppPath(HttpServletRequest request) {

        return StringUtils.remove(request.getRequestURL().toString(), request.getServletPath());

    }

    /**
     * 获取完整的 url : http://localhost:8888/base/menu/ajax/index.html?page=index&tag=menu
     *
     * @param request
     * @return
     */
    public static String getFullPath(HttpServletRequest request) {
        if (request.getQueryString() != null)
            return request.getRequestURL().toString() + "?" + request.getQueryString();
        else return request.getRequestURL().toString();
    }

    /**
     * 取得响应 url 请求，所对应的文件的物理路径
     * Gets the real path corresponding to the given virtual path.
     *
     * @param request the request of servlet context of the web application
     * @param path    the path within the web application, the virtual path to be translated to a real path.
     *                web 应用根目录访问 url 为 http://localhost:8888/appname/
     *                path 值应为 "http://localhost:8888/appname/xxx"  xxx 部分
     *                如果想要得到响应 http://localhost:8888/appname/ 请求对应的物理文件，path 为 "/" 即可
     * @return the corresponding real path
     * @throws FileNotFoundException if the path cannot be resolved to a resource
     * @see javax.servlet.ServletContext#getRealPath
     */
    public static String getRealPath(HttpServletRequest request, String path) throws FileNotFoundException {
        Assert.notNull(request, "request must not be null");
        // Interpret location as relative to the web application root directory.
        if (!path.startsWith("/")) {
            path = "/" + path;
        }
        String realPath = request.getServletContext().getRealPath(path);
        if (realPath == null) {
            throw new FileNotFoundException(
                    "request resource [" + path + "] cannot be resolved to absolute file path - " +
                            "web application archive not expanded?");
        }

        return realPath;
    }

    /**
     * 判断是否是 ajax 请求
     */
    public static boolean isAjaxRequest(HttpServletRequest request) {
        String header = request.getHeader("X-Requested-With");
        if (header != null && "XMLHttpRequest".equals(header)) {
            return true;
        }
        return false;
    }
}
