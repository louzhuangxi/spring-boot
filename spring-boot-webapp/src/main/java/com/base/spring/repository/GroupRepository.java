package com.base.spring.repository;

import com.base.spring.domain.GroupEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

/**
 * Description : TODO()
 * User: h819
 * Date: 2015/12/18
 * Time: 12:46
 * To change this template use File | Settings | File Templates.
 */
public interface GroupRepository extends JpaRepository<GroupEntity, Long>, JpaSpecificationExecutor {

    @Query("select e from GroupEntity e where e.name=?1")
    Optional<GroupEntity> findByName(String name);
}