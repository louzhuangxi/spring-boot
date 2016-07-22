package org.examples.spring.domain.manytomany.onetomany;

import javax.persistence.*;

/**
 * Description : TODO(关系表，可以增加其他的需要的属性)
 * User: h819
 * Date: 2014/11/18
 * Time: 16:54
 * To change this template use File | Settings | File Templates.
 */
@Entity
@Table(name = "example_ref_teacher_student", uniqueConstraints = {@UniqueConstraint
        (columnNames = {"teacher_id", "student_id"})
})  //做唯一性约束
public class RefTeacherStudentEntity {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    //增加的属性
    @Column(name = "mark")
    private String mark;

    //关系属性：关系是否已读
    @Column(name = "isRead", columnDefinition = "boolean default false")
    private boolean isRead;

    //关系属性：关系是否收藏
    @Column(name = "isMark", columnDefinition = "boolean default false")
    private boolean isMark;


    //关系属性：关系是否已发邮件
    @Column(name = "isMail", columnDefinition = "boolean default false")
    private boolean isMail;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "teacher_id", nullable = false)
    // 在多的一方 RefTeacherStudentEntity  表增加 teacher_id 字段，数值是 TeacherEntity 的 id，一的一方 TeacherEntity 表无变化
    private TeacherEntity teacher;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_id", nullable = false)
    // 在多的一方 RefTeacherStudentEntity 表增加 student_id 字段，数值是 StudentEntity 的 id ，一的一方 StudentEntity 表无变化
    private StudentEntity student;


    public RefTeacherStudentEntity() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMark() {
        return mark;
    }

    public void setMark(String mark) {
        this.mark = mark;
    }

    public TeacherEntity getTeacher() {
        return teacher;
    }

    public void setTeacher(TeacherEntity teacher) {
        this.teacher = teacher;
    }

    public StudentEntity getStudent() {
        return student;
    }

    public void setStudent(StudentEntity student) {
        this.student = student;
    }

    public Long getId() {
        return id;
    }

    private void setId(Long id) {
        this.id = id;
    }

    public boolean isRead() {
        return isRead;
    }

    public void setIsRead(boolean isRead) {
        this.isRead = isRead;
    }

    public boolean isMark() {
        return isMark;
    }

    public void setIsMark(boolean isMark) {
        this.isMark = isMark;
    }

    public boolean isMail() {
        return isMail;
    }

    public void setIsMail(boolean isMail) {
        this.isMail = isMail;
    }
}