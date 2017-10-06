package com.example.demo;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * Description : TODO()
 * User: h819
 * Date: 2017/1/24
 * Time: 17:19
 * To change this template use File | Settings | File Templates.
 */
@RestController
@Slf4j
public class Controller {

  //  private static final Logger logger = LoggerFactory.getLogger(Controller.class);

    @RequestMapping("/")
    public String index(HttpServletRequest request) {
        System.out.println("it is logger.");
        log.info("it is logger .");
        return "Hello from log";
    }


}
