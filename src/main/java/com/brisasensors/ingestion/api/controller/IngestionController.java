package com.brisasensors.ingestion.api.controller;

import com.brisasensors.ingestion.common.IdGenerator;
import com.brisasensors.ingestion.infrastructure.rabbitmq.ReadingRegisteredEvent;
import com.brisasensors.ingestion.infrastructure.rabbitmq.ReadingRegisteredPublisher;
import io.hypersistence.tsid.TSID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.AmqpException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.time.OffsetDateTime;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.SERVICE_UNAVAILABLE;
import static org.springframework.http.MediaType.TEXT_PLAIN_VALUE;

@RestController
@RequestMapping("/api/devices/{deviceId}/readings/data")
@Slf4j
@RequiredArgsConstructor
public class IngestionController {

    private static final double MAX_CO2_PPM = 1_000_000;

    private final ReadingRegisteredPublisher readingRegisteredPublisher;

    @PostMapping(consumes = TEXT_PLAIN_VALUE)
    public void ingestData(@PathVariable TSID deviceId, @RequestBody String input) {
        if (input == null || input.isBlank()) {
            throw new ResponseStatusException(BAD_REQUEST);
        }

        Double co2Ppm;

        try {
            co2Ppm = Double.parseDouble(input);
        } catch (NumberFormatException numberFormatException) {
            throw new ResponseStatusException(BAD_REQUEST, "Invalid CO2 value");
        }

        if (!Double.isFinite(co2Ppm) || co2Ppm < 0 || co2Ppm > MAX_CO2_PPM) {
            throw new ResponseStatusException(BAD_REQUEST, "Invalid CO2 value");
        }

        // O adaptador HTTP não recebe a hora da medição; usa a hora de chegada
        OffsetDateTime registeredAt = OffsetDateTime.now();

        ReadingRegisteredEvent event = ReadingRegisteredEvent.builder()
                .id(IdGenerator.generateTimeBasedUUID())
                .deviceId(deviceId)
                .measuredAt(registeredAt)
                .registeredAt(registeredAt)
                .co2Ppm(co2Ppm)
                .build();

        log.info(event.toString());

        try {
            readingRegisteredPublisher.publish(event);
        } catch (AmqpException amqpException) {
            log.warn("Reading not published: {}", amqpException.getMessage());
            throw new ResponseStatusException(SERVICE_UNAVAILABLE, "Reading not accepted, try again later");
        }
    }

}
