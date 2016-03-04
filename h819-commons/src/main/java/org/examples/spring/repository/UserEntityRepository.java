package org.examples.spring.repository;

import org.examples.spring.domain.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;


public interface UserEntityRepository extends JpaRepository<UserEntity, Long>, JpaSpecificationExecutor {

}
