package com.base.spring.custom.security;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collection;

/**
 * 演示登录成功后的处理
 */
@Component
public class CustomAuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();

    @Override
    protected void handle(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        //按照原默认流程处理
        super.handle(request, response, authentication);

        /*如果需要跳转到不同的 url ，启用下面的代码 ，注释掉   super.handle(request, response, authentication);
        String targetUrl = getUrl(authentication);
        if (response.isCommitted()) {
            log.debug("Response has already been committed. Unable to redirect to " + targetUrl);
            return;
        }

        redirectStrategy.sendRedirect(request, response, targetUrl);
        super.clearAuthenticationAttributes(request);  // 即  session.removeAttribute("SPRING_SECURITY_LAST_EXCEPTION");
     */
    }

    /**
     * This method extracts the roles of currently logged-in user and returns
     * appropriate URL according to his/her role.
     */
    protected String getUrl(Authentication authentication) {

        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        for (GrantedAuthority grantedAuthority : authorities) {
            String role = grantedAuthority.getAuthority();
            if (role.equalsIgnoreCase(("ROLE_ADMIN"))) {
                return "/admin";
            } else if (role.equalsIgnoreCase(("ROLE_USER"))) {
                return "/home";
            } else if (role.equalsIgnoreCase(("ROLE_DBA"))) {
                return "/dba";
            } else {
                // throw new IllegalStateException();
            }
        }
        return "/";
    }


    protected RedirectStrategy getRedirectStrategy() {
        return redirectStrategy;
    }

    public void setRedirectStrategy(RedirectStrategy redirectStrategy) {
        this.redirectStrategy = redirectStrategy;
    }

}