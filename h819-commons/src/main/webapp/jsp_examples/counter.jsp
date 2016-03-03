<%@ page language="java" import="java.io.*" pageEncoding="UTF-8" %>
<%@ page import="java.nio.channels.FileChannel" %>
<%@ page import="java.text.SimpleDateFormat" %>
<%@ page import="java.util.Date" %>
<%
    String path = request.getContextPath();
    String basePath = request.getScheme() + "://"
            + request.getServerName() + ":" + request.getServerPort()
            + path + "/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>

<head>
    <base href="<%=basePath%>">

    <title>网页计数器</title>

    <meta http-equiv="pragma" content="no-cache">
    <meta http-equiv="cache-control" content="no-cache">
    <meta http-equiv="expires" content="0">
    <meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
    <meta http-equiv="description" content="This is my page">
    <style type="text/css">
        body {
            margin-left: 0px;
            margin-top: 0px;
            margin-right: 0px;
            margin-bottom: 0px;
            font-size: 12px;
        }

        #x {
            text-align: left;
        }
    </style>
</head>

<body>

<%
    //纯 jsp计数器
    //对于读、取文件，应该用定时任务，以减少io操作。
    //但定时任务应该用 servlet，非单独 jsp 可以完成
    //输出数字，也可输出数字图片
    //简单的，可以选择“刷新纪录为一次”和“打开关闭浏览器记录为一次”

    //创建文件
    File fdir = new File(request.getRealPath("") + File.separator
            + "counter" + File.separator);
    out.println(fdir);

    if (!fdir.exists())
        fdir.mkdir();
    File counterf = new File(fdir, "counter.txt"); //获取   File对象   在相应的位置建一个count.txt的文件
    if (!counterf.exists())
        counterf.createNewFile();


    //找到最新备份的文件
    File[] listFiles = fdir.listFiles();
    long creatTime = 0;
    String newestBackfileName = "";
    for (File f : listFiles) {

        if (f.getName().equals("sbw_counter.txt"))//去掉最新的非备份文件
            continue;
        long temp = f.lastModified();

        if (temp > creatTime) {
            creatTime = temp;
            newestBackfileName = f.getAbsolutePath();
        }
    }


    BufferedReader reader; //BufferedReader对象，读取文件数据
    BufferedReader newreader; //BufferedReader对象，读取文件数据
    PrintWriter outf = null; // 写文件对象
    String numold = ""; //文本文件中已存储的值
    int i = 0; //记数值

    /**
     每刷新一次，计数加 1
     */

    reader = new BufferedReader(new FileReader(counterf));
    newreader = new BufferedReader(new FileReader(newestBackfileName));

		/*
		放在一个 try 中，读或者写有任一出现问题，直接退出，不进行下面的操作。
		*/
    try {
        numold = reader.readLine();

        String newestBackfileNameCount = newreader.readLine();

        //发现问题：经常出现i从0开始计数，即 numold == null 条件出现
        //从最新的备份文件中恢复 : 小于指定值时，重置 i 为最新的备份值
        if (numold == null) { // numold == null 第一次运行时 ，去掉 第一次运行考虑
            i = (int) (Integer.parseInt(newestBackfileNameCount) + 1);
        } else {
            i = (int) (Integer.parseInt(numold) + 1);
        }

        //每500次，备份一次
        //加入i大小判断：如果 i 被清零，小于原始值，则停止备份，保留备份点在清零前
        if (i % 500 == 0 && i > 1710000) {

            Date date = new Date();
            SimpleDateFormat dateformat = new SimpleDateFormat("yyyy-MM-dd HH_mm_ss");
            String sDateSuffix = dateformat.format(date);

            File descFile = new File(fdir, sDateSuffix + "_sbw_counter.txt");


            FileChannel fcin = new FileInputStream(counterf).getChannel();
            FileChannel fcout = new FileOutputStream(descFile).getChannel();
            long size = fcin.size();
            fcin.transferTo(0, fcin.size(), fcout);
            fcin.close();
            fcout.close();
        }

        outf = new PrintWriter(new FileOutputStream(counterf));
        outf.println(i); //将i写入文件中
        reader.close();
        outf.close();

    } catch (FileNotFoundException e) {
        e.printStackTrace();
        reader.close();
        outf.close();
    }
		/*
		 优化，记录登录人数
		
		//每次打开浏览器到关闭浏览器，计数加 1.
		//此方法统计的是浏览人次
		//应用第一次启动
		if (application.getAttribute("counter") == null) {
			application.setAttribute("counter", new Integer(numold));
		}

		int j = (Integer) application.getAttribute("counter");
		if (session.isNew())
			++j;
		application.setAttribute("counter", j);
		//仿照上例，更新到文件
		
		 */
%>

每次刷新，计数加 1 ：<%=i%>

<!--

		打开浏览器到关闭浏览器，计数加 1 ：
		
		<%//=j%>

	-->


</body>
</html>
