package org.examples.newfeatures.jpa21.repository;

import org.examples.newfeatures.jpa21.entity.Order;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;


public interface OrderEntityRepository extends JpaRepository<Order, Long>, JpaSpecificationExecutor {

    //EntityGraph.EntityGraphType.FETCH : @Entity 中，@NamedEntityGraph 中，attributeNodes 定义的属性按照 EAGER ,其他属性按照 LAZY
    //EntityGraph.EntityGraphType.LOAD : : @Entity 中，@NamedEntityGraph 中，attributeNodes 定义的属性按照 EAGER ,其他按照指定或默认 LAZY
//参看 Test 测试
    @EntityGraph(value = "graph.Order.items", type = EntityGraph.EntityGraphType.FETCH)
    @Query("select e from Order e where e.id=?1")
    Order getById(long id);

}
