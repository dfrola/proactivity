package com.proactivity.groovy.dto.request;

import java.util.Map;

public class DecisionData {
	
	private Object data;
	private Map<String, Object> dataMap;

	public DecisionData() {
		super();
	}
	
	public DecisionData(Object data) {
		super();
		this.data = data;
	}	

	public DecisionData(Object data, Map<String, Object> dataMap) {
		super();
		this.data = data;
		this.dataMap = dataMap;
	}

	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
	}

	public Map<String, Object> getDataMap() {
		return dataMap;
	}

	public void setDataMap(Map<String, Object> dataMap) {
		this.dataMap = dataMap;
	}

	@Override
	public String toString() {
		return "DecisionData [data=" + data + ", dataMap=" + dataMap + "]";
	}	
}
