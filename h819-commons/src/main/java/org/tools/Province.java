package org.tools;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Description : TODO()
 * User: h819
 * Date: 2014/12/3
 * Time: 10:01
 * To change this template use File | Settings | File Templates.
 */

//获取 行政区划代码
//http://www.stats.gov.cn/tjsj/tjbz/xzqhdm/

public class Province {


    /**
     * @param args
     */
    public static void main(String[] args) {

        Province province = new Province();
        province.parse();

    }

    /**
     * 整理省市县信息
     */
    private void parse() {

        try {

            List<String> list = FileUtils.readLines(new File("D:\\ftpFiles\\Province.txt"));

            //存放整理后的结果
            List<String> temp = new ArrayList<String>(list.size());

            for (String s : list) {
                s = s.replaceAll(" ", "").replaceAll(" ", "").trim();

                String code = StringUtils.substring(s, 0, 6).trim();
                String name = StringUtils.substringAfter(s, code);
                // System.out.println("code :"+code+" , name:"+name);
                temp.add(code + "," + name);

            }

            for (String s : temp) {

                String code = StringUtils.substringBefore(s, ",");
                String name = StringUtils.substringAfter(s, ",");

                //省、直辖市
                if (code.endsWith("0000")) {

                    System.out.println(code + "," + name + " ========================= ");

                    //循环，找到 -> 市
                    for (String city : temp) {

                        String c_code = StringUtils.substringBefore(city, ",");
                        String c_name = StringUtils.substringAfter(city, ",");

                        if (c_code.startsWith(StringUtils.substring(code, 0, 2)) && c_code.endsWith("00") && !c_code.equals(code)) {

                            System.out.println(c_code + "," + c_name + "=========");

                            //循环，找到 -> 县
                            for (String xian : temp) {
                                String x_code = StringUtils.substringBefore(xian, ",");
                                String x_name = StringUtils.substringAfter(xian, ",");

                                if (x_code.startsWith(StringUtils.substring(c_code, 0, 4)) && !x_code.endsWith("00")) {

                                    System.out.println(x_code + "," + x_name);
                                }
                            }


                        }
                    }

                    System.out.println(StringUtils.center("splite", 80, "*"));

                }


            }


        } catch (IOException e) {
            e.printStackTrace();
        }


    }

}
