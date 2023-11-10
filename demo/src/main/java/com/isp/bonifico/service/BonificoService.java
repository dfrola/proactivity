package com.isp.bonifico.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.isp.bonifico.dmrequest.DecisionManagerRequest;
import com.isp.bonifico.dto.CatalogEl;
import com.isp.bonifico.dto.EmployeeDTO;
import com.isp.bonifico.dto.ISPData;
import com.isp.bonifico.persistence.entity.Bonifico;
import com.isp.bonifico.persistence.repository.BonificoRepository;
import com.proactivity.decision.manager.service.DecisionManagerService;

@Service
public class BonificoService {
	
	
	@Autowired	
	private DecisionManagerService decisionManagerService;
	@Autowired
	private BonificoRepository bonificoRepository;
	@Autowired
	private RestTemplate restTemplate;
	
	public ISPData findISPData(Map<String, Object> values) {
		
		CatalogEl el1 = new CatalogEl("bonifico", "/bonifico-v1/home", 5L);
		CatalogEl el2 = new CatalogEl("carte", "/carte-v1/home", 4L);
		List<CatalogEl> catalog = new ArrayList();
		catalog.add(el1);
		catalog.add(el2);
		
		ISPData ispdata = new ISPData(catalog);
		
		
		
		ispdata.setName("San Paolo");
		ispdata.setPrice(new BigDecimal("123.2"));	
		
		EmployeeDTO employeeDTO = new EmployeeDTO();
		employeeDTO.setName("Pippo");
		employeeDTO.setLastName("Pippo");
		employeeDTO.setRepository("mysql");

		
		Optional<Bonifico> b = bonificoRepository.findById(1L);
		String context = b.get().getCategory();
		
		
		//@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
		
		DecisionManagerRequest dmr = new DecisionManagerRequest();
		
		dmr.setContext("TEST-CLIENT");				
		dmr.setConditionMap(values);		
		dmr.setBody(employeeDTO);
		
		System.out.println("Entity EmployeeDTO before DM: " + employeeDTO);
		HttpEntity<DecisionManagerRequest> entity = new HttpEntity<>(dmr);
		employeeDTO = restTemplate.postForObject("http://localhost:8088/proactivity-decision-manager-ms/v1/entities", entity, EmployeeDTO.class);
		
		ispdata.setEmployee(employeeDTO);
		System.out.println("Entity EmployeeDTO after DM - CONTEXT " + dmr.getContext() + ": " + employeeDTO);
		//@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
		
		return (ISPData) decisionManagerService.modify(values, ispdata, context);
	}

}
