package com.proactivity.rmq.receiver;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.proactivity.rmq.dto.EmployeeDTO;
import com.proactivity.rmq.sender.RabbitMQSender;
import com.proactivity.rmq.service.EmployeeService;

@Service
public class RabbitMQListener implements MessageListener {
	
	static final Logger logger = LoggerFactory.getLogger(RabbitMQListener.class);
    
    RabbitMQSender rabbitMQSender;
    EmployeeService employeeService;
        
    @Autowired
    public RabbitMQListener(RabbitMQSender rabbitMQSender, EmployeeService employeeService) {
		super();
		this.rabbitMQSender = rabbitMQSender;
		this.employeeService = employeeService;
	}
    
    public void onMessage(Message message) {    	
    	
    	
      String messageBody = new String(message.getBody());
      
      ObjectMapper objectMapper = new ObjectMapper();
      try {
		EmployeeDTO employee = objectMapper.readValue(messageBody, EmployeeDTO.class);
		logger.info("Read from queue Employee - " + employee);
		employeeService.save(employee);
		logger.info("Employee saved on db succesfully ");
	  } catch (Exception e) {
		  logger.error("Consumed Message - " + messageBody, e);
		  throw new RuntimeException(e);
	  }
      rabbitMQSender.send("Employee saved on db succesfully: " + messageBody);//Risponde al chiamante sulla coda di risposta
   } 
}
