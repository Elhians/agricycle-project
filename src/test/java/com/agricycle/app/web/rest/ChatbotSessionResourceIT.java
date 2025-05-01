package com.agricycle.app.web.rest;

import static com.agricycle.app.domain.ChatbotSessionAsserts.*;
import static com.agricycle.app.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.agricycle.app.IntegrationTest;
import com.agricycle.app.domain.ChatbotSession;
import com.agricycle.app.domain.Utilisateur;
import com.agricycle.app.repository.ChatbotSessionRepository;
import com.agricycle.app.service.dto.ChatbotSessionDTO;
import com.agricycle.app.service.mapper.ChatbotSessionMapper;
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
 * Integration tests for the {@link ChatbotSessionResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ChatbotSessionResourceIT {

    private static final Instant DEFAULT_DATE_DEBUT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_DATE_DEBUT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_DATE_FIN = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_DATE_FIN = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/chatbot-sessions";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private ChatbotSessionRepository chatbotSessionRepository;

    @Autowired
    private ChatbotSessionMapper chatbotSessionMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restChatbotSessionMockMvc;

    private ChatbotSession chatbotSession;

    private ChatbotSession insertedChatbotSession;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ChatbotSession createEntity() {
        return new ChatbotSession().dateDebut(DEFAULT_DATE_DEBUT).dateFin(DEFAULT_DATE_FIN);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ChatbotSession createUpdatedEntity() {
        return new ChatbotSession().dateDebut(UPDATED_DATE_DEBUT).dateFin(UPDATED_DATE_FIN);
    }

    @BeforeEach
    void initTest() {
        chatbotSession = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedChatbotSession != null) {
            chatbotSessionRepository.delete(insertedChatbotSession);
            insertedChatbotSession = null;
        }
    }

    @Test
    @Transactional
    void createChatbotSession() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the ChatbotSession
        ChatbotSessionDTO chatbotSessionDTO = chatbotSessionMapper.toDto(chatbotSession);
        var returnedChatbotSessionDTO = om.readValue(
            restChatbotSessionMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(chatbotSessionDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            ChatbotSessionDTO.class
        );

        // Validate the ChatbotSession in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedChatbotSession = chatbotSessionMapper.toEntity(returnedChatbotSessionDTO);
        assertChatbotSessionUpdatableFieldsEquals(returnedChatbotSession, getPersistedChatbotSession(returnedChatbotSession));

        insertedChatbotSession = returnedChatbotSession;
    }

    @Test
    @Transactional
    void createChatbotSessionWithExistingId() throws Exception {
        // Create the ChatbotSession with an existing ID
        chatbotSession.setId(1L);
        ChatbotSessionDTO chatbotSessionDTO = chatbotSessionMapper.toDto(chatbotSession);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restChatbotSessionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(chatbotSessionDTO)))
            .andExpect(status().isBadRequest());

        // Validate the ChatbotSession in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllChatbotSessions() throws Exception {
        // Initialize the database
        insertedChatbotSession = chatbotSessionRepository.saveAndFlush(chatbotSession);

        // Get all the chatbotSessionList
        restChatbotSessionMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(chatbotSession.getId().intValue())))
            .andExpect(jsonPath("$.[*].dateDebut").value(hasItem(DEFAULT_DATE_DEBUT.toString())))
            .andExpect(jsonPath("$.[*].dateFin").value(hasItem(DEFAULT_DATE_FIN.toString())));
    }

    @Test
    @Transactional
    void getChatbotSession() throws Exception {
        // Initialize the database
        insertedChatbotSession = chatbotSessionRepository.saveAndFlush(chatbotSession);

        // Get the chatbotSession
        restChatbotSessionMockMvc
            .perform(get(ENTITY_API_URL_ID, chatbotSession.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(chatbotSession.getId().intValue()))
            .andExpect(jsonPath("$.dateDebut").value(DEFAULT_DATE_DEBUT.toString()))
            .andExpect(jsonPath("$.dateFin").value(DEFAULT_DATE_FIN.toString()));
    }

    @Test
    @Transactional
    void getChatbotSessionsByIdFiltering() throws Exception {
        // Initialize the database
        insertedChatbotSession = chatbotSessionRepository.saveAndFlush(chatbotSession);

        Long id = chatbotSession.getId();

        defaultChatbotSessionFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultChatbotSessionFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultChatbotSessionFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllChatbotSessionsByDateDebutIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedChatbotSession = chatbotSessionRepository.saveAndFlush(chatbotSession);

        // Get all the chatbotSessionList where dateDebut equals to
        defaultChatbotSessionFiltering("dateDebut.equals=" + DEFAULT_DATE_DEBUT, "dateDebut.equals=" + UPDATED_DATE_DEBUT);
    }

    @Test
    @Transactional
    void getAllChatbotSessionsByDateDebutIsInShouldWork() throws Exception {
        // Initialize the database
        insertedChatbotSession = chatbotSessionRepository.saveAndFlush(chatbotSession);

        // Get all the chatbotSessionList where dateDebut in
        defaultChatbotSessionFiltering(
            "dateDebut.in=" + DEFAULT_DATE_DEBUT + "," + UPDATED_DATE_DEBUT,
            "dateDebut.in=" + UPDATED_DATE_DEBUT
        );
    }

    @Test
    @Transactional
    void getAllChatbotSessionsByDateDebutIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedChatbotSession = chatbotSessionRepository.saveAndFlush(chatbotSession);

        // Get all the chatbotSessionList where dateDebut is not null
        defaultChatbotSessionFiltering("dateDebut.specified=true", "dateDebut.specified=false");
    }

    @Test
    @Transactional
    void getAllChatbotSessionsByDateFinIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedChatbotSession = chatbotSessionRepository.saveAndFlush(chatbotSession);

        // Get all the chatbotSessionList where dateFin equals to
        defaultChatbotSessionFiltering("dateFin.equals=" + DEFAULT_DATE_FIN, "dateFin.equals=" + UPDATED_DATE_FIN);
    }

    @Test
    @Transactional
    void getAllChatbotSessionsByDateFinIsInShouldWork() throws Exception {
        // Initialize the database
        insertedChatbotSession = chatbotSessionRepository.saveAndFlush(chatbotSession);

        // Get all the chatbotSessionList where dateFin in
        defaultChatbotSessionFiltering("dateFin.in=" + DEFAULT_DATE_FIN + "," + UPDATED_DATE_FIN, "dateFin.in=" + UPDATED_DATE_FIN);
    }

    @Test
    @Transactional
    void getAllChatbotSessionsByDateFinIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedChatbotSession = chatbotSessionRepository.saveAndFlush(chatbotSession);

        // Get all the chatbotSessionList where dateFin is not null
        defaultChatbotSessionFiltering("dateFin.specified=true", "dateFin.specified=false");
    }

    @Test
    @Transactional
    void getAllChatbotSessionsByUtilisateurIsEqualToSomething() throws Exception {
        Utilisateur utilisateur;
        if (TestUtil.findAll(em, Utilisateur.class).isEmpty()) {
            chatbotSessionRepository.saveAndFlush(chatbotSession);
            utilisateur = UtilisateurResourceIT.createEntity();
        } else {
            utilisateur = TestUtil.findAll(em, Utilisateur.class).get(0);
        }
        em.persist(utilisateur);
        em.flush();
        chatbotSession.setUtilisateur(utilisateur);
        chatbotSessionRepository.saveAndFlush(chatbotSession);
        Long utilisateurId = utilisateur.getId();
        // Get all the chatbotSessionList where utilisateur equals to utilisateurId
        defaultChatbotSessionShouldBeFound("utilisateurId.equals=" + utilisateurId);

        // Get all the chatbotSessionList where utilisateur equals to (utilisateurId + 1)
        defaultChatbotSessionShouldNotBeFound("utilisateurId.equals=" + (utilisateurId + 1));
    }

    private void defaultChatbotSessionFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultChatbotSessionShouldBeFound(shouldBeFound);
        defaultChatbotSessionShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultChatbotSessionShouldBeFound(String filter) throws Exception {
        restChatbotSessionMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(chatbotSession.getId().intValue())))
            .andExpect(jsonPath("$.[*].dateDebut").value(hasItem(DEFAULT_DATE_DEBUT.toString())))
            .andExpect(jsonPath("$.[*].dateFin").value(hasItem(DEFAULT_DATE_FIN.toString())));

        // Check, that the count call also returns 1
        restChatbotSessionMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultChatbotSessionShouldNotBeFound(String filter) throws Exception {
        restChatbotSessionMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restChatbotSessionMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingChatbotSession() throws Exception {
        // Get the chatbotSession
        restChatbotSessionMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingChatbotSession() throws Exception {
        // Initialize the database
        insertedChatbotSession = chatbotSessionRepository.saveAndFlush(chatbotSession);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the chatbotSession
        ChatbotSession updatedChatbotSession = chatbotSessionRepository.findById(chatbotSession.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedChatbotSession are not directly saved in db
        em.detach(updatedChatbotSession);
        updatedChatbotSession.dateDebut(UPDATED_DATE_DEBUT).dateFin(UPDATED_DATE_FIN);
        ChatbotSessionDTO chatbotSessionDTO = chatbotSessionMapper.toDto(updatedChatbotSession);

        restChatbotSessionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, chatbotSessionDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(chatbotSessionDTO))
            )
            .andExpect(status().isOk());

        // Validate the ChatbotSession in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedChatbotSessionToMatchAllProperties(updatedChatbotSession);
    }

    @Test
    @Transactional
    void putNonExistingChatbotSession() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        chatbotSession.setId(longCount.incrementAndGet());

        // Create the ChatbotSession
        ChatbotSessionDTO chatbotSessionDTO = chatbotSessionMapper.toDto(chatbotSession);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restChatbotSessionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, chatbotSessionDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(chatbotSessionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ChatbotSession in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchChatbotSession() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        chatbotSession.setId(longCount.incrementAndGet());

        // Create the ChatbotSession
        ChatbotSessionDTO chatbotSessionDTO = chatbotSessionMapper.toDto(chatbotSession);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restChatbotSessionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(chatbotSessionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ChatbotSession in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamChatbotSession() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        chatbotSession.setId(longCount.incrementAndGet());

        // Create the ChatbotSession
        ChatbotSessionDTO chatbotSessionDTO = chatbotSessionMapper.toDto(chatbotSession);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restChatbotSessionMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(chatbotSessionDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the ChatbotSession in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateChatbotSessionWithPatch() throws Exception {
        // Initialize the database
        insertedChatbotSession = chatbotSessionRepository.saveAndFlush(chatbotSession);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the chatbotSession using partial update
        ChatbotSession partialUpdatedChatbotSession = new ChatbotSession();
        partialUpdatedChatbotSession.setId(chatbotSession.getId());

        partialUpdatedChatbotSession.dateDebut(UPDATED_DATE_DEBUT);

        restChatbotSessionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedChatbotSession.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedChatbotSession))
            )
            .andExpect(status().isOk());

        // Validate the ChatbotSession in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertChatbotSessionUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedChatbotSession, chatbotSession),
            getPersistedChatbotSession(chatbotSession)
        );
    }

    @Test
    @Transactional
    void fullUpdateChatbotSessionWithPatch() throws Exception {
        // Initialize the database
        insertedChatbotSession = chatbotSessionRepository.saveAndFlush(chatbotSession);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the chatbotSession using partial update
        ChatbotSession partialUpdatedChatbotSession = new ChatbotSession();
        partialUpdatedChatbotSession.setId(chatbotSession.getId());

        partialUpdatedChatbotSession.dateDebut(UPDATED_DATE_DEBUT).dateFin(UPDATED_DATE_FIN);

        restChatbotSessionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedChatbotSession.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedChatbotSession))
            )
            .andExpect(status().isOk());

        // Validate the ChatbotSession in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertChatbotSessionUpdatableFieldsEquals(partialUpdatedChatbotSession, getPersistedChatbotSession(partialUpdatedChatbotSession));
    }

    @Test
    @Transactional
    void patchNonExistingChatbotSession() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        chatbotSession.setId(longCount.incrementAndGet());

        // Create the ChatbotSession
        ChatbotSessionDTO chatbotSessionDTO = chatbotSessionMapper.toDto(chatbotSession);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restChatbotSessionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, chatbotSessionDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(chatbotSessionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ChatbotSession in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchChatbotSession() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        chatbotSession.setId(longCount.incrementAndGet());

        // Create the ChatbotSession
        ChatbotSessionDTO chatbotSessionDTO = chatbotSessionMapper.toDto(chatbotSession);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restChatbotSessionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(chatbotSessionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ChatbotSession in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamChatbotSession() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        chatbotSession.setId(longCount.incrementAndGet());

        // Create the ChatbotSession
        ChatbotSessionDTO chatbotSessionDTO = chatbotSessionMapper.toDto(chatbotSession);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restChatbotSessionMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(chatbotSessionDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the ChatbotSession in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteChatbotSession() throws Exception {
        // Initialize the database
        insertedChatbotSession = chatbotSessionRepository.saveAndFlush(chatbotSession);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the chatbotSession
        restChatbotSessionMockMvc
            .perform(delete(ENTITY_API_URL_ID, chatbotSession.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return chatbotSessionRepository.count();
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

    protected ChatbotSession getPersistedChatbotSession(ChatbotSession chatbotSession) {
        return chatbotSessionRepository.findById(chatbotSession.getId()).orElseThrow();
    }

    protected void assertPersistedChatbotSessionToMatchAllProperties(ChatbotSession expectedChatbotSession) {
        assertChatbotSessionAllPropertiesEquals(expectedChatbotSession, getPersistedChatbotSession(expectedChatbotSession));
    }

    protected void assertPersistedChatbotSessionToMatchUpdatableProperties(ChatbotSession expectedChatbotSession) {
        assertChatbotSessionAllUpdatablePropertiesEquals(expectedChatbotSession, getPersistedChatbotSession(expectedChatbotSession));
    }
}
