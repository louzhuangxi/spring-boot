package com.base.spring.domain;

import com.base.spring.repository.TreeRepository;
import org.h819.commons.MyJsonUtils;
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
 * Date: 2016/8/4
 * Time: 17:00
 * To change this template use File | Settings | File Templates.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
@Transactional
@Rollback(false)
public class TreeEntityTest {

    @Autowired
    TreeRepository repository;

    @Test
    public void initTreeNodes() {

        // 插入测试树结构数据

//        TreeNodeType.Menu;
//        TreeNodeType.Group;
//        TreeNodeType.DepartMent
//        TreeNodeType.Standard

        //系统其他的时候，这几种类型的父节点已经创建完毕，在下面添加子节点即可
        TreeEntity menu = repository.getRoot(TreeType.Menu).get(); // menu , privilege
        TreeEntity departMent = repository.getRoot(TreeType.DepartMent).get();
        TreeEntity standard = repository.getRoot(TreeType.Standard).get();

        /**
         * 初始化 menu
         */
        TreeEntity menu1 = new TreeEntity(TreeType.Menu, "菜单管理", 0, true, menu);
        TreeEntity menu11 = new TreeEntity(TreeType.Menu, "邮箱管理", 0, true, menu1);
        TreeEntity menu12 = new TreeEntity(TreeType.Menu, "用户管理", 1, false, menu1);
        TreeEntity menu13 = new TreeEntity(TreeType.Menu, "个人设置", 3, false, menu1);
        TreeEntity menu14 = new TreeEntity(TreeType.Menu, "系统设置", 2, true, menu1);

        TreeEntity menu111 = new TreeEntity(TreeType.Menu, "收件箱", 0, false, menu11);
        TreeEntity menu112 = new TreeEntity(TreeType.Menu, "发件箱", 1, false, menu11);
        TreeEntity menu113 = new TreeEntity(TreeType.Menu, "垃圾箱", 2, false, menu11);

        TreeEntity menu141 = new TreeEntity(TreeType.Menu, "系统日志", 0, false, menu14);
        TreeEntity menu142 = new TreeEntity(TreeType.Menu, "参数配置", 1, false, menu14);
        TreeEntity menu143 = new TreeEntity(TreeType.Menu, "定时任务", 2, false, menu14);


        /**
         * 初始化 privilege ，其他非菜单的资源，都设计为树的叶节点，便于授权
         */
        TreeEntity menu2 = new TreeEntity(TreeType.Menu, "资源/操作", 0, true, menu);
        TreeEntity menu21 = new TreeEntity(TreeType.Menu, "文件资源", 0, true, menu2);  //应用中的文件资源
        TreeEntity menu22 = new TreeEntity(TreeType.Menu, "文件操作", 1, false, menu2);
        TreeEntity menu23 = new TreeEntity(TreeType.Menu, "个人设置", 3, false, menu2);
        TreeEntity menu24 = new TreeEntity(TreeType.Menu, "pdf 文件", 2, true, menu2);  //单独对 pdf 的操作

        //应用中的文件资源
        TreeEntity menu211 = new TreeEntity(TreeType.Menu, "查看原文", 0, false, menu21);
        TreeEntity menu212 = new TreeEntity(TreeType.Menu, "导出原文", 0, false, menu21);
        TreeEntity menu213 = new TreeEntity(TreeType.Menu, "下载原文", 0, false, menu21);
        TreeEntity menu214 = new TreeEntity(TreeType.Menu, "下载附件", 0, false, menu21);
        TreeEntity menu215 = new TreeEntity(TreeType.Menu, "查看详情", 0, false, menu21);

        //单独对 pdf 的操作
        TreeEntity menu241 = new TreeEntity(TreeType.Menu, "查看", 0, false, menu24);
        TreeEntity menu242 = new TreeEntity(TreeType.Menu, "打印", 1, false, menu24);
        TreeEntity menu243 = new TreeEntity(TreeType.Menu, "下载", 2, false, menu24);

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

        TreeEntity standard1 = new TreeEntity(TreeType.Standard, "国内", 0, true, standard);
        TreeEntity standard11 = new TreeEntity(TreeType.Standard, "国家标准", 0, true, standard1);
        TreeEntity standard12 = new TreeEntity(TreeType.Standard, "行业标准", 1, true, standard1);
        TreeEntity standard13 = new TreeEntity(TreeType.Standard, "地方标准", 2, true, standard1);


        TreeEntity standard111 = new TreeEntity(TreeType.Standard, "国家标准", 0, false, standard11);
        TreeEntity standard112 = new TreeEntity(TreeType.Standard, " 军用标准", 1, false, standard11);
        TreeEntity standard113 = new TreeEntity(TreeType.Standard, "计量规程", 2, false, standard11);
        TreeEntity standard114 = new TreeEntity(TreeType.Standard, "计量规范", 3, false, standard11);


        TreeEntity standard121 = new TreeEntity(TreeType.Standard, "安全生产", 0, false, standard12);
        TreeEntity standard122 = new TreeEntity(TreeType.Standard, "电子信息", 1, false, standard12);
        TreeEntity standard123 = new TreeEntity(TreeType.Standard, "医药健康", 2, false, standard12);

        TreeEntity standard131 = new TreeEntity(TreeType.Standard, "北京DB11", 0, false, standard13);
        TreeEntity standard132 = new TreeEntity(TreeType.Standard, "天津DB12", 1, false, standard13);
        TreeEntity standard133 = new TreeEntity(TreeType.Standard, "河北DB13", 2, false, standard13);


        TreeEntity standard2 = new TreeEntity(TreeType.Standard, "国外", 0, true, standard);
        TreeEntity standard21 = new TreeEntity(TreeType.Standard, "iso标准", 0, false, standard2);
        TreeEntity standard22 = new TreeEntity(TreeType.Standard, "iec标准", 1, false, standard2);
        TreeEntity standard23 = new TreeEntity(TreeType.Standard, "itu标准", 2, false, standard2);


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

        MyJsonUtils.prettyPrint(entity, preFilter);


    }

    @Test
    public void tesGetTreeType() {

        List<TreeType> list = repository.findTreeTypes();

        MyJsonUtils.prettyPrint(list);


    }

}