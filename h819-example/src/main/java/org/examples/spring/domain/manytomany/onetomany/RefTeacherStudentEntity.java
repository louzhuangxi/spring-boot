package org.examples.spring.domain.manytomany.onetomany;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

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
@Getter
@Setter
@AllArgsConstructor
public class RefTeacherStudentEntity {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    //增加的属性
    @Column(name = "mark")
    private boolean mark;

    //关系属性：关系是否已读
    @Column(name = "isRead", columnDefinition = "boolean default false")
    private boolean isRead;

    //关系属性：关系是否已收藏
    @Column(name = "isMark", columnDefinition = "boolean default false")
    private boolean isMark;


    //关系属性：关系是否已发邮件
    @Column(name = "isMail", columnDefinition = "boolean default false")
    private boolean isMail;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "teacher_id", nullable = false)
    // 在多的一方 RefTeacherStudentEntity  表增加 teacher_id 字段，数值是 TeacherEntity 的 id，一的一方 TeacherEntity 表无变化
    private TeacherEntity3 teacher;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_id", nullable = false)
    // 在多的一方 RefTeacherStudentEntity 表增加 student_id 字段，数值是 StudentEntity 的 id ，一的一方 StudentEntity 表无变化
    private StudentEntity3 student;


    public RefTeacherStudentEntity() {
    }


}