package org.examples.spring.domain.onetomany.jointable.unidirectional.onetomany;

import com.google.common.collect.Sets;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.*;
import java.util.Set;

/**
 * Description : TODO()
 * User: h819
 * Date: 2014/11/18
 * Time: 16:54
 * To change this template use File | Settings | File Templates.
 */
@Entity
@Table(name = "example_parent2")
@NamedEntityGraph(name = "examples.entity.onetomany.ParentEntity2",//唯一id ,jpa 2.1属性
        attributeNodes = {@NamedAttributeNode("children")})
@Getter
@Setter
@AllArgsConstructor
public class ParentEntity2 {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    @OneToMany(fetch = FetchType.LAZY, orphanRemoval = true,targetEntity = ChildEntity2.class)
    @Fetch(FetchMode.SUBSELECT)
    //如果不用此句，默认是 FetchMode.SELECT ,查询每个被关联 child ，会发送一个查询语句，供发送 n+1个。
    //FetchMode.SUBSELECT 通过子查询一次完成，对于 child 过多的情况下，应用。//n+1问题，需要根据实际情况调试
    //对于OneToMany ,ManyToOne , ManyToMany 都有此类问题，可以通过调整抓取策略和启用缓存来解决。
    //FetchMode.SUBSELECT ：只有需要的时候，才会查询关联对象，会使用id in (…..)查询出所有关联的数据 .
    //对于 ManyToOne 一方，常常需要设置 FetchMode.JOIN ，解释详见 Many 一方
    // fetch="join",hibernate会通过select语句使用外连接来加载其关联实体或集合,此时lazy会失效
    @BatchSize(size = 100)//child 过多的情况下应用。
    @JoinTable//通过中间表（自动生成中间表），只在 parent 中设置
            (
                    name = "example_ref_parent2_child2", //指定关联表名
                    joinColumns = {@JoinColumn(name = "parent_id", referencedColumnName = "id")},//生成的中间表的字段，对应关系的发出端(JoinTable 关键字所在的类即是发出端) id
                    inverseJoinColumns = {@JoinColumn(name = "child_id", referencedColumnName = "id")},//生成的中间表的字段，对应关系的接收端(从表) id
                    uniqueConstraints = {@UniqueConstraint(columnNames = {"parent_id", "child_id"})// 唯一性约束，是从表的联合字段
                    }
            )
    private Set<ChildEntity2> children = Sets.newHashSet(); //set 可以过滤重复元素
    // Getters and Setters

    public ParentEntity2() {
    }

    public ParentEntity2(String name) {
        this.name = name;
    }

    /**
     * 增加几个增删关联关系表中数据的方法
     *
     * 1. 双向都是发出端的，双方都可以维护关联关系表中数据
     *
     * 2. 单向是发出端的，仅发出端可以维护关联关系表中数据
     * */

    /**
     * add , remove , clear 方法同 many to many TeacherEntity1
     */
}