/**
 * 表名加上前缀 base_ ，表示这是基础的框架类
 * LocalDateTime jdk 5 才支持
 * <p>
 * 简化 pojo 写法
 * https://projectlombok.org/features/index.html
 * https://blog.codecentric.de/en/2016/05/reducing-boilerplate-code-project-lombok/
 * https://github.com/cbc009/spring-boot-jpa/blob/master/src%2Fmain%2Fjava%2Fcom%2Fchenbaocheng%2Fdomain%2FAdminUser.java
 * https://github.com/mplushnikov/lombok-intellij-plugin
 * <p>
 * 总结：
 * 1. 先在 ide 上安装插件
 * 2. maven 引入依赖
 * --
 * 应用：
 * 1)在类上面标注 getter,setter ，如果同时在属性上面标注，则属性优先级高
 * 2)如果自定义了  getter,setter 方法，会覆盖 lombok 自动生成的
 * 3)
 * -
 *
 * @Setter(AccessLevel.NONE) // 不创建 Setter  方法，反序列化会有问题是会忽略该属性
 * @Getter(AccessLevel.NONE) // 不创建 Getter  方法，序列化输出是会忽略该属性
 * <p>
 * 4)在自动生成的 getter 和 setter 代码时，和有些自定义的代码会有冲突，此时需要修改参数名称来解决
 * 没必要，增加的代码没有影响使用，就是看着好像挺烦还是别把问题复杂化 ????
 * <p>
 * <p>
 * =============================================================================================================================
 * build 模式：
 * 对于参数很多的构造方法，可以灵活的创建构造方法
 * http://www.cnblogs.com/moonz-wu/archive/2011/01/11/1932473.html
 * <p>
 * sql 语句拦截
 * http://www.oschina.net/news/73322/p6spy-2-3-0
 * <p>
 * <p>
 * ===
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
 */
package com.base.spring.domain;