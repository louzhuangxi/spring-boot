package org.h819.commons;

import org.h819.commons.collection.MergeSort;

import java.util.*;

/**
 * @author H819
 * @version V1.0
 * @Title: CollectionUtils.java
 * @Description: TODO(集合工具. 实际应用的时候，主要参考 commons.collections)
 * @date 2010-3-30
 */

//例子在 commons.collection.CollectionExamples 中
public class MyCollectionUtils {

    // https://github.com/k0fis/kfsMegeSort
    // Simple replace Java7 Collection.sort
    // jdk7 的 Collection.sort 排序算法发生了改变，在某些版本的jdk(如 openjdk) 下，Collection.sort  方法会发生
    // " IllegalArgumentException: Comparison method violates its general contract! " 异常
    // 用下面的方法代替 Collection.sort 即可
    // 用法 ： MyCollectionUtils.sortJdk6(list,comparator)  代替 Collection.sort(list,comparator)

    /**
     * @param a
     * @param comp
     * @param <E>
     */
    public static <E> void sortJdk6(E[] a, Comparator<? super E> comp) {
        MergeSort.mergeSort(a, 0, a.length - 1, comp);
    }

    public static <T> void sortJdk6(List<T> list, Comparator<? super T> c) {
        Object[] a = list.toArray();
        sortJdk6(a, (Comparator) c);
        ListIterator i = list.listIterator();
        for (Object a1 : a) {
            i.next();
            i.set(a1);
        }
    }

    /**
     * 去除集合中重复的元素
     *
     * @param collection
     * @param <T>
     * @return
     */
    public static <T> Collection removeDuplicate(Collection<T> collection) {

        // return collection.parallelStream().distinct().collect(Collectors.toList());  jdk8
        return new ArrayList<>(new HashSet<T>(collection));//利用 set 过滤
    }

    /**
     * 移除集合中的 null 对象
     *
     * @param collection
     * @param <T>
     * @return
     */
    public static <T> boolean removeNull(Collection<T> collection) {
        return collection.removeAll(Collections.singleton(null));
      // jdk 8
       // collection.parallelStream().filter(i -> i != null).collect(Collectors.toList());
      //  list.stream().filter(i -> i != null).collect(Collectors.toList());
       // listWithoutNulls.removeIf(p -> p == null);
    }


//    public static void main(String[] arg) {
//
//        MyCollectionUtils c = new MyCollectionUtils();
//        //  u.printTreeSetExample();
//        //  u.printLinkedHashSetExample();
//
//        // u.testFindReduplicate();
//
//    }



}
