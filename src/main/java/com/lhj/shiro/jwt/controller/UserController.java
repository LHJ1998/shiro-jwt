package com.lhj.shiro.jwt.controller;

import com.lhj.shiro.jwt.dto.ResponseBean;
import com.lhj.shiro.jwt.pojo.User;
import com.lhj.shiro.jwt.service.RedisService;
import com.lhj.shiro.jwt.service.UserService;
import com.lhj.shiro.jwt.utils.EncryptUtil;
import com.lhj.shiro.jwt.utils.IPUtil;
import com.lhj.shiro.jwt.utils.JWTUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping(value = "/user")
public class UserController {

    @Value("${shiro.iteration}")
    private int iteration;

    @Autowired
    private UserService userService;

    @Autowired
    private RedisService redisService;

    @PostMapping(value = "")
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

    @GetMapping(value = "/outline")
    public ResponseBean<String> outline(HttpServletRequest request){
        String token = request.getHeader("Authorization");
        String key = IPUtil.getIpAddress(request) + "-" + EncryptUtil.md5Encrypt(token);
        redisService.del(key);
        return ResponseBean.success("outline !!!");
    }

}
