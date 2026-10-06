package com.brisasensors.ingestion.infrastructure.rabbitmq;


import org.springframework.amqp.core.ExchangeBuilder;
import org.springframework.amqp.core.FanoutExchange;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.support.converter.JacksonJsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import tools.jackson.databind.json.JsonMapper;

@Configuration
public class RabbitMQConfig {

    public static final String READING_REGISTERED_EXCHANGE = "ingestion.reading-registered.v1.e";

    @Bean
    public JacksonJsonMessageConverter jacksonJsonMessageConverter(final JsonMapper jsonMapper) {
        return new JacksonJsonMessageConverter(jsonMapper);
    }

    @Bean
    public RabbitAdmin rabbitAdmin(ConnectionFactory connectionFactory) {
        return new RabbitAdmin(connectionFactory);
    }

    @Bean
    public FanoutExchange readingRegisteredExchange() {
        return ExchangeBuilder.fanoutExchange(READING_REGISTERED_EXCHANGE).build();
    }
}
