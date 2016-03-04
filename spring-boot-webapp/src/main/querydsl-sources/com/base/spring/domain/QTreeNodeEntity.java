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
    public final DateTimePath<java.util.Date> createTime = _super.createTime;

    public final StringPath css = createString("css");

    //inherited
    public final BooleanPath enabled = _super.enabled;

    //inherited
    public final DateTimePath<java.util.Date> expireDate = _super.expireDate;

    //inherited
    public final NumberPath<Long> id = _super.id;

    public final NumberPath<Integer> index = createNumber("index", Integer.class);

    public final BooleanPath isParent = createBoolean("isParent");

    public final NumberPath<Integer> level = createNumber("level", Integer.class);

    public final StringPath name = createString("name");

    //inherited
    public final StringPath operator = _super.operator;

    public final QTreeNodeEntity parent;

    public final SetPath<PrivilegeEntity, QPrivilegeEntity> privileges = this.<PrivilegeEntity, QPrivilegeEntity>createSet("privileges", PrivilegeEntity.class, QPrivilegeEntity.class, PathInits.DIRECT2);

    //inherited
    public final StringPath remark = _super.remark;

    public final StringPath target = createString("target");

    public final EnumPath<TreeNodeType> type = createEnum("type", TreeNodeType.class);

    //inherited
    public final DateTimePath<java.util.Date> updateTime = _super.updateTime;

    public final StringPath url = createString("url");

    //inherited
    public final BooleanPath valid = _super.valid;

    public QTreeNodeEntity(String variable) {
        this(TreeNodeEntity.class, forVariable(variable), INITS);
    }

    public QTreeNodeEntity(Path<? extends TreeNodeEntity> path) {
        this(path.getType(), path.getMetadata(), path.getMetadata().isRoot() ? INITS : PathInits.DEFAULT);
    }

    public QTreeNodeEntity(PathMetadata metadata) {
        this(metadata, metadata.isRoot() ? INITS : PathInits.DEFAULT);
    }

    public QTreeNodeEntity(PathMetadata metadata, PathInits inits) {
        this(TreeNodeEntity.class, metadata, inits);
    }

    public QTreeNodeEntity(Class<? extends TreeNodeEntity> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.parent = inits.isInitialized("parent") ? new QTreeNodeEntity(forProperty("parent"), inits.get("parent")) : null;
    }

}

