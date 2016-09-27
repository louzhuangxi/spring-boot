package org.h819.web.spring.jpa.querydsl;

import com.querydsl.core.types.dsl.BooleanExpression;

/**
 * Description : TODO()
 * User: h819
 * Date: 2016-09-27
 * Time: 23:18
 * To change this template use File | Settings | File Templates.
 */
@FunctionalInterface
public interface LazyBooleanExpression {

    BooleanExpression get();
}
