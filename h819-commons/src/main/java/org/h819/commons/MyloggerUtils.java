package org.h819.commons;

/**
 * Description : TODO(打印 logger 所在的类名和方法名，便于定位语句)
 * User: h819
 * Date: 13-12-23
 * Time: 上午10:15
 * To change this template use File | Settings | File Templates.
 */
public class MyloggerUtils {

    /**
     * Java获取调用类名、方法名。
     *
     * 在调用本方法的中，打印调用类名和方法名
     * @return
     */
    public static String getCurrentMethodName(){

        return "class: "+new Exception().getStackTrace()[1].getClassName()+",  method: "+new Exception().getStackTrace()[1].getMethodName();
    }

}
