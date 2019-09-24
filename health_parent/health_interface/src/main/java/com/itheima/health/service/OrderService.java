package com.itheima.health.service;

import com.itheima.health.entity.Result;

import java.util.Map;

public interface OrderService {

    Result saveOrder(Map map);

    Map<String,Object> findById(Integer id);
}
