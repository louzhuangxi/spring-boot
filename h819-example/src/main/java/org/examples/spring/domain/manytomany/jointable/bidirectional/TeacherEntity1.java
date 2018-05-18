package org.examples.spring.domain.manytomany.jointable.bidirectional;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

/**
 * Description : TODO()
 * User: h819
 * Date: 2014/11/18
 * Time: 16:55
 * To change this template use File | Settings | File Templates.
 */


//增加唯一性约束，和主键不同，还应该有独立的主键
//@Table(name = "foodsafe_foodadd_relation",
//        uniqueConstraints = {
//                @UniqueConstraint
//                        (columnNames = {"foodadd_class_id", "food_class_id"})
//        })
//@Entity
//@Table(name = "example_teacher2")
//@NamedEntityGraph(name = "examples.entity.manytomany.TeacherEntity2",//唯一id ,jpa 2.1属性
//        attributeNodes = {@NamedAttributeNode("students")})
@Getter
@Setter
@AllArgsConstructor
public class TeacherEntity1 {

    /**
     * 双向，发出端 ，接收端有两种方式，见 StudentEntity
     */
    /*
     * ManyToMany 用 set 比 list 效率高
     * https://vladmihalcea.com/the-best-way-to-use-the-manytomany-annotation-with-jpa-and-hibernate/
     * */
    @ManyToMany(fetch = FetchType.LAZY, targetEntity = StudentEntity1.class) // 双向向多对多，发出方设置，接收方均做设置，双方级联删除时，都会删除关系表
    @JoinTable(name = "example_ref_teacher_student2", //指定关联表名
            joinColumns = {@JoinColumn(name = "teacher_id", referencedColumnName = "id")},////生成的中间表的字段，对应关系的发出端(主表) id
            inverseJoinColumns = {@JoinColumn(name = "student_id", referencedColumnName = "id")}, //生成的中间表的字段，对应关系的接收端(从表) id
            uniqueConstraints = {@UniqueConstraint(columnNames = {"teacher_id", "student_id"})}) // 唯一性约束，是从表的联合字段
    public Set<StudentEntity1> students = new LinkedHashSet<>();

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;


    public TeacherEntity1() {

    }

    public TeacherEntity1(String name) {
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
     * 自定义方法，添加和学生的关系，会在关系表中添加一条 teacher/student  关系记录
     *
     * @param student
     */
    public void addStudent(StudentEntity1 student) {

        if (student != null)
            this.students.add(student);

    }

    /**
     * 增加多个
     *
     * @param students
     */
    public void addStudents(List<StudentEntity1> students) {

        if (students != null && !students.isEmpty())
            this.students.addAll(students);

    }

    /**
     * 自定义方法，删除和学生的关系，会删除关系表中 teacher/student  对应记录
     *
     * @param student
     */
    public void removeStudent(StudentEntity1 student) {

        if (student != null)
            this.students.remove(student);

    }

    /**
     * 删除多个
     *
     * @param students
     */
    public void removeStudents(List<StudentEntity1> students) {

        if (students != null && !students.isEmpty())
            this.students.removeAll(students);

    }

    /**
     * 会清除所有的关联关系表中  teacher/student  的关联数据
     */
    public void clearStudent() {
        this.students.clear();
    }
}
