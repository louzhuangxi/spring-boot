package org.test;

import org.h819.commons.file.MyPDFUtils;

import java.io.File;
import java.io.IOException;

/**
 * Description : TODO()
 * User: h819
 * Date: 2016/4/1
 * Time: 15:20
 * To change this template use File | Settings | File Templates.
 */
public class Test2 {

    public static void main(String[] args) {

        Test2 t = new Test2();

        t.decryptDirectory();
    }

    private void decryptDirectory() {
        File src = new File("D:\\2016-8-3\\");
        File desc = new File("D:\\2016-8-4_desc\\");
        File bad = new File("D:\\2016-8-4_desc_bad\\");

        try {
            MyPDFUtils.decryptDirectory(src, desc, bad);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void testFileName() {
        File f = new File("d:\\01\\01.txt");
        System.out.println(f.getName());

    }


}
