package com.lhj.shiro.jwt.utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.interfaces.DecodedJWT;
import lombok.extern.slf4j.Slf4j;

import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.Map;

@Slf4j
public class JWTUtil {

    private static final long DEFAULT_EXPIRE = 10 * 60 * 1000;

    public static final String USERNAME_CLAIMS = "username";

    /**
     * 生成token
     * @param claims    需要记录的字段
     * @param secret    登录密钥 - 已加密过的
     * @param expire    超时时间
     * @return          token
     */
    public static String sign(Map<String, String> claims, String secret, long expire){
        try{
            Date date = new Date(System.currentTimeMillis() + expire);
            return JWT.create()
                    .withClaim(USERNAME_CLAIMS, claims.get(USERNAME_CLAIMS))
                    .withExpiresAt(date)
                    .sign(Algorithm.HMAC256(secret));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            log.error("error on sign jwtToken, error message: " + e.getMessage());
        }
        return null;
    }

    public static String sign(Map<String, String> claims, String secret){
        return sign(claims, secret, DEFAULT_EXPIRE);
    }

    /**
     * 验证token是否正确
     * @param token         token
     * @param claims        token里面记录的字段
     * @param secret        凭证密钥
     * @return              如果验证不正确就是false，否则就是true
     */
    public static boolean verify(String token, Map<String, String> claims, String secret){
        try{
            JWTVerifier jwtVerifier = JWT.require(Algorithm.HMAC256(secret))
                    .withClaim(USERNAME_CLAIMS, claims.get(USERNAME_CLAIMS))
                    .build();
            DecodedJWT jwt = jwtVerifier.verify(token);
            return true;
        } catch (UnsupportedEncodingException e) {
            log.error("error on verify token, error message: " + e.getMessage());
            return false;
        }
    }

    public static String getUsername(String token){
        return getClaims(token, USERNAME_CLAIMS);
    }

    /**
     * 从token里面获取指定的字段值
     * @param token         token
     * @param claimName     字段名
     * @return              如果解析错误或者没有这个字段会返回null
     */
    private static String getClaims(String token, String claimName){
        try{
            DecodedJWT jwt = JWT.decode(token);
            return jwt.getClaim(claimName).asString();
        } catch (JWTDecodeException e){
            log.error("error on decode jwtToken, error message: " + e.getMessage());
        }
        return null;
    }

    private static String refresh(String token){
        try{
            DecodedJWT jwt = JWT.decode(token);
            if(canRefresh(token)){
            }
            return token;
        } catch (JWTDecodeException e1){
            log.error("error on decode jwtToken, error message: " + e1.getMessage());
        }
    }

    private static boolean canRefresh(String token){
        try{
            DecodedJWT jwt = JWT.decode(token);
            Date expiresDate = jwt.getExpiresAt();
            return !expiresDate.after(new Date(System.currentTimeMillis() + 5 * 50 * 1000));
        } catch (JWTDecodeException e1){
            log.error("error on decode jwtToken, error message: " + e1.getMessage());
            return false;
        }
    }

}
