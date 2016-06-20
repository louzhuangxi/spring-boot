package org.h819.commons.file.monitor;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Description : TODO()
 * User: h819
 * Date: 2016/5/20
 * Time: 12:12
 * To change this template use File | Settings | File Templates.
 */
public class Test {

    public static void main(String[] args) throws Exception {

        /**
         * 测试 jdk 方法 :DirectoryWatcher
         */
        // 监控根目录，会递归监控子文件夹变化
        String rootDir = "D:\\download";//要监控的文件目录
        //因为是线程安全的所以可以放入ThreadPool中使用
        ExecutorService cachedThreadPool = Executors.newFixedThreadPool(1);
        cachedThreadPool.execute(new DirectoryWatcher(rootDir));

    }
}
