package org.examples.spring;

/**
 * Description : TODO( spring web 最佳实践)
 * User: h819
 * Date: 14-5-3
 * Time: 下午9:14                                  s
 * To change this template use File | Settings | File Templates.
 * <p/>
 * Package showing a simple repository interface to use basic query method execution functionality.
 */

//需要特别注意的是:

// 0.  @Column(name = "name") 可以标注在字段上，也可以标注在 getter 方法上，但不能在一个 Entity 中用两种标注方法，否则会出现异常

//  examples 中演示的例子，均用 mysql ，数据源是 transactionManagerMySQL ，否则在扫描包结构的时候，如果有 oracle 的 entity，会报错
//
//  1. 动态参数查询 : org.springapp.jpa.examples.service.DynamicSearchExampleService.java ,  org.springapp.jpa.examples.web.DynamicSearchExampleController.java
//
//  2. ManyToMany ，对关联关系增加属性 :  org.springapp.jpa.examples.service.BaseService.java
//
//  3. jpql 各种查询语句写法 :  org.springapp.jpa.examples.repository.TreeEntityRepository
//
//  4. OneToMany 中，增加子类的方法
//
//  5. @EntityGraph
//
//  6. 注意 SearchJgridController 中关于返回值的描述。 返回值 @ResponseBody 和 null 均不能通过 Model 传参。
//     只有 一个指定的 jsp 页面 或 Controller 才可以传参。
//
//  7. 增加用户注册模块，演示事件使用