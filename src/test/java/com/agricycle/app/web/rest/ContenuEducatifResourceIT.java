package com.agricycle.app.web.rest;

import static com.agricycle.app.domain.ContenuEducatifAsserts.*;
import static com.agricycle.app.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.agricycle.app.IntegrationTest;
import com.agricycle.app.domain.ContenuEducatif;
import com.agricycle.app.domain.Utilisateur;
import com.agricycle.app.domain.enumeration.TypeMedia;
import com.agricycle.app.repository.ContenuEducatifRepository;
import com.agricycle.app.service.dto.ContenuEducatifDTO;
import com.agricycle.app.service.mapper.ContenuEducatifMapper;
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
 * Integration tests for the {@link ContenuEducatifResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ContenuEducatifResourceIT {

    private static final String DEFAULT_TITRE = "AAAAAAAAAA";
    private static final String UPDATED_TITRE = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final TypeMedia DEFAULT_TYPE_MEDIA = TypeMedia.TEXTE;
    private static final TypeMedia UPDATED_TYPE_MEDIA = TypeMedia.IMAGE;

    private static final String DEFAULT_URL = "AAAAAAAAAA";
    private static final String UPDATED_URL = "BBBBBBBBBB";

    private static final Instant DEFAULT_DATE_PUBLICATION = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_DATE_PUBLICATION = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/contenu-educatifs";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private ContenuEducatifRepository contenuEducatifRepository;

    @Autowired
    private ContenuEducatifMapper contenuEducatifMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restContenuEducatifMockMvc;

    private ContenuEducatif contenuEducatif;

    private ContenuEducatif insertedContenuEducatif;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ContenuEducatif createEntity() {
        return new ContenuEducatif()
            .titre(DEFAULT_TITRE)
            .description(DEFAULT_DESCRIPTION)
            .typeMedia(DEFAULT_TYPE_MEDIA)
            .url(DEFAULT_URL)
            .datePublication(DEFAULT_DATE_PUBLICATION);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ContenuEducatif createUpdatedEntity() {
        return new ContenuEducatif()
            .titre(UPDATED_TITRE)
            .description(UPDATED_DESCRIPTION)
            .typeMedia(UPDATED_TYPE_MEDIA)
            .url(UPDATED_URL)
            .datePublication(UPDATED_DATE_PUBLICATION);
    }

    @BeforeEach
    void initTest() {
        contenuEducatif = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedContenuEducatif != null) {
            contenuEducatifRepository.delete(insertedContenuEducatif);
            insertedContenuEducatif = null;
        }
    }

    @Test
    @Transactional
    void createContenuEducatif() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the ContenuEducatif
        ContenuEducatifDTO contenuEducatifDTO = contenuEducatifMapper.toDto(contenuEducatif);
        var returnedContenuEducatifDTO = om.readValue(
            restContenuEducatifMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(contenuEducatifDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            ContenuEducatifDTO.class
        );

        // Validate the ContenuEducatif in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedContenuEducatif = contenuEducatifMapper.toEntity(returnedContenuEducatifDTO);
        assertContenuEducatifUpdatableFieldsEquals(returnedContenuEducatif, getPersistedContenuEducatif(returnedContenuEducatif));

        insertedContenuEducatif = returnedContenuEducatif;
    }

    @Test
    @Transactional
    void createContenuEducatifWithExistingId() throws Exception {
        // Create the ContenuEducatif with an existing ID
        contenuEducatif.setId(1L);
        ContenuEducatifDTO contenuEducatifDTO = contenuEducatifMapper.toDto(contenuEducatif);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restContenuEducatifMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(contenuEducatifDTO)))
            .andExpect(status().isBadRequest());

        // Validate the ContenuEducatif in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkTitreIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        contenuEducatif.setTitre(null);

        // Create the ContenuEducatif, which fails.
        ContenuEducatifDTO contenuEducatifDTO = contenuEducatifMapper.toDto(contenuEducatif);

        restContenuEducatifMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(contenuEducatifDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkTypeMediaIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        contenuEducatif.setTypeMedia(null);

        // Create the ContenuEducatif, which fails.
        ContenuEducatifDTO contenuEducatifDTO = contenuEducatifMapper.toDto(contenuEducatif);

        restContenuEducatifMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(contenuEducatifDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkUrlIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        contenuEducatif.setUrl(null);

        // Create the ContenuEducatif, which fails.
        ContenuEducatifDTO contenuEducatifDTO = contenuEducatifMapper.toDto(contenuEducatif);

        restContenuEducatifMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(contenuEducatifDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllContenuEducatifs() throws Exception {
        // Initialize the database
        insertedContenuEducatif = contenuEducatifRepository.saveAndFlush(contenuEducatif);

        // Get all the contenuEducatifList
        restContenuEducatifMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(contenuEducatif.getId().intValue())))
            .andExpect(jsonPath("$.[*].titre").value(hasItem(DEFAULT_TITRE)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].typeMedia").value(hasItem(DEFAULT_TYPE_MEDIA.toString())))
            .andExpect(jsonPath("$.[*].url").value(hasItem(DEFAULT_URL)))
            .andExpect(jsonPath("$.[*].datePublication").value(hasItem(DEFAULT_DATE_PUBLICATION.toString())));
    }

    @Test
    @Transactional
    void getContenuEducatif() throws Exception {
        // Initialize the database
        insertedContenuEducatif = contenuEducatifRepository.saveAndFlush(contenuEducatif);

        // Get the contenuEducatif
        restContenuEducatifMockMvc
            .perform(get(ENTITY_API_URL_ID, contenuEducatif.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(contenuEducatif.getId().intValue()))
            .andExpect(jsonPath("$.titre").value(DEFAULT_TITRE))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.typeMedia").value(DEFAULT_TYPE_MEDIA.toString()))
            .andExpect(jsonPath("$.url").value(DEFAULT_URL))
            .andExpect(jsonPath("$.datePublication").value(DEFAULT_DATE_PUBLICATION.toString()));
    }

    @Test
    @Transactional
    void getContenuEducatifsByIdFiltering() throws Exception {
        // Initialize the database
        insertedContenuEducatif = contenuEducatifRepository.saveAndFlush(contenuEducatif);

        Long id = contenuEducatif.getId();

        defaultContenuEducatifFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultContenuEducatifFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultContenuEducatifFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllContenuEducatifsByTitreIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedContenuEducatif = contenuEducatifRepository.saveAndFlush(contenuEducatif);

        // Get all the contenuEducatifList where titre equals to
        defaultContenuEducatifFiltering("titre.equals=" + DEFAULT_TITRE, "titre.equals=" + UPDATED_TITRE);
    }

    @Test
    @Transactional
    void getAllContenuEducatifsByTitreIsInShouldWork() throws Exception {
        // Initialize the database
        insertedContenuEducatif = contenuEducatifRepository.saveAndFlush(contenuEducatif);

        // Get all the contenuEducatifList where titre in
        defaultContenuEducatifFiltering("titre.in=" + DEFAULT_TITRE + "," + UPDATED_TITRE, "titre.in=" + UPDATED_TITRE);
    }

    @Test
    @Transactional
    void getAllContenuEducatifsByTitreIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedContenuEducatif = contenuEducatifRepository.saveAndFlush(contenuEducatif);

        // Get all the contenuEducatifList where titre is not null
        defaultContenuEducatifFiltering("titre.specified=true", "titre.specified=false");
    }

    @Test
    @Transactional
    void getAllContenuEducatifsByTitreContainsSomething() throws Exception {
        // Initialize the database
        insertedContenuEducatif = contenuEducatifRepository.saveAndFlush(contenuEducatif);

        // Get all the contenuEducatifList where titre contains
        defaultContenuEducatifFiltering("titre.contains=" + DEFAULT_TITRE, "titre.contains=" + UPDATED_TITRE);
    }

    @Test
    @Transactional
    void getAllContenuEducatifsByTitreNotContainsSomething() throws Exception {
        // Initialize the database
        insertedContenuEducatif = contenuEducatifRepository.saveAndFlush(contenuEducatif);

        // Get all the contenuEducatifList where titre does not contain
        defaultContenuEducatifFiltering("titre.doesNotContain=" + UPDATED_TITRE, "titre.doesNotContain=" + DEFAULT_TITRE);
    }

    @Test
    @Transactional
    void getAllContenuEducatifsByDescriptionIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedContenuEducatif = contenuEducatifRepository.saveAndFlush(contenuEducatif);

        // Get all the contenuEducatifList where description equals to
        defaultContenuEducatifFiltering("description.equals=" + DEFAULT_DESCRIPTION, "description.equals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllContenuEducatifsByDescriptionIsInShouldWork() throws Exception {
        // Initialize the database
        insertedContenuEducatif = contenuEducatifRepository.saveAndFlush(contenuEducatif);

        // Get all the contenuEducatifList where description in
        defaultContenuEducatifFiltering(
            "description.in=" + DEFAULT_DESCRIPTION + "," + UPDATED_DESCRIPTION,
            "description.in=" + UPDATED_DESCRIPTION
        );
    }

    @Test
    @Transactional
    void getAllContenuEducatifsByDescriptionIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedContenuEducatif = contenuEducatifRepository.saveAndFlush(contenuEducatif);

        // Get all the contenuEducatifList where description is not null
        defaultContenuEducatifFiltering("description.specified=true", "description.specified=false");
    }

    @Test
    @Transactional
    void getAllContenuEducatifsByDescriptionContainsSomething() throws Exception {
        // Initialize the database
        insertedContenuEducatif = contenuEducatifRepository.saveAndFlush(contenuEducatif);

        // Get all the contenuEducatifList where description contains
        defaultContenuEducatifFiltering("description.contains=" + DEFAULT_DESCRIPTION, "description.contains=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllContenuEducatifsByDescriptionNotContainsSomething() throws Exception {
        // Initialize the database
        insertedContenuEducatif = contenuEducatifRepository.saveAndFlush(contenuEducatif);

        // Get all the contenuEducatifList where description does not contain
        defaultContenuEducatifFiltering(
            "description.doesNotContain=" + UPDATED_DESCRIPTION,
            "description.doesNotContain=" + DEFAULT_DESCRIPTION
        );
    }

    @Test
    @Transactional
    void getAllContenuEducatifsByTypeMediaIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedContenuEducatif = contenuEducatifRepository.saveAndFlush(contenuEducatif);

        // Get all the contenuEducatifList where typeMedia equals to
        defaultContenuEducatifFiltering("typeMedia.equals=" + DEFAULT_TYPE_MEDIA, "typeMedia.equals=" + UPDATED_TYPE_MEDIA);
    }

    @Test
    @Transactional
    void getAllContenuEducatifsByTypeMediaIsInShouldWork() throws Exception {
        // Initialize the database
        insertedContenuEducatif = contenuEducatifRepository.saveAndFlush(contenuEducatif);

        // Get all the contenuEducatifList where typeMedia in
        defaultContenuEducatifFiltering(
            "typeMedia.in=" + DEFAULT_TYPE_MEDIA + "," + UPDATED_TYPE_MEDIA,
            "typeMedia.in=" + UPDATED_TYPE_MEDIA
        );
    }

    @Test
    @Transactional
    void getAllContenuEducatifsByTypeMediaIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedContenuEducatif = contenuEducatifRepository.saveAndFlush(contenuEducatif);

        // Get all the contenuEducatifList where typeMedia is not null
        defaultContenuEducatifFiltering("typeMedia.specified=true", "typeMedia.specified=false");
    }

    @Test
    @Transactional
    void getAllContenuEducatifsByUrlIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedContenuEducatif = contenuEducatifRepository.saveAndFlush(contenuEducatif);

        // Get all the contenuEducatifList where url equals to
        defaultContenuEducatifFiltering("url.equals=" + DEFAULT_URL, "url.equals=" + UPDATED_URL);
    }

    @Test
    @Transactional
    void getAllContenuEducatifsByUrlIsInShouldWork() throws Exception {
        // Initialize the database
        insertedContenuEducatif = contenuEducatifRepository.saveAndFlush(contenuEducatif);

        // Get all the contenuEducatifList where url in
        defaultContenuEducatifFiltering("url.in=" + DEFAULT_URL + "," + UPDATED_URL, "url.in=" + UPDATED_URL);
    }

    @Test
    @Transactional
    void getAllContenuEducatifsByUrlIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedContenuEducatif = contenuEducatifRepository.saveAndFlush(contenuEducatif);

        // Get all the contenuEducatifList where url is not null
        defaultContenuEducatifFiltering("url.specified=true", "url.specified=false");
    }

    @Test
    @Transactional
    void getAllContenuEducatifsByUrlContainsSomething() throws Exception {
        // Initialize the database
        insertedContenuEducatif = contenuEducatifRepository.saveAndFlush(contenuEducatif);

        // Get all the contenuEducatifList where url contains
        defaultContenuEducatifFiltering("url.contains=" + DEFAULT_URL, "url.contains=" + UPDATED_URL);
    }

    @Test
    @Transactional
    void getAllContenuEducatifsByUrlNotContainsSomething() throws Exception {
        // Initialize the database
        insertedContenuEducatif = contenuEducatifRepository.saveAndFlush(contenuEducatif);

        // Get all the contenuEducatifList where url does not contain
        defaultContenuEducatifFiltering("url.doesNotContain=" + UPDATED_URL, "url.doesNotContain=" + DEFAULT_URL);
    }

    @Test
    @Transactional
    void getAllContenuEducatifsByDatePublicationIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedContenuEducatif = contenuEducatifRepository.saveAndFlush(contenuEducatif);

        // Get all the contenuEducatifList where datePublication equals to
        defaultContenuEducatifFiltering(
            "datePublication.equals=" + DEFAULT_DATE_PUBLICATION,
            "datePublication.equals=" + UPDATED_DATE_PUBLICATION
        );
    }

    @Test
    @Transactional
    void getAllContenuEducatifsByDatePublicationIsInShouldWork() throws Exception {
        // Initialize the database
        insertedContenuEducatif = contenuEducatifRepository.saveAndFlush(contenuEducatif);

        // Get all the contenuEducatifList where datePublication in
        defaultContenuEducatifFiltering(
            "datePublication.in=" + DEFAULT_DATE_PUBLICATION + "," + UPDATED_DATE_PUBLICATION,
            "datePublication.in=" + UPDATED_DATE_PUBLICATION
        );
    }

    @Test
    @Transactional
    void getAllContenuEducatifsByDatePublicationIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedContenuEducatif = contenuEducatifRepository.saveAndFlush(contenuEducatif);

        // Get all the contenuEducatifList where datePublication is not null
        defaultContenuEducatifFiltering("datePublication.specified=true", "datePublication.specified=false");
    }

    @Test
    @Transactional
    void getAllContenuEducatifsByAuteurIsEqualToSomething() throws Exception {
        Utilisateur auteur;
        if (TestUtil.findAll(em, Utilisateur.class).isEmpty()) {
            contenuEducatifRepository.saveAndFlush(contenuEducatif);
            auteur = UtilisateurResourceIT.createEntity();
        } else {
            auteur = TestUtil.findAll(em, Utilisateur.class).get(0);
        }
        em.persist(auteur);
        em.flush();
        contenuEducatif.setAuteur(auteur);
        contenuEducatifRepository.saveAndFlush(contenuEducatif);
        Long auteurId = auteur.getId();
        // Get all the contenuEducatifList where auteur equals to auteurId
        defaultContenuEducatifShouldBeFound("auteurId.equals=" + auteurId);

        // Get all the contenuEducatifList where auteur equals to (auteurId + 1)
        defaultContenuEducatifShouldNotBeFound("auteurId.equals=" + (auteurId + 1));
    }

    private void defaultContenuEducatifFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultContenuEducatifShouldBeFound(shouldBeFound);
        defaultContenuEducatifShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultContenuEducatifShouldBeFound(String filter) throws Exception {
        restContenuEducatifMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(contenuEducatif.getId().intValue())))
            .andExpect(jsonPath("$.[*].titre").value(hasItem(DEFAULT_TITRE)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].typeMedia").value(hasItem(DEFAULT_TYPE_MEDIA.toString())))
            .andExpect(jsonPath("$.[*].url").value(hasItem(DEFAULT_URL)))
            .andExpect(jsonPath("$.[*].datePublication").value(hasItem(DEFAULT_DATE_PUBLICATION.toString())));

        // Check, that the count call also returns 1
        restContenuEducatifMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultContenuEducatifShouldNotBeFound(String filter) throws Exception {
        restContenuEducatifMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restContenuEducatifMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingContenuEducatif() throws Exception {
        // Get the contenuEducatif
        restContenuEducatifMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingContenuEducatif() throws Exception {
        // Initialize the database
        insertedContenuEducatif = contenuEducatifRepository.saveAndFlush(contenuEducatif);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the contenuEducatif
        ContenuEducatif updatedContenuEducatif = contenuEducatifRepository.findById(contenuEducatif.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedContenuEducatif are not directly saved in db
        em.detach(updatedContenuEducatif);
        updatedContenuEducatif
            .titre(UPDATED_TITRE)
            .description(UPDATED_DESCRIPTION)
            .typeMedia(UPDATED_TYPE_MEDIA)
            .url(UPDATED_URL)
            .datePublication(UPDATED_DATE_PUBLICATION);
        ContenuEducatifDTO contenuEducatifDTO = contenuEducatifMapper.toDto(updatedContenuEducatif);

        restContenuEducatifMockMvc
            .perform(
                put(ENTITY_API_URL_ID, contenuEducatifDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(contenuEducatifDTO))
            )
            .andExpect(status().isOk());

        // Validate the ContenuEducatif in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedContenuEducatifToMatchAllProperties(updatedContenuEducatif);
    }

    @Test
    @Transactional
    void putNonExistingContenuEducatif() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        contenuEducatif.setId(longCount.incrementAndGet());

        // Create the ContenuEducatif
        ContenuEducatifDTO contenuEducatifDTO = contenuEducatifMapper.toDto(contenuEducatif);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restContenuEducatifMockMvc
            .perform(
                put(ENTITY_API_URL_ID, contenuEducatifDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(contenuEducatifDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ContenuEducatif in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchContenuEducatif() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        contenuEducatif.setId(longCount.incrementAndGet());

        // Create the ContenuEducatif
        ContenuEducatifDTO contenuEducatifDTO = contenuEducatifMapper.toDto(contenuEducatif);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restContenuEducatifMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(contenuEducatifDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ContenuEducatif in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamContenuEducatif() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        contenuEducatif.setId(longCount.incrementAndGet());

        // Create the ContenuEducatif
        ContenuEducatifDTO contenuEducatifDTO = contenuEducatifMapper.toDto(contenuEducatif);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restContenuEducatifMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(contenuEducatifDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the ContenuEducatif in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateContenuEducatifWithPatch() throws Exception {
        // Initialize the database
        insertedContenuEducatif = contenuEducatifRepository.saveAndFlush(contenuEducatif);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the contenuEducatif using partial update
        ContenuEducatif partialUpdatedContenuEducatif = new ContenuEducatif();
        partialUpdatedContenuEducatif.setId(contenuEducatif.getId());

        partialUpdatedContenuEducatif.titre(UPDATED_TITRE).datePublication(UPDATED_DATE_PUBLICATION);

        restContenuEducatifMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedContenuEducatif.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedContenuEducatif))
            )
            .andExpect(status().isOk());

        // Validate the ContenuEducatif in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertContenuEducatifUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedContenuEducatif, contenuEducatif),
            getPersistedContenuEducatif(contenuEducatif)
        );
    }

    @Test
    @Transactional
    void fullUpdateContenuEducatifWithPatch() throws Exception {
        // Initialize the database
        insertedContenuEducatif = contenuEducatifRepository.saveAndFlush(contenuEducatif);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the contenuEducatif using partial update
        ContenuEducatif partialUpdatedContenuEducatif = new ContenuEducatif();
        partialUpdatedContenuEducatif.setId(contenuEducatif.getId());

        partialUpdatedContenuEducatif
            .titre(UPDATED_TITRE)
            .description(UPDATED_DESCRIPTION)
            .typeMedia(UPDATED_TYPE_MEDIA)
            .url(UPDATED_URL)
            .datePublication(UPDATED_DATE_PUBLICATION);

        restContenuEducatifMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedContenuEducatif.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedContenuEducatif))
            )
            .andExpect(status().isOk());

        // Validate the ContenuEducatif in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertContenuEducatifUpdatableFieldsEquals(
            partialUpdatedContenuEducatif,
            getPersistedContenuEducatif(partialUpdatedContenuEducatif)
        );
    }

    @Test
    @Transactional
    void patchNonExistingContenuEducatif() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        contenuEducatif.setId(longCount.incrementAndGet());

        // Create the ContenuEducatif
        ContenuEducatifDTO contenuEducatifDTO = contenuEducatifMapper.toDto(contenuEducatif);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restContenuEducatifMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, contenuEducatifDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(contenuEducatifDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ContenuEducatif in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchContenuEducatif() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        contenuEducatif.setId(longCount.incrementAndGet());

        // Create the ContenuEducatif
        ContenuEducatifDTO contenuEducatifDTO = contenuEducatifMapper.toDto(contenuEducatif);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restContenuEducatifMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(contenuEducatifDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ContenuEducatif in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamContenuEducatif() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        contenuEducatif.setId(longCount.incrementAndGet());

        // Create the ContenuEducatif
        ContenuEducatifDTO contenuEducatifDTO = contenuEducatifMapper.toDto(contenuEducatif);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restContenuEducatifMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(contenuEducatifDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the ContenuEducatif in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteContenuEducatif() throws Exception {
        // Initialize the database
        insertedContenuEducatif = contenuEducatifRepository.saveAndFlush(contenuEducatif);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the contenuEducatif
        restContenuEducatifMockMvc
            .perform(delete(ENTITY_API_URL_ID, contenuEducatif.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return contenuEducatifRepository.count();
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

    protected ContenuEducatif getPersistedContenuEducatif(ContenuEducatif contenuEducatif) {
        return contenuEducatifRepository.findById(contenuEducatif.getId()).orElseThrow();
    }

    protected void assertPersistedContenuEducatifToMatchAllProperties(ContenuEducatif expectedContenuEducatif) {
        assertContenuEducatifAllPropertiesEquals(expectedContenuEducatif, getPersistedContenuEducatif(expectedContenuEducatif));
    }

    protected void assertPersistedContenuEducatifToMatchUpdatableProperties(ContenuEducatif expectedContenuEducatif) {
        assertContenuEducatifAllUpdatablePropertiesEquals(expectedContenuEducatif, getPersistedContenuEducatif(expectedContenuEducatif));
    }
}
