package com.itheima.health.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.itheima.health.dao.MemberDao;
import com.itheima.health.dao.OrderDao;
import com.itheima.health.service.ReportService;
import com.itheima.health.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
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
@Service(interfaceClass = ReportService.class)
@Transactional
public class ReportServiceImpl implements ReportService {

    @Autowired
    private OrderDao orderDao;

    @Autowired
    private MemberDao memberDao;

    @Override
    public Map<String, Object> findBusinessReportData() {
        Map<String,Object> map = new HashMap<>();
        try {
            // 当前时间
            Date today = DateUtils.getToday();
            String reportDate = DateUtils.parseDate2String(today,"yyyy-MM-dd");
            // 本周的周一
            String firstDayOfWeek = DateUtils.parseDate2String(DateUtils.getThisWeekMonday());
            // 本周的周日
            String lastDayOfWeek = DateUtils.parseDate2String(DateUtils.getSundayOfThisWeek());
            // 本月的第1天
            String firstDayOfMonth = DateUtils.parseDate2String(DateUtils.getFirstDay4ThisMonth());
            // 本月的最后1天
            String lastDayOfMonth = DateUtils.parseDate2String(DateUtils.getLastDay4ThisMonth());
            /**********************************************************************************************/
            // 今日新增会员
            Integer todayNewMember = memberDao.findTodayNewMember(reportDate);
            // 总会员数
            Integer totalMember = memberDao.findTotalMember();
            // 本周新增会员数
            Integer thisWeekNewMember = memberDao.findThisWeekNewMember(firstDayOfWeek);
            // 本月新增会员数
            Integer thisMonthNewMember = memberDao.findThisWeekNewMember(firstDayOfMonth);
            // 今日预约数
            Integer todayOrderNumber = orderDao.findTodayOrderNumber(reportDate);
            // 今日到诊数
            Integer todayVisitsNumber = orderDao.findTodayVisitsNumber(reportDate);
            // 本周预约数
            Map<String,Object> weekMap = new HashMap<>();
            weekMap.put("begin",firstDayOfWeek);
            weekMap.put("end",lastDayOfWeek);
            Integer thisWeekOrderNumber = orderDao.findThisOrderNumber(weekMap);
            // 本周到诊数
            Map<String,Object> weekMap2 = new HashMap<>();
            weekMap2.put("begin",firstDayOfWeek);
            weekMap2.put("end",lastDayOfWeek);
            Integer thisWeekVisitsNumber = orderDao.findThisVisitsNumber(weekMap2);

            // 本月预约数
            Map<String,Object> monthMap = new HashMap<>();
            monthMap.put("begin",firstDayOfMonth);
            monthMap.put("end",lastDayOfMonth);
            Integer thisMonthOrderNumber = orderDao.findThisOrderNumber(monthMap);

            // 本月到诊数
            Map<String,Object> monthMap2 = new HashMap<>();
            monthMap2.put("begin",firstDayOfMonth);
            monthMap2.put("end",lastDayOfMonth);
            Integer thisMonthVisitsNumber = orderDao.findThisVisitsNumber(monthMap2);

            // 热门套餐
            List<Map<String,Object>> hotSetmeal = orderDao.findHotSetmeal();

            /**********************************************************************************************/
            map.put("reportDate",reportDate);
            map.put("todayNewMember",todayNewMember);
            map.put("totalMember",totalMember);
            map.put("thisWeekNewMember",thisWeekNewMember);
            map.put("thisMonthNewMember",thisMonthNewMember);
            map.put("todayOrderNumber",todayOrderNumber);
            map.put("todayVisitsNumber",todayVisitsNumber);
            map.put("thisWeekOrderNumber",thisWeekOrderNumber);
            map.put("thisWeekVisitsNumber",thisWeekVisitsNumber);
            map.put("thisMonthOrderNumber",thisMonthOrderNumber);
            map.put("thisMonthVisitsNumber",thisMonthVisitsNumber);
            map.put("hotSetmeal",hotSetmeal);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return map;
    }
}
