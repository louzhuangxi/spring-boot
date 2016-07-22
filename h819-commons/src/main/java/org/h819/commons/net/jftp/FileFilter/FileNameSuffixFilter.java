package org.h819.commons.net.jftp.FileFilter;

import com.google.common.collect.Lists;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPFileFilter;

import java.util.Arrays;
import java.util.List;

/**
 * Description : TODO(文件扩展名包含)
 * User: h819
 * Date: 2015/7/3
 * Time: 17:26
 * To change this template use File | Settings | File Templates.
 */
public class FileNameSuffixFilter implements FTPFileFilter {

    //String prefix, String suffix

    private List suffixList = Lists.newArrayList();

    public FileNameSuffixFilter(String[] suffixs) {
        this.suffixList = Arrays.asList(suffixs);
    }

    @Override
    public boolean accept(FTPFile ftpFile) {

        if (suffixList.isEmpty())
            return ftpFile.isFile();

        return (ftpFile.isFile() && suffixList.contains(FilenameUtils.getExtension(ftpFile.getName())));
    }
}
