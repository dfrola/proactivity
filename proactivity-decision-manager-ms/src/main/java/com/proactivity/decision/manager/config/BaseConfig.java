package com.proactivity.decision.manager.config;

import org.jsfr.json.JacksonParser;
import org.jsfr.json.JsonSurfer;
import org.jsfr.json.provider.JacksonProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BaseConfig {
	
	@Bean
	public JsonSurfer jsonSurfer() {
		return new JsonSurfer(JacksonParser.INSTANCE, JacksonProvider.INSTANCE);
	}

}
