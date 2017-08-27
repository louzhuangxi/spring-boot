package org.date.bean;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.google.common.collect.Sets;
import lombok.Data;

import java.util.Set;

/**
 * Description : TODO()
 * User: h819
 * Date: 14-1-13
 * Time: 下午1:56
 * To change this template use File | Settings | File Templates.
 */
@Data
public class UserBean {

    private String name;
    private String passwd;
    private CityBean city;
    @JsonManagedReference  // 双向关联的关系发出方，该属性会输出
    private Set<ArticleBean> articles = Sets.newHashSet();

}
