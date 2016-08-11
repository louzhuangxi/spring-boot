package org.h819.commons.file.excel.poi;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.CellReference;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFRichTextString;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.Calendar;
import java.util.Date;

/**
 * @author H819
 * @version V1.0
 * @Title: PIOUtils.java
 * @Description: TODO(添加描述)
 * @date 2010-4-29
 */
public class POIExcamples {

    private static Logger logger = LoggerFactory.getLogger(POIExcamples.class);

    private final String filepath = "F:/jpa/workspace_huxiao_new/_testpoi/src/test/file/workbook.xls";

    /**
     * Creates a cell and aligns it a certain way.
     *
     * @param wb     the workbook
     * @param row    the row to create the cell in
     * @param column the column number to create the cell in
     * @param halign the horizontal alignment for the cell.
     */
    private static void createCell(Workbook wb, Row row, short column,
                                   short halign, short valign) {
        Cell cell = row.createCell(column);
        cell.setCellValue(new XSSFRichTextString("Align It"));
        CellStyle cellStyle = wb.createCellStyle();
        cellStyle.setAlignment(halign);
        cellStyle.setVerticalAlignment(valign);
        cell.setCellStyle(cellStyle);
    }

    /**
     * @param args
     */
    public static void main(String[] args) {
        // TODO Auto-generated method stub

    }

    /**
     * 读例子
     *
     * @param excelFile
     * @throws Exception
     */
    public void readExcelExample(File excelFile) throws Exception {
        InputStream inp = new FileInputStream(excelFile);
        HSSFWorkbook wb = new HSSFWorkbook(new POIFSFileSystem(inp));
        Sheet sheet1 = wb.getSheetAt(0);
        for (Row row : sheet1) {
            for (Cell cell : row) {
                CellReference cellRef = new CellReference(row.getRowNum(), cell
                        .getColumnIndex());
                System.out.print(cellRef.formatAsString());
                System.out.print(" - ");
                switch (cell.getCellType()) {

                    case Cell.CELL_TYPE_STRING:
                        System.out.println(cell.getRichStringCellValue()
                                .getString());
                        break;

                    case Cell.CELL_TYPE_NUMERIC:
                        if (DateUtil.isCellDateFormatted(cell)) {
                            System.out.println(cell.getDateCellValue());
                        } else {
                            System.out.println(cell.getNumericCellValue());
                        }
                        break;

                    case Cell.CELL_TYPE_BOOLEAN:
                        System.out.println(cell.getBooleanCellValue());
                        break;

                    case Cell.CELL_TYPE_FORMULA:
                        System.out.println(cell.getCellFormula());
                        break;

                    default:
                        System.out.println();
                }
            }
        }
        inp.close();
    }

    // 合并单元格
    public void mergingCell() throws Exception {
        Workbook wb = new HSSFWorkbook();
        org.apache.poi.ss.usermodel.Sheet sheet = wb.createSheet("new sheet");
        Row row = sheet.createRow((short) 1);
        Cell cell = row.createCell((short) 1);
        cell.setCellValue("This is a test of merging");

        sheet.addMergedRegion(new CellRangeAddress(1, // first row (0-based)
                4, // last row (0-based)
                1, // first column (0-based)
                6 // last column (0-based)
        ));

        // Write the output to a file
        FileOutputStream fileOut = new FileOutputStream("workbook.xls");
        wb.write(fileOut);
        fileOut.close();

    }

    public void writeExcel() throws Exception {

        Workbook wb = new HSSFWorkbook();
        CreationHelper createHelper = wb.getCreationHelper();
        Sheet sheet = wb.createSheet("new sheet");

        // ================================================================
        // Create a row and put some cells in it. rows are 0 based
        // line 0
        Row row0 = sheet.createRow((short) 0);
        // 创建一个单元格并填充一个整数的值
        Cell cell = row0.createCell(0);
        cell.setCellValue(1);

        // 链式写法
        row0.createCell(1).setCellValue(1.2);
        row0.createCell(2).setCellValue(
                createHelper.createRichTextString("This is a string"));
        row0.createCell(3).setCellValue(true);

        // ================================================================
        // line 1
        Row row1 = sheet.createRow(1);
        // Create a cell and put a date value in it. The first cell is not
        // styled as a date.
        cell = row1.createCell(0);
        cell.setCellValue(new Date());

        // we style the second cell as a date (and time). It is important to
        // create a new cell style from the workbook otherwise you can end up
        // modifying the built in style and effecting not only this cell but
        // other cells.

        CellStyle cellStyle = wb.createCellStyle();
        cellStyle.setDataFormat(createHelper.createDataFormat().getFormat(
                "yyyy/MM/dd hh:mm"));
        cell = row1.createCell(1);
        cell.setCellValue(createHelper.createRichTextString(new Date()
                .toString()));
        cell.setCellStyle(cellStyle);

        // you can also set date as java.util.Calendar
        cell = row1.createCell(2);
        cell.setCellValue(Calendar.getInstance());
        cell.setCellStyle(cellStyle);

        // line 2 : 创建不同的单元格样式
        Row row2 = sheet.createRow((short) 2);
        row2.createCell(0).setCellValue(1.1);
        row2.createCell(1).setCellValue(new Date());
        row2.createCell(2).setCellValue(Calendar.getInstance());
        row2.createCell(3).setCellValue("a string");
        row2.createCell(4).setCellValue(true);
        row2.createCell(5).setCellType(HSSFCell.CELL_TYPE_ERROR);

        // line 3: 设置单元格水平垂直对齐方式
        Row row3 = sheet.createRow((short) 3);
        row3.setHeightInPoints(30);

        createCell(wb, row3, (short) 0, XSSFCellStyle.ALIGN_CENTER,
                XSSFCellStyle.VERTICAL_BOTTOM);
        createCell(wb, row3, (short) 1, XSSFCellStyle.ALIGN_CENTER_SELECTION,
                XSSFCellStyle.VERTICAL_BOTTOM);
        createCell(wb, row3, (short) 2, XSSFCellStyle.ALIGN_FILL,
                XSSFCellStyle.VERTICAL_CENTER);
        createCell(wb, row3, (short) 3, XSSFCellStyle.ALIGN_GENERAL,
                XSSFCellStyle.VERTICAL_CENTER);
        createCell(wb, row3, (short) 4, XSSFCellStyle.ALIGN_JUSTIFY,
                XSSFCellStyle.VERTICAL_JUSTIFY);
        createCell(wb, row3, (short) 5, XSSFCellStyle.ALIGN_LEFT,
                XSSFCellStyle.VERTICAL_TOP);
        createCell(wb, row3, (short) 6, XSSFCellStyle.ALIGN_RIGHT,
                XSSFCellStyle.VERTICAL_TOP);

        // line 4 设置单元格的边框
        Row row4 = sheet.createRow(4);
        // Create a cell and put a value in it.
        cell = row4.createCell(1);
        cell.setCellValue(4);
        // Style the cell with borders all around.
        CellStyle style = wb.createCellStyle();
        style.setBorderBottom(CellStyle.BORDER_THIN);
        style.setBottomBorderColor(IndexedColors.BLACK.getIndex());
        style.setBorderLeft(CellStyle.BORDER_THIN);
        style.setLeftBorderColor(IndexedColors.GREEN.getIndex());
        style.setBorderRight(CellStyle.BORDER_THIN);
        style.setRightBorderColor(IndexedColors.BLUE.getIndex());
        style.setBorderTop(CellStyle.BORDER_MEDIUM_DASHED);
        style.setTopBorderColor(IndexedColors.BLACK.getIndex());
        cell.setCellStyle(style);

        // ====== line 5 填充和颜色
        Row row5 = sheet.createRow(5);
        // Aqua background
        style.setFillBackgroundColor(IndexedColors.BLUE.getIndex());
        style.setFillPattern(CellStyle.ALIGN_FILL);
        cell = row5.createCell((short) 1);
        cell.setCellValue("X");
        cell.setCellStyle(style);

        // Orange "foreground", foreground being the fill foreground not the
        // font color.
        style = wb.createCellStyle();
        style.setFillForegroundColor(IndexedColors.ORANGE.getIndex());
        style.setFillPattern(CellStyle.SOLID_FOREGROUND);
        cell = row5.createCell((short) 2);
        cell.setCellValue("X");
        cell.setCellStyle(style);

        // 输出文件
        FileOutputStream fileOut = new FileOutputStream("d://workbook.xls");
        wb.write(fileOut);
        fileOut.close();

    }

    public void createFont() throws Exception {
        Workbook wb = new HSSFWorkbook();
        Sheet sheet = wb.createSheet("new sheet");

        // Create a row and put some cells in it. Rows are 0 based.
        Row row = sheet.createRow(1);

        // Create a new font and alter it.
        Font font = wb.createFont();
        font.setFontHeightInPoints((short) 24);
        font.setFontName("Courier New");
        font.setItalic(true);
        font.setStrikeout(true);

        // Fonts are set into a style so create a new one to use.
        CellStyle style = wb.createCellStyle();
        style.setFont(font);

        // Create a cell and put a value in it.
        Cell cell = row.createCell(1);
        cell.setCellValue("This is a test of fonts");
        cell.setCellStyle(style);

        // CellStyle style = workbook.createCellStyle();
        // Font font = workbook.createFont();
        // font.setBoldweight(Font.BOLDWEIGHT_BOLD);
        // style.setFont(font);
        // for (int i = 0; i < 10000; i++) {
        // Row row = sheet.createRow(i);
        // Cell cell = row.createCell((short) 0);
        // cell.setCellStyle(style);
        // }

        // Write the output to a file
        FileOutputStream fileOut = new FileOutputStream("workbook.xls");
        wb.write(fileOut);
        fileOut.close();

    }


}
