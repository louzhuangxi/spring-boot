package com.base.spring.vo;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

/**
 * Description : TODO(ztree json Data 格式，仅做传递数据用)
 * User: h819
 * Date: 2016/2/3
 * Time: 13:36
 * To change this template use File | Settings | File Templates.
 */

public class ZTreeNode {
    @Getter
    @Setter
    private long id;
    @Getter
    @Setter
    private String name;
    @Getter
    @Setter
    private String url;
    @Getter
    @Setter
    private boolean open;
    // @Getter
    // @Setter
    // 不用默认生成的，解决生成 json string ，is_ 问题
    private boolean isParent;
    private boolean checked;
    @Getter
    @Setter
    private List<ZTreeNode> children = new ArrayList<>();

    private ZTreeNode() {
    }


    /**
     * @param id
     * @param name
     * @param url
     * @param open     如果是父节点，是否则展开，true 时即使 isParent = false 也会展开
     * @param isParent true 时会显示为文件夹图标。即使无子节点数据，也会设置为文件夹图标父节点
     * @param checked  节点展示的时候，是否被选中
     */
    public ZTreeNode(long id, String name, String url, boolean open, boolean isParent, boolean checked) {


        this.id = id;
        this.name = name;
        this.url = url;
        this.open = open;
        this.isParent = isParent;
        this.checked = checked;
    }

    public void addChild(ZTreeNode child) {
        this.children.add(child);
    }


    public boolean getIsParent() {
        return isParent;
    }

    public void setIsParent(boolean parent) {
        isParent = parent;
    }

    public boolean getChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }


    @Override
    public boolean equals(Object o) {  // 放入 set 时，判断元素是否相同的依据
        if (this == o) return true;
        if (!(o instanceof ZTreeNode)) return false;

        ZTreeNode zTreeNode = (ZTreeNode) o;
        return id == zTreeNode.id;

    }

    @Override
    public int hashCode() {
        return (int) (id ^ (id >>> 32));
    }
}
