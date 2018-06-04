package com.bootdo.system.controller;

import com.bootdo.common.controller.BaseController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 自建的测试类 by JH
 * @ 2018.05.28
 */
@RequestMapping("/")
@Controller
public class TestController extends BaseController {
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	private String prefix="system/user"  ;

	@GetMapping("my_user_tree")
	String treeView() {
		return  prefix + "/my_userTree";
	}
}
