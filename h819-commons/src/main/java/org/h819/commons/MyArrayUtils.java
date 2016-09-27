package org.h819.commons;

import java.util.Arrays;

/**
 * Description : TODO()
 * User: h819
 * Date: 16-9-8
 * Time: 下午4:06
 * To change this template use File | Settings | File Templates.
 */
public class MyArrayUtils {


    /**
     * 合并两个 array，保持原先后顺序
     *
     * @param firstArgs  第一个 array
     * @param secondArgs 第二个 array
     * @param <T>
     * @return
     */
    public static <T> T[] concatArrays(final T[] firstArgs, final T[] secondArgs) {
        T[] result = Arrays.copyOf(firstArgs, firstArgs.length + secondArgs.length);
        System.arraycopy(secondArgs, 0, result, firstArgs.length, secondArgs.length);
        return result;
    }

}
