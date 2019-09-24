package com.itheima.health.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.health.constants.MessageConstant;
import com.itheima.health.constants.RedisMessageConstant;
import com.itheima.health.entity.Result;
import com.itheima.health.pojo.Member;
import com.itheima.health.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import redis.clients.jedis.JedisPool;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * @ClassName SetmealMobileController
 * @Description TODO
 * @Author ly
 * @Company 深圳黑马程序员
 * @Date 2019/9/16 9:51
 * @Version V1.0
 */
@RestController
@RequestMapping(value = "/login")
public class LoginMobileController {

    @Reference
    MemberService memberService;

    @Autowired
    JedisPool jedisPool;

    // 登录校验
    @RequestMapping(value = "/check")
    public Result check(@RequestBody Map map, HttpServletResponse response){
        // * 校验（输入的验证码和redis中的验证码）
        // 输入的手机号
        String telephone = (String)map.get("telephone");
        // 输入的验证码
        String validateCode = (String)map.get("validateCode");
        String codeInRedis = jedisPool.getResource().get(telephone + RedisMessageConstant.SENDTYPE_LOGIN);
        if(codeInRedis==null || !codeInRedis.equals(validateCode)){
            return new Result(false,MessageConstant.VALIDATECODE_ERROR);
        }
        //* 使用手机号查询会员
        Member member = memberService.findMemberByTelephone(telephone);
        //* 如果不是会员，系统自动注册会员
        if(member==null){
            member = new Member();
            member.setPhoneNumber(telephone);
            memberService.add(member);
        }
        // 保存用户的登录状态
        Cookie cookie = new Cookie("login_member_telephone",telephone);
        cookie.setMaxAge(30*24*60*60); // 30天
        cookie.setPath("/"); // cookie的有效路径
        response.addCookie(cookie);
        return new Result(true,MessageConstant.LOGIN_SUCCESS);
    }

}
