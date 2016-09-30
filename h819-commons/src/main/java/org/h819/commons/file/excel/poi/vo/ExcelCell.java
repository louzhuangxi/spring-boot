package org.h819.commons.file.excel.poi.vo;

/**
 * Description : TODO(实现 Comparable ，在 Excel 行中，按照列的 title 排序)
 * User: h819
 * Date: 2014-10-20
 * Time: 12:29
 * To change this template use File | Settings | File Templates.
 */
public class ExcelCell implements Comparable {


    /**
     * title 构造的时候，直接转换为大写
     */

    //还可以添加其他单元格属性
    //单元格横向表头 A,B,C ...
    private String title = "";
    private String value = "";


    public ExcelCell() {


    }

    /**
     * 构造 excel 单元格键值
     *
     * @param title 单元格 title
     * @param value 数值
     */
    public ExcelCell(String title, String value) {
        this.title = title.toUpperCase();
        this.value = value;

    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title.toUpperCase();
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }


    /**
     * 实现 Comparable ，在 set 中，按照 title 排序
     *
     * @param o
     * @return
     */
    @Override
    public int compareTo(Object o) {
        ExcelCell a = (ExcelCell) o;
        return this.title.compareTo(a.title);
    }


}
