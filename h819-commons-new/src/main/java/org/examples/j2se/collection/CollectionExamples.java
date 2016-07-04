package org.examples.j2se.collection;

import org.apache.commons.collections4.SetUtils;
import org.apache.commons.collections4.map.MultiValueMap;
import org.apache.commons.lang3.StringUtils;
import org.h819.commons.MyLangUtils;
import org.h819.commons.beanutils.bean.NameBean;

import java.util.*;

import static java.lang.System.out;

/**
 * @author H819
 * @version V1.0
 * @Title: CollectionUtils.java
 * @Description: TODO(集合工具. 实际应用的时候，主要参考 commons.collections)
 * @date 2010-3-30
 */
public class CollectionExamples {

    //"06.03.02.04", " 06.03.02.03", " 06.03.02.02", 有两份
    String[] exampleArrays = new String[]{"06.03.02.04", "06.03.02.03", "06.03.02.02",
            "06.03.02.04", "06.03.02.03", "06.03.02.02",
            "06.03.02.01", "06.08", "06.03.02", "06.04.02", "06.02",
            "06.04.02.02", "06.03", "06.02.04", "06.04", "06.0", "06.02.02"};


    private CollectionExamples() {

    }

    public static void main(String[] arg) {

        CollectionExamples c = new CollectionExamples();
        //  u.printTreeSetExample();
        //  u.printLinkedHashSetExample();

        c.testFindReduplicate();

    }


    /**
     *
     * 下面几个方法，演示了JDK Set 的应用，熟悉后，不用这几个方法，直接使用。
     */

    /**
     * Set，过滤重复元素
     * <p/>
     * 自然排序 Set ， (使用元素 .compareTo() 方法比较)
     * <p/>
     * TreeSet 默认排序
     *
     * @return
     */
    public Set getNaturalOrderSet() {

        return new TreeSet();

    }

    /**
     * Set，过滤重复元素
     * 按照添加的顺序
     * Returns a set that maintains the order of elements that are added backed by the given set.
     * If an element is added twice, the order is determined by the first add. The order is observed through the iterator or toArray.
     * <p/>
     *
     * @param set
     * @return
     */
    public Set getOrderSet(Set set) {
        // 创建一个按照 参数 set 元素放入顺序进行排序的空 set ，返回后需要按照需求添加元素
        return SetUtils.orderedSet(set);
    }

    /**
     * List ，不过滤重复元素
     * 按照元素放入顺序排序
     *
     * @return
     */
    public List getOrderList() {

        return new ArrayList();

    }

    /**
     * List ，不过滤重复元素
     * 自然排序
     * Collections.sort 只能对 List 排序，不能对 Set
     *
     * @return
     */
    public List getNaturalOrderList(List list) {
        Collections.sort(list);
        return list;

    }

    /**
     * List ，不过滤重复元素
     * 按照自定义比较器排序
     *
     * @return
     */
    public List getCustomOrderList(List list) {

        //静态方法，直接修改了参数
        Collections.sort(list, new Comparator<String>() {
            @Override
            public int compare(String fruite1, String fruite2) {
                return fruite1.compareTo(fruite2);
            }

            // equals 方法可以不实现
//                    @Override
//                    public boolean equals(Object o) {
//                        return super.equals(o);
//                    }
        });

        return list;
    }


    /**
     * @param sourceArray
     * @param <T>
     * @return
     */
    public <T> List<T> array2ListExample(T[] sourceArray) {
        return Arrays.asList(sourceArray);
    }

    /**
     * List to Array
     *
     * @param sourceList
     * @return
     */
    public Object[] list2ArrayExample(List<Object> sourceList) {

        // return sourceList.toArray(new String[sourceList.size()]);  指定转换类型
        return sourceList.toArray();
    }

    /**
     * @param sourceArray
     * @param <T>
     * @return
     */
    public <T> Set<T> arrayToSettExample(T[] sourceArray) {
        return new HashSet<T>(Arrays.asList(sourceArray));
    }

    /**
     * @param sourceSet
     * @return
     */
    public Object[] setToArrayExample(Set<Object> sourceSet) {
        return sourceSet.toArray(new Integer[sourceSet.size()]);
    }

    /**
     * @param sourceSet
     * @param <T>
     * @return
     */
    public <T> List<T> setToListExample(Set<T> sourceSet) {
        return new ArrayList<T>(sourceSet.size());
    }

    /**
     * @param sourceList
     * @param <T>
     * @return
     */
    public <T> Set<T> listToSettExample(List<T> sourceList) {
        return new HashSet<T>(sourceList.size());
    }


    /**
     * Collection to array
     *
     * @param collection
     * @return
     */


    public Object[] collectionToArrayExample(Collection<Object> collection) {
        return collection.toArray(new Object[collection.size()]);
    }

    /**
     * 此函数演示了，如何在一个集合中查找重复元素（个别属性相同），并作相应处理(以 +1 为示例)。
     *
     * @param list 待检查的集合
     */
    private void findReduplicateExample(ArrayList<NameBean> list) {

        // 存放不重复的、最后的结果
        ArrayList<NameBean> finalList = new ArrayList<NameBean>(list.size());

        //准备一份完全独立的数据，而不是有引用，只能用深度克隆，用于测试
        List<NameBean> listCloneTest = new LinkedList<NameBean>();
        for (NameBean bean : list) {
            //listCloneTest.add(bean); // 这是对象的引用，而不是完全独立的另外一份。
            listCloneTest.add(MyLangUtils.deepClone((bean)));
        }

        int size = list.size();

        // 集合自身相互比较，比较完成的元素设置为 null，可以减少比较次数。注意循环条件，要防止访问 null
        for (int i = 0; i < size; i++) {// 外循环
            boolean isReduplicate = false;
            NameBean nameBean_i = list.get(i);
            //跳过已经检查过的元素
            if (nameBean_i == null)
                continue;

            for (int j = i + 1; j < size; j++) {// 内循环
                NameBean nameBean_j = list.get(j);
                //跳过已经检查过的元素
                if (nameBean_j == null)
                    continue;

                if (nameBean_i.getName().equals(nameBean_j.getName())) { // 条件和处理
                    //  nameBean_i.setAge(nameBean_i.getAge() + 1);
                    nameBean_j.setAge(nameBean_j.getAge() + 1);
                    //重复，修改后添加
                    // finalList.add(nameBean_i);
                    finalList.add(nameBean_j);
                    list.set(j, null);  //内循环中，比较完成的元素设为 null
                    isReduplicate = true;
                }
            }// 内循环结束

            if (isReduplicate) { //重复，演示处理找到的重复元素修改后添加
                nameBean_i.setAge(nameBean_i.getAge() + 1);
                finalList.add(nameBean_i);
            } else//不重复，直接添加
                finalList.add(nameBean_i);

            list.set(i, null);  //外循环中，比较完成的元素设为 null

        }// 外循环结束


        //测试
        // finalList 的顺序和原始不同，因为两层循环的缘故，此处演示原来的顺序
        for (NameBean bean : listCloneTest) {
            System.out.println(bean.getName() + " , " + bean.getAge());
        }
        System.out.println(StringUtils.center(" splite ", 80, "*"));
        // 此处演示去重后，经过处理的元素排序
        for (NameBean bean : finalList) {
            System.out.println(bean.getName() + " , " + bean.getAge());
        }

    }

    private void testFindReduplicate() {

        ArrayList<NameBean> bean = new ArrayList<NameBean>();
        bean.add(new NameBean("jiang", 1));
        bean.add(new NameBean("jiang", 3));
        bean.add(new NameBean("jiang", 5));
        bean.add(new NameBean("jiang", 7));
        bean.add(new NameBean("hui", 0));

        findReduplicateExample(bean);


    }

    /**
     * Set，过滤重复元素
     * <p/>
     * 自定义比较器
     *
     * @return
     */
    public Set getCustomOrderSet() {
        return new TreeSet(new Comparator<String>() {
            @Override
            public int compare(String str1, String str2) {
                return str1.compareTo(str2);
            }
        });
    }

    private void printTreeSetExample() {
        System.out.println(new TreeSet(Arrays.asList(exampleArrays)));
    }

    private void printLinkedHashSetExample() {
        System.out.println(new LinkedHashSet(Arrays.asList(exampleArrays)));
    }


    /**
     * commons collectios 的实例代码
     */
    private void mapExamples() {

        // 多值 map,一个 key 对于多个值
        MultiValueMap mvm = new MultiValueMap();
        mvm.put("1", "one");
        mvm.put("2", "two");
        mvm.put("1", "three");

        for (Iterator it = mvm.entrySet().iterator(); it.hasNext(); ) {
            Map.Entry entry = (Map.Entry) it.next();
            Object key = entry.getKey();
            Object value = entry.getValue();
            out.println("key=" + key + "; value=" + value);
        }

        // 判断是否包含指定的 key value
        out.println(mvm.containsValue("1", "one"));


        // MultiValueMap 的 value 值是 Collection 类型。实际上是  arrayList
        //测试类型
        out.println(mvm.get("1").getClass());
        ArrayList<String> list = (ArrayList<String>) mvm.getCollection("1");

        for (String s : list)
            out.println(s);

    }


    /**
     * 遍历集合的代码
     */
    private void printCollections() {
        // For a set or list
        ArrayList list = new ArrayList();
        for (Iterator it = list.iterator(); it.hasNext(); ) {
            Object element = it.next();
        }

        Map map = new HashMap();
        // For keys of a map
        for (Iterator it = map.keySet().iterator(); it.hasNext(); ) {
            Object key = it.next();
        }

        // For values of a map
        for (Iterator it = map.values().iterator(); it.hasNext(); ) {
            Object value = it.next();
        }

        // For both the keys and values of a map
        for (Iterator it = map.entrySet().iterator(); it.hasNext(); ) {
            Map.Entry entry = (Map.Entry) it.next();
            Object key = entry.getKey();
            Object value = entry.getValue();
        }
    }

}
