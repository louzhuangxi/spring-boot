package org.examples.spring.domain.manytomany.onetomany;

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
@Table(name = "example_teacher")
@NamedEntityGraph(name = "examples.entity.manytomany.onetomany.TeacherEntity",//唯一id ,jpa 2.1属性
        attributeNodes = {@NamedAttributeNode("refTeacherStudent")})
@Getter
@Setter
@AllArgsConstructor
public class TeacherEntity3 {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "teacher", orphanRemoval = true)
    //只在 RefTeacherStudentEntity 表中变化， TeacherEntity 表中无变化
    // mappedBy = "teacher" ，parent 为 student 类中的属性
    @Fetch(FetchMode.SUBSELECT)   // n+1 问题见 one to many ParentEntity2.class
    @BatchSize(size = 100)//child 过多的情况下应用。
    private Set<RefTeacherStudentEntity> refTeacherStudent = Sets.newHashSet(); //set 可以过滤重复元素
    // Getters and Setters

    public TeacherEntity3() {
    }


    /**
     * add , remove , clear 方法同 TeacherEntity1
     */

}