package com.agricycle.app.web.rest;

import static com.agricycle.app.domain.ConsommateurAsserts.*;
import static com.agricycle.app.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.agricycle.app.IntegrationTest;
import com.agricycle.app.domain.Consommateur;
import com.agricycle.app.domain.Utilisateur;
import com.agricycle.app.repository.ConsommateurRepository;
import com.agricycle.app.service.dto.ConsommateurDTO;
import com.agricycle.app.service.mapper.ConsommateurMapper;
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
 * Integration tests for the {@link ConsommateurResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ConsommateurResourceIT {

    private static final String DEFAULT_PREFERENCES = "AAAAAAAAAA";
    private static final String UPDATED_PREFERENCES = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/consommateurs";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private ConsommateurRepository consommateurRepository;

    @Autowired
    private ConsommateurMapper consommateurMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restConsommateurMockMvc;

    private Consommateur consommateur;

    private Consommateur insertedConsommateur;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Consommateur createEntity() {
        return new Consommateur().preferences(DEFAULT_PREFERENCES);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Consommateur createUpdatedEntity() {
        return new Consommateur().preferences(UPDATED_PREFERENCES);
    }

    @BeforeEach
    void initTest() {
        consommateur = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedConsommateur != null) {
            consommateurRepository.delete(insertedConsommateur);
            insertedConsommateur = null;
        }
    }

    @Test
    @Transactional
    void createConsommateur() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Consommateur
        ConsommateurDTO consommateurDTO = consommateurMapper.toDto(consommateur);
        var returnedConsommateurDTO = om.readValue(
            restConsommateurMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(consommateurDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            ConsommateurDTO.class
        );

        // Validate the Consommateur in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedConsommateur = consommateurMapper.toEntity(returnedConsommateurDTO);
        assertConsommateurUpdatableFieldsEquals(returnedConsommateur, getPersistedConsommateur(returnedConsommateur));

        insertedConsommateur = returnedConsommateur;
    }

    @Test
    @Transactional
    void createConsommateurWithExistingId() throws Exception {
        // Create the Consommateur with an existing ID
        consommateur.setId(1L);
        ConsommateurDTO consommateurDTO = consommateurMapper.toDto(consommateur);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restConsommateurMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(consommateurDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Consommateur in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllConsommateurs() throws Exception {
        // Initialize the database
        insertedConsommateur = consommateurRepository.saveAndFlush(consommateur);

        // Get all the consommateurList
        restConsommateurMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(consommateur.getId().intValue())))
            .andExpect(jsonPath("$.[*].preferences").value(hasItem(DEFAULT_PREFERENCES)));
    }

    @Test
    @Transactional
    void getConsommateur() throws Exception {
        // Initialize the database
        insertedConsommateur = consommateurRepository.saveAndFlush(consommateur);

        // Get the consommateur
        restConsommateurMockMvc
            .perform(get(ENTITY_API_URL_ID, consommateur.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(consommateur.getId().intValue()))
            .andExpect(jsonPath("$.preferences").value(DEFAULT_PREFERENCES));
    }

    @Test
    @Transactional
    void getConsommateursByIdFiltering() throws Exception {
        // Initialize the database
        insertedConsommateur = consommateurRepository.saveAndFlush(consommateur);

        Long id = consommateur.getId();

        defaultConsommateurFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultConsommateurFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultConsommateurFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllConsommateursByPreferencesIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedConsommateur = consommateurRepository.saveAndFlush(consommateur);

        // Get all the consommateurList where preferences equals to
        defaultConsommateurFiltering("preferences.equals=" + DEFAULT_PREFERENCES, "preferences.equals=" + UPDATED_PREFERENCES);
    }

    @Test
    @Transactional
    void getAllConsommateursByPreferencesIsInShouldWork() throws Exception {
        // Initialize the database
        insertedConsommateur = consommateurRepository.saveAndFlush(consommateur);

        // Get all the consommateurList where preferences in
        defaultConsommateurFiltering(
            "preferences.in=" + DEFAULT_PREFERENCES + "," + UPDATED_PREFERENCES,
            "preferences.in=" + UPDATED_PREFERENCES
        );
    }

    @Test
    @Transactional
    void getAllConsommateursByPreferencesIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedConsommateur = consommateurRepository.saveAndFlush(consommateur);

        // Get all the consommateurList where preferences is not null
        defaultConsommateurFiltering("preferences.specified=true", "preferences.specified=false");
    }

    @Test
    @Transactional
    void getAllConsommateursByPreferencesContainsSomething() throws Exception {
        // Initialize the database
        insertedConsommateur = consommateurRepository.saveAndFlush(consommateur);

        // Get all the consommateurList where preferences contains
        defaultConsommateurFiltering("preferences.contains=" + DEFAULT_PREFERENCES, "preferences.contains=" + UPDATED_PREFERENCES);
    }

    @Test
    @Transactional
    void getAllConsommateursByPreferencesNotContainsSomething() throws Exception {
        // Initialize the database
        insertedConsommateur = consommateurRepository.saveAndFlush(consommateur);

        // Get all the consommateurList where preferences does not contain
        defaultConsommateurFiltering(
            "preferences.doesNotContain=" + UPDATED_PREFERENCES,
            "preferences.doesNotContain=" + DEFAULT_PREFERENCES
        );
    }

    @Test
    @Transactional
    void getAllConsommateursByUtilisateurIsEqualToSomething() throws Exception {
        Utilisateur utilisateur;
        if (TestUtil.findAll(em, Utilisateur.class).isEmpty()) {
            consommateurRepository.saveAndFlush(consommateur);
            utilisateur = UtilisateurResourceIT.createEntity();
        } else {
            utilisateur = TestUtil.findAll(em, Utilisateur.class).get(0);
        }
        em.persist(utilisateur);
        em.flush();
        consommateur.setUtilisateur(utilisateur);
        consommateurRepository.saveAndFlush(consommateur);
        Long utilisateurId = utilisateur.getId();
        // Get all the consommateurList where utilisateur equals to utilisateurId
        defaultConsommateurShouldBeFound("utilisateurId.equals=" + utilisateurId);

        // Get all the consommateurList where utilisateur equals to (utilisateurId + 1)
        defaultConsommateurShouldNotBeFound("utilisateurId.equals=" + (utilisateurId + 1));
    }

    private void defaultConsommateurFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultConsommateurShouldBeFound(shouldBeFound);
        defaultConsommateurShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultConsommateurShouldBeFound(String filter) throws Exception {
        restConsommateurMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(consommateur.getId().intValue())))
            .andExpect(jsonPath("$.[*].preferences").value(hasItem(DEFAULT_PREFERENCES)));

        // Check, that the count call also returns 1
        restConsommateurMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultConsommateurShouldNotBeFound(String filter) throws Exception {
        restConsommateurMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restConsommateurMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingConsommateur() throws Exception {
        // Get the consommateur
        restConsommateurMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingConsommateur() throws Exception {
        // Initialize the database
        insertedConsommateur = consommateurRepository.saveAndFlush(consommateur);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the consommateur
        Consommateur updatedConsommateur = consommateurRepository.findById(consommateur.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedConsommateur are not directly saved in db
        em.detach(updatedConsommateur);
        updatedConsommateur.preferences(UPDATED_PREFERENCES);
        ConsommateurDTO consommateurDTO = consommateurMapper.toDto(updatedConsommateur);

        restConsommateurMockMvc
            .perform(
                put(ENTITY_API_URL_ID, consommateurDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(consommateurDTO))
            )
            .andExpect(status().isOk());

        // Validate the Consommateur in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedConsommateurToMatchAllProperties(updatedConsommateur);
    }

    @Test
    @Transactional
    void putNonExistingConsommateur() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        consommateur.setId(longCount.incrementAndGet());

        // Create the Consommateur
        ConsommateurDTO consommateurDTO = consommateurMapper.toDto(consommateur);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restConsommateurMockMvc
            .perform(
                put(ENTITY_API_URL_ID, consommateurDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(consommateurDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Consommateur in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchConsommateur() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        consommateur.setId(longCount.incrementAndGet());

        // Create the Consommateur
        ConsommateurDTO consommateurDTO = consommateurMapper.toDto(consommateur);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restConsommateurMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(consommateurDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Consommateur in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamConsommateur() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        consommateur.setId(longCount.incrementAndGet());

        // Create the Consommateur
        ConsommateurDTO consommateurDTO = consommateurMapper.toDto(consommateur);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restConsommateurMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(consommateurDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Consommateur in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateConsommateurWithPatch() throws Exception {
        // Initialize the database
        insertedConsommateur = consommateurRepository.saveAndFlush(consommateur);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the consommateur using partial update
        Consommateur partialUpdatedConsommateur = new Consommateur();
        partialUpdatedConsommateur.setId(consommateur.getId());

        partialUpdatedConsommateur.preferences(UPDATED_PREFERENCES);

        restConsommateurMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedConsommateur.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedConsommateur))
            )
            .andExpect(status().isOk());

        // Validate the Consommateur in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertConsommateurUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedConsommateur, consommateur),
            getPersistedConsommateur(consommateur)
        );
    }

    @Test
    @Transactional
    void fullUpdateConsommateurWithPatch() throws Exception {
        // Initialize the database
        insertedConsommateur = consommateurRepository.saveAndFlush(consommateur);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the consommateur using partial update
        Consommateur partialUpdatedConsommateur = new Consommateur();
        partialUpdatedConsommateur.setId(consommateur.getId());

        partialUpdatedConsommateur.preferences(UPDATED_PREFERENCES);

        restConsommateurMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedConsommateur.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedConsommateur))
            )
            .andExpect(status().isOk());

        // Validate the Consommateur in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertConsommateurUpdatableFieldsEquals(partialUpdatedConsommateur, getPersistedConsommateur(partialUpdatedConsommateur));
    }

    @Test
    @Transactional
    void patchNonExistingConsommateur() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        consommateur.setId(longCount.incrementAndGet());

        // Create the Consommateur
        ConsommateurDTO consommateurDTO = consommateurMapper.toDto(consommateur);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restConsommateurMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, consommateurDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(consommateurDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Consommateur in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchConsommateur() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        consommateur.setId(longCount.incrementAndGet());

        // Create the Consommateur
        ConsommateurDTO consommateurDTO = consommateurMapper.toDto(consommateur);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restConsommateurMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(consommateurDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Consommateur in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamConsommateur() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        consommateur.setId(longCount.incrementAndGet());

        // Create the Consommateur
        ConsommateurDTO consommateurDTO = consommateurMapper.toDto(consommateur);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restConsommateurMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(consommateurDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Consommateur in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteConsommateur() throws Exception {
        // Initialize the database
        insertedConsommateur = consommateurRepository.saveAndFlush(consommateur);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the consommateur
        restConsommateurMockMvc
            .perform(delete(ENTITY_API_URL_ID, consommateur.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return consommateurRepository.count();
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

    protected Consommateur getPersistedConsommateur(Consommateur consommateur) {
        return consommateurRepository.findById(consommateur.getId()).orElseThrow();
    }

    protected void assertPersistedConsommateurToMatchAllProperties(Consommateur expectedConsommateur) {
        assertConsommateurAllPropertiesEquals(expectedConsommateur, getPersistedConsommateur(expectedConsommateur));
    }

    protected void assertPersistedConsommateurToMatchUpdatableProperties(Consommateur expectedConsommateur) {
        assertConsommateurAllUpdatablePropertiesEquals(expectedConsommateur, getPersistedConsommateur(expectedConsommateur));
    }
}
