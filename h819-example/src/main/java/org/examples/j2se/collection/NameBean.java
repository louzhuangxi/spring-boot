package org.examples.j2se.collection;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.google.common.collect.Sets;
import lombok.Data;
import org.date.bean.ArticleBean;
import org.date.bean.CityBean;

import java.util.Set;

/**
 * Description : TODO()
 * User: h819
 * Date: 14-1-13
 * Time: 下午1:56
 * To change this template use File | Settings | File Templates.
 */
@Data
public class NameBean {

    private String name;
    private String passwd;
    private CityBean city;
    private Integer age;
    @JsonManagedReference  // 双向关联的关系发出方，该属性会输出
    private Set<ArticleBean> articles = Sets.newHashSet();

    public NameBean(String name, Integer age) {
        this.name = name;
        this.age = age;
    }
}
