/**
 * Description : TODO()
 * User: h819
 * Date: 2017/6/8
 * Time: 17:08
 * To change this template use File | Settings | File Templates.
 * ------
 * native 查询用 jdbcTemplate ，不用 jpa
 * ---
 * 对于老系统，多表之间无法用 hibernate 建立关联，如果需要关联查询时，通常用 native 语句做级联查询(left join)。
 * native 语句查询有两种方式：
 * 1. jpa 执行 native 语句 : 返回的结果是 Object[] ，可以返回 Bean , 但无法返回 map
 * 2. jdbcTemplate 执行 native 语句：可以返回 Bean ，也可以返回 map ，key 对应的是 列名，value 对应的是值。
 * map 和 Bean 如何选用：
 * 如果返回的结果用 Bean 比较简单，那么在
 * jpa entity 中，字段加上  @Transient //临时即可，jdbcTemplate 直接普通 bean 就可以
 * 如果不想制作复杂的 Bean ，可以考虑用 jdbcTemplate 返回 map
 * 相对于复杂的(如分页等) native sql ， jdbcTemplate 已经做好封装工具类，直接使用即可，可不必使用 jpa
 */
package org.h819.web.spring;