package org.examples.j2se.concurrent;

import java.util.Date;
import java.util.concurrent.*;


/**
 * Description : TODO()
 * User: h819
 * Date: 2015/3/12
 * Time: 11:27
 * To change this template use File | Settings | File Templates.
 */

public class ThreadPoolExample {


    /**
     * spring task 中，也有对线程池的实现，见 http://docs.spring.io/spring/docs/current/spring-framework-reference/html/scheduling.html
     * 但 spring 能否实现线程之间的调度策略，死锁 等，不清楚
     */

    /**
     http://www.infoq.com/cn/articles/java-threadPool
     合理利用线程池能够带来三个好处。
     第一：降低资源消耗。通过重复利用已创建的线程降低线程创建和销毁造成的消耗。
     第二：提高响应速度。当任务到达时，任务可以不需要等到线程创建就能立即执行。
     第三：提高线程的可管理性。线程是稀缺资源，如果无限制的创建，不仅会消耗系统资源，还会降低系统的稳定性，使用线程池可以进行统一的分配，调优和监控。
     但是要做到合理的利用线程池，必须对其原理了如指掌。
     */

    /**
     * java.util.concurrent 并发编程   java 1.5 以上
     * <p/>
     * java.util.concurrent.executors 提供了 java.util.concurrent.executor 接口的一个Java实现，工具类，可以创建线程池。
     * <p/>
     * 一个线程池使用类 ExecutorService 的实例来表示，通过 ExecutorService 你可以提交任务，并进行调度执行。下面列举一些你可以通过 Executors 类来创建的线程池的类型：
     * <p/>
     * 1. Single Thread Executor :  创建一个单线程化的线程池，它只会用唯一的工作线程来执行任务，保证所有任务按照指定顺序(FIFO, LIFO, 优先级)执行。代码： Executors.newSingleThreadExecutor()
     * <p/>
     * 2. Cached Thread Pool : 创建一个可缓存线程池， 线程池为无限大，当执行第二个任务时第一个任务已经完成，会复用执行第一个任务的线程，而不用每次新建线程；如果没有空闲线程，则新建一个。如果线程超过60秒内没执行，那么将被终止并从池中删除。代码：Executors.newCachedThreadPool()
     * <p/>
     * 3. Fixed Thread Pool : 拥有固定线程数的线程池，如果没有任务执行，那么线程会一直等待；超出数量的的线程会在队列中等待   代码： Executors.newFixedThreadPool()
     * <p/>
     * 4. Scheduled Thread Pool :创建一个定长线程池，支持定时及周期性任务执行，可以设定定时时间和延迟等属性。代码：Executors.newScheduledThreadPool()
     * <p/>
     * 5. Single Thread Scheduled Pool : 只有一个线程，用来调度执行将来的任务，代码：Executors.newSingleThreadScheduledExecutor()        线程池为无限大，当执行第二个任务时第一个任务已经完成，会复用执行第一个任务的线程，而不用每次新建线程。
     */

    /**
     * 线程池创建，通过 Executors 工具类
     * 也可以通过 ThreadPoolExecutor 类来直接创建
     */

    public static void main(String[] args) {

        /**
         * http://www.infoq.com/cn/author/%E6%96%B9%E8%85%BE%E9%A3%9E
         *
         * 线程池中的线程，会并发执行
         *
         * 其他内容：
         * 锁机制 、 生产者消费者模式 、
         *
         */

        ThreadPoolExample example = new ThreadPoolExample();
        //缓存类型
        // example.cachedThreadPoolExample();
        //计划任务
        // example.scheduledThreadPoolExample();
        // 调用 callable 类型
        example.callableThreadPoolExample();

    }


    /**
     * 演示线程池创建及调用方法 ：执行 Callable 类型线程，并得到执行结果。
     */
    private void callableThreadPoolExample() {

        // ExecutorService threadPool = Executors.newCachedThreadPool();
        ExecutorService threadPool = Executors.newFixedThreadPool(3); //不同类型的线程池，线程结束的顺序不一样
        CompletionService<String> pool = new ExecutorCompletionService<String>(threadPool);

        Callable worker1 = new ThreadCallableExample1("ThreadCallableExample1");
        pool.submit(worker1);//只能添加 Callable 类型线程
        Callable worker2 = new ThreadCallableExample2("ThreadCallableExample2");
        pool.submit(worker2);//只能添加 Callable 类型线程
        Callable worker3 = new ThreadCallableExample3("ThreadCallableExample3");
        pool.submit(worker3);//只能添加 Callable 类型线程


        for (int i = 0; i < 3; i++) {  //循环线程数量要和上文符合，不然程序不会结束。
            try {

                String result = pool.take().get();  //根据执行结束的线程顺序，获取对应的结果。不同类型的线程池，线程结束的顺序不一样 ，但根据 result 可以区分出是哪个线程结束了

                System.out.println(result + " finished, can do something ...");
                //
                // 此处可以根据需要，判断某个线程结束后，对其进行处理。


            } catch (InterruptedException e) {

                e.printStackTrace();
            } catch (ExecutionException e) {

                e.printStackTrace();
            }
        }

        threadPool.shutdown();

        while (!threadPool.isTerminated()) {

            //此处在循环询问线程是否结束，是死循环，直至所有线程结束，执行下面的打印语句 System.out.println("Finished all threads")
        }

        System.out.println("\nFinished all threads");
    }


    /**
     * 演示线程池创建及调用方法  :  CachedThreadPool
     */
    private void cachedThreadPoolExample() {
        //创建线程池，也可以是其他的类型
        ExecutorService threadPool = Executors.newCachedThreadPool();

        //线程放入线程池 ，延迟 10 秒执行
        Runnable worker1 = new ThreadRunnableExample1("ThreadRunnableExample1");
        threadPool.execute(worker1);
        Runnable worker2 = new ThreadRunnableExample2("ThreadRunnableExample2");
        threadPool.execute(worker2);
        Runnable worker3 = new ThreadRunnableExample3("ThreadRunnableExample3");
        threadPool.execute(worker3);

        threadPool.shutdown();

        while (!threadPool.isTerminated()) {

            //此处在循环询问线程是否结束，是死循环，直至所有线程结束，执行下面的打印语句 System.out.println("Finished all threads")
        }

        System.out.println("Finished all threads");
    }

    /**
     * 演示线程池创建及调用方法   ScheduledThreadPool
     */
    private void scheduledThreadPoolExample() {
        //创建线程池，计划任务类型，可以设置很多参数
        ScheduledExecutorService threadPool = Executors.newScheduledThreadPool(5);
        //线程放入线程池 ，延迟 10 秒执行
        Runnable worker1 = new ThreadRunnableExample1("ThreadRunnableExample1");
        threadPool.schedule(worker1, 3, TimeUnit.SECONDS);
        Runnable worker2 = new ThreadRunnableExample2("ThreadRunnableExample2");
        threadPool.schedule(worker2, 6, TimeUnit.SECONDS);
        Runnable worker3 = new ThreadRunnableExample3("ThreadRunnableExample3");
        threadPool.schedule(worker3, 9, TimeUnit.SECONDS);
        threadPool.shutdown();

        while (!threadPool.isTerminated()) {

            //此处在循环询问线程是否结束，是死循环，直至所有线程结束，执行下面的打印语句 System.out.println("Finished all threads")
        }

        System.out.println("Finished all threads");
    }

    /**
     * @return
     */
//    public String call() {
//        //Long operations
//        try {
//            String result = pool.take().get();
//            //Compute the result
//        } catch (InterruptedException e) {
//            // 处理中断异常
//            e.printStackTrace();
//        } catch (ExecutionException e) {
//            // 处理无法执行任务异常
//            e.printStackTrace();
//        }
//
//        // 关闭线程池
//        threadPool.shutdown();
//
//        return "Run";
//
//    }
}

/**
 * Callable 和 Future接口 (Runnable是自从java1.1就有了，而Callable是1.5之后才加上去的)
 * Callable是类似于Runnable的接口，实现Callable接口的类和实现Runnable的类都是可被其它线程执行的任务。
 * Callable和Runnable有几点不同：
 * （1）Callable规定的方法是call()，而Runnable规定的方法是run().
 * （2）Callable的任务执行后可返回值，而Runnable的任务是不能返回值的。
 * （3）call()方法可抛出异常，而run()方法是不能抛出异常的。
 * （4）运行Callable任务可拿到一个Future对象，Future 表示异步计算的结果。它提供了检查计算是否完成的方法，以等待计算的完成，并检索计算的结果。 通过Future对象可了解任务执行情况，可取消任务的执行，还可获取任务执行的结果。
 * （5）加入线程池运行，Runnable使用ExecutorService的execute方法，Callable使用submit方法。
 */

/**
 * 内部类：创建 Runnable 线程类的例子，里面可以执行操作。
 * 创建的线程类，放在线程池中执行。
 * 线程池的获得见上文。
 */
class ThreadRunnableExample1 implements Runnable {

    //可以设置任意类型参数，此处仅为示例
    private String command;

    public ThreadRunnableExample1(String s) {
        this.command = s;
    }

    @Override
    public void run() {
        System.out.println(Thread.currentThread().getName() + " Start. Command = " + command + ". time = " + new Date());
        // 此处为业务代码
    }


    @Override
    public String toString() {
        return this.command;
    }
}

class ThreadRunnableExample2 implements Runnable {

    //可以设置任意类型参数，此处仅为示例
    private String command;

    public ThreadRunnableExample2(String s) {
        this.command = s;
    }

    @Override
    public void run() {
        System.out.println(Thread.currentThread().getName() + " Start. Command = " + command + ". time = " + new Date());
        // 此处为业务代码
    }


    @Override
    public String toString() {
        return this.command;
    }
}

class ThreadRunnableExample3 implements Runnable {

    //可以设置任意类型参数，此处仅为示例
    private String command;

    public ThreadRunnableExample3(String s) {
        this.command = s;
    }

    @Override
    public void run() {
        System.out.println(Thread.currentThread().getName() + " Start. Command = " + command + ". time = " + new Date());
        // 此处为业务代码
    }


    @Override
    public String toString() {
        return this.command;
    }
}

/**
 * 内部类：创建 Callable 线程类的例子，里面可以执行操作。
 */
class ThreadCallableExample1 implements Callable {

    //可以设置任意类型参数，此处仅为示例
    private String command;

    public ThreadCallableExample1(String command) {
        this.command = command;
    }

    @Override
    public String call() throws Exception {
        System.out.println(Thread.currentThread().getName() + " Start. Command = " + command);
        // 此处为业务代码
        return command;
    }

    @Override
    public String toString() {
        return this.command;
    }
}

class ThreadCallableExample2 implements Callable {

    //可以设置任意类型参数，此处仅为示例
    private String command;

    public ThreadCallableExample2(String command) {
        this.command = command;
    }

    @Override
    public String call() throws Exception {
        System.out.println(Thread.currentThread().getName() + " Start. Command = " + command);
        // 此处为业务代码
        return command;
    }

    @Override
    public String toString() {
        return this.command;
    }
}

class ThreadCallableExample3 implements Callable {

    //可以设置任意类型参数，此处仅为示例
    private String command;

    public ThreadCallableExample3(String command) {
        this.command = command;
    }

    @Override
    public String call() throws Exception {
        System.out.println(Thread.currentThread().getName() + " Start. Command = " + command);
        // 此处为业务代码
        return command;
    }

    @Override
    public String toString() {
        return this.command;
    }
}