package com.agricycle.app.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class TransporteurTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Transporteur getTransporteurSample1() {
        return new Transporteur().id(1L).zoneCouverture("zoneCouverture1");
    }

    public static Transporteur getTransporteurSample2() {
        return new Transporteur().id(2L).zoneCouverture("zoneCouverture2");
    }

    public static Transporteur getTransporteurRandomSampleGenerator() {
        return new Transporteur().id(longCount.incrementAndGet()).zoneCouverture(UUID.randomUUID().toString());
    }
}
