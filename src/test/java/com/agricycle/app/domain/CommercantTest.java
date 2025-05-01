package com.agricycle.app.domain;

import static com.agricycle.app.domain.CommercantTestSamples.*;
import static com.agricycle.app.domain.UtilisateurTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.agricycle.app.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class CommercantTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Commercant.class);
        Commercant commercant1 = getCommercantSample1();
        Commercant commercant2 = new Commercant();
        assertThat(commercant1).isNotEqualTo(commercant2);

        commercant2.setId(commercant1.getId());
        assertThat(commercant1).isEqualTo(commercant2);

        commercant2 = getCommercantSample2();
        assertThat(commercant1).isNotEqualTo(commercant2);
    }

    @Test
    void utilisateurTest() {
        Commercant commercant = getCommercantRandomSampleGenerator();
        Utilisateur utilisateurBack = getUtilisateurRandomSampleGenerator();

        commercant.setUtilisateur(utilisateurBack);
        assertThat(commercant.getUtilisateur()).isEqualTo(utilisateurBack);

        commercant.utilisateur(null);
        assertThat(commercant.getUtilisateur()).isNull();
    }
}
