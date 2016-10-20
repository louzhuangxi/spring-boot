/**
 * Description : TODO(spring mvc , freeMarker 注意事项)
 * <p>
 * -------------------------------------------------------
 * spring mvc 和 freeMarker 整合 (jsp 没有发现)
 * 发现一个奇葩的问题
 * 1. 通过 org.springframework.ui.Model 参数，只能向页面传递字符串变量，如
 * public String role(@RequestParam(value = "treeType", required = true) String treeType, HttpServletRequest request, Model model) {
 * model.addAttribute("treeType", "Menu"); //对菜单进行授权
 * }
 * ---
 * 2. 传递非字符串对象到前端，必须通过 @ModelAttribute("model") 对参数 model 强制赋值，并且是  org.springframework.ui.ModelMap 类型
 * ModelAndView 也不行
 * public String group( @ModelAttribute("model") ModelMap model) {  // 参数写法必须这样
 * List<Message> users = new ArrayList<>();
 * users.add(new User("jiang", "hui"));
 * users.add(new User("jiang1", "hui1"));
 * model.addAttribute("users", users);
 * }
 * <p>
 * -------------------------------------------------------
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
 * @RequestParam(value = "checkbox[]", required = true) String[] checkboxs,
 */

package com.base.spring.controller;

//传递非字符串对象到前端，必须通过 @ModelAttribute("model") 对 model 强制赋值，并且是 ModelMap 类型s