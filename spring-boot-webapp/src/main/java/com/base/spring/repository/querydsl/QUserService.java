package com.base.spring.repository.querydsl;

/**
 * Description : TODO()
 * User: h819
 * Date: 2016/9/27
 * Time: 16:22
 * To change this template use File | Settings | File Templates.
 */
//@Service
public class QUserService {

    // 结论 If we need to create dynamic database queries, we should use Querydsl.
    // 只是在动态查询的时候，用 Querydsl ,其他修改、删除等，还是用 jpa

    //http://www.baeldung.com/rest-api-search-language-spring-data-querydsl

    // querydsl 自动支持了动态查询
    /**
     * If we have to create dynamic database queries, we can use either the JPA Criteria API or Querydsl.
     * The pros of using Querydsl are:
     * 1.  It supports dynamic queries.
     * 2.  It has a very clean API. In other words, it is easy to create complex queries with Querydsl, and the query generation code is easy to read.
     * 3.  It also supports JDO, Lucene, and MongoDB.
     */
//    @Autowired
//    QUserRepository repository;
//
//    @PersistenceContext
//    EntityManager em;
//
//    private void test() {
//
//        //http://www.querydsl.com/static/querydsl/latest/reference/html/ch02.html#jpa_integration
//        //https://spring.io/blog/2011/04/26/advanced-spring-data-jpa-specifications-and-querydsl/
//
//        QUserEntity userEntity = QUserEntity.userEntity;
//
//        /**
//         *  com.querydsl.core.types.Predicate 构建方法
//         */
//
//        //and ,or
//        Predicate predicate = userEntity.userName.equalsIgnoreCase("dave")
//                .and(userEntity.password.startsWithIgnoreCase("mathews"));
//
//        // not
//        Predicate predicate1 = predicate.not();
//
//
//        // Using joins 好像不妥
//        //http://stackoverflow.com/questions/21637636/spring-data-jpa-with-querydslpredicateexecutor-and-joining-into-a-collection
//        JPAQuery<?> query = new JPAQuery<Void>(em);
//
//        /**
//         select e from UserEntity as e
//         left join e.roles as role
//         on role.name = 'dave'
//         */
//        Predicate predicate2 = userEntity.roles.any().name.eq("dave");
//
//
//        /**
//         * QueryDslPredicateExecutor 的接口方法
//         */
//        //exist
//        boolean exist = repository.exists(predicate);
//        //单个
//        UserEntity user = repository.findOne(predicate);
//        // List
//        Iterable<UserEntity> users = repository.findAll(predicate);
//        // List , sort
//        Iterable<UserEntity> users2 = repository.findAll(predicate, new Sort(Sort.Direction.ASC, "name"));
//        // count
//        long count = repository.count(predicate);
//        //page
//        Page<UserEntity> page = repository.findAll(predicate, new PageRequest(1, 10));
//        //page , sort
//        Page<UserEntity> page2 = repository.findAll(predicate, new PageRequest(1, 10, new Sort(Sort.Direction.ASC, "name")));

    //  }
}
