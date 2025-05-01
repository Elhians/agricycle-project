package com.agricycle.app.web.rest;

import static com.agricycle.app.domain.OrganisationAsserts.*;
import static com.agricycle.app.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.agricycle.app.IntegrationTest;
import com.agricycle.app.domain.Organisation;
import com.agricycle.app.domain.Utilisateur;
import com.agricycle.app.repository.OrganisationRepository;
import com.agricycle.app.service.dto.OrganisationDTO;
import com.agricycle.app.service.mapper.OrganisationMapper;
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
 * Integration tests for the {@link OrganisationResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class OrganisationResourceIT {

    private static final String DEFAULT_NOM_ORGANISATION = "AAAAAAAAAA";
    private static final String UPDATED_NOM_ORGANISATION = "BBBBBBBBBB";

    private static final String DEFAULT_SITE_WEB = "AAAAAAAAAA";
    private static final String UPDATED_SITE_WEB = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/organisations";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private OrganisationRepository organisationRepository;

    @Autowired
    private OrganisationMapper organisationMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restOrganisationMockMvc;

    private Organisation organisation;

    private Organisation insertedOrganisation;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Organisation createEntity() {
        return new Organisation().nomOrganisation(DEFAULT_NOM_ORGANISATION).siteWeb(DEFAULT_SITE_WEB);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Organisation createUpdatedEntity() {
        return new Organisation().nomOrganisation(UPDATED_NOM_ORGANISATION).siteWeb(UPDATED_SITE_WEB);
    }

    @BeforeEach
    void initTest() {
        organisation = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedOrganisation != null) {
            organisationRepository.delete(insertedOrganisation);
            insertedOrganisation = null;
        }
    }

    @Test
    @Transactional
    void createOrganisation() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Organisation
        OrganisationDTO organisationDTO = organisationMapper.toDto(organisation);
        var returnedOrganisationDTO = om.readValue(
            restOrganisationMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(organisationDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            OrganisationDTO.class
        );

        // Validate the Organisation in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedOrganisation = organisationMapper.toEntity(returnedOrganisationDTO);
        assertOrganisationUpdatableFieldsEquals(returnedOrganisation, getPersistedOrganisation(returnedOrganisation));

        insertedOrganisation = returnedOrganisation;
    }

    @Test
    @Transactional
    void createOrganisationWithExistingId() throws Exception {
        // Create the Organisation with an existing ID
        organisation.setId(1L);
        OrganisationDTO organisationDTO = organisationMapper.toDto(organisation);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restOrganisationMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(organisationDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Organisation in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllOrganisations() throws Exception {
        // Initialize the database
        insertedOrganisation = organisationRepository.saveAndFlush(organisation);

        // Get all the organisationList
        restOrganisationMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(organisation.getId().intValue())))
            .andExpect(jsonPath("$.[*].nomOrganisation").value(hasItem(DEFAULT_NOM_ORGANISATION)))
            .andExpect(jsonPath("$.[*].siteWeb").value(hasItem(DEFAULT_SITE_WEB)));
    }

    @Test
    @Transactional
    void getOrganisation() throws Exception {
        // Initialize the database
        insertedOrganisation = organisationRepository.saveAndFlush(organisation);

        // Get the organisation
        restOrganisationMockMvc
            .perform(get(ENTITY_API_URL_ID, organisation.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(organisation.getId().intValue()))
            .andExpect(jsonPath("$.nomOrganisation").value(DEFAULT_NOM_ORGANISATION))
            .andExpect(jsonPath("$.siteWeb").value(DEFAULT_SITE_WEB));
    }

    @Test
    @Transactional
    void getOrganisationsByIdFiltering() throws Exception {
        // Initialize the database
        insertedOrganisation = organisationRepository.saveAndFlush(organisation);

        Long id = organisation.getId();

        defaultOrganisationFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultOrganisationFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultOrganisationFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllOrganisationsByNomOrganisationIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedOrganisation = organisationRepository.saveAndFlush(organisation);

        // Get all the organisationList where nomOrganisation equals to
        defaultOrganisationFiltering(
            "nomOrganisation.equals=" + DEFAULT_NOM_ORGANISATION,
            "nomOrganisation.equals=" + UPDATED_NOM_ORGANISATION
        );
    }

    @Test
    @Transactional
    void getAllOrganisationsByNomOrganisationIsInShouldWork() throws Exception {
        // Initialize the database
        insertedOrganisation = organisationRepository.saveAndFlush(organisation);

        // Get all the organisationList where nomOrganisation in
        defaultOrganisationFiltering(
            "nomOrganisation.in=" + DEFAULT_NOM_ORGANISATION + "," + UPDATED_NOM_ORGANISATION,
            "nomOrganisation.in=" + UPDATED_NOM_ORGANISATION
        );
    }

    @Test
    @Transactional
    void getAllOrganisationsByNomOrganisationIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedOrganisation = organisationRepository.saveAndFlush(organisation);

        // Get all the organisationList where nomOrganisation is not null
        defaultOrganisationFiltering("nomOrganisation.specified=true", "nomOrganisation.specified=false");
    }

    @Test
    @Transactional
    void getAllOrganisationsByNomOrganisationContainsSomething() throws Exception {
        // Initialize the database
        insertedOrganisation = organisationRepository.saveAndFlush(organisation);

        // Get all the organisationList where nomOrganisation contains
        defaultOrganisationFiltering(
            "nomOrganisation.contains=" + DEFAULT_NOM_ORGANISATION,
            "nomOrganisation.contains=" + UPDATED_NOM_ORGANISATION
        );
    }

    @Test
    @Transactional
    void getAllOrganisationsByNomOrganisationNotContainsSomething() throws Exception {
        // Initialize the database
        insertedOrganisation = organisationRepository.saveAndFlush(organisation);

        // Get all the organisationList where nomOrganisation does not contain
        defaultOrganisationFiltering(
            "nomOrganisation.doesNotContain=" + UPDATED_NOM_ORGANISATION,
            "nomOrganisation.doesNotContain=" + DEFAULT_NOM_ORGANISATION
        );
    }

    @Test
    @Transactional
    void getAllOrganisationsBySiteWebIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedOrganisation = organisationRepository.saveAndFlush(organisation);

        // Get all the organisationList where siteWeb equals to
        defaultOrganisationFiltering("siteWeb.equals=" + DEFAULT_SITE_WEB, "siteWeb.equals=" + UPDATED_SITE_WEB);
    }

    @Test
    @Transactional
    void getAllOrganisationsBySiteWebIsInShouldWork() throws Exception {
        // Initialize the database
        insertedOrganisation = organisationRepository.saveAndFlush(organisation);

        // Get all the organisationList where siteWeb in
        defaultOrganisationFiltering("siteWeb.in=" + DEFAULT_SITE_WEB + "," + UPDATED_SITE_WEB, "siteWeb.in=" + UPDATED_SITE_WEB);
    }

    @Test
    @Transactional
    void getAllOrganisationsBySiteWebIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedOrganisation = organisationRepository.saveAndFlush(organisation);

        // Get all the organisationList where siteWeb is not null
        defaultOrganisationFiltering("siteWeb.specified=true", "siteWeb.specified=false");
    }

    @Test
    @Transactional
    void getAllOrganisationsBySiteWebContainsSomething() throws Exception {
        // Initialize the database
        insertedOrganisation = organisationRepository.saveAndFlush(organisation);

        // Get all the organisationList where siteWeb contains
        defaultOrganisationFiltering("siteWeb.contains=" + DEFAULT_SITE_WEB, "siteWeb.contains=" + UPDATED_SITE_WEB);
    }

    @Test
    @Transactional
    void getAllOrganisationsBySiteWebNotContainsSomething() throws Exception {
        // Initialize the database
        insertedOrganisation = organisationRepository.saveAndFlush(organisation);

        // Get all the organisationList where siteWeb does not contain
        defaultOrganisationFiltering("siteWeb.doesNotContain=" + UPDATED_SITE_WEB, "siteWeb.doesNotContain=" + DEFAULT_SITE_WEB);
    }

    @Test
    @Transactional
    void getAllOrganisationsByUtilisateurIsEqualToSomething() throws Exception {
        Utilisateur utilisateur;
        if (TestUtil.findAll(em, Utilisateur.class).isEmpty()) {
            organisationRepository.saveAndFlush(organisation);
            utilisateur = UtilisateurResourceIT.createEntity();
        } else {
            utilisateur = TestUtil.findAll(em, Utilisateur.class).get(0);
        }
        em.persist(utilisateur);
        em.flush();
        organisation.setUtilisateur(utilisateur);
        organisationRepository.saveAndFlush(organisation);
        Long utilisateurId = utilisateur.getId();
        // Get all the organisationList where utilisateur equals to utilisateurId
        defaultOrganisationShouldBeFound("utilisateurId.equals=" + utilisateurId);

        // Get all the organisationList where utilisateur equals to (utilisateurId + 1)
        defaultOrganisationShouldNotBeFound("utilisateurId.equals=" + (utilisateurId + 1));
    }

    private void defaultOrganisationFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultOrganisationShouldBeFound(shouldBeFound);
        defaultOrganisationShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultOrganisationShouldBeFound(String filter) throws Exception {
        restOrganisationMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(organisation.getId().intValue())))
            .andExpect(jsonPath("$.[*].nomOrganisation").value(hasItem(DEFAULT_NOM_ORGANISATION)))
            .andExpect(jsonPath("$.[*].siteWeb").value(hasItem(DEFAULT_SITE_WEB)));

        // Check, that the count call also returns 1
        restOrganisationMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultOrganisationShouldNotBeFound(String filter) throws Exception {
        restOrganisationMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restOrganisationMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingOrganisation() throws Exception {
        // Get the organisation
        restOrganisationMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingOrganisation() throws Exception {
        // Initialize the database
        insertedOrganisation = organisationRepository.saveAndFlush(organisation);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the organisation
        Organisation updatedOrganisation = organisationRepository.findById(organisation.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedOrganisation are not directly saved in db
        em.detach(updatedOrganisation);
        updatedOrganisation.nomOrganisation(UPDATED_NOM_ORGANISATION).siteWeb(UPDATED_SITE_WEB);
        OrganisationDTO organisationDTO = organisationMapper.toDto(updatedOrganisation);

        restOrganisationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, organisationDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(organisationDTO))
            )
            .andExpect(status().isOk());

        // Validate the Organisation in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedOrganisationToMatchAllProperties(updatedOrganisation);
    }

    @Test
    @Transactional
    void putNonExistingOrganisation() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        organisation.setId(longCount.incrementAndGet());

        // Create the Organisation
        OrganisationDTO organisationDTO = organisationMapper.toDto(organisation);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restOrganisationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, organisationDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(organisationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Organisation in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchOrganisation() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        organisation.setId(longCount.incrementAndGet());

        // Create the Organisation
        OrganisationDTO organisationDTO = organisationMapper.toDto(organisation);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOrganisationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(organisationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Organisation in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamOrganisation() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        organisation.setId(longCount.incrementAndGet());

        // Create the Organisation
        OrganisationDTO organisationDTO = organisationMapper.toDto(organisation);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOrganisationMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(organisationDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Organisation in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateOrganisationWithPatch() throws Exception {
        // Initialize the database
        insertedOrganisation = organisationRepository.saveAndFlush(organisation);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the organisation using partial update
        Organisation partialUpdatedOrganisation = new Organisation();
        partialUpdatedOrganisation.setId(organisation.getId());

        partialUpdatedOrganisation.siteWeb(UPDATED_SITE_WEB);

        restOrganisationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedOrganisation.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedOrganisation))
            )
            .andExpect(status().isOk());

        // Validate the Organisation in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertOrganisationUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedOrganisation, organisation),
            getPersistedOrganisation(organisation)
        );
    }

    @Test
    @Transactional
    void fullUpdateOrganisationWithPatch() throws Exception {
        // Initialize the database
        insertedOrganisation = organisationRepository.saveAndFlush(organisation);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the organisation using partial update
        Organisation partialUpdatedOrganisation = new Organisation();
        partialUpdatedOrganisation.setId(organisation.getId());

        partialUpdatedOrganisation.nomOrganisation(UPDATED_NOM_ORGANISATION).siteWeb(UPDATED_SITE_WEB);

        restOrganisationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedOrganisation.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedOrganisation))
            )
            .andExpect(status().isOk());

        // Validate the Organisation in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertOrganisationUpdatableFieldsEquals(partialUpdatedOrganisation, getPersistedOrganisation(partialUpdatedOrganisation));
    }

    @Test
    @Transactional
    void patchNonExistingOrganisation() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        organisation.setId(longCount.incrementAndGet());

        // Create the Organisation
        OrganisationDTO organisationDTO = organisationMapper.toDto(organisation);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restOrganisationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, organisationDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(organisationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Organisation in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchOrganisation() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        organisation.setId(longCount.incrementAndGet());

        // Create the Organisation
        OrganisationDTO organisationDTO = organisationMapper.toDto(organisation);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOrganisationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(organisationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Organisation in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamOrganisation() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        organisation.setId(longCount.incrementAndGet());

        // Create the Organisation
        OrganisationDTO organisationDTO = organisationMapper.toDto(organisation);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOrganisationMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(organisationDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Organisation in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteOrganisation() throws Exception {
        // Initialize the database
        insertedOrganisation = organisationRepository.saveAndFlush(organisation);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the organisation
        restOrganisationMockMvc
            .perform(delete(ENTITY_API_URL_ID, organisation.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return organisationRepository.count();
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

    protected Organisation getPersistedOrganisation(Organisation organisation) {
        return organisationRepository.findById(organisation.getId()).orElseThrow();
    }

    protected void assertPersistedOrganisationToMatchAllProperties(Organisation expectedOrganisation) {
        assertOrganisationAllPropertiesEquals(expectedOrganisation, getPersistedOrganisation(expectedOrganisation));
    }

    protected void assertPersistedOrganisationToMatchUpdatableProperties(Organisation expectedOrganisation) {
        assertOrganisationAllUpdatablePropertiesEquals(expectedOrganisation, getPersistedOrganisation(expectedOrganisation));
    }
}
