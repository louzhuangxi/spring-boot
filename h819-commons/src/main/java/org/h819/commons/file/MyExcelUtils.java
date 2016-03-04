package org.h819.commons.file;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.poi.hssf.extractor.ExcelExtractor;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.h819.commons.MyDateUtils;
import org.h819.commons.file.excel.poi.bean.ExcelCell;
import org.h819.commons.file.excel.poi.bean.ExcelLine;
import org.h819.commons.json.JsonStringLineType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.*;

/**
 * Description : TODO(静态方法)
 * User: h819
 * Date: 14-3-13
 * Time: 下午4:02
 * To change this template use File | Settings | File Templates
 * <p/>
 * http://viralpatel.net/blogs/java-read-write-excel-file-apache-poi/
 * .
 */

//其他项目
//https://code.csdn.net/zhangdaiscott/easypoi
//http://git.oschina.net/chyxion/table-to-xls
    //https://github.com/chenjianjx/sep4j
public class MyExcelUtils {

    //没有通过计算得到，硬编码
    public static String[] table = new String[]{
            "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z",
            "AA", "AB", "AC", "AD", "AE", "AF", "AG", "AH", "AI", "AJ", "AK", "AL", "AM", "AN", "AO", "AP", "AQ", "AR", "AS", "AT", "AU", "AV", "AW", "AX", "AY", "AZ",
            "BA", "BB", "BC", "BD", "BE", "BF", "BG", "BH", "BI", "BJ", "BK", "BL", "BM", "BN", "BO", "BP", "BQ", "BR", "BS", "BT", "BU", "BV", "BW", "BX", "BY", "BZ",
            "CA", "CB", "CC", "CD", "CE", "CF", "CG", "CH", "CI", "CJ", "CK", "CL", "CM", "CN", "CO", "CP", "CQ", "CR", "CS", "CT", "CU", "CV", "CW", "CX", "CY", "CZ",
            "DA", "DB", "DC", "DD", "DE", "DF", "DG", "DH", "DI", "DJ", "DK", "DL", "DM", "DN", "DO", "DP", "DQ", "DR", "DS", "DT", "DU", "DV", "DW", "DX", "DY", "DZ",
            "EA", "EB", "EC", "ED", "EE", "EF", "EG", "EH", "EI", "EJ", "EK", "EL", "EM", "EN", "EO", "EP", "EQ", "ER", "ES", "ET", "EU", "EV", "EW", "EX", "EY", "EZ",
            "FA", "FB", "FC", "FD", "FE", "FF", "FG", "FH", "FI", "FJ", "FK", "FL", "FM", "FN", "FO", "FP", "FQ", "FR", "FS", "FT", "FU", "FV", "FW", "FX", "FY", "FZ",
            "GA", "GB", "GC", "GD", "GE", "GF", "GG", "GH", "GI", "GJ", "GK", "GL", "GM", "GN", "GO", "GP", "GQ", "GR", "GS", "GT", "GU", "GV", "GW", "GX", "GY", "GZ",
            "HA", "HB", "HC", "HD", "HE", "HF", "HG", "HH", "HI", "HJ", "HK", "HL", "HM", "HN", "HO", "HP", "HQ", "HR", "HS", "HT", "HU", "HV", "HW", "HX", "HY", "HZ",
            "IA", "IB", "IC", "ID", "IE", "IF", "IG", "IH", "II", "IJ", "IK", "IL", "IM", "IN", "IO", "IP", "IQ", "IR", "IS", "IT", "IU", "IV", "IW", "IX", "IY", "IZ",
            "JA", "JB", "JC", "JD", "JE", "JF", "JG", "JH", "JI", "JJ", "JK", "JL", "JM", "JN", "JO", "JP", "JQ", "JR", "JS", "JT", "JU", "JV", "JW", "JX", "JY", "JZ",
            "KA", "KB", "KC", "KD", "KE", "KF", "KG", "KH", "KI", "KJ", "KK", "KL", "KM", "KN", "KO", "KP", "KQ", "KR", "KS", "KT", "KU", "KV", "KW", "KX", "KY", "KZ",
            "LA", "LB", "LC", "LD", "LE", "LF", "LG", "LH", "LI", "LJ", "LK", "LL", "LM", "LN", "LO", "LP", "LQ", "LR", "LS", "LT", "LU", "LV", "LW", "LX", "LY", "LZ",
            "MA", "MB", "MC", "MD", "ME", "MF", "MG", "MH", "MI", "MJ", "MK", "ML", "MM", "MN", "MO", "MP", "MQ", "MR", "MS", "MT", "MU", "MV", "MW", "MX", "MY", "MZ",
            "NA", "NB", "NC", "ND", "NE", "NF", "NG", "NH", "NI", "NJ", "NK", "NL", "NM", "NN", "NO", "NP", "NQ", "NR", "NS", "NT", "NU", "NV", "NW", "NX", "NY", "NZ",
            "OA", "OB", "OC", "OD", "OE", "OF", "OG", "OH", "OI", "OJ", "OK", "OL", "OM", "ON", "OO", "OP", "OQ", "OR", "OS", "OT", "OU", "OV", "OW", "OX", "OY", "OZ",
            "PA", "PB", "PC", "PD", "PE", "PF", "PG", "PH", "PI", "PJ", "PK", "PL", "PM", "PN", "PO", "PP", "PQ", "PR", "PS", "PT", "PU", "PV", "PW", "PX", "PY", "PZ",
            "QA", "QB", "QC", "QD", "QE", "QF", "QG", "QH", "QI", "QJ", "QK", "QL", "QM", "QN", "QO", "QP", "QQ", "QR", "QS", "QT", "QU", "QV", "QW", "QX", "QY", "QZ",
            "RA", "RB", "RC", "RD", "RE", "RF", "RG", "RH", "RI", "RJ", "RK", "RL", "RM", "RN", "RO", "RP", "RQ", "RR", "RS", "RT", "RU", "RV", "RW", "RX", "RY", "RZ",
            "SA", "SB", "SC", "SD", "SE", "SF", "SG", "SH", "SI", "SJ", "SK", "SL", "SM", "SN", "SO", "SP", "SQ", "SR", "SS", "ST", "SU", "SV", "SW", "SX", "SY", "SZ",
            "TA", "TB", "TC", "TD", "TE", "TF", "TG", "TH", "TI", "TJ", "TK", "TL", "TM", "TN", "TO", "TP", "TQ", "TR", "TS", "TT", "TU", "TV", "TW", "TX", "TY", "TZ",
            "UA", "UB", "UC", "UD", "UE", "UF", "UG", "UH", "UI", "UJ", "UK", "UL", "UM", "UN", "UO", "UP", "UQ", "UR", "US", "UT", "UU", "UV", "UW", "UX", "UY", "UZ",
            "VA", "VB", "VC", "VD", "VE", "VF", "VG", "VH", "VI", "VJ", "VK", "VL", "VM", "VN", "VO", "VP", "VQ", "VR", "VS", "VT", "VU", "VV", "VW", "VX", "VY", "VZ",
            "WA", "WB", "WC", "WD", "WE", "WF", "WG", "WH", "WI", "WJ", "WK", "WL", "WM", "WN", "WO", "WP", "WQ", "WR", "WS", "WT", "WU", "WV", "WW", "WX", "WY", "WZ",
            "XA", "XB", "XC", "XD", "XE", "XF", "XG", "XH", "XI", "XJ", "XK", "XL", "XM", "XN", "XO", "XP", "XQ", "XR", "XS", "XT", "XU", "XV", "XW", "XX", "XY", "XZ",
            "YA", "YB", "YC", "YD", "YE", "YF", "YG", "YH", "YI", "YJ", "YK", "YL", "YM", "YN", "YO", "YP", "YQ", "YR", "YS", "YT", "YU", "YV", "YW", "YX", "YY", "YZ",
            "ZA", "ZB", "ZC", "ZD", "ZE", "ZF", "ZG", "ZH", "ZI", "ZJ", "ZK", "ZL", "ZM", "ZN", "ZO", "ZP", "ZQ", "ZR", "ZS", "ZT", "ZU", "ZV", "ZW", "ZX", "ZY", "ZZ"
    };
    /**
     * 构造的行格式
     * <p/>
     * FileName=03.xls:sheetNumber=0:rowNum=0:rowValue:0=stdid@1=stdcn@2=stden
     * <p/>
     * 本类中，所有函数均以上数格式的行数据为基础
     * <p/>
     * 如果遇到特殊情况，可能是分隔符的问题，调整默认分隔符即可
     */

    //  此函数仅针对 readExcel.java 生成的 set 中的行结构有效

    private static Logger logger = LoggerFactory.getLogger(MyExcelUtils.class);
    //默认输出的 txt 文件名
    private static String defautTxtFile = "txtFile.txt";
    private static String defautDatePattern = MyDateUtils.datePattern;

    public MyExcelUtils() {

    }

    /**
     * @param args
     */
    public static void main(String[] args) {

        Collection<ExcelLine> s = MyExcelUtils.readExcel(new File("d:\\00\\fast.XLS"), MyDateUtils.datePattern, false);

        System.out.println("index of : " + ArrayUtils.indexOf(table, "z"));


        System.out.println(JSON.toJSONString(s));

        for (ExcelLine line : s) {

            System.out.println(JSON.toJSONString(MyExcelUtils.replaceCellValueByColumAlphaName(line, "h", "换成了h")));

        }

        MyExcelUtils.writeExcelToJsonFile(new File("d:\\00\\fast.XLS"), false);

    }

    /**
     * 转换 excel 数据至 txt 数据。
     * <p/>
     * 默认保存在 excel 同级目录，默认文件名和单元格分隔符。
     *
     * @param excelFile
     * @return
     */

    public static void writeExcelToJsonFile(File excelFile, boolean duplicate) {

        writeExcelToJsonFile(excelFile, new File(excelFile.getParent() + "\\" + defautTxtFile), defautDatePattern, duplicate);

    }

    /**
     * 转换 excel 数据至 json  数据。
     * 一次性读取所有数据，没有考虑性能问题。
     *
     * @param excelFile   excel 文件
     * @param txtFile     包含 excel 数据的结果
     * @param datePattern 日期格式  yyyy-MM-dd , yyyy-MM-dd HH:mm:ss  ...
     * @return 包含 excel 数据的集合
     */

    public static void writeExcelToJsonFile(File excelFile, File txtFile, String datePattern, boolean duplicate) {

        Collection rowsSet = readExcel(excelFile, datePattern, duplicate);
        // 输出结果
        if (!rowsSet.isEmpty())
            try {
                FileUtils.write(txtFile, JSON.toJSONString(rowsSet));
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

            List<ExcelLine> lineBeans = JSON.parseArray(FileUtils.readFileToString(jsonFile), ExcelLine.class);
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
    public static Collection<ExcelLine> readExcel(File excelFile, boolean duplicate) {
        return readExcel(excelFile, defautDatePattern, duplicate);
    }

    /**
     * 读取 excel 文件所有内容到 set 中，利用 set 的唯一性，去掉了相同行，并且保留原来的行序</br>
     * 一次性读取所有数据，没有考虑性能问题。
     * <p/>
     *
     * @param excelFile   excel 文件
     * @param datePattern 日期格式  yyyy-MM-dd , yyyy-MM-dd HH:mm:ss  ...
     * @param duplicate   是否过滤重复行 。判断重复行的依据是各个单元格内容是否相同
     * @return 包含 excel 数据的集合
     */

    public static Collection<ExcelLine> readExcel(File excelFile, String datePattern, boolean duplicate) {

        Workbook workbook; //<-Interface, accepts both HSSF and XSSF.
        // set 可以过滤重复元素。

        Collection<ExcelLine> lineValues = null;

        if (duplicate) // 允许重复
            lineValues = new ArrayList<ExcelLine>();
        else
            lineValues = new LinkedHashSet();    // 利用 LinkedHashSet 来保证元素按照添加顺序排序，默认的比较器

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

                    // 跳过空白行
                    //  if (isBlankRow(row))
                    //     continue;

                    ExcelLine excelLine = new ExcelLine();
                    excelLine.setFileName(excelFile.getName());
                    excelLine.setSheetNmuber(i);
                    excelLine.setLineNumber(row.getRowNum());

                    // Set

                    //For each row, iterate through each columns
                    Iterator<Cell> cellIterator = row.cellIterator();
                    //单行的每个单元格
                    while (cellIterator.hasNext()) { //.hasNext() 方法，如果单元格为空，结果为 false，直接跳到下一个有内容的单元格
                        Cell cell = cellIterator.next();

                        ExcelCell excelCell = new ExcelCell();
                        if (cell.getColumnIndex() > table.length)
                            throw new ArrayIndexOutOfBoundsException("列数过多，title 不在 AA-ZZ 区间");

                        excelCell.setTile(table[cell.getColumnIndex()]);
                        excelCell.setValue(getFormatCellValue(cell, datePattern));
                        excelLine.addCellValue(excelCell);

                    } //单行完成
                    // 添加单行数据
                    lineValues.add(excelLine);

                } //单行循环完成

            } //所有行完成

            fileInputStream.close();
//            FileOutputStream out =
//                    new FileOutputStream(new File("d:\\test.xlsx"));
//           workbook.write(out);
//            out.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        logger.info("read excel end...");

        return lineValues;
    }

    /**
     * 通过列号，解析行数据，得到指定的列值。此函数仅针对 ExcelLineBean 结构数据
     * <p/>
     * 用 replaceCellValueByColumAlphaName 代替 ，以符合 Excel 习惯
     *
     * @param excelLineBean 行字符串值
     * @param columnIndex   指定列序号，从 0 开始
     * @return 指定列号的单元格数值，列号不存在时，返回 ""
     */

    @Deprecated
    public static String getCellValueByColumnIndex(ExcelLine excelLineBean, int columnIndex) {

        return getCellValueByCloumnAlphaName(excelLineBean, table[columnIndex]);
    }

    /**
     * 通过列英文名称，解析行数据，得到指定的列值。此函数仅针对 readExcel.java 生成的 set 中的行结构有效
     * <p/>
     *
     * @param excelLineBean   excel 单行数据
     * @param cloumnAlphaName 列名 ，英文名字为 AA-ZZ 之间，不区分大小写
     * @return 不存在该单元格时，返回 null
     */
    public static String getCellValueByCloumnAlphaName(ExcelLine excelLineBean, String cloumnAlphaName) {


        for (ExcelCell bean : excelLineBean.getCellValues()) {
            if (bean.getTile().equals(cloumnAlphaName.toUpperCase()))
                return bean.getValue();
        }

        return null;
    }

    /**
     * 得到指定行，指定列的单元格数值。此函数仅针对 readExcel.java 生成的 set 中的行结构有效
     *
     * @param excelLineBeans  包含所有 excel 行的集合
     * @param lineNumber      行号
     * @param cloumnAlphaName 列名 ，英文名字为 AA-ZZ 之间，不区分大小写
     * @return 不存在该单元格时，返回 null
     */
    public static String getCellValueByCloumnAlphaNameAndLineNumber(Set<ExcelLine> excelLineBeans, String cloumnAlphaName, int lineNumber) {

        for (ExcelLine lineBean : excelLineBeans) {
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
     * <p/>
     * 用 replaceCellValueByColumAlphaName 代替 ，以符合 Excel 习惯
     *
     * @param excelLineBean 行字符串值
     * @param columnIndex   指定列号 ，从 0 开始
     * @param newCellValue  新列值
     * @return 替换后的行字符串 列号不存在时，返回原行字符串
     */
    @Deprecated
    static ExcelLine replaceCellValueByColumnIndex(ExcelLine excelLineBean, int columnIndex, String newCellValue) {

        return replaceCellValueByColumAlphaName(excelLineBean, table[columnIndex], newCellValue);

    }

    /**
     * 替换行的指定列值
     * <p/>
     * * 英文名字为 AA-ZZ 之间，不区分大小写
     * <p/>
     * <p/>
     * 参考  getCellValueByColumnIndex 方法实现
     *
     * @param excelLineBean   行字符串值
     * @param CloumnAlphaName 指定列名称字母
     * @param newCellValue    新列值
     * @return 替换后的行字符串 列号不存在时，返回原行字符串
     */
    public static ExcelLine replaceCellValueByColumAlphaName(ExcelLine excelLineBean, String CloumnAlphaName, String newCellValue) {


        Set<ExcelCell> set = excelLineBean.getCellValues();
        Set<ExcelCell> setNew = new LinkedHashSet<ExcelCell>(set.size());

        //重新构造，以保持单元格在 set 中的顺序
        for (ExcelCell bean : set) {
            if (bean.getTile().equals(CloumnAlphaName)) {
                setNew.add(new ExcelCell(bean.getTile(), newCellValue));
            } else
                setNew.add(bean);
        }

        excelLineBean.setCellValues(setNew);

        return excelLineBean;

    }

    /**
     * 根据 cell 格式，自动转换 cell 内容为 String
     * <p/>
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
     * 没有通过计算得到，硬编码
     * <p/>
     * 通过列英文名称，返回列号 列数从 0 开始计算，如 0->A,1->B ... ，方便计算
     * <p/>
     * 仅能用于列数 AA -> ZZ 26*26= 676
     *
     * @param CloumnAlphaName 列英文名称，小于 AA-ZZ 之间
     * @return
     */
    private static int getCloumnNumberByColumAlphaName(String CloumnAlphaName) {

        List<String> list = Arrays.asList(table);

        CloumnAlphaName = CloumnAlphaName.toUpperCase();

        if (list.contains(CloumnAlphaName))
            return list.indexOf(CloumnAlphaName);
        else {

            throw new ArrayIndexOutOfBoundsException();
        }

    }


    /**
     * 写 excel 例子。因为写的需求每个项目都不一样，所以这里不给通用的函数，仅给个例子，项目可根据这个函数来修改。
     */
    private void writeExample() {
        // create a new workbook
        Workbook wb = new HSSFWorkbook();

        // 创建一个WorkBook，从指定的文件流中创建，即上面指定了的文件流
        // FileInputStream readFile = new FileInputStream(templatefilepath);
        // Workbook wb = new HSSFWorkbook(readFile);
        // 获取指定的 sheet 对象
        // Sheet sheet = wb.getSheetAt(0);

        // create a new sheet
        Sheet s = wb.createSheet("new sheet");
        // declare a row object reference
        Row r = null;
        // declare a cell object reference
        Cell c = null;

        // create a sheet with 30 rows (0-29)
        int rownum;
        for (rownum = (short) 0; rownum < 30; rownum++) {
            // create a row
            r = s.createRow(rownum);
            // create 10 cells (0-9) (the += 2 becomes apparent later
            for (short cellnum = (short) 0; cellnum < 10; cellnum += 2) {
                c = r.createCell(cellnum);
                c.setCellValue("");
            }
        }

        try {
            FileOutputStream out = new FileOutputStream("d:\\workbook.xls");
            wb.write(out);
            out.close();
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

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
