package com.weathered.sql2rpt.rpt;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.Map;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;

import com.opencsv.CSVWriter;
import com.weathered.sql2rpt.log.SchLogger;
import com.weathered.sql2rpt.sql.NamedPreparedStatement;
import com.weathered.sql2rpt.util.DbUtil;
import com.weathered.sql2rpt.util.Util;

public class sql2rpt {

	private static final SchLogger log = new SchLogger(sql2rpt.class);
	
	/*csv start*/
	boolean generateCSVReport(String reportName, String sql, String fileName, String fileLocationSource, String rptSrc, Map<String, String> params) {
		ResultSet rs = null;
		DbUtil dbUtil = new DbUtil();
		Connection connection = dbUtil.getDbConnection(rptSrc);
		NamedPreparedStatement statement = null;
		
		log.info("Generating " + fileName);
		try {
			String csvFilePath = fileLocationSource+fileName;
			CSVWriter writer = new CSVWriter(new FileWriter(fileLocationSource+fileName));			
			
			log.info("Executing statement");
			
			statement = new NamedPreparedStatement(connection, sql);
			for(Map.Entry<String, String> param : params.entrySet()) {
				statement.setString(param.getKey(), param.getValue());
			}
			
			rs = statement.executeQuery();
			
			log.info("Creating csv file: " + csvFilePath);
			
			writer.writeAll(rs, true);
			writer.close();
			
			log.info("Complete: " + fileName);

			return true;
		} catch (Exception e){
			log.error("Error occured while generating csv" + new Util().exceptionToString(e));
			return false; 
		} finally {
			try {
				rs.close();
				statement.close();
			} catch (SQLException e) {
				
			}
			dbUtil.closeDbConnection(connection); 
		}
	}
	/*csv end*/
	
	/*xlsx start*/
	// TODO add support for xls
	boolean generateExcelReport(String reportName, String sql, String fileName, String fileLocationSource, String rptSrc, Map<String, String> params) {		
		SXSSFWorkbook workbook = new SXSSFWorkbook(500);
		FileOutputStream outputStream = null;
		ResultSet rs = null;
		
		DbUtil dbUtil = new DbUtil();
		Connection connection = dbUtil.getDbConnection(rptSrc);
		NamedPreparedStatement statement = null;
		
		log.info("Generating " + fileName);
		try {
			String excelFilePath = fileLocationSource+fileName;

			log.info("Executing statement");
			
			statement = new NamedPreparedStatement(connection, sql);
			for(Map.Entry<String, String> param : params.entrySet()) {
				statement.setString(param.getKey(), param.getValue());
			}
			
			rs = statement.executeQuery();
			
			log.info("Creating xlsx file: " + excelFilePath);
			
			sql2xlsx(rs, workbook);
			
			outputStream = new FileOutputStream(excelFilePath);
	        workbook.write(outputStream);
	        
			workbook.close();
			
			log.info("Complete: " + fileName);

			return true;
		} catch (Exception e){
			log.error("Error occured while generating excel file" + new Util().exceptionToString(e));
			e.printStackTrace();
			return false; 
		} finally {
			try {
				outputStream.flush();
				outputStream.close();
				rs.close();
				statement.close();
	            workbook.close();
			} catch (Exception e) {
				
			}
			dbUtil.closeDbConnection(connection); 
		}
	}
	
	public Workbook sql2xlsx(ResultSet result, Workbook workbook) throws IOException, SQLException{
		return sql2xlsx(result, workbook, "1");
	}
	
	public Workbook sql2xlsx(ResultSet result, Workbook workbook, String sheetName)throws FileNotFoundException, IOException, SQLException{
		Sheet sheet = workbook.createSheet(sheetName);
		
		int rowNum = 0;
		Row headerRow = sheet.createRow(rowNum);
		
		ResultSetMetaData metaData = result.getMetaData();
		int colCount = metaData.getColumnCount();
		
		for(int i = 0; i < colCount; i++){
			Cell cell = headerRow.createCell(i);
			cell.setCellValue(metaData.getColumnLabel(i+1));
		}
		
		Cell cell;
		
		while(result.next()){
			rowNum++;
			Row row = sheet.createRow(rowNum);
			for(int i = 0; i< colCount; i++){
				cell = row.createCell(i);
				cell.setCellValue(result.getString(i+1) == null ? "" : result.getString(i+1));
			}
		}
		
		return workbook;
	}
	/*xlsx end*/
	
	public boolean generateReport(String reportName, String sql, String fileName, String fileFormat, String fileLocationSource, String rptSrc, Map<String, String> params) {
		for(Map.Entry<String, String> param : params.entrySet()) {
			log.info("Parameter: "+param.getKey()+", Value: "+ param.getValue());
		}
		log.info("Report Query: \n\n"+sql+"\n");
		
		if(fileFormat.equalsIgnoreCase("csv")) {
			return generateCSVReport(reportName, sql, fileName, fileLocationSource, rptSrc, params);
		} else if (fileFormat.equalsIgnoreCase("xlsx")) {
			// TODO add support for xls
			return generateExcelReport(reportName, sql, fileName, fileLocationSource, rptSrc, params);
		} else {
			log.error(fileFormat + " is an unsupported or untested file format :)");
			return false;
		}
	}
}
