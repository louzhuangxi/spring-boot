/**
 * Description : TODO(spring mvc 注意事项)
 * <p>
 * <p>
 * =====================================================================================
 * spring mvc controller 接收 array 参数
 * =====================================================================================
 * spring mvc controller 接收 array 参数
 * <p>
 * $.ajax({ //ajax 提交到controller的delApplication方法处理
 * type: "post",
 * async: false,
 * url: "${ctx}/ajax/grid/group/get_checked_checkbox.html",
 * data: { //传递的参数和值
 * checkbox: values, //传递 array(values 为 array), spring mvc controller 用 checkbox[] 参数接收
 * group_id: $("#groupId").val()
 * },
 * dataType: "html", //dataType指定返回值的类型，必须与后台的返回值一致。否则无法进入success回掉
 * success: function (data) { //处理成功的回调函数
 * },
 * error: function () {
 * }
 * })
 * //
 *
 * @RequestParam(value = "checkbox[]", required = true) String[] checkboxs
 * -
 * =====================================================================================
 */

package com.base.spring.controller;