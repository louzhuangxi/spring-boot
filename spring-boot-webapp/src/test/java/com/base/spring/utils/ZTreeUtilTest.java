package com.base.spring.utils;

import com.base.SpringBootWebappApplication;
import com.base.spring.domain.TreeNodeEntity;
import com.base.spring.domain.TreeNodeType;
import com.base.spring.repository.TreeNodeRepository;
import org.h819.commons.MyJsonUtils;
import org.h819.web.spring.jpa.DTOUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.transaction.annotation.Transactional;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = SpringBootWebappApplication.class)
@Rollback(false)
@Transactional
@WebAppConfiguration
public class ZTreeUtilTest {

    @Autowired
    private TreeNodeRepository treeNodeRepository;

    @org.junit.Test
    public void testGetSimpleData() throws Exception {

        DTOUtils dtoUtils = new DTOUtils();
        dtoUtils.addExcludes(TreeNodeEntity.class, "parent", "privilege");
        TreeNodeEntity rootNode = treeNodeRepository.getRoot(TreeNodeType.Menu);
        TreeNodeEntity dtoRootNode = dtoUtils.createDTOcopy(rootNode, 3);

        //Set<ZTreeSimpleNode> set = ZTreeUtil.getSimpleData(dtoRootNode);
        //MyJsonUtils.prettyPrint(set);
        ;
        //   ZTreeUtil.getJsonData(dtoRootNode);
        MyJsonUtils.prettyPrint(ZTreeUtils.getJsonData(dtoRootNode));


    }

    @Test
    public void testGetSimpleData1() throws Exception {

    }
}