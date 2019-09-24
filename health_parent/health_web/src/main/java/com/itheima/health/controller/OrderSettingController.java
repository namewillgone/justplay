package com.itheima.health.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.health.constants.MessageConstant;
import com.itheima.health.entity.Result;
import com.itheima.health.pojo.OrderSetting;
import com.itheima.health.service.OrderSettingService;
import com.itheima.health.utils.POIUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @ClassName CheckItemController
 * @Description TODO
 * @Author ly
 * @Company 深圳黑马程序员
 * @Date 2019/9/11 9:57
 * @Version V1.0
 */
@RestController // json响应
@RequestMapping(value = "/ordersetting")
public class OrderSettingController {

    @Reference
    OrderSettingService orderSettingService;


    // 读取Excel文件的内容批量导入到数据库
    @RequestMapping(value = "/upload")
    public Result upload(@RequestParam(value = "excelFile") MultipartFile excelFile){
        try {
            /**
             * 日期	          可预约数量
             2019/9/1	        100
             2019/9/2	        100
             */
            List<String[]> list = POIUtils.readExcel(excelFile);
            // 转换成保存的javabean
            List<OrderSetting> orderSettingsList = new ArrayList<>();
            if(list!=null && list.size()>0){
                for (String[] strings : list) {
                    OrderSetting orderSetting = new OrderSetting(new Date(strings[0]),Integer.parseInt(strings[1]));
                    orderSettingsList.add(orderSetting);
                }
            }
            orderSettingService.addList(orderSettingsList);
            return new Result(true, MessageConstant.IMPORT_ORDERSETTING_SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.IMPORT_ORDERSETTING_FAIL);
        }
    }

    // 使用当前月，查询当前月份下所对应的日期、每个日期对应的最多预约人数和已经预约人数，List<Map>
    // 参数date=2019-9
    @RequestMapping(value = "/findOrderSettingByOrderDateMonth")
    public Result findOrderSettingByOrderDateMonth(String date) {
        List<Map<String, Object>> list = orderSettingService.findOrderSettingByOrderDateMonth(date);
        if (list != null && list.size() > 0) {
            return new Result(true, MessageConstant.GET_ORDERSETTING_SUCCESS, list);
        } else {
            return new Result(false, MessageConstant.GET_ORDERSETTING_FAIL);
        }
    }

    // 点击“设置”按钮，完成针对当前时间更新最多预约人数的数量
    // 参数date=2019-9
    @RequestMapping(value = "/setOrderSetting")
    public Result setOrderSetting(@RequestBody OrderSetting orderSetting) {
        try {
            orderSettingService.setOrderSetting(orderSetting);
            return new Result(true, MessageConstant.ORDERSETTING_SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.ORDERSETTING_FAIL);
        }
    }
}
