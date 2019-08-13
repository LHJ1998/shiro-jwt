package com.lhj.shiro.jwt.controller;

import com.lhj.shiro.jwt.dto.ResponseBean;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ErrorController {

    @GetMapping(value = "/401")
    public ResponseBean<String> _401() {
        return ResponseBean.fail("非法访问!!!");
    }

}
