package com.example.mybatis.generate.dao;

import com.example.mybatis.generate.domain.BaseRefUsersRoles;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface BaseRefUsersRolesMapper {
    int deleteByPrimaryKey(@Param("roleId") Long roleId, @Param("userId") Long userId);

    int insert(BaseRefUsersRoles record);

    List<BaseRefUsersRoles> selectAll();
}