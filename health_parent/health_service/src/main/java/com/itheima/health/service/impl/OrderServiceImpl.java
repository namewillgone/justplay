package com.itheima.health.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.itheima.health.constants.MessageConstant;
import com.itheima.health.dao.MemberDao;
import com.itheima.health.dao.OrderDao;
import com.itheima.health.dao.OrderSettingDao;
import com.itheima.health.entity.Result;
import com.itheima.health.pojo.Member;
import com.itheima.health.pojo.Order;
import com.itheima.health.pojo.OrderSetting;
import com.itheima.health.service.OrderService;
import com.itheima.health.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @ClassName CheckItemServiceImpl
 * @Description TODO
 * @Author ly
 * @Company 深圳黑马程序员
 * @Date 2019/9/11 9:55
 * @Version V1.0
 */
@Service(interfaceClass = OrderService.class)
@Transactional
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderDao orderDao;

    @Autowired
    private OrderSettingDao orderSettingDao;

    @Autowired
    private MemberDao memberDao;

    @Override
    public Result saveOrder(Map map) {
        try {
            /**
             *  （1）使用预约设置时间，查询预约设置表，返回OrderSetting对象
             如果OrderSetting对象==null，此时提示：“所选日期不能进行体检预约”
             如果OrderSetting获取number（最多预约人数）和reservations（已经预约人数）
             如果reservations大于等于number，此时提示：“预约人数已满，不能预约”
             */
            String orderDate = (String)map.get("orderDate");
            // 将字符串转换成日期
            Date date = DateUtils.parseString2Date(orderDate);
            // 使用预约设置时间，查询预约设置表，返回OrderSetting对象
            OrderSetting orderSetting = orderSettingDao.findOrderSettingByOrderDate(date);
            if(orderSetting==null){
                return new Result(false, MessageConstant.SELECTED_DATE_CANNOT_ORDER);
            }
            int number = orderSetting.getNumber();
            int reservations = orderSetting.getReservations();
            if(reservations>=number){
                return new Result(false, MessageConstant.ORDER_FULL);
            }
            /**
             * （2）使用手机号查询会员表，返回Member对象
             如果Member对象!=null，表示是会员
             使用会员id、预约时间、套餐id，查询订单表，判断当前会员是否是重复预约
             如果Member对象==null，表示不是会员，此时注册会员（新增数据，添加会员表）
             */
            Member member = memberDao.findMemberByTelephone((String)map.get("telephone"));
            // 是会员
            if(member!=null){
                // 使用会员id、预约时间、套餐id，查询订单表，判断当前会员是否是重复预约
                Order order = new Order(member.getId(),date,null,null,Integer.parseInt((String)map.get("setmealId")));
                List<Order> list = orderDao.findOrderListByCondition(order);
                if(list!=null && list.size()>0){
                    return new Result(false, MessageConstant.HAS_ORDERED);
                }
            }
            // 不是会员，注册会员
            else{
                member = new Member();
                member.setPhoneNumber((String)map.get("telephone"));
                member.setIdCard((String)map.get("idCard"));
                member.setRegTime(new Date()); // 注册会员时间，当前时间
                member.setSex((String)map.get("sex"));
                member.setName((String)map.get("name"));
                memberDao.add(member);
            }
            // （3）可以预约，更新预约设置表reservations字段，+1
            orderSetting.setReservations(orderSetting.getReservations()+1);
            orderSettingDao.updateReservationsByOrderDate(orderSetting);
            // （4）可以预约，新增订单表，保存对应的订单数据
            // 组织订单数据
            Order order = new Order(member.getId(),date,(String)map.get("orderType"),Order.ORDERSTATUS_NO,Integer.parseInt((String)map.get("setmealId")));
            orderDao.add(order);
            // 成功
            return new Result(true,MessageConstant.ORDER_SUCCESS,order); // 包括订单id
        } catch (Exception e) {
            e.printStackTrace();
            // 失败
            return new Result(false,MessageConstant.ORDER_FAIL);
        }
    }

    @Override
    public Map<String, Object> findById(Integer id) {
        Map<String, Object> map = orderDao.findById(id);
        if(map!=null){
            Date date = (Date)map.get("orderDate");
            // 将日期类型转换成字符串
            try {
                String sDate = DateUtils.parseDate2String(date);
                map.put("orderDate",sDate);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return map;
    }
}
