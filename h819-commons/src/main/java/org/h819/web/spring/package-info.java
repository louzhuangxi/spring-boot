/**
 * Description : TODO()
 * User: h819
 * Date: 2017/6/8
 * Time: 17:08
 * To change this template use File | Settings | File Templates.
 * ------
 * native 查询用 jdbcTemplate ，不用 jpa
 * ---
 * 对于老系统，多表之间无法用 hibernate 建立关联，如果需要关联查询时，通常用 native 语句 做级联查询。
 * jpa 执行 native 语句，返回的结果是 Object[] ，无法返回 map
 * jdbcTemplate 查询返回的可以是 map ，key 对应的是 列名，value 对应的是值。
 */
package org.h819.web.spring;