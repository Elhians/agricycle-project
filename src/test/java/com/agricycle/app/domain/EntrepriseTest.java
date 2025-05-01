package com.agricycle.app.domain;

import static com.agricycle.app.domain.EntrepriseTestSamples.*;
import static com.agricycle.app.domain.UtilisateurTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.agricycle.app.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class EntrepriseTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Entreprise.class);
        Entreprise entreprise1 = getEntrepriseSample1();
        Entreprise entreprise2 = new Entreprise();
        assertThat(entreprise1).isNotEqualTo(entreprise2);

        entreprise2.setId(entreprise1.getId());
        assertThat(entreprise1).isEqualTo(entreprise2);

        entreprise2 = getEntrepriseSample2();
        assertThat(entreprise1).isNotEqualTo(entreprise2);
    }

    @Test
    void utilisateurTest() {
        Entreprise entreprise = getEntrepriseRandomSampleGenerator();
        Utilisateur utilisateurBack = getUtilisateurRandomSampleGenerator();

        entreprise.setUtilisateur(utilisateurBack);
        assertThat(entreprise.getUtilisateur()).isEqualTo(utilisateurBack);

        entreprise.utilisateur(null);
        assertThat(entreprise.getUtilisateur()).isNull();
    }
}
