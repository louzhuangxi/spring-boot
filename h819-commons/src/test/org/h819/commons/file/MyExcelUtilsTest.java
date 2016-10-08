package org.h819.commons.file;

import org.h819.commons.MyArrayUtils;
import org.h819.commons.MyJsonUtils;
import org.h819.commons.file.excel.poi.vo.ExcelCell;
import org.h819.commons.file.excel.poi.vo.ExcelLine;
import org.junit.Test;

/**
 * Description : TODO()
 * User: h819
 * Date: 2016/9/29
 * Time: 16:14
 * To change this template use File | Settings | File Templates.
 */
public class MyExcelUtilsTest {
    @Test
    public void writeExcelToJsonFile() throws Exception {

    }

    @Test
    public void writeExcelToJsonFile1() throws Exception {

    }

    @Test
    public void readFromJsonFile() throws Exception {

    }

    @Test
    public void readExcel() throws Exception {

    }

    @Test
    public void readExcel1() throws Exception {

    }

    @Test
    public void readExcel2() throws Exception {

    }

    @Test
    public void writeExcel() throws Exception {

    }

    @Test
    public void writeExcel1() throws Exception {

    }

    @Test
    public void getCellValueByColumnIndex() throws Exception {

    }

    @Test
    public void getCellValueByColumnAlphaTitleName() throws Exception {

    }

    @Test
    public void getCellValueByColumnAlphaTitleNameAndLineNumber() throws Exception {

    }

    @Test
    public void copyCellValueByColumnAlphaTitleName() throws Exception {

    }

    @Test
    public void replaceCellValueByColumnTitleIndex() throws Exception {

    }

    @Test
    public void replaceCellValueByColumnAlphaTitleName() throws Exception {

        ExcelLine line = new ExcelLine();

        ExcelCell cell = new ExcelCell();
        cell.setTitle("a");
        cell.setValue("a_value");
        ExcelCell cell2 = new ExcelCell();
        cell2.setTitle("b");
        cell2.setValue("b_value");


        line.setCellValues(MyArrayUtils.asArrayList(cell, cell2));
        // Arrays.asList 生成的 list 大小不可变 ，重新生成
        //   MyJsonUtils.prettyPrint(line);

        System.out.println("=============");

        // System.out.println("\n b0="+MyExcelUtils.getCellValueByColumnAlphaTitleName(line,"b"));

        MyExcelUtils.replaceCellValueByColumnAlphaTitleName(line, "b", "replace_value");
        MyJsonUtils.prettyPrint(line);

        //  MyJsonUtils.prettyPrint(cell);

        //  System.out.println("\n b1="+MyExcelUtils.getCellValueByColumnAlphaTitleName(line,"b"));

    }

    @Test
    public void convertColumnIndexToTitle() throws Exception {

    }

    @Test
    public void convertColumnTitleToIndex() throws Exception {

    }

    @Test
    public void excelExtractor() throws Exception {

    }

}