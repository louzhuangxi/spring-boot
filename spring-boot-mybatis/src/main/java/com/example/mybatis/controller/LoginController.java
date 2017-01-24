package com.example.mybatis.controller;

import com.example.mybatis.domain.City;
import com.example.mybatis.mapper.CityMapper;
import org.springframework.beans.factory.annotation.Autowired;
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
public class LoginController {

    @Autowired
    private CityMapper cityMapper;

    @RequestMapping("/")
    public City index(HttpServletRequest request) {

        return cityMapper.findByState("CA");
    }


}
