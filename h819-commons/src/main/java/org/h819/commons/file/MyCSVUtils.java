package org.h819.commons.file;

import org.apache.commons.collections4.SetUtils;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.h819.commons.MyCharsetUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.*;

/**
 * Description : TODO(利用 org.apache.commons.csv 解析)
 * User: h819
 * Date: 2015-02-28
 * Time: 上午10:33
 * To change this template use File | Settings | File Templates.
 */

public class MyCSVUtils {


    //private static final Logger logger = LoggerFactory.getLogger(MyCSVUtils.class);
    // File from = new File("E:\\123");

    File to = new File("e:\\456");


    public static void main(String[] args) {

        MyCSVUtils t = new MyCSVUtils();

        t.test();


        //    System.out.print(MyStringUtils.startsWithAny("25.36", "1", "2", "3", "4", "5", "6", "7", "8", "9", "0"));
    }

    /**
     * 获取所有行数据
     *
     * @param csvFile   csv 文件
     * @param duplicate 是否过滤重复行 。判断重复行的依据是各个单元格内容是否相同
     * @return
     * @throws IOException
     */
    public static List<CSVRecord> getCSVRecords(File csvFile, boolean duplicate) throws IOException {

        List<CSVRecord> collection = CSVParser.parse(csvFile, MyCharsetUtils.detectEncoding(csvFile), CSVFormat.DEFAULT).getRecords();

        if (duplicate) {
            //    logger.info("in list");
            return collection;
        } else {
            //自定义了一个比较器，只比较单元格内容
            //    logger.info("in set.");
            Set set = new TreeSet(new Comparator<CSVRecord>() {
                @Override
                // record.toString() 方法输出为  CSVRecord [comment=null, mapping=null, recordNumber=55, values=[USDA, 美国农业部, 1]]
                // values 为各行的值
                public int compare(CSVRecord csv1, CSVRecord csv2) {
                    String values1 = getCSVListValues(csv1).toString();
                    String values2 = getCSVListValues(csv2).toString();
                    return values1.compareTo(values2);
                }
            });

            //创建一个按照 参数 set 进行排序的空 set ，用法见 SetUtils
            Set set1 = SetUtils.orderedSet(set);
            //重新用 set 包装，去掉重复元素
            for (CSVRecord csvRecord : collection) {
                set1.add(csvRecord);
            }

            return new ArrayList<>(set1);
        }
    }

    /**
     * 得到指定行号的行 CSVRecord
     *
     * @param csvFile    CSV 文件
     * @param lineNumber 指定行号（第 0 行号，第 7 行 ... 行号从 0 开始）
     * @return
     * @throws IOException
     */
    public static CSVRecord getCSVRecord(File csvFile, int lineNumber) throws IOException {
        return CSVParser.parse(csvFile, MyCharsetUtils.detectEncoding(csvFile), CSVFormat.DEFAULT).getRecords().get(lineNumber);
    }


    /**
     * 在行数据中，得到指定列号的单元格数据
     *
     * @param csvRecord 行数据
     * @param index     列号（从 0 开始）
     * @return
     */
    public static String getCSVValue(CSVRecord csvRecord, int index) {
        return csvRecord.get(index);

    }

    /**
     * 在行数据中，得到指定列名称的单元格数据
     *
     * @param csvRecord                 行数据
     * @param excelColumnAlphaTitleName 英文名字为 AA-ZZ 之间，不区分大小写 。
     *                                  Excel 显示的列名称，在 ExcelUtils 中定义,只能用于微软 Excel 软件打开 CSV 文件格式。
     *                                  CSV 文件本身没有列名称属性，只是微软的 Excel 软件打开 CSV 文件的时候，Excel 软件本身加上了列名称，便于查看。
     * @return
     */
    public static String getCSVValue(CSVRecord csvRecord, String excelColumnAlphaTitleName) {
        return csvRecord.get(MyExcelUtils.convertColumnTitleToIndex(excelColumnAlphaTitleName.toUpperCase()));

    }

    /**
     * 获取行数据的所有单元格值
     *
     * @param csvRecord 行数据
     * @return 数组顺序就是单元格顺序
     */
    public static String[] getCSVArrayValues(CSVRecord csvRecord) {
        int size = csvRecord.size();
        String[] values = new String[size];
        int i = 0;
        while (i < size) {
            values[i] = csvRecord.get(i);
            i++;
        }
        return values;
    }

    /**
     * 获取行数据的所有单元格值
     *
     * @param csvRecord 行数据
     * @return 数组顺序就是单元格顺序
     */
    public static List<String> getCSVListValues(CSVRecord csvRecord) {
        return Arrays.asList(getCSVArrayValues(csvRecord));
    }

    private void test() {

        try {

            File csvFile = new File("D:\\01\\0000.csv");

            Collection<CSVRecord> csvRecords = getCSVRecords(csvFile, false);


            for (CSVRecord record : csvRecords) {

                System.out.println(getCSVListValues(record).toString());

                //  System.out.println("record.size() : " + record.size());

                System.out.println(record.getRecordNumber() + " - > 0=" + MyCSVUtils.getCSVValue(record, "a") + " , 1=" + MyCSVUtils.getCSVValue(record, "b") + " , 2=" + MyCSVUtils.getCSVValue(record, "c"));
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}


