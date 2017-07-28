package com.base.spring.controller.ajax;

import com.base.spring.domain.RoleEntity;
import com.base.spring.repository.RoleRepository;
import com.base.spring.service.RoleService;
import org.h819.web.spring.jpa.DtoUtils;
import org.h819.web.jqgird.JqgridPage;
import org.h819.web.spring.jpa.JpaUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;

@Controller
@RequestMapping("/ajax/grid/role")
public class RoleAjaxController {

    private static final Logger logger = LoggerFactory.getLogger(RoleAjaxController.class);


    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private RoleService roleService;


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
    @RequestMapping(value = "/jqgrid-search", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    //注意 value  /jqgrid-search  ，不能为 /jqgrid-search/ ，不能多加后面的斜线
    @ResponseBody
    public JqgridPage jqgridSearch(
            @RequestParam("_search") Boolean search,
            @RequestParam(value = "filters", required = false) String filters,
            @RequestParam(value = "page", required = true) Integer currentPageNo,
            @RequestParam(value = "rows", required = true) Integer pageSize,
            @RequestParam(value = "sidx", required = true) String sortParameter,
            @RequestParam(value = "sord", required = true) String sort, RedirectAttributes redirectAttrs, HttpServletRequest request) {


        logger.info("search ={},page ={},rows ={},sord={},sidx={},filters={}", search, currentPageNo, pageSize, sort, sortParameter, filters);

        /**
         * 记录集
         */
        Page<RoleEntity> pages = JpaUtils.getJqgridPage(roleRepository, currentPageNo, pageSize, sortParameter, sort, filters);
        if (pages.getTotalElements() == 0)
            return new JqgridPage(pageSize, 0, 0, new ArrayList()); //构造空数据集，否则返回结果集 jqgird 解析会有问题


        /**
         * POJO to DTO
         * 转换原因见 service/package-info.java
         */

        DtoUtils dtoUtils = new DtoUtils();  //用法见 DTOUtils
        //    dtoUtils.addExcludes(MenuEntity.class, "parent"); //在整个转换过程中，无论哪个级联层次，只要遇到 TreeEntity 类，那么他的 parent 属性就不进行转换
        dtoUtils.addExcludes(RoleEntity.class, "treeNodes", "users", "groups");


        JqgridPage<RoleEntity> jqPage = new JqgridPage
                (pages.getSize(), pages.getNumber(), (int) pages.getTotalElements(), dtoUtils.createDTOcopy(pages.getContent()));

        return jqPage;
    }

    /**
     * jqgrid 编辑，参数和具体的业务相关，无法抽象处理
     * -
     * jqgrid 通过 ajax 方式，发送了一个异步请求（仅是向服务器发送了一些参数，让服务器做一些后台操作），并不需要返回值，所以返回什么都可以，发送页面并不需要变化。
     * -
     * ajax 是异步发送的，jqgrid 发送请求的页面并不发生变化(如果后台操作，使前端数据发生了变化，重新导数据库查询，此时如果需要刷新页面重新发送查询请求，用 jqgrid 函数 jQuery(grid_selector).trigger("reloadGrid") )。
     * -
     * 如果需要跳转到其他页面，再加上返回值
     *
     * @param oper 编辑类型(add,edit,del)
     * @param ids  选取的行的 id
     * @param name
     */
    @Transactional(readOnly = false)
    @RequestMapping(value = "/jqgrid-edit")
    //注意 value  /jqgrid-edit  ，不能为 /jqgrid-edit/ ，不能多加后面的斜线
    public void jqgridCURD(
            @RequestParam(value = "oper", required = true) String oper, // jqgrid 传递过来的 edit 标记
            @RequestParam(value = "id", required = true) String[] ids, //多选时，如果为单选，数组只有一个值
            @RequestParam(value = "name", required = false) String name, RedirectAttributes redirectAttrs, Model model, HttpServletRequest request, HttpServletResponse response) {

        /**
         *    删除时，只有 oper 和 id 两个参数，所以其他参数都设置为 false
         */

        logger.info("oper ={},ids={},name={}", oper, ids, name);


        //删除
        if (oper.equals("del")) {
            logger.info("del action.");
            //多选时，逐个处理
            for (String id : ids) {
                logger.info("id =" + id);
                roleRepository.delete(Long.valueOf(id));
            }

            return; //删除后返回

        } else if (oper.equals("add")) {

            logger.info("add action.");
            Assert.hasText(name.trim(), "namecn must not be null!");
            RoleEntity entity = new RoleEntity(name);
            roleRepository.save(entity);
            // do add action

        } else if (oper.equals("edit")) {

            logger.info("edit action.");
            //多选时，逐个处理
            for (String id : ids) {
                logger.info("id =" + id);
                //必填项 。  不能放在方法参数中，用 required = true 限制，因为 del 操作无此参数
                Assert.hasText(name.trim(), "namecn must not be null!");

                RoleEntity entity = roleRepository.findOne(Long.valueOf(id));
                entity.setName(name);
                roleRepository.save(entity);
            }
        } else {
            //do none. 没有其他的 oper 参数值了
        }

        return;
    }

    @RequestMapping(value = "/get_checked_nodes.html")
    //注意 value  /jqgrid-edit  ，不能为 /jqgrid-edit/ ，不能多加后面的斜线
    @ResponseBody
    public void getCheckedNodes(@RequestParam(value = "ids_str", required = true) String ids,
                                @RequestParam(value = "role_id", required = true) String roleId,
                                RedirectAttributes redirectAttrs, Model model, HttpServletRequest request, HttpServletResponse response) {

        /**
         *    删除时，只有 oper 和 id 两个参数，所以其他参数都设置为 false
         */

        logger.info("ids={} , roleId={}", ids, roleId);
//
//        if (ids == null)
//            System.out.println("null");
//
//        if (ids.isEmpty())
//            System.out.println("empty");
//
//        if (ids.equals(""))
//            System.out.println("equals");

         roleService.associate(ids, roleId);
        return;
    }
}
