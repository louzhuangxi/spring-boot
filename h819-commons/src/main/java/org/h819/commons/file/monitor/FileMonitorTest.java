package org.h819.commons.file.monitor;

import org.apache.commons.io.filefilter.FileFilterUtils;
import org.apache.commons.io.filefilter.HiddenFileFilter;
import org.apache.commons.io.filefilter.IOFileFilter;
import org.apache.commons.io.monitor.FileAlterationListenerAdaptor;
import org.apache.commons.io.monitor.FileAlterationMonitor;
import org.apache.commons.io.monitor.FileAlterationObserver;

import java.io.File;
import java.util.concurrent.TimeUnit;

/**
 * 文件监控,commons io 实现  http://commons.apache.org/proper/commons-io/javadocs/api-release/index.html?org/apache/commons/io/monitor/package-summary.html
 * User: Jianghui
 * Date: 2012-6-18
 * Time: 下午2:04
 * To change this template use File | Settings | File Templates.
 */
public class FileMonitorTest {

    /**
     * @param args
     * @throws Exception
     */
    public static void main(String[] args) throws Exception {


        // 监控根目录，会递归监控子文件夹变化

        String rootDir = "D:\\download";
        // 轮询间隔 5 秒，通过重新启动 FileAlterationMonitor 实现
        long interval = TimeUnit.SECONDS.toMillis(5);


        // 创建一个文件观察器用于处理文件的格式
        // Create a FileFilter
        IOFileFilter directories = FileFilterUtils.and(
                FileFilterUtils.directoryFileFilter(),
                HiddenFileFilter.VISIBLE);


        IOFileFilter files = FileFilterUtils.and(
                FileFilterUtils.fileFileFilter(),
                FileFilterUtils.suffixFileFilter(".java"));

        IOFileFilter filter = FileFilterUtils.or(directories, files);

        // Create the File system observer and register File Listeners
        FileAlterationObserver observer = new FileAlterationObserver(
                rootDir, filter, null);

        observer.addListener(new FileMonitorFileListener()); //设置文件变化监听器

        //创建文件变化监听器
        FileAlterationMonitor monitor = new FileAlterationMonitor(interval, observer);

        // 开始监控
        monitor.start();

    }

    private static class FileMonitorFileListener extends FileAlterationListenerAdaptor

    {

        /**
         * 启动
         *
         * @param observer
         */
//        @Override
//        public void onStart(final FileAlterationObserver observer) {
//            System.out.println("The FileListener has started on "
//                    + observer.getDirectory().getAbsolutePath());
//        }


        /**
         * 文件创建执行
         */
        @Override
        public void onFileCreate(File file) {
            System.out.println("[文件 - 新建]:" + file.getAbsolutePath());
        }

        /**
         * 文件创建修改
         */
        @Override
        public void onFileChange(File file) {
            System.out.println("[文件 - 修改]:" + file.getAbsolutePath());
        }


        /**
         * 文件删除
         */
        @Override
        public void onFileDelete(File file) {
            System.out.println("[文件 - 删除]:" + file.getAbsolutePath());
        }

        /**
         * @param directory
         */
        @Override
        public void onDirectoryCreate(final File directory) {
            System.out.println("[文件夹 - 新建]:" + directory.getAbsolutePath());
        }

        /**
         * @param directory
         */
        @Override
        public void onDirectoryChange(final File directory) {
            System.out.println("[文件夹 - 修改]:" + directory.getAbsolutePath());
        }

        /**
         * @param directory
         */
        @Override
        public void onDirectoryDelete(final File directory) {
            System.out.println("[文件夹 - 删除]:" + directory.getAbsolutePath());
        }

        /**
         * 停止
         *
         * @param observer
         */
//        @Override
//        public void onStop(final FileAlterationObserver observer) {
//            System.out.println("The FileListener has stopped on " + observer.getDirectory().getAbsolutePath());
//        }

    }


}
