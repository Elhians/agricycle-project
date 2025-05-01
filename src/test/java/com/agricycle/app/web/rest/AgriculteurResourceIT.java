package com.agricycle.app.web.rest;

import static com.agricycle.app.domain.AgriculteurAsserts.*;
import static com.agricycle.app.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.agricycle.app.IntegrationTest;
import com.agricycle.app.domain.Agriculteur;
import com.agricycle.app.domain.Utilisateur;
import com.agricycle.app.repository.AgriculteurRepository;
import com.agricycle.app.service.dto.AgriculteurDTO;
import com.agricycle.app.service.mapper.AgriculteurMapper;
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
 * Integration tests for the {@link AgriculteurResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class AgriculteurResourceIT {

    private static final String DEFAULT_TYPE_PRODUCTION = "AAAAAAAAAA";
    private static final String UPDATED_TYPE_PRODUCTION = "BBBBBBBBBB";

    private static final Integer DEFAULT_ANNEE_EXPERIENCE = 1;
    private static final Integer UPDATED_ANNEE_EXPERIENCE = 2;
    private static final Integer SMALLER_ANNEE_EXPERIENCE = 1 - 1;

    private static final String DEFAULT_LOCALISATION = "AAAAAAAAAA";
    private static final String UPDATED_LOCALISATION = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/agriculteurs";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private AgriculteurRepository agriculteurRepository;

    @Autowired
    private AgriculteurMapper agriculteurMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restAgriculteurMockMvc;

    private Agriculteur agriculteur;

    private Agriculteur insertedAgriculteur;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Agriculteur createEntity() {
        return new Agriculteur()
            .typeProduction(DEFAULT_TYPE_PRODUCTION)
            .anneeExperience(DEFAULT_ANNEE_EXPERIENCE)
            .localisation(DEFAULT_LOCALISATION);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Agriculteur createUpdatedEntity() {
        return new Agriculteur()
            .typeProduction(UPDATED_TYPE_PRODUCTION)
            .anneeExperience(UPDATED_ANNEE_EXPERIENCE)
            .localisation(UPDATED_LOCALISATION);
    }

    @BeforeEach
    void initTest() {
        agriculteur = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedAgriculteur != null) {
            agriculteurRepository.delete(insertedAgriculteur);
            insertedAgriculteur = null;
        }
    }

    @Test
    @Transactional
    void createAgriculteur() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Agriculteur
        AgriculteurDTO agriculteurDTO = agriculteurMapper.toDto(agriculteur);
        var returnedAgriculteurDTO = om.readValue(
            restAgriculteurMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(agriculteurDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            AgriculteurDTO.class
        );

        // Validate the Agriculteur in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedAgriculteur = agriculteurMapper.toEntity(returnedAgriculteurDTO);
        assertAgriculteurUpdatableFieldsEquals(returnedAgriculteur, getPersistedAgriculteur(returnedAgriculteur));

        insertedAgriculteur = returnedAgriculteur;
    }

    @Test
    @Transactional
    void createAgriculteurWithExistingId() throws Exception {
        // Create the Agriculteur with an existing ID
        agriculteur.setId(1L);
        AgriculteurDTO agriculteurDTO = agriculteurMapper.toDto(agriculteur);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restAgriculteurMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(agriculteurDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Agriculteur in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllAgriculteurs() throws Exception {
        // Initialize the database
        insertedAgriculteur = agriculteurRepository.saveAndFlush(agriculteur);

        // Get all the agriculteurList
        restAgriculteurMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(agriculteur.getId().intValue())))
            .andExpect(jsonPath("$.[*].typeProduction").value(hasItem(DEFAULT_TYPE_PRODUCTION)))
            .andExpect(jsonPath("$.[*].anneeExperience").value(hasItem(DEFAULT_ANNEE_EXPERIENCE)))
            .andExpect(jsonPath("$.[*].localisation").value(hasItem(DEFAULT_LOCALISATION)));
    }

    @Test
    @Transactional
    void getAgriculteur() throws Exception {
        // Initialize the database
        insertedAgriculteur = agriculteurRepository.saveAndFlush(agriculteur);

        // Get the agriculteur
        restAgriculteurMockMvc
            .perform(get(ENTITY_API_URL_ID, agriculteur.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(agriculteur.getId().intValue()))
            .andExpect(jsonPath("$.typeProduction").value(DEFAULT_TYPE_PRODUCTION))
            .andExpect(jsonPath("$.anneeExperience").value(DEFAULT_ANNEE_EXPERIENCE))
            .andExpect(jsonPath("$.localisation").value(DEFAULT_LOCALISATION));
    }

    @Test
    @Transactional
    void getAgriculteursByIdFiltering() throws Exception {
        // Initialize the database
        insertedAgriculteur = agriculteurRepository.saveAndFlush(agriculteur);

        Long id = agriculteur.getId();

        defaultAgriculteurFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultAgriculteurFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultAgriculteurFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllAgriculteursByTypeProductionIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedAgriculteur = agriculteurRepository.saveAndFlush(agriculteur);

        // Get all the agriculteurList where typeProduction equals to
        defaultAgriculteurFiltering("typeProduction.equals=" + DEFAULT_TYPE_PRODUCTION, "typeProduction.equals=" + UPDATED_TYPE_PRODUCTION);
    }

    @Test
    @Transactional
    void getAllAgriculteursByTypeProductionIsInShouldWork() throws Exception {
        // Initialize the database
        insertedAgriculteur = agriculteurRepository.saveAndFlush(agriculteur);

        // Get all the agriculteurList where typeProduction in
        defaultAgriculteurFiltering(
            "typeProduction.in=" + DEFAULT_TYPE_PRODUCTION + "," + UPDATED_TYPE_PRODUCTION,
            "typeProduction.in=" + UPDATED_TYPE_PRODUCTION
        );
    }

    @Test
    @Transactional
    void getAllAgriculteursByTypeProductionIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedAgriculteur = agriculteurRepository.saveAndFlush(agriculteur);

        // Get all the agriculteurList where typeProduction is not null
        defaultAgriculteurFiltering("typeProduction.specified=true", "typeProduction.specified=false");
    }

    @Test
    @Transactional
    void getAllAgriculteursByTypeProductionContainsSomething() throws Exception {
        // Initialize the database
        insertedAgriculteur = agriculteurRepository.saveAndFlush(agriculteur);

        // Get all the agriculteurList where typeProduction contains
        defaultAgriculteurFiltering(
            "typeProduction.contains=" + DEFAULT_TYPE_PRODUCTION,
            "typeProduction.contains=" + UPDATED_TYPE_PRODUCTION
        );
    }

    @Test
    @Transactional
    void getAllAgriculteursByTypeProductionNotContainsSomething() throws Exception {
        // Initialize the database
        insertedAgriculteur = agriculteurRepository.saveAndFlush(agriculteur);

        // Get all the agriculteurList where typeProduction does not contain
        defaultAgriculteurFiltering(
            "typeProduction.doesNotContain=" + UPDATED_TYPE_PRODUCTION,
            "typeProduction.doesNotContain=" + DEFAULT_TYPE_PRODUCTION
        );
    }

    @Test
    @Transactional
    void getAllAgriculteursByAnneeExperienceIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedAgriculteur = agriculteurRepository.saveAndFlush(agriculteur);

        // Get all the agriculteurList where anneeExperience equals to
        defaultAgriculteurFiltering(
            "anneeExperience.equals=" + DEFAULT_ANNEE_EXPERIENCE,
            "anneeExperience.equals=" + UPDATED_ANNEE_EXPERIENCE
        );
    }

    @Test
    @Transactional
    void getAllAgriculteursByAnneeExperienceIsInShouldWork() throws Exception {
        // Initialize the database
        insertedAgriculteur = agriculteurRepository.saveAndFlush(agriculteur);

        // Get all the agriculteurList where anneeExperience in
        defaultAgriculteurFiltering(
            "anneeExperience.in=" + DEFAULT_ANNEE_EXPERIENCE + "," + UPDATED_ANNEE_EXPERIENCE,
            "anneeExperience.in=" + UPDATED_ANNEE_EXPERIENCE
        );
    }

    @Test
    @Transactional
    void getAllAgriculteursByAnneeExperienceIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedAgriculteur = agriculteurRepository.saveAndFlush(agriculteur);

        // Get all the agriculteurList where anneeExperience is not null
        defaultAgriculteurFiltering("anneeExperience.specified=true", "anneeExperience.specified=false");
    }

    @Test
    @Transactional
    void getAllAgriculteursByAnneeExperienceIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedAgriculteur = agriculteurRepository.saveAndFlush(agriculteur);

        // Get all the agriculteurList where anneeExperience is greater than or equal to
        defaultAgriculteurFiltering(
            "anneeExperience.greaterThanOrEqual=" + DEFAULT_ANNEE_EXPERIENCE,
            "anneeExperience.greaterThanOrEqual=" + UPDATED_ANNEE_EXPERIENCE
        );
    }

    @Test
    @Transactional
    void getAllAgriculteursByAnneeExperienceIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedAgriculteur = agriculteurRepository.saveAndFlush(agriculteur);

        // Get all the agriculteurList where anneeExperience is less than or equal to
        defaultAgriculteurFiltering(
            "anneeExperience.lessThanOrEqual=" + DEFAULT_ANNEE_EXPERIENCE,
            "anneeExperience.lessThanOrEqual=" + SMALLER_ANNEE_EXPERIENCE
        );
    }

    @Test
    @Transactional
    void getAllAgriculteursByAnneeExperienceIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedAgriculteur = agriculteurRepository.saveAndFlush(agriculteur);

        // Get all the agriculteurList where anneeExperience is less than
        defaultAgriculteurFiltering(
            "anneeExperience.lessThan=" + UPDATED_ANNEE_EXPERIENCE,
            "anneeExperience.lessThan=" + DEFAULT_ANNEE_EXPERIENCE
        );
    }

    @Test
    @Transactional
    void getAllAgriculteursByAnneeExperienceIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedAgriculteur = agriculteurRepository.saveAndFlush(agriculteur);

        // Get all the agriculteurList where anneeExperience is greater than
        defaultAgriculteurFiltering(
            "anneeExperience.greaterThan=" + SMALLER_ANNEE_EXPERIENCE,
            "anneeExperience.greaterThan=" + DEFAULT_ANNEE_EXPERIENCE
        );
    }

    @Test
    @Transactional
    void getAllAgriculteursByLocalisationIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedAgriculteur = agriculteurRepository.saveAndFlush(agriculteur);

        // Get all the agriculteurList where localisation equals to
        defaultAgriculteurFiltering("localisation.equals=" + DEFAULT_LOCALISATION, "localisation.equals=" + UPDATED_LOCALISATION);
    }

    @Test
    @Transactional
    void getAllAgriculteursByLocalisationIsInShouldWork() throws Exception {
        // Initialize the database
        insertedAgriculteur = agriculteurRepository.saveAndFlush(agriculteur);

        // Get all the agriculteurList where localisation in
        defaultAgriculteurFiltering(
            "localisation.in=" + DEFAULT_LOCALISATION + "," + UPDATED_LOCALISATION,
            "localisation.in=" + UPDATED_LOCALISATION
        );
    }

    @Test
    @Transactional
    void getAllAgriculteursByLocalisationIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedAgriculteur = agriculteurRepository.saveAndFlush(agriculteur);

        // Get all the agriculteurList where localisation is not null
        defaultAgriculteurFiltering("localisation.specified=true", "localisation.specified=false");
    }

    @Test
    @Transactional
    void getAllAgriculteursByLocalisationContainsSomething() throws Exception {
        // Initialize the database
        insertedAgriculteur = agriculteurRepository.saveAndFlush(agriculteur);

        // Get all the agriculteurList where localisation contains
        defaultAgriculteurFiltering("localisation.contains=" + DEFAULT_LOCALISATION, "localisation.contains=" + UPDATED_LOCALISATION);
    }

    @Test
    @Transactional
    void getAllAgriculteursByLocalisationNotContainsSomething() throws Exception {
        // Initialize the database
        insertedAgriculteur = agriculteurRepository.saveAndFlush(agriculteur);

        // Get all the agriculteurList where localisation does not contain
        defaultAgriculteurFiltering(
            "localisation.doesNotContain=" + UPDATED_LOCALISATION,
            "localisation.doesNotContain=" + DEFAULT_LOCALISATION
        );
    }

    @Test
    @Transactional
    void getAllAgriculteursByUtilisateurIsEqualToSomething() throws Exception {
        Utilisateur utilisateur;
        if (TestUtil.findAll(em, Utilisateur.class).isEmpty()) {
            agriculteurRepository.saveAndFlush(agriculteur);
            utilisateur = UtilisateurResourceIT.createEntity();
        } else {
            utilisateur = TestUtil.findAll(em, Utilisateur.class).get(0);
        }
        em.persist(utilisateur);
        em.flush();
        agriculteur.setUtilisateur(utilisateur);
        agriculteurRepository.saveAndFlush(agriculteur);
        Long utilisateurId = utilisateur.getId();
        // Get all the agriculteurList where utilisateur equals to utilisateurId
        defaultAgriculteurShouldBeFound("utilisateurId.equals=" + utilisateurId);

        // Get all the agriculteurList where utilisateur equals to (utilisateurId + 1)
        defaultAgriculteurShouldNotBeFound("utilisateurId.equals=" + (utilisateurId + 1));
    }

    private void defaultAgriculteurFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultAgriculteurShouldBeFound(shouldBeFound);
        defaultAgriculteurShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultAgriculteurShouldBeFound(String filter) throws Exception {
        restAgriculteurMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(agriculteur.getId().intValue())))
            .andExpect(jsonPath("$.[*].typeProduction").value(hasItem(DEFAULT_TYPE_PRODUCTION)))
            .andExpect(jsonPath("$.[*].anneeExperience").value(hasItem(DEFAULT_ANNEE_EXPERIENCE)))
            .andExpect(jsonPath("$.[*].localisation").value(hasItem(DEFAULT_LOCALISATION)));

        // Check, that the count call also returns 1
        restAgriculteurMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultAgriculteurShouldNotBeFound(String filter) throws Exception {
        restAgriculteurMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restAgriculteurMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingAgriculteur() throws Exception {
        // Get the agriculteur
        restAgriculteurMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingAgriculteur() throws Exception {
        // Initialize the database
        insertedAgriculteur = agriculteurRepository.saveAndFlush(agriculteur);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the agriculteur
        Agriculteur updatedAgriculteur = agriculteurRepository.findById(agriculteur.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedAgriculteur are not directly saved in db
        em.detach(updatedAgriculteur);
        updatedAgriculteur
            .typeProduction(UPDATED_TYPE_PRODUCTION)
            .anneeExperience(UPDATED_ANNEE_EXPERIENCE)
            .localisation(UPDATED_LOCALISATION);
        AgriculteurDTO agriculteurDTO = agriculteurMapper.toDto(updatedAgriculteur);

        restAgriculteurMockMvc
            .perform(
                put(ENTITY_API_URL_ID, agriculteurDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(agriculteurDTO))
            )
            .andExpect(status().isOk());

        // Validate the Agriculteur in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedAgriculteurToMatchAllProperties(updatedAgriculteur);
    }

    @Test
    @Transactional
    void putNonExistingAgriculteur() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        agriculteur.setId(longCount.incrementAndGet());

        // Create the Agriculteur
        AgriculteurDTO agriculteurDTO = agriculteurMapper.toDto(agriculteur);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAgriculteurMockMvc
            .perform(
                put(ENTITY_API_URL_ID, agriculteurDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(agriculteurDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Agriculteur in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchAgriculteur() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        agriculteur.setId(longCount.incrementAndGet());

        // Create the Agriculteur
        AgriculteurDTO agriculteurDTO = agriculteurMapper.toDto(agriculteur);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAgriculteurMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(agriculteurDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Agriculteur in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamAgriculteur() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        agriculteur.setId(longCount.incrementAndGet());

        // Create the Agriculteur
        AgriculteurDTO agriculteurDTO = agriculteurMapper.toDto(agriculteur);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAgriculteurMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(agriculteurDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Agriculteur in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateAgriculteurWithPatch() throws Exception {
        // Initialize the database
        insertedAgriculteur = agriculteurRepository.saveAndFlush(agriculteur);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the agriculteur using partial update
        Agriculteur partialUpdatedAgriculteur = new Agriculteur();
        partialUpdatedAgriculteur.setId(agriculteur.getId());

        partialUpdatedAgriculteur.typeProduction(UPDATED_TYPE_PRODUCTION).localisation(UPDATED_LOCALISATION);

        restAgriculteurMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAgriculteur.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedAgriculteur))
            )
            .andExpect(status().isOk());

        // Validate the Agriculteur in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertAgriculteurUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedAgriculteur, agriculteur),
            getPersistedAgriculteur(agriculteur)
        );
    }

    @Test
    @Transactional
    void fullUpdateAgriculteurWithPatch() throws Exception {
        // Initialize the database
        insertedAgriculteur = agriculteurRepository.saveAndFlush(agriculteur);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the agriculteur using partial update
        Agriculteur partialUpdatedAgriculteur = new Agriculteur();
        partialUpdatedAgriculteur.setId(agriculteur.getId());

        partialUpdatedAgriculteur
            .typeProduction(UPDATED_TYPE_PRODUCTION)
            .anneeExperience(UPDATED_ANNEE_EXPERIENCE)
            .localisation(UPDATED_LOCALISATION);

        restAgriculteurMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAgriculteur.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedAgriculteur))
            )
            .andExpect(status().isOk());

        // Validate the Agriculteur in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertAgriculteurUpdatableFieldsEquals(partialUpdatedAgriculteur, getPersistedAgriculteur(partialUpdatedAgriculteur));
    }

    @Test
    @Transactional
    void patchNonExistingAgriculteur() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        agriculteur.setId(longCount.incrementAndGet());

        // Create the Agriculteur
        AgriculteurDTO agriculteurDTO = agriculteurMapper.toDto(agriculteur);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAgriculteurMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, agriculteurDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(agriculteurDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Agriculteur in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchAgriculteur() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        agriculteur.setId(longCount.incrementAndGet());

        // Create the Agriculteur
        AgriculteurDTO agriculteurDTO = agriculteurMapper.toDto(agriculteur);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAgriculteurMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(agriculteurDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Agriculteur in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamAgriculteur() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        agriculteur.setId(longCount.incrementAndGet());

        // Create the Agriculteur
        AgriculteurDTO agriculteurDTO = agriculteurMapper.toDto(agriculteur);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAgriculteurMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(agriculteurDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Agriculteur in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteAgriculteur() throws Exception {
        // Initialize the database
        insertedAgriculteur = agriculteurRepository.saveAndFlush(agriculteur);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the agriculteur
        restAgriculteurMockMvc
            .perform(delete(ENTITY_API_URL_ID, agriculteur.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return agriculteurRepository.count();
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

    protected Agriculteur getPersistedAgriculteur(Agriculteur agriculteur) {
        return agriculteurRepository.findById(agriculteur.getId()).orElseThrow();
    }

    protected void assertPersistedAgriculteurToMatchAllProperties(Agriculteur expectedAgriculteur) {
        assertAgriculteurAllPropertiesEquals(expectedAgriculteur, getPersistedAgriculteur(expectedAgriculteur));
    }

    protected void assertPersistedAgriculteurToMatchUpdatableProperties(Agriculteur expectedAgriculteur) {
        assertAgriculteurAllUpdatablePropertiesEquals(expectedAgriculteur, getPersistedAgriculteur(expectedAgriculteur));
    }
}
