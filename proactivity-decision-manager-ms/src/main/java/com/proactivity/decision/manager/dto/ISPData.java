package com.proactivity.decision.manager.dto;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public class ISPData {
	
	
	
	private String name;
	private BigDecimal price;
	private Map<String, String> headers;
	private EmployeeDTO employee;
	private List<String> names;
	
	private List<CatalogEl> catalog;
	
	public ISPData() {super();}

	public ISPData(List<CatalogEl> catalog) {
		super();
		this.catalog = catalog;
	}

	public List<CatalogEl> getCatalog() {
		return catalog;
	}

	public void setCatalog(List<CatalogEl> catalog) {
		this.catalog = catalog;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public BigDecimal getPrice() {
		return price;
	}

	public void setPrice(BigDecimal price) {
		this.price = price;
	}

	public Map<String, String> getHeaders() {
		return headers;
	}

	public void setHeaders(Map<String, String> headers) {
		this.headers = headers;
	}

	public EmployeeDTO getEmployee() {
		return employee;
	}

	public void setEmployee(EmployeeDTO employee) {
		this.employee = employee;
	}

	public List<String> getNames() {
		return names;
	}

	public void setNames(List<String> names) {
		this.names = names;
	}

	@Override
	public String toString() {
		return "ISPData [name=" + name + ", price=" + price + ", headers=" + headers + ", employee=" + employee
				+ ", names=" + names + ", catalog=" + catalog + "]";
	}	
}
