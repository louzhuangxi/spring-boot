<#--声明变量-->
<#assign ctx = "${context.contextPath}">
<title>Treeview - Ace Admin</title>

<!-- ajax layout which only needs content area -->
<div class="page-header">
	<h1>
		Treeview
		<small>
			<i class="ace-icon fa fa-angle-double-right"></i>
			with selectable elements and custom icons
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
						<h4 class="widget-title lighter smaller">Choose Categories</h4>
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
							Browse Files
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

<!-- page specific plugin scripts -->
<script type="text/javascript">
	var scripts = [null, "${ctx}/ace/assets/js/fuelux/fuelux.tree.js", null];
	$('.page-content-area').ace_ajax('loadScripts', scripts, function () {
		//inline scripts related to this page
		jQuery(function ($) {

			/*
			数据动态加载，需要在 ace_tree 配置之前，初始化该数据   http://fuelux-tutorials.herokuapp.com/tree/#custom-attributes
			每次点击展开文件夹图标，都会自动执行获取 dataSource 数据源一次。所以数据源返回被点击节点的子节点即可，不必展开太多。

			options 是被点击的节点的数据
			callback 为点击完成之后，执行的函数
			
			pre select : http://stackoverflow.com/questions/17169156/how-to-preselect-items-within-a-fuel-ux-treeview
			
			 */
			var remoteDateSource = function (options, callback) {
				//console.log(options);
				var parent_id = null;
				if (!('text' in options || 'type' in options)) { // 父节点 options = Object {}
					parent_id = 0; // load first level data
				} else if ('type' in options && options['type'] == 'folder') { // { // 子节点 options =  Object {text: "For Sale", type: "folder", additionalParameters: Object, template: "treebranch"}
					if ('attr' in options) // 有附加属性
						parent_id = options.attr['id'];
				}
				//console.log(parent_id);
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
			ace 对 http://getfuelux.com/javascript.html#tree  做了包装，下面是 fuelux tree 设置
			下面的演示是读取同一个数据源，不同的样式 
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
			.on('loaded.fu.tree', function (e) {}) // 点击关闭的文件夹后，自动加载其子节点。点击操作执行的事件
			.on('updated.fu.tree', function (e, result) {})
			.on('selected.fu.tree', function (e) {// 点击可以选择的节点，执行的事件
				console.log(e);

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
	});
</script>
