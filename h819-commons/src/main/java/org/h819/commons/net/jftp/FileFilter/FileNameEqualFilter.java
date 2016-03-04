package org.h819.commons.net.jftp.FileFilter;

import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPFileFilter;

/**
 * Description : TODO(单个文件判断，文件名等于)
 * User: h819
 * Date: 2015/7/3
 * Time: 17:26
 * To change this template use File | Settings | File Templates.
 */
public class FileNameEqualFilter implements FTPFileFilter {

    //String prefix, String suffix

    private String fullFileName = new String();

    public FileNameEqualFilter(String fullFileName) {
        this.fullFileName = fullFileName;
    }

    @Override
    public boolean accept(FTPFile ftpFile) {

        if (fullFileName.isEmpty())
            return ftpFile.isFile();

        return (ftpFile.isFile() && ftpFile.getName().equals(fullFileName));
    }
}
