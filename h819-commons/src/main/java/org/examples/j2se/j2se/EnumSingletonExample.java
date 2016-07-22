package org.examples.j2se.j2se;

/**
 * Description : TODO()
 * User: h819
 * Date: 14-8-9
 * Time: 下午11:53
 * To change this template use File | Settings | File Templates.
 */
enum EnumSingleton {


    //文件名不必和 enum 名称相同，如本例
    //枚举常量之间使用 “,” 分割开来，这些常量默认都是“public static final”的 ，这也是为什么枚举常量采用大写字母来命名的原因
    //枚举中定义的每一个常量都是枚举类型 EnumSingleton 的一个实例。
    //枚举中定义的常量默认值是常量本身，可以自定义其常量值，但必须自己实现一个构造方法。

    //  学习任何新版语言的一个危险就是疯狂使用新的语法结构。如果这样做，那么您的代码就会突然之间有 80% 是泛型、标注和枚举。
    // 所以，应当只在适合使用枚举的地方才使用它。那么，枚举在什么地方适用呢？
    // 一条普遍规则是，任何使用常量的地方，例如目前用 switch 代码切换常量的地方。
    // 如果只有单独一个值（例如，鞋的最大尺寸，或者笼子中能装猴子的最大数目），则还是把这个任务留给常量吧。
    // 但是，如果定义了一组值，而这些值中的任何一个都可以用于特定的数据类型，那么将枚举用在这个地方最适合不过。


    /**
     * 演示自定义常量数值
     */
//    INSTANCE("Monday");
//
//    /**定义枚举类型自己的属性,需要自定义一个构造方法**/
//    private final String var;
//
//    private EnumSingleton(String var) {
//        this.var = var;
//    }

    INSTANCE;

    public void doSomeThing() {
        System.out.print("hello ,enum !");
    }

    //使用  EnumSingleton.INSTANCE.doSomeThing();
}
