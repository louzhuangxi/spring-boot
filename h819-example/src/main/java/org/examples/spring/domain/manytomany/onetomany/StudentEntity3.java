package org.examples.spring.domain.manytomany.onetomany;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.*;
import java.util.LinkedHashSet;
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
@Getter
@Setter
@AllArgsConstructor
public class StudentEntity3 {

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
    private Set<RefTeacherStudentEntity> refTeacherStudent =  new LinkedHashSet<>();



    public StudentEntity3() {

    }



    /**
     * add , remove , clear 方法同 TeacherEntity1
     */
}
