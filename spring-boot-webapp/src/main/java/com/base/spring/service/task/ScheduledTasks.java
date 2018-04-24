package com.base.spring.service.task;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;

/**
 * Description : TODO()
 * User: h819
 * Date: 2016/5/27
 * Time: 13:41
 * To change this template use File | Settings | File Templates.
 */

//与 quartz 区别
// spring task 是单机的，quartz 基于 mysql 可以达到分布式环境下同一任务只在一台机器上执行一次的目的
//需要
// @Configuration
// @EnableScheduling
//http://qinghua.github.io/spring-scheduler/
@Service
public class ScheduledTasks {

    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");

    /**
     * 定时任务
     */
    //  @Scheduled(cron = "0 0/5 * * * *") //每隔 10 分钟，测试用
    // @Scheduled(cron = "0 0 3 * * ?") //每天凌晨 3 点
    @Scheduled(fixedRate = 60 * 60 * 1000) //  60 分钟执行
    public void testTask() {
        System.out.println(LocalDateTime.now());
    }
}
