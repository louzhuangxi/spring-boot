package org.h819.commons.crypto;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Base64;

/**
 * Aes encryption
 * http://aesencryption.net/#Java-aes-encryption-example
 */
public class MyAESUtils {

    private static String type = "AES";
    private static String modle = "AES/ECB/PKCS5Padding";

    public static void main(String args[]) throws Exception {
        final String strToEncrypt = "My text to encrypt 中文";
        final String strPssword = "encryptor keys";
        MyAESUtils.generateKey(strPssword);

        // AES.encrypt(strToEncrypt.trim());

        System.out.println("String to Encrypt: " + strToEncrypt);

        final String strToDecrypt = MyAESUtils.encrypt(strToEncrypt, strPssword);

        System.out.println("after Encrypted: " + strToDecrypt);

        System.out.println("Decrypted : " + MyAESUtils.decrypt(strToDecrypt, "encryptor key"));

        System.out.println("Decrypted : " + MyAESUtils.decrypt("Gwg2k5xDpUGuZn8PvYxnkKelNbmEkrV8mMJyiXMIDnw=", "encryptor key"));

    }


    public static String encrypt(String strToEncrypt, String key) throws Exception {

        //根据 key 生成 SecretKey
        SecretKey secretKey = generateKey(key);

        Cipher cipher = Cipher.getInstance(modle);

        cipher.init(Cipher.ENCRYPT_MODE, secretKey);

        byte[] encrypt = cipher.doFinal(strToEncrypt.getBytes(StandardCharsets.UTF_8));

        return Base64.getEncoder().encodeToString(encrypt);

    }

    public static String decrypt(String strToDecrypt, String key) throws Exception {

        SecretKey secretKey = generateKey(key);

        byte[] base64 = Base64.getDecoder().decode(strToDecrypt);


        Cipher cipher = Cipher.getInstance(modle);

        cipher.init(Cipher.DECRYPT_MODE, secretKey);

        byte[] decrypte = cipher.doFinal(base64);

        return new String(decrypte);

    }


    /**
     * AES 加密算法，密码长度有: 128 / 192 / 256 bit ，其中 192 和 256 算法，由于美国对加密软件的限制，不包含在 jdk 中
     * 每个英文字符为 8 bit ，汉字为 16 bit
     * 所以密码长度如果为英文字符，不能超过 16 个，如果是汉字，不能超过 8 个。
     *
     * @param key
     */
    private static SecretKey generateKey(String key) throws NoSuchAlgorithmException {

        if (key.length() > 16)
            throw new IllegalArgumentException("密码长度不能超过 16 Byte，当前密码为 " + key.length() + " Byte");

        byte[] bytes = key.getBytes(StandardCharsets.UTF_8);
        // System.out.println(bytes.length);
        bytes = MessageDigest.getInstance("SHA-1").digest(bytes);
        // Arrays.copyOf ： 复制指定的数组，截取或用 0 填充(长度不足时)
        bytes = Arrays.copyOf(bytes, 16); // use only first 128 bit , 每个英文字符为 8 bit ，汉字为 16 bit
        return new SecretKeySpec(bytes, type);

    }
}
