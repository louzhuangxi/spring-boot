package org.h819;

import org.h819.commons.file.MyExcelUtils;

import java.io.File;

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

        int n = 0;

        while (n < 1000) {

            String s = MyExcelUtils.convertColumnIndexToTitle(n);
            System.out.println(n + " : " + s + " - > " + MyExcelUtils.convertColumnTitleToIndex(s));
            n++;
            System.out.println();
        }

        System.out.println(MyExcelUtils.convertColumnIndexToTitle(36660));
        System.out.println(MyExcelUtils.convertColumnTitleToIndex("bbfa"));

        //   System.out.println(MyExcelUtils.TitleTable[0]);
    }

    private void testFileName() {
        File f = new File("d:\\01\\01.txt");
        System.out.println(f.getName());

    }




}
