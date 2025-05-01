package com.agricycle.app.domain;

import static com.agricycle.app.domain.ContenuEducatifTestSamples.*;
import static com.agricycle.app.domain.UtilisateurTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.agricycle.app.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ContenuEducatifTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ContenuEducatif.class);
        ContenuEducatif contenuEducatif1 = getContenuEducatifSample1();
        ContenuEducatif contenuEducatif2 = new ContenuEducatif();
        assertThat(contenuEducatif1).isNotEqualTo(contenuEducatif2);

        contenuEducatif2.setId(contenuEducatif1.getId());
        assertThat(contenuEducatif1).isEqualTo(contenuEducatif2);

        contenuEducatif2 = getContenuEducatifSample2();
        assertThat(contenuEducatif1).isNotEqualTo(contenuEducatif2);
    }

    @Test
    void auteurTest() {
        ContenuEducatif contenuEducatif = getContenuEducatifRandomSampleGenerator();
        Utilisateur utilisateurBack = getUtilisateurRandomSampleGenerator();

        contenuEducatif.setAuteur(utilisateurBack);
        assertThat(contenuEducatif.getAuteur()).isEqualTo(utilisateurBack);

        contenuEducatif.auteur(null);
        assertThat(contenuEducatif.getAuteur()).isNull();
    }
}
