/**
 * Description : TODO()
 * User: h819
 * Date: 2017/1/24
 * Time: 17:23
 * To change this template use File | Settings | File Templates.
 * ---
 * 迎合中国人口味，使用 MyBaties , 而不是 Hibernate，抛弃 PO , VO 之争
 * ---
 * map generator
 * 动态生成 sql
 * 多级缓存的实现、延迟加载、动态SQL、MyBatis Generator
 * ---
 * <p>
 * 需要解决的常见问题：
 * 1. Repository 式的增删改查 ( 通用 Dao 问题)
 * 2. 动态参数查询
 * 3. 分页
 * 4. 关联查询 one to many , lazy ，怎么映射到关联对象或对象集合?
 * 5. JPA 中其他的特性
 * <p>
 * =====
 * 和 spring jdbc 直接写 sql 区别 ？  有什么好处?
 * spring jdbc 也能，自己手工 RowMapper 各中查询结果 , mybatis 自动 maper
 * =====
 * https://my.oschina.net/wangxincj/blog/841299
 *
 */
package com.example.mybatis;
