package com.weathered.sql2rpt.log;

import java.io.Serializable;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.spi.ExtendedLogger;

import com.weathered.sql2rpt.util.DateUtil;

public final class SchLogger implements Serializable{
	private static final long serialVersionUID = 6020009347428591666L;
	private static final String FQCN = SchLogger.class.getName();
	private final ExtendedLogger logger;
	
	public SchLogger(final String name) {
		this(LogManager.getLogger(name));
	}

	public SchLogger(final Class<?> _class) {
		this(_class.getName());
	}
	

	public SchLogger(Logger logger2) {
		this.logger = (ExtendedLogger) logger2;
	}
	
	public void info(String text) {
		System.out.println(new DateUtil().getCurrentTime() + " " + text);
		this.logger.logIfEnabled(FQCN, Level.INFO, null, String.format("%s", text.trim() ));
	}
	
	public void debug(String text) {
		System.out.println(new DateUtil().getCurrentTime() + " " + text);
		this.logger.logIfEnabled(FQCN, Level.DEBUG, null, String.format("%s", text.trim()));
	}
	
	public void error(String text) {
		System.out.println(new DateUtil().getCurrentTime() + " [[ ERROR ]] " + text.toUpperCase().trim());
		this.logger.logIfEnabled(FQCN, Level.ERROR, null, String.format("[[ ERROR ]] %s", text.toUpperCase().trim()));
	}
}
