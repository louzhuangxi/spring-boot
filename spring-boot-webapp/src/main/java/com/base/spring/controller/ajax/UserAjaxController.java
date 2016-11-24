package com.base.spring.controller.ajax;

import com.base.spring.domain.GroupEntity;
import com.base.spring.domain.RoleEntity;
import com.base.spring.domain.UserEntity;
import com.base.spring.repository.GroupRepository;
import com.base.spring.repository.RoleRepository;
import com.base.spring.repository.UserRepository;
import com.base.spring.service.GroupService;
import com.base.spring.service.UserService;
import com.base.spring.utils.BCryptPassWordUtils;
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
import org.springframework.ui.ModelMap;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.ModelAttribute;
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
@RequestMapping("/ajax/grid/user")
public class UserAjaxController {

    private static final Logger logger = LoggerFactory.getLogger(UserAjaxController.class);


    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserService userService;
    @Autowired
    private GroupRepository groupRepository;
    @Autowired
    private GroupService groupService;

    @Autowired
    private RoleRepository roleRepository;


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
        Page<UserEntity> pages = JpaUtils.getJqgridPage(userRepository, currentPageNo, pageSize, sortParameter, sort, filters);
        if (pages.getTotalElements() == 0)
            return new JqgridPage(pageSize, 0, 0, new ArrayList(0)); //构造空数据集，否则返回结果集 jqgird 解析会有问题


        /**
         * POJO to DTO
         * 转换原因见 service/package-info.java
         */

        DtoUtils dtoUtils = new DtoUtils();  //用法见 DTOUtils
        //    dtoUtils.addExcludes(MenuEntity.class, "parent"); //在整个转换过程中，无论哪个级联层次，只要遇到 TreeEntity 类，那么他的 parent 属性就不进行转换
        dtoUtils.addExcludes(RoleEntity.class, "treeNodes", "users", "groups");
        dtoUtils.addExcludes(GroupEntity.class, "users", "roles");


        JqgridPage<UserEntity> jqPage = new JqgridPage
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
            @RequestParam(value = "loginName", required = false) String loginName, // 删除时没有此变量
            @RequestParam(value = "password", required = false) String password,
            @RequestParam(value = "userName", required = false) String userName,  // 删除时没有此变量
            @RequestParam(value = "company", required = false) String company,
            @RequestParam(value = "telephone", required = false) String telephone,
            @RequestParam(value = "email", required = false) String email,
            @RequestParam(value = "valid", required = false) String valid,
            @RequestParam(value = "remark", required = false) String remark,
            RedirectAttributes redirectAttrs, Model model, HttpServletRequest request, HttpServletResponse response) {

        /**
         *    删除时，只有 oper 和 id 两个参数，所以其他参数都设置为 false
         */

        logger.info("oper ={},ids={},loginname={},password={},userName={},company={},telephone={},email={},valid={},remark={}",
                oper, ids, loginName, password, userName, company, telephone, email, valid, remark);


        //删除
        if (oper.equals("del")) {
            logger.info("del action.");
            //多选时，逐个处理
            for (String id : ids) {
                logger.info("id =" + id);
                userRepository.delete(Long.valueOf(id));
            }

            return; //删除后返回

        } else if (oper.equals("add")) {

            logger.info("add action.");
            Assert.hasText(loginName.trim(), "loginName must not be null!");
            Assert.hasText(password.trim(), "password must not be null!");
            Assert.hasText(userName.trim(), "userName must not be null!");
            Assert.hasText(email.trim(), "email must not be null!");
            UserEntity entity = new UserEntity();
            entity.setLoginName(loginName);
            entity.setPassword(BCryptPassWordUtils.encode(password));
            entity.setUserName(userName);
            entity.setCompany(company);
            entity.setTelephone(telephone);
            entity.setEmail(email);
            if (valid.equals("是"))
                entity.setValid(true);
            else if (valid.equals("否"))
                entity.setValid(false);
            else {
            }
            entity.setRemark(remark);
            userRepository.save(entity);
            // do add action

        } else if (oper.equals("edit")) {

            logger.info("edit action.");
            //多选时，逐个处理
            for (String id : ids) {
                logger.info("id =" + id);
                //必填项 。  不能放在方法参数中，用 required = true 限制，因为 del 操作无此参数
                Assert.hasText(loginName.trim(), "loginName must not be null!");
                Assert.hasText(password.trim(), "password must not be null!");
                Assert.hasText(userName.trim(), "userName must not be null!");
                Assert.hasText(email.trim(), "email must not be null!");
                UserEntity entity = userRepository.findOne(Long.valueOf(id));
                entity.setLoginName(loginName);
                entity.setPassword(BCryptPassWordUtils.encode(password));
                entity.setUserName(userName);
                entity.setCompany(company);
                entity.setTelephone(telephone);
                entity.setEmail(email);
                if (valid.equals("是"))
                    entity.setValid(true);
                else if (valid.equals("否"))
                    entity.setValid(false);
                else {
                }
                entity.setRemark(remark);
                userRepository.save(entity);
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
     * @param userId
     * @param redirectAttrs
     * @param model
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = "/bootstrap-modal-load-groups.html")
    //注意 value  /jqgrid-edit  ，不能为 /jqgrid-edit/ ，不能多加后面的斜线
    public String bootsTrapModalLoadGroups(
            @RequestParam(value = "user_id", required = true) String userId,
            RedirectAttributes redirectAttrs, @ModelAttribute("model") ModelMap model,//传递非字符串对象到前端，必须通过 @ModelAttribute("model") 对 model 强制赋值，并且是 ModelMap 类型
            HttpServletRequest request, HttpServletResponse response) {


        logger.info("user id={}", userId);

        Assert.notNull(userId, "userId is null");

        List<GroupEntity> groupsChecked = new ArrayList<>();
        List<GroupEntity> groupsUnChecked = new ArrayList<>();

        //获取所有用户
        List<GroupEntity> groupList = groupRepository.findAll();
        UserEntity userEntity = userRepository.findOne(Long.valueOf(userId.trim()));

        //分为两组 : 已经和 group 关联的/未关联的
        if (userEntity != null) {
            for (GroupEntity group : groupList) {
                if (group.getUsers().contains(userEntity))
                    groupsChecked.add(group);
                else groupsUnChecked.add(group);
            }
        } else
            groupsUnChecked.addAll(groupList);

        DtoUtils utils = new DtoUtils();
        utils.addExcludes(GroupEntity.class, "roles", "users");

//        MyJsonUtils.prettyPrint(utils.createDTOcopy(usersChecked));
//        System.out.println("=============");
//        MyJsonUtils.prettyPrint(utils.createDTOcopy(usersUnChecked));

        model.addAttribute("checkedbox", utils.createDTOcopy(groupsChecked));
        model.addAttribute("uncheckedbox", utils.createDTOcopy(groupsUnChecked));

        return "admin/ace/html/ajax/content/bootstrap-modal-load-groups";
    }


    /**
     * 关联被选中的 user 到 Group
     * 注意 spring mvc controller 接收 array 类型的参数
     *
     * @param userId
     * @param groupIds
     */
    @RequestMapping(value = "/bootstrap-modal-associate-groups.html")
    //注意 value  /jqgrid-edit  ，不能为 /jqgrid-edit/ ，不能多加后面的斜线
    @ResponseBody
    public void bootsTrapModalAssociateGroups(
            @RequestParam(value = "user_id", required = true) String userId,
            @RequestParam(value = "checkbox[]", required = false) String[] groupIds) {


        logger.info("user id ={}", userId);
        if (groupIds != null)
            logger.info("groupIds id ={}", Arrays.asList(groupIds));

        //发现，提交的 check box , chrome 浏览器会多了一个 on 参数，其他浏览器没有这个问题
        //去掉 ： ArrayUtils.removeElement(groupIds, "on")
        userService.associateGroups(ArrayUtils.removeElement(groupIds, "on"), userId);
    }


    /**
     * 返回 jquery.load 加载指定的页面 , user , group 都关联 roles , 公用一个显示界面 页面
     * <p>
     * 根据 group id ，获取所有 users ，传递到 bootstrap_modal
     *
     * @param userId
     * @param redirectAttrs
     * @param model
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = "/bootstrap-modal-load-roles.html")
    //注意 value  /jqgrid-edit  ，不能为 /jqgrid-edit/ ，不能多加后面的斜线
    public String bootsTrapModalLoadRoles(
            @RequestParam(value = "user_id", required = true) String userId,
            RedirectAttributes redirectAttrs, @ModelAttribute("model") ModelMap model,//传递非字符串对象到前端，必须通过 @ModelAttribute("model") 对 model 强制赋值，并且是 ModelMap 类型
            HttpServletRequest request, HttpServletResponse response) {


        logger.info("user id={}", userId);

        Assert.notNull(userId, "userId is null");

        List<RoleEntity> rolesChecked = new ArrayList<>();
        List<RoleEntity> rolesUnChecked = new ArrayList<>();

        //获取所有 role
        List<RoleEntity> roleEntityList = roleRepository.findAll();
        UserEntity userEntity = userRepository.findOne(Long.valueOf(userId.trim()));

        //分为两组 : 已经和 user 关联的/未关联的
        if (userEntity != null) {
            for (RoleEntity role : roleEntityList) {
                if (role.getUsers().contains(userEntity))
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
        return "admin/ace/html/ajax/content/bootstrap-modal-load-roles";
    }


    /**
     * 关联被选中的 roles 到 user
     * 注意 spring mvc controller 接收 array 类型的参数
     *
     * @param userId
     * @param roleIds
     */
    @RequestMapping(value = "/bootstrap-modal-associate-roles.html")
    //注意 value  /jqgrid-edit  ，不能为 /jqgrid-edit/ ，不能多加后面的斜线
    @ResponseBody
    public void bootsTrapModalAssociateRoles(
            @RequestParam(value = "user_id", required = true) String userId,
            @RequestParam(value = "checkbox[]", required = false) String[] roleIds) // 前端的参数为 checkbox , array 类型
    {

        logger.info("user id ={}", userId);
        if (roleIds != null)
            logger.info("role id ={}", Arrays.asList(roleIds));

        //发现，提交的 check box , chrome 浏览器会多了一个 on 参数，其他浏览器没有这个问题
        //去掉 ： ArrayUtils.removeElement(roleIds, "on")
        userService.associateRoles(ArrayUtils.removeElement(roleIds, "on"), userId);

    }
}
