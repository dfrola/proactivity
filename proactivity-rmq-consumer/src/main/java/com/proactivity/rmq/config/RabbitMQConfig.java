package com.proactivity.rmq.config;

import java.util.List;

import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.listener.MessageListenerContainer;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.support.converter.DefaultJackson2JavaTypeMapper;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.CollectionLikeType;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.proactivity.rmq.dto.EmployeeDTO;
import com.proactivity.rmq.receiver.RabbitMQListener;

@Configuration
public class RabbitMQConfig {


    @Autowired
    RabbitMQListener rabbitMQListener;

    @Value("${send.to.aci.queue}")
    String queueSendToACI;

    @Value("${send.to.erica.queue}")
    String queueSendToErica;

    @Value("${send.to.aci.exchange}")
    String toACIexchange;

    @Value("${send.to.aci.routingkey}")
    private String toACIroutingkey;

    @Value("${send.to.erica.exchange}")
    String toEricaexchange;

    @Value("${send.to.erica.routingkey}")
    private String toEricaroutingkey;

    @Bean
    Queue queueToACI() {
        return new Queue(queueSendToACI, true);
    }

    @Bean
    Queue queueToErica() {
        return new Queue(queueSendToErica, true);
    }

    @Bean
    DirectExchange exchangeToACI() {
        return new DirectExchange(toACIexchange);
    }

    @Bean
    DirectExchange exchangeToErica() {
        return new DirectExchange(toEricaexchange);
    }

    @Bean
    Binding bindingQueueToACI(Queue queueToACI, DirectExchange exchangeToACI) {
        return BindingBuilder.bind(queueToACI).to(exchangeToACI).with(toACIroutingkey);
    }

    @Bean
    Binding bindingQueueToErica(Queue queueToErica, DirectExchange exchangeToErica) {
        return BindingBuilder.bind(queueToErica).to(exchangeToErica).with(toEricaroutingkey);
    }

    @Bean
    public MessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }


    @Bean
    public AmqpTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        final RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(jsonMessageConverter());
        //rabbitTemplate.setMessageConverter(employeeConverter());
        return rabbitTemplate;
    }

    //create MessageListenerContainer using default connection factory
    @Bean
    MessageListenerContainer messageListenerContainer(ConnectionFactory connectionFactory ) {
        SimpleMessageListenerContainer simpleMessageListenerContainer = new SimpleMessageListenerContainer();
        simpleMessageListenerContainer.setConnectionFactory(connectionFactory);
        simpleMessageListenerContainer.setQueues(queueToACI());
        simpleMessageListenerContainer.setMessageListener(rabbitMQListener);
        return simpleMessageListenerContainer;

    }
    
    @Bean
    public MessageConverter employeeConverter() {
        Jackson2JsonMessageConverter converter = new Jackson2JsonMessageConverter();
        converter.setJavaTypeMapper(new DefaultJackson2JavaTypeMapper() {

            @Override
            public JavaType toJavaType(MessageProperties properties) {
                JavaType javaType = super.toJavaType(properties);
                if (javaType instanceof CollectionLikeType) {
                    return TypeFactory.defaultInstance()
                            .constructCollectionLikeType(List.class, EmployeeDTO.class);
                }
                else {
                    return javaType;
                }
            }

        });
        return converter;
    }

    //create custom connection factory
	/*@Bean
	ConnectionFactory connectionFactory() {
		CachingConnectionFactory cachingConnectionFactory = new CachingConnectionFactory("localhost");
		cachingConnectionFactory.setUsername(username);
		cachingConnectionFactory.setUsername(password);
		return cachingConnectionFactory;
	}*/

    //create MessageListenerContainer using custom connection factory
	/*@Bean
	MessageListenerContainer messageListenerContainer() {
		SimpleMessageListenerContainer simpleMessageListenerContainer = new SimpleMessageListenerContainer();
		simpleMessageListenerContainer.setConnectionFactory(connectionFactory());
		simpleMessageListenerContainer.setQueues(queue());
		simpleMessageListenerContainer.setMessageListener(new RabbitMQListner());
		return simpleMessageListenerContainer;

	}*/

}

