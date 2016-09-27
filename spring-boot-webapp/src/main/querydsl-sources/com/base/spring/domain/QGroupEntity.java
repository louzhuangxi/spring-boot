package com.base.spring.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QGroupEntity is a Querydsl query type for GroupEntity
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QGroupEntity extends EntityPathBase<GroupEntity> {

    private static final long serialVersionUID = 969717673L;

    public static final QGroupEntity groupEntity = new QGroupEntity("groupEntity");

    public final QBaseEntity _super = new QBaseEntity(this);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdDate = _super.createdDate;

    //inherited
    public final BooleanPath enabled = _super.enabled;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> expireDate = _super.expireDate;

    //inherited
    public final NumberPath<Long> id = _super.id;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> modifiedDate = _super.modifiedDate;

    public final StringPath name = createString("name");

    //inherited
    public final StringPath operator = _super.operator;

    //inherited
    public final StringPath remark = _super.remark;

    public final ListPath<RoleEntity, QRoleEntity> roles = this.<RoleEntity, QRoleEntity>createList("roles", RoleEntity.class, QRoleEntity.class, PathInits.DIRECT2);

    public final ListPath<UserEntity, QUserEntity> users = this.<UserEntity, QUserEntity>createList("users", UserEntity.class, QUserEntity.class, PathInits.DIRECT2);

    //inherited
    public final BooleanPath valid = _super.valid;

    public QGroupEntity(String variable) {
        super(GroupEntity.class, forVariable(variable));
    }

    public QGroupEntity(Path<? extends GroupEntity> path) {
        super(path.getType(), path.getMetadata());
    }

    public QGroupEntity(PathMetadata metadata) {
        super(GroupEntity.class, metadata);
    }

}

