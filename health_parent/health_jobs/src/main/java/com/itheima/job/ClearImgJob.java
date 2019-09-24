package com.itheima.job;

import com.itheima.health.constants.RedisConstant;
import com.itheima.health.utils.QiniuUtils;
import org.springframework.beans.factory.annotation.Autowired;
import redis.clients.jedis.JedisPool;

import java.util.Iterator;
import java.util.Set;

/**
 * @ClassName ClearImgJob
 * @Description TODO
 * @Author ly
 * @Company 深圳黑马程序员
 * @Date 2019/9/14 12:19
 * @Version V1.0
 */
public class ClearImgJob {

    @Autowired
    JedisPool jedisPool;

    // 清理图片的方法
    public void executeClearImg(){
        //比较redis2个集合中不同的数据
        Set<String> set = jedisPool.getResource().sdiff(RedisConstant.SETMEAL_PIC_RESOURCES, RedisConstant.SETMEAL_PIC_DB_RESOURCES);
        Iterator<String> iterator = set.iterator();
        while(iterator.hasNext()){
            // 返回2个集合中，不一样的文件名称
            String filename = iterator.next();
            // * 删除七牛云上的垃圾图片
            QiniuUtils.deleteFileFromQiniu(filename);
            // * 删除redis的SetMealPicResource中的垃圾图片名称
            jedisPool.getResource().srem(RedisConstant.SETMEAL_PIC_RESOURCES,filename);
        }


    }
}
