package com.base.spring.controller.jqgrid;

import com.base.spring.domain.TreeNodeEntity;
import com.base.spring.repository.TreeNodeRepository;
import com.google.common.collect.Lists;
import org.h819.web.jqgird.JqgridJPAUtils;
import org.h819.web.jqgird.JqgridResponse;
import org.h819.web.spring.jpa.DTOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("/grid/menu")
public class MenuAjaxController {

    private static final Logger logger = LoggerFactory.getLogger(MenuAjaxController.class);

    @Autowired
    private TreeNodeRepository menuRepository;



    /**
     * 查看菜单
     *
     * @param search        是否是搜索请求
     * @param filters       通过 jqgrid search 查询，多个查询条件时，包含查询条件为 json 格式数据。_search = false 时，jqgrid 传递过来的参数没有 filters , 此时 filters 的值为 null
     * @param currentPageNo 当前页码
     * @param pageSize      页面可显示行数
     * @param sortParameter 用于排序的列名 ，启用 groups 时，此项复杂，需要特殊解析
     * @param sort          排序的方式desc/asc
     * @return jqgrid 展示所需要的 json 结构，通过 spring 自动完成
     */
    @RequestMapping(value = "/jqgrid-search", produces = "application/json")
    //注意 value  /jqgrid-search  ，不能为 /jqgrid-search/ ，不能多加后面的斜线
    @ResponseBody
    public JqgridResponse jqgridSearch(
            @RequestParam("_search") Boolean search,
            @RequestParam(value = "filters", required = false) String filters,
            @RequestParam(value = "page", required = true) Integer currentPageNo,
            @RequestParam(value = "rows", required = true) Integer pageSize,
            @RequestParam(value = "sidx", required = true) String sortParameter,
            @RequestParam(value = "sord", required = true) String sort, RedirectAttributes redirectAttrs, HttpServletRequest request) {


        logger.info("search ={},page ={},rows ={},sord={},sidx={},filters={}", search, currentPageNo, pageSize, sort, sortParameter, filters);

        /**
         * 记录总数
         */
        int totalRecordsSize = JqgridJPAUtils.count(menuRepository, filters);
        if (totalRecordsSize == 0)
            return new JqgridResponse(pageSize, 0, 0, Lists.newArrayList()); //构造空数据集，返回 null ，有问题

        /**
         * 记录集
         */
        Page<TreeNodeEntity> list = JqgridJPAUtils.getJqgridResponse(menuRepository, currentPageNo, pageSize, sortParameter, sort, filters);

        /**
         * POJO to DTO
         * 转换原因见 service/package-info.java
         */

        DTOUtils dtoUtils = new DTOUtils();  //用法见 DTOUtils
        //    dtoUtils.addExcludes(MenuEntity.class, "parent"); //在整个转换过程中，无论哪个级联层次，只要遇到 TreeEntity 类，那么他的 parent 属性就不进行转换
        dtoUtils.addExcludes(TreeNodeEntity.class, "children");

        JqgridResponse<TreeNodeEntity> response = new JqgridResponse<TreeNodeEntity>(list.getSize(), totalRecordsSize, list.getNumber(), dtoUtils.createDTOcopy(list.getContent(), 1));
        return response;
    }



}
