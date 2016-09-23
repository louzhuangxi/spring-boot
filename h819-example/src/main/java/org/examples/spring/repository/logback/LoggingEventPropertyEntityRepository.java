package org.examples.spring.repository.logback;

import org.h819.web.spring.jpa.entitybase.logback.LoggingEventPropertyEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;



public interface LoggingEventPropertyEntityRepository extends JpaRepository<LoggingEventPropertyEntity, Long>, JpaSpecificationExecutor {

    @Modifying
    @Query("delete from LoggingEventPropertyEntity e where e.eventId=?1")
    void deleteByEventId(long eventId);

}
