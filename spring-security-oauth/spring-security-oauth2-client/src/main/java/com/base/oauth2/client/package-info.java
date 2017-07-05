/**
 * apache http client 方式，尝试和很久，有很多未解问题，不再尝试，统一用 spring oauth 版本
 * 另外 apache oltu 也有 oauth server 和 client
 * <p/>
 * 本例子，必需运行在 web 环境下
 * <p/>
 * oauth2 client 实现
 * <p/>
 * 方式一 : http client (不采用，不再自己实现)
 * 方式二 : spring oauth2
 * --
 * ----------
 * Oauth2  的五种协议模式 grant_type --
 * ----------
 * grant_type
 * 说明Oauth支持的grant_type(授权方式)与功能
 * <p/>
 * authorization_code -- 授权码模式(即先登录获取code,再获取token)
 * password -- 密码模式(将用户名,密码传过去,直接获取token)
 * refresh_token -- 刷新access_token
 * implicit -- 简化模式(在redirect_uri 的Hash传递token; Auth客户端运行在浏览器中,如JS,Flash)
 * client_credentials -- 客户端模式(无用户,用户向客户端注册,然后客户端以自己的名义向'服务端'获取资源)
 */
package com.base.oauth2.client;