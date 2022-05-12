package com.weathered.sql2rpt.util;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import java.sql.Connection;

import com.weathered.bo.AttachmentInfo;
import com.weathered.bo.DbInfo;
import com.weathered.bo.FtpServerInfo;
import com.weathered.bo.MailServerInfo;
import com.weathered.bo.ReportInfo;
import com.weathered.sql2rpt.log.SchLogger;

public class ReportUtil {
	private static final SchLogger log = new SchLogger(ReportUtil.class);
		
	public ResultSet getData(String sql) {
		Statement stm = null;
		ResultSet rs = null;
		
		Connection conn = new DbUtil().getSchDbConnection();
		
		if(conn!=null) {
			try {
				stm = conn.createStatement();
				rs = stm.executeQuery(sql);
			} catch (SQLException e) {
				log.error("Could not get data"+new Util().exceptionToString(e));
			}
		} else {
			log.error("!!!!Could not fetch data from scheduler database!!!!");
		}
		
		return rs;
	}
	
	public ReportInfo getReportInfo(String reportId) {
		String sql = "SELECT * FROM AUTOMAIL_REPORT_INFO WHERE RPT_ID = "+reportId;

		ReportInfo reportInfo = new ReportInfo();
		
		ResultSet rs = getData(sql);
		
		try {
			if (rs.next()) {
				reportInfo.setRptId(rs.getString("RPT_ID"));
				reportInfo.setReportName(rs.getString("REPORT_NAME"));
				reportInfo.setSender(rs.getString("SENDER"));
				reportInfo.setMailTo(rs.getString("MAIL_TO"));
				reportInfo.setMailCc(rs.getString("MAIL_CC"));
				reportInfo.setMailBcc(rs.getString("MAIL_BCC"));
				reportInfo.setMailSubject(rs.getString("MAIL_SUBJECT"));
				reportInfo.setMailBody(rs.getString("MAIL_BODY"));
				reportInfo.setStatus(rs.getString("STATUS"));
				reportInfo.setConfigDir(rs.getString("CONFIG_DIR"));
				reportInfo.setLocalDir(rs.getString("LOCAL_DIR"));
				reportInfo.setMailServerId(rs.getString("MAIL_SERVER_ID"));
				reportInfo.setFtpServerId(rs.getString("FTP_SERVER_ID"));
				reportInfo.setAsZip(rs.getString("AS_ZIP"));
				reportInfo.setFtpTransfer(rs.getString("FTP_TRANSFER"));
				reportInfo.setSendEmail(rs.getString("SEND_EMAIL"));
			}
		} catch (SQLException e) {
			log.error("Failed to fetch report info"+new Util().exceptionToString(e));
		}
		
		return reportInfo;
	}
	
	public ArrayList<AttachmentInfo> getAllAttachments(String reportId){
		String sql = "SELECT * FROM AUTOMAIL_REPORT_ATTACHMENT_INFO WHERE RPT_ID = "+reportId;
		
		ArrayList<AttachmentInfo> attachmentInfos = new ArrayList<>();       
	
		ResultSet rs = getData(sql);
		try{          
			while(rs.next()) {
				AttachmentInfo attachmentInfo = new AttachmentInfo();
				attachmentInfo.setRptId(rs.getString("RPT_ID"));
				attachmentInfo.setFileName(rs.getString("FILE_NAME"));
				attachmentInfo.setRptQuery(rs.getString("RPT_QUERY"));
				attachmentInfo.setRptCondition(rs.getString("RPT_CONDITION"));
				attachmentInfo.setRptCondQueryOrVal(rs.getString("RPT_COND_QUERY_OR_VAL"));
				attachmentInfo.setExecCondition(rs.getString("EXEC_CONDITION"));
				attachmentInfo.setExecCondQueryOrVal(rs.getString("EXEC_COND_QUERY_OR_VAL"));
				attachmentInfo.setRptSrc(rs.getString("RPT_SRC"));
				attachmentInfo.setFileFormat(rs.getString("FILE_FORMAT"));
				attachmentInfo.setIsPrevDayRptReq(rs.getString("IS_PREV_DAY_RPT_REQ"));
				
				attachmentInfos.add(attachmentInfo);
			}            
		}
		catch(Exception e){
			log.error("Failed to fetch attachment info" + new Util().exceptionToString(e));
		}
		
		return attachmentInfos;
	}
	
	public ArrayList<DbInfo> getReportDbInfo(String rptSrc) {
		
		String sql = "SELECT NAME, CONN_STRING, USERNAME, PASSWORD FROM VW_RPT_DATABASE WHERE RPT_SRC = '"+rptSrc+"'";

		ArrayList<DbInfo> dbInfos = new ArrayList<>();       
		
		ResultSet rs = getData(sql);
		try{          
			while(rs.next()) {
				DbInfo dbInfo = new DbInfo();
				dbInfo.setName(rs.getString("NAME"));
				dbInfo.setConnString(rs.getString("CONN_STRING"));
				dbInfo.setUsername(rs.getString("USERNAME"));
				dbInfo.setPassword(rs.getString("PASSWORD"));
				
				dbInfos.add(dbInfo);
			}            
		}
		catch(Exception e){
			log.error("Failed to fetch report source (database) info" + new Util().exceptionToString(e));
		}
		
		return dbInfos;
	}
	
	public MailServerInfo getMailServerInfo(String mailServerId) {
		String sql = "SELECT * FROM SCH_MAIL_SERVER WHERE MAIL_SERVER_ID = "+mailServerId;

		MailServerInfo mailServerInfo = new MailServerInfo();
		
		ResultSet rs = getData(sql);
		
		try {
			if (rs.next()) {
				mailServerInfo.setMailServerId(rs.getString(1));
				mailServerInfo.setName(rs.getString(2));
				mailServerInfo.setMailHost(rs.getString(3));
				mailServerInfo.setMailUser(rs.getString(4));
				mailServerInfo.setMailPassword(rs.getString(5));
				mailServerInfo.setMailPort(rs.getString(6));
				mailServerInfo.setMailProtocol(rs.getString(7));
				mailServerInfo.setMailStarttlsEnable(rs.getString(8));
			}
		} catch (SQLException e) {
			log.error("Failed to fetch email server configuration" + new Util().exceptionToString(e));
		}
		
		return mailServerInfo;
	}
	
	public FtpServerInfo getFtpServerInfo(String serverId) {
		String sql = "SELECT * FROM SCH_FTP_SERVER WHERE SERVER_ID = "+serverId;

		FtpServerInfo ftpServerInfo = new FtpServerInfo();
		
		ResultSet rs = getData(sql);
		
		try {
			if (rs.next()) {
				ftpServerInfo.setServerId(rs.getString("SERVER_ID"));
				ftpServerInfo.setServerName(rs.getString("SERVER_NAME"));
				ftpServerInfo.setFtpHost(rs.getString("FTP_HOST"));
				ftpServerInfo.setFtpUser(rs.getString("FTP_USER"));
				ftpServerInfo.setFtpPassword(rs.getString("FTP_PASSWORD"));
				ftpServerInfo.setFtpPort(rs.getString("FTP_PORT"));
				ftpServerInfo.setFtpProtocol(rs.getString("FTP_PROTOCOL"));
				ftpServerInfo.setDefaultDirectory(rs.getString("DEFAULT_DIRECTORY"));
			}
		} catch (SQLException e) {
			log.error("Failed to fetch FTP/SFTP server configuration" + new Util().exceptionToString(e));
		}
		
		return ftpServerInfo;
	}
	
	public boolean cumulativeFlag(ArrayList<Boolean> flags) {
		boolean flag = true;
		
		for (int i=0; i<flags.size(); i++) {
			flag = flag && flags.get(i);
		}
		
		return flag;
	}
	
	public boolean isReportConditionMet(AttachmentInfo attachmentInfo, boolean prevdayflag) {
		boolean flag = false;
		
		if(attachmentInfo.getIsPrevDayRptReq().equalsIgnoreCase("Y")) {
			if(!prevdayflag) {
				return false;
			}
		}
		
		DbUtil dbUtil = new DbUtil();
		
		String rptCondition = null,
			   execCondition = null,
			   rptCondQueryOrVal = attachmentInfo.getRptCondQueryOrVal(),
			   execCondQueryOrVal = attachmentInfo.getExecCondQueryOrVal();
		
		if(rptCondQueryOrVal != null) {
			if(rptCondQueryOrVal.equalsIgnoreCase("V")) {
				rptCondition = attachmentInfo.getRptCondition();
			} else if (rptCondQueryOrVal.equalsIgnoreCase("Q")) {
				rptCondition = dbUtil.getSingleValue(attachmentInfo.getRptCondition(), attachmentInfo.getRptSrc());
			} else {
				log.error("Report condition not configured properly.");
			}
		}
		
		if(execCondQueryOrVal != null) {
			if(execCondQueryOrVal.equalsIgnoreCase("V")) {
				execCondition = attachmentInfo.getExecCondition();
			} else if (execCondQueryOrVal.equalsIgnoreCase("Q")) {
				execCondition = dbUtil.getSingleValue(attachmentInfo.getExecCondition(), attachmentInfo.getRptSrc());
			} else {
				log.error("Report execution condition not configured properly.");
			}
		}
						
		if (rptCondition != null && execCondition != null) {
			if(rptCondition.equalsIgnoreCase(execCondition)) {
				flag = true;
			}
		} else {
			log.error("Are report conditions set properly? If there is no condition, make sure they are set as same values, for example: 1 (Report Condition) and 1 (Execution Condition). 1=1 is true.");
		}
		
		return flag;
	}
}
