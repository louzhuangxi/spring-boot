package org.h819.web.spring.vo;

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
     * rows 中包含的 bean ，属性名字应该和前端的变量名称对应
     */
    private List<T> content = new ArrayList();


    /**
     * Contains the actual data
     * <p>
     * 自定义数据
     */
    private Map<String, Object> userdata;

    /**
     * 必须以有参构造方法生成
     */
    private PageBean() {
    }

    /**
     * 构造满足 jqgrid 要求的返回 json 数据
     *
     * @param pageSize      每页记录数
     * @param currentPageNo 当前页码(起始页数为 0)
     * @param totalRecords  记录总数
     * @param content       返回的每页记录集，最后一页的大小可能不等于 pageSize
     */
    public PageBean(int pageSize, int currentPageNo, int totalRecords, List<T> content) {

        if (pageSize < 0)
            throw new IllegalArgumentException("页大小不能小于 0");

        // org.springframework.data.domain.PageRequest 要求起始页 为 0 ，而 jqgrid 为 1，此处做 +1 处理
        this.currentPageNo = currentPageNo + 1;
        this.totalRecords = totalRecords;
        this.content = content;

        /**
         * 根据每页记录数和总记录数，自动计算总页数
         * 不要放在 setTotalPages 方法中，否则 Stirng -> PageBean 反序列化时，会出现错误。
         * 反序列化需要默认的 get set 方法
         *
         * @return
         */
        if (totalRecords < 0) {
            totalPages = -1;
            return;
        }

        long totalTemp = totalRecords / pageSize;
        if (totalRecords % pageSize > 0) {
            totalTemp++;
        }
        totalPages = (int) totalTemp;


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
