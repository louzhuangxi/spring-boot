package org.examples.spring.repository.logback;

import org.h819.web.spring.jpa.entitybase.logback.LoggingEventEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;



public interface LoggingEventEntityRepository extends JpaRepository<LoggingEventEntity, Long>, JpaSpecificationExecutor {

}
