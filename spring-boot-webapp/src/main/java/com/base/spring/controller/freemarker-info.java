/**
 * Description : TODO(freeMarker )
 * User: h819
 * Date: 2016/11/25
 * Time: 14:32
 * =====================================================================================
 * spring mvc 和 freeMarker 整合 , model 传递变量获取问题
 * =====================================================================================
 * 0. 两种方式，采用第一种，第二种不用
 * -
 * 1. 通过 org.springframework.ui.Model 参数，向页面传递变量，如
 * public String role(@RequestParam(value = "treeType", required = true) String treeType, HttpServletRequest request, Model model) {
 * model.addAttribute("treeType", "Menu");
 * List<UserEntity> users = new ArrayList<>();
 * users.add(new UserEntity("jiang", "hui"));
 * users.add(new UserEntity("jiang1", "hui1"));
 * model.addAttribute("users", users);
 * -
 * 1) 页面获取
 * ${"treeType"} ,  ${"users"}
 * -
 * 2) 判断变量是否存在(不带双引号)
 * <#if treeType ??></#if>
 * -
 * <#if users ??>
 * <#list menu.children as user>
 * ${user.name}
 * </#list>
 * </#if>
 * -
 * ---------------------------------------------------------------------------------------------------------------------------------------
 * 2. 通过 @ModelAttribute("model") 对参数 model 强制赋值
 * 容器并且必须是 org.springframework.ui.ModelMap 类型
 * org.springframework.ui.Model 不行
 * ModelAndView 也不行
 * public String group( @ModelAttribute("model") ModelMap model) {  // 参数写法必须这样
 * List<Message> users = new ArrayList<>();
 * users.add(new User("jiang", "hui"));
 * users.add(new User("jiang1", "hui1"));
 * model.addAttribute("users", users);
 * }
 * -
 * 1) 页面获取
 * model["treeType"] ,  model["users"]
 * -
 * 2) 判断变量是否存在(不带双引号)
 * <#if model["treeType"] ??></#if>
 * -
 * <#if model["users"] ??>
 * <#list model["users"]  as user>
 * ${user.name}
 * </#list>
 * </#if>
 * -
 * =====================================================================================
 */
package com.base.spring.controller;