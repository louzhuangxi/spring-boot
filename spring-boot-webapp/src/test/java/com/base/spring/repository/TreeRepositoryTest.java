package com.base.spring.repository;

import com.base.spring.domain.TreeEntity;
import com.base.spring.domain.TreeType;
import org.h819.commons.MyFastJsonUtils;
import org.h819.commons.json.FastJsonPropertyPreFilter;
import org.h819.web.spring.jpa.JpaDynamicSpecificationBuilder;
import org.h819.web.spring.jpa.SearchFilter;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Description : TODO()
 * User: h819
 * Date: 2017-11-08
 * Time: 1:56
 * To change this template use File | Settings | File Templates.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
@Transactional(readOnly = false)
@Rollback(value = false)
public class TreeRepositoryTest {

    @Autowired
    TreeRepository repository;

    @Test
    public void testLocalDateTime() {

        //  TreeNodeEntity entity = repository.getOne(54l);

//        entity.setParentNode(true);


        Specification specification1 = new JpaDynamicSpecificationBuilder()
                .and(new SearchFilter("parent.name", SearchFilter.Operator.EQ, "国外"))
                .and(new SearchFilter("name", SearchFilter.Operator.LIKE, "iso")).build();

        Specification specification = new JpaDynamicSpecificationBuilder()
                .and(new SearchFilter("id", SearchFilter.Operator.BETWEEN, null, 20)).build();


        // Specification specification = JpaDynamicSpecificationUtils.joinSearchFilter(SearchFilter.Relation.AND,
//                new SearchFilter("parent.name", SearchFilter.Operator.EQ, "国外"),
//                new SearchFilter("name", SearchFilter.Operator.LIKE, "iso"));


        List<TreeEntity> entity = repository.findAll(specification);

//        System.out.println(entity.getId());
//
//        System.out.println(entity.getCreatedDate());
//        System.out.println(entity.getModifiedDate());
//        MyJsonUtils.prettyPrint(entity.getCreatedDate());


        FastJsonPropertyPreFilter preFilter = new FastJsonPropertyPreFilter();
        preFilter.addExcludes(TreeEntity.class, "parent");

        MyFastJsonUtils.prettyPrint(entity, preFilter);


    }

    @Test
    public void tesGetTreeType() {

        List<TreeType> list = repository.findTreeTypes();

        MyFastJsonUtils.prettyPrint(list);


    }
}