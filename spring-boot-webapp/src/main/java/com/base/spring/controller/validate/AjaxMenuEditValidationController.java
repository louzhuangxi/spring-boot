package com.base.spring.controller.validate;

import com.base.spring.repository.TreeNodeRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Description : TODO(验证 Menu 编辑。用户管理在 JqgridUserContorlloer 中)
 * User: h819
 * Date: 14-1-17
 * Time: 下午4:47
 * To change this template use File | Settings | File Templates.
 */
@Controller
@RequestMapping("/validate/ajax/menu")
public class AjaxMenuEditValidationController {

    private static Logger logger = LoggerFactory.getLogger(AjaxMenuEditValidationController.class);

    @Autowired
    TreeNodeRepository menuRepository;

    // =================================== user register/login validate property exists  =======================================//

    /**
     * 验证菜单编码是否存在
     *
     * @param code code
     * @return

    @RequestMapping(value = "/code/validate.html")
    @ResponseBody
    public String validateCode(@RequestParam(value = "code", required = false) Optional<String> code) {
        logger.info("code={}", code.get());

        if (menuRepository.findByCode(code.get()).isPresent())
            return "菜单编码已经存在";
        else return "true";

    }*/


    /**
     * 验证菜单编码是否存在
     *
     * @param pCode code
     * @return

    @RequestMapping(value = "/pcode/validate.html")
    @ResponseBody
    public String validatePCode(@RequestParam(value = "pcode", required = false) Optional<String> pCode) {
        logger.info("pCode={}", pCode.get());

        if (!menuRepository.findByCode(pCode.get()).isPresent())
            return "父级菜单编码不存在";
        else return "true";

    }*/

}