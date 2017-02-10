package com.example.mybatis.generate.dao;

import com.example.mybatis.generate.domain.BaseTree;
import java.util.List;

public interface BaseTreeMapper {
    int deleteByPrimaryKey(Long id);

    int insert(BaseTree record);

    BaseTree selectByPrimaryKey(Long id);

    List<BaseTree> selectAll();

    int updateByPrimaryKey(BaseTree record);
}