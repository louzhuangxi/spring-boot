/*******************************************************************************
 * Copyright (c) 2005, 2014 springside.github.io
 * <p/>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * <p/>
 * 参考 springside 4 项目，改动并做了扩展.  SearchFilter 类做了简化
 *******************************************************************************/
package com.base.spring.domain;

/**
 * 定义权限类别
 *z
 */
public enum PrivilegeType {
    TreeNode,  // 表示树状节点的访问权限
    PageElement //表示页面元素的可见性控制 ，可以是一个按钮，一个图片或者其他的内容
    //   File //表示文件修改权限
}



