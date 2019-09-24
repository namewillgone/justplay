package com.itheima.health.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.itheima.health.dao.CheckGroupDao;
import com.itheima.health.entity.PageResult;
import com.itheima.health.pojo.CheckGroup;
import com.itheima.health.service.CheckGroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

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
@Service(interfaceClass = CheckGroupService.class)
@Transactional
public class CheckGroupServiceImpl implements CheckGroupService {

    @Autowired
    private CheckGroupDao checkGroupDao;


    @Override
    public void add(CheckGroup checkGroup, Integer[] checkitemIds) {
        /**1：组织基本信息的表单内容，向检查组的表中插入数据
                同时获取检查组的id
        */
        checkGroupDao.add(checkGroup);
        /**
         * 2：加上页面获取到的检查项id的数组（遍历），向检查组和检查项的中间表中插入数据
         */
        setCheckGroupAndCheckItem(checkGroup.getId(),checkitemIds);

    }

    @Override
    public PageResult findPage(String queryString, Integer currentPage, Integer pageSize) {
        // 1：初始化
        PageHelper.startPage(currentPage,pageSize);
        // 2：定义条件查询
        List<CheckGroup> list = checkGroupDao.findPageByCondition(queryString);
        // 3：处理成PageHelper支持的javabean
        PageInfo<CheckGroup> pageInfo = new PageInfo<>(list);
        return new PageResult(pageInfo.getTotal(),pageInfo.getList());
    }

    @Override
    public CheckGroup findById(Integer id) {
        return checkGroupDao.findById(id);
    }

    @Override
    public List<Integer> findCheckItemListByCheckGroupId(Integer id) {
        return checkGroupDao.findCheckItemListByCheckGroupId(id);
    }

    @Override
    public void update(CheckGroup checkGroup, Integer[] checkitemIds) {
        // 1：传递表单基本信息的内容，更新检查组的表
        checkGroupDao.update(checkGroup);
        // 2：使用检查组id作为条件，先删除之前检查组和检查项中间表的数据
        checkGroupDao.deleteCheckGroupAndCheckItemByCheckGroupId(checkGroup.getId());
        // 3：按照新建立的检查组对应检查项的信息，插入中间表的数据
        setCheckGroupAndCheckItem(checkGroup.getId(),checkitemIds);
    }

    @Override
    public List<CheckGroup> findAll() {
        return checkGroupDao.findAll();
    }

    // 表示向检查组和检查项的中间表中插入数据
    private void setCheckGroupAndCheckItem(Integer checkGroupId, Integer[] checkitemIds) {
        if(checkitemIds!=null && checkitemIds.length>0){
            for (Integer checkItemId : checkitemIds) {
                // 第一种方案Map
                Map<String,Integer> map = new HashMap<>();
                map.put("check_group_id",checkGroupId);
                map.put("check_item_id",checkItemId);
                checkGroupDao.addCheckGroupAndCheckItemMap(map);

                // 第二种方案@Param
                // checkGroupDao.addCheckGroupAndCheckItem(checkGroupId,checkItemId);
            }
        }

    }
}
