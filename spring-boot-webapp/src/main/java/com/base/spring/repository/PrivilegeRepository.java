package com.base.spring.repository;

import com.base.spring.domain.PrivilegeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

/**
 * Description : TODO()
 * User: h819
 * Date: 2015/12/18
 * Time: 12:46
 * To change this template use File | Settings | File Templates.
 */
public interface PrivilegeRepository extends JpaRepository<PrivilegeEntity, Long>, JpaSpecificationExecutor {

    @Query("select e from PrivilegeEntity e where e.name=?1")
    Optional<PrivilegeEntity> findByName(String name);

    @Modifying
    @Query("delete from PrivilegeEntity e where e.name=?1")
    void deleteByName(String name);
}