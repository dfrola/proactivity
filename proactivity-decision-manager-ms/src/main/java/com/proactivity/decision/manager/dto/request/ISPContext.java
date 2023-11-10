package com.proactivity.decision.manager.dto.request;

import java.math.BigDecimal;

public class ISPContext {
	
	private BigDecimal appVersion;
	private String os;
	
	public ISPContext() {
		super();		
	}
	
	public ISPContext(BigDecimal appVersion, String os) {
		super();
		this.appVersion = appVersion;
		this.os = os;
	}

	public BigDecimal getAppVersion() {
		return appVersion;
	}

	public void setAppVersion(BigDecimal appVersion) {
		this.appVersion = appVersion;
	}

	public String getOs() {
		return os;
	}

	public void setOs(String os) {
		this.os = os;
	}

	@Override
	public String toString() {
		return "ISPContext [appVersion=" + appVersion + ", os=" + os + "]";
	}

}
