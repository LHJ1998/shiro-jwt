package com.lhj.shiro.jwt.component;

import com.lhj.shiro.jwt.bo.ActiveUser;
import com.lhj.shiro.jwt.pojo.User;
import com.lhj.shiro.jwt.service.RedisService;
import com.lhj.shiro.jwt.service.UserService;
import com.lhj.shiro.jwt.utils.EncryptUtil;
import com.lhj.shiro.jwt.utils.HttpContextUtil;
import com.lhj.shiro.jwt.utils.IPUtil;
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
    private RedisService redisService;

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
        //从redis中获取，这种办法只能判断当前用户没有被踢下线，并不能保证token仍然有效
        //键是登录IP+ - +md5加密后的token  --> 127.0.0.1-dafsfsdfasdfasf
        String ip = IPUtil.getIpAddress(HttpContextUtil.getHttpServletRequest());
        String key = ip + "-" + EncryptUtil.md5Encrypt(token);
        ActiveUser activeUser = (ActiveUser) redisService.get(key);
        if(activeUser == null){
            //用户已经被踢下线
            throw new AuthenticationException("用户已下线");
        }

        //JWTToken中的principal和credentials都是经过AES加密后的token，因此之后需要使用加密后的token进行验证
        //从token里面获取数据需要的是解密后的token
        String decryptToken = EncryptUtil.aesDecrypt(token);
        String username = JWTUtil.getUsername(decryptToken);
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
        claims.put(JWTUtil.CLIENT_IP_ADDRESS_CLAIMS, IPUtil.getIpAddress(HttpContextUtil.getHttpServletRequest()));
        if(!JWTUtil.verify(decryptToken, claims, user.getPassword())) {
            log.error("error on authentication user with incorrect credentials: 【" + username + "】");
            throw new IncorrectCredentialsException("credentials error");
        }
        return new SimpleAuthenticationInfo(token, token, getName());
    }
}