package org.h819.web.spring.jpa.querydsl;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Predicate;

/**
 * Description : TODO()
 * User: h819
 * Date: 2016-09-27
 * Time: 23:15
 * To change this template use File | Settings | File Templates.
 */
public class WhereClauseBuilder  {

    private BooleanBuilder delegate;

    public WhereClauseBuilder() {
        this.delegate = new BooleanBuilder();
    }

    public WhereClauseBuilder(Predicate pPredicate) {
        this.delegate = new BooleanBuilder(pPredicate);
    }

    public WhereClauseBuilder and(Predicate right) {
        return new WhereClauseBuilder(delegate.and(right));
    }


}


