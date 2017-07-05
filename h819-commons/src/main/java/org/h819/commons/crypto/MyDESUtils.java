package org.h819.commons.crypto;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.DESedeKeySpec;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.security.spec.KeySpec;
import java.util.Base64;

public class MyDESUtils {

    public static final String DESEDE_ENCRYPTION_SCHEME = "DESede";
    public static final String DES_ENCRYPTION_SCHEME = "DES";
    private static final Charset UNICODE_FORMAT = StandardCharsets.UTF_8;
    private static KeySpec keySpec;
    private static SecretKeyFactory keyFactory;
    private static Cipher cipher;


    public static void main(String args[]) throws Exception {

        String DEFAULT_ENCRYPTION_KEY = "Thiss 这门";


        String target = "Java Honk 中文";
        String encrypted = MyDESUtils.encrypt(target, DEFAULT_ENCRYPTION_KEY);
        String decrypted = MyDESUtils.decrypt(encrypted, DEFAULT_ENCRYPTION_KEY);

        System.out.println("String To Encrypt : " + target);
        System.out.println("Encrypted String : " + encrypted);
        System.out.println("Decrypted String : " + decrypted);
        System.out.println("Decrypted String : " + MyDESUtils.decrypt("OIsalERyF0rVbixWuSshPlM2U2LeBqgb", DEFAULT_ENCRYPTION_KEY));


    }

    public static String encrypt(String strToEncrypt, String keys) throws Exception {

        if (strToEncrypt == null || strToEncrypt.trim().length() == 0)
            throw new IllegalArgumentException(
                    "unencrypted string was null or empty");


        initKey(DES_ENCRYPTION_SCHEME, keys);


        SecretKey key = keyFactory.generateSecret(keySpec);

        cipher.init(Cipher.ENCRYPT_MODE, key);

        byte[] encrypt = cipher.doFinal(strToEncrypt.getBytes(UNICODE_FORMAT));

        return Base64.getEncoder().encodeToString(encrypt);

    }

    public static String decrypt(String strToDecrypt, String keys) throws Exception {

        if (strToDecrypt == null || strToDecrypt.trim().length() <= 0)
            throw new IllegalArgumentException("encrypted string was null or empty");

        initKey(DES_ENCRYPTION_SCHEME, keys);

        SecretKey key = keyFactory.generateSecret(keySpec);
        cipher.init(Cipher.DECRYPT_MODE, key);


        byte[] base64 = Base64.getDecoder().decode(strToDecrypt);

        byte[] decrypte = cipher.doFinal(base64);

        return new String(decrypte);

    }

    private static void initKey(String encryptionScheme, String key) throws Exception {

        if (key == null)
            throw new IllegalArgumentException("encryption key was null");

        // System.out.println("key size : " + key.length());

        key = key.trim();

        if (key.length() < 8)
            throw new IllegalArgumentException(
                    "encryption key 长度 不能小于 8 characters , 当前 key 长度为 " + key.length());

        byte[] keyAsBytes = key.getBytes(UNICODE_FORMAT);

        if (encryptionScheme.equals(DESEDE_ENCRYPTION_SCHEME)) {
            keySpec = new DESedeKeySpec(keyAsBytes);

        } else if (encryptionScheme.equals(DES_ENCRYPTION_SCHEME)) {

            keySpec = new DESKeySpec(keyAsBytes);

        } else {

            throw new IllegalArgumentException("Encryption scheme not supported: "
                    + encryptionScheme);
        }

        keyFactory = SecretKeyFactory.getInstance(encryptionScheme);

        cipher = Cipher.getInstance(encryptionScheme);

    }

}
