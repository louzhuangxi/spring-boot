package com.base.spring.controller.ajax.validate;

import com.base.spring.repository.RoleRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Description : TODO(jqgrid 编辑中用到)
 * User: h819
 * Date: 14-1-17
 * Time: 下午4:47
 * To change this template use File | Settings | File Templates.
 */
@Controller
@RequestMapping("/ajax/validate/")  // 此处要和 Spring security 中 ignoring 要呼应，不能进行登录限制，否则无法进行验证
@Slf4j
public class JqgridEditAjaxValidateController {

    //private static final log log = LoggerFactory.getLogger(JqgridEditAjaxValidateController.class);

    @Autowired
    RoleRepository roleRepository;

    // =================================== user register/login validate property exists  =======================================//

    /**
     * 验证 Role 名称是否存在
     *
     * @param name
     * @return
     */
    @RequestMapping(value = "/role/name/exist.html")
    @ResponseBody
    public String validateCode(@RequestParam(value = "ids", required = true) String[] ids, //多选时，如果为单选，数组只有一个值
                               @RequestParam(value = "name", required = false) String name) {
        log.info("ids={} , name={}", ids, name);
        if (roleRepository.findByName(name).isPresent())
            return "权限名称已经存在";
        else return "true";

    }


    /**
     * 验证菜单编码是否存在
     *
     * @param pCode code
     * @return

     @RequestMapping(value = "/pcode/validate.html")
     @ResponseBody public String validatePCode(@RequestParam(value = "pcode", required = false) Optional<String> pCode) {
     log.info("pCode={}", pCode.get());

     if (!menuRepository.findByCode(pCode.get()).isPresent())
     return "父级菜单编码不存在";
     else return "true";

     }*/

}