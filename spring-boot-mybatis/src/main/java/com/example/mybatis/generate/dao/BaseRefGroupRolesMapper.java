package com.example.mybatis.generate.dao;

import com.example.mybatis.generate.domain.BaseRefGroupRoles;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface BaseRefGroupRolesMapper {
    int deleteByPrimaryKey(@Param("groupId") Long groupId, @Param("roleId") Long roleId);

    int insert(BaseRefGroupRoles record);

    List<BaseRefGroupRoles> selectAll();
}