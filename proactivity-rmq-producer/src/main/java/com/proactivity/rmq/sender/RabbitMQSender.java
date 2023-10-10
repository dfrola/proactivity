package com.proactivity.rmq.sender;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.proactivity.rmq.model.EmployeeDTO;

@Service
public class RabbitMQSender {
	
	static final Logger logger = LoggerFactory.getLogger(RabbitMQSender.class);
	
	private AmqpTemplate rabbitTemplate;
	
	@Autowired
    public RabbitMQSender(AmqpTemplate rabbitTemplate) {
		super();
		this.rabbitTemplate = rabbitTemplate;
	}   

    @Value("${send.to.aci.exchange}")
    private String exchange;

    @Value("${send.to.aci.routingkey}")
    private String routingkey;

    public void send(EmployeeDTO employeeDTO) {
        rabbitTemplate.convertAndSend(exchange, routingkey, employeeDTO);
        logger.info("Send message = " + employeeDTO);
    }
}
