package com.base.spring.utils;

import com.base.SpringBootWebappApplication;
import com.base.spring.domain.TreeNodeEntity;
import com.base.spring.domain.TreeNodeType;
import com.base.spring.repository.TreeNodeRepository;
import org.h819.web.spring.jpa.DynamicSpecificationUtils;
import org.h819.web.spring.jpa.SearchFilter;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Description : TODO()
 * User: h819
 * Date: 2016/1/19
 * Time: 11:17
 * To change this template use File | Settings | File Templates.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = SpringBootWebappApplication.class)
@Rollback(false)
@Transactional
@WebAppConfiguration
public class DynamicSpecificationUtilsTest {

    @Autowired
    TreeNodeRepository menuRepository;

    @Test
    public void testBySearchFilter() throws Exception {

        //普通属性
        SearchFilter filtereq = new SearchFilter("name", SearchFilter.Operator.EQ, "child");
        Specification spc = DynamicSpecificationUtils.bySearchFilter(filtereq);
        List<TreeNodeEntity> list = menuRepository.findAll(spc);

        for (TreeNodeEntity menu : list) {
            System.out.println("name=" + menu.getName());
            System.out.println("pname=" + menu.getParent().getName());
        }
    }

    @Test
    public void testBySearchFilter2() throws Exception {

        // 对象属性
        TreeNodeEntity parent = menuRepository.getRoot(TreeNodeType.Menu);
        System.out.println("parent name=" + parent.getName());
        SearchFilter filtereq2 = new SearchFilter("parent", SearchFilter.Operator.EQ, parent);
        Specification spc2 = DynamicSpecificationUtils.bySearchFilter(filtereq2);


        List<TreeNodeEntity> list = menuRepository.findAll(spc2);

        for (TreeNodeEntity menu : list) {
            System.out.println("name=" + menu.getName());
            System.out.println("pname=" + menu.getParent().getName());
        }
    }

    @Test
    public void testBySearchFilter3() throws Exception {

        // 对象属性
//        MenuEntity parent = menuRepository.findByName("root").get();
//        System.out.println("parent name=" + parent.getName());
        SearchFilter filtereq3 = new SearchFilter("parent.name", SearchFilter.Operator.EQ, "root");
        Specification spc3 = DynamicSpecificationUtils.bySearchFilter(filtereq3);

        List<TreeNodeEntity> list = menuRepository.findAll(spc3);

        for (TreeNodeEntity menu : list) {
            System.out.println("name=" + menu.getName());
            System.out.println("pname=" + menu.getParent().getName());
        }
    }

    @Test
    public void testBySearchFilter4() throws Exception {

        //普通属性,like
        SearchFilter filtereq = new SearchFilter("name", SearchFilter.Operator.LIKE, "child");
        Specification spc = DynamicSpecificationUtils.bySearchFilter(filtereq);
        List<TreeNodeEntity> list = menuRepository.findAll(spc);

        for (TreeNodeEntity menu : list) {
            System.out.println("name=" + menu.getName());
            System.out.println("pname=" + menu.getParent().getName());
        }
    }

    @Test
    public void testBySearchFilter5() throws Exception {

        // 对象属性 like
        TreeNodeEntity parent = menuRepository.getRoot(TreeNodeType.Menu);
        System.out.println("parent name=" + parent.getName());
        SearchFilter filtereq2 = new SearchFilter("parent", SearchFilter.Operator.LIKE, parent);
        Specification spc2 = DynamicSpecificationUtils.bySearchFilter(filtereq2);


        List<TreeNodeEntity> list = menuRepository.findAll(spc2);

        for (TreeNodeEntity menu : list) {
            System.out.println("name=" + menu.getName());
            System.out.println("pname=" + menu.getParent().getName());
        }
    }

    @Test
    public void testBySearchFilter6() throws Exception {

        // 对象属性
//        MenuEntity parent = menuRepository.findByName("root").get();
//        System.out.println("parent name=" + parent.getName());
        SearchFilter filtereq3 = new SearchFilter("parent.name", SearchFilter.Operator.LIKE, "root");
        Specification spc3 = DynamicSpecificationUtils.bySearchFilter(filtereq3);

        List<TreeNodeEntity> list = menuRepository.findAll(spc3);

        for (TreeNodeEntity menu : list) {
            System.out.println("name=" + menu.getName());
            System.out.println("pname=" + menu.getParent().getName());
        }
    }

    @Test
    public void testBySearchFilter7() throws Exception {

        //普通属性
        SearchFilter filtereq = new SearchFilter("name", SearchFilter.Operator.GT, "child");
        Specification spc = DynamicSpecificationUtils.bySearchFilter(filtereq);
        List<TreeNodeEntity> list = menuRepository.findAll(spc);

        for (TreeNodeEntity menu : list) {
            System.out.println("name=" + menu.getName());
            System.out.println("pname=" + menu.getParent().getName());
        }
    }

    @Test
    public void testBySearchFilter9() throws Exception {

        //普通属性
        SearchFilter filtereq = new SearchFilter("id", SearchFilter.Operator.GT, 1);
        Specification spc = DynamicSpecificationUtils.bySearchFilter(filtereq);
        List<TreeNodeEntity> list = menuRepository.findAll(spc);

        for (TreeNodeEntity menu : list) {
            System.out.println("name=" + menu.getName());
            System.out.println("pname=" + menu.getParent().getName());
        }
    }

    @Test
    public void testBySearchFilter8() throws Exception {

        // 对象属性 like
        TreeNodeEntity childer = menuRepository.findOne(1l);
        System.out.println("name=" + childer.getName());
        SearchFilter filtereq2 = new SearchFilter("parent", SearchFilter.Operator.GT, childer);
        Specification spc2 = DynamicSpecificationUtils.bySearchFilter(filtereq2);


        List<TreeNodeEntity> list = menuRepository.findAll(spc2);

        for (TreeNodeEntity menu : list) {
            System.out.println("name=" + menu.getName());
            System.out.println("pname=" + menu.getParent().getName());
        }
    }

    @Test
    public void testBySearchFilter10() throws Exception {

        // 对象属性 like
        TreeNodeEntity childer =  menuRepository.getRoot(TreeNodeType.Menu);;
        System.out.println("name=" + childer.getName());
        SearchFilter filtereq2 = new SearchFilter("parent", SearchFilter.Operator.NN, childer);
        Specification spc2 = DynamicSpecificationUtils.bySearchFilter(filtereq2);


        List<TreeNodeEntity> list = menuRepository.findAll(spc2);

        for (TreeNodeEntity menu : list) {
            System.out.println("name=" + menu.getName());
            System.out.println("pname=" + menu.getParent().getName());
        }
    }

    @Test
    public void testBySearchFilter11() throws Exception {

        // in
        List<TreeNodeEntity> childer = menuRepository.findByContainsName("root");

        SearchFilter filtereq2 = new SearchFilter("parent", SearchFilter.Operator.IN, childer);
        Specification spc2 = DynamicSpecificationUtils.bySearchFilter(filtereq2);


        List<TreeNodeEntity> list = menuRepository.findAll(spc2);

        for (TreeNodeEntity menu : list) {
            System.out.println("name=" + menu.getName());
            System.out.println("pname=" + menu.getParent().getName());
        }
    }

    @Test
    public void testBySearchFilter12() throws Exception {

        SearchFilter filtereq1 = new SearchFilter("name", SearchFilter.Operator.LIKE, "child");
        SearchFilter filtereq2 = new SearchFilter("id", SearchFilter.Operator.EQ, "12");
        Specification spc2 = DynamicSpecificationUtils.joinSearchFilter(SearchFilter.Relation.AND,filtereq1,filtereq2);


        List<TreeNodeEntity> list = menuRepository.findAll(spc2);

        for (TreeNodeEntity menu : list) {
            System.out.println("name=" + menu.getName());
            System.out.println("pname=" + menu.getParent().getName());
        }
    }

    @Test
    public void testByInOperator() throws Exception {

    }

    @Test
    public void testByNotInOperator() throws Exception {

    }

    @Test
    public void testByBetweenOperator() throws Exception {

    }

    @Test
    public void testByNotSpecification() throws Exception {

    }

    @Test
    public void testJoinSpecification() throws Exception {

    }


    @Test
    public void testJoinSearchFilter() throws Exception {

    }

}