package com.base.spring.repository;

import com.base.spring.domain.GroupEntity;
import com.base.spring.domain.UserEntity;
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
public interface GroupRepository extends BaseRepository<GroupEntity, Long> {

    @Query("select e from GroupEntity e where e.name=?1")
    Optional<GroupEntity> findByName(String name);

    @Query("select e from GroupEntity e where e.id in ?1")
    List<GroupEntity> findByIdIn(Collection<Long> ids);
}