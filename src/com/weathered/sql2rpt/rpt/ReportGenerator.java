package com.weathered.sql2rpt.rpt;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Map;

import com.weathered.sql2rpt.log.SchLogger;
import com.weathered.sql2rpt.util.NumberUtil;
import com.weathered.sql2rpt.util.Util;

public class ReportGenerator {
	private static final SchLogger log = new SchLogger(ReportGenerator.class);
	
	public boolean callGenerateReport(String reportName, String sql, String fileName, String fileLocationSource, String rptSrc, Map<String, String> params, String fileFormat) {
		boolean flag = false;
		Util util = new Util();
		
		try {
			Files.createDirectories(Paths.get(fileLocationSource));
			
			log.info("Generating report "+fileName);
			
			rptconn:
			for(int i=0; i<Util.rptretrycount; i++) {
				try {
					flag = generateReport(reportName, sql, fileName, fileLocationSource, rptSrc, params, fileFormat);
				} catch (Exception e) {
					log.error("Failed to generate report file on " + NumberUtil.convert(i) + " try" + util.exceptionToString(e));
				}
				
				if(flag) {
					log.info("Report file generated successfully: "+fileName);
					break rptconn;
				} else {
					log.error("Unable to generate report " + fileName);
				}
				
				try {
					log.info("Waiting " + Util.rptretrydelay + " seconds to retry...");
					Thread.sleep(Util.rptretrydelay*1000);
				} catch (Exception e) {
					
				}
			}
			
			if(!flag) {
				log.error("Unable to generate " + fileName + " trying for " + Util.rptretrycount + " times");
			}
		} catch (Exception e) {
			log.error("Failed to start report generation"+ util.exceptionToString(e));
		}

		return flag;
	}
	
	boolean generateReport(String reportName, String sql, String fileName, String fileLocationSource, String rptSrc, Map<String, String> params, String fileFormat) {
		return new sql2rpt().generateReport(reportName, sql, fileName, fileFormat, fileLocationSource, rptSrc, params);
	}
}
