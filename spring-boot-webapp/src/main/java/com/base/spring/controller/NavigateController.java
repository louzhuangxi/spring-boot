package com.base.spring.controller;


import com.base.spring.custom.security.SecurityUser;
import com.base.spring.domain.TreeEntity;
import com.base.spring.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.h819.web.commons.MyServletUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;

/**
 * Description : TODO(ace admin template ajax 方式导航, 本项目用到。都是点击菜单传来，所以都是 Get)
 * 参见 example.AceAdminAjaxMenuExampleController 说明
 * spring mvc , freeMarker : 传递 非字符串变量到页面，必须用  @ModelAttribute("model") ModelMap model
 */

//方法级别权限控制
// @PreAuthorize("hasRole('ADMIN') AND hasRole('DBA')")
@Controller
@RequestMapping("/menu/ajax")
//重要: 必须是 ajax/content/xxx 形式，
// 以符合 ace.js 中 content_url 的要求，menu 为前缀，可以为任意值或者没有
@Slf4j
public class NavigateController {

    //private static final log log = LoggerFactory.getLogger(NavigateController.class);

    @Autowired
    UserService userService;

    /**
     * 跳转到首页，展示菜单及用户名称
     * -
     * ajax url :  http://localhost:8888/base/menu/ajax/index.html#page/index
     * 被解析为  :  http://localhost:8888/base/menu/ajax/index.html (注意没有 content)
     * 跳转到真正的页面 :  html/ajax/index.ftl
     * index.ftl 文件仅是一个导航文件，没有内容
     * index.ftl 会指向 ajax 方法，去加载 content/index.html 文件，该文件不存在，显示空白
     *
     * @param request
     * @param model
     * @param user    SecurityUser user  ModelAttribute("currentUser") 通过 @ControllerAdvice 获得
     * @return
     */
    @RequestMapping(value = "/index.html", method = RequestMethod.GET)
    public String home(HttpServletRequest request, Model model,
                       @ModelAttribute("currentUser") SecurityUser user) { //SecurityUser user  ModelAttribute("currentUser") 通过 @ControllerAdvice 获得
        log.info("request path={} ,  will go to /html/ajax/index.ftl", MyServletUtils.getFullPath(request));
        //初始化菜单
        log.info("user name in security={}", user.getUsername());
        log.info("user name in entity={}", user.getUser().getUserName());
        // 或直接获取 SecurityUser suser = SpringUtils.getSecurityUser();
        // UserEntity uEnity = suser.getUser();
        model.addAttribute("username", user.getUsername());
        TreeEntity menu = userService.getAllMenuByUser(user.getUser());
        model.addAttribute("menus", menu);
        return "admin/ace/html/ajax/index";
    }


    /**
     * 角色
     * -
     * ajax url :  http://localhost:8888/base/menu/ajax/index.html#page/jqgrid-roles
     * 被解析为  :  http://localhost:8888/base/menu/ajax/content/jqgrid-roles.html
     * 跳转到真正的页面 :  html/ajax/content/jqgrid-roles.ftl
     *
     * @return
     */

    @RequestMapping(value = "/content/admin/jqgrid-roles.html", method = RequestMethod.GET)  // 必须有 /content/
    // PreAuthorize 支持 spring el
    // 加在 service 层，好像粒度更细，如区分 add,del,eidt 等
    public String role(@RequestParam(value = "treeType", required = true) String treeType, HttpServletRequest request, Model model) {

        log.info("request path={} ,  will go to /html/ajax/content/jqgrid-roles.ftl", MyServletUtils.getFullPath(request));
        log.info("treeType ={}", treeType);
        model.addAttribute("app_path", MyServletUtils.getAppPath(request));
        model.addAttribute("treeType", treeType); //对菜单进行授权
        return "admin/ace/html/ajax/content/jqgrid-roles";
    }

    /**
     * 用户
     * -
     * ajax url :  http://localhost:8888/base/menu/ajax/index.html#page/jqgrid-user
     * 被解析为  :  http://localhost:8888/base/menu/ajax/content/jqgrid-user.html
     * 跳转到真正的页面 :  html/ajax/content/jqgrid-user.ftl
     *
     * @return
     */
    @RequestMapping(value = "/content/admin/jqgrid-user.html", method = RequestMethod.GET)    // 必须有 /content/
    public String user(HttpServletRequest request, Model model) {
        log.info("request path={} ,  will go to /html/ajax/content/jqgrid-user.ftl", MyServletUtils.getFullPath(request));
        model.addAttribute("app_path", MyServletUtils.getAppPath(request));
        return "admin/ace/html/ajax/content/jqgrid-user";
    }

    /**
     * 组
     * -
     * ajax url :  http://localhost:8888/base/menu/ajax/index.html#page/jqgrid-group
     * 被解析为  :  http://localhost:8888/base/menu/ajax/content/jqgrid-group.html
     * 跳转到真正的页面 :  html/ajax/content/jqgrid-group.ftl
     *
     * @return
     * @ModelAttribute("model") ModelMap model 必须是这句
     */
    @RequestMapping(value = "/content/admin/jqgrid-group.html", method = RequestMethod.GET)    // 必须有 /content/
    public String group(HttpServletRequest request, Model model) {
        log.info("request path={} ,  will go to /html/ajax/content/jqgrid-group.ftl", MyServletUtils.getFullPath(request));
        model.addAttribute("app_path", MyServletUtils.getAppPath(request));
        return "admin/ace/html/ajax/content/jqgrid-group";
    }


    /**
     * 根据 treeType ，获取不同类型的 ztree
     * -
     * ajax url :  http://localhost:8888/base/menu/ajax/index.html#page/ztree-type.html
     * 被解析为  :  http://localhost:8888/base/menu/ajax/content/ztree-type.html
     * 跳转到真正的页面 :  html/ajax/content/ztree-type.ftl
     *
     * @return
     */
    @RequestMapping(value = "/content/admin/ztree-type.html", method = RequestMethod.GET)    // 必须有 /content/
    public String ztree(@RequestParam(value = "treeType", required = true) String treeType, HttpServletRequest request, Model model) {
        log.info("request path={} , type={},  will go to /html/ajax/content/ztree.ftl", MyServletUtils.getFullPath(request), treeType);

        model.addAttribute("app_path", MyServletUtils.getAppPath(request));
        model.addAttribute("treeType", treeType);
        return "admin/ace/html/ajax/content/ztree-type";
    }


    /**
     * ajax url :  http://localhost:8888/base/menu/ajax/index.html#page/fuelux-tree
     * 被解析为  :  http://localhost:8888/base/menu/ajax/content/fuelux-tree.html
     * 跳转到真正的页面 :  html/ajax/content/fuelux-tree.ftl
     *
     * @return
     */
    @Deprecated // 用 zTree 代替，不能全选，不能自动选择所有子节点s
    @RequestMapping(value = "/content/fuelux-tree.html", method = RequestMethod.GET)    // 必须有 /content/
    public String fueluxTree(@RequestParam(value = "type", required = true) String type, HttpServletRequest request, Model model) {
        log.info("request path={} , type={},  will go to /html/ajax/content/fuelux-tree.ftl", MyServletUtils.getFullPath(request), type);

        model.addAttribute("app_path", MyServletUtils.getAppPath(request));
        model.addAttribute("menu_type", type);
        return "admin/ace/html/ajax/content/fuelux-tree";
    }

    // ...... 其他页面的例子

}