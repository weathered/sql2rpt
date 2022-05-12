package com.weathered.bo;

public class ReportInfo {
	String rptId;
	String reportName;
	String sender;
	String mailTo;
	String mailCc;
	String mailBcc;
	String mailSubject;
	String mailBody;
	String status;
	String configDir;
	String localDir;
	String mailServerId;
	String ftpServerId;
	String asZip;
	String ftpTransfer;
	String sendEmail;
	
	public String getMailSubject() {
		return mailSubject;
	}
	public void setMailSubject(String mailSubject) {
		this.mailSubject = mailSubject;
	}
	public String getRptId() {
		return rptId;
	}
	public void setRptId(String rptId) {
		this.rptId = rptId;
	}
	public String getReportName() {
		return reportName;
	}
	public void setReportName(String reportName) {
		this.reportName = reportName;
	}
	public String getSender() {
		return sender;
	}
	public void setSender(String sender) {
		this.sender = sender;
	}
	public String getMailTo() {
		return mailTo;
	}
	public void setMailTo(String mailTo) {
		this.mailTo = mailTo;
	}
	public String getMailCc() {
		return mailCc;
	}
	public void setMailCc(String mailCc) {
		this.mailCc = mailCc;
	}
	public String getMailBcc() {
		return mailBcc;
	}
	public void setMailBcc(String mailBcc) {
		this.mailBcc = mailBcc;
	}
	public String getMailBody() {
		return mailBody;
	}
	public void setMailBody(String mailBody) {
		this.mailBody = mailBody;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getConfigDir() {
		return configDir;
	}
	public void setConfigDir(String configDir) {
		this.configDir = configDir;
	}
	public String getLocalDir() {
		return localDir;
	}
	public void setLocalDir(String localDir) {
		this.localDir = localDir;
	}
	public String getMailServerId() {
		return mailServerId;
	}
	public void setMailServerId(String mailServerId) {
		this.mailServerId = mailServerId;
	}
	public String getFtpServerId() {
		return ftpServerId;
	}
	public void setFtpServerId(String ftpServerId) {
		this.ftpServerId = ftpServerId;
	}
	public String getAsZip() {
		return asZip;
	}
	public void setAsZip(String asZip) {
		this.asZip = asZip;
	}
	public String getFtpTransfer() {
		return ftpTransfer;
	}
	public void setFtpTransfer(String ftpTransfer) {
		this.ftpTransfer = ftpTransfer;
	}
	public String getSendEmail() {
		return sendEmail;
	}
	public void setSendEmail(String sendEmail) {
		this.sendEmail = sendEmail;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((asZip == null) ? 0 : asZip.hashCode());
		result = prime * result + ((configDir == null) ? 0 : configDir.hashCode());
		result = prime * result + ((ftpServerId == null) ? 0 : ftpServerId.hashCode());
		result = prime * result + ((ftpTransfer == null) ? 0 : ftpTransfer.hashCode());
		result = prime * result + ((localDir == null) ? 0 : localDir.hashCode());
		result = prime * result + ((mailBcc == null) ? 0 : mailBcc.hashCode());
		result = prime * result + ((mailBody == null) ? 0 : mailBody.hashCode());
		result = prime * result + ((mailCc == null) ? 0 : mailCc.hashCode());
		result = prime * result + ((mailServerId == null) ? 0 : mailServerId.hashCode());
		result = prime * result + ((mailSubject == null) ? 0 : mailSubject.hashCode());
		result = prime * result + ((mailTo == null) ? 0 : mailTo.hashCode());
		result = prime * result + ((reportName == null) ? 0 : reportName.hashCode());
		result = prime * result + ((rptId == null) ? 0 : rptId.hashCode());
		result = prime * result + ((sendEmail == null) ? 0 : sendEmail.hashCode());
		result = prime * result + ((sender == null) ? 0 : sender.hashCode());
		result = prime * result + ((status == null) ? 0 : status.hashCode());
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
		ReportInfo other = (ReportInfo) obj;
		if (asZip == null) {
			if (other.asZip != null)
				return false;
		} else if (!asZip.equals(other.asZip))
			return false;
		if (configDir == null) {
			if (other.configDir != null)
				return false;
		} else if (!configDir.equals(other.configDir))
			return false;
		if (ftpServerId == null) {
			if (other.ftpServerId != null)
				return false;
		} else if (!ftpServerId.equals(other.ftpServerId))
			return false;
		if (ftpTransfer == null) {
			if (other.ftpTransfer != null)
				return false;
		} else if (!ftpTransfer.equals(other.ftpTransfer))
			return false;
		if (localDir == null) {
			if (other.localDir != null)
				return false;
		} else if (!localDir.equals(other.localDir))
			return false;
		if (mailBcc == null) {
			if (other.mailBcc != null)
				return false;
		} else if (!mailBcc.equals(other.mailBcc))
			return false;
		if (mailBody == null) {
			if (other.mailBody != null)
				return false;
		} else if (!mailBody.equals(other.mailBody))
			return false;
		if (mailCc == null) {
			if (other.mailCc != null)
				return false;
		} else if (!mailCc.equals(other.mailCc))
			return false;
		if (mailServerId == null) {
			if (other.mailServerId != null)
				return false;
		} else if (!mailServerId.equals(other.mailServerId))
			return false;
		if (mailSubject == null) {
			if (other.mailSubject != null)
				return false;
		} else if (!mailSubject.equals(other.mailSubject))
			return false;
		if (mailTo == null) {
			if (other.mailTo != null)
				return false;
		} else if (!mailTo.equals(other.mailTo))
			return false;
		if (reportName == null) {
			if (other.reportName != null)
				return false;
		} else if (!reportName.equals(other.reportName))
			return false;
		if (rptId == null) {
			if (other.rptId != null)
				return false;
		} else if (!rptId.equals(other.rptId))
			return false;
		if (sendEmail == null) {
			if (other.sendEmail != null)
				return false;
		} else if (!sendEmail.equals(other.sendEmail))
			return false;
		if (sender == null) {
			if (other.sender != null)
				return false;
		} else if (!sender.equals(other.sender))
			return false;
		if (status == null) {
			if (other.status != null)
				return false;
		} else if (!status.equals(other.status))
			return false;
		return true;
	}
	@Override
	public String toString() {
		return "ReportInfo [rptId=" + rptId + ", reportName=" + reportName + ", sender=" + sender + ", mailTo=" + mailTo
				+ ", mailCc=" + mailCc + ", mailBcc=" + mailBcc + ", mailSubject=" + mailSubject + ", mailBody="
				+ mailBody + ", status=" + status + ", configDir=" + configDir + ", localDir=" + localDir
				+ ", mailServerId=" + mailServerId + ", ftpServerId=" + ftpServerId + ", asZip=" + asZip
				+ ", ftpTransfer=" + ftpTransfer + ", sendEmail=" + sendEmail + "]";
	}
}
