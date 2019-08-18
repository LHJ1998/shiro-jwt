package com.lhj.shiro.jwt.utils;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.lionsoul.ip2region.DataBlock;
import org.lionsoul.ip2region.DbConfig;
import org.lionsoul.ip2region.DbMakerConfigException;
import org.lionsoul.ip2region.DbSearcher;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * 获取地址信息的工具类
 * @author LHJ
 */
@Slf4j
public class AddressUtil {

    public static String getAddress(String ip){
        if(StringUtils.isEmpty(ip)){
            log.error("error on use invalid ip to find region, ip -> " + ip);
            return "";
        }
        DbSearcher dbSearcher = null;
        try{
            File file = ResourceUtils.getFile("classpath:ip2region.db");
            DbConfig dbConfig = new DbConfig();
            dbSearcher = new DbSearcher(dbConfig, file.getPath());
            Method method = dbSearcher.getClass().getMethod("btreeSearch", String.class);
            DataBlock dataBlock = (DataBlock) method.invoke(dbSearcher, ip);
            return dataBlock.getRegion();
        } catch (FileNotFoundException e) {
            log.error("error on read file from resource, can not find file ip2region.db");
            return "";
        } catch (DbMakerConfigException e) {
            log.error("error on create DbConfig, message: " + e.getMessage());
            e.printStackTrace();
            return "";
        } catch (NoSuchMethodException e) {
            log.error("error on use method: btreeSearch to find region, No Such Method!!!");
            return "";
        } catch (IllegalAccessException | InvocationTargetException e) {
            log.error("error on use reflect method to find region, message: " + e.getMessage());
            e.printStackTrace();
            return "";
        }
    }

}
