package com.isp.bonifico.controller;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.isp.bonifico.dto.ISPData;
import com.isp.bonifico.service.BonificoService;

@RestController
@RequestMapping(value = "/v1/bonifici")
public class BonificoController {
	
	@Autowired
	private BonificoService bonificoService;

	@GetMapping()
    public ResponseEntity<ISPData> findISPData(@RequestHeader("X-SO") String xso, @RequestHeader("X-APP-VERSION") String xAppVersion) {    
		
    	Map<String, Object>values = new HashMap();
    	values.put("X-SO", xso);
    	values.put("X-APP-VERSION", xAppVersion);
    	
    	return ResponseEntity.status(HttpStatus.OK)
    	        .body(bonificoService.findISPData(values));  
    }
}
