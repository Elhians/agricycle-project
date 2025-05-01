package com.agricycle.app.web.rest;

import static com.agricycle.app.domain.TransporteurAsserts.*;
import static com.agricycle.app.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.agricycle.app.IntegrationTest;
import com.agricycle.app.domain.Transporteur;
import com.agricycle.app.domain.Utilisateur;
import com.agricycle.app.domain.enumeration.TypeVehicule;
import com.agricycle.app.repository.TransporteurRepository;
import com.agricycle.app.service.dto.TransporteurDTO;
import com.agricycle.app.service.mapper.TransporteurMapper;
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
 * Integration tests for the {@link TransporteurResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class TransporteurResourceIT {

    private static final String DEFAULT_ZONE_COUVERTURE = "AAAAAAAAAA";
    private static final String UPDATED_ZONE_COUVERTURE = "BBBBBBBBBB";

    private static final TypeVehicule DEFAULT_TYPE_VEHICULE = TypeVehicule.VOITURE;
    private static final TypeVehicule UPDATED_TYPE_VEHICULE = TypeVehicule.CAMION;

    private static final Float DEFAULT_CAPACITE = 1F;
    private static final Float UPDATED_CAPACITE = 2F;
    private static final Float SMALLER_CAPACITE = 1F - 1F;

    private static final String ENTITY_API_URL = "/api/transporteurs";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private TransporteurRepository transporteurRepository;

    @Autowired
    private TransporteurMapper transporteurMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restTransporteurMockMvc;

    private Transporteur transporteur;

    private Transporteur insertedTransporteur;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Transporteur createEntity() {
        return new Transporteur().zoneCouverture(DEFAULT_ZONE_COUVERTURE).typeVehicule(DEFAULT_TYPE_VEHICULE).capacite(DEFAULT_CAPACITE);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Transporteur createUpdatedEntity() {
        return new Transporteur().zoneCouverture(UPDATED_ZONE_COUVERTURE).typeVehicule(UPDATED_TYPE_VEHICULE).capacite(UPDATED_CAPACITE);
    }

    @BeforeEach
    void initTest() {
        transporteur = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedTransporteur != null) {
            transporteurRepository.delete(insertedTransporteur);
            insertedTransporteur = null;
        }
    }

    @Test
    @Transactional
    void createTransporteur() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Transporteur
        TransporteurDTO transporteurDTO = transporteurMapper.toDto(transporteur);
        var returnedTransporteurDTO = om.readValue(
            restTransporteurMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(transporteurDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            TransporteurDTO.class
        );

        // Validate the Transporteur in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedTransporteur = transporteurMapper.toEntity(returnedTransporteurDTO);
        assertTransporteurUpdatableFieldsEquals(returnedTransporteur, getPersistedTransporteur(returnedTransporteur));

        insertedTransporteur = returnedTransporteur;
    }

    @Test
    @Transactional
    void createTransporteurWithExistingId() throws Exception {
        // Create the Transporteur with an existing ID
        transporteur.setId(1L);
        TransporteurDTO transporteurDTO = transporteurMapper.toDto(transporteur);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restTransporteurMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(transporteurDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Transporteur in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllTransporteurs() throws Exception {
        // Initialize the database
        insertedTransporteur = transporteurRepository.saveAndFlush(transporteur);

        // Get all the transporteurList
        restTransporteurMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(transporteur.getId().intValue())))
            .andExpect(jsonPath("$.[*].zoneCouverture").value(hasItem(DEFAULT_ZONE_COUVERTURE)))
            .andExpect(jsonPath("$.[*].typeVehicule").value(hasItem(DEFAULT_TYPE_VEHICULE.toString())))
            .andExpect(jsonPath("$.[*].capacite").value(hasItem(DEFAULT_CAPACITE.doubleValue())));
    }

    @Test
    @Transactional
    void getTransporteur() throws Exception {
        // Initialize the database
        insertedTransporteur = transporteurRepository.saveAndFlush(transporteur);

        // Get the transporteur
        restTransporteurMockMvc
            .perform(get(ENTITY_API_URL_ID, transporteur.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(transporteur.getId().intValue()))
            .andExpect(jsonPath("$.zoneCouverture").value(DEFAULT_ZONE_COUVERTURE))
            .andExpect(jsonPath("$.typeVehicule").value(DEFAULT_TYPE_VEHICULE.toString()))
            .andExpect(jsonPath("$.capacite").value(DEFAULT_CAPACITE.doubleValue()));
    }

    @Test
    @Transactional
    void getTransporteursByIdFiltering() throws Exception {
        // Initialize the database
        insertedTransporteur = transporteurRepository.saveAndFlush(transporteur);

        Long id = transporteur.getId();

        defaultTransporteurFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultTransporteurFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultTransporteurFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllTransporteursByZoneCouvertureIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedTransporteur = transporteurRepository.saveAndFlush(transporteur);

        // Get all the transporteurList where zoneCouverture equals to
        defaultTransporteurFiltering(
            "zoneCouverture.equals=" + DEFAULT_ZONE_COUVERTURE,
            "zoneCouverture.equals=" + UPDATED_ZONE_COUVERTURE
        );
    }

    @Test
    @Transactional
    void getAllTransporteursByZoneCouvertureIsInShouldWork() throws Exception {
        // Initialize the database
        insertedTransporteur = transporteurRepository.saveAndFlush(transporteur);

        // Get all the transporteurList where zoneCouverture in
        defaultTransporteurFiltering(
            "zoneCouverture.in=" + DEFAULT_ZONE_COUVERTURE + "," + UPDATED_ZONE_COUVERTURE,
            "zoneCouverture.in=" + UPDATED_ZONE_COUVERTURE
        );
    }

    @Test
    @Transactional
    void getAllTransporteursByZoneCouvertureIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedTransporteur = transporteurRepository.saveAndFlush(transporteur);

        // Get all the transporteurList where zoneCouverture is not null
        defaultTransporteurFiltering("zoneCouverture.specified=true", "zoneCouverture.specified=false");
    }

    @Test
    @Transactional
    void getAllTransporteursByZoneCouvertureContainsSomething() throws Exception {
        // Initialize the database
        insertedTransporteur = transporteurRepository.saveAndFlush(transporteur);

        // Get all the transporteurList where zoneCouverture contains
        defaultTransporteurFiltering(
            "zoneCouverture.contains=" + DEFAULT_ZONE_COUVERTURE,
            "zoneCouverture.contains=" + UPDATED_ZONE_COUVERTURE
        );
    }

    @Test
    @Transactional
    void getAllTransporteursByZoneCouvertureNotContainsSomething() throws Exception {
        // Initialize the database
        insertedTransporteur = transporteurRepository.saveAndFlush(transporteur);

        // Get all the transporteurList where zoneCouverture does not contain
        defaultTransporteurFiltering(
            "zoneCouverture.doesNotContain=" + UPDATED_ZONE_COUVERTURE,
            "zoneCouverture.doesNotContain=" + DEFAULT_ZONE_COUVERTURE
        );
    }

    @Test
    @Transactional
    void getAllTransporteursByTypeVehiculeIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedTransporteur = transporteurRepository.saveAndFlush(transporteur);

        // Get all the transporteurList where typeVehicule equals to
        defaultTransporteurFiltering("typeVehicule.equals=" + DEFAULT_TYPE_VEHICULE, "typeVehicule.equals=" + UPDATED_TYPE_VEHICULE);
    }

    @Test
    @Transactional
    void getAllTransporteursByTypeVehiculeIsInShouldWork() throws Exception {
        // Initialize the database
        insertedTransporteur = transporteurRepository.saveAndFlush(transporteur);

        // Get all the transporteurList where typeVehicule in
        defaultTransporteurFiltering(
            "typeVehicule.in=" + DEFAULT_TYPE_VEHICULE + "," + UPDATED_TYPE_VEHICULE,
            "typeVehicule.in=" + UPDATED_TYPE_VEHICULE
        );
    }

    @Test
    @Transactional
    void getAllTransporteursByTypeVehiculeIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedTransporteur = transporteurRepository.saveAndFlush(transporteur);

        // Get all the transporteurList where typeVehicule is not null
        defaultTransporteurFiltering("typeVehicule.specified=true", "typeVehicule.specified=false");
    }

    @Test
    @Transactional
    void getAllTransporteursByCapaciteIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedTransporteur = transporteurRepository.saveAndFlush(transporteur);

        // Get all the transporteurList where capacite equals to
        defaultTransporteurFiltering("capacite.equals=" + DEFAULT_CAPACITE, "capacite.equals=" + UPDATED_CAPACITE);
    }

    @Test
    @Transactional
    void getAllTransporteursByCapaciteIsInShouldWork() throws Exception {
        // Initialize the database
        insertedTransporteur = transporteurRepository.saveAndFlush(transporteur);

        // Get all the transporteurList where capacite in
        defaultTransporteurFiltering("capacite.in=" + DEFAULT_CAPACITE + "," + UPDATED_CAPACITE, "capacite.in=" + UPDATED_CAPACITE);
    }

    @Test
    @Transactional
    void getAllTransporteursByCapaciteIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedTransporteur = transporteurRepository.saveAndFlush(transporteur);

        // Get all the transporteurList where capacite is not null
        defaultTransporteurFiltering("capacite.specified=true", "capacite.specified=false");
    }

    @Test
    @Transactional
    void getAllTransporteursByCapaciteIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedTransporteur = transporteurRepository.saveAndFlush(transporteur);

        // Get all the transporteurList where capacite is greater than or equal to
        defaultTransporteurFiltering("capacite.greaterThanOrEqual=" + DEFAULT_CAPACITE, "capacite.greaterThanOrEqual=" + UPDATED_CAPACITE);
    }

    @Test
    @Transactional
    void getAllTransporteursByCapaciteIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedTransporteur = transporteurRepository.saveAndFlush(transporteur);

        // Get all the transporteurList where capacite is less than or equal to
        defaultTransporteurFiltering("capacite.lessThanOrEqual=" + DEFAULT_CAPACITE, "capacite.lessThanOrEqual=" + SMALLER_CAPACITE);
    }

    @Test
    @Transactional
    void getAllTransporteursByCapaciteIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedTransporteur = transporteurRepository.saveAndFlush(transporteur);

        // Get all the transporteurList where capacite is less than
        defaultTransporteurFiltering("capacite.lessThan=" + UPDATED_CAPACITE, "capacite.lessThan=" + DEFAULT_CAPACITE);
    }

    @Test
    @Transactional
    void getAllTransporteursByCapaciteIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedTransporteur = transporteurRepository.saveAndFlush(transporteur);

        // Get all the transporteurList where capacite is greater than
        defaultTransporteurFiltering("capacite.greaterThan=" + SMALLER_CAPACITE, "capacite.greaterThan=" + DEFAULT_CAPACITE);
    }

    @Test
    @Transactional
    void getAllTransporteursByUtilisateurIsEqualToSomething() throws Exception {
        Utilisateur utilisateur;
        if (TestUtil.findAll(em, Utilisateur.class).isEmpty()) {
            transporteurRepository.saveAndFlush(transporteur);
            utilisateur = UtilisateurResourceIT.createEntity();
        } else {
            utilisateur = TestUtil.findAll(em, Utilisateur.class).get(0);
        }
        em.persist(utilisateur);
        em.flush();
        transporteur.setUtilisateur(utilisateur);
        transporteurRepository.saveAndFlush(transporteur);
        Long utilisateurId = utilisateur.getId();
        // Get all the transporteurList where utilisateur equals to utilisateurId
        defaultTransporteurShouldBeFound("utilisateurId.equals=" + utilisateurId);

        // Get all the transporteurList where utilisateur equals to (utilisateurId + 1)
        defaultTransporteurShouldNotBeFound("utilisateurId.equals=" + (utilisateurId + 1));
    }

    private void defaultTransporteurFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultTransporteurShouldBeFound(shouldBeFound);
        defaultTransporteurShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultTransporteurShouldBeFound(String filter) throws Exception {
        restTransporteurMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(transporteur.getId().intValue())))
            .andExpect(jsonPath("$.[*].zoneCouverture").value(hasItem(DEFAULT_ZONE_COUVERTURE)))
            .andExpect(jsonPath("$.[*].typeVehicule").value(hasItem(DEFAULT_TYPE_VEHICULE.toString())))
            .andExpect(jsonPath("$.[*].capacite").value(hasItem(DEFAULT_CAPACITE.doubleValue())));

        // Check, that the count call also returns 1
        restTransporteurMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultTransporteurShouldNotBeFound(String filter) throws Exception {
        restTransporteurMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restTransporteurMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingTransporteur() throws Exception {
        // Get the transporteur
        restTransporteurMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingTransporteur() throws Exception {
        // Initialize the database
        insertedTransporteur = transporteurRepository.saveAndFlush(transporteur);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the transporteur
        Transporteur updatedTransporteur = transporteurRepository.findById(transporteur.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedTransporteur are not directly saved in db
        em.detach(updatedTransporteur);
        updatedTransporteur.zoneCouverture(UPDATED_ZONE_COUVERTURE).typeVehicule(UPDATED_TYPE_VEHICULE).capacite(UPDATED_CAPACITE);
        TransporteurDTO transporteurDTO = transporteurMapper.toDto(updatedTransporteur);

        restTransporteurMockMvc
            .perform(
                put(ENTITY_API_URL_ID, transporteurDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(transporteurDTO))
            )
            .andExpect(status().isOk());

        // Validate the Transporteur in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedTransporteurToMatchAllProperties(updatedTransporteur);
    }

    @Test
    @Transactional
    void putNonExistingTransporteur() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        transporteur.setId(longCount.incrementAndGet());

        // Create the Transporteur
        TransporteurDTO transporteurDTO = transporteurMapper.toDto(transporteur);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTransporteurMockMvc
            .perform(
                put(ENTITY_API_URL_ID, transporteurDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(transporteurDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Transporteur in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchTransporteur() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        transporteur.setId(longCount.incrementAndGet());

        // Create the Transporteur
        TransporteurDTO transporteurDTO = transporteurMapper.toDto(transporteur);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTransporteurMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(transporteurDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Transporteur in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamTransporteur() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        transporteur.setId(longCount.incrementAndGet());

        // Create the Transporteur
        TransporteurDTO transporteurDTO = transporteurMapper.toDto(transporteur);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTransporteurMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(transporteurDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Transporteur in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateTransporteurWithPatch() throws Exception {
        // Initialize the database
        insertedTransporteur = transporteurRepository.saveAndFlush(transporteur);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the transporteur using partial update
        Transporteur partialUpdatedTransporteur = new Transporteur();
        partialUpdatedTransporteur.setId(transporteur.getId());

        partialUpdatedTransporteur.zoneCouverture(UPDATED_ZONE_COUVERTURE).typeVehicule(UPDATED_TYPE_VEHICULE);

        restTransporteurMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTransporteur.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedTransporteur))
            )
            .andExpect(status().isOk());

        // Validate the Transporteur in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertTransporteurUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedTransporteur, transporteur),
            getPersistedTransporteur(transporteur)
        );
    }

    @Test
    @Transactional
    void fullUpdateTransporteurWithPatch() throws Exception {
        // Initialize the database
        insertedTransporteur = transporteurRepository.saveAndFlush(transporteur);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the transporteur using partial update
        Transporteur partialUpdatedTransporteur = new Transporteur();
        partialUpdatedTransporteur.setId(transporteur.getId());

        partialUpdatedTransporteur.zoneCouverture(UPDATED_ZONE_COUVERTURE).typeVehicule(UPDATED_TYPE_VEHICULE).capacite(UPDATED_CAPACITE);

        restTransporteurMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTransporteur.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedTransporteur))
            )
            .andExpect(status().isOk());

        // Validate the Transporteur in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertTransporteurUpdatableFieldsEquals(partialUpdatedTransporteur, getPersistedTransporteur(partialUpdatedTransporteur));
    }

    @Test
    @Transactional
    void patchNonExistingTransporteur() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        transporteur.setId(longCount.incrementAndGet());

        // Create the Transporteur
        TransporteurDTO transporteurDTO = transporteurMapper.toDto(transporteur);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTransporteurMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, transporteurDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(transporteurDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Transporteur in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchTransporteur() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        transporteur.setId(longCount.incrementAndGet());

        // Create the Transporteur
        TransporteurDTO transporteurDTO = transporteurMapper.toDto(transporteur);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTransporteurMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(transporteurDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Transporteur in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamTransporteur() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        transporteur.setId(longCount.incrementAndGet());

        // Create the Transporteur
        TransporteurDTO transporteurDTO = transporteurMapper.toDto(transporteur);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTransporteurMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(transporteurDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Transporteur in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteTransporteur() throws Exception {
        // Initialize the database
        insertedTransporteur = transporteurRepository.saveAndFlush(transporteur);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the transporteur
        restTransporteurMockMvc
            .perform(delete(ENTITY_API_URL_ID, transporteur.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return transporteurRepository.count();
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

    protected Transporteur getPersistedTransporteur(Transporteur transporteur) {
        return transporteurRepository.findById(transporteur.getId()).orElseThrow();
    }

    protected void assertPersistedTransporteurToMatchAllProperties(Transporteur expectedTransporteur) {
        assertTransporteurAllPropertiesEquals(expectedTransporteur, getPersistedTransporteur(expectedTransporteur));
    }

    protected void assertPersistedTransporteurToMatchUpdatableProperties(Transporteur expectedTransporteur) {
        assertTransporteurAllUpdatablePropertiesEquals(expectedTransporteur, getPersistedTransporteur(expectedTransporteur));
    }
}
