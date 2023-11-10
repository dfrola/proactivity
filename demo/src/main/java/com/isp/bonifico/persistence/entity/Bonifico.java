package com.isp.bonifico.persistence.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "BONIFICO")
public class Bonifico {
	
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "BONIFICO_ID")
    private Long bonificoId;
	@Column(name = "CATEGORY")
	private String category;
	public Long getBonificoId() {
		return bonificoId;
	}
	public void setBonificoId(Long bonificoId) {
		this.bonificoId = bonificoId;
	}
	public String getCategory() {
		return category;
	}
	public void setCategory(String category) {
		this.category = category;
	}
	
	
}
