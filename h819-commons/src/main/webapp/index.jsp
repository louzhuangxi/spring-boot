<%--
  Created by IntelliJ IDEA.
  User: Jianghui
  Date: 13-12-23
  Time: 下午5:30
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>

<html>
<head>
    <title></title>
    <!--
    <meta http-equiv="refresh" content="0;url=${ctx}/jpa/admin-index.html">
    -->
    <link href="${ctx}/static/ace/assets/css/bootstrap.min.css" rel="stylesheet" type="text/css">
    <link rel="stylesheet" href="${ctx}/static/ace/assets/css/ace.min.css" id="main-ace-style" />
    <link rel="stylesheet" href="${ctx}/static/ace/assets/css/ace-skins.min.css" />
    <link rel="stylesheet" href="${ctx}/static/ace/assets/css/ace-rtl.min.css" />
    <link rel="stylesheet" href="${ctx}/static/ace/assets/css/ace-fonts.css" />
</head>
<body>

<div class="container">

  <!--首页跳转
    <c:redirect url="/page/ajax.html#page/jqgrid_latest"></c:redirect>
    -->

    <div class="row">
        <div class="col-xs-12">
            <h3 class="header smaller lighter green">ace admin template 演示</h3>
        </div>

        <div class="col-xs-12">
            <!-- PAGE CONTENT BEGINS -->
            <div class="well well-sm">
                <button type="button" class="close line-height-0" data-dismiss="alert"> <i class="ace-icon fa fa-times smaller-75"></i> </button>
                在线帮助: <a href="http://www.canhelp.cn:8080/ace/" target="_blank"> http://www.canhelp.cn:8080/ace/ <i class="fa fa-external-link bigger-110"></i> </a> </div>
            <table id="grid-table">
            </table>
            <div id="grid-pager"></div>
            <script type="text/javascript">
                var $path_base = "../..";//in Ace demo this will be used for editurl parameter
            </script>
            <!-- PAGE CONTENT ENDS -->
        </div>

    </div>


        <div class="row">
            <div class="col-md-2">
                <p>
                    <a href="${ctx}/page/ajax.html">
                        <button type="button" class="btn btn-sm btn-primary">ajax index</button>
                    </a>
                </p>
            </div>

            <div class="col-md-2">
                <p>
                    <a href="${ctx}/static/ace/html/index.html">
                        <button type="button" class="btn btn-sm btn-primary">访问静态页</button>
                    </a>
                </p>
            </div>

            <div class="col-md-2">
                <p>
                    <a href="${ctx}/page/login.html">
                        <button type="button" class="btn btn-sm btn-primary">login</button>
                    </a>
                </p>
            </div>

        </div>
    </div>


</body>
</html>