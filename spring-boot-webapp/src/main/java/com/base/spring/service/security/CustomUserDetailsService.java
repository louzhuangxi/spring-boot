package com.base.spring.service.security;

import com.base.spring.custom.security.SecurityUser;
import com.base.spring.domain.UserEntity;
import com.base.spring.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
@Slf4j
@Service
@Transactional(readOnly = true)
public class CustomUserDetailsService implements UserDetailsService {

    //private static final log log = LoggerFactory.getLogger(CustomUserDetailsService.class);

    @Autowired
    private UserService userService;

//
//    @Autowired
//    private UserRepository userRepository;
//
//    @Autowired
//    private RoleRepository roleRepository;

    @Autowired
    private LoginAttemptService loginAttemptService;

    @Autowired
    private HttpServletRequest request;

    public CustomUserDetailsService() {
        super();
    }

    /**
     * 验证方法，必需返回 UserDetails 类型，如本例中的 org.springframework.security.core.userdetails.User
     * spring security session 中，随时获取该返回值的方法:
     * 1. UserDetails principal = (UserDetails)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
     *
     * @param email
     * @return
     * @throws UsernameNotFoundException
     */
    @Override
    //必需，否则无法级联 role 对象，和 WebSecurityConfig 中 EnableGlobalMethodSecurity、EnableTransactionManagement 呼应
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        log.info("Authenticating user with email={}", email.replaceFirst("@.*", "@***"));

        /**
         * 通过事件机制，验证登陆次数，前端显示登陆次数过多的信息，代码如下
         */

//        <c:when test="${SPRING_SECURITY_LAST_EXCEPTION.message == 'blocked'}">
//        <div class="alert alert-error">
//        <spring:message code="auth.message.blocked"></spring:message>
//        </div>
//        </c:when>

        final String ip = request.getRemoteAddr();
        if (loginAttemptService.isBlocked(ip)) {
            throw new RuntimeException("blocked");
        }


        /**
         * 进行用户验证
         */
        UserEntity userEntity = userService.getUserByEmail(email).orElseThrow(() -> new UsernameNotFoundException(String.format("User with email=%s was not found", email)));

        log.info("password : " + userEntity.getPassword());
        log.info("email : " + userEntity.getEmail());
//        for (String role : userEntity.getStringRoles())
//            log.info("roles name : " + role);

        return new SecurityUser(userEntity);
    }
}
