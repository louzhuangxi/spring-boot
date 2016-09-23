package org.examples.j2se.j2se;

/**
 * Description : TODO(双重校验锁方式创建，适应多线程的单例模式 example)
 * Description : TODO(有的设计模式说，用枚举的方式实现更好，没有测试)
 * User: h819
 * Date: 14-4-25
 * Time: 下午12:24
 * To change this template use File | Settings | File Templates.
 */
public class SingletonExample {

    private static volatile SingletonExample INSTANCE = null;

    // Private constructor suppresses
    // default public constructor
    private SingletonExample() {
    }

    //thread safe and performance  promote
    public static SingletonExample getInstance() {
        if (INSTANCE == null) {
            synchronized (SingletonExample.class) {
                //when more than two threads run into the first null check same time, to avoid instanced more than one time, it needs to be checked again.
                if (INSTANCE == null) {
                    INSTANCE = new SingletonExample();
                }
            }
        }
        return INSTANCE;
    }


    /**
     * 单例模式中的方法，写法如下
     */
    public void method() {

    }

    /**
     * 调用方法
     */
    private void example() {

        SingletonExample.getInstance().method();

    }


}
