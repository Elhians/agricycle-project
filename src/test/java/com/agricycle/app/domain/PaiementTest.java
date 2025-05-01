package com.agricycle.app.domain;

import static com.agricycle.app.domain.PaiementTestSamples.*;
import static com.agricycle.app.domain.TransactionTestSamples.*;
import static com.agricycle.app.domain.UtilisateurTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.agricycle.app.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class PaiementTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Paiement.class);
        Paiement paiement1 = getPaiementSample1();
        Paiement paiement2 = new Paiement();
        assertThat(paiement1).isNotEqualTo(paiement2);

        paiement2.setId(paiement1.getId());
        assertThat(paiement1).isEqualTo(paiement2);

        paiement2 = getPaiementSample2();
        assertThat(paiement1).isNotEqualTo(paiement2);
    }

    @Test
    void transactionTest() {
        Paiement paiement = getPaiementRandomSampleGenerator();
        Transaction transactionBack = getTransactionRandomSampleGenerator();

        paiement.setTransaction(transactionBack);
        assertThat(paiement.getTransaction()).isEqualTo(transactionBack);

        paiement.transaction(null);
        assertThat(paiement.getTransaction()).isNull();
    }

    @Test
    void acheteurTest() {
        Paiement paiement = getPaiementRandomSampleGenerator();
        Utilisateur utilisateurBack = getUtilisateurRandomSampleGenerator();

        paiement.setAcheteur(utilisateurBack);
        assertThat(paiement.getAcheteur()).isEqualTo(utilisateurBack);

        paiement.acheteur(null);
        assertThat(paiement.getAcheteur()).isNull();
    }
}
