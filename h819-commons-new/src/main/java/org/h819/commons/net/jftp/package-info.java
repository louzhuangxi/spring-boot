/**
 * Description : TODO() Connect to FTP, FTPS and SFTP servers
 * https://github.com/JAGFin1/JFTP , 完全拷贝的源码，并做了少许修改。
 * 利用 apache commons net 连接 ftp,ftps
 * 但 apache commons net 不支持 sftp ,(即 sftp over ssl 协议直接连接服务器)
 * sftp 用 JSch (http://www.jcraft.com/jsch/)
 * FtpExamples 演示了用法
 */
    /*
       1、抽象类不能被实例化，实例化的工作应该交由它的子类来完成，它只需要有一个引用即可。
       2、抽象方法必须由子类来进行重写。
       3、只要包含一个抽象方法的抽象类，该方法必须要定义成抽象类，不管是否还包含有其他方法。
       4、抽象类中可以包含具体的方法，当然也可以不包含抽象方法。
       5、子类中的抽象方法不能与父类的抽象方法同名。
       6、abstract不能与final并列修饰同一个类。
       7、abstract 不能与private、static、final或native并列修饰同一个方法。
    */

 //如果判断文件上传成功 ？ http://stackoverflow.com/questions/14452456/how-to-know-if-file-is-complete-on-the-server-using-ftp

package org.h819.commons.net.jftp;