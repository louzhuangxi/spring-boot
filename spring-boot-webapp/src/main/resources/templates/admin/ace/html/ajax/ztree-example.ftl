<#--声明变量-->
<#assign ctx = "${context.contextPath}">

<!DOCTYPE html>
<html lang="en">
<head>
<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1" />
<meta charset="utf-8" />
<title>Treeview - Ace Admin</title>
<meta name="description" content="with selectable elements and custom icons" />
<meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0" />
<!-- bootstrap & fontawesome -->
<link rel="stylesheet" href="${ctx}/ace/assets/css/bootstrap.css" />
<link rel="stylesheet" href="${ctx}/ace/assets/css/font-awesome.css" />
<!-- page specific plugin styles -->
<!-- text fonts -->
<link rel="stylesheet" href="${ctx}/ace/assets/css/ace-fonts.css" />
<!-- ace styles -->
<link rel="stylesheet" href="${ctx}/ace/assets/css/ace.css" class="ace-main-stylesheet" id="main-ace-style" />
<!-- ztree metroStyle -->
<link rel="stylesheet" href="${ctx}/zTree/css/metroStyle/metroStyle.css" type="text/css">
<!--[if lte IE 9]>
			<link rel="stylesheet" href="${ctx}/ace/assets/css/ace-part2.css" class="ace-main-stylesheet" />
		<![endif]-->
<!--[if lte IE 9]>
		  <link rel="stylesheet" href="${ctx}/ace/assets/css/ace-ie.css" />
		<![endif]-->
<!-- inline styles related to this page -->
<!-- ace settings handler -->
<script src="${ctx}/ace/assets/js/ace-extra.js"></script>
<!-- HTML5shiv and Respond.js for IE8 to support HTML5 elements and media queries -->
<!--[if lte IE 8]>
		<script src="${ctx}/ace/assets/js/html5shiv.js"></script>
		<script src="${ctx}/ace/assets/js/respond.js"></script>
		<![endif]-->
<link rel="stylesheet" href="${ctx}/jquery-confirm/jquery-confirm.css"  />
<style type="text/css">
.ztree li a.copy {
	padding-top:0;
	background-color:#2e8965;
	color:white;
	border:1px #316AC5 solid;
}
.ztree li a.cut {
	padding-top:0;
	background-color:silver;
	color:#111;
	border:1px #316AC5 dotted;	
}
</style>
</head>
<body class="no-skin">
<!-- #section:basics/navbar.layout -->
<div id="navbar" class="navbar navbar-default">
  <div class="navbar-container" id="navbar-container">
    <!-- #section:basics/sidebar.mobile.toggle -->
    <!-- /section:basics/sidebar.mobile.toggle -->
    <div class="navbar-header pull-left">
      <!-- #section:basics/navbar.layout.brand -->
      <a href="#" class="navbar-brand"> <small> <i class="fa fa-leaf"></i> Ace Admin </small> </a>
      <!-- /section:basics/navbar.layout.brand -->
      <!-- #section:basics/navbar.toggle -->
      <!-- /section:basics/navbar.toggle -->
    </div>
    <!-- #section:basics/navbar.dropdown -->
    <!-- /section:basics/navbar.dropdown -->
  </div>
  <!-- /.navbar-container -->
</div>
<!-- /section:basics/navbar.layout -->
<div class="main-container" id="main-container">
<!-- #section:basics/sidebar -->
<!-- /section:basics/sidebar -->
<div class="main-content">
<div class="main-content-inner">
  <!-- #section:basics/content.breadcrumbs -->
  <div class="breadcrumbs" id="breadcrumbs">
    <ul class="breadcrumb">
      <li> <i class="ace-icon fa fa-home home-icon"></i> <a href="#">Home</a> </li>
      <li> <a href="#">UI &amp; Elements</a> </li>
      <li class="active">Treeview</li>
    </ul>
    <!-- /.breadcrumb -->
    <!-- #section:basics/content.searchbox -->
    <div class="nav-search" id="nav-search">
      <form class="form-search">
        <span class="input-icon">
        <input type="text" placeholder="Search ..." class="nav-search-input" id="nav-search-input" autocomplete="off" />
        <i class="ace-icon fa fa-search nav-search-icon"></i> </span>
      </form>
    </div>
    <!-- /.nav-search -->
    <!-- /section:basics/content.searchbox -->
  </div>
  <!-- /section:basics/content.breadcrumbs -->
  <div class="page-content">
    <!-- #section:settings.box -->
    <!-- /.ace-settings-container -->
    <!-- /section:settings.box -->
    <div class="page-header">
      <h1> ZTree  <small> <i class="ace-icon fa fa-angle-double-right"></i> Demo</small> </h1>
    </div>
    <!-- /.page-header -->
<div class="row">
  <div class="col-xs-12">
    <!-- PAGE CONTENT BEGINS -->
    <!-- #section:plugins/fuelux.treeview -->
    <div class="row">
      <div class="col-sm-6">
        <div class="widget-box widget-color-green2">
          <div class="widget-header">
            <h4 class="widget-title lighter smaller">树状结构</h4>
          </div>
          <div class="widget-body">
            <div class="widget-main padding-8">
              <div class="zTreeDemoBackground left">
                <ul id="treeDemo" class="ztree">
                </ul>
              </div>
            </div>
          </div>
        </div>
      </div>
      <div class="col-sm-6">
        <div class="widget-box widget-color-blue2">
          <div class="widget-header">
            <h4 class="widget-title lighter smaller">操作选项</h4>
          </div>
          <div class="widget-body">
            <div class="widget-main padding-8">
              <div class="widget-body">
                <div class="widget-main">
                  <div class="row">
                    <ul class="list-unstyled spaced2">
                      <li> <i class="ace-icon fa fa-bell-o bigger-110 purple"></i> 树结构，有一个默认的根节点。创建的树状结构，从此根节点开始。增加节点，只能从父节点开始。 </li>
                      <li> <i class="ace-icon fa fa-check bigger-110 green"></i> 树结构编辑，编辑树的结构</li>
                      <li> <i class="ace-icon fa fa-times bigger-110 red"></i> 如果创建了父节点，想要变成叶节点，删除重新创建即可。 </li>
                    </ul>
                    <hr />
                    <h4 class="header smaller lighter info"> <i class="ace-icon fa fa-sitemap"></i> 树结构编辑 </h4>
                    <p> <a id="addLeaf" href="###" title="增加叶节点" onClick="return false;">
                      <button class="btn btn-white btn-success btn-round"> <i class="ace-icon fa fa-leaf bigger-120 green"></i> 增加叶节点 </button>
                      </a> <a id="edit" href="###" title="编辑名称" onClick="return false;">
                      <button class="btn btn-white btn-success btn-round"> <i class="ace-icon fa fa-pencil-square-o bigger-120"></i> 编辑名称 </button>
                      </a> <a id="remove" href="###" title="删除节点" onClick="return false;">
                      <button class="btn btn-white btn-inverse btn-round"> <i class="ace-icon fa fa-trash-o fa-lg bigger-120 red2"></i> 删除节点 </button>
                      </a> <a id="clearChildren" href="#" title="清空子节点" onClick="return false;">
                      <button class="btn btn-white btn-inverse btn-round"> <i class="ace-icon fa fa-ban bigger-120 red2"></i> 清空子节点 </button>
                      </a> </p>
                    <p> <a id="copy" href="###" title="复制" onClick="return false;">
                      <button class="btn btn-white btn-default btn-round"> <i class="ace-icon fa fa-files-o bigger-110"></i> 复制 </button>
                      </a> <a id="cut" href="###" title="剪切" onClick="return false;">
                      <button class="btn btn-white btn-default btn-round"> <i class="ace-icon fa fa-scissors bigger-110 "></i> 剪切 </button>
                      </a> <a id="paste" href="###" title="粘贴" onClick="return false;">
                      <button class="btn btn-white btn-default btn-round"> <i class="ace-icon fa fa-clipboard bigger-110"></i> 粘贴 </button>
                      </a> </p>
                    <hr />
                    <h4 class="header smaller lighter blue"> <i class="ace-icon fa fa-share-alt"></i> 导航菜单编辑 </h4>
                    <p>
                      <button id="menu-css" class="btn btn-white btn-default btn-round"> <i class="ace-icon fa fa-info bigger-110"></i> 菜单 CSS </button>
                      <button id="menu-url" class="btn btn-white btn-default btn-round"> <i class="ace-icon fa fa-link bigger-110 "></i> 菜单 URL </button>                    
                    </p>
					<hr />
                    <h4 class="header smaller lighter green"> <i class="ace-icon fa fa-object-group"></i> 节点关联对象 </h4>
                    <p>
                      <button id="standard-link" class="btn btn-white btn-default btn-round"> <i class="ace-icon fa fa-files-o bigger-110"></i> 节点关联对象 </button>
                                     
                    </p>
                    <hr />
                    <h4 class="header smaller lighter red"> <i class="ace-icon fa fa-bullhorn"></i> 控制台 </h4>
                    <div class="smaller-90 alert alert-block alert-success">
                      <ul id="log" class="list-unstyled">
                      </ul>
                    </div>
                  </div>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
    <!-- /section:plugins/fuelux.treeview
		<script type="text/javascript">
			var $assets = "${ctx}/ace/assets";//this will be used in fuelux.tree-sampledata.js
		</script>
-->
    <!-- PAGE CONTENT ENDS -->
  </div>
  <!-- /.col -->
</div>
  </div>
  <!-- /.main-content -->
  <div class="footer">
    <div class="footer-inner">
      <!-- #section:basics/footer -->
      <div class="footer-content"> <span class="bigger-120"> <span class="blue bolder">Ace</span> Application &copy; 2013-2014 </span> &nbsp; &nbsp; <span class="action-buttons"> <a href="#"> <i class="ace-icon fa fa-twitter-square light-blue bigger-150"></i> </a> <a href="#"> <i class="ace-icon fa fa-facebook-square text-primary bigger-150"></i> </a> <a href="#"> <i class="ace-icon fa fa-rss-square orange bigger-150"></i> </a> </span> </div>
      <!-- /section:basics/footer -->
    </div>
  </div>
</div>
<!-- /.main-container -->
<!-- jquery ztree 也支持 jquery 2.0 以上-->
<script type="text/javascript" src="${ctx}/ace/assets/js/jquery.js"></script>
<script type="text/javascript" src="${ctx}/ace/assets/js/bootstrap.js"></script>
<script type="text/javascript" src="${ctx}/h819/js/utils.js"></script>
<!-- custom-->
<script type="text/javascript" src="${ctx}/jquery-confirm/jquery-confirm.js"></script>
<!-- 下面为 zTree 官方配置 -->
<script type="text/javascript" src="${ctx}/zTree/js/jquery.ztree.core-3.5.js"></script>
<script type="text/javascript" src="${ctx}/zTree/js/jquery.ztree.excheck-3.5.js"></script>
<script type="text/javascript" src="${ctx}/zTree/js/jquery.ztree.exedit-3.5.js"></script>
<script type="text/javascript">
		jQuery(function ($) {

			var setting = {
				view : {
					selectedMulti : false
				},
				edit : {
					drag : {
						autoExpandTrigger : true, //拖拽时父节点自动展开是否触发 onExpand 事件回调函数
						isCopy : false, // 拖拽拷贝
						isMove : true, //拖拽剪切
						inner : false //成为目标节点的子节点
					},
					enable : true,
					showRemoveBtn : false,
					showRenameBtn : false
				},
				data : {
					keep : {
						parent : false, //父节点 允许添加子节点
						leaf : false //子节点 允许添加子节点
					},
					simpleData : {
						enable : false
					}
				},
				callback : {

					//beforeClick : beforeClick,
					//rename
					beforeRename : beforeRename, // rename 前验证
					onRename : zTreeOnRename, // rename 后处理
					//drag
					beforeDrag : beforeDrag,
					//beforeDrop: beforeDrop,
					//beforeDragOpen: beforeDragOpen,
					//onDrag: onDrag,
					onDrop : onDrop,
					onExpand : onExpand

				},

				async : {
					enable : true, //开启异步加载模式.如果设置为 true，请务必设置 setting.async 内的其它参数。
					url : "${ctx}/tree/ztree/ajax/async.html", //Ajax 获取数据的 URL 地址。第一次加载页面(此时后台确定第一次加载页面需要展示到树的第几级)和点击关闭的父节点时激发此 url。
					autoParam : ["id"], //异步加载时需要自动提交父节点属性的参数 。这些参数应该是：当点击关闭的父节点时，获取的该父节点的数据中存在的参数，他们和 url 一同传递到后台的参数，用于区分点击了哪个关闭的父节点。
					otherParam : {
						"menu_type" : "${menu_type}"
					} //这个是我们可以自定义的参数。第一次加载树，决定树类型
					// dataType: "text",//默认text
					// type:"get",//默认post
				}
			};

			//放置 ztree 的元素 id，参见上文。
			var ztree_root = "treeDemo";

			/*================================ tools begin ================================================*/

			/*
			打印操作过程
			 */
			function printLog(str, type) {
				if (!log)
					log = $("#log");
				log.append("<li> <i class='" + type + "'></i>" + "[ " + DateUtils.getNow() + " ] &nbsp; " + str + "</li>");

				if (log.children("li").length > 10) { // 值保留10 行数据，防止页面变长
					log.get(0).removeChild(log.children("li")[0]);
				}
			}

			/*
			弹出警示框
			jquery-confirm
			 */
			function showAlart(alart_message) {
				$.alert({
					title : '',
					icon : 'fa fa-warning red2',
					content : alart_message,
					animation : 'zoom',
					confirmButton : "确定",
					confirmButtonClass : "btn btn-primary btn-round",
					confirm : function () {}
				});
			}

			/*
			弹出警示框
			jquery-confirm
			处理代码应该写在 confirm 函数体里面，不能包装成方法
			//浏览器会顺序执行，而不等待 confirm 执行完成
			应该是 confirm 方法异步执行造成的

			$.confirm({
			title: '',
			icon: 'fa fa-warning red2',
			content: confirm_message,
			animation: 'zoom',
			confirmButton: "确定",
			cancelButton:"取消",
			confirmButtonClass:"btn btn-primary btn-round",
			cancelButtonClass: 'btn-danger',
			confirm: function(){
			//
			},

			cancel: function(){
			//
			}

			});


			/*
			调用浏览器调试日志, 打印字符串
			message 可以是任何对象。
			需要注意的是:
			如果 message 为 object（如 josn），那么不要使用 logger("obejct is ="+message)，字符串+对象，log 就无法打印了。
			如果 message 字符串，那么可以 + 字符串如： logger("hello"+"world")
			 */
			function logger(message) {
				console.log(message);
			}

			/*
			class = btn 按钮点击之后，鼠标离开，释放焦点。
			 */
			$(".btn").mouseup(function () {
				$(this).blur();
			})

			/*
			当第一次点击复制或者粘帖按钮之后，被选中的节点会自动加上 a.copy or a.cut 样式(本页中面定义)。
			但如果不想复制或者粘帖，再接着选择其他的节点，第一次点击选择的节点的样式仍然存在，影响页面效果，这里去掉。
			下文的 fontCss() 方法也能完成同样的功能，就用这个吧，不研究了。
			 */
			function removeCopyCutClass() {
				$("#treeDemo li").parent().find('li').find('a').removeClass("copy").removeClass("cut");
			}

			/**
			获取选取的唯一节点(如果多个节点，不用此方法)，如果未选取，则弹出警告。
			 */
			function getZTree(root) {
				return $.fn.zTree.getZTreeObj(root);
			}

			/**
			获取选取的唯一节点(如果多个节点，不用此方法)
			返回 treeNode 类型,详见 api
			 */
			function getZTreeSingleNode(root) {
				var zTree = $.fn.zTree.getZTreeObj(root);
				var nodes = zTree.getSelectedNodes();
				return nodes[0];
			}

			/*================================ tools end ================================================*/

			/*================================ contant para ================================================*/

			var log,
			className = "dark",
			curDragNodes,
			autoExpandNode;
			var logIocn_del = "ace-icon fa fa-trash-o fa-lg bigger-110 red";
			var logIocn_clear = "ace-icon fa fa-trash-o fa-lg bigger-110 red";
			var logIocn_add_parent = "ace-icon fa fa-folder bigger-110";
			var logIocn_add_leaf = "ace-icon fa fa-leaf bigger-110";
			var logIocn_edit = "ace-icon fa fa-pencil-square-o bigger-120";
			var logIocn_copy = "ace-icon fa fa-files-o bigger-110 green";
			var logIocn_cut = "ace-icon fa fa-scissors bigger-110 green";
			var logIocn_move = "ace-icon fa fa-exchange bigger-110";
			var logIocn_warning = "ace-icon fa fa-exclamation-triangle bigger-110 red";

			/*================================ contant para ================================================*/

			/*
			var parent = treeNode[0].getParentNode();
			zTree.reAsyncChildNodes(parent, "refresh",true);
			设为异步加载模式时，可以异步刷新父节点。
			对于刚刚添加的节点，如果不刷新，接着对该节点操作，那么这个节点不是从数据库加载的数据，而是 ztree 生成的临时数据，这时候，一定要刷新

			 */

			/*
			addParent,addLeaf,edit,del,clear,copy,cut,paste 对应上面的几个按钮的 id，点击按钮之后，激发下面几个方法。
			 */

			/*================================ add begin ================================================*/

			// 添加新节点的计数器
			var addCount = 1;
			/*
			add 增加节点
			 */
			function add(e) {
				removeCopyCutClass(); // 如果点击了 copy or cut ，去掉点击后为该节点产生的样式
				var zTree = getZTree(ztree_root);
				var isParent = e.data.isParent;
				var treeNode = getZTreeSingleNode(ztree_root); //每次只能选择一个节点选择的节点，如果没有选中一个节点 , nodes = null

				if (treeNode == null) {
					showAlart("请先选择一个节点"); // add 操作，只能先选中一个父节点，所有的 add ，都是给该父节点增加父节点或者叶节点。
					return;
				} else {

					treeNode = zTree.addNodes(treeNode, { // 增加节点，返回的是增加的节点的数组
							id : (100 + addCount),
							pId : treeNode.id,
							isParent : isParent,
							name : "new node" + (addCount++)
						});

				}
				logger(treeNode[0]);
				/**/
				$.ajax({ //ajax 提交到controller的delApplication方法处理
					type : "post",
					async : false,
					url : "${ctx}/tree/ztree/ajax/add.html",
					data : { //传递的参数和值
						menu_type : "${menu_type}",
						name : treeNode[0].name, //
						index : treeNode[0].getIndex(),
						level : treeNode[0].level,
						pId : treeNode[0].getParentNode().id,
						isParent : treeNode[0].isParent //显示为文件夹还是叶节点
					},
					dataType : "html", //dataType指定返回值的类型，必须与后台的返回值一致。否则无法进入success回掉
					success : function (data) { //处理成功的回调函数

						if (isParent)
							printLog("添加了叶节点 :  " + treeNode[0].name, logIocn_add_parent);
						else
							printLog("添加了叶节点 :  " + treeNode[0].name, logIocn_add_leaf);

						//刷新添加节点后的父节点，展示最新的结构
						var parent = treeNode[0].getParentNode();
						zTree.reAsyncChildNodes(parent, "refresh", true);
					},
					error : function () {

						printLog(treeNode.name + "  &nbsp;&nbsp;&nbsp; ---  &nbsp;&nbsp;&nbsp;添加失败 ", logIocn_warning);

					}
					//处理失败的回到函数
				});

			};
			/*================================ add end ================================================*/

			/*================================ edit begin ================================================*/

			var renameBefore; //编辑前的名字
			var renameAfter; //编辑后的名字

			/*
			 *   点击编辑按钮，使节点变为可编辑状态
			 */
			function edit() {
				removeCopyCutClass(); // 如果点击了 copy or cut ，去掉点击后为该节点产生的样式
				var zTree = getZTree(ztree_root);
				var treeNode = getZTreeSingleNode(ztree_root);
				if (treeNode == null) {
					showAlart("请先选择一个节点");
					return;
				}
				renameBefore = treeNode.name;
				zTree.editName(treeNode); // 把选中的节点变为编辑状态
			};

			/*
			 *   回调函数：重命名前进行验证
			 */

			function beforeRename(treeId, treeNode, newName) {
				if (newName.length == 0) {
					showAlart("节点名称不能为空.");
					return false;
				}

				return true;
			}

			/*
			 *   回调函数：重命名后进行处理
			 */

			function zTreeOnRename(event, treeId, treeNode, isCancel) {
				renameAfter = treeNode.name;
				if (renameBefore == renameAfter) // 没有修改
					return;

				//把检查文件名放在此方法中
				var zTree = getZTree(ztree_root);
				logger(treeNode);
				/**/
				$.ajax({ //ajax 提交到controller的delApplication方法处理
					type : "post",
					async : false,
					url : "${ctx}/tree/ztree/ajax/edit.html",
					data : { //传递的参数和值
						id : treeNode.id,
						name : treeNode.name
					},
					dataType : "html", //dataType指定返回值的类型，必须与后台的返回值一致。否则无法进入success回掉
					success : function (data) { //处理成功的回调函数
						//var parent = treeNode.getParentNode();
						//zTree.reAsyncChildNodes(parent, "refresh"); // 关闭刷新，需要时打开
						printLog(treeNode.name + "  &nbsp;&nbsp;&nbsp; ---  &nbsp;&nbsp;&nbsp;重命名完成 ", logIocn_edit);
					},
					error : function () {

						printLog(treeNode.name + "  &nbsp;&nbsp;&nbsp; ---  &nbsp;&nbsp;&nbsp;重命名失败 ", logIocn_warning);

					}
				});

			}

			/*================================ edit end ================================================*/

			/*================================ remove begin ================================================*/

			/*
			 *   删除节点
			 */
			function remove(e) {
				removeCopyCutClass(); // 如果点击了 copy or cut ，去掉点击后为该节点产生的样式
				var zTree = getZTree(ztree_root);
				var treeNode = getZTreeSingleNode(ztree_root);
				if (treeNode == null) {
					removeCopyCutClass(); // 如果点击了 copy or cut ，去掉点击后为该节点产生的样式
					showAlart("请先选择一个节点");
					return;
				}
				
				if (treeNode.name.indexOf("root_") > -1) {
					removeCopyCutClass(); // 如果点击了 copy or cut ，去掉点击后为该节点产生的样式
					showAlart("根节点不能删除");
					return;
				}

				if (treeNode.isParent && treeNode.children != null && treeNode.children.length != 0) {
					showAlart("请先清空子节点。"); // 删除操作，为了防止误删除，不允许直接删除带有子节点的父节点，给操作者一个机会。
					return;
				}

				var confirm_message = "删除节点 :  " + treeNode.name;
				// confirm
				$.confirm({
					title : '',
					icon : 'fa fa-warning red2',
					content : confirm_message,
					animation : 'zoom',
					confirmButton : "确定",
					cancelButton : "取消",
					confirmButtonClass : "btn btn-primary btn-round",
					cancelButtonClass : 'btn-danger',
					confirm : function () {
						/**/
						$.ajax({ //ajax 提交到controller的delApplication方法处理
							type : "post",
							async : false,
							url : "${ctx}/tree/ztree/ajax/del.html",
							data : { //传递的参数和值
								id : treeNode.id
							},
							dataType : "html", //dataType指定返回值的类型，必须与后台的返回值一致。否则无法进入success回掉
							success : function (data) { //处理成功的回调函数
								zTree.removeNode(treeNode);
								printLog(confirm_message, logIocn_del);
								//	var parent = treeNode.getParentNode();
								//zTree.reAsyncChildNodes(parent, "refresh"); // 关闭刷新，需要时打开
								logger(data);
							},
							error : function () {
								printLog(treeNode.name + "  &nbsp;&nbsp;&nbsp; ---  &nbsp;&nbsp;&nbsp;删除失败 ", logIocn_warning);
							}
							//处理失败的回到函数
						});

					},

					cancel : function () {}

				});
			};

			/*================================ remove end ================================================*/

			/*================================ clearChildren begin ================================================*/

			/*
			 *   清空节点
			 */
			function clearChildren(e) {
				removeCopyCutClass(); // 如果点击了 copy or cut ，去掉点击后为该节点产生的样式
				var zTree = getZTree(ztree_root);
				var treeNode = getZTreeSingleNode(ztree_root);
				if (treeNode == null || !treeNode.isParent) {
					removeCopyCutClass(); // 如果点击了 copy or cut ，去掉点击后为该节点产生的样式
					showAlart("请先选择一个父节点");
					return;
				}

				var confirm_message = "清空所有子节点 :  " + treeNode.name;

				// confirm
				$.confirm({
					title : '',
					icon : 'fa fa-warning red2',
					content : confirm_message,
					animation : 'zoom',
					confirmButton : "确定",
					cancelButton : "取消",
					confirmButtonClass : "btn btn-primary btn-round",
					cancelButtonClass : 'btn-danger',
					confirm : function () {
						/**/
						$.ajax({ //ajax 提交到controller的delApplication方法处理
							type : "post",
							async : false,
							url : "${ctx}/tree/ztree/ajax/clear.html",
							data : { //传递的参数和值
								id : treeNode.id
							},
							dataType : "html", //dataType指定返回值的类型，必须与后台的返回值一致。否则无法进入success回掉
							success : function (data) { //处理成功的回调函数
								printLog(confirm_message, logIocn_del);
								zTree.removeChildNodes(treeNode);
								logger(data);
							},
							error : function () {
								printLog(treeNode.name + "  &nbsp;&nbsp;&nbsp; ---  &nbsp;&nbsp;&nbsp;删除失败 ", logIocn_warning);
							}

						});

					},

					cancel : function () {}
				});

			};

			/*================================ clearChildren end ================================================*/

			/*================================ copy,cut,past begin================================================*/

			var curSrcNode,
			curType;

			/*
			复制剪切操作，添加 css
			 */
			function fontCss(treeNode) {
				var aObj = $("#" + treeNode.tId + "_a");
				aObj.removeClass("copy").removeClass("cut");
				if (treeNode === curSrcNode) {
					if (curType == "copy") {
						aObj.addClass(curType);
					} else {
						aObj.addClass(curType);
					}
				}
			}

			function beforeClick(treeId, treeNode) {
				// console.info(treeNode);
				return true;
			}

			function setCurSrcNode(treeNode) {
				var zTree = $.fn.zTree.getZTreeObj("treeDemo");
				if (curSrcNode) {
					delete curSrcNode.isCur;
					var tmpNode = curSrcNode;
					curSrcNode = null;
					fontCss(tmpNode);
				}
				curSrcNode = treeNode;
				if (!treeNode)
					return;

				curSrcNode.isCur = true;
				zTree.cancelSelectedNode();
				fontCss(curSrcNode);
			}

			/*
			点击 copy
			 */
			function copy(e) {
				var zTree = getZTree(ztree_root);
				var treeNode = getZTreeSingleNode(ztree_root);
				if (treeNode == null) {
					removeCopyCutClass(); // 如果点击了 copy or cut ，去掉点击后为该节点产生的样式
					showAlart("请先选择一个节点");
					return;
				}
				curType = "copy";
				setCurSrcNode(treeNode);
			}
			/*
			点击 cut
			 */
			function cut(e) {
				var zTree = getZTree(ztree_root);
				var treeNode = getZTreeSingleNode(ztree_root);
				if (treeNode == null) {
					removeCopyCutClass(); // 如果点击了 copy or cut ，去掉点击后为该节点产生的样式
					showAlart("请先选择一个节点");
					return;
				}
				curType = "cut";
				setCurSrcNode(treeNode);
			}
			/*
			点击 paste
			 */
			function paste(e) {
				if (!curSrcNode) {
					showAlart("请先选择一个节点进行 复制 / 剪切");
					return;
				}

				var zTree = getZTree(ztree_root);
				var targetNode = getZTreeSingleNode(ztree_root);

				if (curSrcNode === targetNode) {
					showAlart("不能移动，源节点 与 目标节点相同");
					return;
				} else if (curType === "cut" && ((!!targetNode && curSrcNode.parentTId === targetNode.tId) || (!targetNode && !curSrcNode.parentTId))) {
					showAlart("不能移动，源节点 已经存在于 目标节点中");
					return;
				} else if (curType === "copy") {
					targetNode = zTree.copyNode(targetNode, curSrcNode, "inner");
				} else if (curType === "cut") {
					targetNode = zTree.moveNode(targetNode, curSrcNode, "inner");
					if (!targetNode) {
						showAlart("剪切失败，源节点是目标节点的父节点");
					}
					targetNode = curSrcNode;
				}
				setCurSrcNode();
				delete targetNode.isCur;
				zTree.selectNode(targetNode);

				logger(targetNode);
				logger(targetNode.getParentNode().id);
				/**/
				$.ajax({ //ajax 提交到controller的delApplication方法处理
					type : "post",
					async : false,
					url : "${ctx}/tree/ztree/ajax/paste.html",
					data : { //传递的参数和值
						id : targetNode.id, //
						//index : targetNode.getIndex(), //无论是那种类型的粘帖，ztree 默认添加到子节点的尾部
						pId : targetNode.getParentNode().id,
						curType : curType
					},
					dataType : "html", //dataType指定返回值的类型，必须与后台的返回值一致。否则无法进入success回掉
					success : function (data) { //处理成功的回调函数
						//添加了子节点之后，刷新其父节点，会从数据库重新读取其父节，这样可以显示添加到数据库的真实子节点。启用了 async 才可以执行
						zTree.reAsyncChildNodes(targetNode.getParentNode(), "refresh", true);
						// 此处进行复制，剪切后的 处理
						if (curType === "copy")
							printLog(targetNode.name + "  &nbsp;&nbsp;&nbsp; ---  &nbsp;&nbsp;&nbsp;复制完成 ", logIocn_copy);

						if (curType === "cut")
							printLog(targetNode.name + "  &nbsp;&nbsp;&nbsp; ---  &nbsp;&nbsp;&nbsp;剪切完成 ", logIocn_cut);

					},
					error : function () {
						printLog(treeNode.name + "  &nbsp;&nbsp;&nbsp; ---  &nbsp;&nbsp;&nbsp;粘帖失败 ", logIocn_warning);

					}
				});

			}

			/*================================ copy,cut,past end ================================================*/

			/*================================ drop begin ================================================*/
			/*================================ 拖拽仅实现位移，不实现复制功能，复制功能用专门的方法 ================================================*/

			var log,
			curDragNodes,
			autoExpandNode;

			/*
			beforeDrag 会修改当前被 drag 的节点，所以此函数必需启用
			 */

			function beforeDrag(treeId, treeNodes) {
				//className = (className === "dark" ? "" : "dark");
				//printLog(" beforeDrag ]&nbsp;&nbsp;&nbsp;&nbsp; drag: " + treeNodes.length + " nodes."); // 不打印中间过程，仅为调试时使用
				removeCopyCutClass(); // 如果点击了 copy or cut ，去掉点击后为该节点产生的样式
				for (var i = 0, l = treeNodes.length; i < l; i++) {
					if (treeNodes[i].drag === false) {
						curDragNodes = null;
						return false;
					} else if (treeNodes[i].parentTId && treeNodes[i].getParentNode().childDrag === false) {
						curDragNodes = null;
						return false;
					}
				}
				curDragNodes = treeNodes;
				return true;
			}

			/*
			treeNodes: 被拖拽的节点 JSON 数据集合
			targetNode: 成为 treeNodes 拖拽结束的目标节点 JSON 数据对象
			 */

			function onDrop(event, treeId, treeNodes, targetNode, moveType, isCopy) {

				var zTree = getZTree(ztree_root);
				var treeNode = getZTreeSingleNode(ztree_root);

				//logger("treeId:"+treeId);
				logger("moveType:" + moveType);
				logger("isCopy:" + isCopy);
				logger("被拖拽的节点，拖拽完成后的状态 : name=" + treeNode.name + ",index=" + treeNode.getIndex());
				//logger("被拖拽节点，参照的节点是 : name="+targetNode.name +",index="+targetNode.getIndex());
				logger("被拖拽的节点，他的现父节点状态是: id=" + targetNode.getParentNode().id + " , name=" + targetNode.getParentNode().name); // 通过 targetNode 获取
				/*
				更新操作，只更新被拖拽的节点现在的父节点的所有子节点即可（排序）
				操作之前的节点，子节点少了一个，其他节点没有排序，所以不必理会，只是节点排序不连续而已
				 */

				$.ajax({
					type : "post",
					async : false,
					url : "${ctx}/tree/ztree/ajax/move.html",
					data : { //传递的参数和值
						id : treeNode.id, //
						index : treeNode.getIndex(),
						pId : treeNode.getParentNode().id
					},
					dataType : "html", //dataType指定返回值的类型，必须与后台的返回值一致。否则无法进入success回掉
					success : function (data) { //处理成功的回调函数
						//添加了子节点之后，刷新其父节点，会从数据库重新读取其父节，这样可以显示添加到数据库的真实子节点。启用了 async 才可以执行
						// zTree.reAsyncChildNodes(targetNode.getParentNode(), "refresh");
						printLog(targetNode.name + "  &nbsp;&nbsp;&nbsp; ---  &nbsp;&nbsp;&nbsp;移动完成 ", logIocn_move);
					},
					error : function () {
						printLog(treeNode.name + "  &nbsp;&nbsp;&nbsp; ---  &nbsp;&nbsp;&nbsp;移动失败 ", logIocn_warning);
					}
				});

			}

			function onExpand(event, treeId, treeNode) {
				if (treeNode === autoExpandNode) {
					className = (className === "dark" ? "" : "dark");
					showLog("[ " + getTime() + " onExpand ]&nbsp;&nbsp;&nbsp;&nbsp;" + treeNode.name);
				}
			}

			/*================================ drop end ================================================*/
			
			
			
			
			/*================================ 节点编辑、关联对象 begin================================================*/

			//修改 CSS
			$("#menu-css").on('click', function () {
				var treeNode = getZTreeSingleNode(ztree_root);
				if (treeNode == null) {
					showAlart("请先选择一个节点");
					return;
				}
				//logger(treeNode);
				$.confirm({
					title : '菜单 CSS 设置',
					icon : 'fa fa-warning red2',
					content : "url:${app_path}/h819/ztree/menu_css.txt",
					confirmButton : "确定",
					cancelButton : "取消",
					confirmButtonClass : "btn btn-primary btn-round",
					cancelButtonClass : 'btn-danger',
					confirm : function () {
						var input_css = this.$b.find('input#input-css');
						var errorText_css = this.$b.find('p#css-error');
						var errorText_css2 = this.$b.find('p#css-error2');
						//logger(treeNode);
						//logger(treeNode.getParentNode());
						//logger(treeNode.getParentNode().name);
						//logger(treeNode.getParentNode().name.indexOf("root_"));

						//treeNode.getParentNode() ==null 选择了根节点
						// treeNode.getParentNode().name.indexOf("root_") < 0 选择了非一级节点
						if (treeNode.getParentNode() == null || treeNode.getParentNode().name.indexOf("root_") < 0) {
							errorText_css.show();
							return false;
						}

						errorText_css.hide();

						if (input_css.val() == '') {
							errorText_css2.show();
							return false;
						} else {

							$.ajax({
								type : "post",
								async : false,
								url : "${ctx}/tree/ztree/ajax/node/edit/css.html",
								data : { //传递的参数和值
									id : treeNode.id, //
									css : input_css.val()
								},
								dataType : "html", //dataType指定返回值的类型，必须与后台的返回值一致。否则无法进入success回掉
								success : function (data) { //处理成功的回调函数
									//添加了子节点之后，刷新其父节点，会从数据库重新读取其父节，这样可以显示添加到数据库的真实子节点。启用了 async 才可以执行
									// zTree.reAsyncChildNodes(targetNode.getParentNode(), "refresh");
									printLog(treeNode.name + "  &nbsp;&nbsp;&nbsp; ---  &nbsp;&nbsp;&nbsp;CSS 已经修改为&nbsp;&nbsp;&nbsp;  <code> " + input_css.val() + " </code>", logIocn_move);
								},
								error : function () {
									printLog(treeNode.name + "  &nbsp;&nbsp;&nbsp; ---  &nbsp;&nbsp;&nbsp;CSS 修改失败 ", logIocn_warning);
								}
							});

						}

					}
				});
			});

			//修改 URL
			$("#menu-url").on('click', function () {
				var treeNode = getZTreeSingleNode(ztree_root);
				if (treeNode == null) {
					showAlart("请先选择一个节点");
					return;
				}
				//logger(treeNode);
				$.confirm({
					title : '菜单 URL 设置',
					icon : 'fa fa-warning red2',
					content : "url:${app_path}/h819/ztree/menu_url.txt",
					confirmButton : "确定",
					cancelButton : "取消",
					confirmButtonClass : "btn btn-primary btn-round",
					cancelButtonClass : 'btn-danger',
					confirm : function () {
						var input_url = this.$b.find('input#input-url');
						var errorText_url1 = this.$b.find('p#url-error1');
						var errorText_url2 = this.$b.find('p#url-error2');
						//logger(treeNode);
						//logger(treeNode.getParentNode());
						//logger(treeNode.getParentNode().name);
						//logger(treeNode.getParentNode().name.indexOf("root_"));

						//treeNode.getParentNode() ==null 选择了根节点
						// treeNode.getParentNode().name.indexOf("root_") < 0 选择了非一级节点
						if (treeNode.children != null && treeNode.children.length != 0) {
							errorText_url1.show();
							return false;
						}

						errorText_url1.hide();

						if (input_url.val() == '') {
							errorText_url2.show();
							return false;
						} else {

							$.ajax({
								type : "post",
								async : false,
								url : "${ctx}/tree/ztree/ajax/node/edit/url.html",
								data : { //传递的参数和值
									id : treeNode.id, //
									url : input_url.val()
								},
								dataType : "html", //dataType指定返回值的类型，必须与后台的返回值一致。否则无法进入success回掉
								success : function (data) { //处理成功的回调函数
									//添加了子节点之后，刷新其父节点，会从数据库重新读取其父节，这样可以显示添加到数据库的真实子节点。启用了 async 才可以执行
									// zTree.reAsyncChildNodes(targetNode.getParentNode(), "refresh");
									printLog(treeNode.name + "  &nbsp;&nbsp;&nbsp; ---  &nbsp;&nbsp;&nbsp;URL 已经修改为&nbsp;&nbsp;&nbsp;  <code> " + input_url.val() + " </code>", logIocn_move);
								},
								error : function () {
									printLog(treeNode.name + "  &nbsp;&nbsp;&nbsp; ---  &nbsp;&nbsp;&nbsp;URL 修改失败 ", logIocn_warning);
								}
							});

						}

					}
				});
			});

			/*
			以 standard 为例，对叶节点进行对象关联
			 */
			$("#standard-link").on('click', function () {
				var treeNode = getZTreeSingleNode(ztree_root);
				if (treeNode == null) {
					showAlart("请先选择一个节点");
					return;
				}
				//logger(treeNode);
				$.confirm({
					title : '叶节点关联对象',
					icon : 'fa fa-warning red2',
					content : "url:${app_path}/h819/ztree/standard_link.txt",
					confirmButton : "确定",
					cancelButton : "取消",
					confirmButtonClass : "btn btn-primary btn-round",
					cancelButtonClass : 'btn-danger',
					confirm : function () {
						var input_standard = this.$b.find('input#input-standard');
						var errorText_standard1 = this.$b.find('p#standard-error1');
						var errorText_standard2 = this.$b.find('p#standard-error2');
						//logger(treeNode);
						//logger(treeNode.getParentNode());
						//logger(treeNode.getParentNode().name);
						//logger(treeNode.getParentNode().name.indexOf("root_"));

						//treeNode.getParentNode() ==null 选择了根节点
						// treeNode.getParentNode().name.indexOf("root_") < 0 选择了非一级节点
						if (treeNode.children != null && treeNode.children.length != 0) {
							errorText_standard1.show();
							return false;
						}

						errorText_standard1.hide();

						if (input_standard.val() == '') {
							errorText_standard2.show();
							return false;
						} else {

							$.ajax({
								type : "post",
								async : false,
								url : "${ctx}/tree/ztree/ajax/node/link/standard.html",
								data : { //传递的参数和值
									id : treeNode.id, //
									standard : input_standard.val()
								},
								dataType : "html", //dataType指定返回值的类型，必须与后台的返回值一致。否则无法进入success回掉
								success : function (data) { //处理成功的回调函数
									//添加了子节点之后，刷新其父节点，会从数据库重新读取其父节，这样可以显示添加到数据库的真实子节点。启用了 async 才可以执行
									// zTree.reAsyncChildNodes(targetNode.getParentNode(), "refresh");
									printLog(treeNode.name + "  &nbsp;&nbsp;&nbsp; ---  &nbsp;&nbsp;&nbsp;URL 已经修改为&nbsp;&nbsp;&nbsp;  <code> " + input_standard.val() + " </code>", logIocn_move);
								},
								error : function () {
									printLog(treeNode.name + "  &nbsp;&nbsp;&nbsp; ---  &nbsp;&nbsp;&nbsp;URL 修改失败 ", logIocn_warning);
								}
							});

						}

					}
				});
			});

			/*================================ 节点编辑 end================================================*/
			
	
			

			$(document).ready(function () {
				//$.fn.zTree.init($("#treeDemo"), setting, zNodes);
				$.fn.zTree.init($("#treeDemo"), setting); //异步加载
				$("#addLeaf").bind("click", {
					isParent : false
				}, add);
				$("#edit").bind("click", edit);
				$("#remove").bind("click", remove);
				$("#copy").bind("click", copy);
				$("#cut").bind("click", cut);
				$("#paste").bind("click", paste);

				$("#clearChildren").bind("click", clearChildren);
			});

		});
</script>
</body>
</html>