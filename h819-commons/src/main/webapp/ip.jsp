<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ page import="java.io.*"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>">
    
    <title>首都标准网</title>
    
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
    
    <%
	
	
			//测试并记录 ip 地址
		File fdir = new File(request.getRealPath("") + File.separator
				+ "logs" + File.separator);
		
		if (!fdir.exists())
			fdir.mkdir();
		
		File counterf = new File(fdir, "ip-logs.txt"); 	
		
    
		String ip = request.getHeader("x-forwarded-for");
		//ip = request.getRemoteAddr();
		
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("WL-Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getRemoteAddr();
		}
	
		
		String ipA = ip.split("\\.")[0];
		String ipB = ip.split("\\.")[1];
		String ipC = ip.split("\\.")[2];
		String ipD = ipA+"."+ipB+"."+ipC;
		
		FileWriter fw = null;

		try {
			// 如果文件存在，则追加内容；如果文件不存在，则创建文件
			fw = new FileWriter(counterf, true);
			//追加
			PrintWriter pw = new PrintWriter(fw);
			pw.println(ip+" -> "+new java.util.Date()+"\r");
			pw.flush();
			
			fw.flush();
			pw.close();
			fw.close();
			
		} catch (IOException e) {
			try {
				fw.close();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			e.printStackTrace();
		}

		
		if(ipD.equals("129.9.101")||ipD.equals("129.9.100")||ipD.equals("129.9.200")){
		    response.sendRedirect("http://129.9.100.10/innerApp");
		}else{
		    response.sendRedirect("http://202.106.162.203/innerApp");
		}
    %>
    
    
  </body>
</html>
