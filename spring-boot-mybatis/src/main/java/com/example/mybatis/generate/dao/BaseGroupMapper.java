package com.example.mybatis.generate.dao;

import com.example.mybatis.generate.domain.BaseGroup;
import java.util.List;

public interface BaseGroupMapper {
    int deleteByPrimaryKey(Long id);

    int insert(BaseGroup record);

    BaseGroup selectByPrimaryKey(Long id);

    List<BaseGroup> selectAll();

    int updateByPrimaryKey(BaseGroup record);
}