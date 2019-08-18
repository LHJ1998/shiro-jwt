package com.lhj.shiro.jwt.utils;

import cn.hutool.core.util.CharsetUtil;
import cn.hutool.crypto.symmetric.SymmetricAlgorithm;
import cn.hutool.crypto.symmetric.SymmetricCrypto;
import com.lhj.shiro.jwt.config.ShiroProperties;
import org.apache.shiro.crypto.SecureRandomNumberGenerator;
import org.apache.shiro.crypto.hash.SimpleHash;

public class EncryptUtil {

    private static final String MD5_ALGORITHM = "md5";

    private static final String DEFAULT_SALT = "";

    private static final Integer DEFAULT_ITERATION = 5;

    private static final ShiroProperties SHIRO_PROPERTIES =
            SpringContextUtil.getBean(ShiroProperties.class);

    private static final SymmetricCrypto aes =
            new SymmetricCrypto(SymmetricAlgorithm.AES, SHIRO_PROPERTIES.getSecret().getBytes());

    public static String md5Encrypt(String secret, String salt, int iteration){
        SimpleHash hash = new SimpleHash(MD5_ALGORITHM, secret, salt, iteration);
        return hash.toString();
    }

    public static String md5Encrypt(String secret){
        return md5Encrypt(secret, DEFAULT_SALT, DEFAULT_ITERATION);
    }

    public static String generateRandomNumber(){
        SecureRandomNumberGenerator secureRandomNumberGenerator = new SecureRandomNumberGenerator();
        return secureRandomNumberGenerator.nextBytes().toHex();
    }

    public static String aesEncrypt(String str){
        return aes.encryptHex(str);
    }

    public static String aesDecrypt(String str){
        return aes.decryptStr(str, CharsetUtil.CHARSET_UTF_8);
    }

}
