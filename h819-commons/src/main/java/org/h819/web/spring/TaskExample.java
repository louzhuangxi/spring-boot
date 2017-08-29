package org.h819.web.spring;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * Description : TODO(演示 spring task 用法)
 * User: h819
 * Date: 14-4-16
 * Time: 上午11:17
 * To change this template use File | Settings | File Templates.
 */
@Service    //只要 spring 容器能够自动加载的 bean 就可以，不一定是 @Service
@Slf4j
public class TaskExample {

    //private static final Logger log = LoggerFactory.getLogger(TaskExample.class);

    public void job1() {
        log.info("task run..");
        System.out.println("Task Job begin new : "+ DateFormatUtils.format(new Date(), "hh:MM:ss"));
    }
}
