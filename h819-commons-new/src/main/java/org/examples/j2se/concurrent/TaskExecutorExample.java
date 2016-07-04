package org.examples.j2se.concurrent;

import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;

/**
 * Description : TODO()
 * User: h819
 * Date: 2015/3/13
 * Time: 17:07
 * To change this template use File | Settings | File Templates.
 */
public class TaskExecutorExample {

    private ThreadPoolTaskScheduler taskScheduler;

    public TaskExecutorExample(ThreadPoolTaskScheduler taskScheduler) {
        this.taskScheduler = taskScheduler;
    }

    public void printMessages() {
        for (int i = 0; i < 25; i++) {
            taskScheduler.submit(new ThreadRunnableExample("Message" + i));

        }
    }

    private class ThreadRunnableExample implements Runnable {

        private String message;

        public ThreadRunnableExample(String message) {
            this.message = message;
        }

        public void run() {
            System.out.println(message);
        }

    }
}

