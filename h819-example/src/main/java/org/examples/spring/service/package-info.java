/*
 *  @Service 层，@Service 类不能继承接口，否则无法自动注入
 */

/*
 * spring jpa ，关于事务中的实体管理
 * <p/>
 * 只读事务和没有事务有什么区别?
 */
//     属性  	                  类型	                                描述
//     value	                  String	                            指定事务管理器名字，默认使用<tx:annotation-driven/>指定的事务管理器，用于支持多事务管理器环境；
//     propagation	              enum: Propagation	                    指定事务传播行为，默认为Required，使用Propagation.REQUIRED指定；
//     isolation	              enum: Isolation	                    指定事务隔离级别，默认为“DEFAULT”，使用Isolation.DEFAULT指定；
//     readOnly	              boolean	                            指定事务是否只读，默认 false 表示事务非只读；
//     timeout	                  int (in seconds granularity)	        指定事务超时时间，以秒为单位，默认-1表示事务超时将依赖于底层事务系统；
//     rollbackFor	              Class对象数组，必须继承自Throwable	    指定一组异常类，遇到该类异常将回滚事务；默认只对 RuntimeException 异常回滚
//     rollbackForClassName	  类名数组，必须继承自Throwable	        指定一组异常类名字，其含义与<tx:method>中的rollback-for属性语义完全一样；
//     noRollbackFor	          Class对象数组，必须继承自Throwable	    指定一组异常类，即使遇到该类异常也将提交事务，即不回滚事务；
//     noRollbackForClassName	  类名数组，必须继承自Throwable	        指定一组异常类名字，其含义与<tx:method>中的no-rollback-for属性语义完全一样；


/*
 * @Transactional
 * 1. 注解在类上，表示该注解对于所有该类中的 public 方法都生效；注解方法上，则代表该注解仅对该方法有效，会覆盖先前从类层次继承下来的注解。
 * 2. 事务一般都在 Service 层配置，默认为 readOnly ，以提高性能；需要覆盖默认配置的，加在 Service 的方法上，如本例的 deleteOrUpdate()
 * 3. spring 代理的 Repository 接口，默认继承 spring jpa 的默认 @Transactional 设置，即 查询为 readOnly=true , update/delete 为 readOnly=false , 不需要在 Repository 接口处做 @Transactional 设置
 * 4.  @Transactional 注解应该只被应用到 public 方法上
 */

/*
 * 类之间相互调用，@Transactional 的作用顺序：事务应该设置在调用类上。
 */

// 只有调用类 BeanA 设置的 @Transactional 起作用，被调用类 BeanB 的设置不起作用。

//@Transactional(readOnly = true)  //此处的设置起作用，为 true
//public class BeanA{
//
//    @Autowired
//    BeanB beanB;
//
//    @Transactional(readOnly = false)
//    public void method1(){
//
//        beanB.method2();
//
//    }
//}
//
//    @Transactional(readOnly = false) //被调用了，此处不起作用
//    public class BeanB{
//
//        @Transactional(readOnly = false) //被调用了，此处不起作用
//        public void method2(){
//            // do something
//        }
//    }

/*
 * spring jpa ，关于事务中的实体管理
 */
// 在开启了事务的方法中，仅标记 @Transactional (默认 readOnly = false) ，那么受该事务管理的实体，发生变化，在事务结束的时候，会自动同步到数据库中，而不必显式的调用事务的保存方法，事务本身就接管了实体管理。
// 例如：
// UserEntity 对应于数据库表 user
// 在代码中，仅仅是 UserEntity user = new UserEntity()  一句，而没有任何保存操作，事务管理器会在事务结束的时候，尝试去把 UserEntity 插入到数据库中。所以此时不能把 UserEntity 作为值对象用来仅仅传递数值。
// 同样，仅仅是 user.setName("")  一句，而没有任何保存操作，事务管理器会去尝试 update 对应表中的记录。
//
// ====================================================================
// 反之，
// 如果方法标注为 @Transactional (readOnly = false) 或 不开启事务
// UserEntity user = new UserEntity() 或   user.setName("")  操作，如果想要保存到数据库，需要显式进行 userRepository.save(user) 才可以。
// 此时 user 可以作为一个值对象，传递到显示层(不推荐，应该转换为 DTO)。
//
// ====================================================================
// 总结
//  在 hibernate 的事务中 (@Transactional)，POJO 代表一个底层的数据库表，POJO 的任何变化，都会自动同步到底层数据库。
//  所以POJO对象不能随意的作为一个值对象在各个层次中传递，需要转换为 DTO 对象。(详见 example 项目描述)
//
//  统一为 POJO -> DTO 形式，用法见 DynamicSearchJqgridController.java ，利用 DTOUtils 工具进行转换



/*
 *  只读事务和没有事务有什么区别?
 */
// 在执行完第一条sql的时候，执行第二条查询SQL,如果两个操作之间有数据被改变了，第二条数据查询就有可能不一致。
//  而只读查询则保证了事务级别的读一致性，即在该事务范围内执行的多条SQL都只会看到执行前点的数据状态，而不会看到事务期间的任何被其他 SQL改变的状态。

//关于拦截方法调用其他内部方法无法被拦截问题的解决

package org.examples.spring.service;