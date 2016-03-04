package com.base.spring.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QPageElementEntity is a Querydsl query type for PageElementEntity
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QPageElementEntity extends EntityPathBase<PageElementEntity> {

    private static final long serialVersionUID = 81941207L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QPageElementEntity pageElementEntity = new QPageElementEntity("pageElementEntity");

    public final QBaseEntity _super = new QBaseEntity(this);

    public final StringPath code = createString("code");

    //inherited
    public final DateTimePath<java.util.Date> createTime = _super.createTime;

    //inherited
    public final BooleanPath enabled = _super.enabled;

    //inherited
    public final DateTimePath<java.util.Date> expireDate = _super.expireDate;

    //inherited
    public final NumberPath<Long> id = _super.id;

    public final StringPath name = createString("name");

    //inherited
    public final StringPath operator = _super.operator;

    public final QPrivilegeEntity privilege;

    //inherited
    public final StringPath remark = _super.remark;

    //inherited
    public final DateTimePath<java.util.Date> updateTime = _super.updateTime;

    //inherited
    public final BooleanPath valid = _super.valid;

    public QPageElementEntity(String variable) {
        this(PageElementEntity.class, forVariable(variable), INITS);
    }

    public QPageElementEntity(Path<? extends PageElementEntity> path) {
        this(path.getType(), path.getMetadata(), path.getMetadata().isRoot() ? INITS : PathInits.DEFAULT);
    }

    public QPageElementEntity(PathMetadata metadata) {
        this(metadata, metadata.isRoot() ? INITS : PathInits.DEFAULT);
    }

    public QPageElementEntity(PathMetadata metadata, PathInits inits) {
        this(PageElementEntity.class, metadata, inits);
    }

    public QPageElementEntity(Class<? extends PageElementEntity> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.privilege = inits.isInitialized("privilege") ? new QPrivilegeEntity(forProperty("privilege"), inits.get("privilege")) : null;
    }

}

