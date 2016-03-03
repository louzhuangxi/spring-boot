package org.h819.commons.file.excel.poi.bean;

import org.h819.commons.file.MyExcelUtils;

import java.util.Arrays;
import java.util.List;

/**
 * Description : TODO()
 * User: h819
 * Date: 2014-10-20
 * Time: 12:29
 * To change this template use File | Settings | File Templates.
 */
public class ExcelCell {


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
        tile = tile.toUpperCase();
        List list = Arrays.asList(MyExcelUtils.table);
        if (!list.contains(tile))
            throw new RuntimeException("添加的列名 title 不在允许的范围之内[AA~ZZ] : " + tile);

        this.tile = tile;
        this.value = value;

    }

    public String getTile() {
        return tile;
    }

    public void setTile(String tile) {
        tile = tile.toUpperCase();
        List list = Arrays.asList(MyExcelUtils.table);
        if (!list.contains(tile))
            throw new RuntimeException("添加的列名 title 不在允许的范围之内[AA~ZZ] : " + tile);
        this.tile = tile;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
