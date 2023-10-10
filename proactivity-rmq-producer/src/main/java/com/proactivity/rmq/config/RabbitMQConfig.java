package com.proactivity.rmq.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.listener.MessageListenerContainer;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.proactivity.rmq.receiver.RabbitMQListener;

@Configuration
public class RabbitMQConfig {

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
    //@Value("#{'${send.to.erica.routingkey}'}")
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
        return rabbitTemplate;
    }

    //create MessageListenerContainer using default connection factory
    @Bean
    MessageListenerContainer messageListenerContainer(ConnectionFactory connectionFactory ) {
        SimpleMessageListenerContainer simpleMessageListenerContainer = new SimpleMessageListenerContainer();
        simpleMessageListenerContainer.setConnectionFactory(connectionFactory);
        simpleMessageListenerContainer.setQueues(queueToErica());
        simpleMessageListenerContainer.setMessageListener(new RabbitMQListener());
        return simpleMessageListenerContainer;

    }
}
