package org.examples.spring.domain.manytomany.onetomany;

import com.google.common.collect.Sets;
import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.*;
import java.util.Set;

/**
 * Description : TODO()
 * User: h819
 * Date: 2014/11/18
 * Time: 16:55
 * To change this template use File | Settings | File Templates.
 */
@Entity
@Table(name = "example_student")
@NamedEntityGraph(name = "examples.entity.manytomany.onetomany.StudentEntity",//唯一id ,jpa 2.1属性
        attributeNodes = {@NamedAttributeNode("refTeacherStudent")})
public class StudentEntity {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "name")
    private String name;



    @OneToMany(fetch = FetchType.LAZY, mappedBy = "student", orphanRemoval = true)  //只在 RefTeacherStudentEntity 表中变化， StudentEntity 表中无变化
    // mappedBy = "student" ，parent 为 RefTeacherStudentEntity 类中的属性
    @Fetch(FetchMode.SUBSELECT)
    //如果不用此句，默认是 FetchMode.SELECT ,查询每个被关联 student ，会发送一个查询语句，供发送 n+1个。FetchMode.SUBSELECT 通过子查询一次完成，对于 student 过多的情况下，应用。
    //n+1问题，需要根据实际情况调试
    @BatchSize(size = 100)//student 过多的情况下应用。
    private Set<RefTeacherStudentEntity> refTeacherStudent = Sets.newHashSet(); //set 可以过滤重复元素



    public StudentEntity() {

    }


    /**
     * 增加，删除一个 TeacherEntity，由于用到了 Repository ,在 Service 层实现，这里无法实现，见 RefTeacherStudentEntityService
     */


    private Long getId() {
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

    public Set<RefTeacherStudentEntity> getRefTeacherStudent() {
        return refTeacherStudent;
    }

    public void setRefTeacherStudent(Set<RefTeacherStudentEntity> refTeacherStudent) {
        this.refTeacherStudent = refTeacherStudent;
    }
}
