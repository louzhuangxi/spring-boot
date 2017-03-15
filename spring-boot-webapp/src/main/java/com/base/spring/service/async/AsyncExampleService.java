package com.base.spring.service.async;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.Random;
import java.util.concurrent.CompletableFuture;

/**
 * Description : TODO()
 * User: h819
 * Date: 2017/3/9
 * Time: 15:04
 * To change this template use File | Settings | File Templates.
 */
@Service  // 测试见测试代码 (，配置见 SpringAsyncConfig)
public class AsyncExampleService {

    public static Random random = new Random();


    //异步方法不能放在另外一个方法内
    //只能直接外部调用，否则就会变成阻塞主线程的同步任务
    @Deprecated // 此种方式不行
    @Async
    public CompletableFuture<String> fakeAsyncTask() throws InterruptedException {
        doTaskOne();
        doTaskTwo();
        doTaskThree();
        return CompletableFuture.completedFuture("包装在另外一个方法中，变成了单线程，只能直接调用异步方法。");
    }

    @Async
    // 通过@Async注解表明该方法是个异步方法，如果注解在类级别，则表明该类所有的方法都是异步方法。
    // 而这里的方法自动被注入使用 ThreadPoolTaskExecutor 作为 TaskExecutor
    // 方法的返回值可以是任意数据，可以通过 CompletableFuture.get()  获得。
    public CompletableFuture<String> doTaskOne() throws InterruptedException {
        System.out.println("开始做任务一");
        System.out.println("Execute method doTaskOne. thread name is : " + Thread.currentThread().getName());
        long start = System.currentTimeMillis();
        Thread.sleep(random.nextInt(10000));
        long end = System.currentTimeMillis();
        System.out.println("完成任务一，耗时：" + (end - start) + "毫秒");
        return CompletableFuture.completedFuture("任务一完成");
    }

    @Async
    public CompletableFuture<String> doTaskTwo() throws InterruptedException {
        System.out.println("开始做任务二");
        System.out.println("Execute method doTaskTwo. thread name is : " + Thread.currentThread().getName());
        long start = System.currentTimeMillis();
        Thread.sleep(random.nextInt(10000));
        long end = System.currentTimeMillis();
        System.out.println("完成任务二，耗时：" + (end - start) + "毫秒");
        return CompletableFuture.completedFuture("任务二完成");
    }

    @Async
    public CompletableFuture<String> doTaskThree() throws InterruptedException {
        System.out.println("开始做任务三");
        System.out.println("Execute method doTaskThree. thread name is : " + Thread.currentThread().getName());
        long start = System.currentTimeMillis();
        Thread.sleep(random.nextInt(10000));
        long end = System.currentTimeMillis();
        System.out.println("完成任务三，耗时：" + (end - start) + "毫秒");
        return CompletableFuture.completedFuture("任务三完成");
    }

}
