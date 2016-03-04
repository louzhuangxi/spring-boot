package org.examples.spring.service;

import org.examples.spring.domain.manytomany.onetomany.RefTeacherStudentEntity;
import org.examples.spring.domain.manytomany.onetomany.StudentEntity;
import org.examples.spring.domain.manytomany.onetomany.TeacherEntity;
import org.examples.spring.repository.RefTeacherStudentEntityRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 有中间表的 one to many ，无法在双方删除对方，只能在 service 层实现。
 */

@Service
@Transactional(readOnly = true)
public class RefTeacherStudentEntityService {

    private static Logger logger = LoggerFactory.getLogger(RefTeacherStudentEntityService.class);

    @Autowired
    private RefTeacherStudentEntityRepository refTeacherStudentEntityRepository;


    /**
     * 解除关联关系，互为主动方
     *
     * @param teacherEntity
     * @param studentEntity
     */
    @Transactional(readOnly = false)
    public void removeRefTeacherStudent(TeacherEntity teacherEntity, StudentEntity studentEntity) {
        RefTeacherStudentEntity refTeacherStudentEntity = refTeacherStudentEntityRepository.findByTeacherAndStudent(teacherEntity, studentEntity);
        //解除关联，在事物状态下，会自动更新 teacherEntity 和 studentEntity ，没有经过验证！！！！
//        teacherEntity.getRefTeacherStudent().remove(studentEntity);
//        studentEntity.getRefTeacherStudent().remove(teacherEntity);
        //如果这一句可行，那么就不需要上面的解除操作！！！！
        refTeacherStudentEntityRepository.delete(refTeacherStudentEntity); //是否需要设置级联操作????      需要设置级联操作  @OneToMany(cascade = CascadeType.ALL ... )

    }

    /**
     * 增加关系，互为主动方。增加关系时，会同时增加一个关系的属性
     *
     * @param teacherEntity
     * @param studentEntity
     * @param read
     * @param mark
     * @param mail
     */
    @Transactional(readOnly = false)
    public void addRefTeacherStudent(TeacherEntity teacherEntity, StudentEntity studentEntity, Boolean read, Boolean mark, Boolean mail) {

        RefTeacherStudentEntity entity = refTeacherStudentEntityRepository.findByTeacherAndStudent(teacherEntity, studentEntity);
        //已经建立关联
        if (entity != null) {

            if (read != null)
                entity.setIsRead(read);

            if (mark != null)
                entity.setIsMark(mark);

            if (mail != null)
                entity.setIsMail(mail);

            refTeacherStudentEntityRepository.save(entity);
            return;
        }

        //没有建立关联，新建立
        RefTeacherStudentEntity entity_new = new RefTeacherStudentEntity();
        entity_new.setTeacher(teacherEntity);
        entity_new.setStudent(studentEntity);
        if (read != null)
            entity_new.setIsRead(read);
        if (mark != null)
            entity_new.setIsMark(mark);

        if (mail != null)
            entity_new.setIsMail(mail);

        refTeacherStudentEntityRepository.save(entity_new);

    }
}

