package com.lhj.shiro.jwt.component;

import com.auth0.jwt.exceptions.TokenExpiredException;
import com.lhj.shiro.jwt.common.CommonResultCode;
import com.lhj.shiro.jwt.dto.ResponseBean;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 全局异常处理
 * @author LHJ
 */
@ControllerAdvice
@ResponseBody
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(value = UnknownAccountException.class)
    public ResponseBean unKnownAccount(Exception e){
        log.error("error on authentication, unknown account, message: " + e.getMessage());
        return ResponseBean.fail(CommonResultCode.AUTHENTICATION_FAILED, "用户名或密码错误");
    }

    @ExceptionHandler(value = IncorrectCredentialsException.class)
    public ResponseBean incorrectCredentials(Exception e){
        log.error("error on authentication, incorrect credentials, message: " + e.getMessage());
        return ResponseBean.fail(CommonResultCode.AUTHENTICATION_FAILED, "用户名或密码错误");
    }

    @ExceptionHandler(value = AuthenticationException.class)
    public ResponseBean authenticationFailed(Exception e){
        log.error("error on authentication, unknown error, message: " + e.getMessage());
        return ResponseBean.fail(CommonResultCode.AUTHENTICATION_FAILED, e.getMessage());
    }

    @ExceptionHandler(value = TokenExpiredException.class)
    public ResponseBean tokenExpired(Exception e){
        log.error("error on verify token, token expired, message: " + e.getMessage());
        return ResponseBean.fail(CommonResultCode.TOKEN_EXPIRED, "token失效");
    }

    @ExceptionHandler(value = Exception.class)
    public ResponseBean exception(Exception e){
        log.error("error !!!");
        return ResponseBean.fail(CommonResultCode.SYSTEM_ERROR, "???");
    }

}
