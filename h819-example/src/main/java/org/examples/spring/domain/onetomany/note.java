package org.examples.spring.domain.onetomany;

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
 *  one to many
 * ==============
 */

/**
 *  注意
 *  1. 选用 join table 方式, foreign key 方式仅是为了演示
 *  2. one to many n+1 问题  , 见 one to many ParentEntity2.class
 *  3. 关联方式 add ,rmove ,clear ，参考 many to many
 */

/**
 * 单向  one -> many (推荐用 中间表方式！)
 */
// == 外键关联方式 ：
// 仅在 parent 类中设置(发出端) ，child 类不做任何标记， 因为 child  中没有 parent  的属性，所以无法 从 child 中获得 parent  的信息，只能单向
//  == 生成的 parent 表没有 child 属性，child 的表有 parent id 属性
//  Parent1,Child1

/**
 *  表关联方式 : 推荐使用
 */
// == 表关联方式：（同外键关联方式）仅在 parent 类中设置(发出端) ，child 不做任何标记， 因为 child  中没有 parent  的属性，所以无法 从 child 中获得 parent  的信息，只能单向
// == 生成的 parent 表没有 child 属性，child 的表也没有 parent 属性，通过中间表来记录两者关系
// Parent2,Child2

/**
 * 单向  many -> one
 * (和 one -> many 区别是：发出端不同，设置方法相同。在发出端设置即可，注意设置为 @ManyToOne 和 @OneToMany )
 */
// == 外键关联方式：仅在 child (发出端) 类中设置，parent 类不做任何标记， 因为 parent  中没有 child  的属性，所以无法 从 parent 中获得 child  的信息，只能单向
// == 生成的 parent 表没有 child 属性，child 的表有 parent id 属性
// Parent3,Child3

/**
 *  表关联方式 : 推荐使用
 */
// == 仅在 child  类中设置(发出端) ，parent 不做任何标记， 因为 parent  中没有 child  的属性，所以无法 从 parent 中获得 child  的信息，只能单向
// == 生成的 parent 表没有 child 属性，child 的表也没有 parent 属性，通过中间表来记录两者关系
// Parent4,Child4


/**
 * 双向 one <-> many
 */
// ==外键关联方式：  parent , child 都需要走设置
// == 生成的 parent 表没有 child 属性，child 的表有 parent id 属性
// Parent5,Child5

/**
 *  表关联方式 : 推荐使用
 */
// 双方都要设置。
// 双向 one to many, 关系表方式，parent 和 child 两个表都无变化，会自动创建多方(child)指定的中间表。
// 关系表方式自动创建，而不需要真正的实体类。关系表有多方 child 指定
// 如果需要对关系添加属性，则需要有真正的实体类，此情形同 many to many 的 one to many + 关系表的实现方式。
// Parent6,Child6
}
