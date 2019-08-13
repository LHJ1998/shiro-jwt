package com.lhj.shiro.jwt.controller;

import com.lhj.shiro.jwt.dto.ResponseBean;
import com.lhj.shiro.jwt.pojo.User;
import com.lhj.shiro.jwt.service.UserService;
import com.lhj.shiro.jwt.utils.EncryptUtil;
import com.lhj.shiro.jwt.utils.JWTUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping(value = "/user")
public class UserController {

    @Value("${shiro.iteration}")
    private int iteration;

    @Autowired
    private UserService userService;

    @PostMapping(value = "/login")
    public ResponseBean<String> login(@RequestBody User user){
        User dbUser = userService.getUserByUsername(user.getUsername());
        if(dbUser != null) {
            String secret = EncryptUtil.md5Encrypt(user.getPassword(), dbUser.getSalt(), iteration);
            if(dbUser.getPassword().equals(secret)){
                Map<String, String> claims = new HashMap<>();
                claims.put(JWTUtil.USERNAME_CLAIMS, dbUser.getUsername());
                return ResponseBean.success("登录成功", JWTUtil.sign(claims, secret));
            }
        }
        return ResponseBean.fail("用户名或密码错误");
    }

    @PostMapping(value = "/register")
    public ResponseBean<String> register(@RequestBody User user){
        String salt = EncryptUtil.generateRandomNumber();
        String secret = EncryptUtil.md5Encrypt(user.getPassword(), salt, iteration);
        user.setPassword(secret);
        user.setSalt(salt);
        boolean success = userService.saveUser(user);
        if(success){
            return ResponseBean.success("注册成功");
        }else{
            return ResponseBean.fail("注册失败");
        }
    }

    @GetMapping(value = "/test")
    public ResponseBean<String> test(){
        return ResponseBean.success("success !!!");
    }

}
