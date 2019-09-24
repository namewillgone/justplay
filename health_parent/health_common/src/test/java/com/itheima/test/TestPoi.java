package com.itheima.test;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.Test;

import java.io.FileOutputStream;
import java.io.OutputStream;

/**
 * @ClassName TestQiniu
 * @Description TODO
 * @Author ly
 * @Company 深圳黑马程序员
 * @Date 2019/9/12 12:22
 * @Version V1.0
 */
public class TestPoi {

    // 使用POI从一个已经存在的EXCEL文件中读取数据（用的较多）
    @Test
    public void readExcel() throws Exception {
        // 1：创建工作簿对象
        XSSFWorkbook workbook = new XSSFWorkbook("D://hello.xlsx");
        // 2：获取工作表对象
        XSSFSheet sheet = workbook.getSheetAt(0);
        // 3：遍历工作表对象，获取行对象
        for (Row row : sheet) {
            // 4：遍历对象，获取单元格的对象
            for (Cell cell : row) {
                // 5:获得数据
                String value = cell.getStringCellValue();
                System.out.println(value);
            }
        }
        // 6：关闭
        workbook.close();
    }

    // 使用POI从一个已经存在的EXCEL文件中读取数据（使用行和单元格的索引）
    @Test
    public void readExcel_2() throws Exception {
        // 1：创建工作簿对象
        XSSFWorkbook workbook = new XSSFWorkbook("D://hello.xlsx");
        // 2：获取工作表对象
        XSSFSheet sheet = workbook.getSheetAt(0);
        // 3：遍历工作表对象，获取行对象
        int rowNum = sheet.getLastRowNum();
        System.out.println(rowNum);
        for(int i=0;i<=rowNum;i++){
            XSSFRow row = sheet.getRow(i);
            short cellNum = row.getLastCellNum();
            System.out.println(cellNum);
            // 4：遍历对象，获取单元格的对象
            for(int j=0;j<cellNum;j++){
                XSSFCell cell = row.getCell(j);
                // 5:获得数据
                String value = cell.getStringCellValue();
                System.out.println(value);
            }
        }
        // 6：关闭
        workbook.close();
    }

    // 使用POI可以在内存中创建一个Excel文件并将数据写入到这个文件，最后通过输出流将内存中的Excel文件下载到磁盘
    @Test
    public void writeExcel() throws Exception {
        // 1：创建工作簿对象
        XSSFWorkbook workbook = new XSSFWorkbook();
        // 2：创建工作表对象
        XSSFSheet sheet = workbook.createSheet("测试Excel");
        // 3：创建行对象
        XSSFRow row1 = sheet.createRow(0);// 第1行
        // 4：创建单元格对象
        row1.createCell(0).setCellValue("姓名");
        row1.createCell(1).setCellValue("年龄");
        row1.createCell(2).setCellValue("地址");

        XSSFRow row2 = sheet.createRow(1);// 第2行
        row2.createCell(0).setCellValue("赵六");
        row2.createCell(1).setCellValue(22);
        row2.createCell(2).setCellValue("深圳");

        XSSFRow row3 = sheet.createRow(2);// 第3行
        row3.createCell(0).setCellValue("田七");
        row3.createCell(1).setCellValue(20);
        row3.createCell(2).setCellValue("深圳");

        XSSFRow row4 = sheet.createRow(3);// 第4行
        row4.createCell(0).setCellValue("胡八");
        row4.createCell(1).setCellValue(20);
        row4.createCell(2).setCellValue("深圳");

        //5：通过输出流将workbook对象下载到磁盘
        OutputStream outputStream = new FileOutputStream("D://excel73.xls");
        workbook.write(outputStream);
        outputStream.flush(); // 让内存中的数据写到磁盘
        outputStream.close();
        workbook.close();

    }
}
