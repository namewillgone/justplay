package com.itheima.health.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.itheima.health.dao.CheckItemDao;
import com.itheima.health.entity.PageResult;
import com.itheima.health.pojo.CheckItem;
import com.itheima.health.service.CheckItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @ClassName CheckItemServiceImpl
 * @Description TODO
 * @Author ly
 * @Company 深圳黑马程序员
 * @Date 2019/9/11 9:55
 * @Version V1.0
 */
@Service(interfaceClass = CheckItemService.class)
@Transactional
public class CheckItemServiceImpl implements CheckItemService {

    @Autowired
    private CheckItemDao checkItemDao;

    @Override
    public void add(CheckItem checkItem) {
        checkItemDao.add(checkItem);
    }

    /**
     * 正常逻辑
     * 写2条sql
     * 第一条sql：select count(*) from t_checkitem where name = '传智身高'  -->封装到PageResult中total
     * 第二条sql：select * from t_checkitem where name = '传智身高' limit ?,?  -->封装到PageResult中rows
     *                     limit ?,?  ：第一个问号：表示从第几条开始检索  计算：(currentPage-1)*pageSize
     *                                  第二个问号：表示当前页最多显示的记录数， 计算：pageSize
     *
     *  mybatis的PageHelper（分页插件）
     * @param queryString
     * @param currentPage（当前页）
     * @param pageSize（当前页最多显示的记录数）
     *
     * @return
     */
    @Override
    public PageResult findPage(String queryString, Integer currentPage, Integer pageSize) {
        // 1：初始化分页的参数
        PageHelper.startPage(currentPage,pageSize);
        // 2：完成查询
        Page<CheckItem> page = checkItemDao.findPageByCondition(queryString);
        // 3：组织PageResult
        return new PageResult(page.getTotal(),page.getResult());
    }

    /**
     * 多对多的业务操作
     1：删除一张表的数据，判断中间表是否有数据
     * 如果有：先删除中间表的数据，然后再删除检查项的数据
     2：删除一张表的数据，判断中间表是否有数据
     * 如果有：提示不能删除，先确定解除中间 表的删除之后才能删除（建议使用，安全）
     */
    @Override
    public void deleteById(Integer id) {
        // 1：判断中间表中是否存在数据，使用检查项id，查询中间表，获取数量
        long count = checkItemDao.findCountByCheckItemId(id);
        // 中间表有数据
        if(count>0){
            throw new RuntimeException("删除的检查项在检查组中存在引用关系，不能删除，请先删除中间表的数据");
        }
        checkItemDao.deleteById(id);
    }

    @Override
    public CheckItem findById(Integer id) {
        return checkItemDao.findById(id);
    }

    @Override
    public void update(CheckItem checkItem) {
        checkItemDao.update(checkItem);
    }

    @Override
    public List<CheckItem> findAll() {
        return checkItemDao.findAll();
    }
}
