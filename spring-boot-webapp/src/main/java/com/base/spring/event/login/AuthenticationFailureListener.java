package com.base.spring.event.login;

import com.base.spring.service.security.LoginAttemptService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.security.authentication.event.AuthenticationFailureBadCredentialsEvent;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.stereotype.Component;

/**
 * 登陆失败监听
 */
@Component
public class AuthenticationFailureListener implements ApplicationListener<AuthenticationFailureBadCredentialsEvent> {

    @Autowired
    private LoginAttemptService loginAttemptService;

    @Override
    public void onApplicationEvent(final AuthenticationFailureBadCredentialsEvent e) {
        final WebAuthenticationDetails auth = (WebAuthenticationDetails) e.getAuthentication().getDetails();
        if (auth != null) {
            loginAttemptService.loginFailed(auth.getRemoteAddress());
        }
    }

}