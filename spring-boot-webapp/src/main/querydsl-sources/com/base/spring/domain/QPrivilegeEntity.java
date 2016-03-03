package com.base.spring.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QPrivilegeEntity is a Querydsl query type for PrivilegeEntity
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QPrivilegeEntity extends EntityPathBase<PrivilegeEntity> {

    private static final long serialVersionUID = -1390166309L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QPrivilegeEntity privilegeEntity = new QPrivilegeEntity("privilegeEntity");

    public final QBaseEntity _super = new QBaseEntity(this);

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

    public final QPageElementEntity pageElement;

    //inherited
    public final StringPath remark = _super.remark;

    public final CollectionPath<RoleEntity, QRoleEntity> roles = this.<RoleEntity, QRoleEntity>createCollection("roles", RoleEntity.class, QRoleEntity.class, PathInits.DIRECT2);

    public final CollectionPath<TreeNodeEntity, QTreeNodeEntity> treeNodes = this.<TreeNodeEntity, QTreeNodeEntity>createCollection("treeNodes", TreeNodeEntity.class, QTreeNodeEntity.class, PathInits.DIRECT2);

    public final EnumPath<PrivilegeType> type = createEnum("type", PrivilegeType.class);

    //inherited
    public final DateTimePath<java.util.Date> updateTime = _super.updateTime;

    //inherited
    public final BooleanPath valid = _super.valid;

    public QPrivilegeEntity(String variable) {
        this(PrivilegeEntity.class, forVariable(variable), INITS);
    }

    public QPrivilegeEntity(Path<? extends PrivilegeEntity> path) {
        this(path.getType(), path.getMetadata(), path.getMetadata().isRoot() ? INITS : PathInits.DEFAULT);
    }

    public QPrivilegeEntity(PathMetadata metadata) {
        this(metadata, metadata.isRoot() ? INITS : PathInits.DEFAULT);
    }

    public QPrivilegeEntity(PathMetadata metadata, PathInits inits) {
        this(PrivilegeEntity.class, metadata, inits);
    }

    public QPrivilegeEntity(Class<? extends PrivilegeEntity> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.pageElement = inits.isInitialized("pageElement") ? new QPageElementEntity(forProperty("pageElement"), inits.get("pageElement")) : null;
    }

}

