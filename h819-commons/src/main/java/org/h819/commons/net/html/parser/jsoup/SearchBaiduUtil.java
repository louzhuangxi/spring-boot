/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.h819.commons.net.html.parser.jsoup;

import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * @author JONE
 * @mail 858305351@qq.com
 * @time 2013-11-11
 * @description 通过Jsoup 获取百度搜索结果的基本信息
 */
public class SearchBaiduUtil {
    private static final Logger LOG = LoggerFactory.getLogger(SearchBaiduUtil.class);
    private Document document = null;
    private String url = "http://www.baidu.com/s";

    /**
     * @author JONE
     * @param name 需要查询的字段
     * @param page
     * @throws java.io.IOException 
     * @time 2013-11-11
     * @description 构造器
     */

    //百度搜索结果每页大小为10，pn参数代表的不是页数，而是返回结果的开始数
    //如获取第一页则pn=0，第二页则pn=10，第三页则pn=20，以此类推，抽象出模式：(page-1)*pageSize
    public SearchBaiduUtil(String name, int page) throws IOException{
        if(StringUtils.isEmpty(StringUtils.trim(name)) || 0 >= page){
            throw new NullPointerException();
         }
        this.document = Jsoup.connect(url).data("wd", name).data("pn", String.valueOf((page-1)*10)).get();
    }

}

