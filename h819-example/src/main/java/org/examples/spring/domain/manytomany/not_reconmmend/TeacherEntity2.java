package org.examples.spring.domain.manytomany.not_reconmmend;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.HashSet;
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
public class TeacherEntity2 {


    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;


    /**
     * 双向，发出端 ，接收端有两种方式，见 StudentEntity2
     */
    @ManyToMany(fetch = FetchType.LAZY, targetEntity = StudentEntity2.class) // 双向向多对多，发出方设置，接收方均做设置，双方级联删除时，都会删除关系表
    @JoinTable(name = "example_ref_teacher_student2", //指定关联表名
            joinColumns = {@JoinColumn(name = "teacher_id", referencedColumnName = "id")},////生成的中间表的字段，对应关系的发出端(主表) id
            inverseJoinColumns = {@JoinColumn(name = "student_id", referencedColumnName = "id")}, //生成的中间表的字段，对应关系的接收端(从表) id
            uniqueConstraints = {@UniqueConstraint(columnNames = {"teacher_id", "student_id"})}) // 唯一性约束，是从表的联合字段
    public Set<StudentEntity2> students = new HashSet<>(); //set 可以过滤重复元素


    public TeacherEntity2() {

    }

    public TeacherEntity2(String name) {
        this.name = name;
    }

    /**
     * 自定义方法，添加和学生的关系，会在关系表中添加一条记录
     *
     * @param student
     */
    public void addStudent(StudentEntity2 student) {

        if (student != null)
            this.students.add(student);

    }

    public void addStudents(List<StudentEntity2> students) {

        if (students != null && !students.isEmpty())
            this.students.addAll(students);

    }

    /**
     * 自定义方法，删除和学生的关系，会删除关系表中的一条记录
     *
     * @param student
     */
    public void removeStudent(StudentEntity2 student) {

        if (student != null)
            this.students.remove(student);

    }

    public void removeStudents(List<StudentEntity2> students) {

        if (students != null && !students.isEmpty())
            this.students.removeAll(students);

    }

    /**
     * 可以直接清除关联关系
     */
    public void clearStudent() {
        this.students.clear();
    }
}
