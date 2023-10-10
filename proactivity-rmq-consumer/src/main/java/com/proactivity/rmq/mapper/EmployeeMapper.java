package com.proactivity.rmq.mapper;

import org.mapstruct.Mapper;

import com.proactivity.rmq.dto.EmployeeDTO;
import com.proactivity.rmq.persistence.entity.Employee;

@Mapper
public interface EmployeeMapper {

	Employee sourceToDestination(EmployeeDTO destination);
	EmployeeDTO destinationToSource(Employee source);	
}
