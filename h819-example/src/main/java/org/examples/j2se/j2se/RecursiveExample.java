package org.examples.j2se.j2se;

import org.apache.commons.io.filefilter.IOFileFilter;

import java.io.File;
import java.io.FileFilter;
import java.util.Collection;

/**
 * Description : TODO(演示递归方法的实现)
 * User: h819
 * Date: 2015/10/10
 * Time: 14:43
 * To change this template use File | Settings | File Templates.
 */
public class RecursiveExample {

    private Collection<File> files = new java.util.LinkedList<File>();

    /**
     * 找到满足条件的文件，把结果保存到函数的参数中。files 不能在本函数中初始化，因为用到了递归，如果初始化的话，会
     */
    private Collection<File> listFiles(File directory, IOFileFilter filter, boolean includeSubDirectories) {

        File[] found = directory.listFiles((FileFilter) filter);

        if (found != null) {
            for (File file : found) {
                if (file.isDirectory()) {
                    if (includeSubDirectories) {
                        files.add(file);
                    }
                    listFiles(file, filter, includeSubDirectories);
                } else {
                    files.add(file);
                }
            }
        }
        return files;
    }

}
