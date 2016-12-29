/**
 * Description : TODO()
 * User: h819
 * Date: 2016/12/20
 * -
 * 在使用静态方法的时候需要注意：
 * 1. 如果一个静态类中使用了静态变量，那么这个变量就是全局变量
 * 2. 此时如果该静态变量是一个集合 collection ，那么每调用一次该静态变量（如 add 操作），那么这个静态 collection 变量 就会增加一次，会变得无限大
 * 3. 需要每次调用时清空，例子见 FileUtilsBase.java
 *
 */
//还可以参考 :

//http://hutool.mydoc.io/?t=821
//https://github.com/looly/hutool/wiki
//https://github.com/venusdrogon
package org.h819.commons;