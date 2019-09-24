package com.itheima.test;

import com.itheima.health.utils.DateUtils;
import org.junit.Test;

import java.util.Date;

/**
 * @ClassName TestDate
 * @Description TODO
 * @Author ly
 * @Company 深圳黑马程序员
 * @Date 2019/9/22 10:24
 * @Version V1.0
 */
public class TestDate {

    @Test
    public void testDate() throws Exception {
        // 当前时间
        Date today = DateUtils.getToday();
        String reportData = DateUtils.parseDate2String(today,"yyyy-MM-dd");
        // 本周的周一
        String firstDayOfWeek = DateUtils.parseDate2String(DateUtils.getThisWeekMonday());
        // 本周的周日
        String lastDayOfWeek = DateUtils.parseDate2String(DateUtils.getSundayOfThisWeek());
        // 本月的第1天
        String firstDayOfMonth = DateUtils.parseDate2String(DateUtils.getFirstDay4ThisMonth());
        // 本月的最后1天
        String lastDayOfMonth = DateUtils.parseDate2String(DateUtils.getLastDay4ThisMonth());
        System.out.println(reportData);
        System.out.println(firstDayOfWeek);
        System.out.println(lastDayOfWeek);
        System.out.println(firstDayOfMonth);
        System.out.println(lastDayOfMonth);
    }
}
