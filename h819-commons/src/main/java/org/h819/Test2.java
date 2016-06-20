package org.h819;

import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.util.Assert;

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

        Assert.isTrue(NumberUtils.isNumber(String.valueOf(i))&&i>=1,"i 不是整数");




    }

    private void testFileName(){
        File f = new File("d:\\01\\01.txt") ;
        System.out.println(f.getName());

    }
}
