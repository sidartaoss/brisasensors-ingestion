package com.brisasensors.ingestion.infrastructure.rabbitmq;

import lombok.RequiredArgsConstructor;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.core.ReturnedMessage;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import static com.brisasensors.ingestion.infrastructure.rabbitmq.RabbitMQConfig.READING_REGISTERED_EXCHANGE;

@Component
@RequiredArgsConstructor
public class ReadingRegisteredPublisher {

    private static final long CONFIRM_TIMEOUT_SECONDS = 5;

    private final RabbitTemplate rabbitTemplate;

    public void publish(final ReadingRegisteredEvent event) {
        CorrelationData correlationData = new CorrelationData(event.getId().toString());
        String routingKey = event.getDeviceId().toString();

        rabbitTemplate.convertAndSend(READING_REGISTERED_EXCHANGE, routingKey, event, correlationData);

        awaitConfirmation(correlationData);
    }

    // A leitura só conta como recebida depois que o broker a aceita e a encaminha a uma fila
    private void awaitConfirmation(final CorrelationData correlationData) {
        CorrelationData.Confirm confirm;

        try {
            confirm = correlationData.getFuture().get(CONFIRM_TIMEOUT_SECONDS, TimeUnit.SECONDS);
        } catch (InterruptedException interruptedException) {
            Thread.currentThread().interrupt();
            throw new AmqpException("Interrupted while awaiting broker confirmation", interruptedException);
        } catch (ExecutionException | TimeoutException exception) {
            throw new AmqpException("Broker confirmation not received", exception);
        }

        if (!confirm.ack()) {
            throw new AmqpException("Reading rejected by the broker: " + confirm.reason());
        }

        ReturnedMessage returned = correlationData.getReturned();
        if (returned != null) {
            throw new AmqpException("Reading not routed to any queue: " + returned.getReplyText());
        }
    }
}
