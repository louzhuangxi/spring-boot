package org.h819.web.jqgird;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * jqgrid 创建表格，数据源有多种，如 xml、json、jsonp 、array 等。
 * -
 * 详见 http://www.trirand.com/jqgridwiki/doku.php?id=wiki:retrieving_data
 * -
 * 数据源为 json 时，jqgrid 会默认生成如下参数(root,page,total,records,repeattimes,cell,id,userdata,subgrid ...)，这些参数都不能为空，并对应于返回的数据源(参数<->数据源)，二者对应之后，jqgrid 根据数据源创建表格。
 * -
 * 默认的对应关系如下( 参数 : 对于的 json 结构中的数据源名称  ，二者用冒号分开)
 * <p>
 * jqgrid 提供 jsonReader 配置来说明设置此种对应关系，如参数 root 对于 json 中的 rows 变量、参数 page 对于 json 中的 page 变量 ...
 * -
 * 其中较为特殊的是参数 id , jqgrid 创建的表格，把此参数作为 the unique id of the row ，来确定每行数据。这对针对行数据的删除和编辑操作等，提供确定是哪一行是必须且关键的。
 * 这个 id 并不是 rownumbers : true  后，表格前面的序号，该序号是 jqgrid 显示用的，和 unique key 无关。
 * -
 * 1. 如果返回的 json 数据，集合 rows 中包含的对象有属性 id ，则 jqgrid 会自动设置创建的表格的 id 值为该对象的 id 的值。（此 id 一定不能为 null，必须有值，即返回的每个对象的属性 id 必须有值，否则表格显示异常。by 排查了两天，才找到原因!）
 * -
 * 2. 如果返回的 json 数据，集合 rows 中包含的对象没有属性 id ，则 jqgrid 根据返回的数据集合的先后顺序，自动生成一个序号，做为 表格的 id （上文提到的表格唯一 id）， 也就是 rownumbers : true  后，表格前面的序号中所示
 * 但此时自动生成的 id ，并不是 the unique id of the row 。那么如何确定 unique key 呢？
 * “集合 rows 中包含的对象没有属性 id”，一般情况下，该对象是有其他属性（这是数据库设计范围的问题），作为了该对象的 unique id ，只是名字不叫 id 而已，那么修改 jsonReader 的默认配置，重新定义对应关键即可，如
 * <p>
 * 此时，当提交对某一行的修改是，提交的还是参数名称还是 id ，但数值是 Entity 的 eventId 属性。
 * <p>
 * jqgrid 根据返回的解析返回到前端的 json 字符串，json 字符串里面的参数要求，引号内为参数名.
 * <p>
 * jqgrid 提供 jsonReader 配置来说明设置此种对应关系，如参数 root 对于 json 中的 rows 变量、参数 page 对于 json 中的 page 变量 ...
 * ---
 * 此时，当提交对某一行的修改是，提交的还是参数名称还是 id ，但数值是 Entity 的 eventId 属性。
 * <p>
 * jqgrid 根据返回的解析返回到前端的 json 字符串，json 字符串里面的参数要求，引号内为参数名.
 * ---
 * 根据 jQgrid 解析要求的 JSON Data 格式，构造返回到前端的数据结构
 * <p/>
 * 此数据结构被 spring 以 ResponseBody 格式返回后，生成的 json 格式数据，可以被 jQgrid 按照 jsonReader 的定义读取，并展示。
 * <p/>
 * <p/>
 * 变量名称固定，不要改变，返回的 json 数据，jqgrid 根据此变量名称读取数据
 *
 * @see <a href="http://www.trirand.com/jqgridwiki/doku.php?id=wiki:retrieving_data#json_data">JSON Data</a>
 * http://www.trirand.com/jqgridwiki/doku.php?id=wiki:retrieving_data
 */

//如果返回的 json 的 rows 中对象属性包含上述名称时，不能为 null，否则 jqgrid 不能解析。也可以设置为其他的名字，避免冲突.

//jsonReader : {
//        root: "rows",
//        page: "page",
//        total: "total",
//        records: "records",
//        repeatitems: true,
//        cell: "cell",
//        id: "id",  // id: "alias",
//        userdata: "userdata",
//        subgrid: {
//           root:"rows",
//           repeatitems: true,
//           cell:"cell"
//          }
//        },

//    jsonReader:{
//          id:"eventId"  //该对象的 eventId 属性作为 jqgrid 的 id 参数。
//        },

public class JqgridPage<T> implements Serializable {


    private static final Logger logger = LoggerFactory.getLogger(JqgridPage.class);
    /**
     * 当前页序号
     */
    private int page;


    /**
     * 总页数
     */
    private int total;


    /**
     * 记录总数
     */

    private int records;


    /**
     * 待显示的记录集合
     * rows 中包含的 bean ，属性名字应该和前端的变量名称对应
     */
    private List<T> rows = new ArrayList();


    /**
     * Contains the actual data
     * <p/>
     * 自定义数据
     */
    private Map<String, Object> userdata;

    /**
     * 必须以有参构造方法生成
     */
    private JqgridPage() {
    }

    /**
     * 构造满足 jqgrid 要求的返回 json 数据
     *
     * @param pageSize      每页记录数
     * @param currentPageNo 当前页码(起始页数为 0)
     * @param totalRecords  记录总数
     * @param content       返回的每页记录集，最后一页的大小可能不等于 pageSize
     */
    public JqgridPage(int pageSize, int currentPageNo, int totalRecords, List<T> content) {

        if (pageSize < 0)
            throw new IllegalArgumentException("页大小不能小于 0");

        // org.springframework.data.domain.PageRequest 要求起始页 为 0 ，而 jqgrid 为 1，此处做 +1 处理
        this.page = currentPageNo + 1;
        this.records = totalRecords;
        this.rows = content;

        /**
         * 根据每页记录数和总记录数，自动计算总页数
         * 不要放在 setTotal 方法中，否则 Stirng -> PageBean 反序列化时，会出现错误。
         * 反序列化需要默认的 get set 方法
         *
         * @return
         */
        if (totalRecords < 0) {
            total = -1;
            return;
        }

        long totalTemp = totalRecords / pageSize;
        if (totalRecords % pageSize > 0) {
            totalTemp++;
        }
        total = (int) totalTemp;

    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getTotal() {
        return total;
    }


    public void setTotal(int total) {

        this.total = total;

    }

    public int getRecords() {
        return records;
    }

    public void setRecords(int records) {
        this.records = records;
    }

    public List<T> getRows() {
        return rows;
    }

    public void setRows(List<T> rows) {
        this.rows = rows;
    }

}
