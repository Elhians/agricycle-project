package com.agricycle.app.web.rest;

import static com.agricycle.app.domain.EntrepriseAsserts.*;
import static com.agricycle.app.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.agricycle.app.IntegrationTest;
import com.agricycle.app.domain.Entreprise;
import com.agricycle.app.domain.Utilisateur;
import com.agricycle.app.repository.EntrepriseRepository;
import com.agricycle.app.service.dto.EntrepriseDTO;
import com.agricycle.app.service.mapper.EntrepriseMapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManager;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link EntrepriseResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class EntrepriseResourceIT {

    private static final String DEFAULT_NOM_ENTREPRISE = "AAAAAAAAAA";
    private static final String UPDATED_NOM_ENTREPRISE = "BBBBBBBBBB";

    private static final String DEFAULT_TYPE_ACTIVITE = "AAAAAAAAAA";
    private static final String UPDATED_TYPE_ACTIVITE = "BBBBBBBBBB";

    private static final String DEFAULT_LICENCE = "AAAAAAAAAA";
    private static final String UPDATED_LICENCE = "BBBBBBBBBB";

    private static final String DEFAULT_ADRESSE_PHYSIQUE = "AAAAAAAAAA";
    private static final String UPDATED_ADRESSE_PHYSIQUE = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/entreprises";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private EntrepriseRepository entrepriseRepository;

    @Autowired
    private EntrepriseMapper entrepriseMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restEntrepriseMockMvc;

    private Entreprise entreprise;

    private Entreprise insertedEntreprise;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Entreprise createEntity() {
        return new Entreprise()
            .nomEntreprise(DEFAULT_NOM_ENTREPRISE)
            .typeActivite(DEFAULT_TYPE_ACTIVITE)
            .licence(DEFAULT_LICENCE)
            .adressePhysique(DEFAULT_ADRESSE_PHYSIQUE);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Entreprise createUpdatedEntity() {
        return new Entreprise()
            .nomEntreprise(UPDATED_NOM_ENTREPRISE)
            .typeActivite(UPDATED_TYPE_ACTIVITE)
            .licence(UPDATED_LICENCE)
            .adressePhysique(UPDATED_ADRESSE_PHYSIQUE);
    }

    @BeforeEach
    void initTest() {
        entreprise = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedEntreprise != null) {
            entrepriseRepository.delete(insertedEntreprise);
            insertedEntreprise = null;
        }
    }

    @Test
    @Transactional
    void createEntreprise() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Entreprise
        EntrepriseDTO entrepriseDTO = entrepriseMapper.toDto(entreprise);
        var returnedEntrepriseDTO = om.readValue(
            restEntrepriseMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(entrepriseDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            EntrepriseDTO.class
        );

        // Validate the Entreprise in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedEntreprise = entrepriseMapper.toEntity(returnedEntrepriseDTO);
        assertEntrepriseUpdatableFieldsEquals(returnedEntreprise, getPersistedEntreprise(returnedEntreprise));

        insertedEntreprise = returnedEntreprise;
    }

    @Test
    @Transactional
    void createEntrepriseWithExistingId() throws Exception {
        // Create the Entreprise with an existing ID
        entreprise.setId(1L);
        EntrepriseDTO entrepriseDTO = entrepriseMapper.toDto(entreprise);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restEntrepriseMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(entrepriseDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Entreprise in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllEntreprises() throws Exception {
        // Initialize the database
        insertedEntreprise = entrepriseRepository.saveAndFlush(entreprise);

        // Get all the entrepriseList
        restEntrepriseMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(entreprise.getId().intValue())))
            .andExpect(jsonPath("$.[*].nomEntreprise").value(hasItem(DEFAULT_NOM_ENTREPRISE)))
            .andExpect(jsonPath("$.[*].typeActivite").value(hasItem(DEFAULT_TYPE_ACTIVITE)))
            .andExpect(jsonPath("$.[*].licence").value(hasItem(DEFAULT_LICENCE)))
            .andExpect(jsonPath("$.[*].adressePhysique").value(hasItem(DEFAULT_ADRESSE_PHYSIQUE)));
    }

    @Test
    @Transactional
    void getEntreprise() throws Exception {
        // Initialize the database
        insertedEntreprise = entrepriseRepository.saveAndFlush(entreprise);

        // Get the entreprise
        restEntrepriseMockMvc
            .perform(get(ENTITY_API_URL_ID, entreprise.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(entreprise.getId().intValue()))
            .andExpect(jsonPath("$.nomEntreprise").value(DEFAULT_NOM_ENTREPRISE))
            .andExpect(jsonPath("$.typeActivite").value(DEFAULT_TYPE_ACTIVITE))
            .andExpect(jsonPath("$.licence").value(DEFAULT_LICENCE))
            .andExpect(jsonPath("$.adressePhysique").value(DEFAULT_ADRESSE_PHYSIQUE));
    }

    @Test
    @Transactional
    void getEntreprisesByIdFiltering() throws Exception {
        // Initialize the database
        insertedEntreprise = entrepriseRepository.saveAndFlush(entreprise);

        Long id = entreprise.getId();

        defaultEntrepriseFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultEntrepriseFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultEntrepriseFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllEntreprisesByNomEntrepriseIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedEntreprise = entrepriseRepository.saveAndFlush(entreprise);

        // Get all the entrepriseList where nomEntreprise equals to
        defaultEntrepriseFiltering("nomEntreprise.equals=" + DEFAULT_NOM_ENTREPRISE, "nomEntreprise.equals=" + UPDATED_NOM_ENTREPRISE);
    }

    @Test
    @Transactional
    void getAllEntreprisesByNomEntrepriseIsInShouldWork() throws Exception {
        // Initialize the database
        insertedEntreprise = entrepriseRepository.saveAndFlush(entreprise);

        // Get all the entrepriseList where nomEntreprise in
        defaultEntrepriseFiltering(
            "nomEntreprise.in=" + DEFAULT_NOM_ENTREPRISE + "," + UPDATED_NOM_ENTREPRISE,
            "nomEntreprise.in=" + UPDATED_NOM_ENTREPRISE
        );
    }

    @Test
    @Transactional
    void getAllEntreprisesByNomEntrepriseIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedEntreprise = entrepriseRepository.saveAndFlush(entreprise);

        // Get all the entrepriseList where nomEntreprise is not null
        defaultEntrepriseFiltering("nomEntreprise.specified=true", "nomEntreprise.specified=false");
    }

    @Test
    @Transactional
    void getAllEntreprisesByNomEntrepriseContainsSomething() throws Exception {
        // Initialize the database
        insertedEntreprise = entrepriseRepository.saveAndFlush(entreprise);

        // Get all the entrepriseList where nomEntreprise contains
        defaultEntrepriseFiltering("nomEntreprise.contains=" + DEFAULT_NOM_ENTREPRISE, "nomEntreprise.contains=" + UPDATED_NOM_ENTREPRISE);
    }

    @Test
    @Transactional
    void getAllEntreprisesByNomEntrepriseNotContainsSomething() throws Exception {
        // Initialize the database
        insertedEntreprise = entrepriseRepository.saveAndFlush(entreprise);

        // Get all the entrepriseList where nomEntreprise does not contain
        defaultEntrepriseFiltering(
            "nomEntreprise.doesNotContain=" + UPDATED_NOM_ENTREPRISE,
            "nomEntreprise.doesNotContain=" + DEFAULT_NOM_ENTREPRISE
        );
    }

    @Test
    @Transactional
    void getAllEntreprisesByTypeActiviteIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedEntreprise = entrepriseRepository.saveAndFlush(entreprise);

        // Get all the entrepriseList where typeActivite equals to
        defaultEntrepriseFiltering("typeActivite.equals=" + DEFAULT_TYPE_ACTIVITE, "typeActivite.equals=" + UPDATED_TYPE_ACTIVITE);
    }

    @Test
    @Transactional
    void getAllEntreprisesByTypeActiviteIsInShouldWork() throws Exception {
        // Initialize the database
        insertedEntreprise = entrepriseRepository.saveAndFlush(entreprise);

        // Get all the entrepriseList where typeActivite in
        defaultEntrepriseFiltering(
            "typeActivite.in=" + DEFAULT_TYPE_ACTIVITE + "," + UPDATED_TYPE_ACTIVITE,
            "typeActivite.in=" + UPDATED_TYPE_ACTIVITE
        );
    }

    @Test
    @Transactional
    void getAllEntreprisesByTypeActiviteIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedEntreprise = entrepriseRepository.saveAndFlush(entreprise);

        // Get all the entrepriseList where typeActivite is not null
        defaultEntrepriseFiltering("typeActivite.specified=true", "typeActivite.specified=false");
    }

    @Test
    @Transactional
    void getAllEntreprisesByTypeActiviteContainsSomething() throws Exception {
        // Initialize the database
        insertedEntreprise = entrepriseRepository.saveAndFlush(entreprise);

        // Get all the entrepriseList where typeActivite contains
        defaultEntrepriseFiltering("typeActivite.contains=" + DEFAULT_TYPE_ACTIVITE, "typeActivite.contains=" + UPDATED_TYPE_ACTIVITE);
    }

    @Test
    @Transactional
    void getAllEntreprisesByTypeActiviteNotContainsSomething() throws Exception {
        // Initialize the database
        insertedEntreprise = entrepriseRepository.saveAndFlush(entreprise);

        // Get all the entrepriseList where typeActivite does not contain
        defaultEntrepriseFiltering(
            "typeActivite.doesNotContain=" + UPDATED_TYPE_ACTIVITE,
            "typeActivite.doesNotContain=" + DEFAULT_TYPE_ACTIVITE
        );
    }

    @Test
    @Transactional
    void getAllEntreprisesByLicenceIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedEntreprise = entrepriseRepository.saveAndFlush(entreprise);

        // Get all the entrepriseList where licence equals to
        defaultEntrepriseFiltering("licence.equals=" + DEFAULT_LICENCE, "licence.equals=" + UPDATED_LICENCE);
    }

    @Test
    @Transactional
    void getAllEntreprisesByLicenceIsInShouldWork() throws Exception {
        // Initialize the database
        insertedEntreprise = entrepriseRepository.saveAndFlush(entreprise);

        // Get all the entrepriseList where licence in
        defaultEntrepriseFiltering("licence.in=" + DEFAULT_LICENCE + "," + UPDATED_LICENCE, "licence.in=" + UPDATED_LICENCE);
    }

    @Test
    @Transactional
    void getAllEntreprisesByLicenceIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedEntreprise = entrepriseRepository.saveAndFlush(entreprise);

        // Get all the entrepriseList where licence is not null
        defaultEntrepriseFiltering("licence.specified=true", "licence.specified=false");
    }

    @Test
    @Transactional
    void getAllEntreprisesByLicenceContainsSomething() throws Exception {
        // Initialize the database
        insertedEntreprise = entrepriseRepository.saveAndFlush(entreprise);

        // Get all the entrepriseList where licence contains
        defaultEntrepriseFiltering("licence.contains=" + DEFAULT_LICENCE, "licence.contains=" + UPDATED_LICENCE);
    }

    @Test
    @Transactional
    void getAllEntreprisesByLicenceNotContainsSomething() throws Exception {
        // Initialize the database
        insertedEntreprise = entrepriseRepository.saveAndFlush(entreprise);

        // Get all the entrepriseList where licence does not contain
        defaultEntrepriseFiltering("licence.doesNotContain=" + UPDATED_LICENCE, "licence.doesNotContain=" + DEFAULT_LICENCE);
    }

    @Test
    @Transactional
    void getAllEntreprisesByAdressePhysiqueIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedEntreprise = entrepriseRepository.saveAndFlush(entreprise);

        // Get all the entrepriseList where adressePhysique equals to
        defaultEntrepriseFiltering(
            "adressePhysique.equals=" + DEFAULT_ADRESSE_PHYSIQUE,
            "adressePhysique.equals=" + UPDATED_ADRESSE_PHYSIQUE
        );
    }

    @Test
    @Transactional
    void getAllEntreprisesByAdressePhysiqueIsInShouldWork() throws Exception {
        // Initialize the database
        insertedEntreprise = entrepriseRepository.saveAndFlush(entreprise);

        // Get all the entrepriseList where adressePhysique in
        defaultEntrepriseFiltering(
            "adressePhysique.in=" + DEFAULT_ADRESSE_PHYSIQUE + "," + UPDATED_ADRESSE_PHYSIQUE,
            "adressePhysique.in=" + UPDATED_ADRESSE_PHYSIQUE
        );
    }

    @Test
    @Transactional
    void getAllEntreprisesByAdressePhysiqueIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedEntreprise = entrepriseRepository.saveAndFlush(entreprise);

        // Get all the entrepriseList where adressePhysique is not null
        defaultEntrepriseFiltering("adressePhysique.specified=true", "adressePhysique.specified=false");
    }

    @Test
    @Transactional
    void getAllEntreprisesByAdressePhysiqueContainsSomething() throws Exception {
        // Initialize the database
        insertedEntreprise = entrepriseRepository.saveAndFlush(entreprise);

        // Get all the entrepriseList where adressePhysique contains
        defaultEntrepriseFiltering(
            "adressePhysique.contains=" + DEFAULT_ADRESSE_PHYSIQUE,
            "adressePhysique.contains=" + UPDATED_ADRESSE_PHYSIQUE
        );
    }

    @Test
    @Transactional
    void getAllEntreprisesByAdressePhysiqueNotContainsSomething() throws Exception {
        // Initialize the database
        insertedEntreprise = entrepriseRepository.saveAndFlush(entreprise);

        // Get all the entrepriseList where adressePhysique does not contain
        defaultEntrepriseFiltering(
            "adressePhysique.doesNotContain=" + UPDATED_ADRESSE_PHYSIQUE,
            "adressePhysique.doesNotContain=" + DEFAULT_ADRESSE_PHYSIQUE
        );
    }

    @Test
    @Transactional
    void getAllEntreprisesByUtilisateurIsEqualToSomething() throws Exception {
        Utilisateur utilisateur;
        if (TestUtil.findAll(em, Utilisateur.class).isEmpty()) {
            entrepriseRepository.saveAndFlush(entreprise);
            utilisateur = UtilisateurResourceIT.createEntity();
        } else {
            utilisateur = TestUtil.findAll(em, Utilisateur.class).get(0);
        }
        em.persist(utilisateur);
        em.flush();
        entreprise.setUtilisateur(utilisateur);
        entrepriseRepository.saveAndFlush(entreprise);
        Long utilisateurId = utilisateur.getId();
        // Get all the entrepriseList where utilisateur equals to utilisateurId
        defaultEntrepriseShouldBeFound("utilisateurId.equals=" + utilisateurId);

        // Get all the entrepriseList where utilisateur equals to (utilisateurId + 1)
        defaultEntrepriseShouldNotBeFound("utilisateurId.equals=" + (utilisateurId + 1));
    }

    private void defaultEntrepriseFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultEntrepriseShouldBeFound(shouldBeFound);
        defaultEntrepriseShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultEntrepriseShouldBeFound(String filter) throws Exception {
        restEntrepriseMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(entreprise.getId().intValue())))
            .andExpect(jsonPath("$.[*].nomEntreprise").value(hasItem(DEFAULT_NOM_ENTREPRISE)))
            .andExpect(jsonPath("$.[*].typeActivite").value(hasItem(DEFAULT_TYPE_ACTIVITE)))
            .andExpect(jsonPath("$.[*].licence").value(hasItem(DEFAULT_LICENCE)))
            .andExpect(jsonPath("$.[*].adressePhysique").value(hasItem(DEFAULT_ADRESSE_PHYSIQUE)));

        // Check, that the count call also returns 1
        restEntrepriseMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultEntrepriseShouldNotBeFound(String filter) throws Exception {
        restEntrepriseMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restEntrepriseMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingEntreprise() throws Exception {
        // Get the entreprise
        restEntrepriseMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingEntreprise() throws Exception {
        // Initialize the database
        insertedEntreprise = entrepriseRepository.saveAndFlush(entreprise);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the entreprise
        Entreprise updatedEntreprise = entrepriseRepository.findById(entreprise.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedEntreprise are not directly saved in db
        em.detach(updatedEntreprise);
        updatedEntreprise
            .nomEntreprise(UPDATED_NOM_ENTREPRISE)
            .typeActivite(UPDATED_TYPE_ACTIVITE)
            .licence(UPDATED_LICENCE)
            .adressePhysique(UPDATED_ADRESSE_PHYSIQUE);
        EntrepriseDTO entrepriseDTO = entrepriseMapper.toDto(updatedEntreprise);

        restEntrepriseMockMvc
            .perform(
                put(ENTITY_API_URL_ID, entrepriseDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(entrepriseDTO))
            )
            .andExpect(status().isOk());

        // Validate the Entreprise in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedEntrepriseToMatchAllProperties(updatedEntreprise);
    }

    @Test
    @Transactional
    void putNonExistingEntreprise() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        entreprise.setId(longCount.incrementAndGet());

        // Create the Entreprise
        EntrepriseDTO entrepriseDTO = entrepriseMapper.toDto(entreprise);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restEntrepriseMockMvc
            .perform(
                put(ENTITY_API_URL_ID, entrepriseDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(entrepriseDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Entreprise in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchEntreprise() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        entreprise.setId(longCount.incrementAndGet());

        // Create the Entreprise
        EntrepriseDTO entrepriseDTO = entrepriseMapper.toDto(entreprise);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEntrepriseMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(entrepriseDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Entreprise in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamEntreprise() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        entreprise.setId(longCount.incrementAndGet());

        // Create the Entreprise
        EntrepriseDTO entrepriseDTO = entrepriseMapper.toDto(entreprise);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEntrepriseMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(entrepriseDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Entreprise in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateEntrepriseWithPatch() throws Exception {
        // Initialize the database
        insertedEntreprise = entrepriseRepository.saveAndFlush(entreprise);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the entreprise using partial update
        Entreprise partialUpdatedEntreprise = new Entreprise();
        partialUpdatedEntreprise.setId(entreprise.getId());

        partialUpdatedEntreprise.typeActivite(UPDATED_TYPE_ACTIVITE).adressePhysique(UPDATED_ADRESSE_PHYSIQUE);

        restEntrepriseMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedEntreprise.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedEntreprise))
            )
            .andExpect(status().isOk());

        // Validate the Entreprise in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertEntrepriseUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedEntreprise, entreprise),
            getPersistedEntreprise(entreprise)
        );
    }

    @Test
    @Transactional
    void fullUpdateEntrepriseWithPatch() throws Exception {
        // Initialize the database
        insertedEntreprise = entrepriseRepository.saveAndFlush(entreprise);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the entreprise using partial update
        Entreprise partialUpdatedEntreprise = new Entreprise();
        partialUpdatedEntreprise.setId(entreprise.getId());

        partialUpdatedEntreprise
            .nomEntreprise(UPDATED_NOM_ENTREPRISE)
            .typeActivite(UPDATED_TYPE_ACTIVITE)
            .licence(UPDATED_LICENCE)
            .adressePhysique(UPDATED_ADRESSE_PHYSIQUE);

        restEntrepriseMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedEntreprise.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedEntreprise))
            )
            .andExpect(status().isOk());

        // Validate the Entreprise in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertEntrepriseUpdatableFieldsEquals(partialUpdatedEntreprise, getPersistedEntreprise(partialUpdatedEntreprise));
    }

    @Test
    @Transactional
    void patchNonExistingEntreprise() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        entreprise.setId(longCount.incrementAndGet());

        // Create the Entreprise
        EntrepriseDTO entrepriseDTO = entrepriseMapper.toDto(entreprise);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restEntrepriseMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, entrepriseDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(entrepriseDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Entreprise in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchEntreprise() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        entreprise.setId(longCount.incrementAndGet());

        // Create the Entreprise
        EntrepriseDTO entrepriseDTO = entrepriseMapper.toDto(entreprise);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEntrepriseMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(entrepriseDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Entreprise in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamEntreprise() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        entreprise.setId(longCount.incrementAndGet());

        // Create the Entreprise
        EntrepriseDTO entrepriseDTO = entrepriseMapper.toDto(entreprise);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEntrepriseMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(entrepriseDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Entreprise in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteEntreprise() throws Exception {
        // Initialize the database
        insertedEntreprise = entrepriseRepository.saveAndFlush(entreprise);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the entreprise
        restEntrepriseMockMvc
            .perform(delete(ENTITY_API_URL_ID, entreprise.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return entrepriseRepository.count();
    }

    protected void assertIncrementedRepositoryCount(long countBefore) {
        assertThat(countBefore + 1).isEqualTo(getRepositoryCount());
    }

    protected void assertDecrementedRepositoryCount(long countBefore) {
        assertThat(countBefore - 1).isEqualTo(getRepositoryCount());
    }

    protected void assertSameRepositoryCount(long countBefore) {
        assertThat(countBefore).isEqualTo(getRepositoryCount());
    }

    protected Entreprise getPersistedEntreprise(Entreprise entreprise) {
        return entrepriseRepository.findById(entreprise.getId()).orElseThrow();
    }

    protected void assertPersistedEntrepriseToMatchAllProperties(Entreprise expectedEntreprise) {
        assertEntrepriseAllPropertiesEquals(expectedEntreprise, getPersistedEntreprise(expectedEntreprise));
    }

    protected void assertPersistedEntrepriseToMatchUpdatableProperties(Entreprise expectedEntreprise) {
        assertEntrepriseAllUpdatablePropertiesEquals(expectedEntreprise, getPersistedEntreprise(expectedEntreprise));
    }
}
