package com.itheima.health.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.health.constants.MessageConstant;
import com.itheima.health.constants.RedisMessageConstant;
import com.itheima.health.entity.Result;
import com.itheima.health.pojo.Order;
import com.itheima.health.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import redis.clients.jedis.JedisPool;

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
@RequestMapping(value = "/order")
public class OrderMobileController {

    @Reference
    OrderService orderService;

    @Autowired
    JedisPool jedisPool;

    // 提交预约信息，保存预约信息
    @RequestMapping(value = "/submit")
    public Result submit(@RequestBody Map map){
        // * 获取手机号和验证码（4位）
        String telephone = (String)map.get("telephone");
        String validateCode = (String)map.get("validateCode");
        // * 使用手机号，从redis中获取当前手机号存放的验证码
        String redisInCode = jedisPool.getResource().get(telephone + RedisMessageConstant.SENDTYPE_ORDER);
        // * 使用输入的验证码和redis中存放的验证码进行比对，比对不成功，验证码输入有误
        if(redisInCode==null || !redisInCode.equals(validateCode)){
            return new Result(false, MessageConstant.VALIDATECODE_ERROR);
        }
        // 此时验证码没有问题，处理预约信息保存
        Result result = null;
        map.put("orderType", Order.ORDERTYPE_WEIXIN);
        result = orderService.saveOrder(map);

//        if(result.isFlag()){
//            //预约成功，发送短信通知，短信通知内容可以是“预约时间”，“预约人”，“预约地点”，“预约事项”等信息。
//            String orderDate = (String) map.get("orderDate");
//            try {
//                SMSUtils.sendShortMessage(telephone,orderDate);
//            } catch (ClientException e) {
//                e.printStackTrace();
//            }
//        }
        return result;
    }

    // 订单详情页面，使用订单id，查询订单的详情
    @RequestMapping(value = "/findById")
    public Result submit(Integer id){
        Map<String,Object> map = orderService.findById(id);
        if(map!=null && map.size()>0){
            return new Result(true,MessageConstant.QUERY_ORDER_SUCCESS,map);
        }else{
            return new Result(false,MessageConstant.QUERY_ORDER_FAIL);
        }
    }
}
