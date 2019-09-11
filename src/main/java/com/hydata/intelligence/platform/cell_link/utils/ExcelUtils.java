package com.hydata.intelligence.platform.cell_link.utils;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
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

    public static void exportExcel(String fileName, List<Map<String,Object>> list, HttpServletRequest request, HttpServletResponse response) {
        //创建excel工作簿
        HSSFWorkbook workbook = new HSSFWorkbook();
        //创建第一页
        Sheet sheet = workbook.createSheet("firstSheet");
        //创建第一行
        Row row = sheet.createRow(0);
        //填写第一行：列名
        if (list.size()>0) {
            Map<String,Object> map = list.get(0);
            int index = 0;
            for (String key :
                    map.keySet()) {
                Cell cell = row.createCell(index++, CellType.STRING);
                cell.setCellValue(key);
            }
        }
        int index_map = 1;
        for (Map<String,Object> map:list) {
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
        fileName = new String(fileName.getBytes(), StandardCharsets.UTF_8)+".xls";
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
}
