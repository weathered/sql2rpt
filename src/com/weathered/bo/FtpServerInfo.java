package com.weathered.bo;

public class FtpServerInfo {
	String serverId;
	String serverName;
	String ftpHost;
	String ftpUser;
	String ftpPassword;
	String ftpPort;
	String ftpProtocol;
	String defaultDirectory;
	public String getServerId() {
		return serverId;
	}
	public void setServerId(String serverId) {
		this.serverId = serverId;
	}
	public String getServerName() {
		return serverName;
	}
	public void setServerName(String serverName) {
		this.serverName = serverName;
	}
	public String getFtpHost() {
		return ftpHost;
	}
	public void setFtpHost(String ftpHost) {
		this.ftpHost = ftpHost;
	}
	public String getFtpUser() {
		return ftpUser;
	}
	public void setFtpUser(String ftpUser) {
		this.ftpUser = ftpUser;
	}
	public String getFtpPassword() {
		return ftpPassword;
	}
	public void setFtpPassword(String ftpPassword) {
		this.ftpPassword = ftpPassword;
	}
	public String getFtpPort() {
		return ftpPort;
	}
	public void setFtpPort(String ftpPort) {
		this.ftpPort = ftpPort;
	}
	public String getFtpProtocol() {
		return ftpProtocol;
	}
	public void setFtpProtocol(String ftpProtocol) {
		this.ftpProtocol = ftpProtocol;
	}
	public String getDefaultDirectory() {
		return defaultDirectory;
	}
	public void setDefaultDirectory(String defaultDirectory) {
		this.defaultDirectory = defaultDirectory;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((defaultDirectory == null) ? 0 : defaultDirectory.hashCode());
		result = prime * result + ((ftpHost == null) ? 0 : ftpHost.hashCode());
		result = prime * result + ((ftpPassword == null) ? 0 : ftpPassword.hashCode());
		result = prime * result + ((ftpPort == null) ? 0 : ftpPort.hashCode());
		result = prime * result + ((ftpProtocol == null) ? 0 : ftpProtocol.hashCode());
		result = prime * result + ((ftpUser == null) ? 0 : ftpUser.hashCode());
		result = prime * result + ((serverId == null) ? 0 : serverId.hashCode());
		result = prime * result + ((serverName == null) ? 0 : serverName.hashCode());
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
		FtpServerInfo other = (FtpServerInfo) obj;
		if (defaultDirectory == null) {
			if (other.defaultDirectory != null)
				return false;
		} else if (!defaultDirectory.equals(other.defaultDirectory))
			return false;
		if (ftpHost == null) {
			if (other.ftpHost != null)
				return false;
		} else if (!ftpHost.equals(other.ftpHost))
			return false;
		if (ftpPassword == null) {
			if (other.ftpPassword != null)
				return false;
		} else if (!ftpPassword.equals(other.ftpPassword))
			return false;
		if (ftpPort == null) {
			if (other.ftpPort != null)
				return false;
		} else if (!ftpPort.equals(other.ftpPort))
			return false;
		if (ftpProtocol == null) {
			if (other.ftpProtocol != null)
				return false;
		} else if (!ftpProtocol.equals(other.ftpProtocol))
			return false;
		if (ftpUser == null) {
			if (other.ftpUser != null)
				return false;
		} else if (!ftpUser.equals(other.ftpUser))
			return false;
		if (serverId == null) {
			if (other.serverId != null)
				return false;
		} else if (!serverId.equals(other.serverId))
			return false;
		if (serverName == null) {
			if (other.serverName != null)
				return false;
		} else if (!serverName.equals(other.serverName))
			return false;
		return true;
	}
	@Override
	public String toString() {
		return "FtpServerInfo [serverId=" + serverId + ", serverName=" + serverName + ", ftpHost=" + ftpHost
				+ ", ftpUser=" + ftpUser + ", ftpPassword=" + ftpPassword + ", ftpPort=" + ftpPort + ", ftpProtocol="
				+ ftpProtocol + ", defaultDirectory=" + defaultDirectory + "]";
	}
}
