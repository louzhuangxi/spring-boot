package org.examples.spring.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletContext;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;


/**
 * Description : TODO(文件处理)
 * User: h819
 * Date: 2015-3-3
 * Time: 下午4:47
 * To change this template use File | Settings | File Templates.
 */
@Controller
@RequestMapping("/files")
@Slf4j
public class UploadFileController {

    //private static final Logger logger = LoggerFactory.getLogger(UploadFileController.class);

    @Autowired
    private ServletContext servletContext; //获得应用的路径用

    /**
     * 文件上传
     * <p/>
     * spring boot 的配置文件 application.properties 中可以配置
     * spring.servlet.multipart.  可以配置上传文件属性
     *
     * @param file
     * @return
     */
//    <form method="POST" enctype="multipart/form-data" action="/files/upload">
//    File to upload:  <input type="file" name="file">
//    <br />
//    <br />
//    <input type="submit" value="Upload">
//    Press here to upload the file!
//    </form>
    @RequestMapping(value = "/upload", method = RequestMethod.POST)
    @ResponseBody
    public String handleFileUpload(@RequestParam("file") MultipartFile file) {
        if (file != null && !file.isEmpty()) {
            try {

                String fileName = file.getOriginalFilename();
                SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
                //文件存放路径为 webApp/upload
                FileOutputStream fos = new FileOutputStream(servletContext.getRealPath("/") +
                        "upload/" + sdf.format(new Date()) + fileName.substring(fileName.lastIndexOf('.')));
                FileCopyUtils.copy(file.getInputStream(), fos);
                log.info("save to db");
                return "You successfully uploaded " + fileName + "!";
            } catch (Exception e) {
                return "You failed to upload " + e.getMessage();
            }
        } else {
            return "You failed to upload because the file was empty.";
        }
    }

    /**
     * 上传多个文件
     *
     * @param files
     * @return
     */
    //    <form method="POST" enctype="multipart/form-data" action="/files/upload">
//    File to upload:  <input type="file" name="files">
//    File to upload:  <input type="file" name="files">
//    File to upload:  <input type="file" name="files">
//    File to upload:  <input type="file" name="files">
//    <br />
//    <br />
//    <input type="submit" value="Upload">
//    Press here to upload the file!
//    </form>
    @RequestMapping(value = "/upload", method = RequestMethod.POST)
    @ResponseBody
    public String handleMultipartFileUpload(@RequestParam("file") MultipartFile[] files) {

        return "";
    }

}
