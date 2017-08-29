package com.base.spring.controller.ajax.validate;

import com.base.spring.domain.UserEntity;
import com.base.spring.repository.UserRepository;
import com.base.spring.utils.BCryptPassWordUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Optional;

/**
 * Description : TODO(处理用户登录时的验证操作。用户管理在 JqgridUserContorlloer 中)
 * User: h819
 * Date: 14-1-17
 * Time: 下午4:47
 * To change this template use File | Settings | File Templates.
 */
@Controller
@RequestMapping("/ajax/validate/login") // 此处要和 Spring security 中 ignoring 要呼应，不能进行登录限制，否则无法进行验证
@Slf4j
public class LoginAjaxValidateController {

    //private static final log log = LoggerFactory.getLogger(LoginAjaxValidateController.class);

    @Autowired
    UserRepository userEntityRepository;

    // =================================== user register/login validate property exists  =======================================//

    /**
     * 邮件验证
     * -
     * 1. 如果是仅检查查用户的属性是否存在(如登陆...)，UserEntity!= null , return "true"
     * -
     * 2. 如果是用户注册 register ，不存在才可以注册，UserEntity== null , return "true"
     *
     * @param checkType 验证类型: register 为用户注册，exists 为验证属性是否存在，两种验证逻辑不同。在 jsp 页面需要传递此参数
     * @param email     email
     * @return
     */
    @RequestMapping(value = "/email.html")
    @ResponseBody
    public String validateEmail(@RequestParam(value = "email", required = false) Optional<String> email, @RequestParam(value = "checktype", required = false) Optional<String> checkType) {

       // log.info("check type : {}, email : {} ", checkType.get(), email.get());

        if (!email.isPresent()) {
       //     log.info("email 没有填写 ");
            return ("email 没有填写"); //登录名已经存在，不能注册
        }

        if (!checkType.isPresent()) {
        //    log.info("不是验证 email 请求 ");
            return ("不是验证 email 请求"); //登录名已经存在，不能注册
        }

        //注册时进行检查，此时需要该属性不存在，才可以注册
        if (checkType.get().equals("register")) {
            log.info("用户注册验证 ，邮件地址不存在，才可以通过验证 ...");

            Optional<UserEntity> userEntity = userEntityRepository.findOneByEmail(email.get());
            if (!userEntity.isPresent()) {
                log.info("email :  {} 不存在 , 验证通过 ", email.get());
                return "true";
            } else {
                log.info("email : {} 已经存在 , 验证不通过 ", email.get());
                return ("email 已经存在"); //登录名已经存在，不能注册
            }

        } else if (checkType.get().equals("login") || checkType.get().equals("find")) { // 验证登陆和找回密码，email 是否存在

            log.info("login or find email validate");

            Optional<UserEntity> userEntity = userEntityRepository.findOneByEmail(email.get());

        //    log.info(" user.isPresent {}", userEntity.isPresent());

            if (userEntity.isPresent()) {
                log.info("email : {} 存在，验证通过 ", email.get());
                return "true";
            } else {
                log.info("email : {}  不存在 , 验证不通过 ", email.get());
                return ("email 不存在"); //登录名已经存在，不能注册
            }

        } else
            return "false";

    }

    /**
     * password 验证
     * -
     * 1. 如果是仅检查查用户的属性是否存在(如登陆...)，UserEntity!= null , return "true"
     * -
     * 2. 如果是用户注册 register ，不存在才可以注册，UserEntity== null , return "true"
     *
     * @param loginPassword 验证类型: register 为用户注册，exists 为验证属性是否存在，两种验证逻辑不同。在 jsp 页面需要传递此参数
     * @param email         email
     * @return
     */
    @RequestMapping(value = "/password.html")
    @ResponseBody
    public String validateLoginPassword(@RequestParam(value = "email", required = false) Optional<String> email, @RequestParam(value = "loginpasword", required = false) Optional<String> loginPassword) {

        log.info("check login_password : {}, email : {} ", loginPassword.get(), email.get());

        if (!email.isPresent()) {
            log.info("email 没有填写 ");
            return ("email 没有填写");
        }

        if (!loginPassword.isPresent()) {
            log.info("登陆密码没有填写 ");
            return ("登陆密码没有填写");
        }

        //登陆时，验证密码是否错误。
        //邮件地址此时 已经验证完毕，验证密码即可

        log.info("用户登录，进行密码验证 ...");
        Optional<UserEntity> userEntity = userEntityRepository.findOneByEmail(email.get());
        if (!userEntity.isPresent()) {

            return "先修正 email 错误";

        } else {

            if (BCryptPassWordUtils.matches(loginPassword.get(), userEntity.get().getPassword())) {
                log.info("密码匹配: {} 验证通过 ", email.get());
                return "true";
            } else
                return "密码错误";
        }

    }
}