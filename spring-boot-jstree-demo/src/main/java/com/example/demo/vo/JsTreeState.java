package com.example.demo.vo;

import lombok.Data;

/**
 * Description : TODO(JsTree 节点属性)
 * User: h819
 * Date: 2018/6/6
 * Time: 14:16
 * To change this template use File | Settings | File Templates.
 */
@Data
public class JsTreeState {
    // if the node should be initially selected
    private boolean selected;
    //if the node should be initially opened
    private boolean opened;
    // if the node should be disabled
    private boolean disabled;
    /**
     * 默认全部为 false
     */
    public JsTreeState() {
        this.selected = false;
        this.opened = false;
        this.disabled = false;
    }
}
