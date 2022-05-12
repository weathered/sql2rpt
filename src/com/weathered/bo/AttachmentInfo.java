package com.weathered.bo;

public class AttachmentInfo {
	String rptId;
	String fileName;
	String rptQuery;
	String rptCondition;
	String rptCondQueryOrVal;
	String execCondition;
	String execCondQueryOrVal;
	String rptSrc;
	String fileFormat;
	String isPrevDayRptReq;
	
	public String getRptId() {
		return rptId;
	}
	public void setRptId(String rptId) {
		this.rptId = rptId;
	}
	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	public String getRptQuery() {
		return rptQuery;
	}
	public void setRptQuery(String rptQuery) {
		this.rptQuery = rptQuery;
	}
	public String getRptCondition() {
		return rptCondition;
	}
	public void setRptCondition(String rptCondition) {
		this.rptCondition = rptCondition;
	}
	public String getRptCondQueryOrVal() {
		return rptCondQueryOrVal;
	}
	public void setRptCondQueryOrVal(String rptCondQueryOrVal) {
		this.rptCondQueryOrVal = rptCondQueryOrVal;
	}
	public String getExecCondition() {
		return execCondition;
	}
	public void setExecCondition(String execCondition) {
		this.execCondition = execCondition;
	}
	public String getExecCondQueryOrVal() {
		return execCondQueryOrVal;
	}
	public void setExecCondQueryOrVal(String execCondQueryOrVal) {
		this.execCondQueryOrVal = execCondQueryOrVal;
	}
	public String getRptSrc() {
		return rptSrc;
	}
	public void setRptSrc(String rptSrc) {
		this.rptSrc = rptSrc;
	}
	public String getFileFormat() {
		return fileFormat;
	}
	public void setFileFormat(String fileFormat) {
		this.fileFormat = fileFormat;
	}
	public String getIsPrevDayRptReq() {
		return isPrevDayRptReq;
	}
	public void setIsPrevDayRptReq(String isPrevDayRptReq) {
		this.isPrevDayRptReq = isPrevDayRptReq;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((execCondQueryOrVal == null) ? 0 : execCondQueryOrVal.hashCode());
		result = prime * result + ((execCondition == null) ? 0 : execCondition.hashCode());
		result = prime * result + ((fileFormat == null) ? 0 : fileFormat.hashCode());
		result = prime * result + ((fileName == null) ? 0 : fileName.hashCode());
		result = prime * result + ((isPrevDayRptReq == null) ? 0 : isPrevDayRptReq.hashCode());
		result = prime * result + ((rptCondQueryOrVal == null) ? 0 : rptCondQueryOrVal.hashCode());
		result = prime * result + ((rptCondition == null) ? 0 : rptCondition.hashCode());
		result = prime * result + ((rptId == null) ? 0 : rptId.hashCode());
		result = prime * result + ((rptQuery == null) ? 0 : rptQuery.hashCode());
		result = prime * result + ((rptSrc == null) ? 0 : rptSrc.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		AttachmentInfo other = (AttachmentInfo) obj;
		if (execCondQueryOrVal == null) {
			if (other.execCondQueryOrVal != null)
				return false;
		} else if (!execCondQueryOrVal.equals(other.execCondQueryOrVal))
			return false;
		if (execCondition == null) {
			if (other.execCondition != null)
				return false;
		} else if (!execCondition.equals(other.execCondition))
			return false;
		if (fileFormat == null) {
			if (other.fileFormat != null)
				return false;
		} else if (!fileFormat.equals(other.fileFormat))
			return false;
		if (fileName == null) {
			if (other.fileName != null)
				return false;
		} else if (!fileName.equals(other.fileName))
			return false;
		if (isPrevDayRptReq == null) {
			if (other.isPrevDayRptReq != null)
				return false;
		} else if (!isPrevDayRptReq.equals(other.isPrevDayRptReq))
			return false;
		if (rptCondQueryOrVal == null) {
			if (other.rptCondQueryOrVal != null)
				return false;
		} else if (!rptCondQueryOrVal.equals(other.rptCondQueryOrVal))
			return false;
		if (rptCondition == null) {
			if (other.rptCondition != null)
				return false;
		} else if (!rptCondition.equals(other.rptCondition))
			return false;
		if (rptId == null) {
			if (other.rptId != null)
				return false;
		} else if (!rptId.equals(other.rptId))
			return false;
		if (rptQuery == null) {
			if (other.rptQuery != null)
				return false;
		} else if (!rptQuery.equals(other.rptQuery))
			return false;
		if (rptSrc == null) {
			if (other.rptSrc != null)
				return false;
		} else if (!rptSrc.equals(other.rptSrc))
			return false;
		return true;
	}
	@Override
	public String toString() {
		return "AttachmentInfo [rptId=" + rptId + ", fileName=" + fileName + ", rptQuery=" + rptQuery
				+ ", rptCondition=" + rptCondition + ", rptCondQueryOrVal=" + rptCondQueryOrVal + ", execCondition="
				+ execCondition + ", execCondQueryOrVal=" + execCondQueryOrVal + ", rptSrc=" + rptSrc + ", fileFormat="
				+ fileFormat + ", isPrevDayRptReq=" + isPrevDayRptReq + "]";
	}
}
