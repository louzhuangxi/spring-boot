<#--声明变量  <#assign user = "${users}">-->
<#assign ctx = "${context.contextPath}">

<title>群组管理</title>

<link rel="stylesheet" href="${ctx}/ace/assets/css/jquery-ui.css"/>
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
            群组管理
        </div>

        <table id="grid-table"></table>

        <div id="grid-pager"></div>

        <script type="text/javascript">
            var $path_base = "../..";//in Ace demo this will be used for editurl parameter
        </script>

        <!-- PAGE CONTENT ENDS -->
    </div><!-- /.col -->
</div><!-- /.row -->

<!-- bootstrap model users-->
<div id="modal-table-users" class="modal fade" tabindex="-1" data-backdrop="static">
    <div class="modal-dialog">
        <form id="informationForm">
            <div class="modal-content">
                <div class="modal-header no-padding">
                    <div class="table-header">
                        <button type="button" class="close" data-dismiss="modal" aria-hidden="true">
                            <span class="white">&times;</span>
                        </button>
                        选择用户
                    </div>
                </div>
                <div class="modal-body" style="max-height: 500px;overflow-y: scroll;">
                    <div id="modal-tip" class="red clearfix"></div>
                    <!--通过表单，在 jqgrid 和 ztree 之间传递参数-->
                    <input id="groupId" type="hidden" value=""/> <!-- groupId -->
                    <div class="widget-box widget-color-blue2">
                        <div class="widget-body">
                            <div id="custom_modal_users" class="widget-main padding-8">
                                <!-- custom begin -->

                                <!-- load by jquery.load()
                                 refreshBootstrapModalUsers();
                                -->

                                <!-- custom end -->
                            </div>
                        </div>
                    </div>
                </div>
                <div class="modal-footer no-margin-top">
                    <div class="text-center">      <!--  data-dismiss="modal" 点击后会关闭 modal -->
                        <button id="submitButtonUsers" type="submit" class="btn btn-white btn-default btn-round"
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

<!-- bootstrap model roles -->
<div id="modal-table-roles" class="modal fade" tabindex="-1" data-backdrop="static">
    <div class="modal-dialog">
        <form id="informationForm">
            <div class="modal-content">
                <div class="modal-header no-padding">
                    <div class="table-header">
                        <button type="button" class="close" data-dismiss="modal" aria-hidden="true">
                            <span class="white">&times;</span>
                        </button>
                        选择角色
                    </div>
                </div>
                <div class="modal-body" style="max-height: 500px;overflow-y: scroll;">
                    <div id="modal-tip" class="red clearfix"></div>
                    <!--通过表单，在 jqgrid 和 ztree 之间传递参数-->
                    <input id="groupId" type="hidden" value=""/> <!-- groupId -->
                    <div class="widget-box widget-color-blue2">
                        <div class="widget-body">
                            <div id="custom_modal_roles" class="widget-main padding-8">
                                <!-- custom begin -->

                                <!-- load by jquery.load()
                                refreshBootstrapModalRoles
                                -->

                                <!-- custom end -->
                            </div>
                        </div>
                    </div>
                </div>
                <div class="modal-footer no-margin-top">
                    <div class="text-center">      <!--  data-dismiss="modal" 点击后会关闭 modal -->
                        <button id="submitButtonRoles" type="submit" class="btn btn-white btn-default btn-round"
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
        "${ctx}/ace/assets/js/jqGrid/jquery.jqGrid.js",
        "${ctx}/ace/assets/js/jqGrid/i18n/grid.locale-cn.js",
        null];

    /**
     * Bootstrap Modals.js 是弹出框，是网页加载后，弹出指定的 div 的内容，该 div 内容是网页加载后的网页呈现内容的一部分
     * modal 只是弹出而已。
     * 所以想要刷新这部分内容，需要程序加载一次 modal div 的内容 , 用 jqury.load() 刷新指定的 div 内容
     * -
     * 此函数可以供页面所有元素调用
     * 点击按钮，刷新 div 内容
     */
    function refreshBootstrapModalUsers() {
        console.log("refreshBootstrapModalUsers , group id =" + $("#groupId").val());
        //传递 group id 到后台，更新 model 中的 users

        /**
         * 每次清空节点下面的信息，否则多次 load 不同的内容，会累加 load 的内容。
         */
        $("#custom_modal_users").empty();
        $("#custom_modal_roles").empty();

        /**
         * 动态加载 bootstrap model
         * .load( url [, data ] [, complete ] )
         * */
        $("#custom_modal_users").load(
                "${ctx}/grid/group/bootstrap-modal-load-users.html",
                {
                    group_id: $("#groupId").val()
                }, // { "choices[]": [ "Jon", "Susan" ] }  or {groupId: 25; ip : 26}
                function (response, status, xhr) {
                    if (status == 'success') {
                        console.log("bootstrap_modal_load_users success");
                        //$('#notification-bar').text('The page has been successfully loaded');
                    } else if (status == "error") {
                        console.log("bootstrap_modal_load_users false");
                        //$('#notification-bar').text('An error occurred');
                    }
                }
        );

    }
    ;


    /**
     * 点击 bootstrap modal 提交按钮
     * */
    $('#submitButtonUsers').click(function () {

        console.log("submitButtonUsers button click");
        //获取所有选中的 checkbox
        var values = $('input[type="checkbox"].ace:checked').map(function () {
            return $(this).val();
        }).toArray();

        $.ajax({ //ajax 提交到controller的delApplication方法处理
            type: "post",
            async: false,
            url: "${ctx}/grid/group/get-checked-checkbox-users-by-group.html",
            data: { //传递的参数和值
                checkbox: values,  // spring mvc controller 用 checkbox[] 参数接收
                group_id: $("#groupId").val()
            },
            dataType: "html", //dataType指定返回值的类型，必须与后台的返回值一致。否则无法进入success回掉
            success: function (data) { //处理成功的回调函数

            }

            ,
            error: function () {

            }
            //处理失败的回到函数
        });
    });

    /**
     * 同上 , for roles
     */
    function refreshBootstrapModalRoles() {
        console.log("refreshBootstrapModalRoles , group id =" + $("#groupId").val());
        //传递 group id 到后台，更新 model 中的 users


        /**
         * 每次清空节点下面的信息，否则多次 load 不同的内容，会累加 load 的内容。
         */
        $("#custom_modal_users").empty();
        $("#custom_modal_roles").empty();


        /**
         * 动态加载 bootstrap model
         * .load( url [, data ] [, complete ] )
         * */
        $("#custom_modal_roles").load(
                "${ctx}/grid/group/bootstrap-modal-load-roles.html",
                {
                    group_id: $("#groupId").val()
                }, // { "choices[]": [ "Jon", "Susan" ] }  or {groupId: 25; ip : 26}
                function (response, status, xhr) {
                    if (status == 'success') {
                        console.log("bootstrap_modal_load_roles success");
                        //$('#notification-bar').text('The page has been successfully loaded');
                    } else if (status == "error") {
                        console.log("bootstrap_modal_load_roles false");
                        //$('#notification-bar').text('An error occurred');
                    }
                }
        );

    }
    ;


    /**
     * 点击 bootstrap modal 提交按钮
     * */
    $('#submitButtonRoles').click(function () {

        console.log("submitButtonRoles button click");
        //获取所有选中的 checkbox
        var values = $('input[type="checkbox"].ace:checked').map(function () {
            return $(this).val();
        }).toArray();

        $.ajax({ //ajax 提交到controller的delApplication方法处理
            type: "post",
            async: false,
            url: "${ctx}/grid/group/get-checked-checkbox-roles-by-group.html",
            data: { //传递的参数和值
                checkbox: values,  // spring mvc controller 用 checkbox[] 参数接收
                group_id: $("#groupId").val()
            },
            dataType: "html", //dataType指定返回值的类型，必须与后台的返回值一致。否则无法进入success回掉
            success: function (data) { //处理成功的回调函数

            }

            ,
            error: function () {

            }
            //处理失败的回到函数
        });
    });

    $('.page-content-area').ace_ajax('loadScripts', scripts, function () {

        jQuery(function ($) {

            var grid_selector = "#grid-table";
            var pager_selector = "#grid-pager";


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
            });

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
                    url: "${ctx}/validate/ajax/role/name/exist.html",
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
                        //jQuery(grid_selector).trigger("reloadGrid"); //ajax 函数执行成功之后，jgrid 刷新当前表格
                    },

                    error: function (jqXHR, textStatus, errorThrown) { //打印出错信息，便于调试
                        alert("error jqXHR: " + jqXHR.status + ',' + jqXHR.statusText);
                        alert("error textStatus: " + textStatus); //"timeout", "error", "abort", and "parsererror" 有四种错误代码，errorThrown 是错误具体信息
                        alert("error errorThrown : " + errorThrown);
                        jQuery(grid_selector).trigger("reloadGrid");
                    }
                });
                return result;
            };

            //检查 invalid
            function myinvalidcheck(value, colname) {

                // alert("value :" +value +" , colname :" +colname);
                if (value == "是" || value == "否") //返回字符串"true"，表示验证通过，不显示任何信息
                    return new Array(true, "");
                else
                    return new Array(false, "通过验证字段：只能填写 '是' 或 '否' "); //返回其他字符串，把其他字符串作为出错的信息，进行显示
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
                url: "${ctx}/grid/group/jqgrid-search", // 查询提交的 remote 地址，该地址返回要求的展示数据
                editurl: "${ctx}/grid/group/jqgrid-edit",//nothing is saved

                datatype: "json", // 返回的数据类型
                mtype: "post", // 提交方式
                caption: "最新消息",//表格描述

                height: 250,
                colNames: [' ', 'ID', '群组名称', '创建时间', '组内用户', '角色授权', '备注'],
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
                        editable: true
                    }, // ace admin 1.3.4 ，不知道为什么，不显示 id 行，真正显示从 name 起

                    {
                        name: 'name',
                        index: 'name',
                        width: 100,
                        search: true,
                        searchoptions: {sopt: ["cn", "eq"]},
                        editable: true,
                        edittype: 'text',
                        editrules: {required: true} //
                    },
                    {
                        name: 'createdDate',
                        index: 'createdDate',
                        width: 100,
                        search: true,
                        searchoptions: {sopt: ["cn", "eq"]},
                        editable: false,
                        edittype: 'text',
                        editrules: {required: true} //
                    },
                    {
                        name: '',
                        index: '',
                        label: '用户群',
                        width: 100,
                        search: false,
                        editable: false,
                        formatter: authorityFormatterUsers //显示一个图标
                    },
                    {
                        name: '',
                        index: '',
                        label: '授权',
                        width: 100,
                        search: false,
                        editable: false,
                        formatter: authorityFormatterRoles //显示一个图标
                    },
                    {
                        name: 'remark',
                        index: 'remark',
                        width: 100,
                        search: true,
                        searchoptions: {sopt: ["cn", "eq"]},
                        editable: true,
                        edittype: 'text',
                        editrules: {required: false} //
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
                sortname: 'createdDate',  //默认排序字段名，点击表头字段排序后，该变量值变为被点击的变量名称
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
                    $(grid_selector).closest(".ui-jqgrid-bdiv").css({"overflow-x": "hidden"}); //强制不显示横向滚动条，反之 hidden 修改为
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
             *点击后，在 Bootstrap Modals 中 ，根据 groupId 加载 users
             *
             * 因为每个 groupId 已经关联的 users 不同，所以每次点击按钮，代表不同 groupId , 都重新异步加载  一次
             * refreshNode 必须放在点击执行的 js 函数的最后，以便于 roleId 赋值后获得
             * refreshNode 函数必须放在调用  jqgrid 函数之外，因为是生成的 button onclick 代码，这段代码在 jqgrid 之外，部内在 jqgrid 内部
             *
             */
            function authorityFormatterUsers(cellvalue, options, cell) {
                var template = "<button data-toggle='modal' onclick='$(\"#modal-table-users\").modal(\"toggle\");$(\"#groupId\").val(\"" + cell.id + "\");refreshBootstrapModalUsers()' class='btn btn-white btn-default btn-round'><i class='ace-icon fa fa-users bigger-110 grey'></i></button>";
                return template;
            }

            function authorityFormatterRoles(cellvalue, options, cell) {
                var template = "<button data-toggle='modal' onclick='$(\"#modal-table-roles\").modal(\"toggle\");$(\"#groupId\").val(\"" + cell.id + "\");refreshBootstrapModalRoles()' class='btn btn-white btn-default btn-round'><i class='ace-icon fa fa-lock bigger-120 red'></i></button>";
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