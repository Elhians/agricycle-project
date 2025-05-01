package com.agricycle.app.domain;

import static com.agricycle.app.domain.TransporteurTestSamples.*;
import static com.agricycle.app.domain.UtilisateurTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.agricycle.app.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class TransporteurTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Transporteur.class);
        Transporteur transporteur1 = getTransporteurSample1();
        Transporteur transporteur2 = new Transporteur();
        assertThat(transporteur1).isNotEqualTo(transporteur2);

        transporteur2.setId(transporteur1.getId());
        assertThat(transporteur1).isEqualTo(transporteur2);

        transporteur2 = getTransporteurSample2();
        assertThat(transporteur1).isNotEqualTo(transporteur2);
    }

    @Test
    void utilisateurTest() {
        Transporteur transporteur = getTransporteurRandomSampleGenerator();
        Utilisateur utilisateurBack = getUtilisateurRandomSampleGenerator();

        transporteur.setUtilisateur(utilisateurBack);
        assertThat(transporteur.getUtilisateur()).isEqualTo(utilisateurBack);

        transporteur.utilisateur(null);
        assertThat(transporteur.getUtilisateur()).isNull();
    }
}
