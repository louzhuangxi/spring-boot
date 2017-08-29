package org.examples.spring.service.logback;

import org.examples.spring.repository.logback.LoggingEventEntityRepository;
import org.examples.spring.repository.logback.LoggingEventPropertyEntityRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class LogbackService {

    //private static final Logger logger = LoggerFactory.getLogger(LogbackService.class);


    @Autowired
    LoggingEventEntityRepository loggingEventEntityRepository;

    @Autowired
    LoggingEventPropertyEntityRepository loggingEventPropertyEntityRepository;


    /**
     * 删除 LoggingEvent 因为 LoggingEventEntity 、 LoggingEventPropertyEntity 有关联关系，不能直接删除，需分两步删除
     * 表结构和 Entity 的对应关系为官方默认，没有进行修改.
     *
     * @param eventId
     */
    @Transactional( readOnly = false)
    public void deleteLoggingEnvent(long eventId) {

        loggingEventPropertyEntityRepository.deleteByEventId(eventId);
        loggingEventEntityRepository.delete(eventId);

    }


}
