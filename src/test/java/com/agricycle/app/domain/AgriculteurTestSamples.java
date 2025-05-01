package com.agricycle.app.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class AgriculteurTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static Agriculteur getAgriculteurSample1() {
        return new Agriculteur().id(1L).typeProduction("typeProduction1").anneeExperience(1).localisation("localisation1");
    }

    public static Agriculteur getAgriculteurSample2() {
        return new Agriculteur().id(2L).typeProduction("typeProduction2").anneeExperience(2).localisation("localisation2");
    }

    public static Agriculteur getAgriculteurRandomSampleGenerator() {
        return new Agriculteur()
            .id(longCount.incrementAndGet())
            .typeProduction(UUID.randomUUID().toString())
            .anneeExperience(intCount.incrementAndGet())
            .localisation(UUID.randomUUID().toString());
    }
}
