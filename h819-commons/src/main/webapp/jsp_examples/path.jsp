
<%@ page language="java" import="java.util.*" pageEncoding="ISO-8859-1"%>
<%

//====经过验证，myeclipse 建立的jsp文件，其中自动生成的计算  base 的代码不好用

//下述代码，用来取得实际文件所在的文件夹，计算出 basePath
//设定正确的 base 之后，dreamweaver 也可以识别相关的资源位置

//取得应用所在的目录
String path = request.getContextPath();
//取得文件所在的文件夹，并且取得最后一个 "/" 的部分
String path1 = request.getServletPath();
int i = path1.lastIndexOf("/");
String path2 = path1.substring(0,i);
String basePath = request.getScheme() + "://"  
        + request.getServerName() + ":" + request.getServerPort()   
        + path + path2 + "/";  

%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>">
    
    <title>My JSP 'index.jsp' starting page</title>
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
	<!--
	<link rel="stylesheet" type="text/css" href="styles.css">
	-->
  </head>
  
  <body>
    This is my JSP page. <br>
  </body>
</html>
