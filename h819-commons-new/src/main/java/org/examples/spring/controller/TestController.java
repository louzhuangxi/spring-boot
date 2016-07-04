package org.examples.spring.controller;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;


/**
 * Description : TODO(测试 controller 返回值问题)
 * User: h819
 * Date: 14-1-17
 * Time: 下午4:47
 * To change this template use File | Settings | File Templates.
 */
@Controller
@RequestMapping("/test")
@SessionAttributes("session")
//@Transactional(readOnly = true)
public class TestController {

    private static Logger logger = LoggerFactory.getLogger(TestController.class);


    @RequestMapping(value = "/jsp")

    public String test1(Model model, RedirectAttributes redirectAttrs) {

        redirectAttrs.addAttribute("attr", "attr").addFlashAttribute("flashattr", "flashattr");
        model.addAttribute("model", "model");
        model.addAttribute("session", "session value"); //必需放入 Model ，@SessionAttributes 才可以读取.

        //return "test";  // 返回到 /WEB-INF/jsp/test.jsp 只能通过 Model 传递变量 ，不能指定为 controller ，这种返回方式按照 spring-mvc-dispatcher-servlet.xml 中定义 进行匹配。
        //return "redirect:test";  // 全新发起一个请求，返回到一个 controller test，只能通过 RedirectAttributes 传递变量，在 test 指定的页面中,addFlashAttribute 方法增加的变量可以读取一次后销毁，addAttribute 方法增加的变量，附加值 url 上
       // return "redirect:/test.jsp"; // 全新发起一个请求，直接访问web应用根目录下的 test.jsp 页面，Model 和 RedirectAttributes 都无法传递变量，仅 addAttribute 方法增加的变量，会附加值 url 上
        return "redirect:test";

    }

    @RequestMapping(value = "/test")
    public String test2(Model model, RedirectAttributes redirectAttrs, HttpServletRequest request) {

        System.out.println("1 = " + request.getSession().getAttribute("attr"));
        System.out.println("2 = " + request.getSession().getAttribute("flashattr"));
        System.out.println("3 = " + request.getSession().getAttribute("model"));


//        redirectAttrs.addAttribute("attr","attr").addFlashAttribute("flashattr","flashattr");
//        model.addAttribute("model","model");


        return "test";

    }

}

