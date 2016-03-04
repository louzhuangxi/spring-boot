package com.base.spring.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;


/**
 * QAbstractMySQLEntity is a Querydsl query type for AbstractMySQLEntity
 */
@Generated("com.querydsl.codegen.SupertypeSerializer")
public class QAbstractMySQLEntity extends EntityPathBase<AbstractMySQLEntity> {

    private static final long serialVersionUID = 1904381674L;

    public static final QAbstractMySQLEntity abstractMySQLEntity = new QAbstractMySQLEntity("abstractMySQLEntity");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public QAbstractMySQLEntity(String variable) {
        super(AbstractMySQLEntity.class, forVariable(variable));
    }

    public QAbstractMySQLEntity(Path<? extends AbstractMySQLEntity> path) {
        super(path.getType(), path.getMetadata());
    }

    public QAbstractMySQLEntity(PathMetadata metadata) {
        super(AbstractMySQLEntity.class, metadata);
    }

}

