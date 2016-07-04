<%@ page contentType="text/html; charset=utf-8" language="java" import="java.sql.*" errorPage=""%>
<%@ page  %>
<%@taglib prefix="s" uri="/struts-tags"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">

<%
	//验证码专用，必须放在前面，否则 js 函数不可识别 path 变量
	String path = request.getContextPath();
%>

<script>
	
function reloadImage() 
{ 
//重新访问一次 servelet
//这里必须加入随机数不然地址相同无法重新加载
var verify=document.getElementById('validatecode');
verify.setAttribute('src','<%=path%>/validateCodeServlet.servlet?'+Math.random());

} 
</script> 


<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />

<title>Validate Code Show Example</title>

</head>
<body leftmargin="0" topmargin="0">
<table width="227" border="0" cellspacing="0" cellpadding="0">
  <tr>
    <td width="67">验证码</td>
    <td width="68"><img id="validatecode" src="<%=path%>/servlet/validateCodeServlet" width="60" height="20" /></td>

    <td width="92"><a href="JavaScript:reloadImage();">看不清楚</a></td>
  </tr>
</table>

</body>
</html>
