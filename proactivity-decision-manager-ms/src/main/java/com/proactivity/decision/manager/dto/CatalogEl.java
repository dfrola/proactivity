package com.proactivity.decision.manager.dto;

public class CatalogEl {	
	
	private String label;
	private String url;
	private Long bt;
	
	public CatalogEl() {
		super();		
	}	
	
	public CatalogEl(String label, String url, Long bt) {
		super();
		this.label = label;
		this.url = url;
		this.bt = bt;
	}

	public String getLabel() {
		return label;
	}
	public void setLabel(String label) {
		this.label = label;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public Long getBt() {
		return bt;
	}
	public void setBt(Long bt) {
		this.bt = bt;
	}

	@Override
	public String toString() {
		return "CatalogEl [label=" + label + ", url=" + url + ", bt=" + bt + "]";
	}
}
