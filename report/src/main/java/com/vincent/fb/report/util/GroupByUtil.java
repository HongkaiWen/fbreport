package com.vincent.fb.report.util;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.vincent.fb.report.to.RowTO;

public class GroupByUtil {
	
	public static Map<String, String> groupBy(List<String> groupBys, List<RowTO> source) throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException{
		
		Map<String, String> result = new LinkedHashMap<String, String>();
		for(RowTO tmp : source){
			StringBuffer key = new StringBuffer();
			for(String groupBy : groupBys){
				groupBy = groupBy.substring(0, 1).toUpperCase() + groupBy.substring(1);
				Method titleMethod = tmp.getClass().getMethod(String.format("get%s", groupBy));
				key.append("-").append(titleMethod.invoke(tmp));
			}
			String title = key.substring(1);
			BigDecimal acmount = new BigDecimal(tmp.getAmount());
			String val = result.get(title);
			if(val != null){
				BigDecimal plus = new BigDecimal(val);
				result.put(title, plus.add(acmount).setScale(2, BigDecimal.ROUND_HALF_UP).toString());
			}else{
				result.put(title, acmount.setScale(2, BigDecimal.ROUND_HALF_UP).toString());
			}
		}
		return result;
		
	}
}
