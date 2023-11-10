package com.isp.bonifico;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

//@SpringBootApplication(scanBasePackages = {"org.dm.lib", "com.isp.bonifico"}, exclude = {DataSourceAutoConfiguration.class })
@SpringBootApplication(scanBasePackages = {"com.proactivity.decision.manager", "com.isp.bonifico"}, exclude = {DataSourceAutoConfiguration.class })
public class DemoApplication extends SpringBootServletInitializer{
	
	@Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(DemoApplication.class);
    }

	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
	}

}//scanBasePackages = "com.proactivity.decision.manager",
