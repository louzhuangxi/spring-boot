package org.h819.commons.file.monitor;

import java.io.IOException;
import java.nio.file.*;

/**
 * 文件监控,1.7版本后，java.nio.file包提供了目录监控的api即 Watch Service API
 * User: Jianghui
 * Date: 2012-6-18
 * Time: 下午2:04
 * To change this template use File | Settings | File Templates.
 */
public class DirectoryWatcher implements Runnable {

    private String fileDirectory;


    public DirectoryWatcher(String fileDirectory) {
        this.fileDirectory = fileDirectory;
    }

    // print the events and the affected file
    private void printEvent(WatchEvent<?> event) {
        WatchEvent.Kind<?> kind = event.kind();
        if (kind.equals(StandardWatchEventKinds.ENTRY_CREATE)) {
            Path pathCreated = (Path) event.context();
            System.out.println(String.format("Entry created : %s", pathCreated));
        } else if (kind.equals(StandardWatchEventKinds.ENTRY_DELETE)) {
            Path pathDeleted = (Path) event.context();
            System.out.println(String.format("Entry pathDeleted : %s", pathDeleted));
        } else if (kind.equals(StandardWatchEventKinds.ENTRY_MODIFY)) {
            Path pathModified = (Path) event.context();
            System.out.println(String.format("Entry pathModified : %s", pathModified));

        }
    }

    @Override
    public void run() {
        WatchService watchService = null;
        try {
            //获取当前文件系统的WatchService监控对象
            watchService = FileSystems.getDefault().newWatchService();

            //获取文件目录下的Path对象注册到 watchService中。
            //监听的事件类型，有创建，删除，以及修改
            Paths.get(fileDirectory)
                    .register(watchService,
                            StandardWatchEventKinds.ENTRY_CREATE,
                            StandardWatchEventKinds.ENTRY_DELETE,
                            StandardWatchEventKinds.ENTRY_MODIFY);


            while (true) {
                //获取可用key.没有可用的就wait
                WatchKey key = watchService.take();

                for (WatchEvent<?> event : key.pollEvents()) {
                    //todo
                    printEvent(event);
                }
                //失效状态，退出监听 //重置，这一步很重要，否则当前的 key 就不再会获取将来发生的事件
                if (!key.reset()) {
                    key.cancel();
                    watchService.close();

                    break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
