package com.lhj.shiro.jwt.controller;

import com.lhj.shiro.jwt.bo.ActiveUser;
import com.lhj.shiro.jwt.dto.ResponseBean;
import com.lhj.shiro.jwt.pojo.User;
import com.lhj.shiro.jwt.service.RedisService;
import com.lhj.shiro.jwt.service.UserService;
import com.lhj.shiro.jwt.utils.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@RestController
public class LoginController {

    @Value("${shiro.iteration}")
    private int iteration;

    @Value("${jwt.expire}")
    private long expire;

    @Autowired
    private UserService userService;

    @Autowired
    private RedisService redisService;

    @PostMapping(value = "/login")
    public ResponseBean<String> login(@RequestBody User user, HttpServletRequest request){
        User dbUser = userService.getUserByUsername(user.getUsername());
        if(dbUser != null) {
            String secret = EncryptUtil.md5Encrypt(user.getPassword(), dbUser.getSalt(), iteration);
            if(dbUser.getPassword().equals(secret)){
                Map<String, String> claims = new HashMap<>();
                claims.put(JWTUtil.USERNAME_CLAIMS, dbUser.getUsername());
                claims.put(JWTUtil.CLIENT_IP_ADDRESS_CLAIMS, IPUtil.getIpAddress(request));
                String token = EncryptUtil.aesEncrypt(JWTUtil.sign(claims, secret));
                //将登录用户存储到Redis
                saveActiveUserToRedis(token, IPUtil.getIpAddress(request));
                return ResponseBean.success("登录成功", token);
            }
        }
        return ResponseBean.fail("用户名或密码错误");
    }

    private void saveActiveUserToRedis(String token, String ip){
        ActiveUser activeUser = new ActiveUser();
        activeUser.setToken(token);
        activeUser.setIp(ip);
        activeUser.setLoginAddress(AddressUtil.getAddress(activeUser.getIp()));
        String key = activeUser.getIp() + "-" + EncryptUtil.md5Encrypt(token);
        redisService.set(key, activeUser, expire);
    }

}
