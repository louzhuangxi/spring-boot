package org.h819.commons;

import org.apache.commons.lang3.StringUtils;
import org.apache.tika.Tika;
import org.h819.commons.entity.MimeTypeEntity;
import org.h819.commons.net.MyUrlUtils;
import org.h819.commons.net.html.parser.jsoup.JsoupContext;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Description : TODO(Mime Type 类，读取文件内容判断，不仅仅是根据扩展名)
 * 方法一： 使用 java.net.URL : 速度很慢
 * <p>
 * 方法二： JMimeMagic http://sourceforge.net/projects/jmimemagic/ ： 已经不更新
 * <p>
 * 方法三： mime-util http://sourceforge.net/projects/mime-util/  ： 已经不更新
 * <p>
 * 方法四： Apache Tika  https://tika.apache.org/ ： 活跃项目，解析各种文档，不仅仅是检测 Mime Type，本类仅用来检测 Mime Type
 * <p>
 * <p>
 * 参考：http://www.freeformatter.com/mime-types-list.html
 * <p>
 * 参考：http://en.wikipedia.org/wiki/Internet_media_type
 * <p>
 * User: h819
 * Date: 14-4-24
 * Time: 下午2:00
 * <p>
 */

//Files.probeContentType(path) 根据扩展名判断，不准确
public class MyMimeTypeUtils {

    private static Logger logger = LoggerFactory.getLogger(MyMimeTypeUtils.class);

    private static String propertiesFileName = "mimetype.properties";


    public static void main(String[] arg) {

        /**
         * 获得 Mime type
         */
        //  MyMimeTypeUtils.getMimeType();

        try {


            System.out.println(" url : " + MyMimeTypeUtils.detect(new URL("http://hlj.zgjsks.com/html/jszp/kszx/ggxx/"), 3));
            System.out.println(MyMimeTypeUtils.detect(new URL("http://hlj.zgjsks.com/"), 3));
            System.out.println(MyMimeTypeUtils.detect(new URL("http://www.jiaoshizhaopin.net/Tianjin/index_1.html"), 3));
            System.out.println(MyMimeTypeUtils.detect(new URL("http://hrss.jl.gov.cn/zxgg/201403/P020140310358023531108.rar"), 3));
            System.out.println(MyMimeTypeUtils.detect(new URL("http://hrss.jl.gov.cn/sydwrsgl/201403/t20140321_1635860.html"), 3));
            System.out.println(MyMimeTypeUtils.detect(new URL("http://www.canhelp.cn:8080/jpa/jpa/admin-index.html"), 3));

            System.out.println(StringUtils.center("split", 80, "="));


            System.out.println(StringUtils.center("split", 80, "="));


        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 获得所有 mime type entity
     *
     * @return
     */
    public static List<MimeTypeEntity> getAllMimeTypeEntities() {

        MyConfigurationPropertiesUtils conf;
        List<MimeTypeEntity> list = new ArrayList<MimeTypeEntity>();
        conf = new MyConfigurationPropertiesUtils(propertiesFileName);

        for (String fileExtension : conf.getKeys()) {
            String[] temp = conf.getStringArrayByEncoding(fileExtension, "GBK");
            MimeTypeEntity mimeTypeEntity = new MimeTypeEntity();

            mimeTypeEntity.setFileExtension(fileExtension);
            mimeTypeEntity.setType(temp[0]);
            mimeTypeEntity.setName(temp[1]);
            list.add(mimeTypeEntity);
        }
        return list;
    }

    /**
     * 根据 扩展名，获得 Mime Type entity
     *
     * @return
     */
    public static MimeTypeEntity getMimeTypeEntity(String fileExtension) {

        MyConfigurationPropertiesUtils conf;
        conf = new MyConfigurationPropertiesUtils(propertiesFileName);

        MimeTypeEntity mimeTypeEntity = new MimeTypeEntity();

        for (String fileExtensionTemp : conf.getKeys()) {
            if (fileExtensionTemp.equals(fileExtension)) {
                String[] temp = conf.getStringArrayByEncoding(fileExtensionTemp, "GBK");
                mimeTypeEntity.setFileExtension(fileExtensionTemp);
                mimeTypeEntity.setType(temp[0]);
                mimeTypeEntity.setName(temp[1]);
                break;
            }
        }
        return mimeTypeEntity;
    }


    /**
     *  Tika 类中还有一些判断方法
     *
     *  因为 Tika 要解析 File, URL 数据流，所以解析需要一定时间。
     *
     *  对于已知扩展名的，用 String 方法，不知道的，用 File, URL 方法
     */

    /**
     * ======================================================================
     */
    /**
     * 根据扩展名判断 mime type ，如果无扩展名，则用 File 或 URL 判断
     * <p/>
     * Detects the media type of a document with the given file name. The type detection is based on known file name extensions.
     * The given name can also be a URL or a full file path. In such cases only the file name part of the string is used for type detection.
     *
     * @param name the file name of the document
     * @return detected media type
     */

    /**
     * 利用 Tika 分析 Mime Type
     * 因为 Tika 要解析 File 、 URL 数据流，所以解析需要一定时间。不要用解析扩展名的方法，无法动态判断，不准。
     * <p>
     * Parses the given file and returns the extracted text content.
     *
     * @param file
     * @return
     */
    public static String detect(File file) throws Exception {

        //文件不存在
        if (!file.exists()) {
            throw new Exception("exception ! " + file.getAbsoluteFile() + " not existes.");
        }
        Tika t = new Tika();
        return t.detect(file);

    }

    /**
     * 利用 Tika 分析 Mime Type
     * 因为 Tika 要解析 File 、 URL 数据流，所以解析需要一定时间。不要用解析扩展名的方法，无法动态判断，不准。
     * Parses the resource at the given URL and returns the extracted text content.
     *
     * @param url
     * @return
     */
    public static String detect(URL url, int timeout) throws Exception {

        //网址不存在
        if (!MyUrlUtils.isURLAvailable(url, timeout)) {
            throw new Exception("exception ! " + url.getAuthority() + " not available");
        }
        Tika t = new Tika();
        return t.detect(url);
    }

    /**
     * 默认超时 5 秒
     *
     * @param url
     * @return
     * @throws Exception
     */
    public static String detect(URL url) throws Exception {
        return detect(url, 5);
    }


    /**
     * @param file
     * @return
     */
    public static boolean isTextMimeType(File file) {
        try {
            return detect(file).startsWith("text/") || detect(file).startsWith("application/xhtml+xml");
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * @param url
     * @return
     */
    public static boolean isTextMimeType(URL url) {
        try {
            return detect(url).startsWith("text/") || detect(url).startsWith("application/xhtml+xml");
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 在线获取 mime type ，打印的结果写入到 properties 文件
     * <p>
     * 参考：http://www.freeformatter.com/mime-types-list.html
     */
    private void printMimeType() {

        Document doc = null;
        //获取网址
        String url = "http://www.freeformatter.com/mime-types-list.html";

        if (!MyUrlUtils.isURLAvailable(url, 3)) {
            logger.info(url + "   not exist,continue  ...");

        }

        try {
            // 不加userAgent会被视为爬虫
            //网址连接时间长，防止超时，设置为 30s
            doc = Jsoup.connect(url).timeout(30 * 1000).userAgent(JsoupContext.agent)
                    .get();
        } catch (IOException e) {
            e.printStackTrace();

        }

        //by class
        Elements content = doc.getElementById("mime-types-list").select("tr");

        //System.out.println(content);

        for (Element em : content) {
            Elements conts = em.select("td");
            //  System.out.println(contents);

            // System.out.println(MyStringUtils.center("splite",80,"="));
            if (!conts.isEmpty()) {

                String fileExtension = conts.get(2).text().trim();
                String mimetype = conts.get(1).text().trim();
                String name = conts.get(0).text().trim();

                /**
                 * 特殊情况处理
                 */
                //处理  Pretty Good Privacy	application/pgp-encrypted
                if (fileExtension.equals(""))
                    fileExtension = ".privacy";
                //处理 .mp4 ，有两个 mime type ,更改其中一个 fileExtension
                if (fileExtension.equals(".mp4") && mimetype.equals("video/mp4"))
                    fileExtension = ".mp4.video";


                String s = "#" + name + "\n" +
                        fileExtension + "=" + mimetype + "," + conts.get(0).text().trim();

                System.out.println(s);
                // System.out.println(MyStringUtils.center("splite", 80, "="));
            }

            System.out.println("");
        }
    }

}
