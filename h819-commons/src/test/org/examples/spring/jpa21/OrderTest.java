package org.examples.spring.jpa21;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.examples.newfeatures.jpa21.entity.Order;
import org.examples.newfeatures.jpa21.entity.OrderItem;
import org.examples.newfeatures.jpa21.entity.Product;
import org.examples.newfeatures.jpa21.repository.OrderEntityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityGraph;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Subgraph;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

//使用 Spring-Test 框架
@RunWith(SpringJUnit4ClassRunner.class)
//需要加载的spring配置文件的地址
@ContextConfiguration(locations = {"classpath:applicationContext.xml"})
//测试时，加入事务属性
@TransactionConfiguration(transactionManager = "transactionManagerMySQL", defaultRollback = false)
@Transactional
public class OrderTest {
    @Autowired
    OrderEntityRepository orderEntityRepository;
    @PersistenceContext
    private EntityManager em;


    @Test
    public void testInit() {

        Order order = new Order();
        order.setOrderNumber("oder1");

        OrderItem item = new OrderItem();
        item.setVersion(1);

        OrderItem item2 = new OrderItem();
        item2.setVersion(2);

        Product product = new Product();
        product.setName("p1");

        Product product2 = new Product();
        product2.setName("p2");

        item.setProduct(product);
        item2.setProduct(product2);

        Set<OrderItem> items = new HashSet<OrderItem>();
        items.add(item);
        items.add(item2);
        order.setItems(items);

        orderEntityRepository.save(order);

    }


    // JPA 2.1 entity graphs are a better solution for it.
    // The definition of an entity graph is independent of the query and defines which attributes to fetch from the database.
    // An entity graph can be used as a fetch or a load graph.
    // If a fetch graph is used, only the attributes specified by the entity graph will be treated as FetchType.EAGER.All other attributes will be lazy.
    // If a load graph is used, all attributes that are not specified by the entity graph will keep their default fetch type.

    @Test  //发送一个查询语句，查出所有的信息，加载了三个 entity
    public void testJPA21() {

        EntityGraph graph = this.em.getEntityGraph("graph.Order.items");
        Map hints = new HashMap();
        hints.put("javax.persistence.fetchgraph", graph);
        Order o = this.em.find(Order.class, Long.valueOf(1l), hints);

        System.out.println(o.getId());
        for (OrderItem item : o.getItems()) {
            System.out.println("product name : " + item.getProduct().getName());

        }

    }

    @Test  //动态指定
    public void testJPA211() {

        //动态查询，只查询用到的关联实体，只发送一条语句；未用到的实体如果查询了，则会单独发送该实体的查询语句，不查询，则不发送
        //实现了用哪个就查询哪个功能，不必多查询多余的实体
        EntityGraph<Order> graph = this.em.createEntityGraph(Order.class);
        //只加载items 信息 ，如用到 product ，则会发送查询 product 实体的信息，没有用到，则不会发送
       //  graph.addAttributeNodes("items");


       //查询 items 和 product
        Subgraph<OrderItem> itemGraph = graph.addSubgraph("items");
        itemGraph.addAttributeNodes("product");


        Map<String, Object> hints = new HashMap<String, Object>();
        hints.put("javax.persistence.loadgraph", graph);

        Order o = this.em.find(Order.class, Long.valueOf(1l), hints);

        System.out.println(o.getId());
        for (OrderItem item : o.getItems()) {
            System.out.println("product name : " + item.getProduct().getName());

        }

    }

    @Test
    public void testSpringJPA21() {
        //发送一个查询语句，查出所有的信息，加载了三个 entity
        //spring jpa 1.7 没有实现动态查询，无法指定 Subgraph。 只实现了testJPA21

        Order o = orderEntityRepository.getById(1l);
        System.out.println(o.getId());
        for (OrderItem item : o.getItems()) {
            System.out.println("product name : " + item.getProduct().getName());

        }

    }

    @Test //lazy 查询级联的 entity ，如 product 如有 n 个 ，就会发送 n 条查询语句 。加载每个 entity 都会发送一条语句
    public void testJPA2() {

        Order order = orderEntityRepository.findOne(1l);
        System.out.println(order.getId());

        for (OrderItem item : order.getItems()) {

            System.out.println("product name : " + item.getProduct().getName());

        }
    }

}