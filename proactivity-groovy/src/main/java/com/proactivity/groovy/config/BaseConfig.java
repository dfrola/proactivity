package com.proactivity.groovy.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.proactivity.groovy.GroovyManager;

@Configuration
public class BaseConfig {
	
	
	@Bean
	public GroovyManager groovyMath() {		
		return new GroovyManager("src/main/groovy/");	
	}

}
