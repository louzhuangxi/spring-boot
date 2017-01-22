package org.h819.commons.net.jftp.connection;

import org.h819.commons.MyDateUtilsJdk8;

import java.time.LocalDateTime;

/**
 * 奇葩的是，org.apache.commons.net.ftp.FTPFile 没有绝对路径属性，而 ftp 操作都是根据绝对路径属性进行的，造成很大不便
 * 这里重新定义一个 JFTPFile 类，增加绝对路径属性，便于操作。
 */
public class JFTPFile {

    private String name;
    private long size;
    private String absolutePath;
    private LocalDateTime lastModified;
    private boolean directory;

    /**
     * 必须有构造参数
     */
    private JFTPFile() {
    }

    public JFTPFile(String name, long size, String absolutePath, long mTime, boolean isDirectory) {

        this.name = name;
        this.size = size;
        this.absolutePath = absolutePath;
        this.lastModified = MyDateUtilsJdk8.getLocalDateTime(mTime);
        this.directory = isDirectory;
    }

    public String getName() {
        return name;
    }

    public long getSize() {
        return size;
    }

    public String getAbsolutePath() {
        return absolutePath;
    }

    public LocalDateTime getLastModified() {
        return lastModified;
    }

    public boolean isDirectory() {
        return directory;
    }
}
