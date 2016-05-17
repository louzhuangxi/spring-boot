/**
 * 表名加上前缀 base_ ，表示这是基础的框架类
 * jpa2.1不支持 jdk8 的 LocalDate and LocalDateTime
 * http://www.thoughts-on-java.org/persist-localdate-localdatetime-jpa/
 
 简化 pojo 写法
 https://projectlombok.org/features/index.html
 https://blog.codecentric.de/en/2016/05/reducing-boilerplate-code-project-lombok/
 https://github.com/cbc009/spring-boot-jpa/blob/master/src%2Fmain%2Fjava%2Fcom%2Fchenbaocheng%2Fdomain%2FAdminUser.java
 https://github.com/mplushnikov/lombok-intellij-plugin

 ===
 builder 模式：
 对于参数很多的构造方法，可以灵活的创建构造方法
 http://www.cnblogs.com/moonz-wu/archive/2011/01/11/1932473.html

 sql 语句拦截
 http://www.oschina.net/news/73322/p6spy-2-3-0
 */
package com.base.spring.domain;