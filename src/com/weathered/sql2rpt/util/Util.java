package com.weathered.sql2rpt.util;

import java.io.FileInputStream;
import java.util.Properties;

import com.weathered.sql2rpt.log.SchLogger;

public class Util {
	private static final SchLogger log = new SchLogger(Util.class);
	
	static String schedulerPropertiesFileLocation = "D:/REPORT_FOLDER/SCH_CONFIG/sch_config.properties";
	
	public static String dbconnstring;
	public static String dbuser;
	public static String dbpass;

	public static String mailhost;
	public static String mailuser;
	public static String mailport;
	public static String mailpass;
	
	public static int dbretrycount;
	public static int dbretrydelay;
	
	public static int rptretrycount;
	public static int rptretrydelay;
	
	public static int mailretrycount;
	public static int mailretrydelay;
	
	public static int ftpretrycount;
	public static int ftpretrydelay;
	
	public static int schretrycount;
	public static int schretrydelay;
	
	public static String admtomail;
	public static String admccmail;
		
	public boolean loadSchedulerProperties() {
		Properties prop = new Properties();
		try {
			prop.load(new FileInputStream(schedulerPropertiesFileLocation));
			dbconnstring = prop.getProperty("sch.dbconnstring");
			dbuser = prop.getProperty("sch.dbuser");
			dbpass = prop.getProperty("sch.dbpass");
			
			mailhost = prop.getProperty("sch.mailhost");
			mailport = prop.getProperty("sch.mailport");
			mailuser = prop.getProperty("sch.mailuser");
			mailpass = prop.getProperty("sch.mailpass");
			
			schretrycount = Integer.parseInt(prop.getProperty("sch.retry_count"));
			schretrydelay = Integer.parseInt(prop.getProperty("sch.retry_delay"));
			
			dbretrycount = Integer.parseInt(prop.getProperty("db.retry_count"));
			dbretrydelay = Integer.parseInt(prop.getProperty("db.retry_delay"));
			
			rptretrycount = Integer.parseInt(prop.getProperty("rpt.retry_count"));
			rptretrydelay = Integer.parseInt(prop.getProperty("rpt.retry_delay"));
			
			mailretrycount = Integer.parseInt(prop.getProperty("mail.retry_count"));
			mailretrydelay = Integer.parseInt(prop.getProperty("mail.retry_delay"));
			
			ftpretrycount = Integer.parseInt(prop.getProperty("ftp.retry_count"));
			ftpretrydelay = Integer.parseInt(prop.getProperty("ftp.retry_delay"));
			
			admtomail = prop.getProperty("adm.tomail");
			admccmail = prop.getProperty("adm.ccmail");
		} catch (Exception e) {
			log.error("!!!! Failed to load properties of this program!: "+exceptionToString(e));
		}
		
		return false;
	}
	
	public String exceptionToString(Exception e){
		try{
			return " - Exception: "+e.getMessage();
		} catch(Exception ex) {
			return "failed to convert exception to string" + ex.getMessage();
		}	
	}
	
	public String exceptionToString(Throwable t){
		try{
			return " - Exception: "+t.getMessage();
		} catch(Exception ex) {
			return "failed to convert exception to string" + ex.getMessage();
		}	
	}
	
	public boolean notifyAdmin(String reportName, boolean rptconditionflag, boolean rptgenflag, boolean transferflag, boolean mailflag, boolean zipflag) {
		boolean flag = false;
		String mailSubject, mailBody;
		
		mailSubject = "[AUTOMAIL] Automail process failed for "+reportName;
		mailBody = 
		"Dear Sir,"+System.lineSeparator()+System.lineSeparator()+
		"Following errors have occured for the automail: " + reportName + System.lineSeparator();
		
		if(!rptconditionflag) {
			mailBody += "- Report Conditions not met" + System.lineSeparator();
		} else {
			if(!rptgenflag) {
				mailBody += "- Report Generation Failed" + System.lineSeparator();
			} else {
				if(!zipflag) {
					mailBody += "- Report Zipping Failed" + System.lineSeparator();
				}
				
				if(!transferflag) {
					mailBody += "- File Transfer to SFTP Failed" + System.lineSeparator();
				}
				
				if(!mailflag) {
					mailBody += "- Internal Email Failed" + System.lineSeparator();
				}
			}
		}
		
		mailBody +=
		System.lineSeparator( )+
		"Kindly look into the issue." + System.lineSeparator() + System.lineSeparator() +
		"Best Regards," + System.lineSeparator() +
		"DBBL" + System.lineSeparator() + System.lineSeparator();
		
		flag = new MailUtil().sendEmailToAdmin(reportName, mailSubject, mailBody);
		
		return flag;
	}
}
