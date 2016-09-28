package com.base.spring.repository;

import com.base.spring.domain.RoleEntity;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

/**
 * Description : TODO()
 * User: h819
 * Date: 2015/12/18
 * Time: 12:46
 * To change this template use File | Settings | File Templates.
 */
public interface RoleRepository extends BaseRepository<RoleEntity, Long> {

    @Query("select e from RoleEntity e where e.name=?1")
    Optional<RoleEntity> findByName(String name);
}