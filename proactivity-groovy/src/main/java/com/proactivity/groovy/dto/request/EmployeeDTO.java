package com.proactivity.groovy.dto.request;

import java.io.Serializable;
import java.util.UUID;

import org.springframework.data.annotation.Id;

public class EmployeeDTO implements Serializable{

    /**
	 * 
	 */
	private static final long serialVersionUID = 3352898907456238376L;
	
	UUID id = UUID.randomUUID();
	private String name;
    private String lastName;
    private String repository;
    
    
	public UUID getId() {
		return id;
	}
	public void setId(UUID id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	public String getRepository() {
		return repository;
	}
	public void setRepository(String repository) {
		this.repository = repository;
	}
	@Override
	public String toString() {
		return "EmployeeDTO [id=" + id + ", name=" + name + ", lastName=" + lastName + ", repository=" + repository
				+ "]";
	}	   
}
