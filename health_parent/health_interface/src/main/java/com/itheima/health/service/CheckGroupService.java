package com.itheima.health.service;

import com.itheima.health.entity.PageResult;
import com.itheima.health.pojo.CheckGroup;

import java.util.List;

public interface CheckGroupService {

    void add(CheckGroup checkGroup, Integer[] checkitemIds);

    PageResult findPage(String queryString, Integer currentPage, Integer pageSize);

    CheckGroup findById(Integer id);

    List<Integer> findCheckItemListByCheckGroupId(Integer id);

    void update(CheckGroup checkGroup, Integer[] checkitemIds);

    List<CheckGroup> findAll();
}
