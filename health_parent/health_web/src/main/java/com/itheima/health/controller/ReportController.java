package com.itheima.health.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.health.constants.MessageConstant;
import com.itheima.health.entity.Result;
import com.itheima.health.service.MemberService;
import com.itheima.health.service.ReportService;
import com.itheima.health.service.SetmealService;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @ClassName CheckItemController
 * @Description TODO
 * @Author ly
 * @Company 深圳黑马程序员
 * @Date 2019/9/11 9:57
 * @Version V1.0
 */
@RestController // json响应
@RequestMapping(value = "/report")
public class ReportController {

    @Reference
    MemberService memberService;

    @Reference
    SetmealService setmealService;

    @Reference
    ReportService reportService;


    // 报表统计，统计会员注册时间的折线图
    @RequestMapping(value = "/getMemberReport")
    public Result getMemberReport(){
        try {
            Map<String,Object> map = new HashMap<String,Object>();
            // 存放日期的字符串集合（统计过去12个月）
            List<String> months = new ArrayList<>();
            Calendar calendar = Calendar.getInstance();
            // 根据当前时间-12，算出12个月之前的时间
            calendar.add(Calendar.MONTH,-12);
            for(int i=1;i<=12;i++){
                calendar.add(Calendar.MONTH,1); //12个月之前的时间+1，得到2018-10
                months.add(new SimpleDateFormat("yyyy-MM").format(calendar.getTime()));
            }
            List<Integer> memberCount = memberService.findMemberCountByRegTime(months);
            map.put("months",months);// 存的月份，List<String> :["2019-07","2019-08","2019-09"]
            map.put("memberCount",memberCount);// 存放月份对应的数据，List<Integer>:[18,21,24]
            return new Result(true,MessageConstant.GET_MEMBER_NUMBER_REPORT_SUCCESS,map);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.GET_MEMBER_NUMBER_REPORT_FAIL);
        }
    }

    // 报表统计，统计套餐预约占比统计（饼图）
    @RequestMapping(value = "/getSetmealReport")
    public Result getSetmealReport(){
        try {
            Map<String,Object> map = new HashMap<String,Object>();
            // 套餐预约占比数量
            List<Map<String,Object>> setmealCount = setmealService.findSetmealCount();
            // 套餐名称
            List<String> setmealNames = new ArrayList<>();
            for (Map<String, Object> stringObjectMap : setmealCount) {
                String name = (String)stringObjectMap.get("name"); // 套餐名称
                setmealNames.add(name);
            }
            map.put("setmealNames",setmealNames);  // 套餐名称
            map.put("setmealCount",setmealCount);  // 套餐数量
            return new Result(true,MessageConstant.GET_SETMEAL_COUNT_REPORT_SUCCESS,map);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.GET_SETMEAL_COUNT_REPORT_FAIL);
        }
    }


    // 报表统计，统计运营数据统计（页面）
    @RequestMapping(value = "/getBusinessReportData")
    public Result getBusinessReportData(){
        try {
            Map<String,Object> map = new HashMap<String,Object>();
            map = reportService.findBusinessReportData();
            return new Result(true,MessageConstant.GET_BUSINESS_REPORT_SUCCESS,map);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.GET_BUSINESS_REPORT_FAIL);
        }
    }

    // 报表统计，统计运营数据统计（excel报表）
    @RequestMapping(value = "/exportBusinessReport")
    public Result exportBusinessReport(HttpServletRequest request, HttpServletResponse response){
        try {
            Map<String,Object> map = new HashMap<String,Object>();
            map = reportService.findBusinessReportData();

            String reportDate = (String)map.get("reportDate");
            Integer todayNewMember = (Integer)map.get("todayNewMember");
            Integer totalMember = (Integer)map.get("totalMember");
            Integer thisWeekNewMember = (Integer)map.get("thisWeekNewMember");
            Integer thisMonthNewMember = (Integer)map.get("thisMonthNewMember");
            Integer todayOrderNumber = (Integer)map.get("todayOrderNumber");
            Integer todayVisitsNumber = (Integer)map.get("todayVisitsNumber");
            Integer thisWeekOrderNumber = (Integer)map.get("thisWeekOrderNumber");
            Integer thisWeekVisitsNumber = (Integer)map.get("thisWeekVisitsNumber");
            Integer thisMonthOrderNumber = (Integer)map.get("thisMonthOrderNumber");
            Integer thisMonthVisitsNumber = (Integer)map.get("thisMonthVisitsNumber");
            List<Map<String,Object>> hotSetmeal = (List<Map<String,Object>>)map.get("hotSetmeal");

            // 将数据存放到excel
            // 使用template文件夹下的report_template.xlsx
            String path = request.getSession().getServletContext().getRealPath("template")+File.separator+"report_template.xlsx";
            XSSFWorkbook workbook = new XSSFWorkbook(new FileInputStream(new File(path)));
            // 获取工作表
            XSSFSheet sheet = workbook.getSheetAt(0);
            // 设置日期
            XSSFRow row = sheet.getRow(2);
            row.getCell(5).setCellValue(reportDate);
            // 设置新增会员数量+总会员数
            row = sheet.getRow(4);
            row.getCell(5).setCellValue(todayNewMember);
            row.getCell(7).setCellValue(totalMember);

            row = sheet.getRow(5);
            row.getCell(5).setCellValue(thisWeekNewMember);//本周新增会员数
            row.getCell(7).setCellValue(thisMonthNewMember);//本月新增会员数

            row = sheet.getRow(7);
            row.getCell(5).setCellValue(todayOrderNumber);//今日预约数
            row.getCell(7).setCellValue(todayVisitsNumber);//今日到诊数

            row = sheet.getRow(8);
            row.getCell(5).setCellValue(thisWeekOrderNumber);//本周预约数
            row.getCell(7).setCellValue(thisWeekVisitsNumber);//本周到诊数

            row = sheet.getRow(9);
            row.getCell(5).setCellValue(thisMonthOrderNumber);//本月预约数
            row.getCell(7).setCellValue(thisMonthVisitsNumber);//本月到诊数

            int rowNum = 12;
            for(Map hotMap : hotSetmeal){//热门套餐
                String name = (String) hotMap.get("name");
                Long setmeal_count = (Long) hotMap.get("setmeal_count");
                BigDecimal proportion = (BigDecimal) hotMap.get("proportion");
                row = sheet.getRow(rowNum ++);
                row.getCell(4).setCellValue(name);//套餐名称
                row.getCell(5).setCellValue(setmeal_count);//预约数量
                row.getCell(6).setCellValue(proportion.doubleValue());//占比
            }
            // 将workbook对象写入到response缓冲区中
            ServletOutputStream out = response.getOutputStream();
            // 指定下载文件、导出文件的类型（excel类型）
            response.setContentType("application/vnd.ms-excel"); // 指定文件的类型  excel类型
            response.setHeader("Content-Disposition","attachement;filename=export.xls"); // 指定下载的方式，内联（inline）/附件（attachment）
            workbook.write(out);
            out.flush(); // 刷新缓冲区，将缓冲区的数据写入到流
            out.close();
            workbook.close();

            return null; // 结果以IO的形式响应
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.GET_BUSINESS_REPORT_FAIL);
        }
    }
}
