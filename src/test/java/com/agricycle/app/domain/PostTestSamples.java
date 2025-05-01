package com.agricycle.app.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class PostTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Post getPostSample1() {
        return new Post().id(1L).contenu("contenu1");
    }

    public static Post getPostSample2() {
        return new Post().id(2L).contenu("contenu2");
    }

    public static Post getPostRandomSampleGenerator() {
        return new Post().id(longCount.incrementAndGet()).contenu(UUID.randomUUID().toString());
    }
}
