package com.agricycle.app.web.rest;

import static com.agricycle.app.domain.CommentaireAsserts.*;
import static com.agricycle.app.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.agricycle.app.IntegrationTest;
import com.agricycle.app.domain.Commentaire;
import com.agricycle.app.domain.Post;
import com.agricycle.app.domain.Utilisateur;
import com.agricycle.app.repository.CommentaireRepository;
import com.agricycle.app.service.dto.CommentaireDTO;
import com.agricycle.app.service.mapper.CommentaireMapper;
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
 * Integration tests for the {@link CommentaireResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class CommentaireResourceIT {

    private static final String DEFAULT_CONTENU = "AAAAAAAAAA";
    private static final String UPDATED_CONTENU = "BBBBBBBBBB";

    private static final Instant DEFAULT_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/commentaires";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private CommentaireRepository commentaireRepository;

    @Autowired
    private CommentaireMapper commentaireMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restCommentaireMockMvc;

    private Commentaire commentaire;

    private Commentaire insertedCommentaire;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Commentaire createEntity() {
        return new Commentaire().contenu(DEFAULT_CONTENU).date(DEFAULT_DATE);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Commentaire createUpdatedEntity() {
        return new Commentaire().contenu(UPDATED_CONTENU).date(UPDATED_DATE);
    }

    @BeforeEach
    void initTest() {
        commentaire = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedCommentaire != null) {
            commentaireRepository.delete(insertedCommentaire);
            insertedCommentaire = null;
        }
    }

    @Test
    @Transactional
    void createCommentaire() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Commentaire
        CommentaireDTO commentaireDTO = commentaireMapper.toDto(commentaire);
        var returnedCommentaireDTO = om.readValue(
            restCommentaireMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(commentaireDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            CommentaireDTO.class
        );

        // Validate the Commentaire in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedCommentaire = commentaireMapper.toEntity(returnedCommentaireDTO);
        assertCommentaireUpdatableFieldsEquals(returnedCommentaire, getPersistedCommentaire(returnedCommentaire));

        insertedCommentaire = returnedCommentaire;
    }

    @Test
    @Transactional
    void createCommentaireWithExistingId() throws Exception {
        // Create the Commentaire with an existing ID
        commentaire.setId(1L);
        CommentaireDTO commentaireDTO = commentaireMapper.toDto(commentaire);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restCommentaireMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(commentaireDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Commentaire in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkContenuIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        commentaire.setContenu(null);

        // Create the Commentaire, which fails.
        CommentaireDTO commentaireDTO = commentaireMapper.toDto(commentaire);

        restCommentaireMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(commentaireDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkDateIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        commentaire.setDate(null);

        // Create the Commentaire, which fails.
        CommentaireDTO commentaireDTO = commentaireMapper.toDto(commentaire);

        restCommentaireMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(commentaireDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllCommentaires() throws Exception {
        // Initialize the database
        insertedCommentaire = commentaireRepository.saveAndFlush(commentaire);

        // Get all the commentaireList
        restCommentaireMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(commentaire.getId().intValue())))
            .andExpect(jsonPath("$.[*].contenu").value(hasItem(DEFAULT_CONTENU)))
            .andExpect(jsonPath("$.[*].date").value(hasItem(DEFAULT_DATE.toString())));
    }

    @Test
    @Transactional
    void getCommentaire() throws Exception {
        // Initialize the database
        insertedCommentaire = commentaireRepository.saveAndFlush(commentaire);

        // Get the commentaire
        restCommentaireMockMvc
            .perform(get(ENTITY_API_URL_ID, commentaire.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(commentaire.getId().intValue()))
            .andExpect(jsonPath("$.contenu").value(DEFAULT_CONTENU))
            .andExpect(jsonPath("$.date").value(DEFAULT_DATE.toString()));
    }

    @Test
    @Transactional
    void getCommentairesByIdFiltering() throws Exception {
        // Initialize the database
        insertedCommentaire = commentaireRepository.saveAndFlush(commentaire);

        Long id = commentaire.getId();

        defaultCommentaireFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultCommentaireFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultCommentaireFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllCommentairesByContenuIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedCommentaire = commentaireRepository.saveAndFlush(commentaire);

        // Get all the commentaireList where contenu equals to
        defaultCommentaireFiltering("contenu.equals=" + DEFAULT_CONTENU, "contenu.equals=" + UPDATED_CONTENU);
    }

    @Test
    @Transactional
    void getAllCommentairesByContenuIsInShouldWork() throws Exception {
        // Initialize the database
        insertedCommentaire = commentaireRepository.saveAndFlush(commentaire);

        // Get all the commentaireList where contenu in
        defaultCommentaireFiltering("contenu.in=" + DEFAULT_CONTENU + "," + UPDATED_CONTENU, "contenu.in=" + UPDATED_CONTENU);
    }

    @Test
    @Transactional
    void getAllCommentairesByContenuIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedCommentaire = commentaireRepository.saveAndFlush(commentaire);

        // Get all the commentaireList where contenu is not null
        defaultCommentaireFiltering("contenu.specified=true", "contenu.specified=false");
    }

    @Test
    @Transactional
    void getAllCommentairesByContenuContainsSomething() throws Exception {
        // Initialize the database
        insertedCommentaire = commentaireRepository.saveAndFlush(commentaire);

        // Get all the commentaireList where contenu contains
        defaultCommentaireFiltering("contenu.contains=" + DEFAULT_CONTENU, "contenu.contains=" + UPDATED_CONTENU);
    }

    @Test
    @Transactional
    void getAllCommentairesByContenuNotContainsSomething() throws Exception {
        // Initialize the database
        insertedCommentaire = commentaireRepository.saveAndFlush(commentaire);

        // Get all the commentaireList where contenu does not contain
        defaultCommentaireFiltering("contenu.doesNotContain=" + UPDATED_CONTENU, "contenu.doesNotContain=" + DEFAULT_CONTENU);
    }

    @Test
    @Transactional
    void getAllCommentairesByDateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedCommentaire = commentaireRepository.saveAndFlush(commentaire);

        // Get all the commentaireList where date equals to
        defaultCommentaireFiltering("date.equals=" + DEFAULT_DATE, "date.equals=" + UPDATED_DATE);
    }

    @Test
    @Transactional
    void getAllCommentairesByDateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedCommentaire = commentaireRepository.saveAndFlush(commentaire);

        // Get all the commentaireList where date in
        defaultCommentaireFiltering("date.in=" + DEFAULT_DATE + "," + UPDATED_DATE, "date.in=" + UPDATED_DATE);
    }

    @Test
    @Transactional
    void getAllCommentairesByDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedCommentaire = commentaireRepository.saveAndFlush(commentaire);

        // Get all the commentaireList where date is not null
        defaultCommentaireFiltering("date.specified=true", "date.specified=false");
    }

    @Test
    @Transactional
    void getAllCommentairesByPostIsEqualToSomething() throws Exception {
        Post post;
        if (TestUtil.findAll(em, Post.class).isEmpty()) {
            commentaireRepository.saveAndFlush(commentaire);
            post = PostResourceIT.createEntity();
        } else {
            post = TestUtil.findAll(em, Post.class).get(0);
        }
        em.persist(post);
        em.flush();
        commentaire.setPost(post);
        commentaireRepository.saveAndFlush(commentaire);
        Long postId = post.getId();
        // Get all the commentaireList where post equals to postId
        defaultCommentaireShouldBeFound("postId.equals=" + postId);

        // Get all the commentaireList where post equals to (postId + 1)
        defaultCommentaireShouldNotBeFound("postId.equals=" + (postId + 1));
    }

    @Test
    @Transactional
    void getAllCommentairesByAuteurIsEqualToSomething() throws Exception {
        Utilisateur auteur;
        if (TestUtil.findAll(em, Utilisateur.class).isEmpty()) {
            commentaireRepository.saveAndFlush(commentaire);
            auteur = UtilisateurResourceIT.createEntity();
        } else {
            auteur = TestUtil.findAll(em, Utilisateur.class).get(0);
        }
        em.persist(auteur);
        em.flush();
        commentaire.setAuteur(auteur);
        commentaireRepository.saveAndFlush(commentaire);
        Long auteurId = auteur.getId();
        // Get all the commentaireList where auteur equals to auteurId
        defaultCommentaireShouldBeFound("auteurId.equals=" + auteurId);

        // Get all the commentaireList where auteur equals to (auteurId + 1)
        defaultCommentaireShouldNotBeFound("auteurId.equals=" + (auteurId + 1));
    }

    private void defaultCommentaireFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultCommentaireShouldBeFound(shouldBeFound);
        defaultCommentaireShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultCommentaireShouldBeFound(String filter) throws Exception {
        restCommentaireMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(commentaire.getId().intValue())))
            .andExpect(jsonPath("$.[*].contenu").value(hasItem(DEFAULT_CONTENU)))
            .andExpect(jsonPath("$.[*].date").value(hasItem(DEFAULT_DATE.toString())));

        // Check, that the count call also returns 1
        restCommentaireMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultCommentaireShouldNotBeFound(String filter) throws Exception {
        restCommentaireMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restCommentaireMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingCommentaire() throws Exception {
        // Get the commentaire
        restCommentaireMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingCommentaire() throws Exception {
        // Initialize the database
        insertedCommentaire = commentaireRepository.saveAndFlush(commentaire);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the commentaire
        Commentaire updatedCommentaire = commentaireRepository.findById(commentaire.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedCommentaire are not directly saved in db
        em.detach(updatedCommentaire);
        updatedCommentaire.contenu(UPDATED_CONTENU).date(UPDATED_DATE);
        CommentaireDTO commentaireDTO = commentaireMapper.toDto(updatedCommentaire);

        restCommentaireMockMvc
            .perform(
                put(ENTITY_API_URL_ID, commentaireDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(commentaireDTO))
            )
            .andExpect(status().isOk());

        // Validate the Commentaire in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedCommentaireToMatchAllProperties(updatedCommentaire);
    }

    @Test
    @Transactional
    void putNonExistingCommentaire() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        commentaire.setId(longCount.incrementAndGet());

        // Create the Commentaire
        CommentaireDTO commentaireDTO = commentaireMapper.toDto(commentaire);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCommentaireMockMvc
            .perform(
                put(ENTITY_API_URL_ID, commentaireDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(commentaireDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Commentaire in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchCommentaire() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        commentaire.setId(longCount.incrementAndGet());

        // Create the Commentaire
        CommentaireDTO commentaireDTO = commentaireMapper.toDto(commentaire);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCommentaireMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(commentaireDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Commentaire in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamCommentaire() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        commentaire.setId(longCount.incrementAndGet());

        // Create the Commentaire
        CommentaireDTO commentaireDTO = commentaireMapper.toDto(commentaire);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCommentaireMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(commentaireDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Commentaire in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateCommentaireWithPatch() throws Exception {
        // Initialize the database
        insertedCommentaire = commentaireRepository.saveAndFlush(commentaire);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the commentaire using partial update
        Commentaire partialUpdatedCommentaire = new Commentaire();
        partialUpdatedCommentaire.setId(commentaire.getId());

        partialUpdatedCommentaire.date(UPDATED_DATE);

        restCommentaireMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCommentaire.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedCommentaire))
            )
            .andExpect(status().isOk());

        // Validate the Commentaire in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertCommentaireUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedCommentaire, commentaire),
            getPersistedCommentaire(commentaire)
        );
    }

    @Test
    @Transactional
    void fullUpdateCommentaireWithPatch() throws Exception {
        // Initialize the database
        insertedCommentaire = commentaireRepository.saveAndFlush(commentaire);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the commentaire using partial update
        Commentaire partialUpdatedCommentaire = new Commentaire();
        partialUpdatedCommentaire.setId(commentaire.getId());

        partialUpdatedCommentaire.contenu(UPDATED_CONTENU).date(UPDATED_DATE);

        restCommentaireMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCommentaire.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedCommentaire))
            )
            .andExpect(status().isOk());

        // Validate the Commentaire in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertCommentaireUpdatableFieldsEquals(partialUpdatedCommentaire, getPersistedCommentaire(partialUpdatedCommentaire));
    }

    @Test
    @Transactional
    void patchNonExistingCommentaire() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        commentaire.setId(longCount.incrementAndGet());

        // Create the Commentaire
        CommentaireDTO commentaireDTO = commentaireMapper.toDto(commentaire);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCommentaireMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, commentaireDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(commentaireDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Commentaire in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchCommentaire() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        commentaire.setId(longCount.incrementAndGet());

        // Create the Commentaire
        CommentaireDTO commentaireDTO = commentaireMapper.toDto(commentaire);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCommentaireMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(commentaireDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Commentaire in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamCommentaire() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        commentaire.setId(longCount.incrementAndGet());

        // Create the Commentaire
        CommentaireDTO commentaireDTO = commentaireMapper.toDto(commentaire);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCommentaireMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(commentaireDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Commentaire in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteCommentaire() throws Exception {
        // Initialize the database
        insertedCommentaire = commentaireRepository.saveAndFlush(commentaire);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the commentaire
        restCommentaireMockMvc
            .perform(delete(ENTITY_API_URL_ID, commentaire.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return commentaireRepository.count();
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

    protected Commentaire getPersistedCommentaire(Commentaire commentaire) {
        return commentaireRepository.findById(commentaire.getId()).orElseThrow();
    }

    protected void assertPersistedCommentaireToMatchAllProperties(Commentaire expectedCommentaire) {
        assertCommentaireAllPropertiesEquals(expectedCommentaire, getPersistedCommentaire(expectedCommentaire));
    }

    protected void assertPersistedCommentaireToMatchUpdatableProperties(Commentaire expectedCommentaire) {
        assertCommentaireAllUpdatablePropertiesEquals(expectedCommentaire, getPersistedCommentaire(expectedCommentaire));
    }
}
