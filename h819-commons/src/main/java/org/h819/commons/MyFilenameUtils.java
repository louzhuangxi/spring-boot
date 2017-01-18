package org.h819.commons;

import org.apache.commons.io.FilenameUtils;

/**
 * Description : TODO(FilenameUtils 补充)
 * User: h819
 * Date: 2016/3/30
 * Time: 16:43
 * To change this template use File | Settings | File Templates.
 */
public class MyFilenameUtils {


    /**
     * windows 系统中，\ , / , : , * , ? , " , < , > , | 九个字符不能出现在文件夹中，用指定的字符代替。
     *
     * @param filename    文件名称
     * @param replacement 替代的字符串
     * @return
     */
    public static String getWindowsLegalFileName(String filename,
                                                 String replacement) {

        return filename.
                replace("\\", replacement).
                replace("/", replacement).
                replace(":", replacement).
                replace("*", replacement).
                replace("?", replacement).
                replace("\"", replacement).
                replace(">", replacement).
                replace("|", replacement).
                replace("<", replacement).trim();


    }

    /**
     * 根据操作系统类型(windows,linux)，自动替换文件路径中的分隔符为操作系统类型，windows 为 \ , linux 为 /
     *
     * @param path
     * @return
     */
    public static String formateSeparatorsToSystem(String path) {
        return FilenameUtils.separatorsToSystem(path);
    }
}
