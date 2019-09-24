package com.itheima.test;

import com.itheima.health.utils.ValidateCodeUtils;
import org.junit.Test;

/**
 * @ClassName TestQiniu
 * @Description TODO
 * @Author ly
 * @Company 深圳黑马程序员
 * @Date 2019/9/12 12:22
 * @Version V1.0
 */
public class TestValidateCode {

    // 生成4位的验证码
    @Test
    public void validateCode4() throws Exception {
        Integer value = ValidateCodeUtils.generateValidateCode(4);
        System.out.println(value);
    }
    // 生成6位的验证码
    @Test
    public void validateCode6() throws Exception {
        Integer value = ValidateCodeUtils.generateValidateCode(6);
        System.out.println(value);
    }

}
