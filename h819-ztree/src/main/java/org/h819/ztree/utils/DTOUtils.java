package org.h819.ztree.utils;

import org.hibernate.Hibernate;
import org.hibernate.proxy.HibernateProxy;
import org.springframework.beans.BeanUtils;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

/**
 * see http://git.oschina.net/h819/spring-boot
 */
public class DTOUtils {


    //准备实例化的对象类别，该类别可以位于级联层次的任意层次，只要属性类别为此类别，那么其属性就会被忽略。用于 hibernate 的关联关系中，可以减少级联层次，并且可以避免深度级联而陷于死循环。
    private Map<Class<?>, String[]> excludes = new HashMap<Class<?>, String[]>();


    public DTOUtils() {
    }


    /**
     * 默认只生成对象的基本属性
     *
     * @param entityBeanPOJO
     * @param <T>
     * @return
     */
    public <T> T createDTOcopy(T entityBeanPOJO) {
        return createDTOcopy(entityBeanPOJO, 0);
    }


    /**
     * 由 POJO 生成为 DTO 工具，如果不清楚深度在那一层，可以从小到大逐个尝试，以满足结果和最少查询数据库为最佳。
     *
     * @param entityBeanPOJO 接受容器管理的 POJO，可以是一个 POJO 对象，也可以是 POJO 对象集合
     * @param depth          拷贝深度
     * @param <T>
     * @return 不受容器管理的 DTO 对象
     */
    public <T> T createDTOcopy(T entityBeanPOJO, int depth) {

        if (entityBeanPOJO == null)
            return null;

        if (Collection.class.isAssignableFrom(entityBeanPOJO.getClass()))  // 集合
            return (T) createDTOCopyList((Collection) entityBeanPOJO, depth);
        else
            return (T) createDTOCopy(entityBeanPOJO, depth);   // 单个对象
    }

    /**
     * POJO 对象集合
     *
     * @param entityBeanPOJOs
     * @param depth
     * @param <T>
     * @return
     */
    private <T> Collection<T> createDTOCopyList(Collection<T> entityBeanPOJOs, int depth) {
        List<T> list = new ArrayList<T>(entityBeanPOJOs.size());//按照原来的顺序
        for (T entity : entityBeanPOJOs)
            list.add((T) createDTOCopy(entity, depth));
        return list;
    }


    /**
     * @param entityBeanPOJO
     * @return
     */
    public Map<String, Object> createDTOMapCopy(Object entityBeanPOJO) {
        return toMap(createDTOCopy(entityBeanPOJO, 0));
    }

    /**
     * 用于 由 POJO 转换为 Map
     * bean to map ，主要用来给 DTO 增加属性，便于向页面端传送额外的变量，这样就不必到 Entity 中增加 @Transient 类型的变量了
     * 增加的属性变量，在具体实现代码中可以灵活实现，不包装成工具类。
     * 如果想对集合中的所有对象增加属性，遍历集合，逐个添加即可。
     *
     * @param entityBeanPOJO 接受容器管理的 POJO
     * @param entityBeanPOJO
     * @return
     */
    public Map<String, Object> createDTOMapCopy(Object entityBeanPOJO, int depth) {
        return toMap(createDTOCopy(entityBeanPOJO, depth));
    }

    private Object createDTOCopy(Object entityBeanPOJO, int depth) {
        return createDTOCopy(entityBeanPOJO, depth, new LinkedList());
    }

    /**
     * 核心方法
     * 根据 POJO ，创建对象的拷贝 DTO，脱离事务环境使用
     *
     * @param entityBeanPOJO
     * @param depth
     * @param ancestors
     * @return
     */
    private Object createDTOCopy(Object entityBeanPOJO, int depth, List ancestors) {

        if (entityBeanPOJO == null) {
            // throw new NullPointerException("Entity passed for initialization is null");
            return null;
        }

        //修改了原作者的代码，如果此处不强制加载 lazy 的对象，在有的情况下会报异常。
        if (!Hibernate.isInitialized(entityBeanPOJO))
            Hibernate.initialize(entityBeanPOJO); //Force initialization of a proxy or persistent collection.
        if (entityBeanPOJO instanceof HibernateProxy) {
            entityBeanPOJO = ((HibernateProxy) entityBeanPOJO).getHibernateLazyInitializer().getImplementation();
        }
        // 修改原作者代码结束

        if (ancestors.contains(entityBeanPOJO)) {
            return null;
        }

        Object entityBeanVO = null;

        try {
            //Convenience method to instantiate a class using its no-arg constructor.
            entityBeanVO = BeanUtils.instantiate(entityBeanPOJO.getClass());
            List<String> nonSimplePropertyNames = getNonSimplePropertyNames(entityBeanPOJO.getClass());
            // copy entityBeanPOJO to entityBeanVO , ignore nonSimplePropertyNames properties , 不拷贝非简单属性
            BeanUtils.copyProperties(entityBeanPOJO, entityBeanVO, nonSimplePropertyNames.toArray(new String[]{}));

            if (depth > 0) {

                // 获取当前需要从数据库加载的对象
                Class<?> clazz = entityBeanPOJO.getClass();
                // System.out.println("descriptors : "+descriptors);
                // System.out.println("=================== 当前需要从数据库加载的对象是 class : " + clazz.getName());

                //本身放入
                ancestors.add(entityBeanPOJO);
                //entityBeanPOJOs 的所有属性
                PropertyDescriptor[] descriptors = BeanUtils.getPropertyDescriptors(entityBeanPOJO.getClass());


                continueTag:
                for (PropertyDescriptor pd : descriptors) {//循环当前对象的所有属性，进行加载
                    // System.out.println(pd.getName());

                    /**
                     * 逐一比当前对象的每个属性，找到不需要的序列化的对象的属性，跳过。
                     * 如果当前对象的的属性，在忽略列表里面，则跳过
                     */
                    for (Map.Entry<Class<?>, String[]> item : excludes.entrySet()) {
                        // isAssignableFrom()，用来判断类型间是否有继承关系
                        if (item.getKey().isAssignableFrom(clazz)) { //key : 类
                            List<String> list = Arrays.asList(item.getValue()); //value : 类中的属性
                            if (list.contains(pd.getName()))//类中属性
                            {
                                // System.out.println("跳过当前类的指定属性 ："+clazz.getName()+" : "+pd.getName());
                                continue continueTag;            // 跳过包含的属性 . 此处用到了 goto 类型的语句，真好使啊!
                            }
                        }
                    }


                    /**
                     * 集合则进行递归
                     */
                    //  System.out.println("type : "+pd.getPropertyType());
                    // entityBeanVO into collections (target should always have a default empty collection assigned)
                    if (Collection.class.isAssignableFrom(pd.getPropertyType())) { // 判断属性名称，是否为 Collection 类型
                        Collection sourceCollection = (Collection) pd.getReadMethod().invoke(entityBeanPOJO);
                        Collection targetCollection = (Collection) pd.getReadMethod().invoke(entityBeanVO);
                        targetCollection.clear();

                        for (Object j : sourceCollection) { //对集合的每一个对象，执行复制上一层操作，拷贝动作在下文
                            targetCollection.add(createDTOCopy(j, depth - 1, ancestors));
                        }

                        continue;
                    }

                    /**
                     * 拷贝非简单属性
                     */

                    //     System.out.println("pd name : "+pd.getName());
                    if (nonSimplePropertyNames.contains(pd.getName())) {
                        Object propertyToCopy = pd.getReadMethod().invoke(entityBeanPOJO);
                        Object propertyCopy = createDTOCopy(propertyToCopy, depth - 1, ancestors);
                        //       System.out.println(" propertyToCopy:  " + propertyToCopy.toString());
                        //      System.out.println(" propertyCopy:  " + propertyCopy.toString());
                        pd.getWriteMethod().invoke(entityBeanVO, propertyCopy);
                        continue;
                    }
                }

                ancestors.remove(ancestors.size() - 1);
            }

        } catch (IllegalAccessException ex) {
            throw new UnsupportedOperationException(entityBeanPOJO.getClass() + " cannot be handled", ex);
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }

        return entityBeanVO;
    }

    /**
     * 找到 bean 的非简单属性名称(isSimpleProperty 进行判断)，包括 :
     * primitive (byte,short,int,long,float,double,boolean,char) Primitive Data Types 基本数据类型，共 8 种
     * String or other CharSequence
     * Number
     * Date
     * URI
     * URL
     * Locale
     * Class
     * corresponding array.
     *
     * @param fromClass
     * @return
     */
    private List<String> getNonSimplePropertyNames(Class fromClass) {
        List<String> names = new ArrayList<String>();
        for (Field field : fromClass.getDeclaredFields()) {
            if (!BeanUtils.isSimpleProperty(field.getType())) {
                names.add(field.getName());
            }
        }
        return names;
    }

    private Map<String, Object> toMap(Object bean) {

        if (bean == null) {
            return null;
        }

        Map<String, Object> map = new HashMap<String, Object>();
        try {
            BeanInfo beanInfo = Introspector.getBeanInfo(bean.getClass());
            PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
            for (PropertyDescriptor property : propertyDescriptors) {
                String key = property.getName();
                // 过滤class属性，否则输出结果会有 class 属性
                if (!key.equals("class")) {
                    // 得到property对应的getter方法
                    Method getter = property.getReadMethod();
                    Object value = getter.invoke(bean);
                    map.put(key, value);
                }
            }
        } catch (Exception e) {
            System.out.println("transBean2Map Error " + e);
        }
        return map;
    }

    /**
     * 设置不进行实例化的属性。
     *
     * @param clasz    准备实例化的对象类别，该类别可以位于级联层次的任意层次，只要属性类别为此类别，那么其属性就会被忽略。用于 hibernate 的关联关系中，可以减少级联层次，并且可以避免深度级联而陷于死循环。
     * @param excludes 不进行序列化的属性
     */
    public void addExcludes(Class clasz, String... excludes) {
        this.excludes.put(clasz, excludes);
    }

}