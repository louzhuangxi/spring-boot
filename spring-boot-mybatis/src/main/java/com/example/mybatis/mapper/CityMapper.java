package com.example.mybatis.mapper;

import com.example.mybatis.domain.City;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 * Description : TODO()
 * User: h819
 * Date: 2017/1/24
 * Time: 16:58
 * To change this template use File | Settings | File Templates.
 */

@Mapper // 同 JPA 中 Repository
public interface CityMapper {

    @Select("select * from city where state = #{state}")
    City findByState(@Param("state") String state);

}
