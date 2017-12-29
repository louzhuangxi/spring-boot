/**
 * Description : TODO()
 * User: h819
 * Date: 2017/12/29
 * Time: 12:12
 * To change this template use File | Settings | File Templates.
 */
package com.base.spring.repository;
/**
 * 如果结果返回的不是一个 entity ，可以用 Object[] 包装
 *
 * @Query("select department_id , count(*) from TreeEntity e where e.name=?1")
 * List<Object[]> findByName(String name);
 */
