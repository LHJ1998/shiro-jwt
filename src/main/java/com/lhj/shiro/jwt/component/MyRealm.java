package com.lhj.shiro.jwt.component;

import com.lhj.shiro.jwt.pojo.User;
import com.lhj.shiro.jwt.service.UserService;
import com.lhj.shiro.jwt.utils.JWTUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.Map;

@Slf4j
public class MyRealm extends AuthorizingRealm {

    private UserService userService;

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    /**
     * 必须重写此方法，否则不能使用自定义的token
     * @param token
     * @return
     */
    @Override
    public boolean supports(AuthenticationToken token) {
        return token instanceof JWTToken;
    }

    /**
     * 权限配置
     * @param principalCollection
     * @return
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        return null;
    }

    /**
     * 身份验证
     * @param authenticationToken
     * @return
     * @throws AuthenticationException
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        String token = (String) authenticationToken.getPrincipal();
        String username = JWTUtil.getUsername(token);
        if(StringUtils.isEmpty(username)) {
            log.error("error on authentication user with invalid token --> can not get username from token");
            throw new AuthenticationException("token invalid");
        }
        User user = userService.getUserByUsername(username);
        if(user == null){
            log.error("error on authentication user with unknown account: 【" + username + "】");
            throw new UnknownAccountException("unknown account");
        }
        Map<String, String> claims = new HashMap<>();
        claims.put(JWTUtil.USERNAME_CLAIMS, username);
        if(!JWTUtil.verify(token, claims, user.getPassword())) {
            log.error("error on authentication user with incorrect credentials: 【" + username + "】");
            throw new IncorrectCredentialsException("credentials error");
        }
        return new SimpleAuthenticationInfo(token, token, getName());
    }
}
