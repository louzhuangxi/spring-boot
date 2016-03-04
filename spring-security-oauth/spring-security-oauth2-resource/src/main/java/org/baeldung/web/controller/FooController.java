package org.baeldung.web.controller;

import org.baeldung.web.dto.Foo;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import static org.apache.commons.lang3.RandomStringUtils.randomAlphabetic;
import static org.apache.commons.lang3.RandomStringUtils.randomNumeric;

@Controller
public class FooController {

    public FooController() {
        super();
    }


    // @PreAuthorize ，需要在 AuthorizationServer 处配置 :
    //  .authorities("TYPE2_USER") 和   .scopes("read")
    // @PreAuthorize("#oauth2.hasScope('read')")
    @PreAuthorize("#oauth2.hasScope('read') and hasRole('ROLE_USER')")
    @RequestMapping(method = RequestMethod.GET, value = "/foos/{id}")
    @ResponseBody
    public Foo findById(@PathVariable final long id) {
        return new Foo(Long.parseLong(randomNumeric(2)), randomAlphabetic(4));
    }

    // 完成 jpa 项目后，在此处添加对外开放的的 api

}
