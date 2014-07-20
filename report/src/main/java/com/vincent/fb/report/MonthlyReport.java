package com.vincent.fb.report;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.vincent.fb.report.to.RowTO;
import com.vincent.fb.report.util.CommonUtil;
import com.vincent.fb.report.util.ExcelUtil;
import com.vincent.fb.report.util.GroupByUtil;

/**
* main entry
*/
public class MonthlyReport {

	public static void main(String[] args) throws Exception {
		String weiyunPath = CommonUtil.getValue("weiyunPath");
		Calendar c = Calendar.getInstance();
		if(args == null || args.length == 0){
			args = new String[1];
			args[0] = String.valueOf(c.get(Calendar.MONTH) + 1);
		}
		
		BufferedWriter writer = null;
		try {
			Date fromDate = CommonUtil.getFromDate(args);
			Date endDate = CommonUtil.getEndDate(args);
			String fileName = String.format(
					weiyunPath + "/fb/report/%s-%s-report.txt",
					CommonUtil.formatDate(fromDate),
					CommonUtil.formatDate(endDate));
			writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(new File(fileName)), "gbk"));
			SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:ss:mm");
			writer.write(String.format(" 统计日期  from : %s  to %s , generated time: %s\r\n",
					CommonUtil.formatDate(fromDate),
					CommonUtil.formatDate(endDate), sf.format(new Date())));
			
			List<RowTO> rowTO = ExcelUtil.getExcelContents(fromDate, endDate);
			List<String> groupBys = new ArrayList<String>();
			groupBys.add("month");
			groupBys.add("day");
			Map<String, String> groupBy = GroupByUtil.groupBy(groupBys, rowTO);
			writer.write("\t分日统计:\r\n");
			BigDecimal tot = new BigDecimal(0);
			for (Entry<String, String> tmp : groupBy.entrySet()) {
				writer.write("\t\t" + tmp.getKey() + "  --  " + tmp.getValue()
						+ "\r\n");
				tot = tot.add(new BigDecimal(tmp.getValue()));
			}
			writer.write("\t\t总计 : "+tot.toString()+"\r\n");
			writer.write("\t信用卡还款预警:\r\n");
			writer.write(String.format("\t\t交通银行信用卡本月底还款: %s\r\n", ExcelUtil.getTransportCreditCard(args[0])));
			writer.write(String.format("\t\t交通银行信用卡下月底还款: %s\r\n", ExcelUtil.getTransportCreditCard(String.valueOf(Integer.valueOf(args[0]) + 1))));
			writer.write("\r\n");
			writer.write(String.format("\t\t招商银行信用卡本月7日还款: %s\r\n", ExcelUtil.getZSCreditCard(args[0])));
			writer.write(String.format("\t\t招商银行信用卡下月7日还款: %s\r\n", ExcelUtil.getZSCreditCard(String.valueOf(Integer.valueOf(args[0]) + 1))));
			writer.write(String.format("\t\t招商银行信用卡下下月7日还款: %s\r\n", ExcelUtil.getZSCreditCard(String.valueOf(Integer.valueOf(args[0]) + 2))));
			writer.write("\r\n");
			Map<String, Map<String, Double>> fenQi = ExcelUtil.getFenQi(args[0]);
			for(Entry<String, Map<String, Double>> temp1 : fenQi.entrySet()){
				String title = temp1.getKey();
				String date = null;
				Double amount = null;
				for(Entry<String, Double> temp2 : temp1.getValue().entrySet()){
					date = temp2.getKey();
					amount = temp2.getValue();
					writer.write(String.format("\t\t%s应于%s还款:%s\r\n", title, date, amount));
				}
				writer.write("\r\n");
			}
			writer.write("\t分主题统计:\r\n");
			groupBys.clear();
			groupBys.add("subject");
			groupBy = GroupByUtil.groupBy(groupBys, rowTO);
			for (Entry<String, String> tmp : groupBy.entrySet()) {
				writer.write("\t\t" + tmp.getKey() + "  --  " + tmp.getValue()
						+ "\r\n");
			}

			writer.write("\t分类别统计:\r\n");
			groupBys.clear();
			groupBys.add("category");
			groupBy = GroupByUtil.groupBy(groupBys, rowTO);
			for (Entry<String, String> tmp : groupBy.entrySet()) {
				writer.write("\t\t" + tmp.getKey() + "  --  " + tmp.getValue()
						+ "\r\n");
			}
			writer.write("\t分人员统计:\r\n");
			groupBys.clear();
			groupBys.add("member");
			groupBy = GroupByUtil.groupBy(groupBys, rowTO);
			for (Entry<String, String> tmp : groupBy.entrySet()) {
				writer.write("\t\t" + tmp.getKey() + "  --  " + tmp.getValue()
						+ "\r\n");
			}
			writer.write("\t分方式统计:\r\n");
			groupBys.clear();
			groupBys.add("channel");
			groupBy = GroupByUtil.groupBy(groupBys, rowTO);
			for (Entry<String, String> tmp : groupBy.entrySet()) {
				writer.write("\t\t" + tmp.getKey() + "  --  " + tmp.getValue()
						+ "\r\n");
			}
			writer.write("\t分商户统计:\r\n");
			groupBys.clear();
			groupBys.add("merchant");
			groupBy = GroupByUtil.groupBy(groupBys, rowTO);
			for (Entry<String, String> tmp : groupBy.entrySet()) {
				
				String merchantName = tmp.getKey();
				if(merchantName == null || merchantName.length() == 0 || "null".equals(merchantName)){
					merchantName = "未知商户";
				}
				writer.write("\t\t" + merchantName + "  --  " + tmp.getValue()
						+ "\r\n");
			}
			System.out.println("生成成功!!");
		} finally {
			if (writer != null) {
				writer.flush();
				writer.close();
			}
		}
	}
}
