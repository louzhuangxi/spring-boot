package org.h819.commons;

import org.apache.commons.collections4.ListUtils;
import org.h819.commons.collection.MergeSort;

import java.util.*;

import static org.h819.commons.MyMathUtils.averageNumberToPart;

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
        return new ArrayList<>(new HashSet<>(collection));//利用 set 过滤
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


    /**
     * 单个 List ，分割成几个 subList , 保持原来 List 中元素的排序
     * 每个 subList 的大小不同，分割成的份数不同
     *
     * @param list the list to return consecutive sublists of
     * @param size 每个 sub list 的大小 the desired size of each sublist (the last may be smaller)
     *             大小不同，最后分割成的份数不同
     * @param <T>
     * @return
     */
    @Deprecated  // 使用下面的  partition ，分组更合理
    private static <T> List<List<T>> toSubList(List<T> list, int size) {
        //Guava
        //return Lists.partition(list, size);
        //Apache Commons Collections
        return ListUtils.partition(list, size);
    }

    /**
     * 单个 List ，分割成几个 List , 保持原来 List 中元素的排序
     * -
     * 经过优化，避免 Apache Commons Collections ListUtils 中会出现最后一个 list 元素很少的情况。
     * 如果遇到此情况，则把最后一个 list 的元素平均分配到其他 list 中，是各个 list 中的元素个数更加均衡。
     *
     * @param list      the list to return consecutive sublists of
     * @param partCount 分成的份数，固定，每个 subList 中的元素个数不同(相除有余数时)
     * @param <T>
     * @return
     */
    public static <T> List<List<T>> partition(List<T> list, int partCount) {
        List<Integer[]> average = averageNumberToPart(list.size(), partCount);
        List<List<T>> f = new ArrayList<>(partCount); // subList 的个数固定
        for (Integer[] i : average) {
//            System.out.println(String.format("start= %d , end = %s", i[0], i[1]));
//            System.out.println(String.format("after : start= %d , end = %s", i[0] - 1, i[1] - 1));
            f.add(list.subList(i[0] - 1, i[1]));
        }
        return f;
    }

}
