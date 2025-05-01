package com.agricycle.app.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class CommercantTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Commercant getCommercantSample1() {
        return new Commercant().id(1L).adresseCommerce("adresseCommerce1").moyenPaiement("moyenPaiement1");
    }

    public static Commercant getCommercantSample2() {
        return new Commercant().id(2L).adresseCommerce("adresseCommerce2").moyenPaiement("moyenPaiement2");
    }

    public static Commercant getCommercantRandomSampleGenerator() {
        return new Commercant()
            .id(longCount.incrementAndGet())
            .adresseCommerce(UUID.randomUUID().toString())
            .moyenPaiement(UUID.randomUUID().toString());
    }
}
