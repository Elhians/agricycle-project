package com.agricycle.app.web.rest;

import static com.agricycle.app.domain.ReactionAsserts.*;
import static com.agricycle.app.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.agricycle.app.IntegrationTest;
import com.agricycle.app.domain.Post;
import com.agricycle.app.domain.Reaction;
import com.agricycle.app.domain.Utilisateur;
import com.agricycle.app.domain.enumeration.Cible;
import com.agricycle.app.domain.enumeration.TypeReaction;
import com.agricycle.app.repository.ReactionRepository;
import com.agricycle.app.service.dto.ReactionDTO;
import com.agricycle.app.service.mapper.ReactionMapper;
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
 * Integration tests for the {@link ReactionResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ReactionResourceIT {

    private static final TypeReaction DEFAULT_TYPE = TypeReaction.LIKE;
    private static final TypeReaction UPDATED_TYPE = TypeReaction.PARTAGE;

    private static final Instant DEFAULT_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Cible DEFAULT_CIBLE = Cible.POST;
    private static final Cible UPDATED_CIBLE = Cible.USER;

    private static final String ENTITY_API_URL = "/api/reactions";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private ReactionRepository reactionRepository;

    @Autowired
    private ReactionMapper reactionMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restReactionMockMvc;

    private Reaction reaction;

    private Reaction insertedReaction;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Reaction createEntity() {
        return new Reaction().type(DEFAULT_TYPE).date(DEFAULT_DATE).cible(DEFAULT_CIBLE);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Reaction createUpdatedEntity() {
        return new Reaction().type(UPDATED_TYPE).date(UPDATED_DATE).cible(UPDATED_CIBLE);
    }

    @BeforeEach
    void initTest() {
        reaction = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedReaction != null) {
            reactionRepository.delete(insertedReaction);
            insertedReaction = null;
        }
    }

    @Test
    @Transactional
    void createReaction() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Reaction
        ReactionDTO reactionDTO = reactionMapper.toDto(reaction);
        var returnedReactionDTO = om.readValue(
            restReactionMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(reactionDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            ReactionDTO.class
        );

        // Validate the Reaction in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedReaction = reactionMapper.toEntity(returnedReactionDTO);
        assertReactionUpdatableFieldsEquals(returnedReaction, getPersistedReaction(returnedReaction));

        insertedReaction = returnedReaction;
    }

    @Test
    @Transactional
    void createReactionWithExistingId() throws Exception {
        // Create the Reaction with an existing ID
        reaction.setId(1L);
        ReactionDTO reactionDTO = reactionMapper.toDto(reaction);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restReactionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(reactionDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Reaction in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkTypeIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        reaction.setType(null);

        // Create the Reaction, which fails.
        ReactionDTO reactionDTO = reactionMapper.toDto(reaction);

        restReactionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(reactionDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCibleIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        reaction.setCible(null);

        // Create the Reaction, which fails.
        ReactionDTO reactionDTO = reactionMapper.toDto(reaction);

        restReactionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(reactionDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllReactions() throws Exception {
        // Initialize the database
        insertedReaction = reactionRepository.saveAndFlush(reaction);

        // Get all the reactionList
        restReactionMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(reaction.getId().intValue())))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE.toString())))
            .andExpect(jsonPath("$.[*].date").value(hasItem(DEFAULT_DATE.toString())))
            .andExpect(jsonPath("$.[*].cible").value(hasItem(DEFAULT_CIBLE.toString())));
    }

    @Test
    @Transactional
    void getReaction() throws Exception {
        // Initialize the database
        insertedReaction = reactionRepository.saveAndFlush(reaction);

        // Get the reaction
        restReactionMockMvc
            .perform(get(ENTITY_API_URL_ID, reaction.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(reaction.getId().intValue()))
            .andExpect(jsonPath("$.type").value(DEFAULT_TYPE.toString()))
            .andExpect(jsonPath("$.date").value(DEFAULT_DATE.toString()))
            .andExpect(jsonPath("$.cible").value(DEFAULT_CIBLE.toString()));
    }

    @Test
    @Transactional
    void getReactionsByIdFiltering() throws Exception {
        // Initialize the database
        insertedReaction = reactionRepository.saveAndFlush(reaction);

        Long id = reaction.getId();

        defaultReactionFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultReactionFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultReactionFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllReactionsByTypeIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedReaction = reactionRepository.saveAndFlush(reaction);

        // Get all the reactionList where type equals to
        defaultReactionFiltering("type.equals=" + DEFAULT_TYPE, "type.equals=" + UPDATED_TYPE);
    }

    @Test
    @Transactional
    void getAllReactionsByTypeIsInShouldWork() throws Exception {
        // Initialize the database
        insertedReaction = reactionRepository.saveAndFlush(reaction);

        // Get all the reactionList where type in
        defaultReactionFiltering("type.in=" + DEFAULT_TYPE + "," + UPDATED_TYPE, "type.in=" + UPDATED_TYPE);
    }

    @Test
    @Transactional
    void getAllReactionsByTypeIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedReaction = reactionRepository.saveAndFlush(reaction);

        // Get all the reactionList where type is not null
        defaultReactionFiltering("type.specified=true", "type.specified=false");
    }

    @Test
    @Transactional
    void getAllReactionsByDateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedReaction = reactionRepository.saveAndFlush(reaction);

        // Get all the reactionList where date equals to
        defaultReactionFiltering("date.equals=" + DEFAULT_DATE, "date.equals=" + UPDATED_DATE);
    }

    @Test
    @Transactional
    void getAllReactionsByDateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedReaction = reactionRepository.saveAndFlush(reaction);

        // Get all the reactionList where date in
        defaultReactionFiltering("date.in=" + DEFAULT_DATE + "," + UPDATED_DATE, "date.in=" + UPDATED_DATE);
    }

    @Test
    @Transactional
    void getAllReactionsByDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedReaction = reactionRepository.saveAndFlush(reaction);

        // Get all the reactionList where date is not null
        defaultReactionFiltering("date.specified=true", "date.specified=false");
    }

    @Test
    @Transactional
    void getAllReactionsByCibleIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedReaction = reactionRepository.saveAndFlush(reaction);

        // Get all the reactionList where cible equals to
        defaultReactionFiltering("cible.equals=" + DEFAULT_CIBLE, "cible.equals=" + UPDATED_CIBLE);
    }

    @Test
    @Transactional
    void getAllReactionsByCibleIsInShouldWork() throws Exception {
        // Initialize the database
        insertedReaction = reactionRepository.saveAndFlush(reaction);

        // Get all the reactionList where cible in
        defaultReactionFiltering("cible.in=" + DEFAULT_CIBLE + "," + UPDATED_CIBLE, "cible.in=" + UPDATED_CIBLE);
    }

    @Test
    @Transactional
    void getAllReactionsByCibleIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedReaction = reactionRepository.saveAndFlush(reaction);

        // Get all the reactionList where cible is not null
        defaultReactionFiltering("cible.specified=true", "cible.specified=false");
    }

    @Test
    @Transactional
    void getAllReactionsByUtilisateurIsEqualToSomething() throws Exception {
        Utilisateur utilisateur;
        if (TestUtil.findAll(em, Utilisateur.class).isEmpty()) {
            reactionRepository.saveAndFlush(reaction);
            utilisateur = UtilisateurResourceIT.createEntity();
        } else {
            utilisateur = TestUtil.findAll(em, Utilisateur.class).get(0);
        }
        em.persist(utilisateur);
        em.flush();
        reaction.setUtilisateur(utilisateur);
        reactionRepository.saveAndFlush(reaction);
        Long utilisateurId = utilisateur.getId();
        // Get all the reactionList where utilisateur equals to utilisateurId
        defaultReactionShouldBeFound("utilisateurId.equals=" + utilisateurId);

        // Get all the reactionList where utilisateur equals to (utilisateurId + 1)
        defaultReactionShouldNotBeFound("utilisateurId.equals=" + (utilisateurId + 1));
    }

    @Test
    @Transactional
    void getAllReactionsByPostIsEqualToSomething() throws Exception {
        Post post;
        if (TestUtil.findAll(em, Post.class).isEmpty()) {
            reactionRepository.saveAndFlush(reaction);
            post = PostResourceIT.createEntity();
        } else {
            post = TestUtil.findAll(em, Post.class).get(0);
        }
        em.persist(post);
        em.flush();
        reaction.setPost(post);
        reactionRepository.saveAndFlush(reaction);
        Long postId = post.getId();
        // Get all the reactionList where post equals to postId
        defaultReactionShouldBeFound("postId.equals=" + postId);

        // Get all the reactionList where post equals to (postId + 1)
        defaultReactionShouldNotBeFound("postId.equals=" + (postId + 1));
    }

    private void defaultReactionFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultReactionShouldBeFound(shouldBeFound);
        defaultReactionShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultReactionShouldBeFound(String filter) throws Exception {
        restReactionMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(reaction.getId().intValue())))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE.toString())))
            .andExpect(jsonPath("$.[*].date").value(hasItem(DEFAULT_DATE.toString())))
            .andExpect(jsonPath("$.[*].cible").value(hasItem(DEFAULT_CIBLE.toString())));

        // Check, that the count call also returns 1
        restReactionMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultReactionShouldNotBeFound(String filter) throws Exception {
        restReactionMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restReactionMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingReaction() throws Exception {
        // Get the reaction
        restReactionMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingReaction() throws Exception {
        // Initialize the database
        insertedReaction = reactionRepository.saveAndFlush(reaction);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the reaction
        Reaction updatedReaction = reactionRepository.findById(reaction.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedReaction are not directly saved in db
        em.detach(updatedReaction);
        updatedReaction.type(UPDATED_TYPE).date(UPDATED_DATE).cible(UPDATED_CIBLE);
        ReactionDTO reactionDTO = reactionMapper.toDto(updatedReaction);

        restReactionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, reactionDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(reactionDTO))
            )
            .andExpect(status().isOk());

        // Validate the Reaction in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedReactionToMatchAllProperties(updatedReaction);
    }

    @Test
    @Transactional
    void putNonExistingReaction() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        reaction.setId(longCount.incrementAndGet());

        // Create the Reaction
        ReactionDTO reactionDTO = reactionMapper.toDto(reaction);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restReactionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, reactionDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(reactionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Reaction in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchReaction() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        reaction.setId(longCount.incrementAndGet());

        // Create the Reaction
        ReactionDTO reactionDTO = reactionMapper.toDto(reaction);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restReactionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(reactionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Reaction in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamReaction() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        reaction.setId(longCount.incrementAndGet());

        // Create the Reaction
        ReactionDTO reactionDTO = reactionMapper.toDto(reaction);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restReactionMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(reactionDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Reaction in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateReactionWithPatch() throws Exception {
        // Initialize the database
        insertedReaction = reactionRepository.saveAndFlush(reaction);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the reaction using partial update
        Reaction partialUpdatedReaction = new Reaction();
        partialUpdatedReaction.setId(reaction.getId());

        partialUpdatedReaction.date(UPDATED_DATE).cible(UPDATED_CIBLE);

        restReactionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedReaction.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedReaction))
            )
            .andExpect(status().isOk());

        // Validate the Reaction in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertReactionUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedReaction, reaction), getPersistedReaction(reaction));
    }

    @Test
    @Transactional
    void fullUpdateReactionWithPatch() throws Exception {
        // Initialize the database
        insertedReaction = reactionRepository.saveAndFlush(reaction);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the reaction using partial update
        Reaction partialUpdatedReaction = new Reaction();
        partialUpdatedReaction.setId(reaction.getId());

        partialUpdatedReaction.type(UPDATED_TYPE).date(UPDATED_DATE).cible(UPDATED_CIBLE);

        restReactionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedReaction.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedReaction))
            )
            .andExpect(status().isOk());

        // Validate the Reaction in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertReactionUpdatableFieldsEquals(partialUpdatedReaction, getPersistedReaction(partialUpdatedReaction));
    }

    @Test
    @Transactional
    void patchNonExistingReaction() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        reaction.setId(longCount.incrementAndGet());

        // Create the Reaction
        ReactionDTO reactionDTO = reactionMapper.toDto(reaction);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restReactionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, reactionDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(reactionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Reaction in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchReaction() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        reaction.setId(longCount.incrementAndGet());

        // Create the Reaction
        ReactionDTO reactionDTO = reactionMapper.toDto(reaction);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restReactionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(reactionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Reaction in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamReaction() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        reaction.setId(longCount.incrementAndGet());

        // Create the Reaction
        ReactionDTO reactionDTO = reactionMapper.toDto(reaction);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restReactionMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(reactionDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Reaction in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteReaction() throws Exception {
        // Initialize the database
        insertedReaction = reactionRepository.saveAndFlush(reaction);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the reaction
        restReactionMockMvc
            .perform(delete(ENTITY_API_URL_ID, reaction.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return reactionRepository.count();
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

    protected Reaction getPersistedReaction(Reaction reaction) {
        return reactionRepository.findById(reaction.getId()).orElseThrow();
    }

    protected void assertPersistedReactionToMatchAllProperties(Reaction expectedReaction) {
        assertReactionAllPropertiesEquals(expectedReaction, getPersistedReaction(expectedReaction));
    }

    protected void assertPersistedReactionToMatchUpdatableProperties(Reaction expectedReaction) {
        assertReactionAllUpdatablePropertiesEquals(expectedReaction, getPersistedReaction(expectedReaction));
    }
}
