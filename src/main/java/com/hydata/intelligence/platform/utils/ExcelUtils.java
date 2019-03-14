package com.hydata.intelligence.platform.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.hydata.intelligence.platform.dto.Device;
import com.hydata.intelligence.platform.dto.Product;
import com.hydata.intelligence.platform.model.RESCODE;
import com.hydata.intelligence.platform.repositories.ProductRepository;
import com.hydata.intelligence.platform.service.DeviceService;

/**
 * @author pyt
 * @createTime 2018年11月12日下午2:28:32
 */
@Component
public class ExcelUtils {

	private static Logger logger = LogManager.getLogger(ExcelUtils.class);
	private static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	public static void exportExcel(HttpServletRequest request, HttpServletResponse response) {
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
			response.setHeader("Content-disposition", "attachment;filename="+"cell_link_device_.xls");//Excel文件名
			workbook.write(response.getOutputStream());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void exportDevice(JSONArray devices, HttpServletRequest request, HttpServletResponse response){
		HSSFWorkbook workbook = new HSSFWorkbook();
		Sheet sheet = workbook.createSheet("firstSheet");
		Row row = sheet.createRow(0);
		Cell cell0 = row.createCell(0);
		Cell cell1 = row.createCell(1);
		Cell cell2 = row.createCell(2);
		Cell cell3 = row.createCell(3);
		Cell cell4 = row.createCell(4);
		cell0.setCellValue("id");
		cell1.setCellValue("name");
		cell2.setCellValue("device_sn");
		cell3.setCellValue("protocol");
		cell4.setCellValue("create_time");
		for(int i =0 ; i < devices.size() ; i++){
			JSONObject object = (JSONObject)devices.get(i);
			Row row_device = sheet.createRow(i+1);
			Cell cell10 = row_device.createCell(0);
			cell10.setCellValue(String.valueOf(object.get("id")));
			Cell cell11 = row_device.createCell(1);
			cell11.setCellValue((String)object.get("name"));
			Cell cell12 = row_device.createCell(2);
			cell12.setCellValue((String)object.get("device_sn"));
			Cell cell13 = row_device.createCell(3);
			cell13.setCellValue((String) object.get("protocol"));
			Cell cell14 = row_device.createCell(4);
			cell14.setCellValue(sdf.format(object.get("create_time")));
		}
		try {
			response.setContentType("application/octet-stream");
			response.setHeader("Content-disposition", "attachment;filename="+"cell_link_device.xls");//Excel文件名
			workbook.write(response.getOutputStream());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static JSONObject importExcel(MultipartFile file) {
		if(file.getContentType().equals("application/vnd.ms-excel")) {
			JSONObject objectReturn = new JSONObject();
			JSONArray array = new JSONArray();
			HSSFWorkbook book;
			try {
				InputStream is = file.getInputStream();
				book = new HSSFWorkbook(is);
				HSSFSheet sheet = book.getSheetAt(0);
				for(int rowNum=1; rowNum<sheet.getLastRowNum()+1; rowNum++) {
					JSONObject object = new JSONObject();
					JSONObject content = new JSONObject();
					HSSFRow row = sheet.getRow(rowNum);
					if(row == null) {
						continue;//此行为空，进入下一行
					}
					//遍历此行的单元格
					HSSFCell cell0 = row.getCell(0);
					if(cell0 == null) {
						continue;//此单元格为空，进入下一单元格
					}
					//读取单元格内值
					int id = (int) Float.parseFloat(readCell(cell0));
					HSSFCell cell1 = row.getCell(1);
					if(cell1 == null) {
						continue;//此单元格为空，进入下一单元格
					}
					//读取单元格内值
					String name = readCell(cell1);
					HSSFCell cell2= row.getCell(2);
					if(cell2 == null) {
						continue;//此单元格为空，进入下一单元格
					}
					//读取单元格内值
					String device_sn = readCell(cell2);
					content.put(name, device_sn);
					object.put(id+"", content);
					array.add(object);
				}
				logger.debug(array);
				return  RESCODE.SUCCESS.getJSONRES(array);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				logger.error(e.getMessage());
				return RESCODE.IO_ERROR.getJSONRES();
			}
		}else {
			return RESCODE.FORMAT_ERROR.getJSONRES();
		}

	}

	/**
	 * @param cell
	 * @return
	 */
	public static String readCell(HSSFCell cell) {
		if(cell.getCellType() == HSSFCell.CELL_TYPE_BOOLEAN) {
			System.out.println("布尔量");
			return String.valueOf(cell.getBooleanCellValue());
		}else if(cell.getCellType() == HSSFCell.CELL_TYPE_NUMERIC) {
			System.out.println("数值型");
			if (HSSFDateUtil.isCellDateFormatted(cell)) {
				System.out.println("This is date");
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
				return sdf.format(HSSFDateUtil.getJavaDate(cell.getNumericCellValue()));
			}else {
				long value = (long)cell.getNumericCellValue();
				System.out.println("数值："+value);
				return String.valueOf(value);
			}
		}else {
			System.out.println("String型");
			return cell.getStringCellValue();
		}
	}

}

