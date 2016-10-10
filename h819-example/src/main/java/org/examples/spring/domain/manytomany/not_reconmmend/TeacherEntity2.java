package org.examples.spring.domain.manytomany.not_reconmmend;

import com.google.common.collect.Sets;

import javax.persistence.*;
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
public class TeacherEntity2 {


    @ManyToMany(fetch = FetchType.LAZY) // 双向向多对多，发出方设置，接收方均做设置
    @JoinTable(name = "example_ref_teacher_student2", //指定关联表名
            joinColumns = {@JoinColumn(name = "teacher_id", referencedColumnName = "id")},////生成的中间表的字段，对应关系的发出端(主表) id
            inverseJoinColumns = {@JoinColumn(name = "student_id", referencedColumnName = "id")}, //生成的中间表的字段，对应关系的接收端(从表) id
            uniqueConstraints = {@UniqueConstraint(columnNames = {"teacher_id", "student_id"})}) // 唯一性约束，是从表的联合字段
    public Set<StudentEntity2> students = Sets.newHashSet(); //set 可以过滤重复元素
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "name")
    private String name;


    public TeacherEntity2() {

    }

    public TeacherEntity2(String name) {
        this.name = name;
    }

    /**
     * 自定义方法，添加学生
     * 注意添加关联的方法(双向关联的，需要双方都要添加)
     * 属性 student.teachers设置为 public ，否则无法直接获取。直接设置其属性，get 方法不方便直接操作
     *
     * @param student
     */
    public void addStudent(StudentEntity2 student) {

        if (!this.students.contains(student)) {
            this.students.add(student);
            student.teachers.add(this);
        }

    }

    /**
     * 自定义方法，删除学生
     * 注意添加关联的方法(双向关联的，需要双方都要删除)
     * 属性 student.teachers 设置为 public ，否则无法直接获取。直接设置其属性，get 方法不方便直接操作
     *
     * @param student
     */
    public void removeStudent(StudentEntity2 student) {

        if (this.students.contains(student)) {
            this.students.remove(student);
            student.teachers.remove(this);

        }

    }


    /**
     * 可以直接清除关联关系
     */
    public void clearStudent() {
        this.students.clear();
    }

    public Set<StudentEntity2> getStudents() {
        return students;
    }

    public void setStudents(Set<StudentEntity2> students) {
        this.students = students;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
