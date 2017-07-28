package com.base.spring.service.task;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.time.LocalDate;

/**
 * Description : TODO()
 * User: h819
 * Date: 2016/5/27
 * Time: 13:41
 * To change this template use File | Settings | File Templates.
 */
@Component
public class ScheduledTasks {

    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");

    /**
     * 定时任务
     */
    @Scheduled(fixedRate = 60 * 60 * 1000) //  60 分钟执行
    public void testTask() {
        System.out.println(LocalDate.now());
    }


}
