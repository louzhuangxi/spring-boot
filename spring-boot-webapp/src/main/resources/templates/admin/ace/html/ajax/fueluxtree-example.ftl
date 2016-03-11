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
	</head>

	<body class="no-skin">
		<!-- #section:basics/navbar.layout -->
		<div id="navbar" class="navbar navbar-default">
			<script type="text/javascript">
				try{ace.settings.check('navbar' , 'fixed')}catch(e){}
			</script>

			<div class="navbar-container" id="navbar-container">
				<!-- #section:basics/sidebar.mobile.toggle -->
				<button type="button" class="navbar-toggle menu-toggler pull-left" id="menu-toggler" data-target="#sidebar">
					<span class="sr-only">Toggle sidebar</span>

					<span class="icon-bar"></span>

					<span class="icon-bar"></span>

					<span class="icon-bar"></span>
				</button>

				<!-- /section:basics/sidebar.mobile.toggle -->
				<div class="navbar-header pull-left">
					<!-- #section:basics/navbar.layout.brand -->
					<a href="#" class="navbar-brand">
						<small>
							<i class="fa fa-leaf"></i>
							Ace Admin
						</small>
					</a>

					<!-- /section:basics/navbar.layout.brand -->

					<!-- #section:basics/navbar.toggle -->

					<!-- /section:basics/navbar.toggle -->
				</div>

				<!-- #section:basics/navbar.dropdown -->
				<!-- /section:basics/navbar.dropdown -->
			</div><!-- /.navbar-container -->
		</div>

		<!-- /section:basics/navbar.layout -->
		<div class="main-container" id="main-container">
			<script type="text/javascript">
				try{ace.settings.check('main-container' , 'fixed')}catch(e){}
			</script>

			<!-- #section:basics/sidebar -->
			<!-- /section:basics/sidebar -->
			<div class="main-content">
				<div class="main-content-inner">
					<!-- #section:basics/content.breadcrumbs -->
					<div class="breadcrumbs" id="breadcrumbs">
						<script type="text/javascript">
							try{ace.settings.check('breadcrumbs' , 'fixed')}catch(e){}
						</script>

						<ul class="breadcrumb">
							<li>
								<i class="ace-icon fa fa-home home-icon"></i>
								<a href="#">Home</a>
							</li>

							<li>
								<a href="#">UI &amp; Elements</a>
							</li>
							<li class="active">Treeview</li>
						</ul><!-- /.breadcrumb -->

						<!-- #section:basics/content.searchbox -->
						<div class="nav-search" id="nav-search">
							<form class="form-search">
								<span class="input-icon">
									<input type="text" placeholder="Search ..." class="nav-search-input" id="nav-search-input" autocomplete="off" />
									<i class="ace-icon fa fa-search nav-search-icon"></i>
								</span>
							</form>
						</div><!-- /.nav-search -->

						<!-- /section:basics/content.searchbox -->
					</div>

					<!-- /section:basics/content.breadcrumbs -->
					<div class="page-content">
						<!-- #section:settings.box -->

						<!-- /section:settings.box -->
						<div class="page-header">
							<h1>
								Fuelux Tree
								<small>
									<i class="ace-icon fa fa-angle-double-right"></i>
									Demo
								</small>
							</h1>
						</div><!-- /.page-header -->

						<div class="row">
							<div class="col-xs-12">
								<!-- PAGE CONTENT BEGINS -->

								<!-- #section:plugins/fuelux.treeview -->
								<div class="row">
									<div class="col-sm-6">
										<div class="widget-box widget-color-green2">
											<div class="widget-header">
												<h4 class="widget-title lighter smaller">Fuelux Tree Demo Style1</h4>
											</div>

											<div class="widget-body">
												<div class="widget-main padding-8">
													<ul id="tree1"></ul>
												</div>
											</div>
										</div>
									</div>

									<div class="col-sm-6">
										<div class="widget-box widget-color-blue2">
											<div class="widget-header">
												<h4 class="widget-title lighter smaller">
													Fuelux Tree Demo Style2
													<span class="smaller-80">(with selectable folders)</span>
												</h4>
											</div>

											<div class="widget-body">
												<div class="widget-main padding-8">
													<ul id="tree2"></ul>
												</div>
											</div>
										</div>
									</div>
								</div>

								<!-- /section:plugins/fuelux.treeview -->
								<script type="text/javascript">
									var $assets = "${ctx}/ace/assets";//this will be used in fuelux.tree-sampledata.js
								</script>

								<!-- PAGE CONTENT ENDS -->
							</div><!-- /.col -->
						</div><!-- /.row -->
					</div><!-- /.page-content -->
				</div>
			</div><!-- /.main-content -->

			<div class="footer">
				<div class="footer-inner">
					<!-- #section:basics/footer -->
					<div class="footer-content">
						<span class="bigger-120">
							<span class="blue bolder">Ace</span>
							Application &copy; 2013-2014
						</span>

						&nbsp; &nbsp;
						<span class="action-buttons">
							<a href="#">
								<i class="ace-icon fa fa-twitter-square light-blue bigger-150"></i>
							</a>

							<a href="#">
								<i class="ace-icon fa fa-facebook-square text-primary bigger-150"></i>
							</a>

							<a href="#">
								<i class="ace-icon fa fa-rss-square orange bigger-150"></i>
							</a>
						</span>
					</div>

					<!-- /section:basics/footer -->
				</div>
			</div>

			<a href="#" id="btn-scroll-up" class="btn-scroll-up btn btn-sm btn-inverse">
				<i class="ace-icon fa fa-angle-double-up icon-only bigger-110"></i>
			</a>
		</div><!-- /.main-container -->

		<!-- basic scripts -->

		<!--[if !IE]> -->
		<script type="text/javascript">
			window.jQuery || document.write("<script src='${ctx}/ace/assets/js/jquery.js'>"+"<"+"/script>");
		</script>

		<!-- <![endif]-->

		<!--[if IE]>
<script type="text/javascript">
 window.jQuery || document.write("<script src='${ctx}/ace/assets/js/jquery1x.js'>"+"<"+"/script>");
</script>
<![endif]-->
		<script type="text/javascript">
			if('ontouchstart' in document.documentElement) document.write("<script src='${ctx}/ace/assets/js/jquery.mobile.custom.js'>"+"<"+"/script>");
		</script>
		<script src="${ctx}/ace/assets/js/bootstrap.js"></script>

		<!-- page specific plugin scripts -->
		<script src="${ctx}/ace/assets/js/fuelux/fuelux.tree.js"></script>

		<!-- ace scripts -->
		<script src="${ctx}/ace/assets/js/ace/elements.scroller.js"></script>
		<script src="${ctx}/ace/assets/js/ace/elements.colorpicker.js"></script>
		<script src="${ctx}/ace/assets/js/ace/elements.fileinput.js"></script>
		<script src="${ctx}/ace/assets/js/ace/elements.typeahead.js"></script>
		<script src="${ctx}/ace/assets/js/ace/elements.wysiwyg.js"></script>
		<script src="${ctx}/ace/assets/js/ace/elements.spinner.js"></script>
		<script src="${ctx}/ace/assets/js/ace/elements.treeview.js"></script>
		<script src="${ctx}/ace/assets/js/ace/elements.wizard.js"></script>
		<script src="${ctx}/ace/assets/js/ace/elements.aside.js"></script>
		<script src="${ctx}/ace/assets/js/ace/ace.js"></script>
		<script src="${ctx}/ace/assets/js/ace/ace.ajax-content.js"></script>
		<script src="${ctx}/ace/assets/js/ace/ace.touch-drag.js"></script>
		<script src="${ctx}/ace/assets/js/ace/ace.sidebar.js"></script>
		<script src="${ctx}/ace/assets/js/ace/ace.sidebar-scroll-1.js"></script>
		<script src="${ctx}/ace/assets/js/ace/ace.submenu-hover.js"></script>
		<script src="${ctx}/ace/assets/js/ace/ace.widget-box.js"></script>
		<script src="${ctx}/ace/assets/js/ace/ace.settings.js"></script>
		<script src="${ctx}/ace/assets/js/ace/ace.settings-rtl.js"></script>
		<script src="${ctx}/ace/assets/js/ace/ace.settings-skin.js"></script>
		<script src="${ctx}/ace/assets/js/ace/ace.widget-on-reload.js"></script>
		<script src="${ctx}/ace/assets/js/ace/ace.searchbox-autocomplete.js"></script>

		<!-- inline scripts related to this page -->
		<script type="text/javascript">
			jQuery(function ($) {

				//
				//每次点击展开文件夹图标，都会自动执行获取 dataSource 数据源一次。所以数据源返回被点击节点的子节点即可，不必展开太多。 
		       //options 是被点击的节点的数据，callback 为点击完成之后，执行的函数
				/*
				数据动态加载，需要在 ace_tree 配置之前，初始化该数据   http://fuelux-tutorials.herokuapp.com/tree/#custom-attributes
				每次点击展开文件夹图标，都会自动执行获取 dataSource 数据源一次。所以数据源返回被点击节点的子节点即可，不必展开太多。
				
				options 是被点击的节点的数据
				callback 为点击完成之后，执行的函数
				*/
				var remoteDateSource = function (options, callback) {
					console.log(options);
					var parent_id = null;
					if (!('text' in options || 'type' in options)) { // 父节点 options = Object {}
						parent_id = 0; // load first level data
					} else if ('type' in options && options['type'] == 'folder') { // { // 子节点 options =  Object {text: "For Sale", type: "folder", additionalParameters: Object, template: "treebranch"}
						if ('attr' in options) // 有附加属性
							parent_id = options.attr['id'];
					}
					console.log(parent_id);
					if (parent_id != null) {
						$.ajax({
							url : "${ctx}/tree/fuelux/ajax/async.html",
							data : {
								pId : parent_id,
								menu_type : 'Menu'
							},
							type : 'POST',
							dataType : 'json',
							success : function (response) {
								console.log(response);
								var childObjectsArray = response;
								if (childObjectsArray != null) //this setTimeout is only for mimicking some random delay(随机 delay)
									setTimeout(function () {
										callback({
											data : childObjectsArray
										});
									}, parseInt(Math.random() * 500) + 200);
							},
							error : function (response) {
								console.log(response);
							}
						});
					};
				};

				/*
				ace 对 http://getfuelux.com/javascript.html#tree  做了包装
				 */
				$('#tree1').ace_tree({
					dataSource : remoteDateSource,
					//select
					'itemSelect' : true,
					'folderSelect' : false,
					multiSelect : true,
					cacheItems : true,
					'selectable' : true, //Whether items are selectable or not
					// iocn
					'open-icon' : 'ace-icon tree-minus',
					'close-icon' : 'ace-icon tree-plus',
					'selected-icon' : 'ace-icon fa fa-check',
					'unselected-icon' : 'ace-icon fa fa-times',
					loadingHTML : '<div class="tree-loading"><i class="ace-icon fa fa-refresh fa-spin blue"></i></div>'
				});

				$('#tree2').ace_tree({
					dataSource : remoteDateSource,
					//select
					'itemSelect' : true,
					'folderSelect' : true,
					'multiSelect' : true,
					'selectable' : true, //Whether items are selectable or not
					// iocn
					'open-icon' : 'ace-icon fa fa-folder-open',
					'close-icon' : 'ace-icon fa fa-folder',
					'folder-open-icon' : 'ace-icon tree-plus',
					'folder-close-icon' : 'ace-icon tree-minus',
					//'selected-icon' : null,
					//'unselected-icon' : null,
					'selected-icon' : 'ace-icon fa fa-check',
					'unselected-icon' : 'ace-icon fa fa-times',
					loadingHTML : '<div class="tree-loading"><i class="ace-icon fa fa-refresh fa-spin blue"></i></div>'
				});

				/**
				//Use something like this to reload data
				$('#tree1').find("li:not([data-template])").remove();
				$('#tree1').tree('render');
				 */

				/***/
				//please refer to docs for more info
				$('#tree1')
				.on('loaded.fu.tree', function (e) {})
				.on('updated.fu.tree', function (e, result) {})
				.on('selected.fu.tree', function (e) {
					//console.log(e);

					//To get the list of user selected items and posting it to server, you can do the following: var items = $('#treeview').tree('selectedItems');
					/*
					var items = $('#treeview').tree('selectedItems');
					var output;
					//for example convert "items" to a custom string
					for (var i in items)
					if (items.hasOwnProperty(i)) {
					var item = items[i];
					output += item.additionalParameters['id'] + ":" + item.text + "\n";
					}*/

					//send output to server
				})
				.on('deselected.fu.tree', function (e) {})
				.on('opened.fu.tree', function (e) {})
				.on('closed.fu.tree', function (e) {});

			});
		</script>

		<!-- the following scripts are used in demo only for onpage help and you don't need them -->
		<link rel="stylesheet" href="${ctx}/ace/assets/css/ace.onpage-help.css" />
		<link rel="stylesheet" href="${ctx}/ace/docs/assets/js/themes/sunburst.css" />

		<script type="text/javascript"> ace.vars['base'] = '..'; </script>
		<script src="${ctx}/ace/assets/js/ace/elements.onpage-help.js"></script>
		<script src="${ctx}/ace/assets/js/ace/ace.onpage-help.js"></script>
		<script src="${ctx}/ace/docs/assets/js/rainbow.js"></script>
		<script src="${ctx}/ace/docs/assets/js/language/generic.js"></script>
		<script src="${ctx}/ace/docs/assets/js/language/html.js"></script>
		<script src="${ctx}/ace/docs/assets/js/language/css.js"></script>
		<script src="${ctx}/ace/docs/assets/js/language/javascript.js"></script>
	</body>
</html>
