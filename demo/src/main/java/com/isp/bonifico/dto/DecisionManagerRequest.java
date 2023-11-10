package com.isp.bonifico.dto;

import java.util.Map;

public class DecisionManagerRequest {
	
	private String callerId;
	private String callerGroupId;
	private String redirectURL;
	
	private Map<String, String> headers;
	
	private Object body;
	private Object response;	
		
	public DecisionManagerRequest(String callerId, String callerGroupId, String redirectURL,
			Map<String, String> headers, Object body, Object response) {
		super();
		this.callerId = callerId;
		this.callerGroupId = callerGroupId;
		this.redirectURL = redirectURL;
		this.headers = headers;
		this.body = body;
		this.response = response;
	}
	public String getCallerId() {
		return callerId;
	}
	public void setCallerId(String callerId) {
		this.callerId = callerId;
	}
	public Map<String, String> getHeaders() {
		return headers;
	}
	public void setHeaders(Map<String, String> headers) {
		this.headers = headers;
	}
	public Object getBody() {
		return body;
	}
	public void setBody(Object body) {
		this.body = body;
	}
	public Object getResponse() {
		return response;
	}
	public void setResponse(Object response) {
		this.response = response;
	}
	public String getCallerGroupId() {
		return callerGroupId;
	}
	public void setCallerGroupId(String callerGroupId) {
		this.callerGroupId = callerGroupId;
	}
	public String getRedirectURL() {
		return redirectURL;
	}
	public void setRedirectURL(String redirectURL) {
		this.redirectURL = redirectURL;
	}	

}
