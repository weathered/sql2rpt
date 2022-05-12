package com.weathered.sql2rpt.rpt;

import java.util.List;

import com.weathered.bo.DbInfo;
import com.weathered.sql2rpt.util.Util;

public class ReportSchedulerBase {
	static DbInfo activeRptDb;
	static List<DbInfo> rptDbList;

	public static DbInfo getActiveRptDb() {
		return activeRptDb;
	}

	public static void setActiveRptDb(DbInfo activeRptDb) {
		ReportSchedulerBase.activeRptDb = activeRptDb;
	}

	public static List<DbInfo> getRptDbList() {
		return rptDbList;
	}

	public static void setRptDbList(List<DbInfo> rptDbList) {
		ReportSchedulerBase.rptDbList = rptDbList;
	}

	static {
		new Util().loadSchedulerProperties();
	}
}
