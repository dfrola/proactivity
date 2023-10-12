package com.proactivity.rmq.receiver;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageListener;
import org.springframework.stereotype.Service;

@Service
public class RabbitMQListener implements MessageListener {
	
	static final Logger logger = LoggerFactory.getLogger(RabbitMQListener.class);

    public void onMessage(Message message) {
        logger.info("Consumed message - " + new String(message.getBody()));
    }
}
