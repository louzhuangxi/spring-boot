package com.base.spring.repository;

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
public interface UserRepository extends BaseRepository<UserEntity, Long> {

    /**
     * loginName 唯一
     *
     * @param loginName
     * @return
     */
    @Query("select e from UserEntity e where e.loginName=?1")
    Optional<UserEntity> findByLoginName(String loginName);

    /**
     * email 唯一
     *
     * @param email
     * @return
     */
    @Query("select e from UserEntity e where e.email=?1")
    Optional<UserEntity> findOneByEmail(String email);


    @Query("select e from UserEntity e where e.id in ?1")
    List<UserEntity> findByIdIn(Collection<Long> ids);

}