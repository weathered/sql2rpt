package com.weathered.sql2rpt.rpt;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Map;

import com.weathered.bo.AttachmentInfo;
import com.weathered.bo.FtpServerInfo;
import com.weathered.bo.MailServerInfo;
import com.weathered.bo.ReportInfo;
import com.weathered.sql2rpt.log.SchLogger;
import com.weathered.sql2rpt.util.DateUtil;
import com.weathered.sql2rpt.util.FileUtil;
import com.weathered.sql2rpt.util.MailUtil;
import com.weathered.sql2rpt.util.NumberUtil;
import com.weathered.sql2rpt.util.ReportUtil;
import com.weathered.sql2rpt.util.Util;

public class BaseReport extends ReportSchedulerBase {
	private static final SchLogger log = new SchLogger(BaseReport.class);
	
	public boolean generateReportCaller(String _reportId, String pDate, Map<String, String> params) {
		boolean flag = false;
		schconn:
		for(int i=0; i<Util.schretrycount; i++) {
			try {
				flag = new BaseReport().generateReport(_reportId, pDate, params);
			} catch (Exception e) {
				log.error("Failed automail on " + NumberUtil.convert(i) + " try" + new Util().exceptionToString(e));
			}
			
			if(flag) {
				log.info("Automail completed successfully");
				break schconn;
			} else {
				log.error("Unable to complete automail process properly");
			}
			
			try {
				log.info("Waiting " + Util.schretrydelay + " seconds to retry...");
				Thread.sleep(Util.schretrydelay*1000);
			} catch (Exception e) {
				
			}
		}
		
		return flag;
	}
	
	private boolean generateReport(String _reportId, String pDate, Map<String, String> params) throws ParseException  {		
	    DateUtil dateUtil = new DateUtil();
		ReportUtil reportUtil = new ReportUtil();
		FileUtil fileUtil = new FileUtil();
		MailUtil mailUtil = new MailUtil();
		Util util = new Util();
		
		boolean mailflag = false;
		ArrayList<Boolean> rptconditionflag = new ArrayList<>(),
						   rptgenflag = new ArrayList<>(),
						   transferflag = new ArrayList<>(),
						   zipflag = new ArrayList<>();
		
		ReportInfo rptInfo = reportUtil.getReportInfo(_reportId);
		FtpServerInfo ftpServerInfo = reportUtil.getFtpServerInfo(rptInfo.getFtpServerId());
		MailServerInfo mailServerInfo = reportUtil.getMailServerInfo(rptInfo.getMailServerId());
		ArrayList<AttachmentInfo> attachments = reportUtil.getAllAttachments(rptInfo.getRptId());
		ArrayList<String> att = new ArrayList<>();
		
		try {
			rptInfo.setLocalDir(fileUtil.getReportGenerationLocation(rptInfo, dateUtil.StringToDate(pDate, "dd-MMM-yyyy")));
		} catch (ParseException e1) { }
		
		boolean ftpTransfer = rptInfo.getFtpTransfer().equalsIgnoreCase("Y"),
				sendEmail = rptInfo.getSendEmail().equalsIgnoreCase("Y"),
				asZip = rptInfo.getAsZip().equalsIgnoreCase("Y");
		
		for (int i = 0; i<attachments.size(); i++) {
			AttachmentInfo attachment = attachments.get(i);
			String reportName = fileUtil.getFinalName(rptInfo.getReportName(), params);
			boolean prevdayflag = fileUtil.isPrevDayRptGenerated(attachment, dateUtil.StringToDate(pDate, "dd-MMM-yyyy"), params);
			
			if (reportUtil.isReportConditionMet(attachment, prevdayflag)) {
				rptconditionflag.add(true);
				try {
					String fileName = String.join(".", fileUtil.getFinalName(attachment.getFileName(), params), attachment.getFileFormat());
					
					String rptSql = attachment.getRptQuery();
					
					boolean genFlag = new ReportGenerator().callGenerateReport(reportName, rptSql, fileName, rptInfo.getLocalDir(), attachment.getRptSrc(), params, attachment.getFileFormat());
					rptgenflag.add(genFlag);
					
					if(genFlag) {
						if(asZip) {
							try {
								zipflag.add(fileUtil.zipReportFile(rptInfo.getLocalDir(), fileName));
								
								fileName = fileUtil.removeExtension(fileName)+".zip";
								
								att.add(fileName);
							} catch (Exception e) {
								log.error("Failed to zip file" + util.exceptionToString(e));
							}
						} else {
							att.add(fileName);
						}
						
						if(ftpTransfer) {
							try {
								transferflag.add(fileUtil.callTransferFile(rptInfo.getLocalDir(), fileName, ftpServerInfo));
							} catch (Exception e) {
								log.error("Failed to transfer file" + util.exceptionToString(e));
							}
						}
					} else {
						if(asZip) zipflag.add(false);
						if(ftpTransfer) transferflag.add(false);
					}
				}
				catch(Exception e){ 
					log.error("Error occurred while processing report" + util.exceptionToString(e));
				} 
			} else {
				rptconditionflag.add(false);
				rptgenflag.add(false);
				transferflag.add(false);
				zipflag.add(false);
				log.error("Report generation condition not met for " + reportName);
			}
		}
		
		if(sendEmail) {
			if (attachments.size()==att.size()) {
				try {
					mailflag = mailUtil.callSendEmail(rptInfo, mailServerInfo, att, params);
				} catch (Exception e) {
					log.error("Failed to initiate email"+ util.exceptionToString(e));
				}
			} else {
				log.error("All the attachments were not generated, please check the error log");
			}
		}
		
		if(	   !reportUtil.cumulativeFlag(rptconditionflag)
			|| !reportUtil.cumulativeFlag(rptgenflag)
			|| (sendEmail && !mailflag)
			|| (ftpTransfer && !reportUtil.cumulativeFlag(transferflag))
			|| (asZip && !reportUtil.cumulativeFlag(zipflag))  )
		{
			boolean adminNotifFlag = util.notifyAdmin ( fileUtil.getFinalName(rptInfo.getMailSubject(), params),
														reportUtil.cumulativeFlag(rptconditionflag),
														reportUtil.cumulativeFlag(rptgenflag),
														(!ftpTransfer || reportUtil.cumulativeFlag(transferflag)),
														(!sendEmail || mailflag),
														(!asZip || reportUtil.cumulativeFlag(zipflag)));
			if(!adminNotifFlag) {
				log.error("Failed to send notification to admin");
			}
			
			return false;
		} else {
			return true;
		}
	}
}
