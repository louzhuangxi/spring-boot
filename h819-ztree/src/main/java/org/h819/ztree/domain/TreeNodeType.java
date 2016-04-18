package org.h819.ztree.domain;

/**
 * 定义树结构对象的类型，这样就可以组建不同类型的树
 * 和  ztree.ftl 中参数对应
 * <a data-url="page/ztree" href="#page/ztree?type=Menu">
 * <i class="menu-icon fa fa-caret-right"></i>
 * 菜单树
 * </a>
 */
public enum TreeNodeType {
    Menu,  // 菜单 ，表示该节点是一个权限树节点，可以通过树状结构展示，并且直接授权
    //其他的类别，表示节点是资源，资源的结构可以通过树状结构表示，例如部门树等，他的授权，在点击节点后弹出相关权限，再进行授权。
    // PrivilegeEntity 中记录授权情况
    DepartMent, //部门
    Group,// 组
    Standard // 具体的业务类型，如果标准
}



