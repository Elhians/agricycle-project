package com.agricycle.app.domain;

import static com.agricycle.app.domain.PostTestSamples.*;
import static com.agricycle.app.domain.ReactionTestSamples.*;
import static com.agricycle.app.domain.UtilisateurTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.agricycle.app.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ReactionTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Reaction.class);
        Reaction reaction1 = getReactionSample1();
        Reaction reaction2 = new Reaction();
        assertThat(reaction1).isNotEqualTo(reaction2);

        reaction2.setId(reaction1.getId());
        assertThat(reaction1).isEqualTo(reaction2);

        reaction2 = getReactionSample2();
        assertThat(reaction1).isNotEqualTo(reaction2);
    }

    @Test
    void utilisateurTest() {
        Reaction reaction = getReactionRandomSampleGenerator();
        Utilisateur utilisateurBack = getUtilisateurRandomSampleGenerator();

        reaction.setUtilisateur(utilisateurBack);
        assertThat(reaction.getUtilisateur()).isEqualTo(utilisateurBack);

        reaction.utilisateur(null);
        assertThat(reaction.getUtilisateur()).isNull();
    }

    @Test
    void postTest() {
        Reaction reaction = getReactionRandomSampleGenerator();
        Post postBack = getPostRandomSampleGenerator();

        reaction.setPost(postBack);
        assertThat(reaction.getPost()).isEqualTo(postBack);

        reaction.post(null);
        assertThat(reaction.getPost()).isNull();
    }
}
