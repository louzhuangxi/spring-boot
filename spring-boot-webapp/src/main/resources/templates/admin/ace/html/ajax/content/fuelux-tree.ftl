<#--声明变量-->
<#assign ctx = "${context.contextPath}">
<title>Treeview - Ace Admin</title>
<link rel="stylesheet" href="${ctx}/jquery-confirm/jquery-confirm.css"/>
<!-- ajax layout which only needs content area -->
<style type="text/css">
<!--
.fontsize {
	font-size: 13px;
	margin-left: 20px;
    font-family: 'Open Sans';
    font-size: 13px;
    color: #393939;
    line-height: 1.5;
}
-->
</style>
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
            <h4 class="widget-title lighter smaller">操作选项</h4>
          </div>
          <div class="widget-body">
            <div class="widget-main padding-8">
              <div class="widget-body">
                <div class="widget-main">
                  <div class="row">
                    <ul class="list-unstyled spaced2">
                      <li> <i class="ace-icon fa fa-bell-o bigger-110 purple"></i> 选择树节点之后，用户可以查看该节点所关联的标准题录信息。 </li>
                      <li> <i class="ace-icon fa fa-check bigger-110 green"></i> 标准文本默认只有浏览权限。</li>
                      <li> <i class="ace-icon fa fa-times bigger-110 red"></i> 如果想对某个节点下的文本权限进行限定，需要单独授权。 </li>
                    </ul>
                    <hr />
					<!--
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
					-->
                    <h4 class="header smaller lighter blue"> <i class="ace-icon fa fa-share-alt"></i> 对节点进行授权</h4>
                    <p>
					
					<div class="control-group">
					                           <!-- 
												<label class="control-label bolder blue">Checkbox</label>
												-->
												<!-- #section:custom/checkbox -->
												<div class="checkbox">
													<label>
														<input name="form-field-checkbox" type="checkbox" class="ace" />
														<span class="lbl fontsize">题录浏览角色</span>
													</label>
													<label>
														<input name="form-field-checkbox" type="checkbox" class="ace" />
														<span class="lbl fontsize">题录批量下载角色</span>
													</label>
												</div>
												<hr />
												<div class="checkbox">
													<label>
														<input name="form-field-checkbox" type="checkbox" class="ace" />
														<span class="lbl fontsize">查看文本角色</span>
													</label>
													</div>
												<div class="checkbox">
													<label>
														<input name="form-field-checkbox" type="checkbox" class="ace" />
														<span class="lbl fontsize">打印文本角色(包含查看)</span>
													</label>
													</div>
												<div class="checkbox">
													<label>
														<input name="form-field-checkbox" type="checkbox" class="ace" checked="checked" />
														<span class="lbl fontsize">下载文本角色(包含查看、打印)</span>
													</label>
													</div>
												<div class="checkbox">
													<label>
														<input name="form-field-checkbox" type="checkbox" class="ace" />
														<span class="lbl fontsize"> 文本批量下载角色(包含查看、打印、下载)</span>
													</label>
												</div>

												<!-- /section:custom/checkbox -->
											</div>                
                    </p>
					<hr />
                    <p>
                      <button id="submit-link" class="btn btn-white btn-default btn-round"> <i class="ace-icon fa fa-files-o bigger-110"></i> 确 定 </button>
                                     
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

		<!-- /section:plugins/fuelux.treeview -->
		<script type="text/javascript">
			var $assets = "${ctx}/ace/assets";//this will be used in fuelux.tree-sampledata.js
		</script>

		<!-- PAGE CONTENT ENDS -->
	</div><!-- /.col -->
</div><!-- /.row -->

<!-- page specific plugin scripts -->
<script type="text/javascript">
	var scripts = [null, "${ctx}/ace/assets/js/fuelux/fuelux.tree.js", "${ctx}/jquery-confirm/jquery-confirm.js","${ctx}/h819/js/utils.js", null];
	$('.page-content-area').ace_ajax('loadScripts', scripts, function () {
		//inline scripts related to this page
		jQuery(function ($) {
			
		
		/*================================ tools begin ================================================*/

			/*
			打印操作过程
			 */
			
			var log;			
			 
			function printLog(array) {
			   
			   var str=array.toString();
			
				if (!log)
					log = $("#log");
				log.append("<li> <i class='ace-icon fa fa-exclamation-triangle bigger-110 red'></i>" + "[ " + DateUtils.getNow() + " ] &nbsp; " + str + "</li>");

				if (log.children("li").length > 10) { // 值保留10 行数据，防止页面变长
					log.get(0).removeChild(log.children("li")[0]);
				}
			}
			
			/*
			处理数据
			获取 fuel ux tree 的事件中，被选取的节点 array 类型
			*/
			function getFueluxSelected(data) {			
				var items =new Array();
				var subItem;				
				var array=data.selected;
				var i;
				for (i = 0; i < array.length; ++i) {
					array[i].attr['id'];
					subItem = array[i].attr['id'] + ":" + array[i].text; //自定义的 item 构成	
                    items.push(subItem);					
				}
				
				return items;		 				  
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

			/* 样式2
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
			});*/

			/**
			//Use something like this to reload data
			$('#tree1').find("li:not([data-template])").remove();
			$('#tree1').tree('render');
			 */

			 var selectedArray=new Array();
			 
			/***/
			//please refer to docs for more info
			$('#tree1')
			.on('loaded.fu.tree', function (evt,data) {}) // 点击关闭的文件夹后，自动加载其子节点。点击操作执行的事件
			.on('updated.fu.tree', function (evt,data) {})
			.on('selected.fu.tree', function (evt,data) {// 点击可以选择的节点，执行的事件
				//console.log(data);				
				selectedArray = getFueluxSelected(data);			
				printLog(selectedArray);											
				//send output to server
			})
			.on('deselected.fu.tree', function(evt, data) {
			    //console.log(data);				
				selectedArray = getFueluxSelected(data);			
				printLog(selectedArray);
				 
				 })
			.on('opened.fu.tree', function (evt,data) {})
			.on('closed.fu.tree', function (evt,data) {});
			
			$( "#submit-link" ).click(function() {
			    showAlart( selectedArray.toString());
			});
			
		});
		
	});
</script>
