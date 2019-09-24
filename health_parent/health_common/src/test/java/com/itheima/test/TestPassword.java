package com.itheima.test;

import com.itheima.health.utils.MD5Utils;
import org.junit.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 * @ClassName TestPassword
 * @Description TODO
 * @Author ly
 * @Company 深圳黑马程序员
 * @Date 2019/9/19 12:19
 * @Version V1.0
 */
public class TestPassword {

    @Test
    public void md5(){
        String s1 = MD5Utils.md5("123");
        String s2 = MD5Utils.md5("123");
        System.out.println(s1);
        System.out.println(s2);

        String s3 = MD5Utils.md5("123zhangsan");
        System.out.println(s3);
    }

    @Test
    public void bcrypt(){
        BCryptPasswordEncoder bc = new BCryptPasswordEncoder();
        String s1 = bc.encode("123");
        String s2 = bc.encode("123");
        System.out.println(s1);
        System.out.println(s2);

        // 匹配
        boolean flag = bc.matches("1234", "$2a$10$hHNj2NFeGwazDR/rVf0UeuxQRQOsPqUtPTDDVGIoAPEnkkdIbPMsq");
        System.out.println(flag);
    }
}
