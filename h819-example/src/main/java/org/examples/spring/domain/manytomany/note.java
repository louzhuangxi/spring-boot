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
 * jointable 包: 为 many to many 方式
 * onetomany 包: 把 many to many 拆分为 one to many 方式，并加上中间关系表，可以对关系添加属性
 *
 */


/**
 * ==============
 *  many to many
 * - 通过两个双向的 one to many + 中间表的方式进行，中间表上还可以附带其他属性。见 onetomany 文件夹
 *   注意事项， many to many 级联关系，不推荐使用，此处仅为演示
 * ==============
 */

// 多对多关联关系中只能通过中间表的方式进行映射。

/*
 * 单向 many -> many
 */
// == 仅在 teacher  类(发出端)中设置关系表的属性，student 不做任何标记， 因为 student  中没有 teacher  的属性，所以无法 从 student  中获得 teacher  的信息，只能单向
// Teacher1,Student1

/*
 * 双向 many <-> many
 */
// == 在 teacher、和 student 中均需要设置，student 中设置方法有两种，略有不同。
// Teacher2,Student2

/*
 * ============================ many to many end ===========================================
 */

}