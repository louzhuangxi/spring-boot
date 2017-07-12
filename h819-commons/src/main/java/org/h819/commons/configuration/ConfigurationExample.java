package org.h819.commons.configuration;

import org.apache.commons.configuration.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public class ConfigurationExample {

    private static final Logger logger = LoggerFactory.getLogger(ConfigurationExample.class);

    public static void readProperties() {
        //注意路径默认指向的是classpath的根目录
        Configuration config = null;
        try {
            config = new PropertiesConfiguration("config.properties");
            String ip = config.getString("ip");
            System.out.println(ip);
            int port = config.getInt("port");
            System.out.println(port);
            String title = config.getString("application.title");
            System.out.println(title);
            //再举个Configuration的比较实用的方法吧,在读取配置文件的时候有可能这个键值对应的值为空，那么在下面这个方法中
            //你就可以为它设置默认值。比如下面这个例子就会在test.properties这个文件中找id的值，如果找不到就会给id设置值为123
            //这样就保证了java的包装类不会返回空值。虽然功能很简单，但是很方便很实用。
            Integer id = config.getInteger("id", new Integer(123));
            System.out.println(id);
//			config.setProperty("enname", "Hello");      
//			((AbstractFileConfiguration) config).save();
            ((AbstractFileConfiguration) config).isAutoSave();
            config.setProperty("enname", "Hello");
            String emmname = config.getString("enname");
            System.out.println(emmname);
            //如果在properties 文件中有如下属性keys=cn,com,org,uk,edu,jp,hk
            //可以实用下面的方式读取：
            String[] keys1 = config.getStringArray("keys");
            for (int i = 0; i < keys1.length; i++) {
                System.out.println(keys1[i]);
            }
            System.out.println();
            System.out.println();
            List keys2 = config.getList("keys");
            for (Iterator iterator = keys2.iterator(); iterator.hasNext(); ) {
                String strKeys = (String) iterator.next();
                System.out.println(strKeys);
            }

            String myname = config.getString("myname");
            System.out.println(myname);
        } catch (ConfigurationException e) {
            logger.error(e.getMessage());
        }
    }

    public static void readPropertieshh() {
        try {
            AbstractConfiguration.setDefaultListDelimiter('/');  //设置指定分隔符
            Configuration config = new PropertiesConfiguration("configiiff.properties");
            String[] keys1 = config.getStringArray("keysh");
            for (int i = 0; i < keys1.length; i++) {
                System.out.println(keys1[i]);
            }
            System.out.println();
            System.out.println();
            List keys2 = config.getList("keysh");
            for (Iterator iterator = keys2.iterator(); iterator.hasNext(); ) {
                String strKeys = (String) iterator.next();
                System.out.println(strKeys);
            }
        } catch (ConfigurationException e) {
            logger.error(e.getMessage());
        }
    }

    public static void readXml() {
        try {
            XMLConfiguration config = new XMLConfiguration("xmltest.xml");
            /**
             *<colors>
             *  <background>#808080</background>
             *  <text>#000000</text>
             *  <header>#008000</header>
             *  <link normal="#000080" visited="#800080"/>
             *  <default>${colors.header}</default>
             *</colors>
             *这是从上面的xml中摘抄的一段，我们现在来解析它，
             *colors是根标签下的直接子标签，所以是顶级名字空间
             */
            String backColor = config.getString("colors.background");

            System.out.println(backColor);
            String textColor = config.getString("colors.text");
            System.out.println(textColor);
            //现在我们知道了如何读取标签下的数据，那么如何读标签中的属性呢？看下面
            //<link normal="#000080" visited="#800080"/>
            String linkNormal = config.getString("colors.link[@normal]");
            System.out.println(linkNormal);
            //还支持引用呢！
            //<default>${colors.header}</default>
            String defColor = config.getString("colors.default");
            System.out.println(defColor);
            //也支持其他类型，但是一定要确定类型正确，否则要报异常哦
            //<rowsPerPage>15</rowsPerPage>
            int rowsPerPage = config.getInt("rowsPerPage");
            System.out.println(rowsPerPage);

            //加属性
            config.addProperty("shihuan", "shihuan");
            config.addProperty("updatehala", "updatehala");
            System.out.println(config.getString("shihuan"));
            System.out.println(config.getString("updatehala"));

            //获得同名结点的集合
            List buttons = config.getList("buttons.name");
            for (Object button : buttons) {
                System.out.println(button.toString());
            }

            System.out.println();
            System.out.println();

            //取消分隔符
            XMLConfiguration configList = new XMLConfiguration();
            configList.setDelimiterParsingDisabled(true);
            configList.setFileName("xmltest.xml");
            configList.load();
            List buttonsList = configList.getList("buttons.name");
            for (Object buttonList : buttonsList) {
                System.out.println(buttonList.toString());
            }

            //更复杂的xml文件
            XMLConfiguration configXml = new XMLConfiguration();
            configXml.setDelimiterParsingDisabled(true);
            configXml.setFileName("database.xml");
            configXml.load();
            Object prop = configXml.getProperty("tables.table.name");
            if (prop instanceof Collection) {
                System.out.println("Number of tables: " + ((Collection) prop).size());
            }

            //return users
            System.out.println(configXml.getProperty("tables.table(0).name"));
            //return system
            System.out.println(configXml.getProperty("tables.table(0)[@tableType]"));
            //return documents
            System.out.println(configXml.getProperty("tables.table(1).name"));
            //return null,因为只有两个table所以这个值为null
            System.out.println(configXml.getProperty("tables.table(2).name"));
            //return [docid, name, creationDate, authorID, version]
            //如果所要找的节点不存在唯一值，则返回Collection类型
            System.out.println(configXml.getProperty("tables.table(1).fields.field.name"));
            //[long, long]
            //与上面的相同，返回值不唯一
            System.out.println(configXml.getProperty("tables.table.fields.field(0).type"));
            //return creationDate
            System.out.println(configXml.getProperty("tables.table(1).fields.field(2).name"));
        } catch (ConfigurationException e) {
            logger.error(e.getMessage());
        }

    }

    public static void readIni() {
        try {
            HierarchicalINIConfiguration config = new HierarchicalINIConfiguration("config.ini");
            String basestr = config.getBasePath();
            System.out.println(basestr);
            String filestr = config.getFileName();
            System.out.println(filestr);
            String pathstr = config.getBasePath();
            System.out.println(pathstr);
            Set zykhstr = config.getSections();
            for (Object setVal : zykhstr) {
                System.out.println(setVal.toString());
            }
            System.out.println();
            System.out.println();
            for (Iterator iter = config.getKeys(); iter.hasNext(); ) {
                System.out.println(iter.next().toString());
                System.out.println(config.getString(iter.next().toString()));
            }

        } catch (ConfigurationException e) {
            logger.error(e.getMessage());
        }
    }

    /**
     * @param args
     */
    public static void main(String[] args) {
//		TestConfiguration.readProperties();
//		TestConfiguration.readPropertieshh();
//		TestConfiguration.readXml();
        ConfigurationExample.readIni();
    }

}
