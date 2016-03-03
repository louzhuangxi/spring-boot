package com.base.spring.controller;

import com.base.spring.dto.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
public class LoginController {

    private static final Logger logger = LoggerFactory.getLogger(LoginController.class);

    /**
     * 访问 login.html ， 仅跳转用，不用来传递参数
     * <p/>
     * login.html 用 html/login.html 改编，不用html/ajax/login.html ，以便于不显示 sidebar 菜单
     * 登陆后，在跳转到 html/ajax/index.html  页面
     * login.jsp 页面，验证部分，做了大量改动，升级的时候，要仔细比较
     *
     * @return
     */

    // Spring Security 中配置的 formLogin().loginPage("/login") 只接受 GET 请求，作用仅是跳转到登陆页。此处的 /login 是为了匹配  formLogin().loginPage("/login")  而设定的
    // spring security 用户登录的 url ，是 loginProcessingUrl("/login_process")配置的，仅接受 POST 请求。
    // loginProcessingUrl 也可以配置为 /login ，但本方法不处理，因为他是 POST 请求。
    //  @RequestParam(value = "error", required = false) String error) 无法接到 error 参数
    //不能通过 @RequestParam 获取额外的参数，只好通过 org.springframework.security.web.savedrequest.SavedRequest 获取
    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public String login(HttpServletRequest request, HttpServletResponse response) {
        // 好像是，只有请求过一次之后，error 才有值，不值得为什么
        // 本方法只用来跳转，所以无限传递参数，此处仅为演示
//        String param = SpringUtils.parseSpringSecurityLoginUrlWithExtraParameters(request, response).get("error");
//        logger.info("Getting login page, error={}", param);
        logger.info("go to login");
        return "admin/ace/html/login";
    }



    @RequestMapping(value = "/about", method = RequestMethod.GET)
    @ResponseBody  // 返回 json ，供提交的 ajax 处理
    public Message getAboutPage(@RequestParam(value = "error", required = false) String error) {
        logger.info("Getting login page, error={}", error);
        return new Message("error", error);
    }


}
