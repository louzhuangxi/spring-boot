package com.example.mybatis.generate.dao;

import com.example.mybatis.generate.domain.BaseRefUserGroup;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface BaseRefUserGroupMapper {
    int deleteByPrimaryKey(@Param("groupId") Long groupId, @Param("userId") Long userId);

    int insert(BaseRefUserGroup record);

    List<BaseRefUserGroup> selectAll();
}