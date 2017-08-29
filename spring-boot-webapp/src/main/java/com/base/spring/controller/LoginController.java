package com.base.spring.controller;

import com.base.spring.vo.CustomErrorResponseMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
@Slf4j
public class LoginController {

    ////private static final log log = LoggerFactory.getLogger(LoginController.class);

    /**
     * 访问 login.html ， 仅跳转用，不用来传递参数
     * <p/>
     * login.html 用 html/login.html 改编，不用html/ajax/login.html ，以便于不显示 sidebar 菜单
     * 登陆后，在跳转到 html/ajax/index.html  页面
     * login.ftl 页面，验证部分，做了大量改动，升级的时候，要仔细比较
     *
     * @return
     */

    // Spring Security 中配置的 formLogin().loginPage("/login") 只接受 GET 请求，作用仅是跳转到登陆页。
    // 此处的 /login 是为了匹配  formLogin().loginPage("/login")  而设定的
    // spring security 用户登录后处理登录情况的 url ，是 loginProcessingUrl("/login_process")配置的，仅接受 POST 请求。
    // loginProcessingUrl 也可以配置为 /login ，但本方法不会处理，因为他是 POST 请求。
    // @RequestParam(value = "error", required = false) String error) 无法接到 error 参数
    // 不能通过 @RequestParam 获取额外的参数，只好通过 org.springframework.security.web.savedrequest.SavedRequest 获取

    /**
     * spring security login , only GET method
     * -
     * spring security 配置中，loginPage 设定的登录页，仅接收 GET 请求
     *
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public String login(HttpServletRequest request, HttpServletResponse response, @RequestParam(value = "error", required = false) String error) {
        // 好像是，只有请求过一次之后，error 才有值，不值得为什么，现象同下面的 error 方法
        // 本方法只用来跳转，所以无法传递参数，此处仅为演示
//        String param = SpringUtils.parseSpringSecurityLoginUrlWithExtraParameters(request, response).get("error");
//        log.info("Getting login page, error={}", param);
        log.info("go to login page");
        return "admin/ace/html/ajax/content/login";
    }

    /**
     * 出错页面
     *
     * @param error
     * @param request
     * @return
     */
    @RequestMapping(value = "/myerror", method = RequestMethod.GET)
    @ResponseBody
    public CustomErrorResponseMessage error(@RequestParam(value = "error", required = false) String error, HttpServletRequest request) {
        log.info("login error, error={}", error);
        return new CustomErrorResponseMessage(request, error);
    }


    /**
     * 默认首页，直接跳转到相关的 Controller
     *
     * @return
     */
    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String index() {
        log.info("redirect to other controller");
        // return "redirect:http://canhelp.cn";
        return "redirect:/menu/ajax/index.html";  // 跳转到 AceAdminAjaxMenuController 中的 url
    }


}
