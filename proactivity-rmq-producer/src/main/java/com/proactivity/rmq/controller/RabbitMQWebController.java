package com.proactivity.rmq.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.proactivity.rmq.model.EmployeeDTO;
import com.proactivity.rmq.sender.RabbitMQSender;

@RestController
@RequestMapping(value = "/v1/")
public class RabbitMQWebController {
	
	RabbitMQSender rabbitMQSender;
	
	@Autowired
    public RabbitMQWebController(RabbitMQSender rabbitMQSender) {
		super();
		this.rabbitMQSender = rabbitMQSender;
	}    

    @PostMapping(value = "/employees")
    public String enqueue(@RequestBody EmployeeDTO emp) {       
        rabbitMQSender.send(emp);
        return "Message sent to the RabbitMQ Successfully";
    }
}
