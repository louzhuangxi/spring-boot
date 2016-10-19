package org.examples.spring.repository;

import org.examples.spring.domain.manytomany.onetomany.RefTeacherStudentEntity;
import org.examples.spring.domain.manytomany.onetomany.StudentEntity3;
import org.examples.spring.domain.manytomany.onetomany.TeacherEntity3;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;


public interface RefTeacherStudentEntityRepository extends JpaRepository<RefTeacherStudentEntity, Long>, JpaSpecificationExecutor {



    @Query("select e from RefTeacherStudentEntity e where e.teacher=?1 and e.student=?2")
    RefTeacherStudentEntity findByTeacherAndStudent(TeacherEntity3 teacherEntity, StudentEntity3 studentEntity);

}
