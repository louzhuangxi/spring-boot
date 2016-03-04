package org.h819.commons.net.html.parser.jsoup;

import org.h819.commons.MyStringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * Description : TODO()
 * User: h819
 * Date: 2014/11/26
 * Time: 15:39
 * To change this template use File | Settings | File Templates.
 */
public class JsoupExamples2 {

    private static Logger logger = LoggerFactory.getLogger(JsoupExamples2.class);

    //由于用 logback 存储无法解析网站名称的情况入数据库(error 级别)，此处信息输出用 System.out.println ，不能用 logger.info

    public static void main(String[] args) {

        //测试
        JsoupExamples2 jsoupExamples2 = new JsoupExamples2();
        try {
            jsoupExamples2.snatch();

        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    public void snatch() throws Exception {

        String url = "http://www.oracle.com/us/syndication/feeds/index.html";

        Document doc = null;

        //url 是否可访问，isTextMimeType 方法已经验证
//        if (!MyMimeTypeUtils.isTextMimeType(new URL(url))) {
//            logger.error(url + " , 'Mime Type' is not Text or 'URL' is not available, to be continue  ...");
//        }


        // 解析
        try {
            //System.out.println("begin to parse url ... ");
            // 不加userAgent会被视为爬虫
            doc = Jsoup.connect(url).timeout(10 * 1000).userAgent(JsoupContext.agent).get();
        } catch (IOException e) {
            logger.error(" Jsoup 无法建立连接 , to be continue  ...");
            // e.printStackTrace();
        }

        //by class
        Elements content = doc.getElementsByClass("innerBoxContent");

        if (!content.hasText()) {
            logger.error(url + " 网址无内容 , 抓取网页结构发生变化，应重新设计抓取程序  ...");
        }

        // System.out.println(MyStringUtils.center(" 层级 1 ", 260, "*"));
        // System.out.println( content.toString());


        // System.out.println(MyStringUtils.center(" 层级 2 ",260,"*"));
        // System.out.println(elems.toString());

        //System.out.println(StringUtils.center(" 分隔符", 80, "="));

        for (Element elem : content) {
           // System.out.println(MyStringUtils.center(" 层级 2 ",260,"*"));
          //  System.out.println(elem.toString());

            Elements elem1 = elem.select("table").select("tr");

            System.out.println(MyStringUtils.center(" 层级 3 ", 260, "*"));
            System.out.println(elem1.toString());

            for (Element elem2 : elem1) {

                Elements elem3 = elem2.select("td");

                if(elem3.size()!=4)
                    continue;

                String title = elem3.get(0).text().trim();
                String href = elem3.get(1).select("a").attr("abs:href").trim();


                // System.out.println(MyStringUtils.center(" 层级 3 ",260,"*"));

           //    String s ="  <outline text=\""+title+"\" title=\""+title+"\" type=\"rss\" xmlUrl=\""+href+"\" htmlUrl=\""+href+"\"/>";

              //  System.out.println(title+","+href);

            }




        }

    }

}

