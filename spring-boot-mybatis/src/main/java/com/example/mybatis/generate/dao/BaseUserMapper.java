package com.example.mybatis.generate.dao;

import com.example.mybatis.generate.domain.BaseUser;
import java.util.List;

public interface BaseUserMapper {
    int deleteByPrimaryKey(Long id);

    int insert(BaseUser record);

    BaseUser selectByPrimaryKey(Long id);

    List<BaseUser> selectAll();

    int updateByPrimaryKey(BaseUser record);
}