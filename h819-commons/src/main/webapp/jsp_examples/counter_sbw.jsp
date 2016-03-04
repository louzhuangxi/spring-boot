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
    <!--
        <link rel="stylesheet" type="text/css" href="styles.css">
        -->

    <meta http-equiv="Content-Type" content="text/html; charset=utf-8">
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
    //创建文件
    File fdir = new File(request.getRealPath("") + File.separator
            + "counter" + File.separator);

    if (!fdir.exists())
        fdir.mkdir();
    File counterf = new File(fdir, "sbw_counter.txt");
    if (!counterf.exists())
        counterf.createNewFile();

    File[] listFiles = fdir.listFiles();
    long creatTime = 0;
    String newestBackfileName = "";
    for (File f : listFiles) {

        if (f.getName().equals("sbw_counter.txt"))
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

    try {
        numold = reader.readLine();

        String newestBackfileNameCount = newreader.readLine();


        if (numold == null) {
            i = (int) (Integer.parseInt(newestBackfileNameCount)+1);;
        } else {
            i = (int) (Integer.parseInt(numold) + 1);
        }


        if (i % 500 == 0 && i > 1710000) {

            Date date = new Date();
            SimpleDateFormat dateformat = new SimpleDateFormat("yyyy-MM-dd HH_mm_ss");
            String sDateSuffix = dateformat.format(date);

            File descFile = new File(fdir, sDateSuffix + "_sbw_counter.txt");


            FileChannel fcin = new FileInputStream(counterf).getChannel();
            FileChannel fcout = new FileOutputStream(descFile).getChannel();
            fcin.transferTo(0, fcin.size(), fcout);
            fcin.close();
            fcout.close();
        }

        outf = new PrintWriter(new FileOutputStream(counterf));
        outf.print(i); //将i写入文件中
        reader.close();
        outf.close();

    } catch (FileNotFoundException e) {
        e.printStackTrace();
        reader.close();
        outf.close();
    }
%>

<%=i%>

</body>
</html>
