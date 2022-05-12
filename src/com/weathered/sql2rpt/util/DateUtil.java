package com.weathered.sql2rpt.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import com.weathered.sql2rpt.log.SchLogger;

public class DateUtil {
	private static final SchLogger log = new SchLogger(DateUtil.class);
	
	public String getReportDate(int dateOffset) {
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DATE, dateOffset);
		
		String pDate = new SimpleDateFormat("dd-MMM-yyyy").format(cal.getTime());
		
		log.info("Report Date = "+ pDate);
		
		return pDate;
	}
	
	public Date getPreviousDate(Date date, int dateOffset) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.DAY_OF_YEAR, dateOffset);
		
		return cal.getTime();
	}
	
	public String getCurrentTime(){
		return (new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").format(new Date()));
	}
	
	public String DateToString(Date _date, String format){
		return (new SimpleDateFormat(format).format(_date)).toUpperCase();
	}
	
	public Date StringToDate(String _date, String format) throws ParseException{
		return (new SimpleDateFormat(format).parse(_date));
	}
	
	public Date getFirstDateOfMonth(String _date, String format) throws ParseException{
		Calendar cal = Calendar.getInstance();
		cal.setTime(StringToDate(_date, format));
		cal.set(Calendar.DAY_OF_MONTH, cal.getActualMinimum(Calendar.DAY_OF_MONTH));
		
		return cal.getTime();
	}
	
	public Date getLastDateOfMonth(String _date, String format) throws ParseException{
		Calendar cal = Calendar.getInstance();
		cal.setTime(StringToDate(_date, format));
		cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH));
		
		return cal.getTime();
	}
	
	public boolean isDateOfSameMonth(Calendar from_date, Calendar to_date){	
		boolean sameMonth = from_date.get(Calendar.YEAR) == to_date.get(Calendar.YEAR) &&
				from_date.get(Calendar.MONTH) == to_date.get(Calendar.MONTH);
		return sameMonth;
	}
}
