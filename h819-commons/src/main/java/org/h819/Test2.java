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

        int i =2999;

      //  Assert.isTrue(NumberUtils.isNumber(String.valueOf(i))&&i>=1,"i 不是整数");

//        int records =49;
//        if (records < 0) {
//            total = -1;
//            return;
//        }
//
//        long totalTemp = records / pageSize;
//        if (records % pageSize > 0) {
//            totalTemp++;
//        }
//
//        total = (int) totalTemp;

        int total =51;
        int page =6;
        int size =0;

        if(total%page >0)
            size = total/page +1;
        else if(total%page ==0)
            size = total/page;

       System.out.println(size);


    }

    private void testFileName(){
        File f = new File("d:\\01\\01.txt") ;
        System.out.println(f.getName());

    }
}
