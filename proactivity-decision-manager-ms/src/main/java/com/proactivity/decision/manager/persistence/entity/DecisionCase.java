package com.proactivity.decision.manager.persistence.entity;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "DECISION_CASE")
public class DecisionCase {
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "DECISION_CASE_ID")
    private Long decisionCaseId;
	@Column(name = "CATEGORY")
	private String category;
	@Column(name = "CONTEXT")
	private String context;	
	@Column(name = "JAVA_CLASS_NAME")
	private String javaClassName;
	@Column(name = "JSON_PATH")
	private String jsonPath;
	@Column(name = "JSON_TYPE")
	private String jsonType;
	@Column(name = "ORDER")
	private Integer order;
	@Column(name = "ENABLED")
	private Boolean enabled;	
	
	@OneToMany(mappedBy="decisionCase")
    private List<DecisionCaseRule> rules;

	public Long getDecisionCaseId() {
		return decisionCaseId;
	}

	public void setDecisionCaseId(Long decisionCaseId) {
		this.decisionCaseId = decisionCaseId;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getContext() {
		return context;
	}

	public void setContext(String context) {
		this.context = context;
	}

	public String getJavaClassName() {
		return javaClassName;
	}

	public void setJavaClassName(String javaClassName) {
		this.javaClassName = javaClassName;
	}

	public String getJsonPath() {
		return jsonPath;
	}

	public void setJsonPath(String jsonPath) {
		this.jsonPath = jsonPath;
	}

	public String getJsonType() {
		return jsonType;
	}

	public void setJsonType(String jsonType) {
		this.jsonType = jsonType;
	}		

	public Integer getOrder() {
		return order;
	}

	public void setOrder(Integer order) {
		this.order = order;
	}

	public Boolean getEnabled() {
		return enabled;
	}

	public void setEnabled(Boolean enabled) {
		this.enabled = enabled;
	}

	public List<DecisionCaseRule> getRules() {
		return rules;
	}

	public void setRules(List<DecisionCaseRule> rules) {
		this.rules = rules;
	}	
}
