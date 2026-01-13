/**
 * 
 */
package com.nokia.ace.ppa.server;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.eclipse.jgit.util.StringUtils;

/**
 * @author chetana
 *
 */
public class TestMain {

	/**
	 * @param args
	 * @throws Exception 
	 */
	public static void main(String[] args) throws Exception {
//		SimpleDateFormat sdf = new SimpleDateFormat(PmatConstants.dateFormate1);
//		SimpleDateFormat safJiraUpdate = new SimpleDateFormat(PmatConstants.JiraDateFormate);
//
//		String startDate = "2023-07-24 06:20:22.000";
//		String endDate = "2024-11-07 06:20:22.000";
//		String serviceStartDate = "2023-06-20 06:20:22.000";
//		
//		Date startDateP = sdf.parse(startDate);
//		Date endDateP = sdf.parse(endDate);
//		Date servicestartP = sdf.parse(serviceStartDate);
//		
//		long diff_in_time = endDateP.getTime()-startDateP.getTime();
//		
//	//	System.out.println((diff_in_time/ (1000 * 60 * 60 * 24))% 365);
//		
//		long days = (diff_in_time/ (1000 * 60 * 60 * 24))% 365;
//		
//		Instant sI = servicestartP.toInstant();
//		
//		Instant serviceEndDateI = sI.plus(days, ChronoUnit.DAYS);
//		Date finalEndDate = Date.from(serviceEndDateI);
//		String serviceEndDate = safJiraUpdate.format(finalEndDate);
//		
//		
//		System.out.println("service end Date : "+serviceEndDate);

//	     	String date = "2023-06-7 16:46:00.0000000";
//			SimpleDateFormat sdf = new SimpleDateFormat(PmatConstants.dateFormate1);
//			Date dataFromDump = sdf.parse(date);
//			SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");
//			
//			System.out.println("sdf1.format(dataFromDump) : "+sdf1.format(dataFromDump));
////
//			Date dateYesterday = new Date(new Date().getTime() - PmatConstants.MILLIS_IN_A_DAY);
//			String dateYesterdayS = sdf.format(dateYesterday);
//			long yesterday = sdf.parse(dateYesterdayS).getTime();
//			System.out.println(" dateYesterdayS "+dateYesterdayS);
//
//			Date dateToday = new Date(new Date().getTime());
//			String dateTodayS = sdf.format(dateToday);
//			long today = sdf.parse(dateTodayS).getTime();
//			System.out.println(" dateTodayS "+dateTodayS);
//			if(yesterday<dataFromDump && today>dataFromDump) {
//				System.out.println(" true");
//			}else {
//				System.out.println(" false");
//			}
//		List<String> listOne =new ArrayList<>();
//		listOne.add("a");
//		listOne.add("b");
//		listOne.add("c");
//		
//		List<String> listTwo =new ArrayList<>();
//		listTwo.add("a");
//		listTwo.add("c");
//		//listTwo.add("d");
//		
//		  List<String> listOneList = listOne.stream()
//				    .filter(two -> listTwo.stream()
//				    .noneMatch(one -> one.contains(two)))
//				    .collect(Collectors.toList());
//		  
//		  for(String a : listOneList) {
//			  System.out.println(" "+a);
//		  }
	}

}
