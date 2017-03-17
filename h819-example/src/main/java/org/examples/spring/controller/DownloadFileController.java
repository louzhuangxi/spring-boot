package org.examples.spring.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletResponse;
import java.io.*;

/**
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
        File file = getFile(fileName);
        InputStream in = new FileInputStream(file);
        response.setContentType(APPLICATION_PDF);
        response.setHeader("Content-Disposition", "attachment; filename=" + file.getName());
        response.setHeader("Content-Length", String.valueOf(file.length()));
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
        File file = getFile(fileName);
        byte[] document = FileCopyUtils.copyToByteArray(file);
        HttpHeaders header = new HttpHeaders();
        header.setContentType(new MediaType("application", "pdf"));
        header.set("Content-Disposition", "inline; filename=" + file.getName());
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
    @GetMapping(value = "/c", produces = APPLICATION_PDF)
    @ResponseBody
    public Resource downloadC(@RequestParam("filename") String fileName, HttpServletResponse response) throws FileNotFoundException {
        File file = getFile(fileName);
        response.setContentType(APPLICATION_PDF);
        response.setHeader("Content-Disposition", "inline; filename=" + file.getName());
        response.setHeader("Content-Length", String.valueOf(file.length()));
        return new FileSystemResource(file);
    }


    /**
     * @param fileName
     * @return
     * @throws FileNotFoundException
     */
    private File getFile(String fileName) throws FileNotFoundException {
        //获取应用内的文件，其他目录的同理
        String filePath = servletContext.getRealPath("/") + fileName;
        File fileToDownload = new File(filePath);
        if (!fileToDownload.exists()) {
            throw new FileNotFoundException("file with path: " + fileName + " was not found.");
        }
        return fileToDownload;
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
