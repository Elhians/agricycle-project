package com.agricycle.app.domain;

import static com.agricycle.app.domain.AgriculteurTestSamples.*;
import static com.agricycle.app.domain.CommercantTestSamples.*;
import static com.agricycle.app.domain.ConsommateurTestSamples.*;
import static com.agricycle.app.domain.EntrepriseTestSamples.*;
import static com.agricycle.app.domain.OrganisationTestSamples.*;
import static com.agricycle.app.domain.TransporteurTestSamples.*;
import static com.agricycle.app.domain.UtilisateurTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.agricycle.app.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class UtilisateurTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Utilisateur.class);
        Utilisateur utilisateur1 = getUtilisateurSample1();
        Utilisateur utilisateur2 = new Utilisateur();
        assertThat(utilisateur1).isNotEqualTo(utilisateur2);

        utilisateur2.setId(utilisateur1.getId());
        assertThat(utilisateur1).isEqualTo(utilisateur2);

        utilisateur2 = getUtilisateurSample2();
        assertThat(utilisateur1).isNotEqualTo(utilisateur2);
    }

    @Test
    void agriculteurTest() {
        Utilisateur utilisateur = getUtilisateurRandomSampleGenerator();
        Agriculteur agriculteurBack = getAgriculteurRandomSampleGenerator();

        utilisateur.setAgriculteur(agriculteurBack);
        assertThat(utilisateur.getAgriculteur()).isEqualTo(agriculteurBack);
        assertThat(agriculteurBack.getUtilisateur()).isEqualTo(utilisateur);

        utilisateur.agriculteur(null);
        assertThat(utilisateur.getAgriculteur()).isNull();
        assertThat(agriculteurBack.getUtilisateur()).isNull();
    }

    @Test
    void commercantTest() {
        Utilisateur utilisateur = getUtilisateurRandomSampleGenerator();
        Commercant commercantBack = getCommercantRandomSampleGenerator();

        utilisateur.setCommercant(commercantBack);
        assertThat(utilisateur.getCommercant()).isEqualTo(commercantBack);
        assertThat(commercantBack.getUtilisateur()).isEqualTo(utilisateur);

        utilisateur.commercant(null);
        assertThat(utilisateur.getCommercant()).isNull();
        assertThat(commercantBack.getUtilisateur()).isNull();
    }

    @Test
    void transporteurTest() {
        Utilisateur utilisateur = getUtilisateurRandomSampleGenerator();
        Transporteur transporteurBack = getTransporteurRandomSampleGenerator();

        utilisateur.setTransporteur(transporteurBack);
        assertThat(utilisateur.getTransporteur()).isEqualTo(transporteurBack);
        assertThat(transporteurBack.getUtilisateur()).isEqualTo(utilisateur);

        utilisateur.transporteur(null);
        assertThat(utilisateur.getTransporteur()).isNull();
        assertThat(transporteurBack.getUtilisateur()).isNull();
    }

    @Test
    void consommateurTest() {
        Utilisateur utilisateur = getUtilisateurRandomSampleGenerator();
        Consommateur consommateurBack = getConsommateurRandomSampleGenerator();

        utilisateur.setConsommateur(consommateurBack);
        assertThat(utilisateur.getConsommateur()).isEqualTo(consommateurBack);
        assertThat(consommateurBack.getUtilisateur()).isEqualTo(utilisateur);

        utilisateur.consommateur(null);
        assertThat(utilisateur.getConsommateur()).isNull();
        assertThat(consommateurBack.getUtilisateur()).isNull();
    }

    @Test
    void organisationTest() {
        Utilisateur utilisateur = getUtilisateurRandomSampleGenerator();
        Organisation organisationBack = getOrganisationRandomSampleGenerator();

        utilisateur.setOrganisation(organisationBack);
        assertThat(utilisateur.getOrganisation()).isEqualTo(organisationBack);
        assertThat(organisationBack.getUtilisateur()).isEqualTo(utilisateur);

        utilisateur.organisation(null);
        assertThat(utilisateur.getOrganisation()).isNull();
        assertThat(organisationBack.getUtilisateur()).isNull();
    }

    @Test
    void entrepriseTest() {
        Utilisateur utilisateur = getUtilisateurRandomSampleGenerator();
        Entreprise entrepriseBack = getEntrepriseRandomSampleGenerator();

        utilisateur.setEntreprise(entrepriseBack);
        assertThat(utilisateur.getEntreprise()).isEqualTo(entrepriseBack);
        assertThat(entrepriseBack.getUtilisateur()).isEqualTo(utilisateur);

        utilisateur.entreprise(null);
        assertThat(utilisateur.getEntreprise()).isNull();
        assertThat(entrepriseBack.getUtilisateur()).isNull();
    }
}
