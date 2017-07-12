package org.h819.web.spring;

import org.apache.commons.lang3.time.DateFormatUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
public class TaskExample {

    private static final Logger logger = LoggerFactory.getLogger(TaskExample.class);

    public void job1() {
        logger.info("task run..");
        System.out.println("Task Job begin new : "+ DateFormatUtils.format(new Date(), "hh:MM:ss"));
    }
}
