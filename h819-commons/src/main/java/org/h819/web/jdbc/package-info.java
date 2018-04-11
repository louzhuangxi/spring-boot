/**
 * Description : TODO()
 * User: h819
 * Date: 2018/4/11
 * Time: 11:13
 * ---
 * apache commons dbutils 工具类，无 spring 环境
 */
package org.h819.web.jdbc;
/**
 * Dbutils的一些注意事项：
 * 1、DBUtils是JDBC的简单封装，可以和JDBC混合使用。
 * -
 * 2、DBUtils对结果集自动封装为JavaBean是有着苛刻要求的：必须满足JavaBean的规范，其次Bean的getter与setter方法的名字与结果集的列名一一对应，而不要求JavaBean的私有成员与表结果集列名一一对应。
 * 比如：
 * person表中有个字段叫：address，那么对应的JavaBean的Person类中必须有getAddress和setAddress两个方法，而Person类中可以将address属性命名为add，这是没问题的。
 * -
 * 3、DBUtils可以将结果集封装为各种类型，主要有：Bean/List<Bean>，Map/List<Map>/Map<Map>，数组/List<数组>，列/List<列>，这些类型。
 * 对于Map<Map>的类型使用KeyedHandler作为结果集处理器，内层的Map是“列名-值"对，外层的Map是“主键-内层Map的引用”，但此处的主键不一定就是数据库的主键，可以随意指定
 * -
 * 4、DBUtils执行插入操作的时候，无法返回自增主键，这是一个很严重的问题，当然不能怪DBUtils，可以通过变通的方法来实现，比如在MySQL中，执行完了一个插入SQL后，接着执行SELECT LAST_INSERT_ID()语句，就可以获取到自增主键。
 * -
 * 5、DBUtils的性能和JDBC性能是一样，测试过程中没发现性能损失，拥有了很高性能的同时，而不失JDBC的灵活性。
 */