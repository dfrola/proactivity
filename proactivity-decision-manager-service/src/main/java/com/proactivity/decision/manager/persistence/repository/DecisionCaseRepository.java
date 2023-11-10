package com.proactivity.decision.manager.persistence.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.proactivity.decision.manager.persistence.entity.DecisionCase;

public interface DecisionCaseRepository extends JpaRepository<DecisionCase, Long>{	

	@Query("SELECT dc FROM DecisionCase dc WHERE dc.javaClassName = :javaClassName and dc.enabled = true")
	List<DecisionCase> findByJavaClassName(String javaClassName);
	
	@Query("SELECT dc FROM DecisionCase dc WHERE dc.javaClassName = :javaClassName and dc.category = :category and dc.enabled = true")
	List<DecisionCase> find(String javaClassName, String category);
	
	@Query("SELECT dc FROM DecisionCase dc WHERE dc.context = :context and dc.category = :category and dc.enabled = true")
	List<DecisionCase> findByContextAndCategory(String context, String category);
	
	@Query("SELECT dc FROM DecisionCase dc WHERE dc.javaClassName = :javaClassName and dc.category = :category and context = :context and dc.enabled = true")
	List<DecisionCase> find(String javaClassName, String category, String context);
}
