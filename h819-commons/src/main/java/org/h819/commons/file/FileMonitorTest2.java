package org.h819.commons.file;

import java.io.IOException;
import java.nio.file.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 文件监控,1.7版本后，java.nio.file包提供了目录监控的api即 Watch Service API
 * User: Jianghui
 * Date: 12-6-18
 * Time: 下午2:04
 * To change this template use File | Settings | File Templates.
 */
public class FileMonitorTest2 {

    /**
     * @param args
     * @throws Exception
     */
    public static void main(String[] args) throws Exception {

        // 监控根目录，会递归监控子文件夹变化

        String rootDir = "D:\\download";//要监控的文件目录
        //因为是线程安全的所以可以放入ThreadPool中使用
        ExecutorService cachedThreadPool = Executors.newFixedThreadPool(1);
        cachedThreadPool.execute(new FileWatchTask(rootDir));

    }

    private static class FileWatchTask implements Runnable {
        private String fileDirectory;

        public FileWatchTask(String fileDirectory) {
            this.fileDirectory = fileDirectory;
        }

        public void run() {
            WatchService watchService = null;
            try {
                //获取当前文件系统的WatchService监控对象
                watchService = FileSystems.getDefault().newWatchService();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                //获取文件目录下的Path对象注册到 watchService中。
                //监听的事件类型，有创建，删除，以及修改
                Paths.get(fileDirectory)
                        .register(watchService, StandardWatchEventKinds.ENTRY_CREATE, StandardWatchEventKinds.ENTRY_DELETE,
                                StandardWatchEventKinds.ENTRY_MODIFY);
            } catch (IOException e) {
                e.printStackTrace();
            }
            while (true) {
                WatchKey key = null;
                try {
                    //获取可用key.没有可用的就wait
                    key = watchService.take();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                for (WatchEvent<?> event : key.pollEvents()) {
                    //todo
                    System.out.println(event.context() + "文件:" + event.kind() + "次数: " + event.count());
                }
                //重置，这一步很重要，否则当前的key就不再会获取将来发生的事件
                boolean valid = key.reset();
                //失效状态，退出监听
                if (!valid) {
                    break;
                }
            }
        }
    }
}
