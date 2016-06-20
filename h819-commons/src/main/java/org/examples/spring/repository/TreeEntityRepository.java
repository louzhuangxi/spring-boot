package org.examples.spring.repository;

import org.examples.spring.domain.TreeEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Repository to access {@link com.springapp.foodsafe.entity.Order}s.
 * <p/>
 * <p/>
 * 相当于 Dao 层，命名为 Repository ，是为了pring 、spring side 项目命名方法保持一致
 * <p/>
 * 1. 定义一个持久层的接口，该接口继承 JpaRepository、JpaSpecificationExecutor 系统接口，系统接口中定义了一些常用的增删改查，以及分页相关的方法。还有其他几个接口，可根据需要继承。
 * <p/>
 * 2. 除继承的接口中的方法外，可以在接口中声明自己需要的业务方法，或者覆盖默认的方法。 spring data 可以根据业务方法的名字，自动对应数据库字段来生成查询代码 。
 * 这种自动生成的代码容易出错，可添加  @Query来自定义 JPQL 语句
 * <p/>
 * 3. Spring 初始化容器时将会扫描配置文件中 repositories 属性的 base-package 指定的包目录及其子目录，为继承 Repository 或其子接口的接口创建代理对象（即接口的实现类），
 * 并将代理对象注册为 Spring Bean，业务层便可以通过 Spring 自动封装的特性来直接使用该对象，而不必为接口写实现类，直接使用接口提供的方法。
 * <p/>
 * CrudRepository 、 PagingAndSortingRepository、JpaRepository 等接口 默认添加了  @Transactional ，因为本接口继承了 JpaRepository，所有默认本接口都拥有默认的事务属性
 * 即：
 * 对 find(...) 读操作添加只读事务；
 * <p/>
 * 对 save(...) 和 delete(...)配置默认事务(默认的隔离级别、没有配置超时、只对运行时异常回滚)
 * <p/>
 * 如果覆盖默认的事务，需要在方法处添加 @Transactional
 */

/**
 * 注意，不用 Bean 标记，因为继承了 JpaRepository ，自动拥有可以被容器注入的 Bean 属性，并且本接口中的所有方法拥有默认事务属性
 */
public interface TreeEntityRepository extends JpaRepository<TreeEntity, Long>, JpaSpecificationExecutor {


    /**
     * jpa 2.1 @EntityGraph特性  : 解决 n+1 问题
     */
    // 解决设置为 LAZY 的级联对象，查询相关联的 Entity 时，每个关联的 Entity 查询都会发送一条的查询语句。加上 @EntityGraph 后，只发送一条语句，即可查询所有级联对象(对 one to many 尤其适用！)
    // 注意:
    // 1. 加在 repository 中的查询方法上，仅在调用该方法的时候，才起作用。定义时，定义在 Entity 上。
    // 2. 加上之后，总是会执行 EAGER 级联查询。
    //    EntityGraph.EntityGraphType.FETCH : @Entity 中，@NamedEntityGraph 中，attributeNodes 定义的属性按照 EAGER ,其他属性按照 LAZY
    //    EntityGraph.EntityGraphType.LOAD : : @Entity 中，@NamedEntityGraph 中，attributeNodes 定义的属性按照 EAGER ,其他按照指定或默认 LAZY
    // 3.对于复杂的对象，有多个不同的级联对象，需要创建不同的查询组合，那么在实体上可以设置不同的 name ，以适应不同的查询语句。见 TreeEntity 例子。
    // 4. 多重级联的情况，可以设置 subgraphs : order -> item -> product 详见 jpa21 example
//    @NamedEntityGraph(name = "graph.Order.items",
//            attributeNodes = @NamedAttributeNode(value = "items", subgraph = "items"),
//            subgraphs = @NamedSubgraph(name = "items", attributeNodes = @NamedAttributeNode("product")))

    // 此特性可以先不用，当出现查询过多时，再用来调优

    /**
     * 如果是修改，必需加上  @Modifying
     */

    @EntityGraph(value = "examples.entity.tree.parent", type = EntityGraph.EntityGraphType.FETCH)  //级联 parent
    @Query("select e from TreeEntity e where e.name=?1")
    TreeEntity findByName1(String name);

    @EntityGraph(value = "examples.entity.tree.children", type = EntityGraph.EntityGraphType.FETCH)  //级联 children
    @Query("select e from TreeEntity e where e.name=?1")
    TreeEntity findByName2(String name);

    @EntityGraph(value = "examples.entity.tree.parnt.children", type = EntityGraph.EntityGraphType.FETCH)
    //级联 parent, children
    @Query("select e from TreeEntity e where e.name=?1")
    TreeEntity findByName3(String name);

    //包含，此种查询在 oracle 中非常慢，用不到索引，所有尽量避免此类操作
    //在查询条件多的情况下，因为缩小了范围，可以加快查询速度
    //如果用到了级联查询，就加上  @EntityGraph ，否则不加。加上之后，总是会执行级联查询
    @EntityGraph(value = "examples.entity.tree", type = EntityGraph.EntityGraphType.FETCH)
    @Query("select e from TreeEntity e where e.name like %?1%")
    TreeEntity findByContainingName(String name);

    //以 name 开始，查询速度快，用到了索引
    //以 name 结束，速度也较快，但需要在数据库中建立反向索引
    //如果用到了级联查询，就加上  @EntityGraph ，否则不加。加上之后，总是会执行级联查询
    @EntityGraph(value = "examples.entity.tree", type = EntityGraph.EntityGraphType.FETCH)
    @Query("select e from TreeEntity e where e.name like ?1||'%'")
    TreeEntity findByStartingWithName(String name);

    //此方法，可以根据 level 获取所有的根节点，从而获取其子节点。
    @EntityGraph(value = "examples.entity.tree", type = EntityGraph.EntityGraphType.FETCH)
    @Query("select e from TreeEntity e where e.level=?1")
    List<TreeEntity> findByLevel(int level);

    @EntityGraph(value = "examples.entity.tree", type = EntityGraph.EntityGraphType.FETCH)
    @Query("select count(e) from TreeEntity e where e.level=?1")
    int countByLevel(int level);

    // 分页
    @Query("select count(e) from TreeEntity e where e.level=?1")
    Page<TreeEntity> findByLevel(String level, Pageable pageable);

    // 分页
    @Query("select count(e) from TreeEntity e where e.level=1")
    Page<TreeEntity> findByLevelOne(Pageable pageable);


    //只有在事务状态下，才可以更新
    @Modifying
    @Query("update TreeEntity e set e.name = ?2 where e.id=?1")
    void renameNameTo(String id, String name);


    //只有在事务状态下，才可以删除
    @Modifying
    @Query("delete from TreeEntity e where e.name=?1")
    void delByName(String name);

}
