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
//        TreeNodeType.Standard

        //系统其他的时候，这几种类型的父节点已经创建完毕，在下面添加子节点即可
        TreeNodeEntity menu = repository.getRoot(TreeNodeType.Menu); // menu , privilege
        TreeNodeEntity group = repository.getRoot(TreeNodeType.Group);
        TreeNodeEntity departMent = repository.getRoot(TreeNodeType.DepartMent);
        TreeNodeEntity standard = repository.getRoot(TreeNodeType.Standard);

        /**
         * 初始化 menu
         */
        TreeNodeEntity menu1 = new TreeNodeEntity(TreeNodeType.Menu, "菜单管理", 0, true, menu);
        TreeNodeEntity menu11 = new TreeNodeEntity(TreeNodeType.Menu, "邮箱管理", 0, true, menu1);
        TreeNodeEntity menu12 = new TreeNodeEntity(TreeNodeType.Menu, "用户管理", 1, false, menu1);
        TreeNodeEntity menu13 = new TreeNodeEntity(TreeNodeType.Menu, "个人设置", 3, false, menu1);
        TreeNodeEntity menu14 = new TreeNodeEntity(TreeNodeType.Menu, "系统设置", 2, true, menu1);

        TreeNodeEntity menu111 = new TreeNodeEntity(TreeNodeType.Menu, "收件箱", 0, false, menu11);
        TreeNodeEntity menu112 = new TreeNodeEntity(TreeNodeType.Menu, "发件箱", 1, false, menu11);
        TreeNodeEntity menu113 = new TreeNodeEntity(TreeNodeType.Menu, "垃圾箱", 2, false, menu11);

        TreeNodeEntity menu141 = new TreeNodeEntity(TreeNodeType.Menu, "系统日志", 0, false, menu14);
        TreeNodeEntity menu142 = new TreeNodeEntity(TreeNodeType.Menu, "参数配置", 1, false, menu14);
        TreeNodeEntity menu143 = new TreeNodeEntity(TreeNodeType.Menu, "定时任务", 2, false, menu14);


        /**
         * 初始化 privilege ，其他非菜单的资源，都设计为树的叶节点，便于授权
         */
        TreeNodeEntity menu2 = new TreeNodeEntity(TreeNodeType.Menu, "资源/操作", 0, true, menu);
        TreeNodeEntity menu21 = new TreeNodeEntity(TreeNodeType.Menu, "文件资源", 0, true, menu2);  //应用中的文件资源
        TreeNodeEntity menu22 = new TreeNodeEntity(TreeNodeType.Menu, "文件操作", 1, false, menu2);
        TreeNodeEntity menu23 = new TreeNodeEntity(TreeNodeType.Menu, "个人设置", 3, false, menu2);
        TreeNodeEntity menu24 = new TreeNodeEntity(TreeNodeType.Menu, "pdf 文件", 2, true, menu2);  //单独对 pdf 的操作

        //应用中的文件资源
        TreeNodeEntity menu211 = new TreeNodeEntity(TreeNodeType.Menu, "查看原文", 0, false, menu21);
        TreeNodeEntity menu212 = new TreeNodeEntity(TreeNodeType.Menu, "导出原文", 0, false, menu21);
        TreeNodeEntity menu213 = new TreeNodeEntity(TreeNodeType.Menu, "下载原文", 0, false, menu21);
        TreeNodeEntity menu214 = new TreeNodeEntity(TreeNodeType.Menu, "下载附件", 0, false, menu21);
        TreeNodeEntity menu215 = new TreeNodeEntity(TreeNodeType.Menu, "查看详情", 0, false, menu21);

        //单独对 pdf 的操作
        TreeNodeEntity menu241 = new TreeNodeEntity(TreeNodeType.Menu, "查看", 0, false, menu24);
        TreeNodeEntity menu242 = new TreeNodeEntity(TreeNodeType.Menu, "打印", 1, false, menu24);
        TreeNodeEntity menu243 = new TreeNodeEntity(TreeNodeType.Menu, "下载", 2, false, menu24);

        //menu
        menu.addChildToLastIndex(menu1);
        menu.addChildToLastIndex(menu2);

        //menu1
        menu1.addChildToLastIndex(menu11);
        menu1.addChildToLastIndex(menu12);
        menu1.addChildToLastIndex(menu13);
        menu1.addChildToLastIndex(menu14);

        //menu11
        menu11.addChildToLastIndex(menu111);
        menu11.addChildToLastIndex(menu112);
        menu11.addChildToLastIndex(menu113);

        //menu14
        menu14.addChildToLastIndex(menu141);
        menu14.addChildToLastIndex(menu142);
        menu14.addChildToLastIndex(menu143);

        //menu2
        menu2.addChildToLastIndex(menu21);
        menu2.addChildToLastIndex(menu22);
        menu2.addChildToLastIndex(menu23);
        menu2.addChildToLastIndex(menu24);

        //menu21
        menu21.addChildToLastIndex(menu211);
        menu21.addChildToLastIndex(menu212);
        menu21.addChildToLastIndex(menu213);
        menu21.addChildToLastIndex(menu214);
        menu21.addChildToLastIndex(menu215);

        //menu24
        menu24.addChildToLastIndex(menu241);
        menu24.addChildToLastIndex(menu242);
        menu24.addChildToLastIndex(menu243);

        //   repository.save(menu); //事务状态下，自动保存


        /**
         * 初始化 Standard
         */

        TreeNodeEntity standard1 = new TreeNodeEntity(TreeNodeType.Standard, "国内", 0, true, standard);
        TreeNodeEntity standard11 = new TreeNodeEntity(TreeNodeType.Standard, "国家标准", 0, true, standard1);
        TreeNodeEntity standard12 = new TreeNodeEntity(TreeNodeType.Standard, "行业标准", 1, true, standard1);
        TreeNodeEntity standard13 = new TreeNodeEntity(TreeNodeType.Standard, "地方标准", 2, true, standard1);


        TreeNodeEntity standard111 = new TreeNodeEntity(TreeNodeType.Standard, "国家标准", 0, false, standard11);
        TreeNodeEntity standard112 = new TreeNodeEntity(TreeNodeType.Standard, " 军用标准", 1, false, standard11);
        TreeNodeEntity standard113 = new TreeNodeEntity(TreeNodeType.Standard, "计量规程", 2, false, standard11);
        TreeNodeEntity standard114 = new TreeNodeEntity(TreeNodeType.Standard, "计量规范", 3, false, standard11);


        TreeNodeEntity standard121 = new TreeNodeEntity(TreeNodeType.Standard, "安全生产", 0, false, standard12);
        TreeNodeEntity standard122 = new TreeNodeEntity(TreeNodeType.Standard, "电子信息", 1, false, standard12);
        TreeNodeEntity standard123 = new TreeNodeEntity(TreeNodeType.Standard, "医药健康", 2, false, standard12);

        TreeNodeEntity standard131 = new TreeNodeEntity(TreeNodeType.Standard, "北京DB11", 0, false, standard13);
        TreeNodeEntity standard132 = new TreeNodeEntity(TreeNodeType.Standard, "天津DB12", 1, false, standard13);
        TreeNodeEntity standard133 = new TreeNodeEntity(TreeNodeType.Standard, "河北DB13", 2, false, standard13);


        TreeNodeEntity standard2 = new TreeNodeEntity(TreeNodeType.Standard, "国外", 0, true, standard);
        TreeNodeEntity standard21 = new TreeNodeEntity(TreeNodeType.Standard, "iso标准", 0, false, standard2);
        TreeNodeEntity standard22 = new TreeNodeEntity(TreeNodeType.Standard, "iec标准", 1, false, standard2);
        TreeNodeEntity standard23 = new TreeNodeEntity(TreeNodeType.Standard, "itu标准", 2, false, standard2);


        //standard
        standard.addChildToLastIndex(standard1);
        standard.addChildToLastIndex(standard2);

        //standard1
        standard1.addChildToLastIndex(standard11);
        standard1.addChildToLastIndex(standard12);
        standard1.addChildToLastIndex(standard13);

        //standard11
        standard11.addChildToLastIndex(standard111);
        standard11.addChildToLastIndex(standard112);
        standard11.addChildToLastIndex(standard113);
        standard11.addChildToLastIndex(standard114);

        //standard12
        standard12.addChildToLastIndex(standard121);
        standard12.addChildToLastIndex(standard122);
        standard12.addChildToLastIndex(standard123);

        //standard13
        standard13.addChildToLastIndex(standard131);
        standard13.addChildToLastIndex(standard132);
        standard13.addChildToLastIndex(standard133);

        //standard2
        standard2.addChildToLastIndex(standard21);
        standard2.addChildToLastIndex(standard22);
        standard2.addChildToLastIndex(standard23);

       //    repository.save(standard); //事务状态下，自动保存

    }


}