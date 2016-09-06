package org.h819.web.spring.jpa;

import org.h819.commons.MyBeanUtils;
import org.hibernate.Hibernate;
import org.hibernate.proxy.HibernateProxy;
import org.springframework.beans.BeanUtils;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.*;

//参考实现：
//http://thoughtfulsoftware.blogspot.sg/2013/05/using-variable-depth-copy-to-prevent.html
//https://thoughtfulsoftware.wordpress.com/2013/05/05/using-a-variable-depth-copy-to-prevent-hibernate-lazyinitializationexception/


/**
 * 在 hibernate 的事务中 (@Transactional)，POJO 代表一个底层的数据库表，POJO 的任何变化，都会自动同步到底层数据库。所以POJO对象不能随意的作为一个值对象在各个层次中传递，需要转换为 DTO 对象。(详见 example.service 描述)
 * 本类是 Hibernate POJO 转换为 DTO 工具，可以设置转换深度。转换后的 DTO 对象，已经脱离事务容器控制，仅是个值对象，可以传递到任意层，并可以对该 DTO 对象进行任意修改，而不会把修改结果同步到底层数据库。
 * 由于用到了 Hibernate 和 spring 技术，所以该类只能用在应用两者的环境中。
 * 普通的 Bean 也可以转换，不需要是 Hibernate 的实体。
 * POJO 必需有默认的无参构造方法，并且满足 Bean 的格式。
 * 转换操作，可以不在事务中 (@Transactional)。
 * -
 * 注意：
 *
 * @Transient 标注的属性，无法转换
 * ====================
 * 作用 ：
 * 一般的 POJO 转换为 DTO 工具，需要自己生成 DTO 类，之后用 Bean 拷贝工具，进行 POJO -> DTO 转换，形如 copy(beanPOJO,beanDTO) ，其做法是进行属性对应后进行拷贝。如 apache BeanUtils ,dozer (http://dozer.sourceforge.net/) 等
 * 这种方案存在两个问题：
 * 1. 需要手工生成 DTO 类，每个 POJO 对象均需要手工生成，操作麻烦；
 * 2. 对于有多重依赖关系的对象，就像一棵对象树一样，拷贝某个属性，会把和该属性对应的所有关联对象都进行加载(加载大量 lazy 设置的级联对象，会频繁查询数据库)，而不能设置拷贝深度或者忽略某些对象属性。
 * 尤其是对于有双向对应关系的对象，拷贝会进入死循环，互相加载。
 * 3. 对于目前流行的 Entity to jsonString 类型的应用，由于问题 2 的存在，转换之后的 json 字符串要不变得复杂无比(包含整个对象树，而不仅仅是需要的对象属性)，
 * 要不在转换过程中就进入了死循环，JackJson( jackJson 提供的 @JsonIdentityInfo 级联层次过多时，不起作用。),fastJson 等工具均是此问题。
 * <p>
 * ====================
 * 本方案针对上述问题，进行了优化：
 * 1. 根据 POJO 对象，自动生成 DTO 对象;
 * 2. 根据需要指定转换深度，而不必把所有的级联对象都加载进来，从而减少不必要的数据库查询；
 * 3. 根据需要，设置不需要加载的对象的属性，可以截断双向关联，避免用工具转换到 json 的时候，进入死循环。
 * 2,3 步骤 共同作用，只加载需要的属性，并且截断了双向关联，减少了不必要的数据库查询，也使得 DTO -> jsonString 变得简单清晰。
 * 4. 为了便于展示，有时候需要对对象临时添加属性，常规做法，一般需要在 Entity 上面添加 @Transient 标记，表示不实例化到数据库。
 * 而 本工具类提供了 POJO -> map DTO 方法，不用修改 Entity 和添加 @Transient 标记，办法为 map 后可以随意添加属性和对应的值，做法返回值。
 * <p>
 * ====================
 * 本类中，提到的基本数据类型包括：
 * Primitive Data Types (byte,short,int,long,float,double,boolean,char)
 * String or other CharSequence, Number, Date, URI, URL, Locale , Class, corresponding array.
 * ====================
 * level (转换深度):
 * - 0 级: 只转换 POJO 的基本类型(见上文)，当属性为一个对象(Entity) 或集合类型时，不转换，此时对象(Entity)属性设置为 null ，集合设置为空集合。
 * - 1 级: 除对对象本身进行 0 层次操作外，对对象属性和集合中的对象进行 0 级操作
 * - 2 级: 以此类推...
 * <p>
 * ====================
 * 1. 本类线程安全
 * <p>
 * ==============  原文注释 ===============
 * For variable depth copy, level 0 is just the primitives of the specified object.
 * Each additional level is a copy of the object's non-primitive fields, and so on.
 * <p>
 * If level is set to zero, non-primitive properties will be set to null and collections will be empty.
 * This copy performs cycle detection and sets any repeated copies to null.
 * <p>
 * If a property is of type byte[], the copy is a direct reference to the original array, no array copy is done.
 * <p>
 * This works for Hibernate objects because hibernate's persistent collections aren't copied,
 * their contents are copied into the default collection of the target class.
 * <p>
 * Copy is done according to bean properties, not necessarily through field reflection.
 * <p>
 * This class has no mutable state so is thread-safe.
 */

/**
 * @Transient 标注的属性，无法进行转换
 * 必要时，可以通过 fastJson 的属性过滤，先序列化成字符串，之后反序列化构造成需要的 vo
 * 用法：
 * <p/>
 * DTOUtils dtoUtils = new DTOUtils();
 * dtoUtils.addExcludes(InfoEntity.class, "refUserInfoEntities");    // 在整个转换过程中，无论在哪个级联层次，只要遇到 InfoEntity 类，那么他的 refUserInfoEntities 属性就不进行转换
 * dtoUtils.addExcludes(ProvinceEntity.class, "parent", "children"); // 多个属性
 * dtoUtils.addExcludes(WebSiteEntity.class, "webSiteColumns");
 * dtoUtils.addExcludes(WebSiteColumnEntity.class, "snatchUrls");
 * dtoUtils.createDTOcopy(list.getContent(), 3); // 拷贝到第三层
 * <p/>
 * //不能用静态方法，静态方法全局只有唯一一个实例，添加的过滤器会一直保存，随着添加会越来越多。
 */


public class DTOUtils {


    //准备实例化的对象类别，该类别可以位于级联层次的任意层次，只要属性类别为此类别，那么其属性就会被忽略。用于 hibernate 的关联关系中，可以减少级联层次，并且可以避免深度级联而陷于死循环。
    //对于 @Transient 标准的属性，是临时状态，无法过滤
    private Map<Class<?>, String[]> excludes = new HashMap();


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
        List<T> list = new ArrayList(entityBeanPOJOs.size());//按照原来的顺序
        for (T entity : entityBeanPOJOs)
            list.add((T) createDTOCopy(entity, depth));
        return list;
    }


    /**
     * @param entityBeanPOJO
     * @return
     */
    public Map<String, Object> createDTOMapCopy(Object entityBeanPOJO) {
        return MyBeanUtils.beanToMap(createDTOCopy(entityBeanPOJO, 0));
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
        return MyBeanUtils.beanToMap(createDTOCopy(entityBeanPOJO, depth));
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