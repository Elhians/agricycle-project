package com.agricycle.app.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class QRCodeTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static QRCode getQRCodeSample1() {
        return new QRCode().id(1L).valeur("valeur1");
    }

    public static QRCode getQRCodeSample2() {
        return new QRCode().id(2L).valeur("valeur2");
    }

    public static QRCode getQRCodeRandomSampleGenerator() {
        return new QRCode().id(longCount.incrementAndGet()).valeur(UUID.randomUUID().toString());
    }
}
