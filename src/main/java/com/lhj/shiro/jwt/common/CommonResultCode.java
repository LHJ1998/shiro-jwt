package com.lhj.shiro.jwt.common;

/**
 * 定义放回状态码
 * @author LHJ
 */
public class CommonResultCode {

    /**
     * 成功
     */
    public static final Integer SUCCESS_CODE = 10041;

    /**
     * 系统错误
     */
    public static final Integer SYSTEM_ERROR = 50040;

    /**
     * 参数错误
     */
    public static final Integer PARAMETER_ERROR = 50046;

    /**
     * 操作失败
     */
    public static final Integer OPERATING_FAILED = 50061;

    /**
     * token过期失效
     */
    public static final Integer TOKEN_EXPIRED = 50067;

    /**
     * 身份验证失败
     */
    public static final Integer AUTHENTICATION_FAILED = 50070;

}
