package org.h819.web.spring.jpa;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.domain.Specifications;

import javax.persistence.criteria.*;
import java.util.Arrays;
import java.util.Collection;


/**
 * Description : TODO(创建动态查询语句 Specification ，有了 Specification 之后，可以利用 JPA 进行查询。Specification 就是各种查询条件集合)
 * Description : TODO(Specification ，实现其接口 toPredicate 方法即可 )
 * User: h819
 * Date: 2016-9-28
 * Time: 下午13:20
 * To change this template use File | Settings | File Templates.
 */

/**
 * jpa 与 querydsl 的区别
 * -
 * 二者都是数据库查询等操作的具体实现
 * jpa 用 jpql 语句
 * querydsl 用自己的实现方式
 * -
 * querydsl 最大的优势是能够动态拼出查询语句，因为和 QEntity 相关，所以无法总结出通用的方法来构建动态查询语句
 * jpql 通过 Specification 也可以拼出动态查询语句 ，已经有总结出通用的类 （本类）
 * -
 * spring data jpa 和 querydsl 结合，主要是定义了 QueryDslPredicateExecutor 接口，可以方便的和 spring data jpa Repository 结合
 * 但 QueryDslPredicateExecutor 接口，只实现了几个方法，如果想要复杂查询，还是要使用 querydsl 自己的实现方式
 * -
 * 总结：
 * spring data jpa 的 jpql 语言能够灵活拼出各种查询语句（用占位符的方式，不会有 sql 注入问题），并且有了工具类来构建动态查询
 * 所以就用 jpa 吧，不在尝试 querydsl
 * -
 * 另外一种查询方式 QBE ：
 * org.springframework.data.domain.Example
 * Support for query by example (QBE).
 * 功能好像和 querydsl 相同，还不如 querydsl 功能完善，不用。
 * -
 * -
 * -
 * <p>
 * 使用方式:
 * TreeEntity
 * private Long id;
 * private String name;
 * private TreeEntity parent
 * Specification specification = new JpaDynamicSpecificationBuilder()
 * .and(new SearchFilter("parent.name", SearchFilter.Operator.EQ, "国外")) // 级联属性，可以比较对象中属性为对象的属性
 * .and(new SearchFilter("id", SearchFilter.Operator.BETWEEN, null,20)).build();
 * -
 */
public class JpaDynamicSpecificationBuilder {

    //  private static Logger logger = LoggerFactory.getLogger(JpaDynamicSpecificationBuilder.class);
    private Specification specification;

    public JpaDynamicSpecificationBuilder() {
        this.specification = Specifications.where(null);
    }

    /**
     * 逐层深入构造 path
     * <p/>
     * 当 jpa 对象有级联关系时，被级联的对象的某个属性作为查询条件构建 Predicate ，如
     * 查询属性为 tree.parent.name (tree 对象中的 parent 对象 的 name 属性)
     * 构建的 Predicate 中的 Path 不是
     * return build.equal(root.get("tree.parent.name"), "value");
     * 而应该是
     * return cb.equal((Path<String>) ((Path<Area>) root.get("tree")).get("parent").get("name"),"value");
     * root.get("tree").get("parent").get("name") 的返回值，即被查询的属性 name 代表的对象类型，可以是对象，也可以是字符串
     *
     * @param root
     * @param filterFieldName 属性名称 , 可以是级联格式
     * @param <T>
     * @return
     * @see "http://stackoverflow.com/questions/13246959/jpa-specification-with-onetoone-relation"
     */
    private <T> Path getNestedPath(Root<T> root, String filterFieldName) {

        String[] names = filterFieldName.split("\\."); // filterFieldName = tree.parent.name
        Path nestedPath = root;
        //递归构造 Path
        // 找到最终属性的 tree.parent.name 所代表的 path ，相当于 root.get("tree").get("parent").get("name");
        for (String subPath : names)
            nestedPath = nestedPath.get(subPath);

        return nestedPath;
    }

    /**
     * 取得属性代表的对象的类型
     *
     * @param root
     * @param filterFieldName 属性名称 , 可以是级联格式
     * @param <T>
     * @return
     */
    private <T> Class getPathJavaType(Root<T> root, String filterFieldName) {
        return getNestedPath(root, filterFieldName).getJavaType();
    }

    /**
     * 条件 and
     * <p>
     * 为了避免方法名称重复，分别用了 Iterable 和 Collection
     *
     * @param searchFilter
     * @return
     */
    public JpaDynamicSpecificationBuilder and(final SearchFilter searchFilter) {
        this.specification = Specifications.where(this.specification).and(bySearchFilter(searchFilter));
        return this;
    }

    public JpaDynamicSpecificationBuilder and(final SearchFilter... searchFilters) {
        return and(Arrays.asList(searchFilters));
    }

    public JpaDynamicSpecificationBuilder and(final Iterable<SearchFilter> searchFilters) {
        for (SearchFilter filter : searchFilters)
            this.specification = Specifications.where(this.specification).and(bySearchFilter(filter));
        return this;
    }

    public JpaDynamicSpecificationBuilder and(final Specification customSpecification) {
        this.specification = Specifications.where(this.specification).and(customSpecification);
        return this;
    }

    public JpaDynamicSpecificationBuilder and(final Specification... customSpecifications) {
        return and(Arrays.asList(customSpecifications));

    }

    public JpaDynamicSpecificationBuilder and(final Collection<Specification> customSpecifications) {
        for (Specification specification : customSpecifications)
            this.specification = Specifications.where(this.specification).and(specification);
        return this;
    }

    /**
     * 条件 or
     *
     * @param searchFilter
     * @return
     */
    public JpaDynamicSpecificationBuilder or(final SearchFilter searchFilter) {
        this.specification = Specifications.where(this.specification).or(bySearchFilter(searchFilter));
        return this;
    }

    public JpaDynamicSpecificationBuilder or(final SearchFilter... searchFilters) {
        return or(Arrays.asList(searchFilters));
    }

    public JpaDynamicSpecificationBuilder or(final Iterable<SearchFilter> searchFilters) {
        for (SearchFilter filter : searchFilters)
            this.specification = Specifications.where(this.specification).or(bySearchFilter(filter));
        return this;
    }

    public JpaDynamicSpecificationBuilder or(final Specification customSpecification) {
        this.specification = Specifications.where(this.specification).or(customSpecification);
        return this;
    }

    public JpaDynamicSpecificationBuilder or(final Specification... customSpecifications) {
        return or(Arrays.asList(customSpecifications));
    }

    public JpaDynamicSpecificationBuilder or(final Collection<Specification> customSpecifications) {
        for (Specification specification : customSpecifications)
            this.specification = Specifications.where(this.specification).or(specification);
        return this;
    }

    /**
     * 条件 not
     *
     * @return
     */
    public JpaDynamicSpecificationBuilder not() {
        this.specification = Specifications.not(this.specification);
        return this;
    }

    public <T> Specification<T> build() {
        return this.specification;
    }

    /**
     * @param searchFilter 查询条件
     *                     根据于查询对象的属性，构造 SearchFilter
     *                     <p/>
     *                     假设查询条件 level 是一个对象的字符串属性，构造为 SearchFilter searchFilter = new SearchFilter("level", SearchFilter.Operator.EQ, "1");
     *                     假设查询条件 user 是一个对象，构造为 searchFilters.add(new SearchFilter("user", SearchFilter.Operator.EQ, userEntity)); // userEntity 为一个对象。
     *                     级联查询时
     *                     new SearchFilter("parent.name", SearchFilter.Operator.EQ, "pname") ，被查询对象的 parent 属性是一个对象，该对象的 name 属性等于 pname
     * @return
     */
    private <T> Specification bySearchFilter(final SearchFilter searchFilter) {

        return new Specification<T>() {
            /**
             *
             * @param root   查询中的条件表达式
             * @param query  条件查询设计器
             * @param builder 条件查询构造器
             * @return
             */
            @Override
            public Predicate toPredicate(final Root<T> root, final CriteriaQuery<?> query, final CriteriaBuilder builder) {

                //  Class<?> javaType = root.get(searchFilter.getFieldName()).getJavaType();   // 只对找到第一层，不会递归找到 tree.parent.name 对象中的对象的属性
                Path path = getNestedPath(root, searchFilter.getFieldName()); // 可以递归查找
                // 被查询的属性代表的对象类型，可以是对象，也可以是字符串。
                // 不同的比较方法，要求的对象类型不同
                // 此处为自动获取
                Class<?> javaType = path.getJavaType();

                switch (searchFilter.getOperator()) {

                    case EQ:
                        return builder.equal(path, searchFilter.getValue());

                    case NE:
                        return builder.notEqual(path, searchFilter.getValue());

                    case LIKE: // like,notlike 操作要求：属性代表的对象， String 类型，才可以比较 。(只有字符串才可以 like)
                    {
                        if (javaType == String.class)
                            return builder.like(path, "%" + searchFilter.getValue() + "%");
                        else
                            throw new IllegalArgumentException("like 操作，属性代表的对象只能为 String.class 类型 !");
                    }

                    case NLIKE: {
                        if (javaType == String.class)
                            return builder.notLike(path, "%" + searchFilter.getValue() + "%");
                        else
                            throw new IllegalArgumentException("not like 操作，属性代表的对象只能为 String.class 类型 !");
                    }

                    case STARTS_WITH: {
                        if (javaType == String.class)
                            return builder.like(path, searchFilter.getValue() + "%");
                        else
                            throw new IllegalArgumentException("STARTS_WITH 操作 ,是 like 操作，属性代表的对象只能为 String.class 类型 !");
                    }


                    case ENDS_WITH: {
                        if (javaType.equals(String.class))
                            return builder.like(path, "%" + searchFilter.getValue());
                        else
                            throw new IllegalArgumentException("ENDS_WITH 操作 ,是 like 操作，属性代表的对象只能为 String.class 类型 !");
                    }
                    case GT: //great,less 操作要求：属性代表的对象，应该可以进行比较，即实现了 Comparable 才可以，这符合 java 对象比较的原则
                    {
                        if (Comparable.class.isAssignableFrom(javaType))
                            return builder.greaterThan((Expression<? extends Comparable>) path, (Comparable) searchFilter.getValue());
                        else
                            throw new IllegalArgumentException("不能比较! greaterThan 操作 ,被比较的对象，应该实现了 Comparable 接口，对象之间能相互比较 !");
                    }
                    case GTE: {
                        if (Comparable.class.isAssignableFrom(javaType))
                            return builder.greaterThanOrEqualTo((Expression<? extends Comparable>) path, (Comparable) searchFilter.getValue());
                        else
                            throw new IllegalArgumentException("不能比较! greaterThanOrEqualTo 操作 ,被比较的对象，应该实现了 Comparable 接口，对象之间能相互比较 !");
                    }

                    case LT: {
                        if (Comparable.class.isAssignableFrom(javaType))
                            return builder.lessThan((Expression<? extends Comparable>) path, (Comparable) searchFilter.getValue());
                        else
                            throw new IllegalArgumentException("不能比较! lessThan 操作 ,被比较的对象，应该实现了 Comparable 接口，对象之间能相互比较 !");
                    }
                    case LTE: {
                        if (Comparable.class.isAssignableFrom(javaType))
                            return builder.lessThanOrEqualTo((Expression<? extends Comparable>) path, (Comparable) searchFilter.getValue());
                        else
                            throw new IllegalArgumentException("不能比较! lessThanOrEqualTo 操作 ,被比较的对象，应该实现了 Comparable 接口，对象之间能相互比较 !");
                    }
                    case NN:  // 此时只需要 searchFilter.getFieldName() ，searchFilter.getValue() 用不到
                        return builder.isNotNull(path);
                    case IN://IN , NIN  操作要求：属性代表的对象，在集合中，对象应该可以比较

                        /**
                         * 构造单个属性的 IN 查询 ，相当于  @Query("select e from InfoEntity e where e.name in ?1")
                         * 此时： attribute 为 "name" ,  values 为 e.name 对象的集合
                         * @param searchFilter.getFieldName() 实体中的属性名称。对应上述列子，应是 InfoEntity 中的 province  属性。
                         *                  如果集合为空，会抛出异常
                         *                  处理办法是：在集合中放入一个不会影响程序运行结果的元素，进行比较。
                         *                  例如集合元素是对象 id ，long 类型，则放入 -1 ，id 不会是负数，所以不会影响程序运行。
                         *                  注意：
                         *                  1. 不能是级联属性，只能比较实体中的直接属性，例如 province.website 就不行。
                         *                  2. 如果遇到比较的是级联属性，则通过其他方法查询级联属性后再比较。如通过查询 website -> province ,之后还是直接比较 province
                         * @param searchFilter.getValue()    用于比较的对象集合。 对应上述列子，应该是 InfoEntity 中的 e.province 对象的集合
                         * @param <T>
                         * @return
                         */
                        if (Collection.class.isAssignableFrom(searchFilter.getValue().getClass()))
                            return path.in(searchFilter.getValue());
                        else
                            throw new IllegalArgumentException("不能比较! in 操作 ,被比较的对象必须是集合");
                    case NIN:
                        if (Collection.class.isAssignableFrom(searchFilter.getValue().getClass()))
                            return path.in(searchFilter.getValue()).not();
                        else
                            throw new IllegalArgumentException("不能比较! not in 操作 ,被比较的对象必须是集合");

                    case BETWEEN:
                        //BETWEEN 操作要求：属性代表的对象，应该可以进行比较，即实现了 Comparable 才可以，这符合 java 对象比较的原则
                        //包含两个边界 : betweenFrom <= object <= betweenTo
                        if (Comparable.class.isAssignableFrom(javaType))
                            return builder.between((Expression<? extends Comparable>) path, (Comparable) searchFilter.getBetweenFrom(), (Comparable) searchFilter.getBetweenTo());
                        else
                            throw new IllegalArgumentException("不能比较! between 操作 ,被比较的对象，应该实现了 Comparable 接口，对象之间能相互比较 !");
                    default:
                        return builder.conjunction();

                }
            }
        };
    }
}
