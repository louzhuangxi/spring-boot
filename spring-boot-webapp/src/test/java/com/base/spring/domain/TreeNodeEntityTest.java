package com.base.spring.domain;

import com.base.spring.repository.TreeNodeRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

/**
 * Description : TODO()
 * User: h819
 * Date: 2016/8/4
 * Time: 17:00
 * To change this template use File | Settings | File Templates.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
@Transactional
@Rollback(false)
public class TreeNodeEntityTest {

    @Autowired
    TreeNodeRepository repository;


    @Test
    public void init() {

        // 插入测试树结构数据

//        TreeNodeType.Menu;
//        TreeNodeType.Group;
//        TreeNodeType.DepartMent
//                TreeNodeType.Standard

        //系统其他的时候，这几种类型的父节点已经创建完毕，在下面添加子节点即可
        TreeNodeEntity menu = repository.getRoot(TreeNodeType.Menu);
        TreeNodeEntity group = repository.getRoot(TreeNodeType.Group);
        TreeNodeEntity departMent = repository.getRoot(TreeNodeType.DepartMent);
        TreeNodeEntity standard = repository.getRoot(TreeNodeType.Standard);

        /**
         * 初始化 menu
         */
        TreeNodeEntity menu1 = new TreeNodeEntity(TreeNodeType.Menu, "邮箱管理", 0, true, menu);
        TreeNodeEntity menu11 = new TreeNodeEntity(TreeNodeType.Menu, "收件箱", 0, false, menu1);
        TreeNodeEntity menu12 = new TreeNodeEntity(TreeNodeType.Menu, "发件箱", 1, false, menu1);
        TreeNodeEntity menu13 = new TreeNodeEntity(TreeNodeType.Menu, "垃圾箱", 3, false, menu1);
        TreeNodeEntity menu14 = new TreeNodeEntity(TreeNodeType.Menu, "附件箱", 2, false, menu1);

        menu.addChildToLastIndex(menu1);
        menu1.addChildToLastIndex(menu11);
        menu1.addChildToLastIndex(menu12);
        menu1.addChildToLastIndex(menu13);
        menu1.addChildToLastIndex(menu14);

        // repository.save(menu); //事务状态下，自动保存


        /**
         * 初始化 Standard
         */

        TreeNodeEntity standard1 = new TreeNodeEntity(TreeNodeType.Standard, "国内", 0, true, standard);
        TreeNodeEntity standard11 = new TreeNodeEntity(TreeNodeType.Standard, "国家标准", 0, false, standard1);
        TreeNodeEntity standard12 = new TreeNodeEntity(TreeNodeType.Standard, "行业标准", 1, false, standard1);
        TreeNodeEntity standard13 = new TreeNodeEntity(TreeNodeType.Standard, "地方标准", 3, false, standard1);
        TreeNodeEntity standard121 = new TreeNodeEntity(TreeNodeType.Standard, "电子信息", 0, false, standard12);

        standard.addChildToLastIndex(standard1);
        standard1.addChildToLastIndex(standard11);
        standard1.addChildToLastIndex(standard12);
        standard1.addChildToLastIndex(standard13);
        standard12.addChildToLastIndex(standard121);


    }


}