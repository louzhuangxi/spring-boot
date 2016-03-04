package com.base.spring.utils;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 * Description : TODO(org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder 重新包装一下)
 * User: h819
 * Date: 2016/1/6
 * Time: 17:29
 * To change this template use File | Settings | File Templates.
 */
//http://www.mindrot.org/projects/jBCrypt/
//org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
//BCrypt implements OpenBSD-style Blowfish password hashing using the scheme described in "A Future-Adaptable Password Scheme" by Niels Provos and David Mazieres.
//专门用来加密密码
public class BCryptPassWordUtils {

    private static BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();


    /**
     * 所有的秘密加密相关的调用都放在这里，统一调用，避免使用其他的加密方式
     */

    /**
     * @return
     */
    public static BCryptPasswordEncoder getBCryptPasswordEncoder() {
        return bCryptPasswordEncoder;
    }

    /**
     * 加密密码
     *
     * @param rawPassword 未加密之前的密码
     * @return 加密后的密码
     */
    public static String encode(CharSequence rawPassword) {
        return bCryptPasswordEncoder.encode(rawPassword);
    }

    /**
     * 比较密码是否相等                                  b
     *
     * @param rawPassword     未加密之前的密码
     * @param encodedPassword 加密后的密码
     * @return
     */
    public static boolean matches(CharSequence rawPassword, String encodedPassword) {
        return bCryptPasswordEncoder.matches(rawPassword, encodedPassword);
    }
}
