package org.examples.spring.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.util.FileCopyUtils;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletResponse;
import java.io.*;

/**
 * 需要去掉 ie 的迅雷自动启动迅雷下载插件，否则会有问题.
 * -
 * chrome 问题
 * 下载窗口不能关闭，否则 response 方式 ，chrome 下有问题。可以让浏览器休眠几秒钟(h819.js 中有相关函数)再关闭，此时 chrome 已经下载完成
 * -
 * Description : TODO()
 * User: h819
 * Date: 2017/3/16
 * Time: 9:56
 * To change this template use File | Settings | File Templates.
 */

@Controller
@RequestMapping("/download")
public class DownloadFileController {

    private static final String APPLICATION_PDF = "application/pdf";

    @Autowired
    private ServletContext servletContext; //获得应用的路径用

    /**
     * Download File via HttpServletResponse
     * Directly display the file in the browser（有的浏览器会直接打开，有的会直接下载）.
     *
     * @param response
     * @throws IOException
     */
    @GetMapping(value = "/a", produces = APPLICATION_PDF)
    @ResponseBody
    public void downloadA(@RequestParam("filename") String fileName, HttpServletResponse response) throws IOException {
        File file = getFromWebAppPathFile(fileName);
        InputStream in = new FileInputStream(file);
        response.setContentType(APPLICATION_PDF);
        response.setContentLength((int) file.length());
        response.setHeader("Content-Disposition", "attachment; filename=" + file.getName()); //filename 前面必须有空格
        FileCopyUtils.copy(in, response.getOutputStream());
    }

    /**
     * Download File via HttpEntity
     * Directly display the file in the browser（有的浏览器会直接打开，有的会直接下载）.
     *
     * @return
     * @throws IOException
     */
    @GetMapping(value = "/b", produces = APPLICATION_PDF)
    @ResponseBody
    public HttpEntity<byte[]> downloadB(@RequestParam("filename") String fileName) throws IOException {
        File file = getFromWebAppPathFile(fileName);
        byte[] document = FileCopyUtils.copyToByteArray(file);
        HttpHeaders header = new HttpHeaders();
        header.setContentType(new MediaType("application", "pdf"));
        header.set("Content-Disposition", "inline; filename=" + file.getName());   //filename 前面必须有空格
        header.setContentLength(document.length);
        return new HttpEntity<>(document, header);
    }

    /**
     * Download File via Resource
     * class path 内的文件
     *
     * @param response
     * @return
     * @throws FileNotFoundException
     */
    @GetMapping(value = "/c", produces = APPLICATION_PDF) // pdf
    @ResponseBody
    public void downloadClassPathFile(@RequestParam("filename") String fileName, HttpServletResponse response) throws IOException {
        File file = getFromClassPath(fileName);
        response.setContentType(APPLICATION_PDF); // excel : MimeTypeUtils.APPLICATION_OCTET_STREAM_VALUE
        response.setContentLength((int) file.length());
        response.setHeader("Content-Disposition", "attachment; filename=" + file.getName());   //filename 前面必须有空格
        response.setHeader("Content-Length", String.valueOf(file.length()));
        FileCopyUtils.copy(new FileInputStream(file), response.getOutputStream());
    }


    /**
     * 获得 web app 应用目录下的文件
     * 以相对于 Web 应用根目录的方式进行访问
     * @param fileName
     * @return
     * @throws FileNotFoundException
     */
    private File getFromWebAppPathFile(String fileName) throws FileNotFoundException {
        //获取应用内的文件，其他目录的同理
        String filePath = servletContext.getRealPath("/") + fileName;
        File file = new File(filePath);
        if (!file.exists()) {
            throw new FileNotFoundException("file with path: " + fileName + " was not found.");
        }
        return file;
    }


    /**
     * 获取 classpath 中的文件
     *
     * @param fileName
     * @return
     */
    public File getFromClassPath(String fileName) throws FileNotFoundException {
        return ResourceUtils.getFile("classpath:" + fileName);
    }


    /**
     * 防盗链措施
     * 判断 referer 参数，这个方法不安全，容易伪造
     */
    private void check(String referer) {
        //Check the renderer
        if (referer != null && !referer.isEmpty()) {
            //do nothing
            //or send error
            //
        }
    }

}
