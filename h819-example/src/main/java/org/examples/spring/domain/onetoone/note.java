package org.examples.spring.domain.onetoone;

/**
 * Description : TODO()
 * User: h819
 * Date: 2015/4/16
 * Time: 18:12
 * To change this template use File | Settings | File Templates.
 */
// 需要用 真正的 Class 文件做注释，否则自动格式化的时候，会合并所有 /** */ 的内容
@Deprecated
abstract class note {

/**
 * ==============
 *  one to one
 * ==============
 */

/**
 *  注意
 *  1. 选用 join table 方式, foreign key 方式仅是为了演示
 *  2. 关联方式 add ,rmove ,clear ，参考 many to many
 */

    /**
     * ==============
     *  one to one
     *  (单向和双向，外键关联方式都会在一端增加另外一端的 id)
     *
     * ==============
     */

    /**
     * 单向  one -> one
     */
// == 外键关联方式：仅在 person 类中设置(发出端) ，address 类不做任何标记， 因为 address  中没有 person  的属性，所以无法 从 address 中获得 person  的信息，只能单向
// == 生成的 person 表没有 address 属性，person  的表有 address id 属性
// Person1,Address1

/**
 *  表关联方式 : 推荐使用
 */
// Person2,Address2


/**
 * 双向 one <-> one
 */
// == 外键关联方式：  parent , child 都需要走设置
// == 生成的 person  表没有 address 属性，address 的表有 person id 属性
// Person2,Address2

/**
 *  表关联方式 : 推荐使用
 */
// Person4,Address4

/*
 * ============================ one to one end ===========================================
 */
}
