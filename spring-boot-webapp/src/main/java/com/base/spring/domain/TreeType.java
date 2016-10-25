package com.base.spring.domain;

/**
 * 定义树结构对象的类型
 */
public enum TreeType {
    Menu,  // 菜单(按钮资源)树，可以通过树状结构展示，并且直接授权
    DepartMent, //部门
   // Group,// 组，用 GroupEntity 单独表示，没有层级
    Standard // 具体的业务类型，如果标准
}



