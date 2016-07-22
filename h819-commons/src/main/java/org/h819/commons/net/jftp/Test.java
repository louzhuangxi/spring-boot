package org.h819.commons.net.jftp;

import org.apache.commons.io.FilenameUtils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Description : TODO( jftp 使用方法演示)
 * User: h819
 * Date: 2015/6/2
 * Time: 16:39
 * To change this template use File | Settings | File Templates.
 */
public class Test {

    public static void main(String[] arg) {

        try {
            Path path = Files.createTempFile("temp", ".txt");

            System.out.println(path.toFile().lastModified());

            System.out.println(FilenameUtils.getExtension(path.toFile().getName()));

        } catch (IOException e) {
            e.printStackTrace();
        }

    }


}
