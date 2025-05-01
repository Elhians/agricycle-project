package com.agricycle.app.domain;

import static com.agricycle.app.domain.AgriculteurTestSamples.*;
import static com.agricycle.app.domain.UtilisateurTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.agricycle.app.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class AgriculteurTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Agriculteur.class);
        Agriculteur agriculteur1 = getAgriculteurSample1();
        Agriculteur agriculteur2 = new Agriculteur();
        assertThat(agriculteur1).isNotEqualTo(agriculteur2);

        agriculteur2.setId(agriculteur1.getId());
        assertThat(agriculteur1).isEqualTo(agriculteur2);

        agriculteur2 = getAgriculteurSample2();
        assertThat(agriculteur1).isNotEqualTo(agriculteur2);
    }

    @Test
    void utilisateurTest() {
        Agriculteur agriculteur = getAgriculteurRandomSampleGenerator();
        Utilisateur utilisateurBack = getUtilisateurRandomSampleGenerator();

        agriculteur.setUtilisateur(utilisateurBack);
        assertThat(agriculteur.getUtilisateur()).isEqualTo(utilisateurBack);

        agriculteur.utilisateur(null);
        assertThat(agriculteur.getUtilisateur()).isNull();
    }
}
