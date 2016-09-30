package org.test;

import org.h819.commons.MyJsonUtils;
import org.h819.commons.file.MyExcelUtils;
import org.h819.commons.file.excel.poi.vo.ExcelCell;
import org.h819.commons.file.excel.poi.vo.ExcelLine;

import java.util.HashSet;
import java.util.Set;

/**
 * Description : TODO()
 * User: h819
 * Date: 2016-09-30
 * Time: 14:49
 * To change this template use File | Settings | File Templates.
 */
public class TestExcelUtils {

    public static void main(String[] args) {

        ExcelCell cell = new ExcelCell("a", "a_value");
        ExcelCell cell1 = new ExcelCell("b", "b_value");
        ExcelCell cell2 = new ExcelCell("c", "c_value");
        ExcelCell cell3 = new ExcelCell("d", "d_value");
        Set set = new HashSet<>(2);
        set.add(cell);
        set.add(cell1);
        set.add(cell2);
        set.add(cell3);



        ExcelLine line = new ExcelLine();
        line.setCellValues(set);
        line.addCellValue(cell);
        line.addCellValue(cell1);

        MyJsonUtils.prettyPrint(line);

        System.out.println("==================");

        MyExcelUtils.replaceCellValueByColumnAlphaTitleName(line,"f","replace_value");

        MyJsonUtils.prettyPrint(line);



    }

}
