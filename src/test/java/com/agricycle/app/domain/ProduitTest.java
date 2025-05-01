package com.agricycle.app.domain;

import static com.agricycle.app.domain.ProduitTestSamples.*;
import static com.agricycle.app.domain.UtilisateurTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.agricycle.app.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ProduitTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Produit.class);
        Produit produit1 = getProduitSample1();
        Produit produit2 = new Produit();
        assertThat(produit1).isNotEqualTo(produit2);

        produit2.setId(produit1.getId());
        assertThat(produit1).isEqualTo(produit2);

        produit2 = getProduitSample2();
        assertThat(produit1).isNotEqualTo(produit2);
    }

    @Test
    void vendeurTest() {
        Produit produit = getProduitRandomSampleGenerator();
        Utilisateur utilisateurBack = getUtilisateurRandomSampleGenerator();

        produit.setVendeur(utilisateurBack);
        assertThat(produit.getVendeur()).isEqualTo(utilisateurBack);

        produit.vendeur(null);
        assertThat(produit.getVendeur()).isNull();
    }

    @Test
    void acheteurTest() {
        Produit produit = getProduitRandomSampleGenerator();
        Utilisateur utilisateurBack = getUtilisateurRandomSampleGenerator();

        produit.setAcheteur(utilisateurBack);
        assertThat(produit.getAcheteur()).isEqualTo(utilisateurBack);

        produit.acheteur(null);
        assertThat(produit.getAcheteur()).isNull();
    }
}
