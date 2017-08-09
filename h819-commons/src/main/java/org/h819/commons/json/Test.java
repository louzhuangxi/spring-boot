package org.h819.commons.json;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.google.common.collect.Sets;
import org.date.bean.ArticleBean;
import org.date.bean.CityBean;
import org.date.bean.UserBean;
import org.h819.commons.MyFastJsonUtils;

/**
 * Description : TODO()
 * User: h819
 * Date: 2017/8/3
 * Time: 11:59
 * To change this template use File | Settings | File Templates.
 */
public class Test {

    UserBean user;


    public static void main(String args[]) throws JsonProcessingException {
        Test test = new Test();
        // test.testFastJsonPropertyPreFilter();
        test.testJackJsonPropertyPreFilter();
    }


    private void testFastJsonPropertyPreFilter() {
        prepareDate();

        FastJsonPropertyPreFilter preFilter = new FastJsonPropertyPreFilter();
        preFilter.addExcludes(ArticleBean.class, "user"); //在整个转换过程中，无论哪个级联层次，只要遇到 ArticleBean 类，那么他的 user 属性就不进行转换 , 本例中截断了双向关联
        preFilter.addExcludes(UserBean.class, "name", "children"); //多个属性
        String json = MyFastJsonUtils.toPrettyJSONString(user, preFilter);
        System.out.println(json);
    }


    private void testJackJsonPropertyPreFilter() throws JsonProcessingException {
        prepareDate();
        ObjectMapper mapper = new ObjectMapper();
        mapper.enable(SerializationFeature.INDENT_OUTPUT);//pretty print
        System.out.println(mapper.writeValueAsString(user));
    }

    private void prepareDate() {
        user = new UserBean();
        user.setName("name");
        user.setPasswd("password");
        //==

        CityBean city = new CityBean();
        city.setCity("My city.");
        user.setCity(city);
        //==
        ArticleBean article = new ArticleBean();
        article.setUser(user);
        article.setTitle("article title");
        article.setCity(city);
        user.setArticles(Sets.newHashSet(article));

    }

}
