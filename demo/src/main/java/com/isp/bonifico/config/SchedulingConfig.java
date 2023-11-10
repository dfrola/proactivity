package com.isp.bonifico.config;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.transaction.annotation.Transactional;

import com.isp.bonifico.service.BonificoService;

@Configuration
@EnableScheduling
public class SchedulingConfig {

    static final Logger LOGGER = LoggerFactory.getLogger(SchedulingConfig.class);

//    private BonificoService bonificoService;
//  
//
//    @Autowired
//    public SchedulingConfig(BonificoService bonificoService) {
//        this.bonificoService = bonificoService;
//        
//    }

    
    //@Scheduled(cron = "*/10 * * * * *")//Ogni 10 secondi
    public void refreshTokenSecrets(){
    	System.out.println(" - @@@ REFRESH TOKEN SECRET BEGIN @@@ - ");
        Map<String, Object>values = new HashMap();
    	values.put("X-SO", "android");
    	values.put("X-APP-VERSION", new BigDecimal("1.1"));
    	//System.out.println("TEST: " + bonificoService.test(values));
    	System.out.println(" - @@@ REFRESH TOKEN SECRET END @@@ - ");
    }

    //@Scheduled(cron = "0 0 * * * *")//Tutti i giorni a ogni ora
    public void IApplicationClientScopeService(){
        LOGGER.info(" - @@@ REFRESH APPLICATION CLIENT SCOPES BEGIN @@@ - ");
       // this.applicationClientsScope = applicationClientScopeService.loadLoadApplicationClientScopes();
        LOGGER.info(" - @@@ REFRESH APPLICATION CLIENT SCOPES END @@@ - ");
    }
}
