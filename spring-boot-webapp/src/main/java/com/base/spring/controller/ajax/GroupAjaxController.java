package com.base.spring.controller.ajax;

/**
 * 构造了两个 modal
 */

import com.base.spring.domain.GroupEntity;
import com.base.spring.domain.RoleEntity;
import com.base.spring.domain.UserEntity;
import com.base.spring.repository.GroupRepository;
import com.base.spring.repository.RoleRepository;
import com.base.spring.repository.UserRepository;
import com.base.spring.service.GroupService;
import org.apache.commons.lang3.ArrayUtils;
import org.h819.web.jqgird.JqgridPage;
import org.h819.web.spring.jpa.DtoUtils;
import org.h819.web.spring.jpa.JpaUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
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
import java.util.Arrays;
import java.util.List;

@Controller
@RequestMapping("/ajax/grid/group")
public class GroupAjaxController {

    private static final Logger logger = LoggerFactory.getLogger(GroupAjaxController.class);


    @Autowired
    private GroupRepository groupRepository;
    @Autowired
    private GroupService groupService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;


    /**
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
        Page<GroupEntity> pages = JpaUtils.getJqgridPage(groupRepository, currentPageNo, pageSize, sortParameter, sort, filters);
        if (pages.getTotalElements() == 0)
            return new JqgridPage(pageSize, 0, 0, new ArrayList(0)); //构造空数据集，否则返回结果集 jqgird 解析会有问题


        /**
         * POJO to DTO
         * 转换原因见 service/package-info.java
         */

        DtoUtils dtoUtils = new DtoUtils();  //用法见 DTOUtils
        //    dtoUtils.addExcludes(MenuEntity.class, "parent"); //在整个转换过程中，无论哪个级联层次，只要遇到 TreeEntity 类，那么他的 parent 属性就不进行转换
        dtoUtils.addExcludes(GroupEntity.class, "users", "roles");

        JqgridPage<GroupEntity> jqPage = new JqgridPage
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
     */
    @Transactional(readOnly = false)
    @RequestMapping(value = "/jqgrid-edit")
    //注意 value  /jqgrid-edit  ，不能为 /jqgrid-edit/ ，不能多加后面的斜线
    public void jqgridCURD(
            @RequestParam(value = "oper", required = true) String oper, // jqgrid 传递过来的 edit 标记
            @RequestParam(value = "id", required = true) String[] ids, //多选时，如果为单选，数组只有一个值
            @RequestParam(value = "name", required = false) String name, // 删除时没有此变量
            @RequestParam(value = "remark", required = false) String remark,
            RedirectAttributes redirectAttrs, Model model, HttpServletRequest request, HttpServletResponse response) {

        /**
         *    删除时，只有 oper 和 id 两个参数，所以其他参数都设置为 false
         */

        logger.info("oper ={},ids={},name={},remark={}", oper, ids, name, remark);


        //删除
        if (oper.equals("del")) {
            logger.info("del action.");
            //多选时，逐个处理
            for (String id : ids) {
                logger.info("id =" + id);
                groupRepository.delete(Long.valueOf(id));
            }

            return; //删除后返回

        } else if (oper.equals("add")) {

            logger.info("add action.");
            Assert.hasText(name.trim(), "name must not be null!");
            GroupEntity entity = new GroupEntity();
            entity.setName(name);
            entity.setRemark(remark);
            groupRepository.save(entity);
            // do add action

        } else if (oper.equals("edit")) {

            logger.info("edit action.");
            //多选时，逐个处理
            for (String id : ids) {
                logger.info("id =" + id);
                //必填项 。  不能放在方法参数中，用 required = true 限制，因为 del 操作无此参数
                Assert.hasText(name.trim(), "name must not be null!");
                GroupEntity entity = groupRepository.findOne(Long.valueOf(id));
                entity.setName(name);
                entity.setRemark(remark);
                groupRepository.save(entity);
            }
        } else {
            //do none. 没有其他的 oper 参数值了
        }

        return;
    }

    /**
     * 返回 jquery.load 加载指定的页面
     * <p>
     * 根据 group id ，获取所有 users ，传递到 bootstrap_modal
     *
     * @param groupId
     * @param redirectAttrs
     * @param model
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = "/bootstrap-modal-load-users.html")
    //注意 value  /jqgrid-edit  ，不能为 /jqgrid-edit/ ，不能多加后面的斜线
    public String bootsTrapModalLoadUsers(
            @RequestParam(value = "group_id", required = true) String groupId,
            RedirectAttributes redirectAttrs, Model model, HttpServletRequest request, HttpServletResponse response) {

        logger.info("groupId={}", groupId);

        Assert.notNull(groupId, "groupId is null");

        List<UserEntity> usersChecked = new ArrayList<>();
        List<UserEntity> usersUnChecked = new ArrayList<>();

        //获取所有用户
        List<UserEntity> userEntityList = userRepository.findAll();
        GroupEntity groupEntity = groupRepository.findOne(Long.valueOf(groupId.trim()));

        //分为两组 : 已经和 group 关联的/未关联的
        if (groupEntity != null) {
            for (UserEntity user : userEntityList) {
                if (user.getGroups().contains(groupEntity))
                    usersChecked.add(user);
                else usersUnChecked.add(user);
            }
        } else
            usersUnChecked.addAll(userEntityList);

        DtoUtils utils = new DtoUtils();
        utils.addExcludes(UserEntity.class, "roles");
        utils.addExcludes(GroupEntity.class, "roles", "users");

//        MyJsonUtils.prettyPrint(utils.createDTOcopy(usersChecked));
//        System.out.println("=============");
//        MyJsonUtils.prettyPrint(utils.createDTOcopy(usersUnChecked));

        model.addAttribute("checkedbox", utils.createDTOcopy(usersChecked));
        model.addAttribute("uncheckedbox", utils.createDTOcopy(usersUnChecked));

        return "admin/ace/html/ajax/content/bootstrap-modal-load-users";
    }


    /**
     * 关联被选中的 user 到 Group
     * 注意 spring mvc controller 接收 array 类型的参数
     *
     * @param groupId
     * @param userIds
     */
    @RequestMapping(value = "/bootstrap-modal-associate-users.html")
    //注意 value  /jqgrid-edit  ，不能为 /jqgrid-edit/ ，不能多加后面的斜线
    @ResponseBody
    public void bootsTrapModalAssociateUsers(
            @RequestParam(value = "group_id", required = true) String groupId,
            @RequestParam(value = "checkbox[]", required = false) String[] userIds) {


        logger.info("group id ={}", groupId);
        if (userIds != null)
            logger.info("user id ={}", Arrays.asList(userIds));
        //发现，提交的 check box , chrome 浏览器会多了一个 on 参数，其他浏览器没有这个问题
        //去掉 ： ArrayUtils.removeElement(userIds, "on")
        groupService.associateUsers(ArrayUtils.removeElement(userIds, "on"), groupId);
    }

    /**
     * 返回 jquery.load 加载指定的页面
     * <p>
     * 根据 group id ，获取所有 users ，传递到 bootstrap_modal
     *
     * @param groupId
     * @param redirectAttrs
     * @param model
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = "/bootstrap-modal-load-roles.html")
    //注意 value  /jqgrid-edit  ，不能为 /jqgrid-edit/ ，不能多加后面的斜线
    public String bootsTrapModalLoadRoles(
            @RequestParam(value = "group_id", required = true) String groupId,
            RedirectAttributes redirectAttrs, Model model,HttpServletRequest request, HttpServletResponse response) {

        /**
         *    删除时，只有 oper 和 id 两个参数，所以其他参数都设置为 false
         */

        logger.info("ids={}", groupId);

        Assert.notNull(groupId, "groupId is null");

        List<RoleEntity> rolesChecked = new ArrayList<>();
        List<RoleEntity> rolesUnChecked = new ArrayList<>();

        //获取所有角色
        List<RoleEntity> roleEntityList = roleRepository.findAll();
        GroupEntity groupEntity = groupRepository.findOne(Long.valueOf(groupId.trim()));

        //分为两组 : 已经和 group 关联的/未关联的
        if (groupEntity != null) {
            for (RoleEntity role : roleEntityList) {
                if (role.getGroups().contains(groupEntity))
                    rolesChecked.add(role);
                else rolesUnChecked.add(role);
            }
        } else
            rolesUnChecked.addAll(roleEntityList);

        DtoUtils utils = new DtoUtils();
        utils.addExcludes(RoleEntity.class, "roles", "users", "groups");

//        MyJsonUtils.prettyPrint(utils.createDTOcopy(usersChecked));
//        System.out.println("=============");
//        MyJsonUtils.prettyPrint(utils.createDTOcopy(usersUnChecked));

        model.addAttribute("checkedbox", utils.createDTOcopy(rolesChecked));
        model.addAttribute("uncheckedbox", utils.createDTOcopy(rolesUnChecked));

        //和 jqgrid-user get roles 用一个页面
        return "admin/ace/html/ajax/content/bootstrap-modal-load-roles";
    }


    /**
     * 关联被选中的 roles 到 Group
     * 注意 spring mvc controller 接收 array 类型的参数
     *
     * @param groupId
     * @param roleIds
     */
    @RequestMapping(value = "/bootstrap-modal-associate-roles.html")
    //注意 value  /jqgrid-edit  ，不能为 /jqgrid-edit/ ，不能多加后面的斜线
    @ResponseBody
    public void bootsTrapModalAssociateRoles(
            @RequestParam(value = "group_id", required = true) String groupId,
            @RequestParam(value = "checkbox[]", required = false) String[] roleIds) // 前端的参数为 checkbox , array 类型
    {

        logger.info("group id ={}", groupId);
        if (roleIds != null)
            logger.info("role id ={}", Arrays.asList(roleIds));

        //发现，提交的 check box , chrome 浏览器会多了一个 on 参数，其他浏览器没有这个问题
        //去掉 ： ArrayUtils.removeElement(roleIds, "on")
        groupService.associateRoles(ArrayUtils.removeElement(roleIds, "on"), groupId);
    }
}
