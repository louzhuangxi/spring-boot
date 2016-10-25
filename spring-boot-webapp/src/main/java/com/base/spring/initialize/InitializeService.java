package com.base.spring.initialize;

import com.base.spring.domain.*;
import com.base.spring.repository.GroupRepository;
import com.base.spring.repository.RoleRepository;
import com.base.spring.repository.TreeRepository;
import com.base.spring.repository.UserRepository;
import com.base.spring.utils.BCryptPassWordUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import java.util.*;

/**
 * Description : TODO(初始化相关数据)
 * User: h819
 * Date: 2016/1/18
 * Time: 15:38
 * To change this template use File | Settings | File Templates.
 */
@Component
@Transactional(readOnly = true)
public class InitializeService {
    private static final Logger logger = LoggerFactory.getLogger(InitializeService.class);


    @Autowired
    TreeRepository treeRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    GroupRepository groupRepository;
    @Autowired
    RoleRepository roleRepository;


    /**
     * 初始化 TreeNodeType 中定义的所有类型的根节点，所有节点均为该节点的子节点
     *
     * @PostConstruct // 在 Bean 初始化之前执行的操作
     * @PreDestroy //在 bean 销毁之前执行的操作
     */
    @PostConstruct // 在 InitializeService Bean 初始化之后执行的方法
    @Transactional(readOnly = false)
    public void initBaseData() {

        /**
         * 初始化 Tree
         */

        List<TreeType> rootTreeType = treeRepository.findTreeTypes();
        List<TreeType> listType = Arrays.asList(TreeType.values());
        for (TreeType type : listType) {
            if (!rootTreeType.contains(type)) {
                logger.info("init {} tree root node", type);
                TreeEntity root = new TreeEntity(type, "root_" + type, 0, true, null);
                treeRepository.save(root);
            }
        }

        initMenuTree();

        /**
         * 初始化 User
         */

        initUser();

        /**
         * 初始化 Group
         */

        initGroup();

        /**
         * 初始化 Role
         */

        initRole();
    }

    /**
     * 初始化菜单树，作为基础树
     * 其中下面几个树节点，不能修改，为本系统默认的菜单(index.ftl 导航也是这个名称)
     * 1. 菜单管理
     * 1.1 树结构管理
     * 1.1.1 菜单树
     * 1.1.2 部门树
     * 1.1.3 其他业务树
     * -
     * 1.2 用户管理
     * 1.2.1 用户
     * 1.2.2 用户组
     * -
     * 1.3 角色管理
     * 1.3.1 菜单角色
     * 1.3.2 标准资源角色
     * -
     * 2. 按钮/资源
     * 2.1 菜单树
     * 2.1.1 树结构编辑
     * 2.1.2 节点 URL
     * -
     * 其他菜单只作为示例，可根据实际需要修改
     */
    private void initMenuTree() {

        /**
         * 包含 "菜单管理" and "按钮/资源" ，意味着已经初始化，
         */
        List<TreeEntity> exist1 = treeRepository.findByName("菜单管理");
        List<TreeEntity> exist2 = treeRepository.findByName("按钮/资源");

        if (!exist1.isEmpty() && !exist2.isEmpty()) {
            logger.info("menu tree has initialized , continue ");
            return;
        }

        logger.info("initialize menu tree ... ");
        createMenuTree();

    }

    private void createMenuTree() {

        //系统其他的时候，这几种类型的父节点已经创建完毕，在下面添加子节点即可
        TreeEntity menu = treeRepository.getRoot(TreeType.Menu).get(); // menu , privilege
        TreeEntity departMent = treeRepository.getRoot(TreeType.DepartMent).get();
        TreeEntity standard = treeRepository.getRoot(TreeType.Standard).get();


        Set<TreeEntity> set = new HashSet<>();

        /**
         * 初始化 "菜单管理"
         */
        //一级菜单
        TreeEntity menu1 = new TreeEntity(TreeType.Menu, "菜单管理", 0, true, menu);
        TreeEntity menu11 = new TreeEntity(TreeType.Menu, "树结构管理", 0, true, menu1);
        TreeEntity menu12 = new TreeEntity(TreeType.Menu, "用户管理", 1, true, menu1);
        TreeEntity menu13 = new TreeEntity(TreeType.Menu, "角色管理", 3, true, menu1);
        TreeEntity menu14 = new TreeEntity(TreeType.Menu, "系统设置", 4, true, menu1);
        TreeEntity menu15 = new TreeEntity(TreeType.Menu, "邮箱管理", 5, true, menu1);
        TreeEntity menu16 = new TreeEntity(TreeType.Menu, "其他菜单", 6, true, menu1);
        set.addAll(Arrays.asList(menu1, menu11, menu12, menu13, menu14, menu15, menu16));

        // 树结构管理 (系统默认菜单)
        TreeEntity menu111 = new TreeEntity(TreeType.Menu, "菜单树", 0, false, menu11);
        TreeEntity menu112 = new TreeEntity(TreeType.Menu, "部门树", 1, false, menu11);
        TreeEntity menu113 = new TreeEntity(TreeType.Menu, "其他业务树", 2, false, menu11);
        set.addAll(Arrays.asList(menu111, menu112, menu113));

        // 用户管理  (系统默认菜单)
        TreeEntity menu121 = new TreeEntity(TreeType.Menu, "用户", 0, false, menu12);
        TreeEntity menu122 = new TreeEntity(TreeType.Menu, "用户组", 1, false, menu12);
        set.addAll(Arrays.asList(menu121, menu122));

        //角色管理  (系统默认菜单)
        TreeEntity menu131 = new TreeEntity(TreeType.Menu, "菜单角色", 0, false, menu13);
        TreeEntity menu132 = new TreeEntity(TreeType.Menu, "标准资源角色", 1, false, menu13);
        set.addAll(Arrays.asList(menu131, menu132));

        //系统设置  (菜单示例，可根据需要修改)
        TreeEntity menu141 = new TreeEntity(TreeType.Menu, "参数配置", 1, false, menu14);
        TreeEntity menu142 = new TreeEntity(TreeType.Menu, "定时任务", 2, false, menu14);
        TreeEntity menu143 = new TreeEntity(TreeType.Menu, "系统日志", 0, false, menu14);
        set.addAll(Arrays.asList(menu141, menu142, menu143));

        //邮箱管理 (菜单示例，可根据需要修改)
        TreeEntity menu151 = new TreeEntity(TreeType.Menu, "收件箱", 0, false, menu15);
        TreeEntity menu152 = new TreeEntity(TreeType.Menu, "发件箱", 1, false, menu15);
        TreeEntity menu153 = new TreeEntity(TreeType.Menu, "垃圾箱", 2, false, menu15);
        set.addAll(Arrays.asList(menu151, menu152, menu153));

        //其他菜单 (菜单示例，可根据需要修改)
        TreeEntity menu161 = new TreeEntity(TreeType.Menu, "菜单1", 0, false, menu16);
        TreeEntity menu162 = new TreeEntity(TreeType.Menu, "菜单2", 1, false, menu16);
        set.addAll(Arrays.asList(menu161, menu162));


        /**
         * 初始化 "按钮/资源" . 其他非菜单的资源，如按钮等其他页面显示元素，都设计为树的叶节点，便于授权
         */
        //一级菜单
        TreeEntity menu2 = new TreeEntity(TreeType.Menu, "按钮/资源", 1, true, menu);
        TreeEntity menu21 = new TreeEntity(TreeType.Menu, "菜单树", 0, true, menu2);  //应用中的文件资源
        TreeEntity menu22 = new TreeEntity(TreeType.Menu, "文件资源", 1, true, menu2);
        TreeEntity menu23 = new TreeEntity(TreeType.Menu, "其他资源", 2, true, menu2);
        TreeEntity menu24 = new TreeEntity(TreeType.Menu, "PDF 文件", 3, true, menu2);  //单独对 pdf 的操作
        set.addAll(Arrays.asList(menu2, menu21, menu22, menu23, menu24));


        //菜单树
        TreeEntity menu211 = new TreeEntity(TreeType.Menu, "树结构编辑", 0, false, menu21);
        TreeEntity menu212 = new TreeEntity(TreeType.Menu, "节点 URL", 0, false, menu21);
        TreeEntity menu213 = new TreeEntity(TreeType.Menu, "节点关联标准", 0, false, menu21);
        set.addAll(Arrays.asList(menu211, menu212, menu213));

        //文件资源
        TreeEntity menu221 = new TreeEntity(TreeType.Menu, "查看文件", 0, false, menu22);
        TreeEntity menu222 = new TreeEntity(TreeType.Menu, "下载附件", 0, false, menu22);
        TreeEntity menu223 = new TreeEntity(TreeType.Menu, "查看列表", 0, false, menu22);
        set.addAll(Arrays.asList(menu221, menu222, menu223));

        //其他资源
        TreeEntity menu231 = new TreeEntity(TreeType.Menu, "显示图片", 0, false, menu23);
        TreeEntity menu232 = new TreeEntity(TreeType.Menu, "资源列表", 0, false, menu23);
        set.addAll(Arrays.asList(menu231, menu232));
//        menu23.addChildToIndex(menu231, 0);
//        menu23.addChildToIndex(menu232, 1);


        //PDF 的操作
        TreeEntity menu241 = new TreeEntity(TreeType.Menu, "查看", 0, false, menu24);
        TreeEntity menu242 = new TreeEntity(TreeType.Menu, "打印", 1, false, menu24);
        TreeEntity menu243 = new TreeEntity(TreeType.Menu, "下载", 2, false, menu24);
        set.addAll(Arrays.asList(menu241, menu242, menu243));

        /**
         * 初始化 Standard
         */
        // standard 1
        TreeEntity standard1 = new TreeEntity(TreeType.Standard, "国内", 0, true, standard);
        TreeEntity standard11 = new TreeEntity(TreeType.Standard, "国家标准", 0, true, standard1);
        TreeEntity standard12 = new TreeEntity(TreeType.Standard, "行业标准", 1, true, standard1);
        TreeEntity standard13 = new TreeEntity(TreeType.Standard, "地方标准", 2, true, standard1);
        set.addAll(Arrays.asList(standard1, standard11, standard12, standard13));

        // standard 11
        TreeEntity standard111 = new TreeEntity(TreeType.Standard, "国家标准", 0, false, standard11);
        TreeEntity standard112 = new TreeEntity(TreeType.Standard, "军用标准", 1, false, standard11);
        TreeEntity standard113 = new TreeEntity(TreeType.Standard, "计量规程", 2, false, standard11);
        TreeEntity standard114 = new TreeEntity(TreeType.Standard, "计量规范", 3, false, standard11);
        set.addAll(Arrays.asList(standard111, standard112, standard113, standard114));

        // standard 12
        TreeEntity standard121 = new TreeEntity(TreeType.Standard, "安全生产", 0, false, standard12);
        TreeEntity standard122 = new TreeEntity(TreeType.Standard, "电子信息", 1, false, standard12);
        TreeEntity standard123 = new TreeEntity(TreeType.Standard, "医药健康", 2, false, standard12);
        set.addAll(Arrays.asList(standard121, standard122, standard123));

        // standard 13
        TreeEntity standard131 = new TreeEntity(TreeType.Standard, "北京DB11", 0, false, standard13);
        TreeEntity standard132 = new TreeEntity(TreeType.Standard, "天津DB12", 1, false, standard13);
        TreeEntity standard133 = new TreeEntity(TreeType.Standard, "河北DB13", 2, false, standard13);
        set.addAll(Arrays.asList(standard131, standard132, standard133));

        // standard 2
        TreeEntity standard2 = new TreeEntity(TreeType.Standard, "国外", 0, true, standard);
        TreeEntity standard21 = new TreeEntity(TreeType.Standard, "iso标准", 0, false, standard2);
        TreeEntity standard22 = new TreeEntity(TreeType.Standard, "iec标准", 1, false, standard2);
        TreeEntity standard23 = new TreeEntity(TreeType.Standard, "itu标准", 2, false, standard2);
        set.addAll(Arrays.asList(standard2, standard21, standard22, standard23));

        treeRepository.save(set); //事务状态下，自动保存
    }

    /**
     * 初始化一个超级用户 sysadmin ，作为系统保留用户，可以对系统做任何操作
     * -
     */
    private void initUser() {

        /**
         * 用户已经初始化
         */
        if (userRepository.findByLoginName("admin").isPresent()) {
            logger.info("user has initialized , continue");
            return;
        }

        logger.info("initialize user ...");

        UserEntity user = new UserEntity();
        user.setLoginName("admin");
        user.setUserName("系统管理员");
        user.setPassword(BCryptPassWordUtils.encode("sysadmin"));
        user.setValid(true);
        user.setEnabled(true);

        userRepository.save(user);

    }


    /**
     * 初始化用户组 ，作为系统保留组
     * -
     */
    private void initGroup() {

        if (groupRepository.findByName("系统管理员组").isPresent() &&
                groupRepository.findByName("用户组").isPresent()) {

            logger.info("group has initialized , continue");
            return;
        }

        logger.info("initialize group ...");


        GroupEntity admins = new GroupEntity();
        admins.setName("系统管理员组");
        admins.setValid(true);
        admins.setEnabled(true);

        GroupEntity users = new GroupEntity();
        users.setName("用户组");
        users.setValid(true);
        users.setEnabled(true);

        groupRepository.save(admins);
        groupRepository.save(users);

    }

    /**
     * 初始化一个超级用户 sysadmin ，作为系统保留用户，可以对系统做任何操作
     * -
     */
    private void initRole() {

        if (roleRepository.findByName("系统管理员角色").isPresent() &&
                roleRepository.findByName("用户角色").isPresent()) {

            logger.info("role has initialized , continue");
            return;
        }

        logger.info("initialize role ...");


        RoleEntity admins = new RoleEntity();
        admins.setName("系统管理员角色");
        admins.setValid(true);
        admins.setEnabled(true);

        RoleEntity users = new RoleEntity();
        users.setName("用户角色");
        users.setValid(true);
        users.setEnabled(true);

        roleRepository.save(admins);
        roleRepository.save(users);

    }

}
