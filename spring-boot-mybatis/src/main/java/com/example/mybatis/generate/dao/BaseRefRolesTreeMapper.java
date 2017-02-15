package com.example.mybatis.generate.dao;

import com.example.mybatis.generate.domain.BaseRefRolesTree;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface BaseRefRolesTreeMapper {
    int deleteByPrimaryKey(@Param("roleId") Long roleId, @Param("treeId") Long treeId);

    int insert(BaseRefRolesTree record);

    List<BaseRefRolesTree> selectAll();
}