package com.proactivity.decision.manager.dto.request;

import java.util.Map;

public class DecisionManagerRequest {

	private String context;
	private Map<String, Object> conditionMap;	
	private Object body;
	
	public DecisionManagerRequest() {
		super();		
	}
		
	public DecisionManagerRequest(String context, Map<String, Object> conditionMap, Object body) {
		super();
		this.context = context;
		this.conditionMap = conditionMap;
		this.body = body;
	}

	public Map<String, Object> getConditionMap() {
		return conditionMap;
	}
	public void setConditionMap(Map<String, Object> conditionMap) {
		this.conditionMap = conditionMap;
	}
	public Object getBody() {
		return body;
	}
	public void setBody(Object body) {
		this.body = body;
	}

	public String getContext() {
		return context;
	}

	public void setContext(String context) {
		this.context = context;
	}

	@Override
	public String toString() {
		return "DecisionManagerRequest [context=" + context + ", conditionMap=" + conditionMap + ", body=" + body + "]";
	}		
}
