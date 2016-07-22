package org.h819.commons.net.html.parser.jsoup;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;

/**
 * Description : TODO()
 * User: h819
 * Date: 14-4-14
 * Time: 下午2:39
 * To change this template use File | Settings | File Templates.
 */

//http://jsoup.org/cookbook/
public class JsoupExamples {


    public static void main(String[] args) throws IOException {
        JsoupExamples jsoupUtils = new JsoupExamples();
        //jsoupUtils.test();
        // jsoupUtils.ListLinks();
     //jsoupUtils.getZgjsksJobInfo("");
        jsoupUtils.ListLinks();


    }

    private static void print(String msg, Object... args) {
        System.out.println(String.format(msg, args));
    }

    private static String trim(String s, int width) {
        if (s.length() > width)
            return s.substring(0, width - 1) + ".";
        else
            return s;
    }

    public void getZgjsksJobInfo(String urls) {


        String url = "http://hlj.zgjsks.com/html/jszp/kszx/ggxx/";


        Document doc = null;
        try {
            // 不加userAgent会被视为爬虫
            doc = Jsoup.connect(url).userAgent("Mozilla/5.0 (Windows; U; Windows NT 5.2) Gecko/2008070208 Firefox/3.0.1 ")
                    .get();
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }

        //by class
        Elements content = doc.getElementsByClass("kszx_left_list");

        if (content.isEmpty()) {
            System.out.println("content is empty");
            return;
        }


        //System.out.println(content);
        Elements elems = content.select("li"); // 3、通过选择器,把包含 li 标签的数据提取出来
        // System.out.println(elems);

        for (Element li : elems) {

            Elements elemsli = li.select("span");
            Elements elemsa = li.select("a");



            //abs:href
            //To get an absolute URL from an attribute that may be a relative URL, prefix the key with abs
            //String url = a.attr("abs:href");
            System.out.println(" date :" + elemsli.text() + "   " + "herf :" + elemsa.attr("abs:href") + "   " + "text :" + elemsa.text());

        }
    }

    public void test() {
        Document doc;

        String url = "http://jsoup.org/cookbook/";

        try {
            doc = Jsoup.connect(url)
                    .get();
            ;
            System.out.println(doc.getAllElements());


        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void ListLinks() throws IOException {

        String url = "http://www.oracle.com/us/syndication/feeds/index.html";
        Document doc = Jsoup.connect(url).get();
        Elements links = doc.select("a[href]");
        Elements media = doc.select("[src]");
        Elements imports = doc.select("link[href]");
        //=======
        print("\nMedia: (%d)", media.size());
        for (Element src : media) {
            if (src.tagName().equals("img"))
                print(" * %s: <%s> %sx%s (%s)",
                        src.tagName(), src.attr("abs:src"), src.attr("width"), src.attr("height"),
                        trim(src.attr("alt"), 20));
            else
                print(" * %s: <%s>", src.tagName(), src.attr("abs:src"));
        }
        //==========
        print("\nImports: (%d)", imports.size());
        for (Element link : imports) {
            print(" * %s <%s> (%s)", link.tagName(), link.attr("abs:href"), link.attr("rel"));
        }
        //=======
        print("\nLinks: (%d)", links.size());
        for (Element link : links) {
            print(" * a: <%s>  (%s)", link.attr("abs:href"), trim(link.text(), 35));
        }
    }

}

