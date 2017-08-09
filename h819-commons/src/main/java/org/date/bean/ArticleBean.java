package org.date.bean;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

/**
 * Description : TODO()
 * User: h819
 * Date: 2017/8/3
 * Time: 11:55
 * To change this template use File | Settings | File Templates.
 */
@Data
@JsonIgnoreProperties({"city"})
public class ArticleBean {
    private String title;
    @JsonBackReference  // 双向关联的关系接收方，该属性不会输出
    private UserBean user;
    private CityBean city;
}
