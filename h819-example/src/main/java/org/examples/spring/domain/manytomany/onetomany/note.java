package org.examples.spring.domain.manytomany.onetomany;

/**
 * Description : TODO()
 * User: h819
 * Date: 2016-10-19
 * Time: 下午9:14                                  s
 */

// 需要用 真正的 Class 文件做注释，否则自动格式化的时候，会合并所有 /** */ 的内容
@Deprecated
abstract class note {

/**
 使用 one to many 和 关系表的方式实现 many to many ，关系表中可以增加其他需要的附加属性
 如果不需要添加属性，则不需要添加中间表，和 双向 one to many 一样了
 *
 */

/**
 * ============================ 对关系增加属性 ===========================================
 *
 * 参考 ：http://en.wikibooks.org/wiki/Java_Persistence/ManyToMany#Mapping_a_Join_Table_with_Additional_Columns
 *
 *
 */

// 例如：
//  一个 teacher 可以有多个 student ,一个 student 可以有多个 teacher，是一个双向的 ManyToMany 关系，拆分为两个双向的 one to many 和中间关系表
//  对二者之间的关系，可以增加其他的属性，如 student 选择 teacher 的时间，是一个二者关系的附带属性，此属性就可以建立在中间表上。

//  需要把关系独立出来，做为第三个 Entity ，在这个实体上增加属性，变成：
//  TeacherEntity <->  RefTeacherStudentEntity :  双向 one to many
//  StudentEntity <->  RefTeacherStudentEntity :  双向 one to many

//  注意：对增加和删除关联关系，由于用到了 Repository ,在 Service 层实现，见  RefTeacherStudentEntityService.java
}
