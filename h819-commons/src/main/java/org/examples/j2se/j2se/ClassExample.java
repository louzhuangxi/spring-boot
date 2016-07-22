package org.examples.j2se.j2se;


/**
 * 演示类加载顺序
 * 
 * @author jianghui
 * 
 */

//先父类，后子类
//静态变量，后普通变量
//先构造方法，后普通方法


// 父类静态变量->子类静态变量->父类变量声明->父类游离块>父类构造方法->子类变量声明->子类游离块->子类构造方法
public class ClassExample extends father {

	public static Display dis5 = new Display("子类静态区");

	public Display dis6 = new Display("子类变量声明");

	public static void teststatic() {
		Display dis11 = new Display("子类静态方法");
	}
	
	public ClassExample() {
		Display dis8 = new Display("子类构造方法");
	}

	{
		Display dis7 = new Display("子类游离块");
	}

	public static void main(String[] args) {
		Display dis12 = new Display("测试开始：=====");
		ClassExample sub = new ClassExample();
		Display dis13 = new Display("测试结束：=====");	
		father.teststatic();
		
		
	}

}

class father {

	public static Display dis1 = new Display("父类静态区");

	public Display dis2 = new Display("父类变量声明");

	public static void teststatic() {
		Display dis10 = new Display("父类静态方法");
	}
	
	public father() {
		Display dis4 = new Display("父类构造方法");
	}

	{
		Display dis3 = new Display("父类游离块");
	}
}

class Display {
	public Display(String str) {
		System.out.println(str);
	}
}
