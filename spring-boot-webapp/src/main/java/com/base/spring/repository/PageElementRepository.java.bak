package com.base.spring.repository;

import com.base.spring.domain.PageElementEntity;
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
public interface PageElementRepository extends JpaRepository<PageElementEntity, Long>, JpaSpecificationExecutor {

    @Query("select e from PageElementEntity e where e.name=?1")
    Optional<PageElementEntity> findByName(String name);
}