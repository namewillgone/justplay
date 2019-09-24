package com.itheima.health.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.itheima.health.dao.OrderSettingDao;
import com.itheima.health.pojo.OrderSetting;
import com.itheima.health.service.OrderSettingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
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
@Service(interfaceClass = OrderSettingService.class)
@Transactional
public class OrderSettingServiceImpl implements OrderSettingService {

    @Autowired
    private OrderSettingDao orderSettingDao;


    @Override
    public void addList(List<OrderSetting> orderSettingsList) {
        if(orderSettingsList!=null && orderSettingsList.size()>0){
            for (OrderSetting orderSetting : orderSettingsList) {
                // 使用预约设置的时间，查询预约设置表，判断数据是否存在
                long count = orderSettingDao.findCountByOrderDate(orderSetting.getOrderDate());
                // 有数据，更新
                if(count>0){
                    // 根据预约设置的时间，更新最多预约的人数字段
                    orderSettingDao.update(orderSetting);
                }
                // 没有数据，新增
                else{
                    // 保存
                    orderSettingDao.add(orderSetting);
                }

            }
        }
    }

    @Override
    public List<Map<String, Object>> findOrderSettingByOrderDateMonth(String date) {
        String beginDate = date+"-1"; // 2019-9-1
        String endDate = date+"-31"; // 2019-9-31
        Map<String,Object> paramsMap = new HashMap<>();
        paramsMap.put("beginDate",beginDate);
        paramsMap.put("endDate",endDate);
        List<OrderSetting> list = orderSettingDao.findOrderSettingByOrderDateMonth(paramsMap);
        // 组织需要数据格式
        /**
         * [
         *      {data:1,number:120,reservations:1},
         *      {data:1,number:120,reservations:1},
         *      {data:1,number:120,reservations:1}
         * ]
         */
        List<Map<String, Object>> listMap = new ArrayList<>();
        if(list!=null && list.size()>0){
            for (OrderSetting orderSetting : list) {
                Map<String, Object> map = new HashMap<>();
                map.put("date",orderSetting.getOrderDate().getDate());
                map.put("number",orderSetting.getNumber());
                map.put("reservations",orderSetting.getReservations());
                listMap.add(map);
            }
        }
        return listMap;
    }

    @Override
    public void setOrderSetting(OrderSetting orderSetting) {
        // 使用预约设置的时间，查询预约设置表，判断数据是否存在
        long count = orderSettingDao.findCountByOrderDate(orderSetting.getOrderDate());
        // 有数据，更新
        if(count>0){
            // 根据预约设置的时间，更新最多预约的人数字段
            orderSettingDao.update(orderSetting);
        }
        // 没有数据，新增
        else{
            // 保存
            orderSettingDao.add(orderSetting);
        }
    }
}
