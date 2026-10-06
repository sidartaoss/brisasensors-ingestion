package com.brisasensors.ingestion;

import com.brisasensors.ingestion.common.IdGenerator;
import com.brisasensors.ingestion.common.UUIDv7Utils;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;

class UUIDv7Test {

    @Test
    void shouldGenerateUUIDv7() {
        final var uuid1 = IdGenerator.generateTimeBasedUUID();
        final var uuid2 = IdGenerator.generateTimeBasedUUID();
        final var uuid3 = IdGenerator.generateTimeBasedUUID();
        final var uuid4 = IdGenerator.generateTimeBasedUUID();

        System.out.println("UUIDv7: " + uuid1);
        System.out.println("UUIDv7: " + uuid2);
        System.out.println("UUIDv7: " + uuid3);
        System.out.println("UUIDv7: " + uuid4);

        assertNotNull(uuid1);
        assertNotNull(uuid2);
        assertNotNull(uuid3);
        assertNotNull(uuid4);
    }

    @Test
    void shouldGenerateUUIDv7OffsetDateTime() {
        final var uuid1 = IdGenerator.generateTimeBasedUUID();
        final var uuid2 = IdGenerator.generateTimeBasedUUID();
        final var uuid3 = IdGenerator.generateTimeBasedUUID();
        final var uuid4 = IdGenerator.generateTimeBasedUUID();

        System.out.println("UUIDv7: " + UUIDv7Utils.extractOffsetDateTime(uuid1));
        System.out.println("UUIDv7: " + UUIDv7Utils.extractOffsetDateTime(uuid2));
        System.out.println("UUIDv7: " + UUIDv7Utils.extractOffsetDateTime(uuid3));
        System.out.println("UUIDv7: " + UUIDv7Utils.extractOffsetDateTime(uuid4));

        assertNotNull(uuid1);
        assertNotNull(uuid2);
        assertNotNull(uuid3);
        assertNotNull(uuid4);
    }
}
