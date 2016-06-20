package org.h819.web.jqgird;

import com.google.common.collect.Lists;
import org.apache.commons.lang3.StringUtils;
import org.h819.web.spring.jpa.JPADynamicSpecificationUtils;
import org.h819.web.spring.jpa.SearchFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.Repository;
import org.springframework.util.Assert;

import java.util.List;

/**
 * Jqgrid 工具类，只适用于 spring data jpa
 * <p>
 * 当 filters 参数为 null  时，可以用作非 jqgrid 情况下
 * <p>
 * <p>
 * 根据 jqgrid 传递过来的参数，通过 Repository 进行查询，封装了各项查询条件。
 */
public class JqgridJPAUtils {

    private static Logger logger = LoggerFactory.getLogger(JqgridJPAUtils.class);

    /**
     * 仅通过静态方法调用
     */
    private JqgridJPAUtils() {
    }


    /**
     * 根据 jqgrid filters 条件，进行数量查询。
     *
     * @param repository 查询器，必须是 extends JpaRepository<???, Long>, JpaSpecificationExecutor 类型的接口。
     * @param filters    jqgrid 的 filters 参数 ,  filters 为 null  时，可以用作非 jqgrid 情况下
     * @return
     */
    public static int count(Repository repository, String filters) {

        if (filters == null || filters.isEmpty()) {  //刷新表格时，filters.isEmpty() = true
            JpaRepository rep = (JpaRepository) repository;
            return (int) rep.count();
        } else {
            JpaSpecificationExecutor rep = (JpaSpecificationExecutor) repository;
            JqgridUtils.Filter f = JqgridUtils.getSearchFilters(filters);
            return (int) rep.count(JPADynamicSpecificationUtils.joinSearchFilter(f.getGroupRelation(), f.getSearchFilters()));
        }
    }

    /**
     * 根据 jqgrid filters 条件，和附加的其他查询信息，进行数量查询。
     *
     * @param repository          查询器，必须是 extends JpaRepository<???, Long>, JpaSpecificationExecutor 类型的接口。
     * @param filters             jqgrid 的 filters 参数  ,  filters 为 null  时，可以用作非 jqgrid 情况下
     * @param customSpecification 除了 jqgrid 传递过来的查询条件外，自己又附加查询条件，与 filters AND 关系的查询条件，specification 的构造符合 SearchFilter 写法，详见示例项目。
     *                            customSpecification 中指定的属性名称应该是待查询的 entity 中的属性名称，并且用改 entity 的 repository 进行查询
     * @return
     */
    public static int count(Repository repository, String filters, Specification customSpecification) {

        JpaSpecificationExecutor rep = (JpaSpecificationExecutor) repository;

        if (filters == null || filters.isEmpty()) {  //刷新 jqgrid 表格时，filters.isEmpty() = true
            return (int) rep.count(customSpecification);
        } else {
            JqgridUtils.Filter f = JqgridUtils.getSearchFilters(filters);
            //根据 jqgrid filters 参数，构造查询条件
            Specification specificationFilters = (JPADynamicSpecificationUtils.joinSearchFilter(f.getGroupRelation(), f.getSearchFilters()));
            return (int) rep.count(JPADynamicSpecificationUtils.joinSpecification(SearchFilter.Relation.AND, customSpecification, specificationFilters));
        }
    }

    /**
     * 根据 jqgrid 的 search 操作传递过来的条件(含 filters 条件)，进行分页查询。
     *
     * @param repository          查询器
     * @param currentPageNo       当前页，实际对应 jqgrid 传递过来的 page 参数，起始页为 0
     * @param pageSize            页面可显示行数
     * @param sortParameter       用于排序的列名 ，启用 groups 时，此项复杂，需要特殊解析
     * @param sortDirection       排序的方式，只能为  desc 或 asc
     * @param filters             通过 jqgrid search 按键查询，多个查询条件时，包含查询条件的 json 格式数据   ,  filters 为 null  时，可以用作非 jqgrid 情况下
     * @param customSpecification 除了 jqgrid 传递过来的查询条件外，自己又附加查询条件,与 filters AND 关系的查询条件，specification 的构造符合 SearchFilter 写法，详见示例项目。
     *                            customSpecification 中指定的属性名称应该是待查询的 entity 中的属性名称，并且用改 entity 的 repository 进行查询
     * @return
     */

    public static Page getResponse(Repository repository, int currentPageNo, int pageSize, String sortParameter, String sortDirection, String filters, Specification customSpecification) {

        JpaSpecificationExecutor rep = (JpaSpecificationExecutor) repository;


        //jpa 中起始页为 0，但传递过来的参数 currentPageNo 不能小于1
        Assert.isTrue(currentPageNo >= 1, "currentPageNo  需要 >= 1 ");
        currentPageNo = currentPageNo - 1;

        if (filters == null || filters.isEmpty()) {  //刷新表格时，filters.isEmpty() = true
            return rep.findAll(customSpecification, new PageRequest(currentPageNo, pageSize, JqgridJPAUtils.getSpringJPASort(sortParameter, sortDirection)));

        } else {

            JqgridUtils.Filter f = JqgridUtils.getSearchFilters(filters);
            //根据 jqgrid filters 参数，构造查询条件
            Specification specificationFilters = JPADynamicSpecificationUtils.joinSearchFilter(f.getGroupRelation(), f.getSearchFilters());
            return rep.findAll(
                    JPADynamicSpecificationUtils.joinSpecification(SearchFilter.Relation.AND, customSpecification, specificationFilters),
                    new PageRequest(currentPageNo, pageSize, JqgridJPAUtils.getSpringJPASort(sortParameter, sortDirection))
            );
        }
    }

    /**
     * 根据 jqgrid 的 search 操作传递过来的条件(含 filters 条件)，和附加的其他查询信息，进行分页查询。
     *
     * @param repository    查询器，必须是 extends JpaRepository<???, Long>, JpaSpecificationExecutor 类型的写法。
     * @param currentPageNo 当前页，实际对应 jqgrid 传递过来的 page 参数，起始页为 0
     * @param pageSize      页面可显示行数
     * @param sortParameter 用于排序的列名 ，启用 groups 时，此项复杂，需要特殊解析
     * @param sortDirection 排序的方式，只能为  desc 或 asc
     * @param filters       通过 jqgrid search 按键查询，多个查询条件时，包含查询条件的 json 格式数据  ,  filters 为 null  时，可以用作非 jqgrid 情况下
     * @return
     */
    public static Page getResponse(Repository repository, int currentPageNo, int pageSize, String sortParameter, String sortDirection, String filters) {

        //其实页为 0
        Assert.isTrue(currentPageNo >= 1, "currentPageNo  不能小于 1 ");
        currentPageNo = currentPageNo - 1;

        if (filters == null || filters.isEmpty()) {  //刷新表格时，filters.isEmpty() = true
            JpaRepository rep = (JpaRepository) repository;
            return rep.findAll(new PageRequest(currentPageNo, pageSize, JqgridJPAUtils.getSpringJPASort(sortParameter, sortDirection)));

        } else {

            JpaSpecificationExecutor rep = (JpaSpecificationExecutor) repository;
            JqgridUtils.Filter f = JqgridUtils.getSearchFilters(filters);
            //根据 jqgrid filters 参数，构造查询条件
            Specification spec = JPADynamicSpecificationUtils.joinSearchFilter(f.getGroupRelation(), f.getSearchFilters());

            return rep.findAll(spec, new PageRequest(currentPageNo, pageSize, JqgridJPAUtils.getSpringJPASort(sortParameter, sortDirection)));
        }
    }


    /**
     * 根据 jqgrid 传过来的排序信息，构造排序所需要的 Sort
     *
     * @param sortParameter 用于排序的列名, grouping:true 时格式特殊，需要正确解析
     * @param sortDirection 排序的方式，只能为  desc 或 asc
     * @return
     */
    private static Sort getSpringJPASort(String sortParameter, String sortDirection) {
        //排序字段
        if (!sortParameter.contains(",")) { //未分组

            Assert.isTrue(!sortParameter.isEmpty(), " 'jqgrid' 中 sortname 属性没有设置");
            Assert.state(sortDirection.equalsIgnoreCase("asc") || sortDirection.equalsIgnoreCase("desc"), " 'jqgrid' 中 sortorder 属性设置不对只能为 asc 或 desc (也适用于非 jqgrid 应用)");
            Sort.Direction direction;
            if (sortDirection.toLowerCase().equals("asc"))
                direction = Sort.Direction.ASC;
            else direction = Sort.Direction.DESC;

            return new Sort(direction, sortParameter);

        } else { //分组,grouping:true 时

            String[] strings = StringUtils.removeEnd(sortParameter.trim(), ",").split(",");  //传来的排序要求 sidx =name asc, herf desc,
            List<Sort.Order> orders = Lists.newArrayList();
            List<String> unique = Lists.newArrayList();
            for (String s : strings) { //拼接所有的排序请求。
                String sortField = StringUtils.substringBefore(s.trim(), " ");
                //为了避免同一个属性，重复添加。此情况发生在 grouping:true ，但  sortname 和 sortorder 参数没有注释掉。
                if (unique.contains(sortField))
                    continue;
                unique.add(sortField);
                String sortDirectionByGroup = StringUtils.substringAfter(s.trim(), " ");
                Assert.state(sortDirectionByGroup.equalsIgnoreCase("asc") || sortDirectionByGroup.equalsIgnoreCase("desc"), " 'jqgrid' 中 group 的属性 groupOrder 设置不对");
                Sort.Direction direction;
                if (sortDirection.toLowerCase().equals("asc"))
                    direction = Sort.Direction.ASC;
                else direction = Sort.Direction.DESC;

                orders.add(new Sort.Order(direction, sortField));
            }
            return new Sort(orders);
        }
    }

}