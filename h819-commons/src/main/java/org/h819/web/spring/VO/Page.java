package org.h819.web.spring.vo;

import org.h819.commons.MyExceptionUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


/**
 * 分页数据，有这四项，既可以组成分页列表
 *
 * @param <T>
 */
public class Page<T> implements Serializable {

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
    private List<T> content = new ArrayList<>();


    /**
     * Contains the actual data
     * <p>
     * 自定义数据
     */
    private Map<String, Object> userdata;

    /**
     * 必须以有参构造方法生成
     */
    private Page() {
    }

    /**
     * 构造满足 jqgrid 要求的返回 json 数据
     *
     * @param pageSize      每页记录数
     * @param currentPageNo 当前页码(起始页数为 0)
     * @param content       返回的记录集
     */
    public Page(int pageSize,
                int currentPageNo,
                List<T> content) {

        if (pageSize < 0 || totalRecords < 0)

            try {
                throw new MyExceptionUtils("JqgridResponseBean 构造方法异常。");
            } catch (Exception e) {
                e.printStackTrace();
            }
        // org.springframework.data.domain.PageRequest 要求起始页 为 0 ，而 jqgrid 为 1，此处做 +1 处理
        this.currentPageNo = currentPageNo + 1;
        this.totalRecords = content.size();
        this.content = content;
        this.setTotalPages(pageSize);

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

    /**
     * 根据每页记录数和总记录数，自动计算总页数
     *
     * @return
     */
    public void setTotalPages(int pageSize) {


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
