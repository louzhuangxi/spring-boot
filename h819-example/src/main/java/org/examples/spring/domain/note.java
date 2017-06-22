package org.examples.spring.domain;

/**
 * Description : TODO()
 * User: h819
 * Date: 2015/4/16
 * Time: 18:12
 * To change this template use File | Settings | File Templates.
 */
// 需要用 真正的 Class 文件做注释，否则自动格式化的时候，会合并所有 /** */ 的内容
@Deprecated
abstract class note {

    /**
     * ============================ 实体关联关系设计 ===========================================
     */


    /**
     * 注意事项 :
     * ---------------------------------------------------------------------------
     * 1. 关系表方式，有主表和从表之分，顺序不要搞反
     *
     * ---------------------------------------------------------------------------
     *
     * 2. 不用 many to many ，如果必须要用，用两个 one to many 代替。(hibernate reference : Chapter 24. Best Practices)
     *    多对多连接用得好的例子实际上相当少见。
     *    大多数时候你在“连接表”中需要保存额外的信息。这种情况下，用两个指向中介类的一对多的连接比较好。(对关系添加属性)
     *    实际上，我们认为绝大多数的连接是一对多和多对一的。因此，你应该谨慎使用其它连接风格。
     *
     * ---------------------------------------------------------------------------
     *
     * 3. 单向关联优先于双向关联，根据业务需要，如无明显使用场景，尽量用单向。使用双向为特例，需仔细比较二者的优缺点。
     *
     * 3.1  双向缺点:
     *   1) 双向操作虽会带来便利，但增加复杂度。如有的 one to many 单向，如果变成双向， 会转换为 many to many 的关系，变得复杂
     *   2) 双向的关联，大多数 object to json string 工具都会陷入死循环。(已经用 DtoUtils 解决)
     *
     *   注： 设置为单向之后，即使是需要双向单向的操作，通过 JPQL 语句也能查出来.
     * -
     * 3.2 双向优点:
     *  1) 小部分设计，双向会比单向无论在逻辑上还是实体的设计上，都会简洁很多，此时再考虑用双向。
     *  2) 关联关系双方都可以维护，而单向关联只能从主表（发出端）维护
     * -
     * 3.3 单向缺点
     *  1）关联关系只能从主表（发出端）维护
     * -
     * 3.4 单向优点
     *  1）设计简单，逻辑清晰
     *
     *  重要：
     *  在实体关系设计的时候，用双向还是单向，需要仔细斟酌，达到实体构成设计简单，所需要的业务逻辑代码简单的最优效果。
     * -
     *  经验总结：
     *  刚开始接触 hibernate 实体设计的时候，大量的用了双向，大量的把一个实体同其他实体都建立关联，以图在一个实体上能查询到所需要的所有信息，其结果是实体变得复杂无比
     *  其实大多数情况下，通过一个实体查询另外一个实体，通过相关的 JPQL 就可以实现(JPQL 可以和 sql 一样，可以拼接出各种查询)，而不一定需要在实体上建立关联，通过实体对象直接获得。
     *
     * ---------------------------------------------------------------------------
     *
     * 4. 表关联 or 主键关联 ？
     *   如果一个实体和多个实体对应，那么主键方式，会在相关的表中建立很多冗余数据， 会变得凌乱不堪，尤其是一对多的情况下。
     *   而中间表的方式，双方实体都不发生变化，所有的关联关系，都通过中间表建立，中间表记录的都是双方的 id，所以表结构和内容都非常干净。
     *   关系表表不需要建立真正的实体，由 hibernate 自动建立数据库表，代码中还是两个实体，所以对编码来讲，不多费功夫
     * -
     *   多个实体之间相互建立关系，会自动创建很多关系表，使数据库表数量大增，不过这又有什么呢，系统自动维护这些表，不管他就是了。
     *   只有需要对关系增加属性，才需要建立真正的关系实体，在该实体上增加属性，见  "many to many ，one to many + 关系表实现"
     *   所以统一用关系表的方式吧！！！
     *
     *   个别情况下，再考虑用主键方式，主要看设计出的数据库表是否凌乱！
     *
     *   主键方式会凌乱的例子:
     *   如 用户(user)和省市(website)的订阅关系，即一个用户可以订阅很多 website 的信息，website 一般会作为基础信息表。
     *   如果利用外键的方式：
     *   1）同一用户订阅不同的 website ，会在 website 表中， copyPages 每个被订阅的 website 的基础信息和对应的 users id
     *   2) 不同的用户，订阅不同的 website ，重复 1）
     *   这样就会在 website 中出现大量的 user id , 因为又和 website 基础信息对应，使 website 表变得很凌乱
     *   而我们希望 website 是基础信息表，记录 website 的基本信息，供其他方法调用，很不是重复很多次 website 基础信息和 大量 user id 的表
     *
     *   此时应该用中间表，以保持 website 基础信息表不被污染
     *   如果 website 再和其他的对象有关系，还会在 website 表中插入其他的标记，更加凌乱！
     *
     *   在表关联的情况下，设置 cascade = CascadeType.ALL ，级联操作的是关联表，而不是被级联对象，所以设置 cascade = CascadeType.ALL 是没有问题的!

     *   7. bug :
     *      用自动创建表的功能， <property name="generateDdl" value="true"/>
     *      JoinTable 方式 自定义中间表的唯一性约束   uniqueConstraints = {@UniqueConstraint(columnNames = {"parent_id", "child_id"})
     *      测试的时候发现，自动创建的表，有时候只生成一个外键约束，有时候生成两个，不知道什么原因
     *      如果生成的不正确，手工设置
     *      生成的表，还有其他的约束性错误，只能发现之后，手工修改
     *
     *
     *
     */


    /**
     * 注意事项
     * -
     * 1. 在 one to many 关系中，注意调整  @Fetch(FetchMode.SUBSELECT) 类避免 n+1 问题 ，详见 TreeEntity
     *
     * ---------------------------------------------------------------------------
     *
     * 2. bug :
     *    用自动创建表的功能， <property name="generateDdl" value="true"/>
     *    JoinTable 方式 自定义中间表的唯一性约束   uniqueConstraints = {@UniqueConstraint(columnNames = {"parent_id", "child_id"})
     *    测试的时候发现，自动创建的表，有时候只生成一个外键约束，有时候生成两个，不知道什么原因
     *    如果生成的不正确，手工设置
     *    生成的表，还有其他的约束性错误，只能发现之后，手工修改
     *
     * 3. Spring JPA 中应用
     *    实体必须有无参的构造方法, JPA 中动态构造查询条件 org.springframework.data.jpa.domain.Specification 需要无参的构造方法
     *
     */


    /**
     * 实体映射规则：
     *
     * 1. 实体类必须用 @javax.persistence.Entity 进行注解；
     *
     * 2. 必须使用 @javax.persistence.Id 来注解一个主键；
     *
     * 3. 实体类必须拥有一个 public 或者 protected 的无参构造函数，之外实体类还可以拥有其他的构造函数
     *    (如果应用了 org.springframework.data.jpa.domain.Specification ，则必须为 public)；
     *
     * 4. 实体类必须是一个顶级类（top-level class）。一个枚举（enum）或者一个接口（interface）不能被注解为一个实体；
     *
     * 5. 实体类不能是 final 类型的，也不能有 final 类型的方法；
     *
     * 6. 如果实体类的一个实例需要用传值的方式调用（例如，远程调用），则这个实体类必须实现（implements）java.io.Serializable 接口。
     *
     * 7. 集合用 set ，可以自动过滤重复元素
     */

    /**
     * 名词：
     *
     * 1. foreign key ：外键关联方式，不创建中间表
     *
     * 2. join table :  中间表方式，创建中间表来维护
     *
     * 3. bidirectional : 双向的，两个表可以互相访问。区分主从表
     *                   1） 两个都是主表（发出端），都可以维护关联关系
     *                   2） 一个表为主表（发出端），另外一个表是从表（接收端），主表能维护关联关系，从表不能。
     *
     * 4. unidirectional : 单向的，只能从一个表访问另外一个表，反之不能。
     *                    主表维护关联关系
     * */


/*
 * ============================ 注意各个 Entity 中的 添加、删除关联关系的自定义方法 ===========================================
 *
 *   在实体类中增加添加和删除方法，添加之前进行判断
 *
 *   ParentEntity5
 *
    public void addChild(ChildEntity5 child) {
        if (child != null) {
            this.children.add(child);
            child.setParent(this);
        }
    }

    public void removeChild(ChildEntity5 child) {
        if (child != null) {
            child.setParent(null);
            this.children.remove(child);
        }
    }
 *
 *
 *   调用
 *
 *    ParentEntity5 p = repositoryParent.findOne(Long.valueOf("p_id"));
 *
 *   添加 ParentEntity5 和 ChildEntity5 的一个关系
 *    p.addChild(repositoryChild.findOne(Long.valueOf("c_id")));
 *
 *   删除 ParentEntity5 和 ChildEntity5 的一个关系
 *    p.removeChild(repositoryChild.findOne(Long.valueOf("c_id")));
 *
 *   最后保存
 *    repositoryParent.save(p);
 *
 *    查看后台 hibernate 语句可以看出：
 *    此种添加和删除关系方式，会先删除所有关系，再逐个插入 “添加/删除后的关系集合”，就是说，会执行集合元素个数次 copyPages 操作，不知道为什么不能单单仅对关系表插入一条记录或删除一条记录
 *
 *
 * */

// ====


/*
 * ============================ 树状结构 ===========================================
 *   TreeEntity
 */
//注意 根节点为 null 问题，详见 实现类 TreeEntity

/*
 * ============================ jpa 2.1 ===========================================
 */
/*
 * jpa 2.1 @EntityGraph特性  : 解决设置为 LAZY 的级联对象，查询子 Entity 时，每个子 Entity 都会发送一条的查询语句。加上 @EntityGraph 后，只发送一条语句，即可查询所有级联对象
 *
 * jpa2.0 中，对于 Entity 中关联关系设置为 LAZY 的属性，如果需要马上加载，级联查询其关联的子 Entity 时，每个子 Entity 都会发送一条查询语句，子 Entity 数量大时，影响性能
 *
 * jpa2.1 中，在 Entity 中定义 @NamedEntityGraph ，在 repository 中查询用 @EntityGraph，可以只发送一条查询语句，级联查询关联的所有指定对象(hibernate 自动用 join 查询)。
 *
 * 需要注意的是，应用中调用 repository 中方法时，如果用到了级联查询，就加上  @EntityGraph ，否则不加，加上之后，总是会执行级联查询。
 *
 * 但 spring jpa 1.8 没有实现动态查询功能，即没有 Subgraph 属性设置
 *
 * jpa21 文件夹演示了相关的功能
 *
 * 目前不知道  Querydsl 是否有相应的功能
 *
 */


}
