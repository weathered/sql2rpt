package com.weathered.sql2rpt.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.Reader;
import java.util.Date;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.apache.commons.io.FilenameUtils;
import org.json.simple.JSONArray;
import org.json.simple.parser.JSONParser;

import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;

import com.weathered.bo.AttachmentInfo;
import com.weathered.bo.FtpServerInfo;
import com.weathered.bo.ReportInfo;
import com.weathered.sql2rpt.log.SchLogger;

public class FileUtil {
	private static final SchLogger log = new SchLogger(FileUtil.class);
	
	boolean isEmptyFile(String source){
		try{
			File file = new File(source);
			if(file.isFile() && file.exists()){
				long size = file.length();
				if(size!=0){
					return false;
				}else{
					return true;
				}
			}else{
				return true;
			}
		} catch(Exception e) {
			log.error("Could not confirm is file is empty or not" + new Util().exceptionToString(e));
			return false;
		}
	}
	
	public String removeExtension(String filename) {
		return FilenameUtils.removeExtension(filename);
	}
	
	public boolean isPrevDayRptGenerated(AttachmentInfo attachment, Date date, Map<String, String> params) {
		ReportInfo reportInfo = new ReportUtil().getReportInfo(attachment.getRptId());
		String prevDayDir = getReportGenerationLocation(reportInfo, new DateUtil().getPreviousDate(date, -1));
		boolean asZip = reportInfo.getAsZip().equalsIgnoreCase("Y");
		
		String fileName = String.join(".", new FileUtil().getFinalName(attachment.getFileName(), params), attachment.getFileFormat());
		
		if(asZip) {
			fileName = removeExtension(fileName)+".zip";
		}
		
		return !isEmptyFile(prevDayDir+fileName);
	}
	
	public String getReportGenerationLocation(ReportInfo rptInfo, Date date) {
		DateUtil dateUtil = new DateUtil();
		return rptInfo.getLocalDir()+ dateUtil.DateToString(date, "yyyyMM")+"/"+dateUtil.DateToString(date, "yyyy-MM-dd")+"/";
	}
	
	public boolean zipReportFile(String filepath, String filename){
		Util util = new Util();
		
		log.info("Initiating file zip process for "+filename);
		
		try {
			File fileToZip = new File(filepath+filename);
			FileInputStream fis = new FileInputStream(fileToZip);

	        FileOutputStream fos = new FileOutputStream(filepath+removeExtension(fileToZip.getName())+".zip");
	        ZipOutputStream zipOut = new ZipOutputStream(fos);
	        
	        ZipEntry zipEntry = new ZipEntry(fileToZip.getName());
	        zipOut.putNextEntry(zipEntry);

	        byte[] bytes = new byte[1024];
	        int length;
	        while((length = fis.read(bytes)) >= 0) {
	            zipOut.write(bytes, 0, length);
	        }
	        zipOut.close();
	        fis.close();
	        fos.close();
	        
	        log.info("File "+filename+" zipped successfully");
	        
	        try {
	        	fileToZip.delete();
	        } catch (Exception e) {
	        	log.error("Failed to delete file " + filename + " after zip"+ util.exceptionToString(e));
	        	try { fileToZip.deleteOnExit(); } catch (Exception ex) { }
	        }
	        
	        return true;
        } catch (Exception e) {
        	log.error("Failed to zip " + filename + util.exceptionToString(e));
        	return false;
        }
    }
	
	public String getFinalName(String fileNameTemplate, Map<String, String> arr) {
		String fileName = fileNameTemplate;
		
		for(Map.Entry<String, String> v : arr.entrySet()) {
			fileName = fileName.replaceAll(v.getKey(), v.getValue());
		}
		
		return fileName;
	}
	
	public JSONArray readFromJson(String fileName, String source) {
		Util util = new Util();
		
		JSONParser parser = new JSONParser();
		JSONArray arr = null;
		try {
			Reader reader = new FileReader(source+fileName);
			
			arr = (JSONArray) parser.parse(reader);
		} catch(Exception e) {
			log.error("Could not read JSON file" + util.exceptionToString(e));
		} 
		
		return arr;
	}
	
	public boolean callTransferFile(String source, String fileName, FtpServerInfo ftpServerInfo) {
		boolean flag = false;
		Util util =  new Util();
		log.info("Transferring file "+fileName+" to "+ftpServerInfo.getServerName());
		
		ftpconn:
		for(int i=0; i<Util.ftpretrycount; i++) {
			try {
				flag = transferFile(source, fileName, ftpServerInfo);
			} catch (Exception e) {
				log.error("Failed to transfer file on " + NumberUtil.convert(i) + " try" + util.exceptionToString(e));
			}
			
			if(flag) {
				break ftpconn;
			} else {
				log.error("Unable to transfer to " + ftpServerInfo.getServerName());
			}
			
			try {
				log.info("Waiting "+Util.ftpretrydelay + " seconds to retry...");
				Thread.sleep(Util.ftpretrydelay*1000);
			} catch (Exception e) {
				
			}
		}
		
		if(flag) {
			log.info("Transferred file "+fileName+" to "+ftpServerInfo.getServerName()+": "+ftpServerInfo.getDefaultDirectory());
		} else {
			log.error("Could not transfer " + fileName + " to " + ftpServerInfo.getServerName() + " trying for " + Util.ftpretrycount + " times");
		}
		
		return flag;
	}
	
	boolean transferFile(String source, String fileName, FtpServerInfo ftpServerInfo) 
	{	
		Util util = new Util();
		com.jcraft.jsch.Session session = null;
	    com.jcraft.jsch.Channel channel = null;
	    ChannelSftp channelSftp = null;
	    
	    try {
	    	JSch jsch = new JSch();	
	        String SFTPHOST = ftpServerInfo.getFtpHost(); 
	        String SFTPUSER = ftpServerInfo.getFtpUser();
	        String SFTPPASS = ftpServerInfo.getFtpPassword();
	        String SFTPWORKINGDIR = ftpServerInfo.getDefaultDirectory();
	        
	        session = jsch.getSession(SFTPUSER, SFTPHOST, Integer.parseInt(ftpServerInfo.getFtpPort()));
	        session.setPassword(new TextUtil().decode(SFTPPASS));
	        java.util.Properties config = new java.util.Properties();
	        config.put("StrictHostKeyChecking", "no");
	        session.setConfig(config);
	        session.connect();
	        channel = session.openChannel(ftpServerInfo.getFtpProtocol());
	        channel.connect();
	        channelSftp = (ChannelSftp) channel;
	        channelSftp.cd(SFTPWORKINGDIR);
		   
	        File f1 = new File(source+fileName);
	        try {
	        	FileInputStream fis = new FileInputStream(f1);
	        	channelSftp.put(fis, fileName, 0);
	        	fis.close();
	        } catch (Exception e) {
	        	log.error("Failed to close file" + util.exceptionToString(e));
	        }
	        
	        channelSftp.exit();
	        session.disconnect();
	        return true;
	    }
		catch (Exception e) {
			log.error("Failed to transfer file to "+ftpServerInfo.getServerName() + util.exceptionToString(e));
		    return false;
		}
	}
}
