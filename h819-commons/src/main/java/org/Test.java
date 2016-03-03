package org;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOCase;
import org.apache.commons.io.filefilter.SuffixFileFilter;
import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.examples.spring.domain.TreeEntity;
import org.h819.commons.MyStringUtils;
import org.h819.commons.file.MyExcelUtils;
import org.h819.commons.file.excel.poi.bean.ExcelLine;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.springframework.beans.BeanUtils;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.util.*;

/**
 * Description : TODO()
 * User: h819
 * Date: 14-3-14
 * Time: 上午10:33
 * To change this template use File | Settings | File Templates.
 */

public class Test {


    // File from = new File("E:\\123");

    //由于是递归，所以防止图片的根目录放在类变量
    File to = new File("e:\\456");


    public static void main(String[] args) {

        Test t = new Test();




        String s = "abc, sf ,ss ss,";
        //String[] ss = s.split(",");
        String a =",";
        String b ="ss";
        String s1 = s.replaceAll(",.+,","");

//        System.out.println(s1);
//        System.out.println(MyStringUtils.removePattern(s,",.+,"));

       //t.testFilfilter();

      //  System.out.println(Comparable.class.isAssignableFrom(s.getClass()));

      //  t.test2();

        t.testGetBeanInfo();

    }



    private void testGetBeanInfo(){


        BeanInfo beanInfo = null;
        try {
            beanInfo = Introspector.getBeanInfo(TreeEntity.class);
        } catch (IntrospectionException e) {
            e.printStackTrace();
        }

        PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors(); //得到属性描述符


        PropertyDescriptor[] descriptors = BeanUtils.getPropertyDescriptors(TreeEntity.class);

        for (PropertyDescriptor propertyDescriptor : propertyDescriptors) {
            System.out.println(propertyDescriptor.getName());
        }

        System.out.println(StringUtils.center("  ",80,"*"));

        for (PropertyDescriptor propertyDescriptor : descriptors) {
            System.out.println(propertyDescriptor.getName());
        }

    }


    private void test2(){

        String[] strings = {"1","2","3","4"};

       System.out.println( Arrays.asList(strings).subList(0,strings.length));
        System.out.println( Arrays.asList(strings).subList(1,strings.length));

    }
    /**
     * 深圳重复标准号查看
     */

    private void compareCollections() {

        Collection<ExcelLine> set = MyExcelUtils.readExcel(new File("d:\\00\\2014.06.XLS"), true);

        ArrayList<String> compareSet = new ArrayList<String>(set.size());

        for (ExcelLine line1 : set) {

            String stdcode1 = MyExcelUtils.getCellValueByColumnIndex(line1, 0);

            // System.out.println(s);

            if (compareSet.contains(stdcode1)) {

                for (ExcelLine line2 : set) {

                    String stdcode2 = MyExcelUtils.getCellValueByColumnIndex(line2, 0);

                    //遍历：可以比较到 stdcode1 和 stdcode2
                    if (stdcode2.equals(stdcode1)) {

                        System.out.println(
                                MyExcelUtils.getCellValueByColumnIndex(line2, 0) + "@" +
                                        MyExcelUtils.getCellValueByColumnIndex(line2, 4) + "@" +
                                        MyExcelUtils.getCellValueByColumnIndex(line2, 5) + "@" +
                                        MyExcelUtils.getCellValueByColumnIndex(line2, 16) + "@" +
                                        MyExcelUtils.getCellValueByColumnIndex(line2, 20)
                        );


                    }

                }


//                System.out.println(
//                        ExcelUtils.getCellValueByColumnIndex(line1, 0) + "@" +
//                                ExcelUtils.getCellValueByColumnIndex(line1, 4) + "@" +
//                                ExcelUtils.getCellValueByColumnIndex(line1, 5) + "@" +
//                                ExcelUtils.getCellValueByColumnIndex(line1, 16) + "@" +
//                                ExcelUtils.getCellValueByColumnIndex(line1, 20));

            } else compareSet.add(stdcode1);

        }

    }


    private void testExcel() {

        Collection<ExcelLine> lineString = MyExcelUtils.readExcel(new File("D:\\ftpFiles\\00.XLSx"), true);

        for (ExcelLine s : lineString) {

            System.out.println(s);

            String productName = MyExcelUtils.getCellValueByCloumnAlphaName(s, "A");

            String stdCodes = MyExcelUtils.getCellValueByCloumnAlphaName(s, "b");

            System.out.println("productName:" + productName);

            System.out.println("stdCodes:" + stdCodes);

        }
    }

    private void findSame() {

        try {

            Set<String> set = new LinkedHashSet();

            List<String> list = FileUtils.readLines(new File("d:\\00\\txtFile.txt"));

            for (String s : list) {

                String ss = MyStringUtils.substringAfter(s, ":rowValue:");

                System.out.println(ss);

                set.add(ss);
            }
            System.out.print("size :" + set.size());


        } catch (IOException e) {
            e.printStackTrace();
        }


    }



    private void testDate() {

        try {

            Date a = DateUtils.parseDate("2014-02-31", "yyyy-MM-dd");

            Date b = DateUtils.parseDate("2014-01-32", "yyyy-MM-dd");

            System.out.println(a.compareTo(b));
            System.out.println(b.compareTo(a));


        } catch (ParseException e) {
            e.printStackTrace();
        }

    }

    private void testStringUtils() {
        //  MyStringUtils.con
        System.out.println(MyStringUtils.containsAnyCharSequence("安达市政府", new String[]{"政府"}));
        System.out.println(MyStringUtils.containsAnyCharSequence("安达市政府"));
    }

    private void testMimeType() {

        //   System.out.println(MyMimeTypeUtils.isTextMimeType("http://hrss.jl.gov.cn/zxgg/201404/t20140425_1652612.html"));
    }

    private void testReadFile() {

        try {

            List<String> list = FileUtils.readLines(new File("D:\\ftpFiles\\ftpfile.txt"));

            Set<String> set = new TreeSet<String>();

            for (String s : list)
                set.add(s);

            //   String[] strings =set.toArray();

            String[] arrayString = set.toArray(new String[set.size()]);

            for (String s : arrayString)
                System.out.println(s);

            System.out.println("  ====  ");

            String temp;
            for (int i = 0; i < arrayString.length - 1; i++) {
                for (int j = i + 1; j < arrayString.length; j++) {
                    if (MyStringUtils.substringAfter(arrayString[i], "-").compareTo(MyStringUtils.substringAfter(arrayString[j], "-")) > 0) {
                        temp = arrayString[i];
                        arrayString[i] = arrayString[j];
                        arrayString[j] = temp;
                    }
                }
            }


            for (String s : arrayString)
                System.out.println(s);


        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void SqlEscapeExample() {

        String userName = "1' or '1'='1";
        String password = "123456";
        String sql = "SELECT COUNT(userId) FROM t_user WHERE userName='"
                + userName + "' AND password ='" + password + "'";
        System.out.println(sql);
        userName = StringEscapeUtils.escapeSql(userName);
        password = StringEscapeUtils.escapeSql(password);
        String sql2 = "SELECT COUNT(userId) FROM t_user WHERE userName='"
                + userName + "' AND password ='" + password + "'";
        System.out.println(sql2);

    }

    private void renameSongs() {


        Collection<File> files = FileUtils.listFiles(new File("I:\\story"), new String[]{"VOB"}, true);

        List<String> names = null;

        try {
            names = FileUtils.readLines(new File("D:\\story.txt"), "GBK");

        } catch (IOException e) {
            e.printStackTrace();
        }

        for (File f : files) {
            String name = f.getAbsolutePath();
            System.out.println(name);
            String sub1 = StringUtils.substringBetween(name, "story\\", "\\V");
            // System.out.println(sub1);
            String sub2 = StringUtils.substringBetween(name, "R_", "_1");
            //  System.out.println(sub2);
            String sub = sub1 + "-" + sub2;

            // System.out.println(sub);

            for (String s : names) {
                if (s.startsWith(sub)) {

                    System.out.println(sub);
                    System.out.println(s);

                    try {
                        FileUtils.moveFile(new File(name), new File("I:\\story\\" + s + ".VOB"));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }
            }

        }

    }

    private void copySongs() {

        List<String> names = null;

        try {

            names = FileUtils.readLines(new File("D:\\2.qpl"));

            String src = "";

            for (String s : names) {
                if (s.contains(".VOB")) {
                    src = StringUtils.substringBetween(s, "file=\"", ".VOB") + ".VOB";

                    System.out.println(src);

                    FileUtils.copyFileToDirectory(new File(src), new File("d:\\c"));
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }


    }


    /**
     * 拷贝相片到根目录下，用于整理相片
     *
     * @param fromDerictory
     */
    private void moveFiles(File fromDerictory) {


        File[] allfile = fromDerictory.listFiles();


        for (File ff : allfile) {

            if (ff.isFile())
                try {

                    File toFile = new File(to.getAbsoluteFile() + "\\" + ff.getName());

                    System.out.println(toFile.getAbsoluteFile());

                    if (toFile.exists())
                        FileUtils.moveFile(ff, new File(to.getAbsoluteFile() + "\\_bak_" + ff.getName()));
                    else
                        FileUtils.moveFile(ff, toFile);

                } catch (IOException e) {
                    e.printStackTrace();
                }

            if (ff.isDirectory())
                moveFiles(ff);

        }
    }


    private void findNewFile() {

        //创建文件
        File fdir = new File("D:\\00\\");

        File[] listFiles = fdir.listFiles();

        long creatTime = 0;
        String fileName = "";

        for (File f : listFiles) {

            if (f.getName().equals("2014.06.XLS"))
                continue;

            long temp = f.lastModified();

            if (temp > creatTime) {
                creatTime = temp;
                fileName = f.getAbsolutePath();
            }
        }

        System.out.println(fileName + " " + creatTime);
    }


    private void testHtml() {


        String url = "http://video.sina.com.cn/p/ent/z/y/2014-10-07/225764130461.html";

        Document doc = null;
        try {
            doc = Jsoup.connect(url).get();

            System.out.print(doc.toString());

            Elements newsHeadlines = doc.select("#mp-itn b a");


        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    private void testFilfilter() {


        String[] files = new File("d:\\00").list(new SuffixFileFilter(".xls", IOCase.SYSTEM));

        for (String s : files)
            System.out.println(s);

    }


}


