<#--声明变量-->
<#assign ctx = "${context.contextPath}">
<#assign treeType = "${treeType}">
<title>角色授权</title>

<link rel="stylesheet" href="${ctx}/ace/assets/css/jquery-ui.css"/>
<link rel="stylesheet" href="${ctx}/ace/assets/css/bootstrap-datepicker3.css"/>
<link rel="stylesheet" href="${ctx}/ace/assets/css/ui.jqgrid.css"/>

<!-- ajax layout which only needs content area -->
<div class="page-header">
    <h1>
        jqGrid
        <small>
            <i class="ace-icon fa fa-angle-double-right"></i>
            Dynamic tables and grids using jqGrid plugin
        </small>
    </h1>
</div><!-- /.page-header -->

<div class="row">
    <div class="col-xs-12">
        <!-- PAGE CONTENT BEGINS -->
        <div class="well well-sm">

        <#if treeType == "Menu">
            定义菜单资源角色
        <#elseif treeType == "Standard">
            定义标准资源角色
        <#elseif treeType == "">
            定义 xx 资源角色
        </#if>

        </div>

        <table id="grid-table"></table>

        <div id="grid-pager"></div>

        <script type="text/javascript">
            var $path_base = "../..";//in Ace demo this will be used for editurl parameter
        </script>

        <!-- PAGE CONTENT ENDS -->
    </div><!-- /.col -->
</div><!-- /.row -->


<div id="modal-table" class="modal fade" tabindex="-1" data-backdrop="static">
    <div class="modal-dialog">
        <form id="informationForm">
            <div class="modal-content">
                <div class="modal-header no-padding">
                    <div class="table-header">
                        <button type="button" class="close" data-dismiss="modal" aria-hidden="true">
                            <span class="white">&times;</span>
                        </button>
                        角色授权
                    </div>
                </div>
                <div class="modal-body" style="max-height: 500px;overflow-y: scroll;">
                    <div id="modal-tip" class="red clearfix"></div>
                    <!--通过表单，在 jqgrid 和 ztree 之间传递参数-->
                    <input id="roleId" type="hidden" value=""/> <!-- roleId 权限 id -->
                    <div class="widget-box widget-color-blue2">
                        <div class="widget-body">
                            <div class="widget-main padding-8">
                                <div class="zTreeDemoBackground left">
                                    <ul id="treeDemo" class="ztree"></ul>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
                <div class="modal-footer no-margin-top">
                    <div class="text-center">      <!--  data-dismiss="modal" 点击后会关闭 modal -->
                        <button id="submitButton" type="submit" class="btn btn-white btn-default btn-round"
                                data-dismiss="modal">
                            <i class="ace-icon fa fa-floppy-o bigger-120"></i>
                            保存
                        </button>
                        <button class="btn btn-white btn-default btn-round" data-dismiss="modal">
                            <i class="ace-icon fa fa-share bigger-120"></i>
                            取消
                        </button>
                    </div>
                </div>
            </div><!-- /.modal-content -->
        </form>
    </div><!-- /.modal-dialog -->
</div>

<!-- page specific plugin scripts -->
<script type="text/javascript">
    var scripts = [null,
        "${ctx}/ace/assets/js/date-time/bootstrap-datepicker.js",
        "${ctx}/ace/assets/js/jqGrid/jquery.jqGrid.js",
        "${ctx}/ace/assets/js/jqGrid/i18n/grid.locale-cn.js",
        "${ctx}/h819/jqgrid/JqgridUtils.js",
        null]


    /**
     spring security ajax csrf
     spring security 中使用 ajax ，必须在使用 ajax 方法的页面中做如下设置:

     1. 页面中配置 (已在 index.ftl 中设置)
     <!-- ajax 请求时用到 default header name is X-CSRF-TOKEN 例子在 login.js 中 -->
     <meta name="_csrf" th:content="${_csrf.token}"/>
     <meta name="_csrf_header" th:content="${_csrf.headerName}"/>
     2. ajax 如定义如下函数即可
     */
    var token = $("meta[name='_csrf']").attr("content");
    var header = $("meta[name='_csrf_header']").attr("content");
    $(document).ajaxSend(function (e, xhr, options) {
        xhr.setRequestHeader(header, token);
    });


    $('.page-content-area').ace_ajax('loadScripts', scripts, function () {

        jQuery(function ($) {
            var grid_selector = "#grid-table";
            var pager_selector = "#grid-pager";


            /**
             点击保存按钮，关联所有树节点到选中的 roleId 上
             */

            $('#submitButton').click(function () {

                /**
                 * ids 设为局部变量，每次点击后都重赋值
                 * */
                var ids = new Array();
                var ids_str = "";

                var zTree = getZTree(ztree_root);
                var nodes = zTree.getCheckedNodes(true); //返回的是一个数组
                //logger(nodes);

                //id 数组
                for (i = 0; i < nodes.length; i++) {
                    ids.push(nodes[i].id);
                }

                //id 数组转换为字符串， 形如 1,2,3,
                for (i = 0; i < ids.length; i++) {
                    ids_str += ids[i] + ",";
                }
                //logger("text="+text);


                $.ajax({ //ajax 提交到controller的delApplication方法处理
                    type: "post",
                    async: false,
                    url: "${ctx}/ajax/grid/role/get_checked_nodes.html",
                    data: { //传递的参数和值
                        ids_str: ids_str,
                        role_id: $("#roleId").val()
                    },
                    dataType: "html", //dataType指定返回值的类型，必须与后台的返回值一致。否则无法进入success回掉
                    success: function (data) { //处理成功的回调函数

                    },
                    error: function () {

                    }
                    //处理失败的回到函数
                });

            });


            var parent_column = $(grid_selector).closest('[class*="col-"]');
            //resize to fit page size
            $(window).on('resize.jqGrid', function () {
                $(grid_selector).jqGrid('setGridWidth', parent_column.width());
            })

            //resize on sidebar collapse/expand
            $(document).on('settings.ace.jqGrid', function (ev, event_name, collapsed) {
                if (event_name === 'sidebar_collapsed' || event_name === 'main_container_fixed') {
                    //setTimeout is for webkit only to give time for DOM changes and then redraw!!!
                    setTimeout(function () {
                        $(grid_selector).jqGrid('setGridWidth', parent_column.width());
                    }, 0);
                }
            })

            //if your grid is inside another element, for example a tab pane, you should use its parent's width:
            /**
             $(window).on('resize.jqGrid', function () {
			var parent_width = $(grid_selector).closest('.tab-pane').width();
			$(grid_selector).jqGrid( 'setGridWidth', parent_width );
		})
             //and also set width when tab pane becomes visible
             $('#myTab a[data-toggle="tab"]').on('shown.bs.tab', function (e) {
		  if($(e.target).attr('href') == '#mygrid') {
			var parent_width = $(grid_selector).closest('.tab-pane').width();
			$(grid_selector).jqGrid( 'setGridWidth', parent_width );
		  }
		})
             */


            /*
            自定义的验证方法 远程
            验证 code
             */

            function checkname(value, name) {

                // alert("value :" +value +" , colname :" +colname);
                var result = null; // checkname 函数的返回值，作为验证结果
                var ids = jQuery(grid_selector).jqGrid('getGridParam', 'selrow'); //编辑时，返回的被选择的条目。只能返回一个，无法选了多个行时进行判断，未找到方法。
                // alert("ids :" +ids);
                $.ajax({
                    async: false, //同步请求，ajax 返回
                    type: "POST",
                    url: "${ctx}/ajax/validate/role/name/exist.html",
                    data: { //传递的参数和值
                        //oper: "unsubscribe",
                        // 在jqgrid 中提交，必须包含 oper 变量，名称 "oper" 不能改变
                        name: value,
                        ids: ids //传递被选中的行 id
                    },
                    dataType: "html",
                    //data 发送到 服务器端的格式 // 不要用 json ，会有 syntaxerror unexpected token < 错误发生，直接跳到 error : 部分
                    //contentType: "application/json; charset=utf-8",
                    success: function (data, textStatus, jqXHR) { //data 为 url 返回的字符串，用于提示不同的验证信息    , 可以为 "true"  或 "false" 或 "其他错误信息"
                        //alert("data: "+data);
                        if (data == "true") //返回字符串"true"，表示验证通过，不显示任何信息
                            result = new Array(true, "");
                        else
                            result = new Array(false, data); //返回其他字符串，把其他字符串作为出错的信息，进行显示. 如果返回字符串为 false ，不显示任何信息

                        // alert("success textStatus: "+textStatus);
                        //alert("success jqXHR: "+jqXHR.status+','+jqXHR.statusText);
                        // JqgridUtils.reloadGrid(grid_selector) ; //ajax 函数执行成功之后，jgrid 刷新当前表格
                    },

                    error: function (jqXHR, textStatus, errorThrown) { //打印出错信息，便于调试
                        alert("error jqXHR: " + jqXHR.status + ',' + jqXHR.statusText);
                        alert("error textStatus: " + textStatus); //"timeout", "error", "abort", and "parsererror" 有四种错误代码，errorThrown 是错误具体信息
                        alert("error errorThrown : " + errorThrown);
                        JqgridUtils.reloadGrid(grid_selector) ;

                    }
                });
                return result;
            };


            jQuery(grid_selector).jqGrid({
                //direction: "rtl",

                //subgrid options
                subGrid: false,
                //subGridModel: [{ name : ['No','Item Name','Qty'], width : [55,200,80] }],
                //datatype: "xml",
                subGridOptions: {
                    plusicon: "ace-icon fa fa-plus center bigger-110 blue",
                    minusicon: "ace-icon fa fa-minus center bigger-110 blue",
                    openicon: "ace-icon fa fa-chevron-right center orange"
                },
                //for this example we are using local data
                subGridRowExpanded: function (subgridDivId, rowId) {
                    var subgridTableId = subgridDivId + "_t";
                    $("#" + subgridDivId).html("<table id='" + subgridTableId + "'></table>");
                    $("#" + subgridTableId).jqGrid({
                        datatype: 'local',
                        data: subgrid_data,
                        colNames: ['No', 'Item Name', 'Qty'],
                        colModel: [
                            {name: 'id', width: 50},
                            {name: 'name', width: 150},
                            {name: 'qty', width: 50}
                        ]
                    });
                },


                //配置,参见 http://www.trirand.com/jqgridwiki/doku.php?id=wiki:options
                url: "${ctx}/ajax/grid/role/jqgrid-search", // 查询提交的 remote 地址，该地址返回要求的展示数据
                editurl: "${ctx}/ajax/grid/role/jqgrid-edit",//nothing is saved

                datatype: "json", // 返回的数据类型
                mtype: "post", // 提交方式
                caption: "最新消息",//表格描述

                height: 250,
                colNames: [' ', 'ID', '角色名称', '资源授权'],
                colModel: [
                    {
                        name: 'myac', index: '', width: 20, fixed: true, sortable: false, resize: false, search: false
                        //	formatter:'actions',
                        //	formatoptions:{
                        //		keys:true,
                        //delbutton: false,//disable delete button

                        //		delOptions:{recreateForm: true, beforeShowForm:beforeDeleteCallback},
                        //		editformbutton:true, editOptions:{recreateForm: true, beforeShowForm:beforeEditCallback}
                        //	}
                    },


                    //	{name:'id',index:'id', width:60, sorttype:"int", editable: true},
                    /* 添加链接
                    {name:'code',index:'code',width:200, formatter:'link',search:true,searchoptions:{sopt:["cn","eq"]},

                        formatter: function(cellvalue, options, rowObject) {
                        //	alert(rowObject.herf);
                           return  "<a href='"+rowObject.herf+"' target='_blank'>"+rowObject.title+"</a>";

                         }
                    },
                    */
                    {
                        name: 'id',
                        index: 'id',
                        width: 50,
                        hidden: true,
                        search: false,
                        sorttype: "int",
                        editable: true  // 必须设为 true ，否则不能正确显示
                    }, // ace admin 1.3.4 ，不知道为什么，不显示 id 行，真正显示从 name 起
                    {
                        name: 'name',
                        index: 'name',
                        width: 100,
                        search: true,
                        searchoptions: {sopt: ["cn", "eq"]},
                        editable: true,
                        edittype: 'text',
                        editrules: {required: true, custom: true, custom_func: checkname} //
                    },
                    {
                        name: '',
                        index: '',
                        label: '授权',
                        width: 100,
                        search: false,
                        editable: false,
                        formatter: authorityFormatterMenu //显示一个图标
                    }
                    /*
                    formatter: 'date', formatoptions: { srcformat: 'Y-m-d H:i:s', newformat: 'Y-m-d'}
                    }*///如果应用在 spring mvc 中，必须在 getter 方法中，设置日期格式  @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+08:00")
                ],

                //显示
                viewrecords: true,//是否在浏览导航栏显示记录总数等信息
                emptyrecords: '无结果',//当返回的数据行数为0时显示的信息。只有当属性 viewrecords 设置为ture时起作用
                loadtext: '正在加载...',//当数据还没加载完或数据格式不正确时显示


                rownumbers: true, // 是否显示前面的行号
                multiselect: true,//记录是否能多选
                //multikey: "ctrlKey",
                multiboxonly: true, //在点击表格row时只支持单选，只有当点击checkbox时才多选，需要multiselect=true是有效

                height: "auto", // 表格宽度，或写数字 height : 250,
                autowidth: true, // 是否自动调整宽度

                //prmNames: {page:"page",rows:"rows", sort: "sidx",order: "sord", search:"_search", nd:"nd", npage:null} //修改发送到服务器端的默认参数名称


                sortable: true,  //可以排序
                sortname: 'name',  //默认排序字段名，点击表头字段排序后，该变量值变为被点击的变量名称
                sortorder: "desc", //默认排序方式：正序，点击表头字段排序后，该变量值变为 desc

                rowNum: 10, //每页默认显示记录数目
                rowList: [10, 20, 30],//供选择的每页显示记录数目
                pager: pager_selector,//选择每页显示记录数目的显示位置，依附于文档 id ，该 id 在哪，就在哪显示
                //  altRows: true,
                //toppager: true,

                loadComplete: function () {
                    var table = this;
                    setTimeout(function () {
                        styleCheckbox(table);

                        updateActionIcons(table);
                        updatePagerIcons(table);
                        enableTooltips(table);
                    }, 0);
                    JqgridUtils.hideX(grid_selector); //强制不显示横向滚动条
                }

                /**
                 ,
                 grouping:true,
                 groupingView : {
				 groupField : ['name'],
				 groupDataSorted : true,
				 plusicon : 'fa fa-chevron-down bigger-110',
				 minusicon : 'fa fa-chevron-up bigger-110'
			},
                 caption: "Grouping"
                 */

            });


            /**
             *点击后，在 Bootstrap Modals 中 加载 menu tree
             *
             * 每次点击授权按钮，都要显示该 role 已经关联的节点
             * 因为每个 role 已经关联的节点不同，所以每次点击授权按钮，代表不同的roleId , 都重新异步加载 ztree 一次
             * refreshNode 必须放在点击执行的 js 函数的最后，以便于 roleId 赋值后获得
             * refreshNode 函数必须放在调用  jqgrid 函数之外，因为是生成的 button onclick 代码，这段代码在 jqgrid 之外，不要在 jqgrid 内部
             *
             */
            function authorityFormatterMenu(cellvalue, options, cell) {
                // alert(cell.id);
                var template = "<button data-toggle='modal' onclick='$(\"#modal-table\").modal(\"toggle\");$(\"#roleId\").val(\"" + cell.id + "\");refreshNode()' class='btn btn-white btn-default btn-round'><i class='ace-icon fa fa-lock bigger-120 red'></i></button>";
                return template;
            }


            $(window).triggerHandler('resize.jqGrid');//trigger window resize to make the grid get the correct size


            //enable search/filter toolbar
            //jQuery(grid_selector).jqGrid('filterToolbar',{defaultSearch:true,stringResult:true})
            //jQuery(grid_selector).filterToolbar({});


            //switch element when editing inline
            function aceSwitch(cellvalue, options, cell) {
                setTimeout(function () {
                    $(cell).find('input[type=checkbox]')
                            .addClass('ace ace-switch ace-switch-5')
                            .after('<span class="lbl"></span>');
                }, 0);
            }

            //enable datepicker
            function pickDate(cellvalue, options, cell) {
                setTimeout(function () {
                    $(cell).find('input[type=text]')
                            .datepicker({format: 'yyyy-mm-dd', autoclose: true});
                }, 0);
            }


            //navButtons
            jQuery(grid_selector).jqGrid('navGrid', pager_selector,
                    { 	//navbar options
                        edit: true,
                        editicon: 'ace-icon fa fa-pencil blue',
                        add: true,
                        addicon: 'ace-icon fa fa-plus-circle purple',
                        del: true,
                        delicon: 'ace-icon fa fa-trash-o red',
                        search: true,
                        searchicon: 'ace-icon fa fa-search orange',
                        refresh: true,
                        refreshicon: 'ace-icon fa fa-refresh green',
                        view: true,
                        viewicon: 'ace-icon fa fa-search-plus grey',
                    },
                    {
                        //edit record form
                        //closeAfterEdit: true,
                        //width: 700,
                        recreateForm: true,
                        beforeShowForm: function (e) {
                            var form = $(e[0]);
                            form.closest('.ui-jqdialog').find('.ui-jqdialog-titlebar').wrapInner('<div class="widget-header" />')
                            style_edit_form(form);
                        }
                    },
                    {
                        //new record form
                        //width: 700,
                        closeAfterAdd: true,
                        recreateForm: true,
                        viewPagerButtons: false,
                        beforeShowForm: function (e) {
                            var form = $(e[0]);
                            form.closest('.ui-jqdialog').find('.ui-jqdialog-titlebar')
                                    .wrapInner('<div class="widget-header" />')
                            style_edit_form(form);
                        }
                    },
                    {
                        //delete record form
                        recreateForm: true,
                        beforeShowForm: function (e) {
                            var form = $(e[0]);
                            if (form.data('styled')) return false;

                            form.closest('.ui-jqdialog').find('.ui-jqdialog-titlebar').wrapInner('<div class="widget-header" />')
                            style_delete_form(form);

                            form.data('styled', true);
                        },
                        onClick: function (e) {
                            //alert(1);
                        }
                    },
                    {
                        //search form
                        recreateForm: true,
                        afterShowSearch: function (e) {
                            var form = $(e[0]);
                            form.closest('.ui-jqdialog').find('.ui-jqdialog-title').wrap('<div class="widget-header" />')
                            style_search_form(form);
                        },
                        afterRedraw: function () {
                            style_search_filters($(this));
                        }
                        ,
                        multipleSearch: true,
                        /**
                         multipleGroup:true,
                         showQuery: true
                         */
                    },
                    {
                        //view record form
                        recreateForm: true,
                        beforeShowForm: function (e) {
                            var form = $(e[0]);
                            form.closest('.ui-jqdialog').find('.ui-jqdialog-title').wrap('<div class="widget-header" />')
                        }
                    }
            )


            function style_edit_form(form) {
                //enable datepicker on "sdate" field and switches for "stock" field
                form.find('input[name=sdate]').datepicker({format: 'yyyy-mm-dd', autoclose: true})

                form.find('input[name=stock]').addClass('ace ace-switch ace-switch-5').after('<span class="lbl"></span>');
                //don't wrap inside a label element, the checkbox value won't be submitted (POST'ed)
                //.addClass('ace ace-switch ace-switch-5').wrap('<label class="inline" />').after('<span class="lbl"></span>');


                //update buttons classes
                var buttons = form.next().find('.EditButton .fm-button');
                buttons.addClass('btn btn-sm').find('[class*="-icon"]').hide();//ui-icon, s-icon
                buttons.eq(0).addClass('btn-primary').prepend('<i class="ace-icon fa fa-check"></i>');
                buttons.eq(1).prepend('<i class="ace-icon fa fa-times"></i>')

                buttons = form.next().find('.navButton a');
                buttons.find('.ui-icon').hide();
                buttons.eq(0).append('<i class="ace-icon fa fa-chevron-left"></i>');
                buttons.eq(1).append('<i class="ace-icon fa fa-chevron-right"></i>');
            }

            function style_delete_form(form) {
                var buttons = form.next().find('.EditButton .fm-button');
                buttons.addClass('btn btn-sm btn-white btn-round').find('[class*="-icon"]').hide();//ui-icon, s-icon
                buttons.eq(0).addClass('btn-danger').prepend('<i class="ace-icon fa fa-trash-o"></i>');
                buttons.eq(1).addClass('btn-default').prepend('<i class="ace-icon fa fa-times"></i>')
            }

            function style_search_filters(form) {
                form.find('.delete-rule').val('X');
                form.find('.add-rule').addClass('btn btn-xs btn-primary');
                form.find('.add-group').addClass('btn btn-xs btn-success');
                form.find('.delete-group').addClass('btn btn-xs btn-danger');
            }

            function style_search_form(form) {
                var dialog = form.closest('.ui-jqdialog');
                var buttons = dialog.find('.EditTable')
                buttons.find('.EditButton a[id*="_reset"]').addClass('btn btn-sm btn-info').find('.ui-icon').attr('class', 'ace-icon fa fa-retweet');
                buttons.find('.EditButton a[id*="_query"]').addClass('btn btn-sm btn-inverse').find('.ui-icon').attr('class', 'ace-icon fa fa-comment-o');
                buttons.find('.EditButton a[id*="_search"]').addClass('btn btn-sm btn-purple').find('.ui-icon').attr('class', 'ace-icon fa fa-search');
            }

            function beforeDeleteCallback(e) {
                var form = $(e[0]);
                if (form.data('styled')) return false;

                form.closest('.ui-jqdialog').find('.ui-jqdialog-titlebar').wrapInner('<div class="widget-header" />')
                style_delete_form(form);

                form.data('styled', true);
            }

            function beforeEditCallback(e) {
                var form = $(e[0]);
                form.closest('.ui-jqdialog').find('.ui-jqdialog-titlebar').wrapInner('<div class="widget-header" />')
                style_edit_form(form);
            }


            //it causes some flicker when reloading or navigating grid
            //it may be possible to have some custom formatter to do this as the grid is being created to prevent this
            //or go back to default browser checkbox styles for the grid
            function styleCheckbox(table) {
                /**
                 $(table).find('input:checkbox').addClass('ace')
                 .wrap('<label />')
                 .after('<span class="lbl align-top" />')


                 $('.ui-jqgrid-labels th[id*="_cb"]:first-child')
                 .find('input.cbox[type=checkbox]').addClass('ace')
                 .wrap('<label />').after('<span class="lbl align-top" />');
                 */
            }


            //unlike navButtons icons, action icons in rows seem to be hard-coded
            //you can change them like this in here if you want
            function updateActionIcons(table) {
                /**
                 var replacement =
                 {
                     'ui-ace-icon fa fa-pencil' : 'ace-icon fa fa-pencil blue',
                     'ui-ace-icon fa fa-trash-o' : 'ace-icon fa fa-trash-o red',
                     'ui-icon-disk' : 'ace-icon fa fa-check green',
                     'ui-icon-cancel' : 'ace-icon fa fa-times red'
                 };
                 $(table).find('.ui-pg-div span.ui-icon').each(function(){
				var icon = $(this);
				var $class = $.trim(icon.attr('class').replace('ui-icon', ''));
				if($class in replacement) icon.attr('class', 'ui-icon '+replacement[$class]);
			})
                 */
            }

            //replace icons with FontAwesome icons like above
            function updatePagerIcons(table) {
                var replacement =
                {
                    'ui-icon-seek-first': 'ace-icon fa fa-angle-double-left bigger-140',
                    'ui-icon-seek-prev': 'ace-icon fa fa-angle-left bigger-140',
                    'ui-icon-seek-next': 'ace-icon fa fa-angle-right bigger-140',
                    'ui-icon-seek-end': 'ace-icon fa fa-angle-double-right bigger-140'
                };
                $('.ui-pg-table:not(.navtable) > tbody > tr > .ui-pg-button > .ui-icon').each(function () {
                    var icon = $(this);
                    var $class = $.trim(icon.attr('class').replace('ui-icon', ''));

                    if ($class in replacement) icon.attr('class', 'ui-icon ' + replacement[$class]);
                })
            }

            function enableTooltips(table) {
                $('.navtable .ui-pg-button').tooltip({container: 'body'});
                $(table).find('.ui-pg-div').tooltip({container: 'body'});
            }

            //var selr = jQuery(grid_selector).jqGrid('getGridParam','selrow');

            $(document).one('ajaxloadstart.page', function (e) {
                $.jgrid.gridDestroy(grid_selector);
                $('.ui-jqdialog').remove();
            });
        });
    });
</script>

<!-- ztree -->
<script type="text/javascript">
    //引入加载的 js
    var scripts = [null, "${ctx}/zTree/js/jquery.ztree.core-3.5.js", "${ctx}/zTree/js/jquery.ztree.excheck-3.5.js", "${ctx}/zTree/js/jquery.ztree.exedit-3.5.js", "${ctx}/h819/js/MyUtils.js", null]
    //var scripts = [null,"../../assets/js/fuelux/fuelux.tree.js", null]

    //放置 ztree 的元素 id，参见上文。js 函数最后定义
    var ztree_root = "treeDemo";


    function getZTree(root) {
        return $.fn.zTree.getZTreeObj(root);
    }


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

    /**
     *
     * Bootstrap Modals.js 是弹出框，是网页加载后，弹出指定的 div 的内容，该 div 内容是网页加载后的网页呈现内容的一部分
     * modal 只是弹出而已。
     * 所以想要刷新这部分内容，需要程序再加载一次 modal div 的内容
     * ---
     * 重新异步加载 ztree 节点  reAsyncChildNodes
     * 此函数可以供页面所有元素调用
     * 自动调用 ztree setting 中的 asyncByTreeType 定义的方法
     */
    function refreshNode() {
        var zTree = getZTree(ztree_root);
        var type = "refresh";
        var silent = true;
        var nodes = zTree.getNodes(); // 返回所有根节点
        if (nodes.length == 0) {
            return;
        }
        for (var i = 0, l = nodes.length; i < l; i++) {
            zTree.reAsyncChildNodes(nodes[i], type, silent);
            if (!silent) zTree.selectNode(nodes[i]);
        }
    }


    $('.page-content-area').ace_ajax('loadScripts', scripts, function () {
        //inline scripts related to this page
        jQuery(function ($) {

            var setting = {

                view: {
                    selectedMulti: true
                },

                check: {
                    enable: true
                },

                data: {
                    keep: {
                        parent: false, //父节点 允许添加子节点
                        leaf: false //子节点 允许添加子节点
                    },
                    simpleData: {
                        enable: false
                    }
                },

                callback: {},

                async: {
                    enable: true, //开启异步加载模式.如果设置为 true，请务必设置 setting.asyncByTreeType 内的其它参数。
                    url: "${ctx}/ajax/tree/ztree/asyncByTreeTypeAndRole.html", //Ajax 获取数据的 URL 地址。第一次加载页面(此时后台确定第一次加载页面需要展示到树的第几级)和点击关闭的父节点时激发此 url。
                    autoParam: ["id"], //异步加载子节点时，需要自动提交父节点属性的参数 。参数应该是：当点击关闭的父节点时，获取的该父节点的数据中存在的参数，他们和 url 一同传递到后台的参数，用于区分点击了哪个关闭的父节点。
                    otherParam: {
                        "treeType": "${treeType}",
                        //  "role_id": $("#roleId").val(),  不行，只能初始化时读取一次，无法动态获取 roleId 的值。只能用函数返回,函数会动态执行，每次加载都会重新读取一遍
                        "role_id": function () {
                            return $("#roleId").val();
                        }
                    } //这个是我们可以自定义的参数。第一次加载树，决定树类型
                    // dataType: "text",//默认text
                    // type:"get",//默认post
                }
            };

            /*================================ tools begin ================================================*/


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

            $(document).ready(function () {
                //$.fn.zTree.init($("#treeDemo"), setting, zNodes);
                $.fn.zTree.init($("#treeDemo"), setting); //异步加载,设置 ztree 节点
            });

        });
    });
</script>
