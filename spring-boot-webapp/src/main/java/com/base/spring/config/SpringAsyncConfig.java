package com.base.spring.config;

import com.base.spring.custom.exception.CustomAsyncExceptionHandler;
import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;

/**
 * Description : 对于执行操作数量非常大，并且每个操作结果之间相对独立，没有相互调用，不必一个操作等待另一个操作完成，可以考虑用多线程方式执行
 * User: h819
 * Date: 2017/3/9
 * Time: 15:05
 * To change this template use File | Settings | File Templates.
 */
@Configuration
@EnableAsync// 利用 @EnableAsync 注解，开启异步任务支持
public class SpringAsyncConfig implements AsyncConfigurer {
    // 配置类实现 AsyncConfigurer 接口并重写 getAsyncExcutor方法，并返回一个ThreadPoolTaskExevutor
    // 这样我们就获得了一个基于线程池的 TaskExecutor
    @Override
    public Executor getAsyncExecutor() {

        /**
         * 线程池按以下行为执行任务
         * 1. 当前线程数小于核心线程数时，创建线程，满足核心线程数量。
         * 2. 当前线程数大于等于核心线程数，且任务队列未满时，将任务放入任务队列。
         * 3. 当前线程数大于等于核心线程数，且任务队列已满
         *  - 若线程数小于最大线程数，创建线程
         *  - 若线程数等于最大线程数，抛出异常，拒绝任务
         */

        ThreadPoolTaskExecutor taskExecutor = new ThreadPoolTaskExecutor();

        //核心线程
        // 线程池初始化时，会最小保持核心线程数量的线程，一直存活。
        // 同时也是执行任务的线程数量
        // - 如果任务线程大于此数量，放入队列(setQueueCapacity)等待执行
        // - 如果任务线程小于此数量，则部分线程执行。
        // - 不能大于 setMaxPoolSize
        /**
         * 如何合理地估算线程池大小？
         * http://www.infoq.com/cn/articles/Java-Thread-Pool-Performance-Tuning
         *
         *  可以简单按如下公式计算
         *  N 为 CPU 内核线程数
         * - 如果是CPU密集型应用，则线程池大小设置为 N+1
         * - 如果是IO 密集型应用(如读取文件，连接网络，数据查询 等)，则线程池大小设置为 2N+1
         *
         * 可以根据机器的性能，调试合理的数目。
         * 有时候，单线程反倒比多线程速度快！
         *
         * */

        int n = Runtime.getRuntime().availableProcessors();

        //单位机器 , n=4

        //同时执行的线程数
        taskExecutor.setCorePoolSize(10); //2x4+1=9 , 设为 10
        //当线程数>=corePoolSize，且任务队列已满时 , 线程池会创建新线程来处理任务
        //当线程数=maxPoolSize，且任务队列已满时，线程池会拒绝处理任务而抛出异常
        taskExecutor.setMaxPoolSize(20);
        //当核心线程数达到最大时，新任务会放在队列中排队等待执行
        taskExecutor.setQueueCapacity(25);

        taskExecutor.setThreadNamePrefix("My-custom-thread : ");
        taskExecutor.initialize();
        return taskExecutor;

    }

    @Override
    public AsyncUncaughtExceptionHandler getAsyncUncaughtExceptionHandler() {
        return new CustomAsyncExceptionHandler();
    }
}
