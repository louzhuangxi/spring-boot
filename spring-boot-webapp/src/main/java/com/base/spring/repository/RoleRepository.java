package com.base.spring.repository;

import com.base.spring.domain.RoleEntity;
import com.base.spring.domain.TreeEntity;
import org.springframework.data.jpa.repository.Query;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

/**
 * Description : TODO()
 * User: h819
 * Date: 2015/12/18
 * Time: 12:46
 * To change this template use File | Settings | File Templates.
 */
public interface RoleRepository extends BaseRepository<RoleEntity, Long> {

    /**
     * @param name 唯一
     * @return
     */
    @Query("select e from RoleEntity e where e.name=?1")
    Optional<RoleEntity> findByName(String name);

    @Query("select e from RoleEntity e where e.id in ?1")
    List<RoleEntity> findByIdIn(Collection<Long> ids);

    /**
     * 为解决三层级联查询设置
     *
     * @param roleId
     * @return
     */
    @Query("select e.treeNodes from RoleEntity e where e.id = ?1")
    List<TreeEntity> findTreeEntitiesById(Long roleId);

}