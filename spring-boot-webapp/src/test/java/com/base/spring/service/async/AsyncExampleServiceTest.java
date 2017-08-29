package com.base.spring.service.async;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.concurrent.CompletableFuture;

/**
 * Description : TODO()
 * User: h819
 * Date: 2017/3/13
 * Time: 12:03
 * To change this template use File | Settings | File Templates.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class AsyncExampleServiceTest {

    @Autowired
    AsyncExampleService task;

    /**
     * 每个线程的名字都不同，是多线程并行执行,注意要用 CompletableFuture
     *
     * @throws Exception
     */
    @Test
    public void executeAsyncTask() throws Exception {

        long start = System.currentTimeMillis();
        String allStr;
        CompletableFuture<String> task1 = task.doTaskOne();
        CompletableFuture<String> task2 = task.doTaskTwo();
        CompletableFuture<String> task3 = task.doTaskThree();
        // CompletableFuture 能确认三个线程都结束，再退出主线程
        //否则没等线程结束，主线程就结束了（程序停止了）
        //wait until all they are completed.
        CompletableFuture.allOf(task1, task2, task3).join(); // 等待所有线程执行完成
        //==
        //线程结束后，可以获取线程的返回值，进行汇总处理
        // 更复杂的例子，见中关村数据查询
        allStr = task1.get() + task2.get() + task3.get();
        System.out.println("all str = " + allStr);
        long end = System.currentTimeMillis();
        System.out.println("任务全部完成，总耗时：" + (end - start) / 1000 + "秒");

    }

    /**
     * 放在了另一个方法的内部
     * 可以看到，线程的名字都一样 ，是单线程顺序执行
     *
     * @throws Exception
     */
    @Test
    public void executeAsyncTask2() throws Exception {
        long start = System.currentTimeMillis();
        CompletableFuture<String> taskAll = task.fakeAsyncTask();
        CompletableFuture.allOf(taskAll).join();
        long end = System.currentTimeMillis();
        System.out.println("任务全部完成，总耗时：" + (end - start) / 1000 + "秒");

    }

}