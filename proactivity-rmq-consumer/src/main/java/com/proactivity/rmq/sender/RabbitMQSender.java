package com.proactivity.rmq.sender;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.proactivity.rmq.dto.EmployeeDTO;
import com.proactivity.rmq.receiver.RabbitMQListener;

@Service
public class RabbitMQSender {
	
	static final Logger logger = LoggerFactory.getLogger(RabbitMQSender.class);
    
    private AmqpTemplate rabbitTemplate;
        
    @Autowired
    public RabbitMQSender(AmqpTemplate rabbitTemplate) {
		super();
		this.rabbitTemplate = rabbitTemplate;
	}

	@Value("${send.to.erica.exchange}")
    private String exchange;

    @Value("${send.to.erica.routingkey}")
    private String routingkey;

    public void send(EmployeeDTO company) {
        rabbitTemplate.convertAndSend(exchange, routingkey, company);
        System.out.println("Send msg = " + company);
    }

    public void send(String message) {
        rabbitTemplate.convertAndSend(exchange, routingkey, message);
        logger.info("Send message = " + message);
    }
}
