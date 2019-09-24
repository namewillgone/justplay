package com.itheima.health.dao;

import com.itheima.health.pojo.CheckGroup;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface CheckGroupDao {

    void add(CheckGroup checkGroup);

    void addCheckGroupAndCheckItem(@Param(value = "check_group_id") Integer checkGroupId, @Param(value = "check_item_id") Integer checkItemId);

    void addCheckGroupAndCheckItemMap(Map<String, Integer> map);

    List<CheckGroup> findPageByCondition(String queryString);

    CheckGroup findById(Integer id);

    List<Integer> findCheckItemListByCheckGroupId(Integer id);

    void update(CheckGroup checkGroup);

    void deleteCheckGroupAndCheckItemByCheckGroupId(Integer id);

    List<CheckGroup> findAll();

    List<CheckGroup> findCheckGroupListBySetmealId(Integer id);
}
