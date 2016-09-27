package com.base.spring.repository.querydsl;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;

import java.io.Serializable;

/**
 * Description : TODO()
 * User: h819
 * Date: 2016/9/27
 * Time: 16:17
 * To change this template use File | Settings | File Templates.
 */
public interface BaseRepository<T, ID extends Serializable> extends JpaRepository<T, ID>, JpaSpecificationExecutor, QueryDslPredicateExecutor<T> {
}
