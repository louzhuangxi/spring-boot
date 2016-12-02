package com.base.spring.custom;

import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.security.web.authentication.session.SessionAuthenticationException;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Description : TODO()
 * User: h819
 * Date: 2016/12/2
 * Time: 15:20
 * To change this template use File | Settings | File Templates.
 */
@Component
public class CustomAuthenticationFailureHandler2 extends SimpleUrlAuthenticationFailureHandler {
  //  private static final Logger logger = LoggerFactory.getLogger(CustomAuthenticationFailureHandler2.class);
   //https://github.com/stormpath/stormpath-default-spring-boot-custom-unverified-handler-example/blob/master/src/main/java/com/stormpath/examples/auth/UnverifiedAuthenticationHandler.java
    http://stackoverflow.com/questions/26502958/an-explanation-about-overriding-simpleurlauthenticationfailurehandler
    https://github.com/anthavio/anthavio-spring/blob/master/src/main/java/net/anthavio/spring/security/ExceptionMappingAuthenticationFailureHandler.java

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {


        super.onAuthenticationFailure(request, response, exception);

        if (exception.getClass().isAssignableFrom(UsernameNotFoundException.class)) {
            logger.info("用户名没找");
        } else if (exception.getClass().isAssignableFrom(DisabledException.class)) {
            logger.info("用户无效");
        } else if (exception.getClass().isAssignableFrom(BadCredentialsException.class)) {
            logger.info("BadCredentialsException");
            // setDefaultFailureUrl("/url1");
        } else if (exception.getClass().isAssignableFrom(SessionAuthenticationException.class)) {
            logger.info("SessionAuthenticationException");
            // setDefaultFailureUrl("/url3");
        }
    }

}
