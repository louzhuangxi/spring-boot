package com.base.spring.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * Description : TODO()
 * User: h819
 * Date: 2017/7/26
 * Time: 17:05
 * To change this template use File | Settings | File Templates.
 */
@Configuration
@EnableScheduling  //ensures that a background task executor is created. Without it, nothing gets scheduled.
public class TaskConfig {
}
