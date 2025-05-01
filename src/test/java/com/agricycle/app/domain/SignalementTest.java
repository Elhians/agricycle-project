package com.agricycle.app.domain;

import static com.agricycle.app.domain.PostTestSamples.*;
import static com.agricycle.app.domain.SignalementTestSamples.*;
import static com.agricycle.app.domain.UtilisateurTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.agricycle.app.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class SignalementTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Signalement.class);
        Signalement signalement1 = getSignalementSample1();
        Signalement signalement2 = new Signalement();
        assertThat(signalement1).isNotEqualTo(signalement2);

        signalement2.setId(signalement1.getId());
        assertThat(signalement1).isEqualTo(signalement2);

        signalement2 = getSignalementSample2();
        assertThat(signalement1).isNotEqualTo(signalement2);
    }

    @Test
    void auteurTest() {
        Signalement signalement = getSignalementRandomSampleGenerator();
        Utilisateur utilisateurBack = getUtilisateurRandomSampleGenerator();

        signalement.setAuteur(utilisateurBack);
        assertThat(signalement.getAuteur()).isEqualTo(utilisateurBack);

        signalement.auteur(null);
        assertThat(signalement.getAuteur()).isNull();
    }

    @Test
    void ciblePostTest() {
        Signalement signalement = getSignalementRandomSampleGenerator();
        Post postBack = getPostRandomSampleGenerator();

        signalement.setCiblePost(postBack);
        assertThat(signalement.getCiblePost()).isEqualTo(postBack);

        signalement.ciblePost(null);
        assertThat(signalement.getCiblePost()).isNull();
    }
}
