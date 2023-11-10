package com.isp.bonifico.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.isp.bonifico.persistence.entity.Bonifico;

public interface BonificoRepository extends JpaRepository<Bonifico, Long>{	

}
