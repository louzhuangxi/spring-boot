package com.base.spring.controller.validate;

import com.base.spring.repository.RoleRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Description : TODO(验证 Menu 编辑。用户管理在 JqgridUserContorlloer 中)
 * User: h819
 * Date: 14-1-17
 * Time: 下午4:47
 * To change this template use File | Settings | File Templates.
 */
@Controller
@RequestMapping("/validate/ajax/role")
public class JqgridRoleEditValidationController {

    private static Logger logger = LoggerFactory.getLogger(JqgridRoleEditValidationController.class);

    @Autowired
    RoleRepository roleRepository;

    // =================================== user register/login validate property exists  =======================================//

    /**
     * 验证 Role 名称是否存在
     *
     * @param name
     * @return
     */
    @RequestMapping(value = "/name/exist.html")
    @ResponseBody
    public String validateCode(@RequestParam(value = "ids", required = true) String[] ids, //多选时，如果为单选，数组只有一个值
                               @RequestParam(value = "name", required = false) String name) {
        logger.info("ids={} , name={}", ids, name);
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
     logger.info("pCode={}", pCode.get());

     if (!menuRepository.findByCode(pCode.get()).isPresent())
     return "父级菜单编码不存在";
     else return "true";

     }*/

}