package com.itheima.health.dao;

import com.github.pagehelper.Page;
import com.itheima.health.pojo.CheckItem;

import java.util.List;

public interface CheckItemDao {
    void add(CheckItem checkItem);

    Page<CheckItem> findPageByCondition(String queryString);

    void deleteById(Integer id);

    long findCountByCheckItemId(Integer id);

    CheckItem findById(Integer id);

    void update(CheckItem checkItem);

    List<CheckItem> findAll();

    List<CheckItem> findCheckItemListByCheckGroupId(Integer checkGroupId);
}
