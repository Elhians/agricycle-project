package com.agricycle.app.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class ProduitTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static Produit getProduitSample1() {
        return new Produit().id(1L).nom("nom1").description("description1").quantite(1).imageUrl("imageUrl1");
    }

    public static Produit getProduitSample2() {
        return new Produit().id(2L).nom("nom2").description("description2").quantite(2).imageUrl("imageUrl2");
    }

    public static Produit getProduitRandomSampleGenerator() {
        return new Produit()
            .id(longCount.incrementAndGet())
            .nom(UUID.randomUUID().toString())
            .description(UUID.randomUUID().toString())
            .quantite(intCount.incrementAndGet())
            .imageUrl(UUID.randomUUID().toString());
    }
}
