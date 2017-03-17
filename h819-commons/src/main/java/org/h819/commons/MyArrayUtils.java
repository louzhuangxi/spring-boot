package org.h819.commons;

import com.google.common.collect.Lists;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

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
    public static <T> T[] joinArrays(final T[] firstArgs, final T[] secondArgs) {
        T[] result = Arrays.copyOf(firstArgs, firstArgs.length + secondArgs.length);
        System.arraycopy(secondArgs, 0, result, firstArgs.length, secondArgs.length);
        return result;
    }

    /**
     * Arrays.asList 生成的 list 大小不可变，重新包装。
     *
     * @param ts
     * @param <T>
     * @return
     */
    public static <T> List<T> asArrayList(T... ts) {
        return new ArrayList<>(Arrays.asList(ts));
    }

    /**
     * Collection to Array  example
     * 和具体类型相关，无法写成通用方法
     */
    private void collectionToArrayExample() {

        //String 类型
        Collection<String> collection = Lists.newArrayList("sting1", "sting1", "sting1");
        String[] foos = collection.toArray(new String[collection.size()]);
    }


}