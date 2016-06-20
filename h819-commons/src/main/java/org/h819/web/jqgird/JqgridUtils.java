package org.h819.web.jqgird;

import com.alibaba.fastjson.JSON;
import org.h819.web.spring.jpa.SearchFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Jqgrid 工具类，可以在非 Spring 环境下运行
 * <p>
 * 根据 jqgrid 传递过来的参数，构造查询条件
 * 也可以用在类似 jqgrid 查询条件的应用中，此时需要自行构造  JqgridFilter
 * <p>
 * 使用 ： JqgridUtils.Filter f = JqgridUtils.getSearchFilters(filters);
 */
public class JqgridUtils {

    private static Logger logger = LoggerFactory.getLogger(JqgridUtils.class);

    /**
     * 仅通过静态方法调用
     */
    private JqgridUtils() {
    }


    /**
     * 由 Jqgrid 的请求参数 filters ，构造查询条件的集合。
     *
     * @param filters Jqgrid 的请求参数 filters ，格式固定为 json 格式，可以构造为 JqgridFilterBean 对象。
     * @return 构造完成后的查询条件
     */
    public static Filter getSearchFilters(String filters) {

        //如果是查询操作，有条件约束，重新准备 JqgridResponseBean
        //组装查询条件 filter
        //将查询字符串(json 格式)，包装成对象
        JqgridFilter jqgridFilterBean = JSON.parseObject(filters, JqgridFilter.class);
        Collection<SearchFilter> searchFilters = new ArrayList<SearchFilter>();
        //
        logger.info("查询条件之间的关系：" + jqgridFilterBean.getGroupOp());
        //组装 searchParams
        for (JqgridFilter.Rule rule : jqgridFilterBean.getRules()) {
            logger.info(rule.getField() + " - " + rule.getOp() + " - " + rule.getData());
            //要求 jqgrid 传递的操作符，应该和 SearchFilter.Operator 中的含义一致
            searchFilters.add(new SearchFilter(rule.getField(), SearchFilter.Operator.valueOf(rule.getOp()), rule.getData()));
        }
        // 转换 Relation
        SearchFilter.Relation filterRelation;
        if (jqgridFilterBean.getGroupOp().toUpperCase().equals("AND"))
            filterRelation = SearchFilter.Relation.AND;
        else filterRelation = SearchFilter.Relation.OR;

        return new Filter(searchFilters, filterRelation);
    }

//    /**
//     * 根据自行构造的 filters ，构造查询条件的集合。
//     *
//     * @param filters 为自行构造的 JqgridFilter
//     * @return
//     */
//    public static Filter getSearchFilters(JqgridFilter filters) {
//
//        //如果是查询操作，有条件约束，重新准备 JqgridResponseBean
//        //组装查询条件 filter
//        //将查询字符串(json 格式)，包装成对象
//
//        Collection<SearchFilter> searchFilters = new ArrayList<SearchFilter>();
//        //
//        logger.info("查询条件之间的关系：" + filters.getGroupOp());
//        //组装 searchParams
//        for (JqgridFilter.Rule rule : filters.getRules()) {
//            logger.info(rule.getField() + " - " + rule.getOp() + " - " + rule.getData());
//            //要求 jqgrid 传递的操作符，应该和 SearchFilter.Operator 中的含义一致
//            searchFilters.add(new SearchFilter(rule.getField(), SearchFilter.Operator.valueOf(rule.getOp()), rule.getData()));
//        }
//        // 转换 Relation
//        SearchFilter.Relation filterRelation;
//        if (filters.getGroupOp().toUpperCase().equals("AND"))
//            filterRelation = SearchFilter.Relation.AND;
//        else filterRelation = SearchFilter.Relation.OR;
//
//        return new Filter(searchFilters, filterRelation);
//    }

    /**
     * Inner class ，bean 格式，用于传递查询条件。
     */
    public static class Filter {
        private SearchFilter.Relation groupRelation;
        private Collection<SearchFilter> searchFilters;

        private Filter() {
        }

        public Filter(Collection<SearchFilter> searchFilters, SearchFilter.Relation groupRelation) {
            super();
            this.searchFilters = searchFilters;
            this.groupRelation = groupRelation;
        }

        public SearchFilter.Relation getGroupRelation() {
            return groupRelation;
        }

        public Collection<SearchFilter> getSearchFilters() {
            return searchFilters;
        }
    }

}