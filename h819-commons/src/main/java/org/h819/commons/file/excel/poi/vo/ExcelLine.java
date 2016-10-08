package org.h819.commons.file.excel.poi.vo;

import java.util.Set;
import java.util.TreeSet;

/**
 * Description : TODO(excel 行数据，单元格无内容时，读取程序会跳过，所以单元格可能不连续)
 * User: h819
 * Date: 2014-10-20
 * Time: 12:29
 * To change this template use File | Settings | File Templates.
 */
public class ExcelLine {

    private String fileName = "";
    private String sheetName = "";
    private int sheetNumber = 0;
    private int lineNumber = 0;
    private Set<ExcelCell> cellValues = new TreeSet<>() ;

    public ExcelLine() {

    }

    /**
     * 构造 Excel 行数据，包含行的所有常用信息
     *
     * @param fileName    行所在文件名称
     * @param sheetName   行所在 sheet 名称
     * @param sheetNumber 行所在 sheet 序号
     * @param lineNumber  所在 sheet 的行号
     * @param cellValues  行单元格,TreeSet 重新包装去重复，变为有序(按照 title 排序, ExcelCell 实现了 Comparable)
     */
    public ExcelLine(String fileName, String sheetName, int sheetNumber, int lineNumber, Set<ExcelCell> cellValues) {

        this.fileName = fileName;
        this.sheetName = sheetName;
        this.sheetNumber = sheetNumber;
        this.lineNumber = lineNumber;
        this.cellValues = cellValues;

    }

    public String getSheetName() {
        return sheetName;
    }

    public void setSheetName(String sheetName) {
        this.sheetName = sheetName;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public int getSheetNumber() {
        return sheetNumber;
    }

    public void setSheetNumber(int sheetNumber) {
        this.sheetNumber = sheetNumber;
    }

    public int getLineNumber() {
        return lineNumber;
    }

    public void setLineNumber(int lineNumber) {
        this.lineNumber = lineNumber;
    }

    /**
     * TreeSet 重新包装，变为有序且不重复，按照 title 排序
     */
    public Set<ExcelCell> getCellValues() {
        return new TreeSet<>(cellValues);
    }

    public void setCellValues(Set<ExcelCell> cellValues) {
        this.cellValues = cellValues;
    }

    /**
     * 增加一个单元格
     *
     * @param cellValue
     */
    public void addCellValue(ExcelCell cellValue) {
        this.cellValues.add(cellValue);
    }

}
