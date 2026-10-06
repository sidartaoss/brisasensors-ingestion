package com.brisasensors.ingestion.infrastructure.rabbitmq;

import io.hypersistence.tsid.TSID;
import lombok.Builder;
import lombok.Data;

import java.time.OffsetDateTime;
import java.util.UUID;

@Data
@Builder
public class ReadingRegisteredEvent {
    private UUID id;
    private TSID deviceId;
    private OffsetDateTime measuredAt;
    private OffsetDateTime registeredAt;
    private Double co2Ppm;
}
