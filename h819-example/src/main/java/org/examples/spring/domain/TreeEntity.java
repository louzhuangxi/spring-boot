package org.examples.spring.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.h819.web.spring.jpa.entitybase.AbstractMySQLEntity;

import javax.persistence.*;

/**
 * Description : TODO(树状结构，不用这个，参考 TreeNodeEntity)
 * User: h819
 * Date: 14-7-7
 * Time: 下午3:18
 * To change this template use File | Settings | File Templates.
 */
@Entity
@Table(name = "example_tree", uniqueConstraints = {@UniqueConstraint
        (columnNames = {"name", "code"})})
@NamedEntityGraphs({
        @NamedEntityGraph(name = "examples.entity.tree.parent", attributeNodes = {@NamedAttributeNode("parent")}),//级联 parent
        @NamedEntityGraph(name = "examples.entity.tree.children", attributeNodes = {@NamedAttributeNode("children")}), // 级联 children
        @NamedEntityGraph(name = "examples.entity.tree.parent.children", attributeNodes = {@NamedAttributeNode("parent"), @NamedAttributeNode("children")})})
//级联 parent , children
//有相互关联关系的类：jackjson 转换为 json 结构的 String 的时候，可以避免进入死循环。但实际项目测试发现，如果本类还有其他复杂的关联关系，还是不行。只能用过滤属性的办法进行过滤。
@Getter
@Setter
@AllArgsConstructor
@Deprecated // 用 spring boot web 中的
public class TreeEntity extends AbstractMySQLEntity {

    @Column(name = "name")
    private String name;

    /**
     *    用 spring boot web 中的
     */
}
