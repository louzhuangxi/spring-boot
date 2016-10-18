package org.examples.spring.domain.manytomany;

/**
 * Description : TODO( many to many 实际中不使用，用 one to many 和关系表的方式实现)
 * User: h819
 * Date: 14-5-3
 * Time: 下午9:14                                  s
 * To change this template use File | Settings | File Templates.
 * 使用 one to many 和 关系表的方式实现
 */

// 需要用 真正的 Class 文件做注释，否则自动格式化的时候，会合并所有 /** */ 的内容
@Deprecated
abstract class note {

/**
 * 此处仅为演示 :
 *
 * 不用 many to many ，如果必须要用，用两个 one to many 代替。(hibernate reference : Chapter 24. Best Practices)
 * 多对多连接用得好的例子实际上相当少见。
 * 大多数时候你在“连接表”中需要保存额外的信息(对关系增加属性)。这种情况下，用两个指向中介类的一对多的连接比较好。
 *
 * 实际上，我们认为绝大多数的连接是一对多和多对一的。因此，你应该谨慎使用其它连接风格。
 */

/**
 * 注意事项
 * 1. 集合用 set ，可以自动过滤重复元素
 * 2. 单发出端和双发出端的区别
 * */

}