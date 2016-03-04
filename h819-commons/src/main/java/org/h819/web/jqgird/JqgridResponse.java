package org.h819.web.jqgird;

import org.h819.commons.MyExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

/**
 * jqgrid 创建表格，数据源有多种，如 xml、json、jsonp 、array 等。
 * -
 * 详见 http://www.trirand.com/jqgridwiki/doku.php?id=wiki:retrieving_data
 * -
 * 数据源为 json 时，jqgrid 会默认生成如下参数(root,page,total,records,repeattimes,cell,id,userdata,subgrid ...)，这些参数都不能为空，并对应于返回的数据源(参数<->数据源)，二者对应之后，jqgrid 根据数据源创建表格。
 * -
 * 默认的对应关系如下( 参数 : 对于的 json 结构中的数据源名称  ，二者用冒号分开)
 * <p/>
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
 * <p/>
 * 此时，当提交对某一行的修改是，提交的还是参数名称还是 id ，但数值是 Entity 的 eventId 属性。
 * <p/>
 * jqgrid 根据返回的解析返回到前端的 json 字符串，json 字符串里面的参数要求，引号内为参数名.
 */
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

/**
 * jqgrid 提供 jsonReader 配置来说明设置此种对应关系，如参数 root 对于 json 中的 rows 变量、参数 page 对于 json 中的 page 变量 ...
 * -
 * 其中较为特殊的是参数 id , jqgrid 创建的表格，把此参数作为 the unique id of the row ，来确定每行数据。这对针对行数据的删除和编辑操作等，提供确定是哪一行是必须且关键的。
 * 这个 id 并不是 rownumbers : true  后，表格前面的序号，该序号是 jqgrid 显示用的，和 unique key 无关。
 * -
 * 1. 如果返回的 json 数据，集合 rows 中包含的对象有属性 id ，则 jqgrid 会自动设置创建的表格的 id 值为该对象的 id 的值。（此 id 一定不能为 null，必须有值，即返回的每个对象的属性 id 必须有值，否则表格显示异常。by 排查了两天，才找到原因!）
 * -
 * 2. 如果返回的 json 数据，集合 rows 中包含的对象没有属性 id ，则 jqgrid 根据返回的数据集合的先后顺序，自动生成一个序号，做为 表格的 id （上文提到的表格唯一 id）， 也就是 rownumbers : true  后，表格前面的序号中所示
 *    但此时自动生成的 id ，并不是 the unique id of the row 。那么如何确定 unique key 呢？
 *    “集合 rows 中包含的对象没有属性 id”，一般情况下，该对象是有其他属性（这是数据库设计范围的问题），作为了该对象的 unique id ，只是名字不叫 id 而已，那么修改 jsonReader 的默认配置，重新定义对应关键即可，如
 */
//    jsonReader:{
//          id:"eventId"  //该对象的 eventId 属性作为 jqgrid 的 id 参数。
//        },
/**
 *  此时，当提交对某一行的修改是，提交的还是参数名称还是 id ，但数值是 Entity 的 eventId 属性。
 *
 * jqgrid 根据返回的解析返回到前端的 json 字符串，json 字符串里面的参数要求，引号内为参数名.
 */

//如果返回的 json 的 rows 中对象属性包含上述名称时，不能为 null，否则 jqgrid 不能解析。也可以设置为其他的名字，避免冲突.

/**
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

public class JqgridResponse<T> {


    private static Logger logger = LoggerFactory.getLogger(JqgridResponse.class);
    /**
     * 当前页码
     */
    private int page;
    /**
     * Total pages
     * <p/>
     * 总页数
     */
    private int total;
    /**
     * Total number of records
     * <p/>
     * 记录总数
     */

    private int records;
    /**
     * Contains the actual data
     * <p/>
     * 待显示的记录集合
     * rows 中包含的 bean ，属性名字应该和前端的变量名称对应
     */
    private Iterable<T> rows;
    /**
     * Contains the actual data
     * <p/>
     * 自定义数据
     */
    private Map<String, Object> userdata;

/**
 * 必须以有参构造方法生成
 */
//    public JqgridResponseBean() {
//    }

    /**
     * 构造满足 jqgrid 要求的返回 json 数据
     *
     * @param pageSize         每页记录数
     * @param totalRecordsSize 总记录数
     * @param currentPageNo    当前页码(起始页数为 0)
     * @param allRecords       返回的记录集
     */
    public JqgridResponse(int pageSize, int totalRecordsSize, int currentPageNo,
                          Iterable<T> allRecords) {

        if (pageSize < 0 || totalRecordsSize < 0)

            try {
                throw new MyExceptionUtils("JqgridResponseBean 构造方法异常。");
            } catch (Exception e) {
                e.printStackTrace();
            }
        // org.springframework.data.domain.PageRequest 要求起始页 为 0 ，而 jqgrid 为 1，此处做 +1 处理
        this.page = currentPageNo + 1;
        this.records = totalRecordsSize;
        this.rows = allRecords;
        this.setTotal(pageSize);

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

    /**
     * 根据每页记录数和总记录数，自动计算总页数
     *
     * @return
     */
    public void setTotal(int pageSize) {


        if (records < 0) {
            total = -1;
            return;
        }

        long totalTemp = records / pageSize;
        if (records % pageSize > 0) {
            totalTemp++;
        }

        total = (int) totalTemp;

    }

    public int getRecords() {
        return records;
    }

    public void setRecords(int records) {
        this.records = records;
    }

    public Iterable<T> getRows() {
        return rows;
    }

    public void setRows(Iterable<T> rows) {
        this.rows = rows;
    }

    @Override
    public String toString() {
        return "JqgridResponse 当前页(page)=" + this.getPage() + ", (总页数)total=" + this.getTotal()
                + ", (总记录数)records=" + this.getRecords() + "]";
    }
}
