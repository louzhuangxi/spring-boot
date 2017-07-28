/**
 * 表名加上前缀 base_ ，表示这是基础的框架类
 * LocalDateTime jdk 8 才支持
 * =============================================================================================================================
 * 简化 pojo 写法
 * =============================================================================================================================
 * https://projectlombok.org/features/index.html
 * https://blog.codecentric.de/en/2016/05/reducing-boilerplate-code-project-lombok/
 * https://github.com/cbc009/spring-boot-jpa/blob/master/src%2Fmain%2Fjava%2Fcom%2Fchenbaocheng%2Fdomain%2FAdminUser.java
 * https://github.com/mplushnikov/lombok-intellij-plugin
 * <p>
 * 总结：
 * 1. 先在 idea 上安装插件
 * 2. maven 引入依赖
 * --
 * 应用：
 * 1)在类上面标注 getter,setter ，如果同时在属性上面标注，则属性优先级高
 * 2)如果自定义了  getter,setter 方法，会覆盖 lombok 自动生成的
 * 3)
 *
 * @Setter(AccessLevel.NONE) // 不创建 Setter  方法，此时反序列化会有问题，会忽略该属性
 * @Getter(AccessLevel.NONE) // 不创建 Getter  方法，序列化输出是会忽略该属性
 * -
 * 4)
 * 在自动生成的 getter 和 setter 代码时，和有些自定义的代码会有冲突，此时需要修改参数名称来解决
 * 没必要，增加的代码没有影响使用，就是看着好像挺烦还是别把问题复杂化 ????
 * -
 * 5)
 * 注意 idea 自动生成工具，对 boolean 变量自动生成 get/set 方法时候的方法名称问题
 * 例如：
 * private Boolean isA;
 * public Boolean getA() {
 * return isA;
 * }
 * public void setA(Boolean a) {
 * isA = a;
 * }
 * <p>
 * 应该是 getIsA() 和 setIsA()
 * 这回影响到另外一些 bean 工具读取和设置属性问题
 * bean 工具是完全按照 Bean 规范，根据属性名称 isA ，通过 getIsA() 和 setIsA() 读取设设置属性的，
 * getA() /setA() 无法辨认，所以不要用这中自动生成的语句。
 * （目前发现 idea 有个毛病）
 * =============================================================================================================================
 * build 模式
 * =============================================================================================================================
 * 对于参数很多的构造方法，可以灵活的创建构造方法
 * http://www.cnblogs.com/moonz-wu/archive/2011/01/11/1932473.html
 * <p>
 * sql 语句拦截
 * http://www.oschina.net/news/73322/p6spy-2-3-0
 * -
 * -
 * =============================================================================================================================
 * Entity 多层次级联，fetch = FetchType.LAZY 方式加载问题
 * =============================================================================================================================
 * 多个层次级联例子：
 * UserEntity ->(级联) RoleEntity ->(级联) TreeEntity  ，三者之间的级联关系均为 fetch = FetchType.LAZY
 * 1. 在方法中加上 @Transactional ，可以级联查询一层的级联关系
 * 如：
 * 通过 UserEntity 可以直接得到 RoleEntity
 * 通过 RoleEntity 可以直接得到 TreeEntity
 * 但无法级联查询到两层及以上层次
 * UserEntity 无法直接得到  TreeEntity
 * -
 * 2. 级联查询两层及以上层次级联关系方法
 * 1）UserEntity 可以直接得到 RoleEntity
 * 2）通过 RoleRepository(RoleEntity) 获取 TreeEntity (再次通过步骤一得到的 RoleEntity 进行查询，而不是自动级联加载)
 * 3）例子见 RoleRepository ,  UserService.getAllMenuByUser(UserEntity user) 方法
 * <p>
 * <p>
 * =============================================================================================================================
 * -
 * spring boot 1.5.4 问题
 * javax.persistence.Transient
 * 标注在变量上，在使用 repository 查找返回 entity 时，提示无法对应变量 ccsName
 * @Transient private String ccsName;
 * -
 * 标注在 getter 方法上时，可以
 * @Transient //临时
 * public String getCcsName() {
 * return ccsName;  }
 * --
 * 日后确认是什么问题
 *=============================================================================================================================
 * <p>
 * <p>
 * ===
 */
package com.base.spring.domain;