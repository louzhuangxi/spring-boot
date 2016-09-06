package com.base.spring.vo;

/**
 * Description : TODO(fuelux tree json Data 格式，仅做传递数据用)
 * Description : TODO(每次点击节点，仅返回当前节点的子节点，所以不必设计成父子关系。)
 *
 * @see "http://getfuelux.com/javascript.html#tree"
 * User: h819
 * Date: 2016/2/3
 * Time: 13:36
 * To change this template use File | Settings | File Templates.
 */
public class FueluxTreeJsonNode {

    // http://getfuelux.com/javascript.html#tree

    /**
     * 显示一个 FueluxTree 节点，有 text 和 type 即可
     * attr 是这个节点的附加属性，可选
     */

    // 节点名字，必须
    private String text;
    // 节点类型，必须
    private FueluxTreeNodeType type;
    // 节点附加属性，可选
    // 前端调用附加属性方法： attr.id  (...)
    private FueluxTreeJsonNodeAttr attr;


    private FueluxTreeJsonNode() {
    }


    public FueluxTreeJsonNode(String text, FueluxTreeNodeType type, Long id, boolean itemSelected) {
        this.text = text;
        this.type = type;
        FueluxTreeJsonNodeAttr attr = new FueluxTreeJsonNodeAttr();
        attr.setId(id);
        attr.setItemSelected(itemSelected);
        this.attr = attr;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public FueluxTreeNodeType getType() {
        return type;
    }

    public void setType(FueluxTreeNodeType type) {
        this.type = type;
    }

    public FueluxTreeJsonNodeAttr getAttr() {
        return attr;
    }

    public void setAttr(FueluxTreeJsonNodeAttr attr) {
        this.attr = attr;
    }
}
