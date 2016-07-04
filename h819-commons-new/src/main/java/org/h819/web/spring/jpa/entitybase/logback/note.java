package org.h819.web.spring.jpa.entitybase.logback;

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
     * ============================logback.xml 配置信息入数据库，用到的表 ===========================================
     */
    // 发现问题：
    // 只能用 logback-classic-1.1.3.jar\ch\qos\logback\classic\db\script - 中的脚本，创建三个表，之后用 orm 工具生成 Entity

    // 但反过来，删除三个表，用生成的 Entity 自动创建表，就不行。

    //  不再浪费时间查找原因，直接使用这几个类吧

/**
 * 使用过程
 * 1. 用 logback-classic-1.1.3.jar\ch\qos\logback\classic\db\script - 中的脚本，创建三个表
 * 2. orm 工具自动生成 Entity ( logback 包中已经创建完成，直接使用。)
 * 3. 使用。
 */

}