package com.lhj.shiro.jwt.controller;

import com.lhj.shiro.jwt.dto.ResponseBean;
import com.lhj.shiro.jwt.pojo.User;
import com.lhj.shiro.jwt.service.UserService;
import com.lhj.shiro.jwt.utils.EncryptUtil;
import com.lhj.shiro.jwt.utils.IPUtil;
import com.lhj.shiro.jwt.utils.JWTUtil;
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

    @Autowired
    private UserService userService;

    @PostMapping(value = "/login")
    public ResponseBean<String> login(@RequestBody User user, HttpServletRequest request){
        User dbUser = userService.getUserByUsername(user.getUsername());
        if(dbUser != null) {
            String secret = EncryptUtil.md5Encrypt(user.getPassword(), dbUser.getSalt(), iteration);
            if(dbUser.getPassword().equals(secret)){
                Map<String, String> claims = new HashMap<>();
                claims.put(JWTUtil.USERNAME_CLAIMS, dbUser.getUsername());
                claims.put(JWTUtil.CLIENT_IP_ADDRESS_CLAIMS, IPUtil.getIpAddress(request));
                return ResponseBean.success("登录成功", EncryptUtil.aesEncrypt(JWTUtil.sign(claims, secret)));
            }
        }
        return ResponseBean.fail("用户名或密码错误");
    }

}
