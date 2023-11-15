package com.proactivity.decision.manager.controller;


import java.util.Base64;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.proactivity.decision.manager.dto.request.DecisionManagerRequest;
import com.proactivity.decision.manager.service.DecisionManagerService;

@RestController
@RequestMapping(value = "/v1")
public class DecisionManagerController {
	
	DecisionManagerService decisionManagerService;
	
	@Autowired
    public DecisionManagerController(DecisionManagerService decisionManagerService) {
		super();
		this.decisionManagerService = decisionManagerService;
	}       
    
    @PostMapping(path = "/entities")
    public ResponseEntity<?> modifyResource(@RequestBody DecisionManagerRequest request) {      	
    	
    	
    	if(request.getJson() != null && ( ! request.getJson().isEmpty())) {
    		return ResponseEntity.status(HttpStatus.OK)
        	        .body(decisionManagerService.modify(request.getConditionMap(), new String(Base64.getDecoder().decode(request.getJson())), request.getContext()));  
    	}
        	
    	return ResponseEntity.status(HttpStatus.OK)
    	        .body(decisionManagerService.modify(request.getConditionMap(), request.getBody(), request.getContext()));  
    }
    
    @PostMapping(path = "/routes")
    public ResponseEntity<?> route(@RequestBody DecisionManagerRequest request) {  
    	
    	return ResponseEntity.status(HttpStatus.OK)
    	        .body(decisionManagerService.route(request.getConditionMap(), request.getContext()));  
    }    

}
