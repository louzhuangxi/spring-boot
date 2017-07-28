package com.base.Test;

import org.apache.commons.lang3.time.DateUtils;
import org.h819.commons.MyFastJsonUtils;
import org.h819.commons.file.MyExcelUtils;
import org.h819.commons.file.excel.poi.vo.ExcelLine;

import java.io.File;
import java.text.ParseException;
import java.util.*;

/**
 * Description : TODO()
 * User: h819
 * Date: 2016/8/23
 * Time: 13:56
 * To change this template use File | Settings | File Templates.
 */
public class Test {

    private static List list = new ArrayList();

    public static void main(String[] args) throws Exception {

        Test test = new Test();

        String[] ss = {"on", "1", "2"};
        String[] ss1 = {"1", "2"};
//        System.out.print(Arrays.asList(ArrayUtils.removeElement(ss1, "on")));
//        System.out.print("中文");

        test.listDir(new File("F:\\tianyiyun\\13811998709\\工作资料"));

        Collections.sort(list, new Comparator<MyFile>() {
            @Override
            public int compare(MyFile o, MyFile t1) {
                return o.getSize().compareTo(t1.getSize());
            }
        });

        MyFastJsonUtils.prettyPrint(list);
        // MyExcelUtils.writeExcel(test.findStandardEntityByCompany(new File("D:\\ftpFiles\\1.xlsx")), "sheet1", new File("D:\\ftpFiles\\5_out.xlsx"));

        //   System.out.print("中文");
        // Assert.state("assc".equalsIgnoreCase("asc")|| "DESCs".equalsIgnoreCase("desc") , " 排序 direction 只能为 asc or desc");
    }


    // Map 结构
    // key : 公司名称;所属园区
    // value : 该单位参加编写的所有标准

    /**
     * 演示如果在
     *
     * @param fileInput
     * @return
     */
    public List<ExcelLine> findStandardEntityByCompany(File fileInput) {

        List<ExcelLine> list = MyExcelUtils.readExcel(fileInput, new Integer(1), true);
        // 逐一循环企业 ，保存查询结果到 result
        List<ExcelLine> result = new ArrayList<>();

        int i = 0;
        for (ExcelLine s : list) {

            // MyJsonUtils.prettyPrint(s);

            int j = 0;

            String issueDate = MyExcelUtils.getCellValueByColumnAlphaTitleName(s, "h");

            //   MyJsonUtils.prettyPrint(issuDate);

            if (issueDate == null || issueDate.equals("") || issueDate.equals("发布日期"))
                continue;

            try {
                Date isDate = DateUtils.parseDate(issueDate, "yyyy-MM-dd");
                Date thisyear = DateUtils.parseDate("2016-01-01", "yyyy-MM-dd");

                if (isDate.before(thisyear))
                    continue;

            } catch (ParseException e) {

                e.printStackTrace();
                break;
            }

            String companyname = MyExcelUtils.getCellValueByColumnAlphaTitleName(s, "a");  //企业名称

            if (companyname != null && !companyname.equals("")) {
                result.add(s);
                // MyJsonUtils.prettyPrint(s);
                continue;
            }


            i = list.indexOf(s);
            j = i - 1;
            while (j != 0) {
                ExcelLine lineT = list.get(j);
                String company = MyExcelUtils.getCellValueByColumnAlphaTitleName(lineT, "a");
                if (company != null && !company.equals("")) {

                    MyExcelUtils.copyCellValueByColumnAlphaTitleName(lineT, "a", s, "a");
                    MyExcelUtils.copyCellValueByColumnAlphaTitleName(lineT, "b", s, "b");
                    MyExcelUtils.copyCellValueByColumnAlphaTitleName(lineT, "c", s, "c");
                    result.add(s);
                    break;
                }
                j--;
            }

        }
        return result;
    }

    private void listDir(File dir) {

        for (File file : dir.listFiles()) {

            if (file.isFile()) {
                list.add(new MyFile(file.getAbsolutePath(), file.length() / (1024*1024)));

            } else listDir(file);

        }


    }

    class MyFile {

        String name;
        Long size;

        MyFile(String name, Long size) {
            this.name = name;
            this.size = size;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public Long getSize() {
            return size;
        }

        public void setSize(Long size) {
            this.size = size;
        }
    }

}
