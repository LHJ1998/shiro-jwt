package com.lhj.shiro.jwt.utils;

import org.apache.shiro.crypto.SecureRandomNumberGenerator;
import org.apache.shiro.crypto.hash.SimpleHash;

public class EncryptUtil {

    private static final String MD5_ALGORITHM = "md5";

    public static String md5Encrypt(String secret, String salt, int iteration){
        SimpleHash hash = new SimpleHash(MD5_ALGORITHM, secret, salt, iteration);
        return hash.toString();
    }

    public static String generateRandomNumber(){
        SecureRandomNumberGenerator secureRandomNumberGenerator = new SecureRandomNumberGenerator();
        return secureRandomNumberGenerator.nextBytes().toHex();
    }
}
