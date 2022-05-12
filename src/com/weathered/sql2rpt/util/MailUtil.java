package com.weathered.sql2rpt.util;

import java.util.ArrayList;
import java.util.Date;
import java.util.Map;
import java.util.Properties;

import com.weathered.bo.MailServerInfo;
import com.weathered.bo.ReportInfo;
import com.weathered.sql2rpt.log.SchLogger;

import javax.activation.*;
import javax.mail.*;
import javax.mail.internet.*;

public class MailUtil {
	private static final SchLogger log = new SchLogger(MailUtil.class);
	
	private void addAttachment(Multipart multipart, String absolutePath, String fileName) {
		Util util = new Util();
		
		try {
			FileDataSource source = new FileDataSource(absolutePath);
			BodyPart messageBodyPart = new MimeBodyPart();
			messageBodyPart.setDataHandler(new DataHandler(source));
			messageBodyPart.setFileName(fileName);
			multipart.addBodyPart(messageBodyPart);
		} catch (Exception e) {
			log.error("Failed to attach file " + fileName + util.exceptionToString(e));
		}
	}
	
	boolean sendEmail(ReportInfo rptInfo, MailServerInfo mailServerInfo, ArrayList<String> attachments, Map<String, String> val) {
		FileUtil fileUtil = new FileUtil();
		Util util = new Util();
		
		log.info("Initializing email for " + fileUtil.getFinalName(rptInfo.getMailSubject(), val));
		
		String cc, bcc;
		try {
			Properties props = new Properties();

			props.put("mail.transport.protocol", mailServerInfo.getMailProtocol());
			props.put("mail.smtp.host", mailServerInfo.getMailHost());
			props.put("mail.smtp.auth", "true");
			props.put("mail.smtp.port", Integer.parseInt(mailServerInfo.getMailPort()));
			props.put("mail.smtp.connectiontimeout", 5000);
			props.put("mail.smtp.timeout", 5000);
			if(mailServerInfo.getMailStarttlsEnable() != null)
				props.put("mail.smtp.starttls.enable", mailServerInfo.getMailStarttlsEnable());

			Session session = Session.getInstance(props,
					new Authenticator() {
						protected PasswordAuthentication getPasswordAuthentication() {
							return new PasswordAuthentication(mailServerInfo.getMailUser(), new TextUtil().decode(mailServerInfo.getMailPassword()));
						}
					});

			Message msg = new MimeMessage(session);
			
			msg.setFrom(new InternetAddress(mailServerInfo.getMailUser(), rptInfo.getSender()));

			// Recepient
			String[] splittedToEmail = rptInfo.getMailTo().split(";");
			for (String toMail : splittedToEmail) {
				if ( !toMail.isEmpty() || !toMail.equals("") || toMail.equals("null") || null == toMail ) {
					msg.addRecipient(Message.RecipientType.TO, new InternetAddress(toMail));
				}
			}
			// CC
			if ( rptInfo.getMailCc() == null ){
				cc = "";
			} else {
				cc = rptInfo.getMailCc();
			}
			
			try{
				String[] splittedCcEmail = cc.split(";");
				for (String ccMail : splittedCcEmail) {
					if (!ccMail.isEmpty() || !ccMail.equals("")) {
						msg.addRecipient(Message.RecipientType.CC, new InternetAddress(ccMail));
					}
				}
			} catch (Exception e){
				log.error("Could not process mail cc addresses, are they provided properly?" + util.exceptionToString(e));
			}
			

			// BCC
			if ( rptInfo.getMailBcc() == null ){
				bcc = "";
			} else {
				bcc = rptInfo.getMailBcc();
			}
			try{
				String[] splittedBccEmail = bcc.split(";");
				for (String bccMail : splittedBccEmail) {
					if (!bccMail.isEmpty() || !bccMail.equals("") ) {
						msg.addRecipient(Message.RecipientType.BCC, new InternetAddress(bccMail));
					}
				}
			} catch (Exception e){
				log.error("Could not process mail bcc addresses, are they provided properly?" + util.exceptionToString(e));
			}
		
			msg.setSubject(fileUtil.getFinalName(rptInfo.getMailSubject(), val));
			msg.setSentDate(new Date());

			BodyPart messageBodyPart = new MimeBodyPart();
			messageBodyPart.setText(fileUtil.getFinalName(rptInfo.getMailBody(), val));

			Multipart multipart = new MimeMultipart();
			multipart.addBodyPart(messageBodyPart);

			for (String att : attachments) {
				log.info("Attached report = " + att);
				addAttachment(multipart, rptInfo.getLocalDir() + att, att);
			}

			msg.setContent(multipart);

			Transport.send(msg);
			
			return true;
		} catch (Exception e) {
			log.error("Could not send email" + util.exceptionToString(e));
			return false;
		}
	}
	
	public boolean callSendEmail(ReportInfo rptInfo, MailServerInfo mailServerInfo, ArrayList<String> attachments, Map<String, String> val) {
		boolean flag = false;
		FileUtil fileUtil = new FileUtil();
		Util util = new Util();
		
		log.info("Sending email for " + fileUtil.getFinalName(rptInfo.getMailSubject(), val));
		
		ftpconn:
		for(int i=0; i<Util.mailretrycount; i++) {
			try {
				flag = sendEmail(rptInfo, mailServerInfo, attachments, val);
			} catch (Exception e) {
				log.error("Failed to send email file on " + NumberUtil.convert(i) + " try" + util.exceptionToString(e));
			}
			
			if(flag) {
				break ftpconn;
			} else {
				log.error("Unable to send email");
			}
			
			try {
				log.info("Waiting "+Util.mailretrydelay + " seconds to retry...");
				Thread.sleep(Util.mailretrydelay*1000);
			} catch (Exception e) {
				
			}
		}
		
		if(flag) {
			log.info("Email sent successfully for " + fileUtil.getFinalName(rptInfo.getMailSubject() + " via " + mailServerInfo.getName(), val));
		} else {
			log.error("Failed to send email for  " + fileUtil.getFinalName(rptInfo.getMailSubject(), val) + " trying for " + Util.mailretrycount + " times via " + mailServerInfo.getName());
		}
		
		return flag;
	}
	
	public boolean sendEmailToAdmin(String reportName, String mailSubject, String mailBody) {
		Util util = new Util();
		
		log.info("[[ ADMIN ]] Initializing email notification for: " + reportName);
		
		String cc;
		try {
			Properties props = new Properties();

			props.put("mail.transport.protocol", "smtp");
			props.put("mail.smtp.host", Util.mailhost);
			props.put("mail.smtp.auth", "true");
			props.put("mail.smtp.port", Integer.parseInt(Util.mailport));
			props.put("mail.smtp.connectiontimeout", 5000);
			props.put("mail.smtp.timeout", 5000);

			Session session = Session.getInstance(props,
					new Authenticator() {
						protected PasswordAuthentication getPasswordAuthentication() {
							return new PasswordAuthentication(Util.mailuser, new TextUtil().decode(Util.mailpass));
						}
					});

			Message msg = new MimeMessage(session);
			
			msg.setFrom(new InternetAddress(Util.mailuser, "Automail Scheduler"));

			// Recepient
			String[] splittedToEmail = Util.admtomail.split(";");
			for (String toMail : splittedToEmail) {
				if ( !toMail.isEmpty() || !toMail.equals("") || toMail.equals("null") || null == toMail ) {
					msg.addRecipient(Message.RecipientType.TO, new InternetAddress(toMail));
				}
			}
			// CC
			if ( Util.admccmail == null ){
				cc = "";
			} else {
				cc = Util.admccmail;
			}
			
			try{
				String[] splittedCcEmail = cc.split(";");
				for (String ccMail : splittedCcEmail) {
					if (!ccMail.isEmpty() || !ccMail.equals("")) {
						msg.addRecipient(Message.RecipientType.CC, new InternetAddress(ccMail));
					}
				}
			} catch (Exception e){
				log.error("Could not process mail cc addresses, are they provided properly?" + util.exceptionToString(e));
			}
			
			// BCC
			try{
				String[] splittedBccEmail = "mohammad.alamin5993@dbbl.com".split(";");
				for (String bccMail : splittedBccEmail) {
					if (!bccMail.isEmpty() || !bccMail.equals("") ) {
						msg.addRecipient(Message.RecipientType.BCC, new InternetAddress(bccMail));
					}
				}
			} catch (Exception e){
				log.error("Could not process mail bcc addresses, are they provided properly?" + util.exceptionToString(e));
			}
		
			msg.setSubject(mailSubject);
			msg.setSentDate(new Date());

			BodyPart messageBodyPart = new MimeBodyPart();
			messageBodyPart.setText(mailBody);
			
			Multipart multipart = new MimeMultipart();
			multipart.addBodyPart(messageBodyPart);
			msg.setContent(multipart);
			
			Transport.send(msg);
			log.info("[[ ADMIN ]] Email notification to admin for " + msg.getSubject() + " sent successfully");
			return true;
		} catch (Exception e) {
			log.error("[[ ADMIN ]] Could not send email notification to admin" + util.exceptionToString(e));
			e.printStackTrace();
			return false;
		}
	}
}
