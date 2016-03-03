package org.examples.newfeatures.jpa21.entity;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * Description : TODO()
 * User: h819
 * Date: 2015/1/14
 * Time: 13:22
 * To change this template use File | Settings | File Templates.
 */


/**
 * NamedEntityGraph api
 */
//        NamedAttributeNode[]	attributeNodes
//        (Optional) A list of attributes of the entity that are included in this graph.(需要随 order 加载的 entity 列表)
//        boolean	includeAllAttributes
//        (Optional) Includes all of the attributes of the annotated entity class as attribute nodes in the NamedEntityGraph without the need to explicitly list them.
//        String	name
//        (Optional) The name of the entity graph.
//        NamedSubgraph[]	subclassSubgraphs
//        (Optional) A list of subgraphs that will add additional attributes for subclasses of the annotated entity class to the entity graph.
//        NamedSubgraph[]	subgraphs
//        (Optional) A list of subgraphs that are included in the entity graph.

/**
 * NamedAttributeNode api
 */

//        String	value
//        (Required) The name of the attribute that must be included in the graph.
//        String	keySubgraph
//        (Optional) If the attribute references a Map type, this element can be used to specify a subgraph for the Key in the case of an Entity key type.
//        String	subgraph
//        (Optional) If the attribute references a managed type that has its own AttributeNodes, this element is used to refer to that NamedSubgraph definition.

/**
 * NamedSubgraph api
 */

//        NamedAttributeNode[]	attributeNodes
//        (Required) The list of the attributes of the class that must be included.
//        String	name
//        (Required) The name of the subgraph as referenced from a NamedAttributeNode element.
//        Class	type
//        (Optional) The type represented by this subgraph.

@Entity
@Table(name = "example_jpa21_order")
@NamedEntityGraph(
        name = "graph.Order.items",
        attributeNodes = @NamedAttributeNode(value = "items", subgraph = "items"),
        subgraphs = @NamedSubgraph(name = "items",
                attributeNodes = @NamedAttributeNode("product")))
public class Order implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", updatable = false, nullable = false)
    private Long id = null;
    @Version
    @Column(name = "version")
    private int version = 0;

    @Column
    private String orderNumber;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "order")
    private Set<OrderItem> items = new HashSet<OrderItem>();


    public Order() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public String getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(String orderNumber) {
        this.orderNumber = orderNumber;
    }

    public Set<OrderItem> getItems() {
        return items;
    }

    public void setItems(Set<OrderItem> items) {
        this.items = items;
    }
}