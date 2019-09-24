package com.itheima.health.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.health.constants.MessageConstant;
import com.itheima.health.entity.Result;
import com.itheima.health.pojo.Setmeal;
import com.itheima.health.service.SetmealService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @ClassName SetmealMobileController
 * @Description TODO
 * @Author ly
 * @Company 深圳黑马程序员
 * @Date 2019/9/16 9:51
 * @Version V1.0
 */
@RestController
@RequestMapping(value = "/setmeal")
public class SetmealMobileController {

    @Reference
    SetmealService setmealService;

    // 查询所有套餐
    @RequestMapping(value = "/getSetmeal")
    public Result getSetmeal(){
        List<Setmeal> list = setmealService.findAll();
        if(list!=null && list.size()>0){
            return new Result(true, MessageConstant.GET_SETMEAL_LIST_SUCCESS,list);
        }else{
            return new Result(false, MessageConstant.GET_SETMEAL_LIST_FAIL);
        }
    }

    // 使用套餐id，查询套餐的详情信息
    @RequestMapping(value = "/findById")
    public Result findById(Integer id){
        Setmeal setmeal = setmealService.findById(id);
        if(setmeal!=null){
            return new Result(true, MessageConstant.QUERY_SETMEAL_SUCCESS,setmeal);
        }else{
            return new Result(false, MessageConstant.QUERY_SETMEAL_FAIL);
        }
    }
}
