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
//@Entity
//@Table(name = "example_student2")
//@NamedEntityGraph(name = "examples.entity.manytomany.StudentEntity2",//唯一id ,jpa 2.1属性
//        attributeNodes = {@NamedAttributeNode("teachers")})
public class StudentEntity2 {

    /**
     * 第二种写法：
     */
    //此方法，二者均做为发出端，双方级联删除时，都会删除关系表
    // 注意主从表，和 teacher 端设置正好相反
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "example_ref_teacher_student2", //指定关联表名，和发出方 teacher 指定的表名相同
            joinColumns = {@JoinColumn(name = "student_id", referencedColumnName = "id")},////生成的中间表的字段，对应关系的发出端(主表) id 。（和 teacher 方相反）
            inverseJoinColumns = {@JoinColumn(name = "teacher_id", referencedColumnName = "id")}, //生成的中间表的字段，对应关系的接收端(从表) id。（和 teacher 方相反）
            uniqueConstraints = {@UniqueConstraint(columnNames = {"teacher_id", "student_id"})}) // 唯一性约束，是从表的联合字段


    public Set<TeacherEntity2> teachers = Sets.newHashSet(); //set 可以过滤重复元素
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 第一种写法：
     */
    //此方法，区分了发出端和接收端，删除发出端时，会删除关系表中的关联关系，但删除接收端，不会删除关联关系
//    @ManyToMany(mappedBy = "students")
//    private List<TeacherEntity2> teachers = new ArrayList<TeacherEntity2>();
    @Column(name = "name")
    private String name;


    public StudentEntity2() {

    }

    public StudentEntity2(String name) {

        this.name = name;
    }


    /**
     * 自定义方法，添加老师
     * 注意添加关联的方法(双向关联的，需要双方都要添加)
     * 属性 teacher.students 设置为 public ，否则无法直接获取
     *
     * @param teacher
     */
    public void addTeacher(TeacherEntity2 teacher) {

        if (!this.teachers.contains(teacher)) {
            this.teachers.add(teacher);
            teacher.students.add(this);
        }

    }

    /**
     * 自定义方法，删除老师
     * 注意删除关联的方法(双向关联的，需要双方都要删除)
     * 属性 teacher.students 设置为 public ，否则无法直接获取
     *
     * @param teacher
     */

    public void removeTeacher(TeacherEntity2 teacher) {

        if (this.teachers.contains(teacher)) {
            this.teachers.remove(teacher);
            teacher.students.remove(this);
        }

    }

    /**
     * 可以直接清除关联关系
     */
    public void clearTeacher() {

        this.teachers.clear();

    }


    public Set<TeacherEntity2> getTeachers() {
        return teachers;
    }

    public void setTeachers(Set<TeacherEntity2> teachers) {
        this.teachers = teachers;
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
