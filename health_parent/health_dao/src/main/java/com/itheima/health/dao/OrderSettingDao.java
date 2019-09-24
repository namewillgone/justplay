package com.itheima.health.dao;

import com.itheima.health.pojo.OrderSetting;

import java.util.Date;
import java.util.List;
import java.util.Map;

public interface OrderSettingDao {

    void add(OrderSetting orderSetting);

    long findCountByOrderDate(Date orderDate);

    void update(OrderSetting orderSetting);

    List<OrderSetting> findOrderSettingByOrderDateMonth(Map<String, Object> paramsMap);

    OrderSetting findOrderSettingByOrderDate(Date date);

    void updateReservationsByOrderDate(OrderSetting orderSetting);
}
