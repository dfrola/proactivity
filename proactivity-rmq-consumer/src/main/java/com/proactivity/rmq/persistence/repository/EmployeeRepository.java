package com.proactivity.rmq.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.proactivity.rmq.persistence.entity.Employee;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {
}
