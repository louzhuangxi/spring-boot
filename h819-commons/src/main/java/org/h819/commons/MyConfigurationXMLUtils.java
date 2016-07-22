package org.h819.commons;

/**
 * 参考 org.h819.net.ftp.FTPClientUtils 实现
 *
 */

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.HierarchicalConfiguration;
import org.apache.commons.configuration.XMLConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;


public class MyConfigurationXMLUtils {

    private static Logger logger = LoggerFactory.getLogger(MyConfigurationXMLUtils.class);
    //如果需要对 config 进行设置，或直接调用 config 的方法，通过 get 方法获得 config 后，再进行设置
    private XMLConfiguration config;

    /**
     * 禁止无参数实例化
     */
    private MyConfigurationXMLUtils() {

    }

    public MyConfigurationXMLUtils(String xmlFileName) {

        try {
            XMLConfiguration config = new XMLConfiguration(xmlFileName);
            // do something with config
        } catch (ConfigurationException cex) {
            // something went wrong, e.g. the file was not found
        }
    }

    public static void main(String[] args) {

        MyConfigurationXMLUtils configuration = new MyConfigurationXMLUtils();
        configuration.getHierarchicalExample();


    }

    public XMLConfiguration getConfig() {
        return config;
    }

    /**
     * 得到指定名称的属性的字符串值。属性值中不能有标点逗号 "," "="      如果确实需要 ，则用转义字符分开即 "\,"
     * <p/>
     * getString 方法，返回的是 ISO-8859-1 编码字符串，如果是中文的话，会乱码。应进行转码
     *
     * @param key      属性名称
     * @param Encoding 编码格式 ，如 GBK, UTF-8 等
     * @return
     * @throws java.io.UnsupportedEncodingException
     */
    public String getStringByEncoding(String key, String Encoding) {

        try {

            return new String(config.getString(key).getBytes("ISO-8859-1"), Encoding);

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 得到指定名称的属性的字符串值,为多值字符串,值之间用","分开 如 keys=Deur 3,Deur 4,Deur 5
     *
     * @param key      属性名称
     * @param Encoding 编码格式 ，如 GBK, UTF-8 等
     */
    public String[] getStringArrayByEncoding(String key, String Encoding) {

        String[] str = config.getStringArray(key);
        String[] strFinal = new String[str.length];

        try {

            for (int i = 0; i < str.length; i++) {
                strFinal[i] = new String(str[i].getBytes("ISO-8859-1"), Encoding);
            }
            return strFinal;

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 获得所有 key
     * <p/>
     * 对 Iterator 进行了二次包装
     *
     * @return
     */
    public List<String> getKeys() {
        Iterator iters = config.getKeys();
        List<String> list = new ArrayList<String>();
        while (iters.hasNext()) {
            list.add((String) iters.next());
        }
        return list;
    }

    /**
     * 获取树状结构数据
     */
    public void getHierarchicalExample() {

        try {
            XMLConfiguration config = new XMLConfiguration("properties-website-job.xml");
            logger.info(config.getFileName());
            //   logger.info(config.getStringByEncoding("jdbc.oracle.work.235.url", "GBK"));

            //包含 websites.site.name 的集合
            Object prop = config.getProperty("websites.site.name");

            if (prop instanceof Collection) {
                //  System.out.println("Number of tables: " + ((Collection<?>) prop).size());
                Collection<String> c = (Collection<String>) prop;
                int i = 0;

                for (String s : c) {

                    System.out.println("sitename :" + s);

                    List<HierarchicalConfiguration> fields = config.configurationsAt("websites.site(" + String.valueOf(i) + ").fields.field");
                    for (HierarchicalConfiguration sub : fields) {
                        // sub contains all data about a single field
                        //此处可以包装成 bean
                        String name = sub.getString("name");
                        String type = sub.getString("type");
                        System.out.println("name :" + name + " , type :" + type);
                    }

                    i++;
                    System.out.println(" === ");
                }
            }
        } catch (ConfigurationException e) {
            e.printStackTrace();
        }


    }
}