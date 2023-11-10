package com.proactivity.decision.manager.dto.request;

public class ISPRequest {

	private String name;
	private Long num;
	private ISPContext context;
	
	public ISPRequest() {
		super();		
	}
	
	public ISPRequest(String name, Long num, ISPContext context) {
		super();
		this.name = name;
		this.num = num;
		this.context = context;
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Long getNum() {
		return num;
	}
	public void setNum(Long num) {
		this.num = num;
	}
	public ISPContext getContext() {
		return context;
	}
	public void setContext(ISPContext context) {
		this.context = context;
	}

	@Override
	public String toString() {
		return "ISPRequest [name=" + name + ", num=" + num + ", context=" + context + "]";
	}
	
}
