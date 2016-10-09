package com.base.Test;

import org.apache.commons.lang3.time.DateUtils;
import org.h819.commons.file.MyExcelUtils;
import org.h819.commons.file.excel.poi.vo.ExcelLine;
import org.springframework.util.Assert;

import java.io.File;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Description : TODO()
 * User: h819
 * Date: 2016/8/23
 * Time: 13:56
 * To change this template use File | Settings | File Templates.
 */
public class Test {


    public static void main(String[] args) throws Exception {

        Test test = new Test();

       // MyExcelUtils.writeExcel(test.findStandardEntityByCompany(new File("D:\\ftpFiles\\1.xlsx")), "sheet1", new File("D:\\ftpFiles\\5_out.xlsx"));

        System.out.print("中文");
        Assert.state("assc".equalsIgnoreCase("asc")|| "DESCs".equalsIgnoreCase("desc") , " 排序 direction 只能为 asc or desc");
    }


    // Map 结构
    // key : 公司名称;所属园区
    // value : 该单位参加编写的所有标准

    /**
     * 演示如果在
     * @param fileInput
     * @return
     */
    public List<ExcelLine> findStandardEntityByCompany(File fileInput) {

        List<ExcelLine> list = MyExcelUtils.readExcel(fileInput,new Integer(1), true);
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
}
