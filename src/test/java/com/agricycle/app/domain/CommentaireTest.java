package com.agricycle.app.domain;

import static com.agricycle.app.domain.CommentaireTestSamples.*;
import static com.agricycle.app.domain.PostTestSamples.*;
import static com.agricycle.app.domain.UtilisateurTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.agricycle.app.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class CommentaireTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Commentaire.class);
        Commentaire commentaire1 = getCommentaireSample1();
        Commentaire commentaire2 = new Commentaire();
        assertThat(commentaire1).isNotEqualTo(commentaire2);

        commentaire2.setId(commentaire1.getId());
        assertThat(commentaire1).isEqualTo(commentaire2);

        commentaire2 = getCommentaireSample2();
        assertThat(commentaire1).isNotEqualTo(commentaire2);
    }

    @Test
    void postTest() {
        Commentaire commentaire = getCommentaireRandomSampleGenerator();
        Post postBack = getPostRandomSampleGenerator();

        commentaire.setPost(postBack);
        assertThat(commentaire.getPost()).isEqualTo(postBack);

        commentaire.post(null);
        assertThat(commentaire.getPost()).isNull();
    }

    @Test
    void auteurTest() {
        Commentaire commentaire = getCommentaireRandomSampleGenerator();
        Utilisateur utilisateurBack = getUtilisateurRandomSampleGenerator();

        commentaire.setAuteur(utilisateurBack);
        assertThat(commentaire.getAuteur()).isEqualTo(utilisateurBack);

        commentaire.auteur(null);
        assertThat(commentaire.getAuteur()).isNull();
    }
}
