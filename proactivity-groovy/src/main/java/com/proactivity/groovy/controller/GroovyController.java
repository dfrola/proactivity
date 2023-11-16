package com.proactivity.groovy.controller;



import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.proactivity.groovy.dto.request.CatalogEl;
import com.proactivity.groovy.dto.request.DecisionManagerRequest;
import com.proactivity.groovy.dto.request.ISPData;
import com.proactivity.groovy.service.GroovyService;

@RestController
@RequestMapping(value = "/v1/resources")
public class GroovyController {
	
	GroovyService groovyService;
	
	@Autowired
    public GroovyController(GroovyService groovyService) {
		super();
		this.groovyService = groovyService;
	}       
//    
//    @PutMapping(path = "/ispdata")
//    public ResponseEntity<?> modifyISPData(@RequestBody ISPData data) { 
//    
//    	return ResponseEntity.status(HttpStatus.OK)
//    	        .body(groovyService.modify(data));  
//    }
//    
//    @PutMapping(path = "/catalogs")
//    public ResponseEntity<?> modifyCatalogEl(@RequestBody CatalogEl data) { 
//    
//    	return ResponseEntity.status(HttpStatus.OK)
//    	        .body(groovyService.modify(data));  
//    }
    
    @PutMapping(path = "/catalogs")
    public ResponseEntity<?> modify(HttpServletRequest httpRequest, @RequestHeader("X-SO") String xso, @RequestHeader("X-APP-VERSION") String xAppVersion, @RequestBody CatalogEl data) { 
    	
    
    	return ResponseEntity.status(HttpStatus.OK)
    	        .body(groovyService.modify(httpRequest, data));  
    }
    
    @PutMapping(path = "/ispdata")
    public ResponseEntity<?> modify(HttpServletRequest httpRequest, @RequestHeader("X-SO") String xso, @RequestHeader("X-APP-VERSION") String xAppVersion, @RequestBody ISPData data) { 
    	
    
    	return ResponseEntity.status(HttpStatus.OK)
    	        .body(groovyService.modify(httpRequest, data));  
    }
    
    

    @PostMapping
    public ResponseEntity<?> route(@RequestBody DecisionManagerRequest request) {  
    	
    	return ResponseEntity.status(HttpStatus.OK)
    	        .body("Hello world");  
    }   
    
//    @GetMapping
//    public ResponseEntity<?> create() {  
//    	
//    	return ResponseEntity.status(HttpStatus.OK)
//    	        .body(groovyService.create());  
//    }   

}
