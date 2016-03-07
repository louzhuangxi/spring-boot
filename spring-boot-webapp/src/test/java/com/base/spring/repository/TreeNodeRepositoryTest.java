package com.base.spring.repository;

import com.base.SpringBootWebappApplication;
import com.base.spring.domain.TreeNodeEntity;
import com.base.spring.domain.TreeNodeType;
import com.google.common.collect.Lists;
import org.h819.commons.MyJsonUtil;
import org.h819.commons.json.FastJsonPropertyPreFilter;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Description : TODO()
 * User: h819
 * Date: 2016/2/3
 * Time: 11:23
 * To change this template use File | Settings | File Templates.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = SpringBootWebappApplication.class)
@Rollback(false)
@Transactional
@WebAppConfiguration
public class TreeNodeRepositoryTest {

    private static final Logger logger = LoggerFactory.getLogger(TreeNodeRepositoryTest.class);


    @Autowired
    TreeNodeRepository treeNodeRepository;


    @Test
    public void testSave() {


        TreeNodeEntity father = treeNodeRepository.getRoot(TreeNodeType.Menu);
        List list = Lists.newArrayList(
                new TreeNodeEntity(TreeNodeType.Menu, "child1", 0,0,  true, father),
                new TreeNodeEntity(TreeNodeType.Menu, "child2", 0,1,  true, father)
        );

        treeNodeRepository.save(list);

    }

    @Test
    public void testSaveChild() {


        TreeNodeEntity father = treeNodeRepository.findOne(1l);
        if (father != null) {
            List list = Lists.newArrayList(
                    new TreeNodeEntity(TreeNodeType.Menu, "child3",  2,0, true, father),
                    new TreeNodeEntity(TreeNodeType.Menu, "child4",  2,1, true, father));
            treeNodeRepository.save(list);
        }
    }


    @Test
    public void testFind() {

        List<TreeNodeEntity> menus = treeNodeRepository.findAll();
        System.out.println("size:" + menus.size());

        FastJsonPropertyPreFilter preFilter = new FastJsonPropertyPreFilter();
        preFilter.addExcludes(TreeNodeEntity.class, "parent", "children", "privilege"); //多个属性

        // System.out.println(":"+ JSON.toJSONString(menus, preFilter, SerializerFeature.DisableCircularReferenceDetect));

        MyJsonUtil.prettyPrint(menus, preFilter);


    }

    @Test
    public void testFindByName() throws Exception {

    }

    @Test
    public void testFindByContainsName() throws Exception {

    }


}