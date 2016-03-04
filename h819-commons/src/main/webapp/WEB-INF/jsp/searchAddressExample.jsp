<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
 <!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
 <html>
 <head>
 <meta http-equiv="Content-Type" content="text/html; charset=utf-8">
 <title>Insert title here</title>
 </head>
 <body>
     <div>
         <form action="${ctx}/search/list" methed="post">
             <!--
              search_LIKE_city

             表单名字命名规则:
             search  : 可以在 AddressControllerExample 类中，通过 Servlets.getParametersStartingWith(request, "search_") 进行处理，所以 search 可为任意字符串
             LIKE : SearchFilter 中定义，只能为  EQ, LIKE, GT, LT, GTE, LTE  之一
             city ： 待查询的字段， 必须是待查询的数据库表有的字段名

             三者直接有下划线 _ 连接

             可以有多个表单，多个表单直接的关系为只能为 AND 或 OR 两张，在 Controller 中处理

             -->
             <label>名称：</label> <input type="text" name="search_LIKE_city" class="input-medium" value="">
             <label>地址：</label> <input type="text" name="search_LIKE_street" class="input-medium" value="">
             <button type="submit" class="btn" id="search_btn">Search</button>
         </form>
     </div>
 </body>
 </html>
