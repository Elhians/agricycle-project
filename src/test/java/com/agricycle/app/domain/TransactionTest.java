package com.agricycle.app.domain;

import static com.agricycle.app.domain.ProduitTestSamples.*;
import static com.agricycle.app.domain.TransactionTestSamples.*;
import static com.agricycle.app.domain.UtilisateurTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.agricycle.app.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class TransactionTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Transaction.class);
        Transaction transaction1 = getTransactionSample1();
        Transaction transaction2 = new Transaction();
        assertThat(transaction1).isNotEqualTo(transaction2);

        transaction2.setId(transaction1.getId());
        assertThat(transaction1).isEqualTo(transaction2);

        transaction2 = getTransactionSample2();
        assertThat(transaction1).isNotEqualTo(transaction2);
    }

    @Test
    void produitTest() {
        Transaction transaction = getTransactionRandomSampleGenerator();
        Produit produitBack = getProduitRandomSampleGenerator();

        transaction.setProduit(produitBack);
        assertThat(transaction.getProduit()).isEqualTo(produitBack);

        transaction.produit(null);
        assertThat(transaction.getProduit()).isNull();
    }

    @Test
    void acheteurTest() {
        Transaction transaction = getTransactionRandomSampleGenerator();
        Utilisateur utilisateurBack = getUtilisateurRandomSampleGenerator();

        transaction.setAcheteur(utilisateurBack);
        assertThat(transaction.getAcheteur()).isEqualTo(utilisateurBack);

        transaction.acheteur(null);
        assertThat(transaction.getAcheteur()).isNull();
    }
}
