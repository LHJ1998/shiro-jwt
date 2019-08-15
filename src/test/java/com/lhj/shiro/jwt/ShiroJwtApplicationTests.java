package com.lhj.shiro.jwt;

import com.lhj.shiro.jwt.utils.EncryptUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ShiroJwtApplicationTests {

    @Test
    public void testForEncryptUtil() {
        String str = "abcdefg";
        String encrypt = EncryptUtil.aesEncrypt(str);
        System.out.println(encrypt);
        System.out.println(EncryptUtil.aesDecrypt(encrypt));
    }

}
