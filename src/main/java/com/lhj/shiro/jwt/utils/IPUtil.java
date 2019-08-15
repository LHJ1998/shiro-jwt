package com.lhj.shiro.jwt.utils;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * 获取客户端的IP地址
 * @author LHJ
 */
@Slf4j
public class IPUtil {

    private static final String UNKNOWN = "unknown";

    private static final String LOCAL_HOST_IP = "127.0.0.1";

    private static final String HEADER_X_FORWARD_FOR = "x-forward-for";

    private static final String HEADER_PROXY_CLIENT_IP = "Proxy-Client-IP";

    private static final String HEADER_WL_PROXY_CLIENT_IP = "WL-Proxy-Client-IP";

    public static String getIpAddress(HttpServletRequest request){
        String ipAddress = null;
        try{
            ipAddress = request.getHeader(HEADER_X_FORWARD_FOR);
            if(StringUtils.isEmpty(ipAddress) || UNKNOWN.equalsIgnoreCase(ipAddress)){
                ipAddress = request.getHeader(HEADER_PROXY_CLIENT_IP);
            }
            if(StringUtils.isEmpty(ipAddress) || UNKNOWN.equalsIgnoreCase(ipAddress)){
                ipAddress = request.getHeader(HEADER_WL_PROXY_CLIENT_IP);
            }
            if(StringUtils.isEmpty(ipAddress) || UNKNOWN.equalsIgnoreCase(ipAddress)){
                ipAddress = request.getRemoteAddr();
                if(LOCAL_HOST_IP.equalsIgnoreCase(ipAddress)){
                    InetAddress inetAddress = null;
                    try{
                        inetAddress = InetAddress.getLocalHost();
                        ipAddress = inetAddress.getHostAddress();
                    } catch (UnknownHostException e) {
                        e.printStackTrace();
                        log.error("error on get local inetAddress, message: " + e.getMessage());
                    }
                }
            }
            if(!StringUtils.isEmpty(ipAddress) && ipAddress.length() > 15){
                //通过多个代理访问，第一个IP地址为真实地址
                if(ipAddress.indexOf(',') > 0){
                    ipAddress = ipAddress.substring(0, ipAddress.indexOf(','));
                }
            }
        } catch (Exception e){
            e.printStackTrace();
            ipAddress = "";
            log.error("error on get ipAddress, set ipAddress to empty");
        }
        return ipAddress;
    }

}
