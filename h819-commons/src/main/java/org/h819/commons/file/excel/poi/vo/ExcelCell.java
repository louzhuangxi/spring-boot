package org.h819.commons.file.excel.poi.vo;

/**
 * Description : TODO(实现 Comparable ，在 Excel 行中，按照列的 title 排序)
 * User: h819
 * Date: 2014-10-20
 * Time: 12:29
 * To change this template use File | Settings | File Templates.
 */
public class ExcelCell implements Comparable {


    //还可以添加其他单元格属性
    private String tile = "";
    private String value = "";


    public ExcelCell() {


    }

    /**
     * 构造 excel 单元格键值
     *
     * @param tile  单元格 title
     * @param value 数值
     */
    public ExcelCell(String tile, String value) {
        this.tile = tile.toUpperCase();
        this.value = value;

    }

    public String getTile() {
        return tile;
    }

    public void setTile(String tile) {
        this.tile = tile.toUpperCase();
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }





    @Override
    public int compareTo(Object o) {
        ExcelCell a = (ExcelCell) o;
        return this.tile.compareTo(a.tile);
    }


}
