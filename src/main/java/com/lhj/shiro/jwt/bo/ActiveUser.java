package com.lhj.shiro.jwt.bo;

import com.lhj.shiro.jwt.utils.EncryptUtil;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * Redis中存储的在线用户信息
 * @author LHJ
 */
@Data
public class ActiveUser implements Serializable {

    public ActiveUser(){
        this.id = EncryptUtil.generateRandomNumber();
        this.loginTime = new Date();
    }

    private String id;

    private String username;

    private String ip;

    private String token;

    private Date loginTime;

    private String loginAddress;

}
