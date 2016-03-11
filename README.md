# spring-boot

## 更新
> 便于查看是否有重要更新
- spring-boot-web : 0.1.0
- h819-commons : 1.0.10
- spring-security-oauth
- 逐步发布其他项目  ...

## 概述
做这个项目的想法来自于 [springside](https://github.com/springside/springside4/) ,           当时自己在不停的尝试各种框架和实现技术，为了选某一种实现而比来比去大费脑筋，
[springside](https://github.com/springside/springside4/)   给了一个很好的示范，从中学习到了很多知识，感叹是水太深了，什么时候能学完呢 --

Spring 现在基本上也一统江湖了，只要你想要的解决方案，Spring 基本上都有相关实现，没事可以多看看他的 [项目](http://spring.io/projects/)。

我的习惯是，一边学习一边敲代码，读书笔记就用注释写在代码示例里，我只记住我做过什么就可以了，当需要相关功能的时候，到示例里去找，扫一眼代码写法，读一下注释，基本上也就知道怎么编写了。代码中有引用其他项目的代码，不一定按规矩注明出处，基本上都是忘了，逐渐养成习惯吧。

> 初学者多读读注释，也算是学习的一个过程。有经验的同学，也帮我指点一下理解的偏差，给我推荐点资料也好提高一下。

随着对相关原理理解的加深，代码在不停的 Refactor，就算是自己的编程经验总结吧。

项目取名为 **spring-boot** ，意在显式的指出用的是 **Spring Boot**。

h819 是我的 id。

这些项目会随着自己的总结，不停的添加和优化，示例也会越来越多。过一站子，在 aliyun 上把做好的应用搭建起来，也好有个直观感受。

## 项目简介

:recycle:
### spring-boot-web

> 基于 Spring Boot 的 J2EE 开发实践，不发明什么，只是探索一种快速开发体验，开箱即用。

#### Core
- spring boot

#### Data
- spring data JPA
- hibernate
- querydsl

#### Web
- spring mvc
- FreeMarker Template
- Bootstrap
- JQuery
- Ace Admin

#### security
- spring security
- spring security oauth2

#### Web Server
- tomcat
- jetty

#### DB Server
- mysql
- oracle

#### Environment
- Intellij IDEA
- Maven
- git

#### Utils
- h819 commons
- Apache Commons
- Guava
- fastjson


#### Ask
- [Google](http://www.google.com/) (一定想办法上)
- [git hub ](https://github.com//)(你能想到的，基本都有实现)
- [Stack Overflow ](http://stackoverflow.com/)
- [spring boot examples](https://github.com/spring-projects/spring-boot/tree/master/spring-boot-samples)
- [oschina ](http://www.oschina.net/)


:recycle:

### h819-commons
> h819-commons
这是一个基础工具包，能总结出来的都放在这里，可以生成 jar 文件引用到其他项目中，模拟 [apache commons](https://commons.apache.org/) 做法，做成一个符合自己需要的工具包。

可以多翻翻代码，里面有各种例子总结。

值得提到的工具有：

#### java se commons

Components | Description | Source
---|---|---
Ftp | 可以连接 ftp 和 sftp，支持断点续传，比较文件是否发生变换 | /commons
Exec  | java 执行系统命令 | /commons
Pdf  | Pdf 新建、加密解密、加水印、页数统计、删除指定页等 | /commons
QRCode  | 二维码 | /commons
Others  | 还有一些常用工具，就不列举了 ... | /commons


#### web 工具
Components | Description | Source | Demo
---|---|---|---
DTOUtils | PO to DTO 工具。使用 ***hibernate*** 的同学，估计对这个比较挠头，每次转换都费时费力，还容易出错。DTOUtils 可以实现自动转换，截断递归关联，对于级联层次很深的对象，可以指定转换深度。比目前大多数人采用 bean copy 的方案好。这个有时间我写一篇博客，详细说一下。 | /web | [url](###) 
Spring JPA  | spring jpa 动态查询工具，可以动态组装查询条件，自动分页，很好用 | /web | [url](###) 
Jqgird  | [Jqgrid](http://www.trirand.com/blog/?page_id=6/) 工具类，可以方便的处理查询条件。 | /web | [url](###) 
ZTree | [ZTree](http://www.ztree.me/v3/main.php#_zTreeInfo/) java utils ，功能强大，做后台管理用。 | /web| [demo](http://www.canhelp.cn/boot/example/tree/manage/ztree.html)
Fuelux Tree  | [FueluxTree](http://getfuelux.com/javascript.html#tree/) java utils ，ui 很好看，做展示用吧。 | /web | [demo](http://www.canhelp.cn/boot/example/tree/manage/fuelux.html)
flexpaper  | [flexpaper](http://flexpaper.devaldi.com/annotate-pdf-documents-online.jsp) 在线文档展示的一种解决方案 | /web | [url](###) 
Others | 逐步添加 ... | /web

:recycle:
### spring-security-oauth
> spring security oauth2 , annotation 。需要注意的大坑是，oauth2-server 和 oauth2-resource 不能配置在一起，否则不能出现登录界面。貌似 xml 方式没问题。
配置了很久，没有解决。

- spring-security-oauth2-server
- spring-security-oauth2-resource
- spring-security-oauth2-client


## Contact
:e-mail: h81900 at gmail . com