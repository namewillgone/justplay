package com.itheima.health.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.itheima.health.constants.RedisConstant;
import com.itheima.health.dao.SetmealDao;
import com.itheima.health.entity.PageResult;
import com.itheima.health.pojo.Setmeal;
import com.itheima.health.service.SetmealService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import redis.clients.jedis.JedisPool;

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
@Service(interfaceClass = SetmealService.class)
@Transactional
public class SetmealServiceImpl implements SetmealService {

    @Autowired
    private SetmealDao setmealDao;

    @Autowired
    JedisPool jedisPool;

    @Override
    public void add(Setmeal setmeal, Integer[] checkgroupIds) {
        //1：保存套餐基本信息，向t_setmeal中插入1条数据
        setmealDao.add(setmeal);
        //2：保存套餐和检查组的中间表，向t_setmeal+checkgroup表中插入多条数据
        setSetmealAndCheckGroup(setmeal.getId(),checkgroupIds);
        // 保存套餐的时候，将图片的名称存放到redis的集合中，集合的名称是（setmealpicdbresource）
        jedisPool.getResource().sadd(RedisConstant.SETMEAL_PIC_DB_RESOURCES,setmeal.getImg());

    }

    @Override
    public PageResult findPage(String queryString, Integer currentPage, Integer pageSize) {
        PageHelper.startPage(currentPage,pageSize);
        Page<Setmeal> page = setmealDao.findPageByCondition(queryString);
        return new PageResult(page.getTotal(),page.getResult());
    }

    @Override
    public List<Setmeal> findAll() {
        return setmealDao.findAll();
    }

    @Override
    public Setmeal findById(Integer id) {
        // 1：使用套餐id，查询套餐的详情
        // SELECT * FROM t_setmeal WHERE id = 3
        // 2：使用套餐id，查询当前套餐具有的检查组的集合
        // SELECT * FROM t_checkgroup WHERE id IN (SELECT checkgroup_id FROM t_setmeal_checkgroup WHERE setmeal_id = 3)
        // 3：使用检查组的id，查询当前检查组具有的检查项的集合
        // SELECT * FROM t_checkitem WHERE id IN (SELECT checkitem_id FROM t_checkgroup_checkitem WHERE checkgroup_id = 3)
        // SELECT * FROM t_checkitem WHERE id IN (SELECT checkitem_id FROM t_checkgroup_checkitem WHERE checkgroup_id = 5)

        Setmeal setmeal = setmealDao.findById(id);
//        List<CheckGroup> checkGroupList = checkGroupDao.findCheckGroupListBySetmealId(id);
//        setmeal.setCheckGroups(checkGroupList);
//        for (CheckGroup checkGroup : checkGroupList) {
//            List<CheckItem> checkItemList = checkItemDao.findCheckItemListByCheckGroupId(checkGroup.getId());
//            checkGroup.setCheckItems(checkItemList);
//        }
        return setmeal;
    }

    @Override
    public List<Map<String, Object>> findSetmealCount() {
        return setmealDao.findSetmealCount();
    }

    // 保存套餐和检查组的中间表，向t_setmeal+checkgroup表中插入多条数据
    private void setSetmealAndCheckGroup(Integer setmealId, Integer[] checkgroupIds) {
        if(checkgroupIds!=null && checkgroupIds.length>0){
            for (Integer checkgroupId : checkgroupIds) {
                Map<String ,Integer> map = new HashMap<>();
                map.put("setmeal_id",setmealId);
                map.put("checkgroup_id",checkgroupId);
                setmealDao.addSetmealAndCheckGroup(map);
            }
        }
    }
}
