/**
 * ****************************************************************************
 * Copyright (c) 2005, 2014 springside.github.io
 * <p/>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * <p/>
 * 参考 springside 4 项目，改动并做了扩展
 * *****************************************************************************
 */
package org.h819.web.spring.jpa;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.util.Assert;


/**
 * Description : TODO(创建分页请求)
 * User: h819
 * Date: 2015-5-3
 * Time: 下午9:20
 * To change this template use File | Settings | File Templates.
 */
public class JPAPageRequestUtils {

    private static Logger logger = LoggerFactory.getLogger(JPAPageRequestUtils.class);


    /**
     * 创建分页请求.
     *
     * @param currentPageNo  当前页码，第一页为 0
     * @param pageSize       每页记录数
     * @param sortParameter 排序字段
     * @param direction      排序关键字，应为 DESC、ASC，可以为 null ，此时按默认排序，即 DESC
     * @return
     */
    public static PageRequest createPageRequest(int currentPageNo, int pageSize, String sortParameter, Sort.Direction direction) {

        Assert.hasText(sortParameter, "fieldName must not be null or empty!");
        return new PageRequest(currentPageNo, pageSize, new Sort(direction, sortParameter));
    }

    /**
     * 创建分页请求，默认的排序.
     *
     * @param currentPageNo 当前页码，第一页为 0
     * @param pageSize      每页记录数
     * @return
     */
    public static PageRequest createPageRequest(int currentPageNo, int pageSize) {
        return new PageRequest(currentPageNo, pageSize);
    }

    /**
     * 创建分页请求，默认的排序.
     *
     * @param currentPageNo 当前页码，第一页为 0
     * @param pageSize      每页记录数
     * @param sort          排序
     * @return
     */
    public static PageRequest createPageRequest(int currentPageNo, int pageSize, Sort sort) {
        return new PageRequest(currentPageNo, pageSize, sort);
    }

    private void test(){
       // JPAPageRequestUtils.createPageRequest(1,10,"","s") ;
    }
}
