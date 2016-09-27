package com.base.spring.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QTreeNodeEntity is a Querydsl query type for TreeNodeEntity
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QTreeNodeEntity extends EntityPathBase<TreeNodeEntity> {

    private static final long serialVersionUID = -1901814532L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QTreeNodeEntity treeNodeEntity = new QTreeNodeEntity("treeNodeEntity");

    public final QBaseEntity _super = new QBaseEntity(this);

    public final ListPath<TreeNodeEntity, QTreeNodeEntity> children = this.<TreeNodeEntity, QTreeNodeEntity>createList("children", TreeNodeEntity.class, QTreeNodeEntity.class, PathInits.DIRECT2);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdDate = _super.createdDate;

    public final StringPath css = createString("css");

    //inherited
    public final BooleanPath enabled = _super.enabled;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> expireDate = _super.expireDate;

    //inherited
    public final NumberPath<Long> id = _super.id;

    public final NumberPath<Integer> index = createNumber("index", Integer.class);

    public final BooleanPath isParentNode = createBoolean("isParentNode");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> modifiedDate = _super.modifiedDate;

    public final StringPath name = createString("name");

    //inherited
    public final StringPath operator = _super.operator;

    public final QTreeNodeEntity parent;

    //inherited
    public final StringPath remark = _super.remark;

    public final ListPath<RoleEntity, QRoleEntity> roles = this.<RoleEntity, QRoleEntity>createList("roles", RoleEntity.class, QRoleEntity.class, PathInits.DIRECT2);

    public final StringPath target = createString("target");

    public final EnumPath<TreeNodeType> type = createEnum("type", TreeNodeType.class);

    public final StringPath url = createString("url");

    //inherited
    public final BooleanPath valid = _super.valid;

    public QTreeNodeEntity(String variable) {
        this(TreeNodeEntity.class, forVariable(variable), INITS);
    }

    public QTreeNodeEntity(Path<? extends TreeNodeEntity> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QTreeNodeEntity(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QTreeNodeEntity(PathMetadata metadata, PathInits inits) {
        this(TreeNodeEntity.class, metadata, inits);
    }

    public QTreeNodeEntity(Class<? extends TreeNodeEntity> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.parent = inits.isInitialized("parent") ? new QTreeNodeEntity(forProperty("parent"), inits.get("parent")) : null;
    }

}

