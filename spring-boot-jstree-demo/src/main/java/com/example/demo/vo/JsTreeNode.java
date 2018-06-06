package com.example.demo.vo;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Description : TODO(ztree json Data 格式，仅做传递数据用)
 * User: h819
 * Date: 2016/2/3
 * Time: 13:36
 * To change this template use File | Settings | File Templates.
 */
@Data
@NoArgsConstructor
public class JsTreeNode {

    /**
     * 节点ID
     */
    private long id;
    /**
     * 节点名称
     */
    private String text;
    /**
     * 节点状态
     */
    private JsTreeState state;
    /**
     * 该节点的自定义数据，可以为任何类型，此处设为 String
     */
    private String icon;

    /**
     * 节点的子节点
     */
    //  private List<JsTreeNode> children;

    /**
     * 父节点id , 和  children 只能用一个，此处为了避免双向循环，用 parent
     * 根节点为  "#"
     */
    private long parent;
}
