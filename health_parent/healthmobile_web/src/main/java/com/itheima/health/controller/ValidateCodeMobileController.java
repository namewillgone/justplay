package com.itheima.health.controller;

import com.itheima.health.constants.MessageConstant;
import com.itheima.health.constants.RedisMessageConstant;
import com.itheima.health.entity.Result;
import com.itheima.health.utils.ValidateCodeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import redis.clients.jedis.JedisPool;

/**
 * @ClassName SetmealMobileController
 * @Description TODO
 * @Author ly
 * @Company 深圳黑马程序员
 * @Date 2019/9/16 9:51
 * @Version V1.0
 */
@RestController
@RequestMapping(value = "/validateCode")
public class ValidateCodeMobileController {

    @Autowired
    JedisPool jedisPool;

    // 在在线预约中，发送验证码
    @RequestMapping(value = "/send4Order")
    public Result send4Order(String telephone){
        try {
            // *（1）生成验证码
            Integer code = ValidateCodeUtils.generateValidateCode(4);
            // *（2）向对应 输入的手机号发送验证码
            //SMSUtils.sendShortMessage(telephone,code.toString());
            System.out.println("验证码："+code.toString());
            // *（3）将生成的验证码存放redis中，用于对页面输入的验证码进行比对校验
            jedisPool.getResource().setex(telephone+ RedisMessageConstant.SENDTYPE_ORDER,5*60,code.toString());
            return new Result(true,MessageConstant.SEND_VALIDATECODE_SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,MessageConstant.SEND_VALIDATECODE_FAIL);
        }
    }


    // 在在线预约中，发送验证码
    @RequestMapping(value = "/send4Login")
    public Result send4Login(String telephone){
        try {
            // *（1）生成验证码
            Integer code = ValidateCodeUtils.generateValidateCode(4);
            // *（2）向对应 输入的手机号发送验证码
            //SMSUtils.sendShortMessage(telephone,code.toString());
            System.out.println("验证码："+code.toString());
            // *（3）将生成的验证码存放redis中，用于对页面输入的验证码进行比对校验
            jedisPool.getResource().setex(telephone+ RedisMessageConstant.SENDTYPE_LOGIN,5*60,code.toString());
            return new Result(true,MessageConstant.SEND_VALIDATECODE_SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,MessageConstant.SEND_VALIDATECODE_FAIL);
        }
    }
}
