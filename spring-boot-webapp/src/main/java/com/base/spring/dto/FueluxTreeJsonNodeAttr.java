package com.base.spring.dto;

/**
 * Description : TODO(FueluxTree 节点附加属性，属性名称和个数不是固定的，可以自定义，只要页面段能够调用即可)
 * User: h819
 * Date: 2016/2/20
 * Time: 16:57
 * To change this template use File | Settings | File Templates.
 */
public class FueluxTreeJsonNodeAttr {

   // http://getfuelux.com/javascript.html#tree
    private Long id;
    private boolean hasChildren = true; //是否有子节点
    //CSS classes that will be added to .tree-item or .tree-branch
    private String cssClass;
   // CSS classes that will be added to .icon-item
    private String dataIcon; //实际上应为  data-icon ，java 变量命名不允许 - ，转换为 json 字符串时，统一替换



    /**
     * 禁止无参构造，避免出错
     */
    private FueluxTreeJsonNodeAttr() {
    }

    public FueluxTreeJsonNodeAttr(Long id) {
        this.id = id;
    }

    public FueluxTreeJsonNodeAttr(Long id, boolean hasChildren) {
        this.id = id;
        this.hasChildren = hasChildren;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public boolean isHasChildren() {
        return hasChildren;
    }

    public void setHasChildren(boolean hasChildren) {
        this.hasChildren = hasChildren;
    }

    public String getCssClass() {
        return cssClass;
    }

    public void setCssClass(String cssClass) {
        this.cssClass = cssClass;
    }

    public String getDataIcon() {
        return dataIcon;
    }

    public void setDataIcon(String dataIcon) {
        this.dataIcon = dataIcon;
    }
}
