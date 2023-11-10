package com.isp.bonifico.dto;

public class DecisionResult {
	
	Object value;
	Object result;
	
	public DecisionResult() {
		super();		
	}
	
	public DecisionResult(Object value, Object result) {
		super();
		this.value = value;
		this.result = result;
	}
	public Object getValue() {
		return value;
	}
	public void setValue(Object value) {
		this.value = value;
	}
	public Object getResult() {
		return result;
	}
	public void setResult(Object result) {
		this.result = result;
	}
	
	

}
