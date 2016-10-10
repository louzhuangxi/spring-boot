package org.example.ztree.dto;

import java.util.ArrayList;
import java.util.List;

/**
 * Description : TODO(ztree json Data 格式，仅做传递数据用)
 * User: h819
 * Date: 2016/2/3
 * Time: 13:36
 * To change this template use File | Settings | File Templates.
 */
public class ZTreeJsonNode {

    private long id;
    private String name;
    private String url;
    private boolean open;
    private boolean isParent;
    private List<ZTreeJsonNode> children = new ArrayList<>();

    private ZTreeJsonNode() {
    }

    /**
     * @param name

     */
    public ZTreeJsonNode(String name) {
        this.name = name;
    }

    /**
     * @param id
     * @param name
     * @param url
     * @param open     如果是父节点，则展开，如果 children 不为空，即使 isParent = false 也会展开
     * @param isParent true 时会显示为文件夹图标。即使无子节点数据，也会设置为文件夹图标父节点
     */
    public ZTreeJsonNode(long id, String name, String url, boolean open, boolean isParent) {
        this.id = id;
        this.name = name;
        this.url = url;
        this.open = open;
        this.isParent = isParent;
    }

    public void addChild(ZTreeJsonNode child) {
        this.children.add(child);
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public boolean isOpen() {
        return open;
    }

    public void setOpen(boolean open) {
        this.open = open;
    }

    public boolean getIsParent() {
        return isParent;
    }

    public void setIsParent(boolean isParent) {
        isParent = isParent;
    }

    public List<ZTreeJsonNode> getChildren() {
        return children;
    }

    public void setChildren(List<ZTreeJsonNode> children) {
        this.children = children;
    }

    public boolean isParent() {
        return isParent;
    }

    public void setParent(boolean parent) {
        isParent = parent;
    }

    @Override
    public boolean equals(Object o) {  // 放入 set 时，判断元素是否相同的依据
        if (this == o) return true;
        if (!(o instanceof ZTreeJsonNode)) return false;

        ZTreeJsonNode zTreeNode = (ZTreeJsonNode) o;

        return id == zTreeNode.id;

    }

    @Override
    public int hashCode() {
        return (int) (id ^ (id >>> 32));
    }
}
