package org.test;

import org.apache.poi.ss.usermodel.Sheet;
import org.h819.commons.MyStringUtils;
import org.h819.commons.file.MyExcelUtils;
import org.h819.commons.file.excel.poi.vo.ExcelLine;
import org.springframework.util.Assert;

import java.io.File;
import java.util.List;

/**
 * Description : TODO()
 * User: h819
 * Date: 2016/4/1
 * Time: 15:20
 * To change this template use File | Settings | File Templates.
 */
public class Test2 {

    public static void main(String[] args) {

        Test2 t = new Test2();
        // t.decryptDirectory();
        //t.testExcelSheet();
        //t.testExcelRead();
       // t.testHtmlText();
        t.testAssert();
    }

    private void decryptDirectory() {
        File src = new File("D:\\2016-8-3\\");
        File desc = new File("D:\\2016-8-4_desc\\");
        File bad = new File("D:\\2016-8-4_desc_bad\\");

//        try {
//            MyPDFUtils.decryptDirectory(src, desc, bad);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }

    }

    private void testFileName() {
        File f = new File("d:\\01\\01.txt");
        System.out.println(f.getName());

    }

    private void testExcelSheet() {
        File f = new File("D:\\r.xlsx");
      //  MyJsonUtils.prettyPrint(MyExcelUtils.readExcel(f, true));

        List<Sheet> sheets = MyExcelUtils.getSheets(f);
        for(Sheet sheet : sheets)
        System.out.println(sheet.getSheetName());
    }


    private void testExcelRead() {
        File f = new File("D:\\r.xlsx");
        //  MyJsonUtils.prettyPrint(MyExcelUtils.readExcel(f, true));

        List<ExcelLine> lines = MyExcelUtils.readExcel(f,false);

            System.out.println(lines.size());
    }

    private void testHtmlText(){

        String s ="<td><span class=\"sortSpan\" style=\"display:none\">26</span><a href=\"home/store/catalogue_tc/catalogue_detail.htm?csnumber=70124\" class=\"develop\">ISO/NP 11299-3</a><br>Plastics piping systems for renovation of underground gas supply networks -- Part 3: Lining with close-fit pipes</td>";

       System.out.println(MyStringUtils.htmlToPlainText(s));

    }

    private void testAssert(){

      //  Assert.isTrue(1!=1,"is true");

        Assert.isTrue("coun . a".contains("count"),"queryNativeSql 和 countNativeSql 参数顺序不对");


    }

}
