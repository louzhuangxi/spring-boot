package org.h819.commons.net.jftp;

import org.apache.commons.io.FilenameUtils;

/**
 * Description : TODO( jftp 使用方法演示)
 * User: h819
 * Date: 2015/6/2
 * Time: 16:39
 * To change this template use File | Settings | File Templates.
 */
public class Test {

    public static void main(String[] arg) {


        String path = "\\folder\\subfolder";
        String path1 = "\\\\folder\\subfolder";
        String path2 = "\\folder//subfolder";
        String path3 = "\\folder/subfolder.pdf";

        System.out.println(FilenameUtils.separatorsToSystem(path));
        System.out.println(FilenameUtils.separatorsToSystem(path1));
        System.out.println(FilenameUtils.separatorsToSystem(path2));
        System.out.println(FilenameUtils.separatorsToSystem(path3));

        System.out.println("===");
        System.out.println(FilenameUtils.getName(path3));
    }


}
