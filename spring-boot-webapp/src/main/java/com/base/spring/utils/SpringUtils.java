package com.base.spring.utils;

import com.base.spring.custom.security.SecurityUser;
import com.google.common.collect.Maps;
import org.h819.commons.MyFastJsonUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.security.web.savedrequest.SavedRequest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

/**
 * Description : TODO()
 * User: h819
 * Date: 2015/12/30
 * Time: 18:34
 * To change this template use File | Settings | File Templates.
 */
public class SpringUtils {
    //private static final log log = LoggerFactory.getLogger(SpringUtils.class);

    /**
     * 坑爹大全 !
     * 在 spring security 中，loginPage("/login") 是个特殊的 url (其他的 url 没有此限制，非 spring security 环境也无此限制)
     * 处理 /login 的 controller ，利用 @RequestParam(value = "error", required = false) 是无法接到任何参数信息的
     * "http://localhost:8888/login?error=错误信息" 的 error 参数无法接到，不光是 error ，所有的参数都接不到
     * spring security 把  "http://localhost:8888/login?error=错误信息"
     * 处理为 "http://localhost:8888/login" ，直接发给 controller ，为啥呢？
     * 当常见的需求是，登陆成功或者不成功，还想返回 /login ，并且传递点参数 /login?error=失败
     * 无法处理
     * 但 spring security 又提供了一个 org.springframework.security.web.savedrequest.SavedRequest ，来还原原始 request，可以利用它来获取参数
     * 这么做为什么？不知道
     * 又浪费了几个小时查找资料
     *
     * @param request  GET 方式发送的 http://localhost:8888/login?error=abc&rr=dce
     * @param response
     * @return
     */
    public static Map<String, String> parseSpringSecurityLoginUrlWithExtraParameters(HttpServletRequest request, HttpServletResponse response) {

        SavedRequest savedRequest = new HttpSessionRequestCache().getRequest(request, response);
        if (savedRequest == null)
            return Maps.newHashMap(); // 空 map，避免异常

        Map<String, String[]> map0 = savedRequest.getParameterMap(); //难道参数的值是个多个字符串？ 为什么返回 Map<String, String[]>  ?
        Map map = new HashMap<String, String>(map0.size());

        for (Map.Entry<String, String[]> entry : map0.entrySet()) {
            map.put(entry.getKey(), entry.getValue()[0]);
        }

        MyFastJsonUtils.prettyPrint(map);

        return map;
    }

    /**
     * spring security 验证通过之后，获取验证通过的用户信息。
     *     * @return
     */
    public static SecurityUser getSecurityUser() {
        return (SecurityUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }
}
