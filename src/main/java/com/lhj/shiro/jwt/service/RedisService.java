package com.lhj.shiro.jwt.service;

import cn.hutool.json.JSONUtil;
import com.lhj.shiro.jwt.service.RedisService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

/**
 * 一些操作redis的简单方法
 * @author LHJ
 */
@Service
@Slf4j
public class RedisService {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    public boolean set(String key, Object value) {
        try{
            redisTemplate.opsForValue().set(key, value);
            return true;
        } catch (Exception e){
            e.printStackTrace();
            log.error("error on set value to redis，【" + key + "】-【" + JSONUtil.toJsonStr(value) + "】，message: " + e.getMessage());
            return false;
        }
    }

    public boolean set(String key, Object value, long expire) {
        try{
            if(expire > 0){
                redisTemplate.opsForValue().set(key, value, expire, TimeUnit.SECONDS);
            }else{
                redisTemplate.opsForValue().set(key, value);
            }
            return true;
        } catch (Exception e){
            e.printStackTrace();
            log.error("error on set value to redis，【" + key + "】-【" + JSONUtil.toJsonStr(value) + "】，message: " + e.getMessage());
            return false;
        }
    }

    public Object get(String key) {
        try{
            return StringUtils.isEmpty(key) ? null : redisTemplate.opsForValue().get(key);
        } catch (Exception e){
            e.printStackTrace();
            log.error("error on get value from redis, key: " + key);
            return null;
        }
    }

    public boolean del(String key){
        try{
            redisTemplate.delete(key);
            return true;
        }catch (Exception e){
            e.printStackTrace();
            log.error("error on delete key from redis, 【key】: " + key + ", message: " + e.getMessage());
            return false;
        }
    }
}
