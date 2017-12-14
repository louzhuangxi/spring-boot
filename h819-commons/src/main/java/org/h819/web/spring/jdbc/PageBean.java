package org.h819.web.spring.jdbc;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


/**
 * 分页数据，有这四项，就可以组成分页列表
 *
 * @param <T>
 */
public class PageBean<T> implements Serializable {

    /**
     * 当前页码
     */
    private int currentPageNo;


    /**
     * Total pages
     * 总页数
     */
    private int totalPages;


    /**
     * Total number of records
     * <p>
     * 记录总数
     */

    private int totalRecords;


    /**
     * Contains the actual data
     * <p>
     * 待显示的记录集合
     * content 中包含的 bean ，属性名字应该和前端的变量名称对应
     * ==== 需要注意：
     * 在 String to PageBean 时(反序列化)：
     * String 中的 bean 集合名字是 content ，并不能直接转换为 T 类型，所有需要再次把 String 中的 content 内容转换为 Bean，形如
     * PageBean vo = JSON.parseObject(jsonString, PageBean.class);
     * List<StStandardEntity> list = MyBeanUtils.mapToBeans(vo.getContent(), StStandardEntity.class);
     * 除非指定 T 为具体的类的类型，就可以直接转换了，不需要转换为 map
     */
    private List<T> content = new ArrayList<T>();


    /**
     * Contains the actual data
     * <p>
     * 自定义数据
     */
    private Map<String, Object> userdata;

    /**
     * 必须以有参构造方法生成，强制输入相关参数，避免输入错误
     */
    private PageBean() {
    }

    /**
     * 构造满足页数据，前端有了这几个数据之后，就可以显示了分页信息了
     *
     * @param pageSize      每页记录数
     * @param currentPageNo 当前页码(此处规定起始页为 1)
     * @param totalRecords  记录总数
     * @param content       返回的每页记录集，最后一页的大小可能不等于 pageSize
     */
    public PageBean(int pageSize, int currentPageNo, int totalRecords, List<T> content) {

        if (currentPageNo < 1)
            throw new IllegalArgumentException("currentPageNo : 起始页不应小于 1 ，且从 1 开始。");

        if (pageSize < 0)
            throw new IllegalArgumentException("pageSize : 页大小不能小于 0");

        // org.springframework.data.domain.PageRequest 要求起始页 为 0 ，而 jqgrid 为 1
        this.currentPageNo = currentPageNo;
        this.totalRecords = totalRecords;
        this.content = content;

        /**
         * 根据每页记录数和总记录数，自动计算总页数
         * 不要放在 setTotalPages 方法中，否则 String -> PageBean 反序列化时，会出现错误。
         * 反序列化需要默认的 get set 方法
         */
        if (totalRecords < 0) {
            totalPages = -1;
            return;
        }

        int totalTemp = totalRecords / pageSize;
        if (totalRecords % pageSize > 0) {
            totalTemp++;
        }
        totalPages = totalTemp;
    }

    public int getCurrentPageNo() {
        return currentPageNo;
    }

    public void setCurrentPageNo(int currentPageNo) {
        this.currentPageNo = currentPageNo;
    }

    public int getTotalPages() {
        return totalPages;
    }


    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }

    public int getTotalRecords() {
        return totalRecords;
    }

    public void setTotalRecords(int totalRecords) {
        this.totalRecords = totalRecords;
    }

    public List<T> getContent() {
        return content;
    }

    public void setContent(List<T> content) {
        this.content = content;
    }
}
