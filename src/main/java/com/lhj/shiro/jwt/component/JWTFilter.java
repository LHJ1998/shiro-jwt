package com.lhj.shiro.jwt.component;

import com.lhj.shiro.jwt.config.ShiroProperties;
import com.lhj.shiro.jwt.utils.SpringContextUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.web.filter.authc.BasicHttpAuthenticationFilter;
import org.springframework.http.HttpStatus;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 过滤器处理流程
 * preHandle->isAccessAllowed->isLoginAttempt->executeLogin
 * @author LHJ
 */
@Slf4j
public class JWTFilter extends BasicHttpAuthenticationFilter {

    private final String AUTHORIZATION_HEADER = "Authorization";

    private AntPathMatcher pathMatcher = new AntPathMatcher();

    /**
     * 验证用户是否需要进行身份验证
     * 除了登录、注册外的不带token的，都需要进行身份验证
     * @param request
     * @param response
     * @return token为空就不需要否则需要
     */
    @Override
    protected boolean isLoginAttempt(ServletRequest request, ServletResponse response) {
        HttpServletRequest req = (HttpServletRequest) request;
        String token = req.getHeader(AUTHORIZATION_HEADER);
        return !StringUtils.isEmpty(token);
    }

    @Override
    protected boolean executeLogin(ServletRequest request, ServletResponse response) {
        HttpServletRequest req = (HttpServletRequest) request;
        String token = req.getHeader(AUTHORIZATION_HEADER);
        //封装为JWTToken
        JWTToken jwtToken = new JWTToken(token);
        //交给realm处理
        try{
            getSubject(request, response).login(jwtToken);
            return true;
        } catch (Exception e){
            log.error("error on execute login, message: " + e.getMessage());
            return false;
        }
        //如果没有抛出异常，说明身份验证通过，否则会走异常处理
    }

    @Override
    protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) {
        HttpServletRequest req = (HttpServletRequest) request;
        ShiroProperties shiroProperties = SpringContextUtil.getBean(ShiroProperties.class);
        //分割字符串过程中会按照每个分隔符进行分割，不忽略任何空白项
        String[] anonUrls = StringUtils.splitByWholeSeparatorPreserveAllTokens(shiroProperties.getAnonUrl(), ",");
        //如果访问的路径在匿名区，允许访问
        for(String url : anonUrls){
            if(pathMatcher.match(url, req.getRequestURI())){
                return true;
            }
        }
        /**
         * 废除的危险做法
         * 如果用户在认证以后修改了token，那么下次操作的时候需要从token中获取用户信息的时候就会出现问题
         */
        //已通过认证的，允许访问
//        if(getSubject(request, response).isAuthenticated()){
//            return true;
//        }
        if(isLoginAttempt(request, response) && executeLogin(request, response)){
            return true;
        }else{
            //未携带token或验证失败 禁止访问
            response401(request, response);
            return false;
        }
    }

    /**
     * 跨域支持
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    @Override
    protected boolean preHandle(ServletRequest request, ServletResponse response) throws Exception {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse resp = (HttpServletResponse) response;

        String[] allowOrigins = {
                "localhost:8080",
                "127.0.0.1:8080"
        };
        String origin = req.getHeader("Origin");
        boolean allow = false;
        for(String allowOrigin : allowOrigins){
            if(allowOrigin.equals(origin)){
                allow = true;
                break;
            }
        }

        resp.setHeader("Access-control-Allow-Origin", allow ? origin : "*");
        resp.setHeader("Access-Control-Allow-Methods", "GET,POST,OPTIONS,PUT,DELETE");
        resp.setHeader("Access-Control-Allow-Headers", req.getHeader("Access-Control-Request-Headers"));
        // 跨域时会首先发送一个option请求，这里我们给option请求直接返回正常状态
        if (req.getMethod().equals(RequestMethod.OPTIONS.name())) {
            resp.setStatus(HttpStatus.OK.value());
            return false;
        }
        return super.preHandle(request, response);
    }

    private void response401(ServletRequest request, ServletResponse response){
        try{
            HttpServletResponse resp = (HttpServletResponse) response;
            resp.sendRedirect("/v1/api/401");
        } catch (IOException e) {
            log.error(e.getLocalizedMessage());
        }
    }

}
