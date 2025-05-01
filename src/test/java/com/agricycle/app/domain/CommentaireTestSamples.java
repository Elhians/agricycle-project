package com.agricycle.app.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class CommentaireTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Commentaire getCommentaireSample1() {
        return new Commentaire().id(1L).contenu("contenu1");
    }

    public static Commentaire getCommentaireSample2() {
        return new Commentaire().id(2L).contenu("contenu2");
    }

    public static Commentaire getCommentaireRandomSampleGenerator() {
        return new Commentaire().id(longCount.incrementAndGet()).contenu(UUID.randomUUID().toString());
    }
}
