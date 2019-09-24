package com.itheima.health.service;

import com.itheima.health.pojo.OrderSetting;

import java.util.List;
import java.util.Map;

public interface OrderSettingService {

    void addList(List<OrderSetting> orderSettingsList);

    List<Map<String,Object>> findOrderSettingByOrderDateMonth(String date);

    void setOrderSetting(OrderSetting orderSetting);
}
