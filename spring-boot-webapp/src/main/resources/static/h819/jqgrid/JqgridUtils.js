/* 
 jqgrid 工具类
 */

var JqgridUtils = (function () {

    /**
     * 获取选择的单行数据的 id
     *
     * getGridParam : 调用的方法名称   http://www.trirand.com/jqgridwiki/doku.php?id=wiki:methods
     * selarrrow : 方法中的参数名   http://www.trirand.com/jqgridwiki/doku.php?id=wiki:options
     *
     * @param grid_table :   <table id="grid_table"></table>  ，输入形式为  "#grid_table"
     */
    var getSelectedSingleRowId = function (grid_table) {
        var id = jQuery(grid_table).jqGrid('getGridParam', 'selrow'); //单行 id     //编辑时，返回的被选择的条目。只能返回一个，无法选了多个行时进行判断，未找到方法。
        return id;
    };

    /**
     * 获取选择的单行数据的值
     */
    var getSelectedSingleRowCellValue = function (grid_table) {
        var cellValue = jQuery(grid_table).jqGrid('getCell', selRowId, 'columnName'); //单行 cell value
        return cellValue;
    };

    /**
     * 获取选择的单行数据的 id
     */
    var getSelectedMultiRowIdArray = function (grid_table) {
        var id = jQuery(grid_table).jqGrid('getGridParam', 'selarrrow'); //多行 id ，为数组
        return id;
    };

    /**
     * 获取选择的单行数据的值
     */
    var getSelectedMultiRowCellValueArray = function (grid_table) {
        var cellValues = jQuery(grid_table).jqGrid('getRowData', selRowId, 'columnName'); //单行 cell value，为数组
        return cellValues;
        //return $jqGrid.jqGrid("getRowData", getSelectedMultiRowIdArray(grid_table)); //根据上面的id获得本行的所有数据
    };


    /**
     *
     * @param grid_table
     */
    var reloadGrid = function reloadGrid(grid_table) {
        jQuery(grid_table).trigger("reloadGrid");
    };

    /**
     * 强制不显示 jqgird 横向滚动条
     * @param grid_table
     */
    var hideX = function hideX(grid_table) {
        jQuery(grid_table).closest(".ui-jqgrid-bdiv").css({"overflow-x": "hidden"});

    }

    /**
     * 强制不显示 jqgird 纵向滚动条
     * @param grid_table
     */
    var hideY = function hideY(grid_table) {
        jQuery(grid_table).closest(".ui-jqgrid-bdiv").css({"overflow-y": "hidden"});
    }

    return {
        getSelectedSingleRowId: getSelectedSingleRowId,
        getSelectedSingleRowCellValue: getSelectedSingleRowCellValue,
        getSelectedMultiRowIdArray: getSelectedMultiRowIdArray,
        getSelectedMultiRowCellValueArray: getSelectedMultiRowCellValueArray,
        reloadGrid: reloadGrid,
        hideX: hideX,
        hideY: hideY
    };

})();