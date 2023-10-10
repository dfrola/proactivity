package com.proactivity.rmq.service;

import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.proactivity.rmq.dto.EmployeeDTO;
import com.proactivity.rmq.mapper.EmployeeMapper;
import com.proactivity.rmq.persistence.entity.Employee;
import com.proactivity.rmq.persistence.repository.EmployeeRepository;

@Service
@Transactional(readOnly = true)
public class EmployeeService {
	
	private EmployeeRepository employeeRepository;
	static EmployeeMapper ENPLOYEE_MAPPER = Mappers.getMapper(EmployeeMapper.class);

	@Autowired
	public EmployeeService(EmployeeRepository employeeRepository) {
		super();
		this.employeeRepository = employeeRepository;
	}
	
	@Transactional
	public void save(EmployeeDTO employeeDTO) {		
		Employee employee = ENPLOYEE_MAPPER.sourceToDestination(employeeDTO);		
		employeeRepository.save(employee);
	}
}
