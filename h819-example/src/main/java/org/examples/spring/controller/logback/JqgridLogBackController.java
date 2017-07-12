package org.examples.spring.controller.logback;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.google.common.collect.Lists;
import org.examples.spring.repository.logback.LoggingEventEntityRepository;
import org.examples.spring.service.logback.LogbackService;
import org.h819.web.spring.jpa.JpaUtils;
import org.h819.web.jqgird.JqgridPage;
import org.h819.web.spring.jpa.DtoUtils;
import org.h819.web.spring.jpa.JpaDynamicSpecificationUtils;
import org.h819.web.spring.jpa.SearchFilter;
import org.h819.web.spring.jpa.entitybase.logback.LoggingEventEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;


/**
 * Description : TODO(演示动态查询实现方法,表格用 jagrid)
 * User: h819
 * Date: 14-1-17
 * Time: 下午4:47
 * To change this template use File | Settings | File Templates.
 */
@Controller
@RequestMapping("/grid/sys/task")
public class JqgridLogBackController {

    private static final Logger logger = LoggerFactory.getLogger(JqgridLogBackController.class);

    @Autowired
    LoggingEventEntityRepository loggingEventEntityRepository;
    @Autowired
    LogbackService logbackService;



    /**
     * jqgrid json 数据查询，为 url 提供数据
     * <p/>
     * 请求参数名称固定，为 jqgrid 默认
     *
     * @param search  是否是搜索请求
     * @param filters 多个查询条件时，包含查询条件的 json 格式数据
     * @param page    当前页码
     * @param rows    页面可显示行数
     * @param sidx    用于排序的列名
     * @param sord    排序的方式desc/asc
     * @return jqgrid 展示所需要的 数据结构
     */
    @RequestMapping(value = "/search", produces = "application/json")
    @ResponseBody
    public String jqgridWebSiteSearchJsonData(
            @RequestParam("_search") Boolean search,
            @RequestParam(value = "filters", required = false) String filters,
            @RequestParam(value = "page", required = false) Integer page,
            @RequestParam(value = "rows", required = false) Integer rows,
            @RequestParam(value = "sidx", required = false) String sidx,
            @RequestParam(value = "sord", required = false) String sord, HttpServletRequest request) {


        logger.info("search =" + search + " " + "page =" + page + " " +
                "rows =" + rows + " " + "sord =" + sord + " " + "sidx =" + sidx + " " + "filters =" + filters);
        /**
         * 准备 JqgridResponseBean 所需参数
         */


        Specification specification = JpaDynamicSpecificationUtils.bySearchFilter(
                new SearchFilter("levelString", SearchFilter.Operator.EQ, "INFO"));

        DtoUtils dtoUtils = new DtoUtils();  //用法见 DTOUtils

        //记录总数
        Page<LoggingEventEntity> list = JpaUtils.getJqgridPage(loggingEventEntityRepository, page, rows, sidx, sord, filters, specification);

        if (list.getTotalElements() == 0)
            return JSON.toJSONString(new JqgridPage(rows, 0, 0, Lists.newArrayList())); //构造空数据集，返回 null ，有问题


        JqgridPage<LoggingEventEntity> response = new JqgridPage
                (list.getSize(), list.getNumber(), (int) list.getTotalElements(), dtoUtils.createDTOcopy(list.getContent()));

        return JSON.toJSONString(response, SerializerFeature.DisableCircularReferenceDetect);
    }


    /**
     * 编辑，参数和具体的业务相关，无法抽象处理
     *
     * @param oper 编辑类型(add,edit,de)
     * @param ids
     */

    @RequestMapping(value = "/edit", produces = "application/json")
    public String jqgridCURD(
            @RequestParam(value = "oper", required = true) String oper,
            @RequestParam(value = "id", required = true) String[] ids,
            @RequestParam(value = "name", required = false) String name,
            @RequestParam(value = "herf", required = false) String herf,
            RedirectAttributes redirectAttributes) {

       // logger.info("action : " + oper);

        //删除时，只有 oper 和 id 两个参数，所以其他参数都设置为 false
        //删除
        if (oper.equals("del")) {
            logger.info("del action.");
            for (String id : ids) {
                logger.info("id =" + id);
                logbackService.deleteLoggingEnvent(Long.valueOf(id));
            }

        }

        return null;// 返回 null ，也可以返回当前页
    }
}