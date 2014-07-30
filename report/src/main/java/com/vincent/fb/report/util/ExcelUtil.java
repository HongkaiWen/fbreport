package com.vincent.fb.report.util;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

import com.vincent.fb.report.to.RowTO;

/**
 * refer to http://poi.apache.org/spreadsheet/quick-guide.html#CellContents
 * 
 * @author lovesusu
 *
 */
public class ExcelUtil {
	
	public static List<RowTO> getExcelContents(Date fromDate, Date endDate) throws InvalidFormatException, IOException, ParseException{
		String weiyunPath = CommonUtil.getValue("weiyunPath");
		Workbook wb = WorkbookFactory.create(new File(weiyunPath + "/fb/流水账.xlsx"));
		Sheet sheet1 = wb.getSheetAt(0);
	    List<RowTO> datas = new ArrayList<RowTO>();
		for (Row row : sheet1) {
			if(row.getRowNum() <=2){
            	continue;
            }
			int month = (int) row.getCell(0).getNumericCellValue();
			int day = (int) row.getCell(0).getNumericCellValue();
			String year = "2014";
			Date parseDate = CommonUtil.parseDate(String.format("%s/%s/%s", year, month, day));
			if(parseDate.compareTo(fromDate) < 0 || parseDate.compareTo(endDate) > 0){
				continue;
			}
			RowTO temp = new RowTO();
	        for (Cell cell : row) {
	            int columnIndex = cell.getColumnIndex();
	            if(columnIndex ==0){
	            	temp.setMonth((int)cell.getNumericCellValue());
	            } else if(columnIndex ==1){
	            	temp.setDay((int)cell.getNumericCellValue());
	            } else if(columnIndex ==2){
	            	temp.setSubject(cell.getRichStringCellValue().getString());
	            } else if(columnIndex ==3){
	            	temp.setCategory(cell.getRichStringCellValue().getString());
	            } else if(columnIndex ==4){
	            	temp.setMember(cell.getRichStringCellValue().getString());
	            } else if(columnIndex ==5){
	            	temp.setChannel(cell.getRichStringCellValue().getString());
	            } else if(columnIndex ==6){
	            	temp.setAmount(cell.getNumericCellValue());
	            } else if(columnIndex ==7){
	            	temp.setMerchant(cell.getRichStringCellValue().getString());
	            } else if(columnIndex ==8){
	            	temp.setComment(cell.getRichStringCellValue().getString());
	            }
	        }
	        if(temp.flag()){
	        	datas.add(temp);
	        }
	        
	    }
		return datas;
	}
	
	public static Double getTransportCreditCard(String monthStr) throws Exception{
		String weiyunPath = CommonUtil.getValue("weiyunPath");
		String calMonth = String.valueOf(Integer.valueOf(monthStr));
		Date fromDate = CommonUtil.getJTStartDate(calMonth);
		Date endDate = CommonUtil.getJTEndDate(calMonth);
		Workbook wb = WorkbookFactory.create(new File(weiyunPath + "/fb/流水账.xlsx"));
		Sheet sheet1 = wb.getSheetAt(0);
		BigDecimal total = new BigDecimal(0);
		for (Row row : sheet1) {
			if(row.getRowNum() <=2){
            	continue;
            }
			Cell cell5 = row.getCell(5);
			if(cell5 != null){
				String channel = cell5.getStringCellValue();
				if(!"交信卡".equals(channel)){
					continue;
				}
			}
			int month = (int) row.getCell(0).getNumericCellValue();
			int day = (int) row.getCell(1).getNumericCellValue();
			String year = "2014";
			Date parseDate = CommonUtil.parseDate(String.format("%s/%s/%s", year, month, day));

			if(parseDate.compareTo(fromDate) < 0 || parseDate.compareTo(endDate) > 0){
				continue;
			}
            Cell cell6 = row.getCell(6);
			if(cell6 != null){
				double amount = cell6.getNumericCellValue();
				total = total.add(new BigDecimal(amount));
			}
	    }
		return total.setScale(2,   BigDecimal.ROUND_HALF_UP).doubleValue(); 
	}
	
	public static Double getZSCreditCard(String monthStr) throws Exception{
		String weiyunPath = CommonUtil.getValue("weiyunPath");
		String calMonth = String.valueOf(Integer.valueOf(monthStr) - 1);
		Date fromDate = CommonUtil.getZSStartDate(calMonth);
		Date endDate = CommonUtil.getZSEndDate(calMonth);
		Workbook wb = WorkbookFactory.create(new File(weiyunPath + "/fb/流水账.xlsx"));
		Sheet sheet1 = wb.getSheetAt(0);
		BigDecimal total = new BigDecimal(0);
		for (Row row : sheet1) {
			if(row.getRowNum() <=2){
            	continue;
            }
			Cell cell5 = row.getCell(5);
			if(cell5 != null){
				String channel = cell5.getStringCellValue();
				if(!"招信卡".equals(channel)){
					continue;
				}
			}
			int month = (int) row.getCell(0).getNumericCellValue();
			int day = (int) row.getCell(1).getNumericCellValue();
			String year = "2014";
			Date parseDate = CommonUtil.parseDate(String.format("%s/%s/%s", year, month, day));
			if(parseDate.compareTo(fromDate) < 0 || parseDate.compareTo(endDate) > 0){
				continue;
			}
			Cell cell6 = row.getCell(6);
			if(cell6 != null){
				double amount = cell6.getNumericCellValue();
				total = total.add(new BigDecimal(amount));
			}
	    }
		return total.setScale(2,   BigDecimal.ROUND_HALF_UP).doubleValue();
	}
	
	public static Map<String, Map<String, Double>> getFenQi(String month) throws Exception{
		String weiyunPath = CommonUtil.getValue("weiyunPath");
		Workbook wb = WorkbookFactory.create(new File(weiyunPath + "/fb/流水账.xlsx"));
		Sheet sheet1 = wb.getSheetAt(1);
		if(sheet1 == null){
			return null;
		}
		Map<String, Map<String, Double>> result = new HashMap<String, Map<String, Double>>();
		Row row2 = sheet1.getRow(0);
		int index = 0;
		while(row2.getCell(index) != null){
			index += 2;
		}
		int count = index/2;
		for(int i = 1; i<=count; i++){
			String title = sheet1.getRow(0).getCell((i-1)*2).getStringCellValue();
			Map<String, Double> temp = new HashMap<String, Double>();
			for (Row row : sheet1) {
				if(row.getRowNum() == 0){
					continue;
				}
				Cell cell = row.getCell((i-1)*2);
				if(cell == null){
					continue;
				}
				Date fqDate = cell.getDateCellValue();
				if(!CommonUtil.compareMonth(fqDate, month)){
					continue;
				}
				double amount = row.getCell((i-1)*2 + 1).getNumericCellValue();
				temp.put(CommonUtil.formatDate(fqDate), amount);
		    }
			result.put(title, temp);
		}
		return result;
	}


}
