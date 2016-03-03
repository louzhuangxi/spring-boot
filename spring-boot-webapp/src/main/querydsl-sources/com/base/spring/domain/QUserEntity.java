package com.base.spring.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QUserEntity is a Querydsl query type for UserEntity
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QUserEntity extends EntityPathBase<UserEntity> {

    private static final long serialVersionUID = 651543111L;

    public static final QUserEntity userEntity = new QUserEntity("userEntity");

    public final QBaseEntity _super = new QBaseEntity(this);

    public final BooleanPath accountNonExpired = createBoolean("accountNonExpired");

    public final StringPath address = createString("address");

    public final StringPath company = createString("company");

    //inherited
    public final DateTimePath<java.util.Date> createTime = _super.createTime;

    public final StringPath email = createString("email");

    public final BooleanPath emailValid = createBoolean("emailValid");

    //inherited
    public final BooleanPath enabled = _super.enabled;

    //inherited
    public final DateTimePath<java.util.Date> expireDate = _super.expireDate;

    public final StringPath fax = createString("fax");

    public final SetPath<GroupEntity, QGroupEntity> group = this.<GroupEntity, QGroupEntity>createSet("group", GroupEntity.class, QGroupEntity.class, PathInits.DIRECT2);

    //inherited
    public final NumberPath<Long> id = _super.id;

    public final BooleanPath isAccountNonLocked = createBoolean("isAccountNonLocked");

    public final BooleanPath isCredentialsNonExpired = createBoolean("isCredentialsNonExpired");

    public final StringPath loginName = createString("loginName");

    public final StringPath mobile = createString("mobile");

    public final StringPath nickName = createString("nickName");

    //inherited
    public final StringPath operator = _super.operator;

    public final StringPath password = createString("password");

    public final StringPath postcode = createString("postcode");

    public final StringPath qq = createString("qq");

    public final BooleanPath receiveEmailInfo = createBoolean("receiveEmailInfo");

    //inherited
    public final StringPath remark = _super.remark;

    public final SetPath<RoleEntity, QRoleEntity> roles = this.<RoleEntity, QRoleEntity>createSet("roles", RoleEntity.class, QRoleEntity.class, PathInits.DIRECT2);

    public final StringPath telephone = createString("telephone");

    public final StringPath token = createString("token");

    //inherited
    public final DateTimePath<java.util.Date> updateTime = _super.updateTime;

    public final StringPath userName = createString("userName");

    //inherited
    public final BooleanPath valid = _super.valid;

    public final StringPath weixin = createString("weixin");

    public QUserEntity(String variable) {
        super(UserEntity.class, forVariable(variable));
    }

    public QUserEntity(Path<? extends UserEntity> path) {
        super(path.getType(), path.getMetadata());
    }

    public QUserEntity(PathMetadata metadata) {
        super(UserEntity.class, metadata);
    }

}

