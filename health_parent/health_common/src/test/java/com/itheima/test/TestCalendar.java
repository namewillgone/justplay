package com.itheima.test;

import org.junit.Test;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * @ClassName TestCalendar
 * @Description TODO
 * @Author ly
 * @Company 深圳黑马程序员
 * @Date 2019/9/21 12:38
 * @Version V1.0
 */
public class TestCalendar {

    @Test
    public void test(){
        List<String> months = new ArrayList<>();
        Calendar calendar = Calendar.getInstance();
        // 根据当前时间-12，算出12个月之前的时间
        calendar.add(Calendar.MONTH,-12);
        for(int i=1;i<=12;i++){
            calendar.add(Calendar.MONTH,1); //12个月之前的时间+1，得到2018-10
            months.add(new SimpleDateFormat("yyyy-MM").format(calendar.getTime()));
        }
        System.out.println(months);
    }
}
