package com.hydata.intelligence.platform.cell_link.utils;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.hydata.intelligence.platform.cell_link.model.RESCODE;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @ClassName ExcelUtils
 * @Description TODO
 * @Author pyt
 * @Date 2019/9/10 13:58
 * @Version
 */
@Component
public class ExcelUtils {
    private static Logger logger = LogManager.getLogger(ExcelUtils.class);
    private static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public static void exportExcel(String fileName, List<Map<String, Object>> list, HttpServletRequest request, HttpServletResponse response) {
        //创建excel工作簿
        HSSFWorkbook workbook = new HSSFWorkbook();
        //创建第一页
        Sheet sheet = workbook.createSheet("firstSheet");
        //创建第一行
        Row row = sheet.createRow(0);
        //填写第一行：列名
        if (list.size() > 0) {
            Map<String, Object> map = list.get(0);
            int index = 0;
            for (String key :
                    map.keySet()) {
                Cell cell = row.createCell(index++, CellType.STRING);
                cell.setCellValue(key);
            }
        }
        int index_map = 1;
        for (Map<String, Object> map : list) {
            Row row1 = sheet.createRow(index_map);
            int index = 0;
            for (String key :
                    map.keySet()) {
                String t = map.get(key).getClass().getName();
                Cell cell0 = row1.createCell(index++);
                switch (t) {
                    case "java.lang.String":
                        cell0.setCellValue(map.get(key).toString());
                        break;
                    case "java.util.Date":
                    case "java.sql.Timestamp":
                        Date date = (Date) map.get(key);
                        String d = StringUtil.getDateString(date);
                        cell0.setCellValue(d);
                        break;
                    case "java.lang.Byte":
                        cell0.setCellValue((Byte) map.get(key));
                        break;
                    case "java.lang.Short":
                        cell0.setCellValue((Short) map.get(key));
                        break;
                    case "java.lang.Integer":
                        cell0.setCellValue((Integer) map.get(key));
                        break;
                    case "java.lang.Long":
                        cell0.setCellValue((Long) map.get(key));
                        break;
                    case "java.lang.Float":
                        cell0.setCellValue((Float) map.get(key));
                        break;
                    case "java.lang.Double":
                        cell0.setCellValue((Double) map.get(key));
                        break;
                }
            }
            index_map++;
        }
        fileName = new String(fileName.getBytes(), StandardCharsets.UTF_8) + ".xls";
        try {
            response.setContentType("application/octet-stream");
            response.setHeader("Content-disposition", "attachment;filename=" + fileName);//Excel文件名
            response.addHeader("Pargam", "no-cache");
            response.addHeader("Cache-Control", "no-cache");
            workbook.write(response.getOutputStream());
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public static void exportModel(HttpServletRequest request, HttpServletResponse response) {
        //创建excel工作簿
        HSSFWorkbook workbook = new HSSFWorkbook();
        //创建第一页
        Sheet sheet = workbook.createSheet("firstSheet");
        //创建第一行
        Row row = sheet.createRow(0);
        Row row1 = sheet.createRow(1);
        Row row2 = sheet.createRow(2);
        //创建第一行上第一个单元格
        Cell cell0 = row.createCell(0);
        Cell cell1 = row.createCell(1);
        Cell cell2 = row.createCell(2);
        //设置第一个单元格内显示
        cell0.setCellValue("index");
        cell1.setCellValue("设备名称");
        cell2.setCellValue("设备鉴权信息");
        Cell cell10 = row1.createCell(0);
        Cell cell11 = row1.createCell(1);
        Cell cell12 = row1.createCell(2);
        cell10.setCellValue(1);
        cell11.setCellValue("test1");
        cell12.setCellValue("3264XXX83");
        Cell cell20 = row2.createCell(0);
        Cell cell21 = row2.createCell(1);
        Cell cell22 = row2.createCell(2);
        cell20.setCellValue(2);
        cell21.setCellValue("test2");
        cell22.setCellValue("3264XXX84");
        try {
            response.setContentType("application/octet-stream");
            response.setHeader("Content-disposition", "attachment;filename=" + "cell_link_device_.xls");//Excel文件名
            workbook.write(response.getOutputStream());
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public static JSONObject importExcel(MultipartFile file) {
        if (file.getContentType().equals("application/octet-stream")
                || file.getContentType().equals("application/vnd.ms-excel")) {
            JSONArray array = new JSONArray();
            HSSFWorkbook book;
            try {
                InputStream is = file.getInputStream();
                book = new HSSFWorkbook(is);
                HSSFSheet sheet = book.getSheetAt(0);
                for (int rowNum = 1; rowNum < sheet.getLastRowNum() + 1; rowNum++) {
                    JSONObject object = new JSONObject();
                    JSONObject content = new JSONObject();
                    HSSFRow row = sheet.getRow(rowNum);
                    if (row == null) {
                        continue;//此行为空，进入下一行
                    }
                    //遍历此行的单元格
                    HSSFCell cell0 = row.getCell(0);
                    if (cell0 == null) {
                        continue;//此单元格为空，进入下一单元格
                    }
                    //读取单元格内值
                    int id = (int) Float.parseFloat(readCell(cell0));
                    HSSFCell cell1 = row.getCell(1);
                    if (cell1 == null) {
                        continue;//此单元格为空，进入下一单元格
                    }
                    //读取单元格内值
                    String name = readCell(cell1);
                    HSSFCell cell2 = row.getCell(2);
                    if (cell2 == null) {
                        continue;//此单元格为空，进入下一单元格
                    }
                    //读取单元格内值
                    String device_sn = readCell(cell2);
                    content.put(name, device_sn);
                    object.put(id + "", content);
                    array.add(object);
                }
                logger.debug(array);
                return RESCODE.SUCCESS.getJSONRES(array);
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                logger.error(e.getMessage());
                return RESCODE.IO_ERROR.getJSONRES();
            }
        } else {
            return RESCODE.FORMAT_ERROR.getJSONRES();
        }

    }

    public static String readCell(HSSFCell cell) {
        if (cell.getCellTypeEnum() == CellType.BOOLEAN) {
//            System.out.println("布尔量");
            return String.valueOf(cell.getBooleanCellValue());
        } else if (cell.getCellTypeEnum() == CellType.NUMERIC) {
//            System.out.println("数值型");
            if (HSSFDateUtil.isCellDateFormatted(cell)) {
//                System.out.println("This is date");
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
                return sdf.format(HSSFDateUtil.getJavaDate(cell.getNumericCellValue()));
            } else {
                long value = (long) cell.getNumericCellValue();
//                System.out.println("数值：" + value);
                return String.valueOf(value);
            }
        } else {
//            System.out.println("String型");
            return cell.getStringCellValue();
        }
    }

    /**
     * 打印命令日志
     * @param cmdLogs
     * @param request
     * @param response
     */
    public static void exportCmdLogs(JSONArray cmdLogs, HttpServletRequest request, HttpServletResponse response){
        HSSFWorkbook workbook = new HSSFWorkbook();
        Sheet sheet = workbook.createSheet("firstSheet");
        Row row = sheet.createRow(0);
        Cell cell0 = row.createCell(0);
        Cell cell1 = row.createCell(1);
        Cell cell2 = row.createCell(2);
        Cell cell3 = row.createCell(3);
        Cell cell4 = row.createCell(4);
        Cell cell5 = row.createCell(5);
        cell0.setCellValue("cmd_uuid");
        cell1.setCellValue("设备id");
        cell2.setCellValue("命令内容");
        cell3.setCellValue("下发时间");
        cell4.setCellValue("响应状态");
        cell5.setCellValue("响应内容");
        for(int i =0 ; i < cmdLogs.size() ; i++){
            JSONObject object = (JSONObject)cmdLogs.get(i);
            Row row_cmdLog = sheet.createRow(i+1);
            Cell cell10 = row_cmdLog.createCell(0);
            cell10.setCellValue(String.valueOf(object.get("id")));
            Cell cell11 = row_cmdLog.createCell(1);
            cell11.setCellValue(object.get("device_id").toString());
            Cell cell12 = row_cmdLog.createCell(2);
            cell12.setCellValue((String)object.get("msg"));
            Cell cell13 = row_cmdLog.createCell(3);
            cell13.setCellValue(sdf.format(object.get("sendTime")));
            Cell cell14 = row_cmdLog.createCell(4);
            cell14.setCellValue(String.valueOf(object.get("res_code")));
            Cell cell15 = row_cmdLog.createCell(5);
            cell15.setCellValue((String)object.get("res_msg"));
        }
        try {
            response.setContentType("application/octet-stream");
            response.setHeader("Content-disposition", "attachment;filename="+"cell_link_cmdLog.xls");//Excel文件名
            workbook.write(response.getOutputStream());
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }



}
