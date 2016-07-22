package org.examples.j2se.concurrent;

import java.util.concurrent.ThreadPoolExecutor;

/**
 * Description : TODO(线程监控器)
 * User: h819
 * Date: 2015/3/12
 * Time: 16:52
 * To change this template use File | Settings | File Templates.
 */


//ThreadPoolExecutor 提供了一些方法，可以查看执行状态、线程池大小、活动线程数和任务数。所以，我通过一个监视线程在固定间隔输出执行信息。
public class MyThreadMonitor implements Runnable {

    private ThreadPoolExecutor executor;

    private int seconds;

    private boolean run = true;

    public MyThreadMonitor(ThreadPoolExecutor executor, int delay) {
        this.executor = executor;
        this.seconds = delay;
    }

    public void shutdown() {
        this.run = false;
    }

    @Override
    public void run() {
        while (run) {
            System.out.println(
                    String.format("[monitor] [%d/%d] Active: %d, Completed: %d, Task: %d, isShutdown: %s, isTerminated: %s",
                            this.executor.getPoolSize(),
                            this.executor.getCorePoolSize(),
                            this.executor.getActiveCount(),
                            this.executor.getCompletedTaskCount(),
                            this.executor.getTaskCount(),
                            this.executor.isShutdown(),
                            this.executor.isTerminated()));
            try {

                Thread.sleep(seconds * 1000);

            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }
}
