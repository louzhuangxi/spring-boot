package com.base.spring.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QRoleEntity is a Querydsl query type for RoleEntity
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QRoleEntity extends EntityPathBase<RoleEntity> {

    private static final long serialVersionUID = 543091378L;

    public static final QRoleEntity roleEntity = new QRoleEntity("roleEntity");

    public final QBaseEntity _super = new QBaseEntity(this);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdDate = _super.createdDate;

    //inherited
    public final BooleanPath enabled = _super.enabled;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> expireDate = _super.expireDate;

    public final ListPath<GroupEntity, QGroupEntity> group = this.<GroupEntity, QGroupEntity>createList("group", GroupEntity.class, QGroupEntity.class, PathInits.DIRECT2);

    //inherited
    public final NumberPath<Long> id = _super.id;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> modifiedDate = _super.modifiedDate;

    public final StringPath name = createString("name");

    //inherited
    public final StringPath operator = _super.operator;

    //inherited
    public final StringPath remark = _super.remark;

    public final ListPath<TreeNodeEntity, QTreeNodeEntity> treeNodes = this.<TreeNodeEntity, QTreeNodeEntity>createList("treeNodes", TreeNodeEntity.class, QTreeNodeEntity.class, PathInits.DIRECT2);

    public final ListPath<UserEntity, QUserEntity> users = this.<UserEntity, QUserEntity>createList("users", UserEntity.class, QUserEntity.class, PathInits.DIRECT2);

    //inherited
    public final BooleanPath valid = _super.valid;

    public QRoleEntity(String variable) {
        super(RoleEntity.class, forVariable(variable));
    }

    public QRoleEntity(Path<? extends RoleEntity> path) {
        super(path.getType(), path.getMetadata());
    }

    public QRoleEntity(PathMetadata metadata) {
        super(RoleEntity.class, metadata);
    }

}

