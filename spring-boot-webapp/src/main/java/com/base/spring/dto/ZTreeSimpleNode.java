package com.base.spring.dto;

/**
 * Description : TODO(ztree simpleData 格式，仅做传递数据用)
 * User: h819
 * Date: 2016/2/3
 * Time: 13:36
 * To change this template use File | Settings | File Templates.
 */

@Deprecated  // 不用简单类型
public class ZTreeSimpleNode {

    private long id;
    private long pId;
    private String name;
    private String url;
    private boolean open;
    private boolean isParent;

    private ZTreeSimpleNode() {
    }

    /**
     * @param id
     * @param pId
     * @param name
     * @param url
     * @param open     如果是父节点，则展开，如果 children 不为空，即使 isParent = false 也会展开
     * @param isParent true 时会显示为文件夹图标。即使无子节点数据，也会设置为文件夹图标父节点
     */
    public ZTreeSimpleNode(long id, long pId, String name, String url, boolean open, boolean isParent) {
        this.id = id;
        this.pId = pId;
        this.name = name;
        this.url = url;
        this.open = open;
        this.isParent = isParent;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getpId() {
        return pId;
    }

    public void setpId(long pId) {
        this.pId = pId;
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

    public boolean isParent() {
        return isParent;
    }

    public void setParent(boolean parent) {
        isParent = parent;
    }

    @Override
    public boolean equals(Object o) {  // 放入 set 时，判断元素是否相同的依据
        if (this == o) return true;
        if (!(o instanceof ZTreeSimpleNode)) return false;

        ZTreeSimpleNode zTreeNode = (ZTreeSimpleNode) o;

        return id == zTreeNode.id;

    }

    @Override
    public int hashCode() {
        return (int) (id ^ (id >>> 32));
    }
}
