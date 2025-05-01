package com.agricycle.app.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class SignalementTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Signalement getSignalementSample1() {
        return new Signalement().id(1L).raison("raison1");
    }

    public static Signalement getSignalementSample2() {
        return new Signalement().id(2L).raison("raison2");
    }

    public static Signalement getSignalementRandomSampleGenerator() {
        return new Signalement().id(longCount.incrementAndGet()).raison(UUID.randomUUID().toString());
    }
}
