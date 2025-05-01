package com.agricycle.app.domain;

import static com.agricycle.app.domain.ConsommateurTestSamples.*;
import static com.agricycle.app.domain.UtilisateurTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.agricycle.app.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ConsommateurTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Consommateur.class);
        Consommateur consommateur1 = getConsommateurSample1();
        Consommateur consommateur2 = new Consommateur();
        assertThat(consommateur1).isNotEqualTo(consommateur2);

        consommateur2.setId(consommateur1.getId());
        assertThat(consommateur1).isEqualTo(consommateur2);

        consommateur2 = getConsommateurSample2();
        assertThat(consommateur1).isNotEqualTo(consommateur2);
    }

    @Test
    void utilisateurTest() {
        Consommateur consommateur = getConsommateurRandomSampleGenerator();
        Utilisateur utilisateurBack = getUtilisateurRandomSampleGenerator();

        consommateur.setUtilisateur(utilisateurBack);
        assertThat(consommateur.getUtilisateur()).isEqualTo(utilisateurBack);

        consommateur.utilisateur(null);
        assertThat(consommateur.getUtilisateur()).isNull();
    }
}
