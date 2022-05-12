package com.weathered.bo;

public class MailServerInfo {
	String mailServerId;
	String name;
	String mailHost;
	String mailUser;
	String mailPassword;
	String mailPort;
	String mailProtocol;
	String mailStarttlsEnable;
	public String getMailServerId() {
		return mailServerId;
	}
	public void setMailServerId(String mailServerId) {
		this.mailServerId = mailServerId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getMailHost() {
		return mailHost;
	}
	public void setMailHost(String mailHost) {
		this.mailHost = mailHost;
	}
	public String getMailUser() {
		return mailUser;
	}
	public void setMailUser(String mailUser) {
		this.mailUser = mailUser;
	}
	public String getMailPassword() {
		return mailPassword;
	}
	public void setMailPassword(String mailPassword) {
		this.mailPassword = mailPassword;
	}
	public String getMailPort() {
		return mailPort;
	}
	public void setMailPort(String mailPort) {
		this.mailPort = mailPort;
	}
	public String getMailProtocol() {
		return mailProtocol;
	}
	public void setMailProtocol(String mailProtocol) {
		this.mailProtocol = mailProtocol;
	}
	public String getMailStarttlsEnable() {
		return mailStarttlsEnable;
	}
	public void setMailStarttlsEnable(String mailStarttlsEnable) {
		this.mailStarttlsEnable = mailStarttlsEnable;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((mailHost == null) ? 0 : mailHost.hashCode());
		result = prime * result + ((mailPassword == null) ? 0 : mailPassword.hashCode());
		result = prime * result + ((mailPort == null) ? 0 : mailPort.hashCode());
		result = prime * result + ((mailProtocol == null) ? 0 : mailProtocol.hashCode());
		result = prime * result + ((mailServerId == null) ? 0 : mailServerId.hashCode());
		result = prime * result + ((mailStarttlsEnable == null) ? 0 : mailStarttlsEnable.hashCode());
		result = prime * result + ((mailUser == null) ? 0 : mailUser.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
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
		MailServerInfo other = (MailServerInfo) obj;
		if (mailHost == null) {
			if (other.mailHost != null)
				return false;
		} else if (!mailHost.equals(other.mailHost))
			return false;
		if (mailPassword == null) {
			if (other.mailPassword != null)
				return false;
		} else if (!mailPassword.equals(other.mailPassword))
			return false;
		if (mailPort == null) {
			if (other.mailPort != null)
				return false;
		} else if (!mailPort.equals(other.mailPort))
			return false;
		if (mailProtocol == null) {
			if (other.mailProtocol != null)
				return false;
		} else if (!mailProtocol.equals(other.mailProtocol))
			return false;
		if (mailServerId == null) {
			if (other.mailServerId != null)
				return false;
		} else if (!mailServerId.equals(other.mailServerId))
			return false;
		if (mailStarttlsEnable == null) {
			if (other.mailStarttlsEnable != null)
				return false;
		} else if (!mailStarttlsEnable.equals(other.mailStarttlsEnable))
			return false;
		if (mailUser == null) {
			if (other.mailUser != null)
				return false;
		} else if (!mailUser.equals(other.mailUser))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}
	@Override
	public String toString() {
		return "MailServerInfo [mailServerId=" + mailServerId + ", name=" + name + ", mailHost=" + mailHost
				+ ", mailUser=" + mailUser + ", mailPassword=" + mailPassword + ", mailPort=" + mailPort
				+ ", mailProtocol=" + mailProtocol + ", mailStarttlsEnable=" + mailStarttlsEnable + "]";
	}
	
}
