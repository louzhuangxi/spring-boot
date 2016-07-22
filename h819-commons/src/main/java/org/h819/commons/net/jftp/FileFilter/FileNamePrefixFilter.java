package org.h819.commons.net.jftp.FileFilter;

import com.google.common.collect.Lists;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPFileFilter;

import java.util.Arrays;
import java.util.List;

/**
 * Description : TODO(文件名称中包含)
 * User: h819
 * Date: 2015/7/3
 * Time: 17:26
 * To change this template use File | Settings | File Templates.
 */
public class FileNamePrefixFilter implements FTPFileFilter {

    //String prefix, String suffix

    private List prefixList = Lists.newArrayList();

    public FileNamePrefixFilter(String[] prefixContains) {
        this.prefixList = Arrays.asList(prefixContains);
    }

    @Override
    public boolean accept(FTPFile ftpFile) {

        if (prefixList.isEmpty())
            return ftpFile.isFile();

        return (ftpFile.isFile() && prefixList.contains(FilenameUtils.getPrefix(ftpFile.getName())));
    }
}
