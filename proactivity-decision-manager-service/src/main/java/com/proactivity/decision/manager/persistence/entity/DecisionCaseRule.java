package com.proactivity.decision.manager.persistence.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "DECISION_CASE_RULE")
public class DecisionCaseRule {	
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "DECISION_CASE_RULE_ID")
    private Long decisionCaseRuleId;
	@Column(name = "RULE_FILE")
    private String ruleFile;
	@Column(name = "SEARCH_KEY")
	private String searchKey;
	@Column(name = "ENABLED")
	private Boolean enabled;	
	@Column(name = "ORDER")
	private Integer order;
	@Column(name = "EXIT_ON_FAILURE")
	private Boolean exitOnFailure;
	@Column(name = "EXIT_ON_SUCCESS")
	private Boolean exitOnSuccess;   
	
	@ManyToOne
    @JoinColumn(name="DECISION_CASE_ID", nullable=false)
    private DecisionCase decisionCase;
	
	public Long getDecisionCaseRuleId() {
		return decisionCaseRuleId;
	}
	public void setDecisionCaseRuleId(Long decisionCaseRuleId) {
		this.decisionCaseRuleId = decisionCaseRuleId;
	}
	public String getRuleFile() {
		return ruleFile;
	}
	public void setRuleFile(String ruleFile) {
		this.ruleFile = ruleFile;
	}	
	public String getSearchKey() {
		return searchKey;
	}
	public void setSearchKey(String searchKey) {
		this.searchKey = searchKey;
	}
	public Boolean getEnabled() {
		return enabled;
	}
	public void setEnabled(Boolean enabled) {
		this.enabled = enabled;
	}
	public DecisionCase getDecisionCase() {
		return decisionCase;
	}
	public void setDecisionCase(DecisionCase decisionCase) {
		this.decisionCase = decisionCase;
	}
	public Integer getOrder() {
		return order;
	}
	public void setOrder(Integer order) {
		this.order = order;
	}
	public Boolean getExitOnFailure() {
		return exitOnFailure;
	}
	public void setExitOnFailure(Boolean exitOnFailure) {
		this.exitOnFailure = exitOnFailure;
	}
	public Boolean getExitOnSuccess() {
		return exitOnSuccess;
	}
	public void setExitOnSuccess(Boolean exitOnSuccess) {
		this.exitOnSuccess = exitOnSuccess;
	}	
}
