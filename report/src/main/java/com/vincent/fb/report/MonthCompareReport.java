package com.vincent.fb.report;

import com.vincent.fb.report.to.RowTO;
import com.vincent.fb.report.util.CommonUtil;
import com.vincent.fb.report.util.ExcelUtil;
import com.vincent.fb.report.util.GroupByUtil;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.math.BigDecimal;
import java.util.*;
import java.text.SimpleDateFormat;

/**
 * Created by susu on 2014/7/21.
 */
public class MonthCompareReport {

    public static void main(String args[]) throws Exception{
        String weiyunPath = CommonUtil.getValue("weiyunPath");
        SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd_HH:ss:mm");
        String reportFile = weiyunPath + "/fb/report/summary.txt";

        BufferedWriter writer = null;
        try{
            writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(new File(reportFile)), "gbk"));

            for(int i=5; i < 13; i++){
                args = new String[1];
                args[0] = String.valueOf(i);
                Date fromDate = CommonUtil.getFromDate(args);
                Date endDate = CommonUtil.getEndDate(args);
                Calendar endDay = Calendar.getInstance();
                endDay.setTime(endDate);
                List<RowTO> rowTO = ExcelUtil.getExcelContents(fromDate, endDate);
                if(rowTO.size() == 0){
                    continue;
                }
                BigDecimal total = new BigDecimal(0);

                BigDecimal workDayTotal = new BigDecimal(0);

                BigDecimal weekendTatal = new BigDecimal(0);

                for(RowTO tmp : rowTO){
                    int month = tmp.getMonth();
                    int day = tmp.getDay();
                    String year = "2014";
                    Date parseDate = CommonUtil.parseDate(String.format("%s/%s/%s", year, month, day));
                    Calendar c = Calendar.getInstance();
                    c.setTime(parseDate);
                    int dayOfWeek = c.get(Calendar.DAY_OF_WEEK);
                    if(tmp.flag()){
                        total =total.add(new BigDecimal(tmp.getAmount()));
                        if(dayOfWeek == 0 || dayOfWeek == 6){
                            weekendTatal = weekendTatal.add(new BigDecimal(tmp.getAmount()));
                        }else{
                            workDayTotal = workDayTotal.add(new BigDecimal(tmp.getAmount()));
                        }
                    }

                }
                BigDecimal avg = null;
                if(i ==  5){
                    avg = total.divide(new BigDecimal(23), 4 , BigDecimal.ROUND_UP);
                }else{
                    avg = total.divide(new BigDecimal(endDay.get(Calendar.DAY_OF_MONTH)), 4 , BigDecimal.ROUND_UP);
                }


                java.text.DecimalFormat df=new java.text.DecimalFormat("0.00");
                writer.write("【" + i + " 月】 总消费：" +df.format(total) + "， 工作日： " +df.format(workDayTotal) + ", 周末：" + df.format(weekendTatal) + ", 日均： " + avg.toString());
                writer.write("\r\n");



            }

            List<RowTO> rowTO = ExcelUtil.getExcelContents(CommonUtil.parseDate(CommonUtil.STARTDATE, "yyyyMMdd"), new Date());
            List<String> groupBys = new ArrayList<String>();
            groupBys.add("subject");
            groupBys.add("category");
            Map<String, String> count =  GroupByUtil.groupByCount(groupBys, rowTO);
            writer.write("\r\n");
            writer.write("\r\n");
            writer.write("\r\n");

            ValueComparator comparator = new ValueComparator(count);
            TreeMap<String,String> sorted_map = new TreeMap<String,String>(comparator);
            sorted_map.putAll(count);

            for(Map.Entry<String, String> tmp : sorted_map.entrySet()){
                String title = tmp.getKey();
                String val = tmp.getValue();
                String pl = new BigDecimal(CommonUtil.getTotalStatisticsDays()).divide(new BigDecimal(val),  2 , BigDecimal.ROUND_UP).toString();

                    writer.write(String.format("%s 发生频率： %s 天", title, pl));
                

                writer.write("\r\n");
            }


            System.out.println("生成成功！");
        }finally{
            if(writer != null){
                writer.flush();
                writer.close();
            }


        }

    }

    static class ValueComparator implements Comparator<String> {

        Map<String, String> base;
        public ValueComparator(Map<String, String> base) {
            this.base = base;
        }

        // Note: this comparator imposes orderings that are inconsistent with equals.
        public int compare(String a, String b) {
            if (Double.parseDouble(base.get(a)) >= Double.parseDouble(base.get(b))) {
                return -1;
            } else {
                return 1;
            } // returning 0 would merge keys
        }
    }

}
