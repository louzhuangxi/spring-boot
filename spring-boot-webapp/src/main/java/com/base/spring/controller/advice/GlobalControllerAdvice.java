package com.base.spring.controller.advice;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;

import javax.servlet.http.HttpServletResponse;
import java.io.FileNotFoundException;
import java.io.IOException;


/**
 * @ControllerAdvice 作用
 */
// 1. 该注解使用了@Component注解,所以会被自动注册为 spring bean
// 2. 相当于一个拦截器, 在所有的 @Controller 的 @RequestMapping 方法执行之前执行。相当于全局 @Controller 的拦截器
// 2.1 @ControllerAdvice 中使用 @ExceptionHandler、@InitBinder、@ModelAttribute 的三个方法，
//     都在所有的 @Controller 中的 @RequestMapping 注解的方法执行之前执行
// 2.2 被 @ControllerAdvice 注解的类，使用 @ExceptionHandler、@InitBinder、@ModelAttribute 三个注解来添加操作。

// @ControllerAdvice(basePackages = {"com.concretepage.controller"} ) //指定拦截应用到哪些包的 controller 中
// @ControllerAdvice(annotations=RestController.class)仅处理 @RestController 标记的的 controller
@ControllerAdvice
public class GlobalControllerAdvice {

    //private static final log log = LoggerFactory.getLogger(GlobalControllerAdvice.class);


    /**
     * 执行每个 url 时，都检查是否经过验证,把返回值放入 Model
     *
     * @param authentication 会被
     * @return 返回值 和 CustomUserDetailsService 的返回值org.springframework.security.core.userdetails.User  为 UserDetails类型
     * ---
     * @ModelAttribute 会把键值对，放入到全局 ，所有注解 @RequstMapping 的方法，均可以获得到。
     * 本例中，把获得的用户信息，放入到全局 Model
     * ---
     * public void getSomething(@ModelAttribute("currentUser") SecurityUser user) {
     * user.get...
     * //此处可以获得   @ModelAttribute 放入 Model 中的信息 UserDetails
     * }
     * ---
     */

    // @ModelAttribute 在方法上注解 :
    // 1. 则会在 spring context 中查找属性名为 authentication 的对象，并把该对象赋值给该方法的参数。
    //    本例中 authentication 参数会被自动赋值为 SecurityContextHolder.getContext().getAuthentication()
    // 2. 该方法的返回值，以 @ModelAttribute 的 value 值为名字，放入 Model，可以在页面段获取。
    //    在本项目中 : 可以通过 currentUser.username 获得用户名 (username 为 UserDetails 属性)
    @ModelAttribute(value = "currentUser") // currentUser 在页面端用到
    public UserDetails getCurrentUser(Authentication authentication) {

        //如果 loadUserByUsername 没有找到用户，会返回 返回用户名 anonymousUser 的用户默认用户
        // 下面 log 的结果为测试信息
        // is isAuthenticated() ? true
        // Authentication name : anonymousUser
//        if (authentication != null) {
//            log.info("is isAuthenticated() ? " + SecurityContextHolder.getContext().getAuthentication().isAuthenticated());
//            log.info("Authentication name : " + SecurityContextHolder.getContext().getAuthentication().getName());
//            log.info("getCredentials name : " + SecurityContextHolder.getContext().getAuthentication().getCredentials());
//            log.info("getDetails name : " + SecurityContextHolder.getContext().getAuthentication().getDetails());
//            for (GrantedAuthority authority : SecurityContextHolder.getContext().getAuthentication().getAuthorities())
//                log.info("authority(role) : " + authority.getAuthority());
//
//            //测试验证对象类型
//            Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
//            if (principal instanceof UserDetails) {
//                log.info("loadUserByUsername 的返回值类型是 UserDetails : " + ((UserDetails) principal).getUsername());
//            }
//            if (principal instanceof Principal) {
//                log.info("loadUserByUsername 的返回值类型是 Principal : " + ((Principal) principal).getName());
//            }
//        } else {
//            log.info("authentication is null.");
//        }

        return (authentication == null) ? null : (UserDetails) authentication.getPrincipal();
    }

    /***
     * @param binder
     * @InitBinder 用来配置全局 Controller ，设置 WebDataBinder，WebDataBinder 用来自动绑定前台请求参数到 Model 中。
     */
    @InitBinder
    public void initBinder(WebDataBinder binder) {
        //忽略 request 中的参数 dis ，更多关于 WebDataBinder ，可参考文档。
        //binder.setDisallowedFields("dis");
//        binder.registerCustomEditor(LocalDate.class, new PropertyEditorSupport() {
//            @Override
//            public void setAsText(String text) throws IllegalArgumentException {
//                LocalDate.parse(text, DateTimeFormatter.ISO_DATE);
//            }
//        });  //自动绑定 LocalDate
//
//        @GetMapping
//        public ResponseEntity<List<Order>> getOrdersByDate(
//                @RequestParam(name = "date")LocalDate date) {
//            // retrieve and return orders by date
//        }

    }


    // 应用到所有 @RequestMapping 注解的方法，在该方法抛出 Exception 异常时执行此方法
    @ExceptionHandler(value = FileNotFoundException.class)
    public void handle(FileNotFoundException ex, HttpServletResponse response) throws IOException {
        System.out.println("handling file not found exception");
        response.sendError(404, ex.getMessage());
    }

    @ExceptionHandler(value = IOException.class)
    public void handle(IOException ex, HttpServletResponse response) throws IOException {
        System.out.println("handling io exception");
        response.sendError(500, ex.getMessage());
    }

}
