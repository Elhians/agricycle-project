package com.agricycle.app.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class ConsommateurTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Consommateur getConsommateurSample1() {
        return new Consommateur().id(1L).preferences("preferences1");
    }

    public static Consommateur getConsommateurSample2() {
        return new Consommateur().id(2L).preferences("preferences2");
    }

    public static Consommateur getConsommateurRandomSampleGenerator() {
        return new Consommateur().id(longCount.incrementAndGet()).preferences(UUID.randomUUID().toString());
    }
}
