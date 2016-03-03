package com.base.spring.domain;

/**
 * 定义树结构对象的类型，这样就可以组建不同类型的树
 * 和  ztree.ftl 中参数对应
 * <a data-url="page/ztree" href="#page/ztree?type=Menu">
 * <i class="menu-icon fa fa-caret-right"></i>
 * 菜单树
 * </a>
 */
public enum TreeNodeType {
    Menu,  // 菜单
    DepartMent, //部门
    Group,// 组
    Standard // 具体的业务类型，如果标准
}



