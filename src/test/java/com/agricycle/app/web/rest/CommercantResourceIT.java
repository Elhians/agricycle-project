package com.agricycle.app.web.rest;

import static com.agricycle.app.domain.CommercantAsserts.*;
import static com.agricycle.app.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.agricycle.app.IntegrationTest;
import com.agricycle.app.domain.Commercant;
import com.agricycle.app.domain.Utilisateur;
import com.agricycle.app.repository.CommercantRepository;
import com.agricycle.app.service.dto.CommercantDTO;
import com.agricycle.app.service.mapper.CommercantMapper;
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
 * Integration tests for the {@link CommercantResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class CommercantResourceIT {

    private static final String DEFAULT_ADRESSE_COMMERCE = "AAAAAAAAAA";
    private static final String UPDATED_ADRESSE_COMMERCE = "BBBBBBBBBB";

    private static final String DEFAULT_MOYEN_PAIEMENT = "AAAAAAAAAA";
    private static final String UPDATED_MOYEN_PAIEMENT = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/commercants";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private CommercantRepository commercantRepository;

    @Autowired
    private CommercantMapper commercantMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restCommercantMockMvc;

    private Commercant commercant;

    private Commercant insertedCommercant;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Commercant createEntity() {
        return new Commercant().adresseCommerce(DEFAULT_ADRESSE_COMMERCE).moyenPaiement(DEFAULT_MOYEN_PAIEMENT);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Commercant createUpdatedEntity() {
        return new Commercant().adresseCommerce(UPDATED_ADRESSE_COMMERCE).moyenPaiement(UPDATED_MOYEN_PAIEMENT);
    }

    @BeforeEach
    void initTest() {
        commercant = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedCommercant != null) {
            commercantRepository.delete(insertedCommercant);
            insertedCommercant = null;
        }
    }

    @Test
    @Transactional
    void createCommercant() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Commercant
        CommercantDTO commercantDTO = commercantMapper.toDto(commercant);
        var returnedCommercantDTO = om.readValue(
            restCommercantMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(commercantDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            CommercantDTO.class
        );

        // Validate the Commercant in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedCommercant = commercantMapper.toEntity(returnedCommercantDTO);
        assertCommercantUpdatableFieldsEquals(returnedCommercant, getPersistedCommercant(returnedCommercant));

        insertedCommercant = returnedCommercant;
    }

    @Test
    @Transactional
    void createCommercantWithExistingId() throws Exception {
        // Create the Commercant with an existing ID
        commercant.setId(1L);
        CommercantDTO commercantDTO = commercantMapper.toDto(commercant);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restCommercantMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(commercantDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Commercant in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllCommercants() throws Exception {
        // Initialize the database
        insertedCommercant = commercantRepository.saveAndFlush(commercant);

        // Get all the commercantList
        restCommercantMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(commercant.getId().intValue())))
            .andExpect(jsonPath("$.[*].adresseCommerce").value(hasItem(DEFAULT_ADRESSE_COMMERCE)))
            .andExpect(jsonPath("$.[*].moyenPaiement").value(hasItem(DEFAULT_MOYEN_PAIEMENT)));
    }

    @Test
    @Transactional
    void getCommercant() throws Exception {
        // Initialize the database
        insertedCommercant = commercantRepository.saveAndFlush(commercant);

        // Get the commercant
        restCommercantMockMvc
            .perform(get(ENTITY_API_URL_ID, commercant.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(commercant.getId().intValue()))
            .andExpect(jsonPath("$.adresseCommerce").value(DEFAULT_ADRESSE_COMMERCE))
            .andExpect(jsonPath("$.moyenPaiement").value(DEFAULT_MOYEN_PAIEMENT));
    }

    @Test
    @Transactional
    void getCommercantsByIdFiltering() throws Exception {
        // Initialize the database
        insertedCommercant = commercantRepository.saveAndFlush(commercant);

        Long id = commercant.getId();

        defaultCommercantFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultCommercantFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultCommercantFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllCommercantsByAdresseCommerceIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedCommercant = commercantRepository.saveAndFlush(commercant);

        // Get all the commercantList where adresseCommerce equals to
        defaultCommercantFiltering(
            "adresseCommerce.equals=" + DEFAULT_ADRESSE_COMMERCE,
            "adresseCommerce.equals=" + UPDATED_ADRESSE_COMMERCE
        );
    }

    @Test
    @Transactional
    void getAllCommercantsByAdresseCommerceIsInShouldWork() throws Exception {
        // Initialize the database
        insertedCommercant = commercantRepository.saveAndFlush(commercant);

        // Get all the commercantList where adresseCommerce in
        defaultCommercantFiltering(
            "adresseCommerce.in=" + DEFAULT_ADRESSE_COMMERCE + "," + UPDATED_ADRESSE_COMMERCE,
            "adresseCommerce.in=" + UPDATED_ADRESSE_COMMERCE
        );
    }

    @Test
    @Transactional
    void getAllCommercantsByAdresseCommerceIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedCommercant = commercantRepository.saveAndFlush(commercant);

        // Get all the commercantList where adresseCommerce is not null
        defaultCommercantFiltering("adresseCommerce.specified=true", "adresseCommerce.specified=false");
    }

    @Test
    @Transactional
    void getAllCommercantsByAdresseCommerceContainsSomething() throws Exception {
        // Initialize the database
        insertedCommercant = commercantRepository.saveAndFlush(commercant);

        // Get all the commercantList where adresseCommerce contains
        defaultCommercantFiltering(
            "adresseCommerce.contains=" + DEFAULT_ADRESSE_COMMERCE,
            "adresseCommerce.contains=" + UPDATED_ADRESSE_COMMERCE
        );
    }

    @Test
    @Transactional
    void getAllCommercantsByAdresseCommerceNotContainsSomething() throws Exception {
        // Initialize the database
        insertedCommercant = commercantRepository.saveAndFlush(commercant);

        // Get all the commercantList where adresseCommerce does not contain
        defaultCommercantFiltering(
            "adresseCommerce.doesNotContain=" + UPDATED_ADRESSE_COMMERCE,
            "adresseCommerce.doesNotContain=" + DEFAULT_ADRESSE_COMMERCE
        );
    }

    @Test
    @Transactional
    void getAllCommercantsByMoyenPaiementIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedCommercant = commercantRepository.saveAndFlush(commercant);

        // Get all the commercantList where moyenPaiement equals to
        defaultCommercantFiltering("moyenPaiement.equals=" + DEFAULT_MOYEN_PAIEMENT, "moyenPaiement.equals=" + UPDATED_MOYEN_PAIEMENT);
    }

    @Test
    @Transactional
    void getAllCommercantsByMoyenPaiementIsInShouldWork() throws Exception {
        // Initialize the database
        insertedCommercant = commercantRepository.saveAndFlush(commercant);

        // Get all the commercantList where moyenPaiement in
        defaultCommercantFiltering(
            "moyenPaiement.in=" + DEFAULT_MOYEN_PAIEMENT + "," + UPDATED_MOYEN_PAIEMENT,
            "moyenPaiement.in=" + UPDATED_MOYEN_PAIEMENT
        );
    }

    @Test
    @Transactional
    void getAllCommercantsByMoyenPaiementIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedCommercant = commercantRepository.saveAndFlush(commercant);

        // Get all the commercantList where moyenPaiement is not null
        defaultCommercantFiltering("moyenPaiement.specified=true", "moyenPaiement.specified=false");
    }

    @Test
    @Transactional
    void getAllCommercantsByMoyenPaiementContainsSomething() throws Exception {
        // Initialize the database
        insertedCommercant = commercantRepository.saveAndFlush(commercant);

        // Get all the commercantList where moyenPaiement contains
        defaultCommercantFiltering("moyenPaiement.contains=" + DEFAULT_MOYEN_PAIEMENT, "moyenPaiement.contains=" + UPDATED_MOYEN_PAIEMENT);
    }

    @Test
    @Transactional
    void getAllCommercantsByMoyenPaiementNotContainsSomething() throws Exception {
        // Initialize the database
        insertedCommercant = commercantRepository.saveAndFlush(commercant);

        // Get all the commercantList where moyenPaiement does not contain
        defaultCommercantFiltering(
            "moyenPaiement.doesNotContain=" + UPDATED_MOYEN_PAIEMENT,
            "moyenPaiement.doesNotContain=" + DEFAULT_MOYEN_PAIEMENT
        );
    }

    @Test
    @Transactional
    void getAllCommercantsByUtilisateurIsEqualToSomething() throws Exception {
        Utilisateur utilisateur;
        if (TestUtil.findAll(em, Utilisateur.class).isEmpty()) {
            commercantRepository.saveAndFlush(commercant);
            utilisateur = UtilisateurResourceIT.createEntity();
        } else {
            utilisateur = TestUtil.findAll(em, Utilisateur.class).get(0);
        }
        em.persist(utilisateur);
        em.flush();
        commercant.setUtilisateur(utilisateur);
        commercantRepository.saveAndFlush(commercant);
        Long utilisateurId = utilisateur.getId();
        // Get all the commercantList where utilisateur equals to utilisateurId
        defaultCommercantShouldBeFound("utilisateurId.equals=" + utilisateurId);

        // Get all the commercantList where utilisateur equals to (utilisateurId + 1)
        defaultCommercantShouldNotBeFound("utilisateurId.equals=" + (utilisateurId + 1));
    }

    private void defaultCommercantFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultCommercantShouldBeFound(shouldBeFound);
        defaultCommercantShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultCommercantShouldBeFound(String filter) throws Exception {
        restCommercantMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(commercant.getId().intValue())))
            .andExpect(jsonPath("$.[*].adresseCommerce").value(hasItem(DEFAULT_ADRESSE_COMMERCE)))
            .andExpect(jsonPath("$.[*].moyenPaiement").value(hasItem(DEFAULT_MOYEN_PAIEMENT)));

        // Check, that the count call also returns 1
        restCommercantMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultCommercantShouldNotBeFound(String filter) throws Exception {
        restCommercantMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restCommercantMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingCommercant() throws Exception {
        // Get the commercant
        restCommercantMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingCommercant() throws Exception {
        // Initialize the database
        insertedCommercant = commercantRepository.saveAndFlush(commercant);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the commercant
        Commercant updatedCommercant = commercantRepository.findById(commercant.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedCommercant are not directly saved in db
        em.detach(updatedCommercant);
        updatedCommercant.adresseCommerce(UPDATED_ADRESSE_COMMERCE).moyenPaiement(UPDATED_MOYEN_PAIEMENT);
        CommercantDTO commercantDTO = commercantMapper.toDto(updatedCommercant);

        restCommercantMockMvc
            .perform(
                put(ENTITY_API_URL_ID, commercantDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(commercantDTO))
            )
            .andExpect(status().isOk());

        // Validate the Commercant in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedCommercantToMatchAllProperties(updatedCommercant);
    }

    @Test
    @Transactional
    void putNonExistingCommercant() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        commercant.setId(longCount.incrementAndGet());

        // Create the Commercant
        CommercantDTO commercantDTO = commercantMapper.toDto(commercant);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCommercantMockMvc
            .perform(
                put(ENTITY_API_URL_ID, commercantDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(commercantDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Commercant in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchCommercant() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        commercant.setId(longCount.incrementAndGet());

        // Create the Commercant
        CommercantDTO commercantDTO = commercantMapper.toDto(commercant);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCommercantMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(commercantDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Commercant in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamCommercant() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        commercant.setId(longCount.incrementAndGet());

        // Create the Commercant
        CommercantDTO commercantDTO = commercantMapper.toDto(commercant);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCommercantMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(commercantDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Commercant in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateCommercantWithPatch() throws Exception {
        // Initialize the database
        insertedCommercant = commercantRepository.saveAndFlush(commercant);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the commercant using partial update
        Commercant partialUpdatedCommercant = new Commercant();
        partialUpdatedCommercant.setId(commercant.getId());

        partialUpdatedCommercant.adresseCommerce(UPDATED_ADRESSE_COMMERCE).moyenPaiement(UPDATED_MOYEN_PAIEMENT);

        restCommercantMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCommercant.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedCommercant))
            )
            .andExpect(status().isOk());

        // Validate the Commercant in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertCommercantUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedCommercant, commercant),
            getPersistedCommercant(commercant)
        );
    }

    @Test
    @Transactional
    void fullUpdateCommercantWithPatch() throws Exception {
        // Initialize the database
        insertedCommercant = commercantRepository.saveAndFlush(commercant);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the commercant using partial update
        Commercant partialUpdatedCommercant = new Commercant();
        partialUpdatedCommercant.setId(commercant.getId());

        partialUpdatedCommercant.adresseCommerce(UPDATED_ADRESSE_COMMERCE).moyenPaiement(UPDATED_MOYEN_PAIEMENT);

        restCommercantMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCommercant.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedCommercant))
            )
            .andExpect(status().isOk());

        // Validate the Commercant in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertCommercantUpdatableFieldsEquals(partialUpdatedCommercant, getPersistedCommercant(partialUpdatedCommercant));
    }

    @Test
    @Transactional
    void patchNonExistingCommercant() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        commercant.setId(longCount.incrementAndGet());

        // Create the Commercant
        CommercantDTO commercantDTO = commercantMapper.toDto(commercant);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCommercantMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, commercantDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(commercantDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Commercant in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchCommercant() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        commercant.setId(longCount.incrementAndGet());

        // Create the Commercant
        CommercantDTO commercantDTO = commercantMapper.toDto(commercant);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCommercantMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(commercantDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Commercant in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamCommercant() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        commercant.setId(longCount.incrementAndGet());

        // Create the Commercant
        CommercantDTO commercantDTO = commercantMapper.toDto(commercant);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCommercantMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(commercantDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Commercant in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteCommercant() throws Exception {
        // Initialize the database
        insertedCommercant = commercantRepository.saveAndFlush(commercant);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the commercant
        restCommercantMockMvc
            .perform(delete(ENTITY_API_URL_ID, commercant.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return commercantRepository.count();
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

    protected Commercant getPersistedCommercant(Commercant commercant) {
        return commercantRepository.findById(commercant.getId()).orElseThrow();
    }

    protected void assertPersistedCommercantToMatchAllProperties(Commercant expectedCommercant) {
        assertCommercantAllPropertiesEquals(expectedCommercant, getPersistedCommercant(expectedCommercant));
    }

    protected void assertPersistedCommercantToMatchUpdatableProperties(Commercant expectedCommercant) {
        assertCommercantAllUpdatablePropertiesEquals(expectedCommercant, getPersistedCommercant(expectedCommercant));
    }
}
