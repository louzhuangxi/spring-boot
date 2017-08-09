package org.h819.commons.json;

/**
 * Description : TODO()
 * User: h819
 * Date: 2017/8/3
 * Time: 10:19
 * To change this template use File | Settings | File Templates.
 */
@Deprecated
public class JackJsonPropertyPreFilter {


    /**
     *  //双向及联关系阻断
     *  @JsonManagedReference  // 双向关联的关系发出方，该属性会输出
     *  @JsonBackReference  // 双向关联的关系接收方，该属性不会输出
     *
     *  //忽略某些属性
     *  @JsonIgnoreProperties({"city"})
     *
     *  // 缺点
     *  // 一次设定之后，无法改变，不能应用在不同的场景中(动态改变)
     *  // 老老实实的用 fastJson 吧
     *
     */

}
