package org.h819.commons.file;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.poi.hssf.extractor.ExcelExtractor;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.h819.commons.MyDateUtils;
import org.h819.commons.file.excel.poi.vo.ExcelCell;
import org.h819.commons.file.excel.poi.vo.ExcelLine;
import org.h819.commons.json.JsonStringLineType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.*;

/**
 * Description : TODO(静态方法)
 * User: h819
 * Date: 14-3-13
 * Time: 下午4:02
 * To change this template use File | Settings | File Templates
 * <p>
 * http://viralpatel.net/blogs/java-read-write-excel-file-apache-poi/
 * .
 */

//其他项目
//https://github.com/spring-projects/spring-batch-extensions/tree/master/spring-batch-excel
//https://code.csdn.net/zhangdaiscott/easypoi
//http://git.oschina.net/chyxion/table-to-xls
//https://github.com/chenjianjx/sep4j
public class MyExcelUtils {

    private static Logger logger = LoggerFactory.getLogger(MyExcelUtils.class);
    //默认输出的 txt 文件名
    private static String defaultTxtFile = "txtFile.txt";
    private static String defaultDatePattern = MyDateUtils.datePattern;

    /**
     * 只能静态引用
     */
    private MyExcelUtils() {

    }

    /**
     * 转换 excel 数据至 txt 数据。
     * <p>
     * 默认保存在 excel 同级目录，默认文件名和单元格分隔符。
     *
     * @param excelFile
     * @return
     */

    public static void writeExcelToJsonFile(File excelFile, boolean duplicate) {

        writeExcelToJsonFile(excelFile, new File(excelFile.getParent() + "\\" + defaultTxtFile), defaultDatePattern, duplicate);

    }

    /**
     * 转换 excel 数据至 json  数据。
     * 一次性读取所有数据，没有考虑性能问题。
     *
     * @param excelFile   excel 文件
     * @param jsonFile    包含 excel 数据的结果
     * @param datePattern 日期格式  yyyy-MM-dd , yyyy-MM-dd HH:mm:ss  ...
     * @return 包含 excel 数据的集合
     */

    public static void writeExcelToJsonFile(File excelFile, File jsonFile, String datePattern, boolean duplicate) {

        List rowsSet = readExcel(excelFile, datePattern, duplicate);
        // 输出结果
        if (!rowsSet.isEmpty())
            try {
                FileUtils.write(jsonFile, JSON.toJSONString(rowsSet), StandardCharsets.UTF_8);
            } catch (IOException e) {
                e.printStackTrace();
            }
    }

    /**
     * 反序列化 json 数据为 excel 对象
     *
     * @param jsonFile 包含由 excle 序列化生成的 json 字符串的文件
     * @param type     jsonFile 中的序列化字符串：
     *                 每行是一个 json ，表示 excel 一行数据用 true 表示；
     *                 多个 json在一个集合中，序列化之后为一个字符串， false。
     *                 反序列化时，读取字符串方法不同。
     * @return 无序 list ，可通过相关属性判断顺序
     * @throws IOException
     */
    public static List<ExcelLine> readFromJsonFile(File jsonFile, JsonStringLineType.LineType type) throws IOException {

        if (type.equals(JsonStringLineType.LineType.ArrayObject)) {//读出一个字符串，反序列化数组

            List<ExcelLine> lineBeans = JSON.parseArray(FileUtils.readFileToString(jsonFile, StandardCharsets.UTF_8), ExcelLine.class);
            return lineBeans;

        } else if (type.equals(JsonStringLineType.LineType.Object)) { //逐行读出单个对象

            List<String> list = FileUtils.readLines(jsonFile);
            List<ExcelLine> multi = new ArrayList<ExcelLine>(list.size());
            for (String s : list)
                multi.add(JSON.parseObject(s, ExcelLine.class));
            return multi;
        } else return Lists.newArrayList();
    }

    /**
     * 默认分隔符，默认日期格式
     *
     * @param excelFile
     * @return
     */
    public static List<ExcelLine> readExcel(File excelFile, boolean duplicate) {
        return readExcel(excelFile, defaultDatePattern, duplicate);
    }

    /**
     * 读取 excel 文件所有内容到 set 中，利用 set 的唯一性，去掉了相同行，并且保留原来的行序</br>
     * 一次性读取所有数据，没有考虑性能问题。
     * 如果单元格为空白，则返回的行中，无此单元格，所以返回的行的单元格可能是不连续的，如 A 列，C 列 ... ，没有 B 列
     *
     * @param excelFile   excel 文件
     * @param datePattern 日期格式  yyyy-MM-dd , yyyy-MM-dd HH:mm:ss  ...
     * @param duplicate   是否过滤重复行 。判断重复行的依据是各个单元格内容是否相同
     * @return 包含 excel 数据的集合
     */

    public static List<ExcelLine> readExcel(File excelFile, String datePattern, boolean duplicate) {

        Workbook workbook; //<-Interface, accepts both HSSF and XSSF.
        // set 可以过滤重复元素。

        Collection<ExcelLine> lines;

        if (duplicate) // 允许重复
            lines = new ArrayList();
        else
            lines = new LinkedHashSet();    // 利用 LinkedHashSet 来保证元素按照添加顺序排序，默认的比较器

        logger.info("read excel begin...");

        //判断文件类型
        try {

            FileInputStream fileInputStream = new FileInputStream(excelFile);

            if (FilenameUtils.isExtension(excelFile.getName().toLowerCase(), "xls")) {
                logger.info(excelFile.getName() + " extension is  xls");
                workbook = new HSSFWorkbook(fileInputStream);
            } else if (FilenameUtils.isExtension(excelFile.getName().toLowerCase(), "xlsx")) {
                logger.info(excelFile.getName() + " extension is  xlsx");
                workbook = new XSSFWorkbook(fileInputStream);
            } else {
                throw new IllegalArgumentException("Received file does not have a standard excel extension.");
            }

            // 循环 sheet
            for (int i = 0; i < workbook.getNumberOfSheets(); i++) {
                // 循环行
                for (Row row : workbook.getSheetAt(i)) {

                    ExcelLine excelLine = new ExcelLine();
                    excelLine.setFileName(excelFile.getName());
                    excelLine.setSheetName(workbook.getSheetName(i));
                    excelLine.setSheetNmuber(i);
                    excelLine.setLineNumber(row.getRowNum());
                    //For each row, iterate through each columns
                    Iterator<Cell> cellIterator = row.cellIterator();
                    //单行的每个单元格
                    //   * <p>

                    while (cellIterator.hasNext()) { //.hasNext() 方法原理：如果单元格为空，结果为 false，直接跳到下一个有内容的单元格。所以返回的行的单元格可能是不连续的，如 A 列，C 列 ... ，没有 B 列
                        Cell cell = cellIterator.next();
                        ExcelCell excelCell = new ExcelCell();
                        excelCell.setTile(convertColumnIndexToTitle(cell.getColumnIndex()));
                        excelCell.setValue(getFormatCellValue(cell, datePattern));
                        excelLine.addCellValue(excelCell);

                    } //单行完成
                    // 添加单行数据
                    lines.add(excelLine);

                } //单行循环完成

            } //所有行完成

            fileInputStream.close();

        } catch (IOException e) {
            e.printStackTrace();
        }

        logger.info("read excel end...");

        //重新包装为 list
        return new ArrayList(lines);
    }

    public static void writeExcel(List<ExcelLine> lines, File outExcelFile) throws Exception {

        writeExcel(lines, null, outExcelFile);
    }

    public static void writeExcel(List<ExcelLine> lines, String sheetName, File outExcelFile) throws Exception {

        if (!outExcelFile.getAbsolutePath().toLowerCase().endsWith(".xlsx")) {
            throw new Exception("输出文件必须是 xlsx 类型");
        }

        //Blank workbook
        XSSFWorkbook workbook = new XSSFWorkbook();
        XSSFSheet sheet;
        //Create a blank sheet
        if (sheetName == null)
            sheet = workbook.createSheet();
        else
            sheet = workbook.createSheet(sheetName);
        int rowNum = 0;
        for (ExcelLine line : lines) {
            Row row = sheet.createRow(rowNum++); //第一行
            Set<ExcelCell> cells = line.getCellValues();
            for (ExcelCell excelCell : cells) {
                int cellIndex = convertColumnTitleToIndex(excelCell.getTile());  // 根据 title ，获得列序号
                Cell newCell = row.createCell(cellIndex);
                String value = excelCell.getValue();
                newCell.setCellValue(value);
            }
        }
        try {
            //Write the workbook in file system
            FileOutputStream out = new FileOutputStream(outExcelFile);
            workbook.write(out);
            workbook.close();
            out.flush();
            out.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 通过列号，解析行数据，得到指定的列值。此函数仅针对 ExcelLine Bean 结构数据
     * <p>
     * 用 replaceCellValueByColumAlphaName 代替 ，以符合 Excel 习惯
     *
     * @param excelLine   行字符串值
     * @param columnIndex 指定列序号，从 0 开始
     * @return 指定列号的单元格数值，列号不存在时，返回 ""
     */

    @Deprecated
    public static String getCellValueByColumnIndex(ExcelLine excelLine, int columnIndex) {

        return getCellValueByColumnAlphaTitleName(excelLine, convertColumnIndexToTitle(columnIndex));
    }

    /**
     * 通过列英文名称，解析行数据，得到指定的列值。此函数仅针对 readExcel.java 生成的 set 中的行结构有效
     * <p>
     *
     * @param excelLine   excel 单行数据
     * @param cloumnTitle 列名 ，英文名字为 AA-ZZ 之间，不区分大小写
     * @return 不存在该单元格时，返回 null
     */
    public static String getCellValueByColumnAlphaTitleName(ExcelLine excelLine, String cloumnTitle) {


        for (ExcelCell bean : excelLine.getCellValues()) {
            if (bean.getTile().equals(cloumnTitle.toUpperCase()))
                return bean.getValue();
        }

        return null;
    }

    /**
     * 得到指定行，指定列的单元格数值。此函数仅针对 readExcel.java 生成的 set 中的行结构有效
     *
     * @param excelLines      包含所有 excel 行的集合
     * @param lineNumber      行号
     * @param cloumnAlphaName 列名 ，英文名字为 AA-ZZ 之间，不区分大小写
     * @return 不存在该单元格时，返回 null
     */
    public static String getCellValueByCloumnAlphaNameAndLineNumber(Set<ExcelLine> excelLines, String cloumnAlphaName, int lineNumber) {

        for (ExcelLine lineBean : excelLines) {
            if (lineBean.getLineNumber() == lineNumber) {
                for (ExcelCell bean : lineBean.getCellValues()) {
                    if (bean.getTile().equals(cloumnAlphaName.toUpperCase()))
                        return bean.getValue();
                }
            }
        }
        return null;
    }

    /**
     * 替换行的指定列值
     * <p>
     * 用 replaceCellValueByColumAlphaName 代替 ，以符合 Excel 习惯
     *
     * @param excelLine    行字符串值
     * @param columnIndex  指定列号 ，从 0 开始
     * @param newCellValue 新列值
     * @return 替换后的行字符串 列号不存在时，返回原行字符串
     */
    @Deprecated
    static ExcelLine replaceCellValueByColumnTitleIndex(ExcelLine excelLine, int columnIndex, String newCellValue) {

        return replaceCellValueByColumnAlphaTitleName(excelLine, convertColumnIndexToTitle(columnIndex), newCellValue);

    }

    /**
     * 替换行的指定列值
     *
     * @param excelLine            excel 行，只包含有值的列，空白列不包含，所以列号可能不连续
     * @param columnAlphaTitleName 指定列名称字母
     * @param newCellValue         新列值
     * @return 替换后的行字符串 列名 ColumnAlphaTitleName 不存在时，返回原行字符串
     */
    public static ExcelLine replaceCellValueByColumnAlphaTitleName(ExcelLine excelLine, String columnAlphaTitleName, String newCellValue) {

        boolean tag = false;

        Set<ExcelCell> set = excelLine.getCellValues();
        Set<ExcelCell> setNew = new TreeSet();

        //重新构造，以保持单元格在 set 中的顺序
        for (ExcelCell bean : set) {// 循环所遇列
            if (bean.getTile().equals(columnAlphaTitleName)) {  // 找到了对应的列名
                tag = true;
                setNew.add(new ExcelCell(bean.getTile(), newCellValue));
            } else  // 没找到，则之间添加
                setNew.add(bean);
        }

        if (!tag) //excelLine 不包含指定的列名，增加一列
            setNew.add(new ExcelCell(columnAlphaTitleName.toUpperCase(), newCellValue));

        // 重置 excelLineBean
        excelLine.setCellValues(setNew);

        return excelLine;

    }

    /**
     * 根据 cell 格式，自动转换 cell 内容为 String
     *
     * @param cell
     * @param datePattern 日期格式  yyyy-MM-dd , yyyy-MM-dd HH:mm:ss  ...
     * @return
     */
    private static String getFormatCellValue(Cell cell, String datePattern) {

        //如果是日期格式，重新格式化
        if (cell.getCellType() == Cell.CELL_TYPE_NUMERIC && DateUtil.isCellDateFormatted(cell)) {

            return DateFormatUtils.format(cell.getDateCellValue(), datePattern);

        } else //默认格式化

            return new DataFormatter().formatCellValue(cell).trim();


        /**
         * DataFormatter().formatCellValue(cell) 的源码
         */
//        if (cell == null) {
//            return "";
//        }
//
//        int cellType = cell.getCellType();
//        if (cellType == Cell.CELL_TYPE_FORMULA) {
//            if (evaluator == null) {
//                return cell.getCellFormula();
//            }
//            cellType = evaluator.evaluateFormulaCell(cell);
//        }
//        switch (cellType) {
//            case Cell.CELL_TYPE_NUMERIC :
//
//                if (DateUtil.isCellDateFormatted(cell)) {
//                    return getFormattedDateString(cell);
//                }
//                return getFormattedNumberString(cell);
//
//            case Cell.CELL_TYPE_STRING :
//                return cell.getRichStringCellValue().getString();
//
//            case Cell.CELL_TYPE_BOOLEAN :
//                return String.valueOf(cell.getBooleanCellValue());
//            case Cell.CELL_TYPE_BLANK :
//                return "";
//            case Cell.CELL_TYPE_ERROR:
//                return FormulaError.forInt(cell.getErrorCellValue()).getString();
//        }
//        throw new RuntimeException("Unexpected celltype (" + cellType + ")");


    }

    /**
     * 根据 excel 列序号，计算列名称
     * Given a positive integer, return its corresponding column title as appear in an Excel sheet
     * ---
     * 1 -> A
     * 2 -> B
     * 3 -> C
     * ...
     * 26 -> Z
     * 27 -> AA
     * 28 -> AB
     * <p>
     * 把10进制的转换成26进制，做法是除26取余，一直除到0，最后把余数逆序一下就行了。不过因为A是1，而不是0，相当于26进制的数都整体减1，才能对应上从0开始的十进制数。
     *
     * @param columnIndex 列序号，从 0 开始
     * @return http://www.programcreek.com/2014/03/leetcode-excel-sheet-column-title-java/
     */

    public static String convertColumnIndexToTitle(int columnIndex) {
        //下面的计算，index 需要大于 0 ，所以对于从 0 开始的 index，需要 +1
        columnIndex++; // n+1
        if (columnIndex <= 0) {
            throw new IllegalArgumentException("Input is not valid!");
        }
        String s = "";
        while (columnIndex > 0) {
            columnIndex--;
            s = ((char) (columnIndex % 26 + 'A')) + s;
            columnIndex = columnIndex / 26;
        }
        return s;

    }

    /**
     * 根据 excel 列名称，计算列序号 , 和 indexToTitle(int index) 方法相反
     * ---
     * 通过列英文名称，返回列号 列数从 0 开始计算，如 0->A,1->B ... ，方便计算
     * ---
     * 其实就是把26进制的数转换为10进制的数。
     * 算法就是基本的进制转换方法，从后往前第n位的值乘上26^(n-1)。
     * 这里26进制数是1开始的，即A是1。
     *
     * @param title
     * @return
     */
    public static int convertColumnTitleToIndex(String title) {
        if (title == null || title.length() == 0) {
            throw new IllegalArgumentException("Input is not valid!");
        }
        title = title.toUpperCase();
        int result = 0;
        for (int i = 0; i < title.length(); i++) {
            result = result * 26 + 1 + title.charAt(i) - 'A';
        }
        return result - 1;
    }


    /**
     * 判断给定的行是否为空白行
     *
     * @param row 指定行
     * @return
     */
    private boolean isBlankRow(Row row) {

        boolean isBlank = true;
        for (Cell cell : row) {// 循环列
            // 跳过空白 cell
            if (cell.getCellType() == cell.CELL_TYPE_BLANK)
                continue;
            isBlank = false;
        }
        return isBlank;
    }

    /**
     * 利用 POI 提供的工具，提取文件内容为字符串
     *
     * @param excelFile 待提取的 excel 文件
     * @return
     */
    public String excelExtractor(File excelFile) {

        try {
            HSSFWorkbook wb = new HSSFWorkbook(new FileInputStream(excelFile));
            ExcelExtractor extractor = new ExcelExtractor(wb);
            extractor.setFormulasNotResults(true);
            extractor.setIncludeSheetNames(true);
            return extractor.getText();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return null;
        }
    }


}
