package com.agricycle.app.web.rest;

import static com.agricycle.app.domain.SignalementAsserts.*;
import static com.agricycle.app.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.agricycle.app.IntegrationTest;
import com.agricycle.app.domain.Post;
import com.agricycle.app.domain.Signalement;
import com.agricycle.app.domain.Utilisateur;
import com.agricycle.app.domain.enumeration.Cible;
import com.agricycle.app.repository.SignalementRepository;
import com.agricycle.app.service.dto.SignalementDTO;
import com.agricycle.app.service.mapper.SignalementMapper;
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
 * Integration tests for the {@link SignalementResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class SignalementResourceIT {

    private static final String DEFAULT_RAISON = "AAAAAAAAAA";
    private static final String UPDATED_RAISON = "BBBBBBBBBB";

    private static final Cible DEFAULT_CIBLE = Cible.POST;
    private static final Cible UPDATED_CIBLE = Cible.USER;

    private static final Instant DEFAULT_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/signalements";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private SignalementRepository signalementRepository;

    @Autowired
    private SignalementMapper signalementMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restSignalementMockMvc;

    private Signalement signalement;

    private Signalement insertedSignalement;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Signalement createEntity() {
        return new Signalement().raison(DEFAULT_RAISON).cible(DEFAULT_CIBLE).date(DEFAULT_DATE);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Signalement createUpdatedEntity() {
        return new Signalement().raison(UPDATED_RAISON).cible(UPDATED_CIBLE).date(UPDATED_DATE);
    }

    @BeforeEach
    void initTest() {
        signalement = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedSignalement != null) {
            signalementRepository.delete(insertedSignalement);
            insertedSignalement = null;
        }
    }

    @Test
    @Transactional
    void createSignalement() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Signalement
        SignalementDTO signalementDTO = signalementMapper.toDto(signalement);
        var returnedSignalementDTO = om.readValue(
            restSignalementMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(signalementDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            SignalementDTO.class
        );

        // Validate the Signalement in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedSignalement = signalementMapper.toEntity(returnedSignalementDTO);
        assertSignalementUpdatableFieldsEquals(returnedSignalement, getPersistedSignalement(returnedSignalement));

        insertedSignalement = returnedSignalement;
    }

    @Test
    @Transactional
    void createSignalementWithExistingId() throws Exception {
        // Create the Signalement with an existing ID
        signalement.setId(1L);
        SignalementDTO signalementDTO = signalementMapper.toDto(signalement);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restSignalementMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(signalementDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Signalement in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkRaisonIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        signalement.setRaison(null);

        // Create the Signalement, which fails.
        SignalementDTO signalementDTO = signalementMapper.toDto(signalement);

        restSignalementMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(signalementDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCibleIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        signalement.setCible(null);

        // Create the Signalement, which fails.
        SignalementDTO signalementDTO = signalementMapper.toDto(signalement);

        restSignalementMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(signalementDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllSignalements() throws Exception {
        // Initialize the database
        insertedSignalement = signalementRepository.saveAndFlush(signalement);

        // Get all the signalementList
        restSignalementMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(signalement.getId().intValue())))
            .andExpect(jsonPath("$.[*].raison").value(hasItem(DEFAULT_RAISON)))
            .andExpect(jsonPath("$.[*].cible").value(hasItem(DEFAULT_CIBLE.toString())))
            .andExpect(jsonPath("$.[*].date").value(hasItem(DEFAULT_DATE.toString())));
    }

    @Test
    @Transactional
    void getSignalement() throws Exception {
        // Initialize the database
        insertedSignalement = signalementRepository.saveAndFlush(signalement);

        // Get the signalement
        restSignalementMockMvc
            .perform(get(ENTITY_API_URL_ID, signalement.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(signalement.getId().intValue()))
            .andExpect(jsonPath("$.raison").value(DEFAULT_RAISON))
            .andExpect(jsonPath("$.cible").value(DEFAULT_CIBLE.toString()))
            .andExpect(jsonPath("$.date").value(DEFAULT_DATE.toString()));
    }

    @Test
    @Transactional
    void getSignalementsByIdFiltering() throws Exception {
        // Initialize the database
        insertedSignalement = signalementRepository.saveAndFlush(signalement);

        Long id = signalement.getId();

        defaultSignalementFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultSignalementFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultSignalementFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllSignalementsByRaisonIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedSignalement = signalementRepository.saveAndFlush(signalement);

        // Get all the signalementList where raison equals to
        defaultSignalementFiltering("raison.equals=" + DEFAULT_RAISON, "raison.equals=" + UPDATED_RAISON);
    }

    @Test
    @Transactional
    void getAllSignalementsByRaisonIsInShouldWork() throws Exception {
        // Initialize the database
        insertedSignalement = signalementRepository.saveAndFlush(signalement);

        // Get all the signalementList where raison in
        defaultSignalementFiltering("raison.in=" + DEFAULT_RAISON + "," + UPDATED_RAISON, "raison.in=" + UPDATED_RAISON);
    }

    @Test
    @Transactional
    void getAllSignalementsByRaisonIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedSignalement = signalementRepository.saveAndFlush(signalement);

        // Get all the signalementList where raison is not null
        defaultSignalementFiltering("raison.specified=true", "raison.specified=false");
    }

    @Test
    @Transactional
    void getAllSignalementsByRaisonContainsSomething() throws Exception {
        // Initialize the database
        insertedSignalement = signalementRepository.saveAndFlush(signalement);

        // Get all the signalementList where raison contains
        defaultSignalementFiltering("raison.contains=" + DEFAULT_RAISON, "raison.contains=" + UPDATED_RAISON);
    }

    @Test
    @Transactional
    void getAllSignalementsByRaisonNotContainsSomething() throws Exception {
        // Initialize the database
        insertedSignalement = signalementRepository.saveAndFlush(signalement);

        // Get all the signalementList where raison does not contain
        defaultSignalementFiltering("raison.doesNotContain=" + UPDATED_RAISON, "raison.doesNotContain=" + DEFAULT_RAISON);
    }

    @Test
    @Transactional
    void getAllSignalementsByCibleIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedSignalement = signalementRepository.saveAndFlush(signalement);

        // Get all the signalementList where cible equals to
        defaultSignalementFiltering("cible.equals=" + DEFAULT_CIBLE, "cible.equals=" + UPDATED_CIBLE);
    }

    @Test
    @Transactional
    void getAllSignalementsByCibleIsInShouldWork() throws Exception {
        // Initialize the database
        insertedSignalement = signalementRepository.saveAndFlush(signalement);

        // Get all the signalementList where cible in
        defaultSignalementFiltering("cible.in=" + DEFAULT_CIBLE + "," + UPDATED_CIBLE, "cible.in=" + UPDATED_CIBLE);
    }

    @Test
    @Transactional
    void getAllSignalementsByCibleIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedSignalement = signalementRepository.saveAndFlush(signalement);

        // Get all the signalementList where cible is not null
        defaultSignalementFiltering("cible.specified=true", "cible.specified=false");
    }

    @Test
    @Transactional
    void getAllSignalementsByDateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedSignalement = signalementRepository.saveAndFlush(signalement);

        // Get all the signalementList where date equals to
        defaultSignalementFiltering("date.equals=" + DEFAULT_DATE, "date.equals=" + UPDATED_DATE);
    }

    @Test
    @Transactional
    void getAllSignalementsByDateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedSignalement = signalementRepository.saveAndFlush(signalement);

        // Get all the signalementList where date in
        defaultSignalementFiltering("date.in=" + DEFAULT_DATE + "," + UPDATED_DATE, "date.in=" + UPDATED_DATE);
    }

    @Test
    @Transactional
    void getAllSignalementsByDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedSignalement = signalementRepository.saveAndFlush(signalement);

        // Get all the signalementList where date is not null
        defaultSignalementFiltering("date.specified=true", "date.specified=false");
    }

    @Test
    @Transactional
    void getAllSignalementsByAuteurIsEqualToSomething() throws Exception {
        Utilisateur auteur;
        if (TestUtil.findAll(em, Utilisateur.class).isEmpty()) {
            signalementRepository.saveAndFlush(signalement);
            auteur = UtilisateurResourceIT.createEntity();
        } else {
            auteur = TestUtil.findAll(em, Utilisateur.class).get(0);
        }
        em.persist(auteur);
        em.flush();
        signalement.setAuteur(auteur);
        signalementRepository.saveAndFlush(signalement);
        Long auteurId = auteur.getId();
        // Get all the signalementList where auteur equals to auteurId
        defaultSignalementShouldBeFound("auteurId.equals=" + auteurId);

        // Get all the signalementList where auteur equals to (auteurId + 1)
        defaultSignalementShouldNotBeFound("auteurId.equals=" + (auteurId + 1));
    }

    @Test
    @Transactional
    void getAllSignalementsByCiblePostIsEqualToSomething() throws Exception {
        Post ciblePost;
        if (TestUtil.findAll(em, Post.class).isEmpty()) {
            signalementRepository.saveAndFlush(signalement);
            ciblePost = PostResourceIT.createEntity();
        } else {
            ciblePost = TestUtil.findAll(em, Post.class).get(0);
        }
        em.persist(ciblePost);
        em.flush();
        signalement.setCiblePost(ciblePost);
        signalementRepository.saveAndFlush(signalement);
        Long ciblePostId = ciblePost.getId();
        // Get all the signalementList where ciblePost equals to ciblePostId
        defaultSignalementShouldBeFound("ciblePostId.equals=" + ciblePostId);

        // Get all the signalementList where ciblePost equals to (ciblePostId + 1)
        defaultSignalementShouldNotBeFound("ciblePostId.equals=" + (ciblePostId + 1));
    }

    private void defaultSignalementFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultSignalementShouldBeFound(shouldBeFound);
        defaultSignalementShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultSignalementShouldBeFound(String filter) throws Exception {
        restSignalementMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(signalement.getId().intValue())))
            .andExpect(jsonPath("$.[*].raison").value(hasItem(DEFAULT_RAISON)))
            .andExpect(jsonPath("$.[*].cible").value(hasItem(DEFAULT_CIBLE.toString())))
            .andExpect(jsonPath("$.[*].date").value(hasItem(DEFAULT_DATE.toString())));

        // Check, that the count call also returns 1
        restSignalementMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultSignalementShouldNotBeFound(String filter) throws Exception {
        restSignalementMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restSignalementMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingSignalement() throws Exception {
        // Get the signalement
        restSignalementMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingSignalement() throws Exception {
        // Initialize the database
        insertedSignalement = signalementRepository.saveAndFlush(signalement);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the signalement
        Signalement updatedSignalement = signalementRepository.findById(signalement.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedSignalement are not directly saved in db
        em.detach(updatedSignalement);
        updatedSignalement.raison(UPDATED_RAISON).cible(UPDATED_CIBLE).date(UPDATED_DATE);
        SignalementDTO signalementDTO = signalementMapper.toDto(updatedSignalement);

        restSignalementMockMvc
            .perform(
                put(ENTITY_API_URL_ID, signalementDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(signalementDTO))
            )
            .andExpect(status().isOk());

        // Validate the Signalement in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedSignalementToMatchAllProperties(updatedSignalement);
    }

    @Test
    @Transactional
    void putNonExistingSignalement() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        signalement.setId(longCount.incrementAndGet());

        // Create the Signalement
        SignalementDTO signalementDTO = signalementMapper.toDto(signalement);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSignalementMockMvc
            .perform(
                put(ENTITY_API_URL_ID, signalementDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(signalementDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Signalement in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchSignalement() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        signalement.setId(longCount.incrementAndGet());

        // Create the Signalement
        SignalementDTO signalementDTO = signalementMapper.toDto(signalement);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSignalementMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(signalementDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Signalement in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamSignalement() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        signalement.setId(longCount.incrementAndGet());

        // Create the Signalement
        SignalementDTO signalementDTO = signalementMapper.toDto(signalement);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSignalementMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(signalementDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Signalement in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateSignalementWithPatch() throws Exception {
        // Initialize the database
        insertedSignalement = signalementRepository.saveAndFlush(signalement);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the signalement using partial update
        Signalement partialUpdatedSignalement = new Signalement();
        partialUpdatedSignalement.setId(signalement.getId());

        partialUpdatedSignalement.date(UPDATED_DATE);

        restSignalementMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSignalement.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedSignalement))
            )
            .andExpect(status().isOk());

        // Validate the Signalement in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertSignalementUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedSignalement, signalement),
            getPersistedSignalement(signalement)
        );
    }

    @Test
    @Transactional
    void fullUpdateSignalementWithPatch() throws Exception {
        // Initialize the database
        insertedSignalement = signalementRepository.saveAndFlush(signalement);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the signalement using partial update
        Signalement partialUpdatedSignalement = new Signalement();
        partialUpdatedSignalement.setId(signalement.getId());

        partialUpdatedSignalement.raison(UPDATED_RAISON).cible(UPDATED_CIBLE).date(UPDATED_DATE);

        restSignalementMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSignalement.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedSignalement))
            )
            .andExpect(status().isOk());

        // Validate the Signalement in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertSignalementUpdatableFieldsEquals(partialUpdatedSignalement, getPersistedSignalement(partialUpdatedSignalement));
    }

    @Test
    @Transactional
    void patchNonExistingSignalement() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        signalement.setId(longCount.incrementAndGet());

        // Create the Signalement
        SignalementDTO signalementDTO = signalementMapper.toDto(signalement);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSignalementMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, signalementDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(signalementDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Signalement in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchSignalement() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        signalement.setId(longCount.incrementAndGet());

        // Create the Signalement
        SignalementDTO signalementDTO = signalementMapper.toDto(signalement);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSignalementMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(signalementDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Signalement in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamSignalement() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        signalement.setId(longCount.incrementAndGet());

        // Create the Signalement
        SignalementDTO signalementDTO = signalementMapper.toDto(signalement);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSignalementMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(signalementDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Signalement in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteSignalement() throws Exception {
        // Initialize the database
        insertedSignalement = signalementRepository.saveAndFlush(signalement);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the signalement
        restSignalementMockMvc
            .perform(delete(ENTITY_API_URL_ID, signalement.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return signalementRepository.count();
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

    protected Signalement getPersistedSignalement(Signalement signalement) {
        return signalementRepository.findById(signalement.getId()).orElseThrow();
    }

    protected void assertPersistedSignalementToMatchAllProperties(Signalement expectedSignalement) {
        assertSignalementAllPropertiesEquals(expectedSignalement, getPersistedSignalement(expectedSignalement));
    }

    protected void assertPersistedSignalementToMatchUpdatableProperties(Signalement expectedSignalement) {
        assertSignalementAllUpdatablePropertiesEquals(expectedSignalement, getPersistedSignalement(expectedSignalement));
    }
}
