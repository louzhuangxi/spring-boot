package org.examples.spring.controller;


import org.apache.commons.lang3.StringUtils;
import org.examples.spring.domain.TreeEntity;
import org.examples.spring.repository.TreeEntityRepository;
import org.h819.web.jqgird.JqgridPage;
import org.h819.web.spring.jpa.DtoUtils;
import org.h819.web.spring.jpa.JpaDynamicSpecificationUtils;
import org.h819.web.spring.jpa.JpaUtils;
import org.h819.web.spring.jpa.SearchFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.Map;


/**
 * Description : TODO(演示动态查询实现方法,表格用 jagrid，其他应用，按照本例即可。)
 * Description : TODO(注意返回值的描述)
 * User: h819
 * Date: 2015-3-3
 * Time: 下午4:47
 * To change this template use File | Settings | File Templates.
 */
@Controller
@RequestMapping("/ace-admin")
public class DynamicSearchJqgridController {

    private static Logger logger = LoggerFactory.getLogger(DynamicSearchJqgridController.class);
    @Autowired
    TreeEntityRepository treeEntityRepository;

    @Autowired
    private ServletContext servletContext; //获得应用的路径用

    /**
     * jqgrid 通过 ajax 方式发送请求，得到返回的 json 数据后，在当前表格进行展示。
     * -
     * ajax 异步发送请求的方法，发送请求后，通过 success 和 error 方法来处理返回值，整个过程是异步发送，不会刷新页面。
     * -
     * ajax 发送的请求是字符串，返回值也是字符串，字符串的类型通过 dataType 来设定。
     * 这个过程没有 spring context  session 等概念 ，所以无法通过 session 传递变量，保存并获取返回的数据。
     * 一般返回值是 json ，返回的信息通过解析 json 来获得。
     * 所以常见的
     * session.setAttribute("user", new User("name"));
     * return new ModelAndView("login","user", new User("name"));
     * 页面端 ajax 的 success 和 error 方法中  ${user.name} 是无法获取, 这种方式是不行的，没有 spring context  session 等概念
     * -
     * 如果想要在 ajax 中获得返回值，需要在 spring 中设置 @ResponseBody 返回 json ，ajax 中通过 success方法类解析 json
     * -
     * 如果一定要传递，只能用传统的 Session  来传递 ，在页面端（非 ajax 的 success 方法）通过 $ 获取(此时页面端应该是重新载入)。
     * 因为 jqgrid 是异步发送发送请求，当前页不变，jqgrid 发送请求得到结果后，只刷新表格，而页面其他部分不变，所以页面无法读取放在 session 中的变量，只有刷新 jsp 页面才可以从 session 中读取
     * (在 ajax success 中刷新整个页面，可以是页面端从 seesion 中获取?)
     * -
     * 请求参数名称固定，为 jqgrid 默认，不要修改，这里不包装为 Bean ，为了显式的表明 jqgrid 传递过来的参数。通过 firebugs 可以查看
     *
     * @param search           是否是搜索请求 , 该参数在本方法中，用不到。
     * @param filters          通过 jqgrid search 查询，多个查询条件时，包含查询条件为 json 格式数据。_search = false 时，jqgrid 传递过来的参数没有 filters , 此时 filters 的值为 null
     * @param currentPageNo    当前页码
     * @param pageSize         页面可显示行数
     * @param sortParameter    用于排序的列名 ，启用 groups 时，此项复杂，需要特殊解析
     * @param direction        排序的方式desc/asc
     * @param allRequestParams 可以自动获取所有的参数
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
            @RequestParam(value = "sord", required = true) String direction, @RequestParam Map<String, String> allRequestParams, RedirectAttributes redirectAttrs, Model model, HttpServletRequest request, HttpServletResponse response) {

        logger.info("search ={},page ={},rows ={},direction={},sidx={},filters={}", search, currentPageNo, pageSize, direction, sortParameter, filters);


        logger.info("当前应用路径是 ： " + servletContext.getContextPath()); //返回：/h819-flexpaper
        logger.info("当前应用的磁盘路径 ： " + servletContext.getRealPath("/")); //返回：E:\program\IntelliJ IDEA Project\my-project\h819-flexpaper\src\main\webapp


        /**
         * @RequestParam Map<String, String> allRequestParams
         * 自动获取所有的参数，需要时可以启用
         */
        for (Map.Entry<String, String> e : allRequestParams.entrySet()) {
            System.out.println(String.format("key=%s , value=%s", e.getKey(), e.getValue()));
        }

        /**
         *返回值类型为  @ResponseBody ，只能通过 HttpSession 传递变量，Model 不可以。
         */
        //下面也可以通过 @SessionAttributes 来实现
        HttpSession session = request.getSession();
        session.setAttribute("param", "spring controller 的返回值类型为 @ResponseBody ，只能利用 session 向 jsp 传递变量");
        // 向 jsp 传递变量
        // 在 jqgrid 中存在问题 ：
        // 因为 jqgrid 是异步发送发送请求，当前页不变，jqgrid 发送请求得到结果后，只刷新表格，而页面其他部分不变，所以页面无法读取放在 session 中的变量，只有刷新 jsp 页面才可以从 session 中读取
        // 既然是刷新表格，把变量放在表格的 caption 属性中科院读取。
        // 在其他代码中读取无问题.
        //HttpSession session = request.getSession();
        //session.setAttribute("warning", " : 用户 " + user.getName() + " 没有订阅信息，此处无展示，请订阅后查看。 我的账户 -> 订阅设置");
        //有时间想办法解决 ?


        /**
         * 可以用在 jqgrid 和 非 jqgrid 条件下
         */

        /**
         * 除了 jqgrid 传递过来的查询条件外, 自己又附加查询条件，构造一个自定义查询条件
         * 多个查询条件时，可以用 DynamicSpecificationUtils 合并
         */
        Specification customSpecification = JpaDynamicSpecificationUtils.bySearchFilter(
                new SearchFilter("level", SearchFilter.Operator.EQ, "1"));


        /**
         * 查询结果:
         *
         * ---
         * 1. 仅有 jqgird 的 filter 查询条件时:
         * Page list1 = JqgridJPAUtils.getJqgridResponse(treeEntityRepository, currentPageNo, pageSize, sortParameter, direction, filters);
         * -
         * 2. 除了 jqgrid 传递过来的查询条件外，自己又附加查询条件时写法 (customSpecification):
         * Page list = JqgridJPAUtils.getJqgridResponse(treeEntityRepository, currentPageNo, pageSize, sortParameter, direction, filters, customSpecification);
         *  -
         * 3. 用在非 jqgrid 的条件下，没有 filter 查询条件 ,仅有自己附加查询条件时写法 (customSpecification):
         *Page list = JqgridJPAUtils.getJqgridResponse(treeEntityRepository, currentPageNo, pageSize, sortParameter, direction, null, customSpecification);
         */

        Page<TreeEntity> pages = JpaUtils.getJqgridPage(treeEntityRepository, currentPageNo, pageSize, sortParameter, direction, filters, customSpecification);
        if (pages.getTotalElements() == 0)
            return new JqgridPage(pageSize, 0, 0, new ArrayList(0)); //构造空数据集，否则返回结果集 jqgird 解析会有问题


        /**
         * POJO to DTO
         * 转换原因见 service/package-info.java
         */

        DtoUtils dtoUtils = new DtoUtils();  //用法见 DTOUtils
        dtoUtils.addExcludes(TreeEntity.class, "parent"); //在整个转换过程中，无论哪个级联层次，只要遇到 TreeEntity 类，那么他的 parent 属性就不进行转换
//       dtoUtils.addExcludes(TreeEntity.class, "parent", "children"); //多个属性例子
//       dtoUtils.addExcludes(WebSiteEntity.class, "webSiteColumns");
//       dtoUtils.addExcludes(WebSiteColumnEntity.class, "snatchUrls");


        JqgridPage<TreeEntity> jqPage = new JqgridPage
                (pages.getSize(), pages.getNumber(), (int) pages.getTotalElements(), dtoUtils.createDTOcopy(pages.getContent()));

        // 非 Jqgrid 情况下，JqgridPage 换成 PageBean 即可
//        PageBean<StStandardEntity> response = new PageBean
//                (page.getSize(), page.getNumber(), (int)page.getTotalElements(), dtoUtils.createDTOcopy(page.getContent()));



        /**
         * 增加额外属性演示 :
         */
        //利用 map 增加自定义属性，主要用来给 DTO 增加属性，便于向页面端传送额外的变量，这样就不必到 Entity 中增加 @Transient 类型的变量了
//        List<Map<String, Object>> listMap = Lists.newArrayList();
//        for (TreeEntity tree : page.getContent()) {
//            Map<String, Object> map = dtoUtils.createDTOMapCopy(tree); //单个对象转换为 map ，可以给这个对象添加属性
//            if (tree.getLevel()==0)
//                map.put("root", "是");
//            else
//                map.put("root", "否");
//
//            if (tree.getParent() != null)
//                map.put("parentName", tree.getParent().getName());
//            else
//                map.put("parentName", "否");
//
//            listMap.add(map);
//        }
//
//        JqgridResponse<Map<String, Object>> response = new JqgridResponse<Map<String, Object>>
//                (list.getSize(), totalRecordsSize, list.getNumber(), listMap);


        /**
         * Object to jsonString ，利用 spring  @ResponseBody 标记自动转换
         *
         * jqgrid 根据取返回的 json 数据，创建表格
         * -
         * 参数 id 有特殊要求，详见 JqgridResponseBean.java
         */

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
     * @param oper  编辑类型(add,edit,del)
     * @param ids   选取的行的 id
     * @param code
     * @param name
     * @param css
     * @param url
     * @param order
     */
    @RequestMapping(value = "/jqgrid-edit")
    //注意 value  /jqgrid-edit  ，不能为 /jqgrid-edit/ ，不能多加后面的斜线
    public void jqgridCURD(
            @RequestParam(value = "oper", required = true) String oper,
            //  @RequestParam(value = "id", required = true) String id, //单选时
            @RequestParam(value = "id", required = true) String[] ids, //多选时，如果为单选，数组只有一个值
            @RequestParam(value = "code", required = false) String code,
            @RequestParam(value = "name", required = false) String name,
            @RequestParam(value = "css", required = false) String css,
            @RequestParam(value = "url", required = false) String url,
            @RequestParam(value = "order", required = false) Integer order,
            @RequestParam(value = "parent.code", required = false) String pCode, RedirectAttributes redirectAttrs, Model model, HttpServletRequest request, HttpServletResponse response) {

        /**
         *    删除时，只有 oper 和 id 两个参数，所以其他参数都设置为 false
         */

        logger.info("oper ={},id={},code={},name={},css={},url={},oder={},parent.code={}", oper, ids, code, name, css, url, order, pCode);


        //删除
        if (oper.equals("del")) {
            logger.info("del action.");
            //多选时，逐个处理
            for (String id : ids) {
                logger.info("id =" + id);
                // do delete action
            }

            return; //删除后返回

        } else if (oper.equals("add")) {

            logger.info("add action.");

            // do add action

        } else if (oper.equals("edit")) {

            logger.info("edit action.");
            //多选时，逐个处理
            for (String id : ids) {
                logger.info("id =" + id);

                //必填项 。  不能放在方法参数中，用 required = true 限制，因为 del 操作无此参数
                Assert.hasText(code.trim(), "namecn must not be null!");
                Assert.hasText(css.trim(), "source must not be null!");
                Assert.hasText(pCode.trim(), "validated must not be null!");
                // do delete action

                // load the Entity
//                PackageAddClassEntity e = packageAddClassRepository.getOne(Long.valueOf(idStr));
//                e.setNameCn(namecn.trim());
//                e.setCas(cas.trim());
//                e.setSource(source.trim());
//                e.setPage(Integer.valueOf(page.trim()));
//                e.setRemark(remark.trim());
//                e.setCanceled(flag);
//                packageAddClassRepository.save(e);

            }
        } else {
            //do none. 没有其他的 oper 参数值了
        }

        return;
    }

    /**
     * 自定义按钮，对行进行操作。
     * -
     * - 选中行的 id ，通过 jqgrid 的函数获得，并传递到后台
     * - var ids = jQuery(grid_selector).jqGrid('getGridParam','selarrrow') ;  //获取选中的多行
     * - var id = jQuery(grid_selector).jqGrid('getGridParam','selrow'); // 获取选中的单行
     * -
     * 返回值说明见上文。
     *
     * @param oper          每个 jqgrid 提交的动作，均会自动提交该变量，无论在 js中设置与否
     * @param ids           选取的行的 id
     * @param redirectAttrs
     * @return
     */

    @RequestMapping(value = "/custom_buttons")
    //注意 value  /custom_buttons  ，不能为 /custom_buttons/
    public String jqgridCustomButton(@RequestParam(value = "oper", required = true) String oper, @RequestParam(value = "id", required = true) String[] ids,
                                     RedirectAttributes redirectAttrs, Model model, HttpServletRequest request) {

        //获取 Session
        // request.getSession();

        if (oper.equals("read")) {
            logger.info("read");
            for (String id : ids) {
                //自定义按钮，提交的是字符串 ["1","2"] ，需要自己提取出 id 值
                String idstr = StringUtils.substringBetween(id, "\"", "\"");
                logger.info("id =" + idstr);

                //do something

                /**
                 * 返回值即携带变量总结
                 */
                //return "test";  // 返回到 /WEB-INF/jsp/test.jsp 只能通过 Model 传递变量 ，不能指定为 controller ，这种返回方式按照 spring-mvc-dispatcher-servlet.xml 中定义 进行匹配。
                //return "redirect:test";  // 全新发起一个请求，返回到一个 controller test，只能通过 RedirectAttributes 传递变量，在 test 指定的页面中,addFlashAttribute 方法增加的变量可以读取一次后销毁，addAttribute 方法增加的变量，附加值 url 上
                //return "redirect:/test.jsp"; // 全新发起一个请求，直接访问web应用根目录下的 test.jsp 页面，Model 和 RedirectAttributes 都无法传递变量，仅 addAttribute 方法增加的变量，会附加值 url 上

                //session 携带变量见下文。


                /**
                 * 存放变量的三种容器 ： Model , RedirectAttributes , Session
                 */


                //  ============================= Model =============================
                /**
                 * 只能用于返回到 jsp 页面(其他前段视图也可以，如 pdf ...)， 详见下文 "跳转到 jsp 页面 第一部分"
                 */
                /**
                 * Model 中的变量存在范围和读取
                 *
                 * Model :
                 *  1. 在默认情况下，ModelMap 中的属性作用域是 request 级别，也就是说，当本次请求结束后，ModelMap 中的属性将销毁。
                 *  2. 返回值不能是 Redirect 类型，Redirect 是发起一个全新的请求，放在其中的变量全部摒弃。
                 *  3. 浏览器地址栏不变，还是原来的请求地址
                 *  4. jsp 页面页面读取 ${param3}
                 *
                 *
                 *
                 * 即只能在返回的结果视图上读取一次
                 *-
                 * 如果希望在多个请求中共享 ModelMap 中的属性，必须将其属性转存到 session 中，这样ModelMap 的属性才可以被跨请求访问。
                 * -
                 * Spring 允许我们有选择地指定 ModelMap 中的哪些属性需要转存到 session 中，以便下一个请求属对应的ModelMap 的属性列表中还能访问到这些属性。
                 * 这一功能是通过类定义处标注 @SessionAttributes 注解来实现的，先下文 session。
                 *
                 */


                //  ============================= RedirectAttributes =============================
                /**
                 *   主要用法：返回到一个 controller ，再通过该 controller 回到到 jsp
                 *
                 *   参考下文 " redirect 方式返回  "
                 *
                 *   RedirectAttributes 只能用在 Redirect 方式返回的结果上携带变量(主要用来避免重复提交操作)
                 *
                 * - 为了防止用户刷新重复提交，save 操作之后一般会 redirect 到另一个页面，同时带点操作成功的提示信息。
                 * -
                 *  因为是 Redirect，会发起一个全新的地址请求，原系统参数变量都销毁了，存放在在返回的 Request 里的 attribute 不会传递过去，如果放在 session 中，则需要在显示后及时清理，不然下面每一页都带着这个信息。
                 * -
                 *  spring 提供了 RedirectAttributes 类型的变量， 跳转时附带参数，下一个请求可以捕获
                 * -
                 *  Redirect 方式返回 关键点 ：
                 *  - 0. 结合 下文 " redirect 方式返回  " 内容
                 *  - 1. 返回值必需以 "redirect: ... " 开头
                 *  - 2. 参数只能通过 RedirectAttributes 参数携带，Model 不可以
                 *  - 3. 下文 1 中提到：返回到另外一个 controller ，addFlashAttribute 方法携带的变量，可以到该 controller 指定的 jsp 中读取，addAttribute 方法添加的变量，到其指定的 jsp 页面不可以读取。
                 *  - 4. 下文 2 中提到：返回到 jsp 的绝对路径， addFlashAttribute 和 addAttribute 方法携带的变量，该 jsp 中都不可取，addAttribute 变量会添加到跳转路径上。
                 */

                /**
                 * addAttribute、addFlashAttribute 二者有区别
                 * 以 return "redirect:manage"  为例 ， (返回到一个 controller manage ,  WEB-INF/page/jsp/manage.jsp)
                 */
                /**
                 *- addAttribute 方法: 在 url 上拼接参数
                 */
                // redirectAttrs.addAttribute("param", "value1");
                // 1. 会在跳转地址后进行字符串拼接，加上 ?param=value1  , 以 get 形式发送， 最终浏览器地址栏会显示形如  http://localhost:8888/info/manage??param=value1
                // 2. 既然是进行 String 的拼接，所以无法传递对象属性，只能是字符串
                // 3. 返回地址形如  " http://localhost:8888/info/manage?param=value1 "  实际上是发起了一个新的请求，只不过是拼接了几个字符串参数，传递到新的请求。 该变量不会再 session 中保存，只在这个请求中有效。
                // 4. 这个方法一般用来返回到一个 controller ，这样的话，传递的参数可以直接被接收的 controller 的方法进行参数匹配
                // -
                /**
                 *- addFlashAttribute 方法:
                 */
                //  redirectAttrs.addFlashAttribute("param", "value1");
                // 1. 会把属性放入 session ，且只能在 jsp 页面中读取，(如果跳转到其他 controller ，在其中 request.getSession().getAttribute("param") 无法读取)
                // 2. 相当于 以 post 形式发送， 最终浏览器地址栏会显示形如  http://localhost:8888/info/manage ，不显示参数
                // 3. 在 jsp 页面获得： ${param}  或 session.getAttribute("param");
                // 4. 被读取一次之后，自动销毁，不必显示从 session 中去除，此时刷新 jsp 页面，param 不存在。
                // 5. 主要用来向 jsp 页面传递临时变量


                //  ============================= Session  =============================

                /**
                 *  通过 session 传递变量
                 */
                //放入 session 的变量，在整个会话中都需要使用
                // 步骤：
                // 1. 在类级别 @SessionAttributes("username");
                // 2. 在方法内，发变量放入 Model，  model.addAttribute("username", "session value");
                // 3. jsp 页面读取 ${username}
                // 删除 见下文

                //传统方式：
                // request.getSession().setAttribute("id", id);
                // jsp 页面获得: id=${id}
                // class 中获得: request.getSession().getAttribute("id").toString()
                //使用后消除 request.getSession().removeAttribute("id");

                // jstl方式 显示和删除
                //   <c:if test="${param != null}">
                //
                //   <div class="well well-sm">
                //   <button type="button" class="close" data-dismiss="alert">&times;</button>
                //   <div class="inline middle red bigger-110"> ${user_update_succeed}</div>
                //            &nbsp; &nbsp; &nbsp; </div>
                //   </c:if>
                //
                //   移除信息，此句一定要在 显示代码之后，否则直接移除了，不能显示
                //   <c:remove var="param" scope="session"></c:remove>


                // -  例子
                redirectAttrs.addAttribute("param", "这个参数会议 get 请求的方式发送，拼接在url上").addFlashAttribute("param2", "这个属性存在 session 中，只能在controller 指定的 jsp 页面中读取，第一次提取之后即销毁");


                model.addAttribute("param3", "普通方式进行跳转");  // 参数也会以 post 形式发送，不会在地址栏显示参数

                /**
                 *=============================================== redirect 方式返回  ===============================================
                 */

                // 只能返回到两种路径
                // 1. controller : 详见下文 " 跳转到 controller "
                // 2. jsp : 详见下文 "以 redirect 方式，定位到具体 jsp 页面 "


                /**
                 *=============================================== 跳转到不同的视图 :  jsp , controller  ===============================================
                 */

                /**
                 * 跳转到 jsp 页面(由 spring controller 指定)
                 */
                // =====  在 spring-mvc-dispatcher-servlet.xml 中定义
                // 1.  return "ace/html/ajax/index";  //此种返回方式，会自动的按照 spring-mvc-dispatcher-servlet.xml 定义去找 jsp

                // =====  以 redirect 方式，定位到具体 jsp 页面
                // 2.  return "redirect:/views/test.jsp"   网站根目录下的一个具体jsp 页面 (webapp/views/test.jsp)   jsp 页面的绝对路径
                //     或  return "redirect:http://localhost:8888/info//views/test.jsp"
                //     也就是说 redirect 是发起一个全新的请求，所以只有一个能访问到的jsp页面才可以， 所以 1 提到的 jsp， 放在 WEB-INF 中，无法被直接访问，必需通过 controller 访问，所是不能作为 redirect 的返回值的。

                /**
                 * 跳转到 controller (只能用 redirect 才可以跳转到其他 controller)
                 */
                // =====  在当前 controller 类内跳转，一般用相对地址
                // 如跳转到地址为：   @RequestMapping(value = "/search")
                // return "redirect:search";
                // ===== 跳转到其他的 controller ，用绝对地址
                //跳转地址为绝对地址，是该 controller 中方法的 @RequestMapping 值(含类级别的路径)(即从应用根目录开始，浏览器可以单独访问到的路径)
                //return "redirect:/page/jqgrid_province";


                /**
                 * =============================================== 绝对地址，相对地址 ===============================================
                 */

                //绝对地址，从应用的根目录开始：return "/page/jqgrid_province";
                //相对地址，从该请求开始：return "page/jqgrid_province";


                /**
                 * controller 返回值
                 */

                //     研究一下下面的写法
                // return new ModelAndView(new RedirectView("/ticket/list", true, false));

                return "";
            }
        }

        if (oper.equals("mark")) {

            logger.info("mark");
            for (String id : ids) {
                //自定义按钮，提交的是字符串 ["1","2"] 需要自己提取出 id 值
                String idstr = StringUtils.substringBetween(id, "\"", "\"");
                logger.info("id =" + idstr);


                //do something

            }
        }


        return null;
    }


    /**
     * 用来辅助返回值为 redirect 方式 ，返回到 jsp 页面。（见上文 “Redirect 返回到 jsp 页面的问题” 分析）
     *
     * @return
     */
    @RequestMapping(value = "/showjsp.html", method = RequestMethod.GET)
    public String registration() {
        // 此文中，没有从 session 读取变量，所以 addFlashAttribute 方式保存的变量，还在 session 中，科院传递到本方法返回到的 jsp 页面
        return "ace/html/ajax/profile";

    }


}
