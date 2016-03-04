/**
 * http://www.baeldung.com/rest-api-spring-oauth2-angularjs
 * https://github.com/eugenp/tutorials/tree/master/spring-security-oauth
 * 该示例拷贝自 上述
 * -
 * AuthorizationServer 和 ResourceServer 分开设置，系统正常运行。
 * (这种分开设置的方式，只能用 JdbcTokenStore 方式共享 token , InMemoryTokenStore 不行)。
 * -
 * 如果合在一起，就会出现拦截顺序问题，AuthorizationServer 无法登陆(google 了半个月的资料，不再试了)。
 * 所以使用分开设置，合在一起的例子等有成功的例子再看。
 * -
 * AuthorizationServer 和 ResourceServer 用不同的端口，可以配置在一条机器上。
 * 最后配置在两天机器上，这样都是 80 端口，不必用特殊端口
 * -
 * client 的详细情况参见 Oauth2ClientRestTemplate
 *
 */
package org;