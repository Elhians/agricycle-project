package com.agricycle.app.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class EntrepriseTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Entreprise getEntrepriseSample1() {
        return new Entreprise()
            .id(1L)
            .nomEntreprise("nomEntreprise1")
            .typeActivite("typeActivite1")
            .licence("licence1")
            .adressePhysique("adressePhysique1");
    }

    public static Entreprise getEntrepriseSample2() {
        return new Entreprise()
            .id(2L)
            .nomEntreprise("nomEntreprise2")
            .typeActivite("typeActivite2")
            .licence("licence2")
            .adressePhysique("adressePhysique2");
    }

    public static Entreprise getEntrepriseRandomSampleGenerator() {
        return new Entreprise()
            .id(longCount.incrementAndGet())
            .nomEntreprise(UUID.randomUUID().toString())
            .typeActivite(UUID.randomUUID().toString())
            .licence(UUID.randomUUID().toString())
            .adressePhysique(UUID.randomUUID().toString());
    }
}
