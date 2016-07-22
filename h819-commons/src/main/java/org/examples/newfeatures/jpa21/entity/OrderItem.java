package org.examples.newfeatures.jpa21.entity;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Description : TODO()
 * User: h819
 * Date: 2015/1/14
 * Time: 13:23
 * To change this template use File | Settings | File Templates.
 */
@Entity
@Table(name = "example_jpa21_order_item")
public class OrderItem implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", updatable = false, nullable = false)
    private Long id = null;
    @Version
    @Column(name = "version")
    private int version = 0;

    @Column
    private int quantity;

    @ManyToOne (fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    private Order order;


    @ManyToOne(cascade = CascadeType.ALL,fetch = FetchType.LAZY)
    private Product product;

    public OrderItem() {
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

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }
}
