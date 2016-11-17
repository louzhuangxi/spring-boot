/**
 * Description : TODO(配置文件)
 * User: h819
 * -------------------------------------------------------
 * spring security ajax csrf
 * spring security 中使用 ajax ，必须在使用 ajax 方法的页面中做如下设置:
 * -
 * 1. 页面中配置
 * <!-- ajax 请求时用到 default header name is X-CSRF-TOKEN 例子在 login.js 中 -->
 * <meta name="_csrf" th:content="${_csrf.token}"/>
 * <meta name="_csrf_header" th:content="${_csrf.headerName}"/>
 * 2. ajax 代码前面定义如下函数即可
 * var token=$("meta[name='_csrf']").attr("content");
 * var header=$("meta[name='_csrf_header']").attr("content");
 * $(document).ajaxSend(function(e,xhr,options){
 * xhr.setRequestHeader(header,token);
 * });
 */
package com.base.spring.config;