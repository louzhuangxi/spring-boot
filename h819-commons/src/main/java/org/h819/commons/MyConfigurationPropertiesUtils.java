package org.h819.commons;

/**
 * 参考 org.h819.net.ftp.FTPClientUtils 实现
 *
 */

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


public class MyConfigurationPropertiesUtils {

    private static Logger logger = LoggerFactory.getLogger(MyConfigurationPropertiesUtils.class);
    //如果需要对 propertiesConfiguration 进行设置，或直接调用 propertiesConfiguration 的方法，通过 get 方法获得 propertiesConfiguration 后，再进行设置
    private PropertiesConfiguration propertiesConfiguration;

    /**
     * 禁止无参数实例化
     */
    private MyConfigurationPropertiesUtils() {

    }

    public MyConfigurationPropertiesUtils(String propertyFileName) {

        try {
            propertiesConfiguration = new PropertiesConfiguration(propertyFileName);
        } catch (ConfigurationException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {


        /**
         * 测试代码
         */


        MyConfigurationPropertiesUtils s = new MyConfigurationPropertiesUtils("work.properties");
        logger.info(s.getPropertiesConfiguration().getString(("jdbc.oracle.work.235.url")));
        logger.info(s.getStringByEncoding("jdbc.oracle.work.235.url", "GBK"));


    }

    /**
     * 返回 PropertiesConfiguration ，可以调用 PropertiesConfiguration 的方法
     * @return
     */
    public PropertiesConfiguration getPropertiesConfiguration() {
        return propertiesConfiguration;
    }

    /**
     * 得到指定名称的属性的字符串值。属性值中不能有标点逗号 "," "="      如果确实需要 ，则用转义字符分开即 "\,"
     * <p/>
     * getString 方法，返回的是 ISO-8859-1 编码字符串，如果是中文的话，会乱码。应进行转码
     *
     * @param key      属性名称
     * @param Encoding 编码格式 ，如 GBK, UTF-8 等
     * @return
     * @throws UnsupportedEncodingException
     */
    public String getStringByEncoding(String key, String Encoding) {

        try {

            return new String(propertiesConfiguration.getString(key).getBytes("ISO-8859-1"), Encoding);

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

        String[] str = propertiesConfiguration.getStringArray(key);
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
        Iterator iters = propertiesConfiguration.getKeys();
        List<String> list = new ArrayList<String>();
        while (iters.hasNext()) {
            list.add((String) iters.next());
        }
        return list;
    }

}
