package com.agricycle.app.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class ContenuEducatifTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static ContenuEducatif getContenuEducatifSample1() {
        return new ContenuEducatif().id(1L).titre("titre1").description("description1").url("url1");
    }

    public static ContenuEducatif getContenuEducatifSample2() {
        return new ContenuEducatif().id(2L).titre("titre2").description("description2").url("url2");
    }

    public static ContenuEducatif getContenuEducatifRandomSampleGenerator() {
        return new ContenuEducatif()
            .id(longCount.incrementAndGet())
            .titre(UUID.randomUUID().toString())
            .description(UUID.randomUUID().toString())
            .url(UUID.randomUUID().toString());
    }
}
