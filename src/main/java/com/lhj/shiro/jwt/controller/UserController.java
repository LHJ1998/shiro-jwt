package com.lhj.shiro.jwt.controller;

import com.lhj.shiro.jwt.dto.ResponseBean;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/user")
public class UserController {

    @GetMapping(value = "/test")
    public ResponseBean<String> test(){
        return ResponseBean.success("success !!!");
    }

}
