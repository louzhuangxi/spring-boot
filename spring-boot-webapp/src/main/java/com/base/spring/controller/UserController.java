package com.base.spring.controller;

import com.base.spring.domain.validator.UserCreateFormValidator;
import com.base.spring.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.NoSuchElementException;

@Controller
public class UserController {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserController.class);
    @Autowired
    private UserService userService;
    @Autowired
    private UserCreateFormValidator userCreateFormValidator;

    @InitBinder("form") //spring mvc 验证
    public void initBinder(WebDataBinder binder) {
        binder.addValidators(userCreateFormValidator);
    }


    /**
     *  @PreAuthorize() 中使用 Spring EL 表达式
     *
     *  1. 可以通过在参数名前添加 # 的方式引用任意的方法参数，或者其属性，如 #id , #user.id
     *  2. hasAuthority('ADMIN') 等表达式 ，详见 http://docs.spring.io/spring-security/site/docs/4.0.1.RELEASE/reference/htmlsingle/#el-common-built-in
     *  3. principal 参数 ：  @PreAuthorize("principal.userId")  principal 直接引用当前用户，principal 相当于 SecurityContextHolder.getContext().getAuthentication().getPrincipal()
     *  4. authentication 参数：  @PreAuthorize("authentication.principal.userId")  authentication 直接引用，authentication 相当于 SecurityContextHolder.getContext().getAuthentication()
     *  5. 直接调用spring context 中的 bean ,例如
     *     @PreAuthorize("@currentUserService.canAccessUser(principal, #id)")
     *     调用 spring context 中的 名字为 currentUserService 的实例，并执行其 canAccessUser 方法。
     *     尝试一下，不用 @ 行不行  ?????
     *
     */
    // 调用容器管理的名字为 currentUserService 的 bean 对象 , currentUserService.canAccessUser 方法，返回 true 时，验证通过
    // 执行过程：
    // 在 PreAuthorize 中的表达式，可以通过在参数名前添加 # 的方式引用任意的方法参数
    // 把 spring security 验证通过后，放入 spring security session 中的 principal ( 即 CustomUserDetailsService 中的 loadUserByUsername 的返回值)
    // 和
    // 本方法参数中的 id 参数  的值
    // 传递到 currentUserService.canAccessUser 方法中
    //Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    //currentUserService

    /**
     * 调用容器管理的名字为 currentUserService 的 bean 对象 , currentUserService.canAccessUser 方法，返回 true 时，验证通过
     * true:只有自己或者 ADMIN 才可以查看用户信息 (currentUserService.canAccessUser 中定义)
     *
     * @param id
     * @return
     */
    @PreAuthorize("@currentUserService.canAccessUser(principal, #id)")
    @RequestMapping("/user/{id}")
    public ModelAndView getUserPage(@PathVariable Long id) {
        LOGGER.debug("Getting user page for user={}", id);

        return new ModelAndView("user", "user", userService.getUserById(id).orElseThrow(() -> new NoSuchElementException(String.format("User=%s not found", id))));
    }

    /**
    @PreAuthorize("hasAuthority('ADMIN')")
    @RequestMapping(value = "/user/create", method = RequestMethod.GET)
    public ModelAndView getUserCreatePage() {
        LOGGER.debug("Getting user create form");
        //返回到前端一个默认的 UserEntity，便于携带信息
        return new ModelAndView("user_create", "form", new UserEntity());
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @RequestMapping(value = "/user/create", method = RequestMethod.POST)
    public String handleUserCreateForm(@Valid @ModelAttribute("form") UserEntity form, BindingResult bindingResult) {
        LOGGER.debug("Processing user create form={}, bindingResult={}", form, bindingResult);
        if (bindingResult.hasErrors()) {
            // failed validation
            return "user_create";
        }
        try {
            userService.create(form);
        } catch (DataIntegrityViolationException e) {
            // probably email already exists - very rare case when multiple admins are adding same user
            // at the same time and form validation has passed for more than one of them.
            LOGGER.warn("Exception occurred when trying to save the user, assuming duplicate email", e);
            bindingResult.reject("email.exists", "Email already exists");
            return "user_create";
        }
        // ok, redirect
        return "redirect:/users";
    }

     */
    @RequestMapping("/users")
    public ModelAndView getUsersPage() {
        LOGGER.debug("Getting users page..");
        return new ModelAndView("users", "users", userService.getAllUsers());
    }

}
