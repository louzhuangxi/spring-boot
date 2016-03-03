package com.base.spring.repository.querydsl;

import com.base.spring.domain.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * Description : TODO()  官方的例子，还继承了 QuerydslBinderCustomizer 我没用上，不知道怎么用?
 *
 * spring  data commons 1.12.x 以后，才支持 querydsl 4 ，等吧
 * 从 spring boot jpa 依赖关系中，可以看到 spring data commons 的依赖
 * http://docs.spring.io/spring-boot/docs/current/reference/htmlsingle/ 文档末尾也有版本
 * spring boot 1.4 以后
 * User: h819
 * Date: 2016-02-27
 * Time: 10:45
 * To change this template use File | Settings | File Templates.    QueryDslPredicateExecutor<UserEntity>, QuerydslBinderCustomizer<QUserEntity>
 */
//public interface QUserRepository extends JpaRepository<UserEntity, Long>, JpaSpecificationExecutor, QueryDslPredicateExecutor<UserEntity> {
//
//}

public interface QUserRepository extends JpaRepository<UserEntity, Long>, JpaSpecificationExecutor {

}


//https://github.com/spring-projects/spring-data-examples/tree/master/web/querydsl/src
//http://www.petrikainulainen.net/programming/spring-framework/spring-data-jpa-tutorial-part-five-querydsl/