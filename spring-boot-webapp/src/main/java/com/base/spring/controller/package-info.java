/**
 * Description : TODO(spring mvc , freeMarker 注意事项)
 * <p>
 * spring mvc 和 freeMarker 整合
 * 发现一个奇葩的问题
 * 1. 通过 org.springframework.ui.Model 参数，只能向页面传递字符串变量，如
 * public String role(@RequestParam(value = "treeType", required = true) String treeType, HttpServletRequest request, Model model) {
 * model.addAttribute("treeType", "Menu"); //对菜单进行授权
 * }
 * ---
 * 2. 传递非字符串对象到前端，必须通过 @ModelAttribute("model") 对参数 model 强制赋值，并且是  org.springframework.ui.ModelMap 类型
 * public String group( @ModelAttribute("model") ModelMap model) {  // 参数写法必须这样
 * List<Message> users = new ArrayList<>();
 * users.add(new User("jiang", "hui"));
 * users.add(new User("jiang1", "hui1"));
 * model.addAttribute("users", users);
 * }
 */

package com.base.spring.controller;

//传递非字符串对象到前端，必须通过 @ModelAttribute("model") 对 model 强制赋值，并且是 ModelMap 类型s