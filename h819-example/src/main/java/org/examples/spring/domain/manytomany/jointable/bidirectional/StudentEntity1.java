package org.examples.spring.domain.manytomany.jointable.bidirectional;

import com.google.common.collect.Sets;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Set;

/**
 * Description : TODO()
 * User: h819
 * Date: 2014/11/18
 * Time: 16:55
 * To change this template use File | Settings | File Templates.
 */
//@Entity
//@Table(name = "example_student2")
//@NamedEntityGraph(name = "examples.entity.manytomany.StudentEntity2",//唯一id ,jpa 2.1属性
//        attributeNodes = {@NamedAttributeNode("teachers")})
@Getter
@Setter
@AllArgsConstructor
public class StudentEntity1 {


    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    /**
     * 	cascade 默认，没有设置
     *
     * 1.双向第一种写法：teacher 作为发出端，student 作为接收端
     * 2.此方法，区分了发出端和接收端:
     * 1).发出端 teacher 删除 student 时，会删除关系表中的关联关系
     * 2).但接收端 student 删除 teacher，不会删除关系表中的关联关系
     *
     * @ManyToMany(mappedBy = "students", targetEntity = TeacherEntity2.class)
     *  public Set<TeacherEntity2> teachers = Sets.newHashSet(); //set 可以过滤重复元素
     */

    /**
     * 双向第二种写法： 二者均做为发出端，双方级联删除时，都会删除关系表
     */
    // 注意主从表，和 teacher 端设置正好相反
    @ManyToMany(fetch = FetchType.LAZY, targetEntity = TeacherEntity1.class)
    @JoinTable(name = "example_ref_teacher_student2", //指定关联表名，和发出方 teacher 指定的表名相同
            joinColumns = {@JoinColumn(name = "student_id", referencedColumnName = "id")},////生成的中间表的字段，对应关系的发出端(主表) id 。（和 teacher 方相反）
            inverseJoinColumns = {@JoinColumn(name = "teacher_id", referencedColumnName = "id")}, //生成的中间表的字段，对应关系的接收端(从表) id。（和 teacher 方相反）
            uniqueConstraints = {@UniqueConstraint(columnNames = {"teacher_id", "student_id"})}) // 唯一性约束，是从表的联合字段
    public Set<TeacherEntity1> teachers = Sets.newHashSet(); //set 可以过滤重复元素


    public StudentEntity1() {

    }

    public StudentEntity1(String name) {

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
