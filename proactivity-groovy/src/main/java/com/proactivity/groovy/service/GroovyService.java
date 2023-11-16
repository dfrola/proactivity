package com.proactivity.groovy.service;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.proactivity.groovy.GroovyManager;
import com.proactivity.groovy.dto.request.CatalogEl;
import com.proactivity.groovy.dto.request.EmployeeDTO;
import com.proactivity.groovy.dto.request.ISPData;

@Service
public class GroovyService {

	static final Logger LOG = LoggerFactory.getLogger(GroovyService.class);	
	
	private GroovyManager groovy;

	@Autowired
	public GroovyService(GroovyManager groovyMath) {
		super();
		this.groovy = groovyMath;
	}	
	
//	public EmployeeDTO adjustName(EmployeeDTO employeeDTO) {				
//		employeeDTO = (EmployeeDTO) groovy.invokeClass("/home/davide/repo-proactivity/proactivity/proactivity-groovy/src/main/groovy/EmployeeManager.groovy", "adjustName", employeeDTO );				
//		return employeeDTO;		
//	}
//	
//	public EmployeeDTO create() {					
//		return (EmployeeDTO) groovy.invokeClass("/home/davide/repo-proactivity/proactivity/proactivity-groovy/src/main/groovy/EmployeeManager.groovy", "create", null );			
//	}
	
	
	//Riferimenti assoluti a file system (Classi Groovy esterne all'applicazione) il file ResponseManager.groovy si trova in src/main/groovy
	public ISPData modify(ISPData ispData) {
		return (ISPData) groovy.invokeClass("/home/davide/GROOVY/CLASS/ResponseManager.groovy", "modify", ispData );
	}
	
	public CatalogEl modify(CatalogEl catalog) {
		return (CatalogEl) groovy.invokeClass("/home/davide/GROOVY/CLASS/ResponseManager.groovy", "modify", catalog );
	}	
	
	public ISPData modify(HttpServletRequest httpRequest, ISPData ispData) {
		return (ISPData) groovy.invokeClass("/home/davide/GROOVY/CLASS/ResponseManager.groovy", "modify", new Object[] {httpRequest, ispData} );
	}
	
	public CatalogEl modify(HttpServletRequest httpRequest, CatalogEl catalog) {
		return (CatalogEl) groovy.invokeClass("/home/davide/GROOVY/CLASS/ResponseManager.groovy", "modify", new Object[] {httpRequest, catalog} );
	}		
}
