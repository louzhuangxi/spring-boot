package com.base.spring.utils;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 * Description : TODO(org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder 重新包装一下)
 * User: h819
 * Date: 2016/1/6
 * Time: 17:29
 * To change this template use File | Settings | File Templates.
 * <p>
 * BCrypt 加密算法 : 不能得到明文，只能比较
 * bcrypt 验证方式和其它加密方式不同，不是直接解密得到明文，也不是二次加密比较密文，而是把明文和存储的密文一块运算得到另一个密文，如果这两个密文相同则验证成功。
 * BCrypt 算法与 MD5/SHA 算法有一个很大的区别，每次生成的 hash 值都是不同的，就可以免除存储 salt，暴力破解起来也更困难。
 * BCrypt 加密后的字符长度比较长，有60位，所以用户表中密码字段的长度，如果打算采用BCrypt 加密存储，字段长度不得低于 60。
 * <p>
 * 另外一个：
 * org.springframework.security.crypto.scrypt.SCryptPasswordEncoder
 * <p>
 * BCrypt 加密算法 : 不能得到明文，只能比较
 * bcrypt 验证方式和其它加密方式不同，不是直接解密得到明文，也不是二次加密比较密文，而是把明文和存储的密文一块运算得到另一个密文，如果这两个密文相同则验证成功。
 * BCrypt 算法与 MD5/SHA 算法有一个很大的区别，每次生成的 hash 值都是不同的，就可以免除存储 salt，暴力破解起来也更困难。
 * BCrypt 加密后的字符长度比较长，有60位，所以用户表中密码字段的长度，如果打算采用BCrypt 加密存储，字段长度不得低于 60。
 * <p>
 * 另外一个：
 * org.springframework.security.crypto.scrypt.SCryptPasswordEncoder
 * <p>
 * 也可以通过 bean 方式创建
 *
 * @Bean public BCryptPasswordEncoder passwordEncoder() {
 * return new BCryptPasswordEncoder();
 * }
 * <p>
 * BCrypt 加密算法 : 不能得到明文，只能比较
 * bcrypt 验证方式和其它加密方式不同，不是直接解密得到明文，也不是二次加密比较密文，而是把明文和存储的密文一块运算得到另一个密文，如果这两个密文相同则验证成功。
 * BCrypt 算法与 MD5/SHA 算法有一个很大的区别，每次生成的 hash 值都是不同的，就可以免除存储 salt，暴力破解起来也更困难。
 * BCrypt 加密后的字符长度比较长，有60位，所以用户表中密码字段的长度，如果打算采用BCrypt 加密存储，字段长度不得低于 60。
 * <p>
 * 另外一个：
 * org.springframework.security.crypto.scrypt.SCryptPasswordEncoder
 * <p>
 * 也可以通过 bean 方式创建
 * @Bean public BCryptPasswordEncoder passwordEncoder() {
 * return new BCryptPasswordEncoder();
 * }
 */
//http://www.mindrot.org/projects/jBCrypt/
//org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
//BCrypt implements OpenBSD-style Blowfish password hashing using the scheme described in "A Future-Adaptable Password Scheme" by Niels Provos and David Mazieres.
// Most of the other mechanism, such as the MD5PasswordEncoder and ShaPasswordEncoder use weaker algorithms and are now deprecated.
// MD5PasswordEncoder 和 ShaPasswordEncoder 加密强度不够，已经不推荐

/**
 * BCrypt 加密算法 : 不能得到明文，只能比较
 * bcrypt 验证方式和其它加密方式不同，不是直接解密得到明文，也不是二次加密比较密文，而是把明文和存储的密文一块运算得到另一个密文，如果这两个密文相同则验证成功。
 * BCrypt 算法与 MD5/SHA 算法有一个很大的区别，每次生成的 hash 值都是不同的，就可以免除存储 salt，暴力破解起来也更困难。
 * BCrypt 加密后的字符长度比较长，有60位，所以用户表中密码字段的长度，如果打算采用BCrypt 加密存储，字段长度不得低于 60。
 */

/**
 * 另外一个：
 * org.springframework.security.crypto.scrypt.SCryptPasswordEncoder
 */


/**
 * 也可以通过 bean 方式创建
 * @Bean public BCryptPasswordEncoder passwordEncoder() {
return new BCryptPasswordEncoder();
}
 */

/**
 scrypt	Yes
 bcrypt	Yes
 SHA-1	No  (不安全)
 SHA-2	No  (不安全)
 MD5	No  (不安全)
 PBKDF2	No   (不安全)
 */
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
