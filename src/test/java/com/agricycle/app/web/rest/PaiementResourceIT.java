package com.agricycle.app.web.rest;

import static com.agricycle.app.domain.PaiementAsserts.*;
import static com.agricycle.app.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.agricycle.app.IntegrationTest;
import com.agricycle.app.domain.Paiement;
import com.agricycle.app.domain.Transaction;
import com.agricycle.app.domain.Utilisateur;
import com.agricycle.app.repository.PaiementRepository;
import com.agricycle.app.service.dto.PaiementDTO;
import com.agricycle.app.service.mapper.PaiementMapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManager;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
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
 * Integration tests for the {@link PaiementResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class PaiementResourceIT {

    private static final String DEFAULT_MOYEN_PAIEMENT = "AAAAAAAAAA";
    private static final String UPDATED_MOYEN_PAIEMENT = "BBBBBBBBBB";

    private static final String DEFAULT_PREUVE = "AAAAAAAAAA";
    private static final String UPDATED_PREUVE = "BBBBBBBBBB";

    private static final Instant DEFAULT_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/paiements";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private PaiementRepository paiementRepository;

    @Autowired
    private PaiementMapper paiementMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restPaiementMockMvc;

    private Paiement paiement;

    private Paiement insertedPaiement;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Paiement createEntity() {
        return new Paiement().moyenPaiement(DEFAULT_MOYEN_PAIEMENT).preuve(DEFAULT_PREUVE).date(DEFAULT_DATE);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Paiement createUpdatedEntity() {
        return new Paiement().moyenPaiement(UPDATED_MOYEN_PAIEMENT).preuve(UPDATED_PREUVE).date(UPDATED_DATE);
    }

    @BeforeEach
    void initTest() {
        paiement = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedPaiement != null) {
            paiementRepository.delete(insertedPaiement);
            insertedPaiement = null;
        }
    }

    @Test
    @Transactional
    void createPaiement() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Paiement
        PaiementDTO paiementDTO = paiementMapper.toDto(paiement);
        var returnedPaiementDTO = om.readValue(
            restPaiementMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(paiementDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            PaiementDTO.class
        );

        // Validate the Paiement in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedPaiement = paiementMapper.toEntity(returnedPaiementDTO);
        assertPaiementUpdatableFieldsEquals(returnedPaiement, getPersistedPaiement(returnedPaiement));

        insertedPaiement = returnedPaiement;
    }

    @Test
    @Transactional
    void createPaiementWithExistingId() throws Exception {
        // Create the Paiement with an existing ID
        paiement.setId(1L);
        PaiementDTO paiementDTO = paiementMapper.toDto(paiement);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restPaiementMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(paiementDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Paiement in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkMoyenPaiementIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        paiement.setMoyenPaiement(null);

        // Create the Paiement, which fails.
        PaiementDTO paiementDTO = paiementMapper.toDto(paiement);

        restPaiementMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(paiementDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllPaiements() throws Exception {
        // Initialize the database
        insertedPaiement = paiementRepository.saveAndFlush(paiement);

        // Get all the paiementList
        restPaiementMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(paiement.getId().intValue())))
            .andExpect(jsonPath("$.[*].moyenPaiement").value(hasItem(DEFAULT_MOYEN_PAIEMENT)))
            .andExpect(jsonPath("$.[*].preuve").value(hasItem(DEFAULT_PREUVE)))
            .andExpect(jsonPath("$.[*].date").value(hasItem(DEFAULT_DATE.toString())));
    }

    @Test
    @Transactional
    void getPaiement() throws Exception {
        // Initialize the database
        insertedPaiement = paiementRepository.saveAndFlush(paiement);

        // Get the paiement
        restPaiementMockMvc
            .perform(get(ENTITY_API_URL_ID, paiement.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(paiement.getId().intValue()))
            .andExpect(jsonPath("$.moyenPaiement").value(DEFAULT_MOYEN_PAIEMENT))
            .andExpect(jsonPath("$.preuve").value(DEFAULT_PREUVE))
            .andExpect(jsonPath("$.date").value(DEFAULT_DATE.toString()));
    }

    @Test
    @Transactional
    void getPaiementsByIdFiltering() throws Exception {
        // Initialize the database
        insertedPaiement = paiementRepository.saveAndFlush(paiement);

        Long id = paiement.getId();

        defaultPaiementFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultPaiementFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultPaiementFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllPaiementsByMoyenPaiementIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedPaiement = paiementRepository.saveAndFlush(paiement);

        // Get all the paiementList where moyenPaiement equals to
        defaultPaiementFiltering("moyenPaiement.equals=" + DEFAULT_MOYEN_PAIEMENT, "moyenPaiement.equals=" + UPDATED_MOYEN_PAIEMENT);
    }

    @Test
    @Transactional
    void getAllPaiementsByMoyenPaiementIsInShouldWork() throws Exception {
        // Initialize the database
        insertedPaiement = paiementRepository.saveAndFlush(paiement);

        // Get all the paiementList where moyenPaiement in
        defaultPaiementFiltering(
            "moyenPaiement.in=" + DEFAULT_MOYEN_PAIEMENT + "," + UPDATED_MOYEN_PAIEMENT,
            "moyenPaiement.in=" + UPDATED_MOYEN_PAIEMENT
        );
    }

    @Test
    @Transactional
    void getAllPaiementsByMoyenPaiementIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedPaiement = paiementRepository.saveAndFlush(paiement);

        // Get all the paiementList where moyenPaiement is not null
        defaultPaiementFiltering("moyenPaiement.specified=true", "moyenPaiement.specified=false");
    }

    @Test
    @Transactional
    void getAllPaiementsByMoyenPaiementContainsSomething() throws Exception {
        // Initialize the database
        insertedPaiement = paiementRepository.saveAndFlush(paiement);

        // Get all the paiementList where moyenPaiement contains
        defaultPaiementFiltering("moyenPaiement.contains=" + DEFAULT_MOYEN_PAIEMENT, "moyenPaiement.contains=" + UPDATED_MOYEN_PAIEMENT);
    }

    @Test
    @Transactional
    void getAllPaiementsByMoyenPaiementNotContainsSomething() throws Exception {
        // Initialize the database
        insertedPaiement = paiementRepository.saveAndFlush(paiement);

        // Get all the paiementList where moyenPaiement does not contain
        defaultPaiementFiltering(
            "moyenPaiement.doesNotContain=" + UPDATED_MOYEN_PAIEMENT,
            "moyenPaiement.doesNotContain=" + DEFAULT_MOYEN_PAIEMENT
        );
    }

    @Test
    @Transactional
    void getAllPaiementsByPreuveIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedPaiement = paiementRepository.saveAndFlush(paiement);

        // Get all the paiementList where preuve equals to
        defaultPaiementFiltering("preuve.equals=" + DEFAULT_PREUVE, "preuve.equals=" + UPDATED_PREUVE);
    }

    @Test
    @Transactional
    void getAllPaiementsByPreuveIsInShouldWork() throws Exception {
        // Initialize the database
        insertedPaiement = paiementRepository.saveAndFlush(paiement);

        // Get all the paiementList where preuve in
        defaultPaiementFiltering("preuve.in=" + DEFAULT_PREUVE + "," + UPDATED_PREUVE, "preuve.in=" + UPDATED_PREUVE);
    }

    @Test
    @Transactional
    void getAllPaiementsByPreuveIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedPaiement = paiementRepository.saveAndFlush(paiement);

        // Get all the paiementList where preuve is not null
        defaultPaiementFiltering("preuve.specified=true", "preuve.specified=false");
    }

    @Test
    @Transactional
    void getAllPaiementsByPreuveContainsSomething() throws Exception {
        // Initialize the database
        insertedPaiement = paiementRepository.saveAndFlush(paiement);

        // Get all the paiementList where preuve contains
        defaultPaiementFiltering("preuve.contains=" + DEFAULT_PREUVE, "preuve.contains=" + UPDATED_PREUVE);
    }

    @Test
    @Transactional
    void getAllPaiementsByPreuveNotContainsSomething() throws Exception {
        // Initialize the database
        insertedPaiement = paiementRepository.saveAndFlush(paiement);

        // Get all the paiementList where preuve does not contain
        defaultPaiementFiltering("preuve.doesNotContain=" + UPDATED_PREUVE, "preuve.doesNotContain=" + DEFAULT_PREUVE);
    }

    @Test
    @Transactional
    void getAllPaiementsByDateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedPaiement = paiementRepository.saveAndFlush(paiement);

        // Get all the paiementList where date equals to
        defaultPaiementFiltering("date.equals=" + DEFAULT_DATE, "date.equals=" + UPDATED_DATE);
    }

    @Test
    @Transactional
    void getAllPaiementsByDateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedPaiement = paiementRepository.saveAndFlush(paiement);

        // Get all the paiementList where date in
        defaultPaiementFiltering("date.in=" + DEFAULT_DATE + "," + UPDATED_DATE, "date.in=" + UPDATED_DATE);
    }

    @Test
    @Transactional
    void getAllPaiementsByDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedPaiement = paiementRepository.saveAndFlush(paiement);

        // Get all the paiementList where date is not null
        defaultPaiementFiltering("date.specified=true", "date.specified=false");
    }

    @Test
    @Transactional
    void getAllPaiementsByTransactionIsEqualToSomething() throws Exception {
        Transaction transaction;
        if (TestUtil.findAll(em, Transaction.class).isEmpty()) {
            paiementRepository.saveAndFlush(paiement);
            transaction = TransactionResourceIT.createEntity();
        } else {
            transaction = TestUtil.findAll(em, Transaction.class).get(0);
        }
        em.persist(transaction);
        em.flush();
        paiement.setTransaction(transaction);
        paiementRepository.saveAndFlush(paiement);
        Long transactionId = transaction.getId();
        // Get all the paiementList where transaction equals to transactionId
        defaultPaiementShouldBeFound("transactionId.equals=" + transactionId);

        // Get all the paiementList where transaction equals to (transactionId + 1)
        defaultPaiementShouldNotBeFound("transactionId.equals=" + (transactionId + 1));
    }

    @Test
    @Transactional
    void getAllPaiementsByAcheteurIsEqualToSomething() throws Exception {
        Utilisateur acheteur;
        if (TestUtil.findAll(em, Utilisateur.class).isEmpty()) {
            paiementRepository.saveAndFlush(paiement);
            acheteur = UtilisateurResourceIT.createEntity();
        } else {
            acheteur = TestUtil.findAll(em, Utilisateur.class).get(0);
        }
        em.persist(acheteur);
        em.flush();
        paiement.setAcheteur(acheteur);
        paiementRepository.saveAndFlush(paiement);
        Long acheteurId = acheteur.getId();
        // Get all the paiementList where acheteur equals to acheteurId
        defaultPaiementShouldBeFound("acheteurId.equals=" + acheteurId);

        // Get all the paiementList where acheteur equals to (acheteurId + 1)
        defaultPaiementShouldNotBeFound("acheteurId.equals=" + (acheteurId + 1));
    }

    private void defaultPaiementFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultPaiementShouldBeFound(shouldBeFound);
        defaultPaiementShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultPaiementShouldBeFound(String filter) throws Exception {
        restPaiementMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(paiement.getId().intValue())))
            .andExpect(jsonPath("$.[*].moyenPaiement").value(hasItem(DEFAULT_MOYEN_PAIEMENT)))
            .andExpect(jsonPath("$.[*].preuve").value(hasItem(DEFAULT_PREUVE)))
            .andExpect(jsonPath("$.[*].date").value(hasItem(DEFAULT_DATE.toString())));

        // Check, that the count call also returns 1
        restPaiementMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultPaiementShouldNotBeFound(String filter) throws Exception {
        restPaiementMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restPaiementMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingPaiement() throws Exception {
        // Get the paiement
        restPaiementMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingPaiement() throws Exception {
        // Initialize the database
        insertedPaiement = paiementRepository.saveAndFlush(paiement);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the paiement
        Paiement updatedPaiement = paiementRepository.findById(paiement.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedPaiement are not directly saved in db
        em.detach(updatedPaiement);
        updatedPaiement.moyenPaiement(UPDATED_MOYEN_PAIEMENT).preuve(UPDATED_PREUVE).date(UPDATED_DATE);
        PaiementDTO paiementDTO = paiementMapper.toDto(updatedPaiement);

        restPaiementMockMvc
            .perform(
                put(ENTITY_API_URL_ID, paiementDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(paiementDTO))
            )
            .andExpect(status().isOk());

        // Validate the Paiement in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedPaiementToMatchAllProperties(updatedPaiement);
    }

    @Test
    @Transactional
    void putNonExistingPaiement() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        paiement.setId(longCount.incrementAndGet());

        // Create the Paiement
        PaiementDTO paiementDTO = paiementMapper.toDto(paiement);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPaiementMockMvc
            .perform(
                put(ENTITY_API_URL_ID, paiementDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(paiementDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Paiement in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchPaiement() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        paiement.setId(longCount.incrementAndGet());

        // Create the Paiement
        PaiementDTO paiementDTO = paiementMapper.toDto(paiement);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPaiementMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(paiementDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Paiement in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamPaiement() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        paiement.setId(longCount.incrementAndGet());

        // Create the Paiement
        PaiementDTO paiementDTO = paiementMapper.toDto(paiement);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPaiementMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(paiementDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Paiement in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdatePaiementWithPatch() throws Exception {
        // Initialize the database
        insertedPaiement = paiementRepository.saveAndFlush(paiement);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the paiement using partial update
        Paiement partialUpdatedPaiement = new Paiement();
        partialUpdatedPaiement.setId(paiement.getId());

        partialUpdatedPaiement.date(UPDATED_DATE);

        restPaiementMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPaiement.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedPaiement))
            )
            .andExpect(status().isOk());

        // Validate the Paiement in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPaiementUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedPaiement, paiement), getPersistedPaiement(paiement));
    }

    @Test
    @Transactional
    void fullUpdatePaiementWithPatch() throws Exception {
        // Initialize the database
        insertedPaiement = paiementRepository.saveAndFlush(paiement);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the paiement using partial update
        Paiement partialUpdatedPaiement = new Paiement();
        partialUpdatedPaiement.setId(paiement.getId());

        partialUpdatedPaiement.moyenPaiement(UPDATED_MOYEN_PAIEMENT).preuve(UPDATED_PREUVE).date(UPDATED_DATE);

        restPaiementMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPaiement.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedPaiement))
            )
            .andExpect(status().isOk());

        // Validate the Paiement in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPaiementUpdatableFieldsEquals(partialUpdatedPaiement, getPersistedPaiement(partialUpdatedPaiement));
    }

    @Test
    @Transactional
    void patchNonExistingPaiement() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        paiement.setId(longCount.incrementAndGet());

        // Create the Paiement
        PaiementDTO paiementDTO = paiementMapper.toDto(paiement);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPaiementMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, paiementDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(paiementDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Paiement in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchPaiement() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        paiement.setId(longCount.incrementAndGet());

        // Create the Paiement
        PaiementDTO paiementDTO = paiementMapper.toDto(paiement);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPaiementMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(paiementDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Paiement in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamPaiement() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        paiement.setId(longCount.incrementAndGet());

        // Create the Paiement
        PaiementDTO paiementDTO = paiementMapper.toDto(paiement);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPaiementMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(paiementDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Paiement in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deletePaiement() throws Exception {
        // Initialize the database
        insertedPaiement = paiementRepository.saveAndFlush(paiement);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the paiement
        restPaiementMockMvc
            .perform(delete(ENTITY_API_URL_ID, paiement.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return paiementRepository.count();
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

    protected Paiement getPersistedPaiement(Paiement paiement) {
        return paiementRepository.findById(paiement.getId()).orElseThrow();
    }

    protected void assertPersistedPaiementToMatchAllProperties(Paiement expectedPaiement) {
        assertPaiementAllPropertiesEquals(expectedPaiement, getPersistedPaiement(expectedPaiement));
    }

    protected void assertPersistedPaiementToMatchUpdatableProperties(Paiement expectedPaiement) {
        assertPaiementAllUpdatablePropertiesEquals(expectedPaiement, getPersistedPaiement(expectedPaiement));
    }
}
