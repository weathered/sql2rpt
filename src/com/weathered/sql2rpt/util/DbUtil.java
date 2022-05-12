package com.weathered.sql2rpt.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import com.weathered.bo.DbInfo;
import com.weathered.sql2rpt.log.SchLogger;
import com.weathered.sql2rpt.rpt.ReportSchedulerBase;

public class DbUtil {
	private static final SchLogger log = new SchLogger(DbUtil.class);
	
	public String getSingleValue(String sql, String rptSrc) {
		String value = null;
		ResultSet rs = getData(sql, rptSrc);
		try {
			if(rs.next()) {
				value = rs.getString(1);
			}
		} catch (Exception e) {
			log.error("Could not fetch value"+ new Util().exceptionToString(e));
		}
		
		return value;
	}
	
	public ResultSet getData(String sql, String rptSrc) {
		Util util = new Util();
		
		Statement stm = null;
		ResultSet rs = null;
		
		Connection conn = getDbConnection(rptSrc);
		
		try {
			stm = conn.createStatement();
			rs = stm.executeQuery(sql);
		} catch (SQLException e) {
			log.error("Could not fetch data"+util.exceptionToString(e));
		}
		
		return rs;
	}
	
	public Connection getSchDbConnection() {
		Connection connection = null;
		try{
			connection = getConnection(Util.dbconnstring, Util.dbuser, Util.dbpass);
		} catch (Exception e) {
			log.error("!!!!Could not connect to scheduler database!!!!" + new Util().exceptionToString(e));
		}
		
		return connection;
	}
	
	public Connection getConnection(String connectionString, String dbUser, String dbPassword) {
		Util util = new Util();
		
		try {			 
			Class.forName("oracle.jdbc.driver.OracleDriver"); 
		} 
		catch (ClassNotFoundException e) { 
			log.error("Database Connectivity API/Driver for not found, please ensure driver file is in place"+util.exceptionToString(e));
		} 
		
		Connection connection = null;
		
		Properties props = new Properties();
        props.setProperty("user", dbUser);
        props.setProperty("password", new TextUtil().decode(dbPassword));
		
		try {			
			connection = DriverManager.getConnection(connectionString, props);
		}  catch (Throwable t) {
			log.error("Could not connect to database" + util.exceptionToString(t));
		}
		
		return connection;
	}
	
	public Connection getDbConnection(String rptSrc) {
		Connection connection = null;
		List<DbInfo> dbs = ReportSchedulerBase.getRptDbList();
		DbInfo lastSuccessfullyConnectionRptDb = ReportSchedulerBase.getActiveRptDb();
		
		if(lastSuccessfullyConnectionRptDb != null) {
			if(dbs!=null) {
				dbs.clear();
			} else {
				dbs = new ArrayList<DbInfo>();
			}
			dbs.add(lastSuccessfullyConnectionRptDb);
		} else {
			if(dbs == null) {
				dbs = new ReportUtil().getReportDbInfo(rptSrc);
				ReportSchedulerBase.setRptDbList(dbs);
			}
		}
		
		dbconn:
		for(int j=0; j<Util.dbretrycount; j++) {
			for(int i=0; i<dbs.size(); i++) {
				log.info("Connecting to "+dbs.get(i).getName());
				
				try {
					connection = getConnection(dbs.get(i).getConnString(), dbs.get(i).getUsername(), dbs.get(i).getPassword());
				} catch (Exception e) {
					
				}
				
				if(connection!=null) {
					log.info("Connected to "+dbs.get(i).getName());
					ReportSchedulerBase.setActiveRptDb(dbs.get(i));
					break dbconn;
				} else {
					log.error("Unable to connected to "+dbs.get(i).getName());
					ReportSchedulerBase.setActiveRptDb(null);
				}
				
				try {
					log.info("Waiting "+Util.dbretrydelay + " seconds to retry...");
					Thread.sleep(Util.dbretrydelay*1000);
				} catch (Exception e) {
					
				}
			}
		}
		if(connection==null) {
			log.error("Unable to connect to " + rptSrc);
		}
		
		return connection;
	}
	
    public void closeDbConnection(Connection connection) {
        Util util = new Util();
    	try {
            if(connection!=null){
            	connection.close();
            	log.info("Disconnected from database.");
                
            }
            else{
            	log.info("Not connected to any database.");
            }
        } catch (Exception e) {
        	log.error("Error while trying to disconnect from database" + util.exceptionToString(e));
        }
    }
}
