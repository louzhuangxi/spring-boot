package org.h819;

import java.io.File;

/**
 * Description : TODO()
 * User: h819
 * Date: 2016/4/1
 * Time: 15:20
 * To change this template use File | Settings | File Templates.
 */
public class Test2 {

    public static void main(String[] args){
           Test2 t = new Test2();
        t.testFileName();

    }

    private void testFileName(){
        File f = new File("d:\\01\\01.txt") ;
        System.out.println(f.getName());

    }
}
