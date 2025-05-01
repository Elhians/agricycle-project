package com.agricycle.app.web.rest;

import static com.agricycle.app.domain.QRCodeAsserts.*;
import static com.agricycle.app.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.agricycle.app.IntegrationTest;
import com.agricycle.app.domain.QRCode;
import com.agricycle.app.domain.Transaction;
import com.agricycle.app.repository.QRCodeRepository;
import com.agricycle.app.service.dto.QRCodeDTO;
import com.agricycle.app.service.mapper.QRCodeMapper;
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
 * Integration tests for the {@link QRCodeResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class QRCodeResourceIT {

    private static final String DEFAULT_VALEUR = "AAAAAAAAAA";
    private static final String UPDATED_VALEUR = "BBBBBBBBBB";

    private static final Instant DEFAULT_DATE_EXPIRATION = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_DATE_EXPIRATION = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/qr-codes";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private QRCodeRepository qRCodeRepository;

    @Autowired
    private QRCodeMapper qRCodeMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restQRCodeMockMvc;

    private QRCode qRCode;

    private QRCode insertedQRCode;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static QRCode createEntity() {
        return new QRCode().valeur(DEFAULT_VALEUR).dateExpiration(DEFAULT_DATE_EXPIRATION);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static QRCode createUpdatedEntity() {
        return new QRCode().valeur(UPDATED_VALEUR).dateExpiration(UPDATED_DATE_EXPIRATION);
    }

    @BeforeEach
    void initTest() {
        qRCode = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedQRCode != null) {
            qRCodeRepository.delete(insertedQRCode);
            insertedQRCode = null;
        }
    }

    @Test
    @Transactional
    void createQRCode() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the QRCode
        QRCodeDTO qRCodeDTO = qRCodeMapper.toDto(qRCode);
        var returnedQRCodeDTO = om.readValue(
            restQRCodeMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(qRCodeDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            QRCodeDTO.class
        );

        // Validate the QRCode in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedQRCode = qRCodeMapper.toEntity(returnedQRCodeDTO);
        assertQRCodeUpdatableFieldsEquals(returnedQRCode, getPersistedQRCode(returnedQRCode));

        insertedQRCode = returnedQRCode;
    }

    @Test
    @Transactional
    void createQRCodeWithExistingId() throws Exception {
        // Create the QRCode with an existing ID
        qRCode.setId(1L);
        QRCodeDTO qRCodeDTO = qRCodeMapper.toDto(qRCode);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restQRCodeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(qRCodeDTO)))
            .andExpect(status().isBadRequest());

        // Validate the QRCode in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkValeurIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        qRCode.setValeur(null);

        // Create the QRCode, which fails.
        QRCodeDTO qRCodeDTO = qRCodeMapper.toDto(qRCode);

        restQRCodeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(qRCodeDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllQRCodes() throws Exception {
        // Initialize the database
        insertedQRCode = qRCodeRepository.saveAndFlush(qRCode);

        // Get all the qRCodeList
        restQRCodeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(qRCode.getId().intValue())))
            .andExpect(jsonPath("$.[*].valeur").value(hasItem(DEFAULT_VALEUR)))
            .andExpect(jsonPath("$.[*].dateExpiration").value(hasItem(DEFAULT_DATE_EXPIRATION.toString())));
    }

    @Test
    @Transactional
    void getQRCode() throws Exception {
        // Initialize the database
        insertedQRCode = qRCodeRepository.saveAndFlush(qRCode);

        // Get the qRCode
        restQRCodeMockMvc
            .perform(get(ENTITY_API_URL_ID, qRCode.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(qRCode.getId().intValue()))
            .andExpect(jsonPath("$.valeur").value(DEFAULT_VALEUR))
            .andExpect(jsonPath("$.dateExpiration").value(DEFAULT_DATE_EXPIRATION.toString()));
    }

    @Test
    @Transactional
    void getQRCodesByIdFiltering() throws Exception {
        // Initialize the database
        insertedQRCode = qRCodeRepository.saveAndFlush(qRCode);

        Long id = qRCode.getId();

        defaultQRCodeFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultQRCodeFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultQRCodeFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllQRCodesByValeurIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedQRCode = qRCodeRepository.saveAndFlush(qRCode);

        // Get all the qRCodeList where valeur equals to
        defaultQRCodeFiltering("valeur.equals=" + DEFAULT_VALEUR, "valeur.equals=" + UPDATED_VALEUR);
    }

    @Test
    @Transactional
    void getAllQRCodesByValeurIsInShouldWork() throws Exception {
        // Initialize the database
        insertedQRCode = qRCodeRepository.saveAndFlush(qRCode);

        // Get all the qRCodeList where valeur in
        defaultQRCodeFiltering("valeur.in=" + DEFAULT_VALEUR + "," + UPDATED_VALEUR, "valeur.in=" + UPDATED_VALEUR);
    }

    @Test
    @Transactional
    void getAllQRCodesByValeurIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedQRCode = qRCodeRepository.saveAndFlush(qRCode);

        // Get all the qRCodeList where valeur is not null
        defaultQRCodeFiltering("valeur.specified=true", "valeur.specified=false");
    }

    @Test
    @Transactional
    void getAllQRCodesByValeurContainsSomething() throws Exception {
        // Initialize the database
        insertedQRCode = qRCodeRepository.saveAndFlush(qRCode);

        // Get all the qRCodeList where valeur contains
        defaultQRCodeFiltering("valeur.contains=" + DEFAULT_VALEUR, "valeur.contains=" + UPDATED_VALEUR);
    }

    @Test
    @Transactional
    void getAllQRCodesByValeurNotContainsSomething() throws Exception {
        // Initialize the database
        insertedQRCode = qRCodeRepository.saveAndFlush(qRCode);

        // Get all the qRCodeList where valeur does not contain
        defaultQRCodeFiltering("valeur.doesNotContain=" + UPDATED_VALEUR, "valeur.doesNotContain=" + DEFAULT_VALEUR);
    }

    @Test
    @Transactional
    void getAllQRCodesByDateExpirationIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedQRCode = qRCodeRepository.saveAndFlush(qRCode);

        // Get all the qRCodeList where dateExpiration equals to
        defaultQRCodeFiltering("dateExpiration.equals=" + DEFAULT_DATE_EXPIRATION, "dateExpiration.equals=" + UPDATED_DATE_EXPIRATION);
    }

    @Test
    @Transactional
    void getAllQRCodesByDateExpirationIsInShouldWork() throws Exception {
        // Initialize the database
        insertedQRCode = qRCodeRepository.saveAndFlush(qRCode);

        // Get all the qRCodeList where dateExpiration in
        defaultQRCodeFiltering(
            "dateExpiration.in=" + DEFAULT_DATE_EXPIRATION + "," + UPDATED_DATE_EXPIRATION,
            "dateExpiration.in=" + UPDATED_DATE_EXPIRATION
        );
    }

    @Test
    @Transactional
    void getAllQRCodesByDateExpirationIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedQRCode = qRCodeRepository.saveAndFlush(qRCode);

        // Get all the qRCodeList where dateExpiration is not null
        defaultQRCodeFiltering("dateExpiration.specified=true", "dateExpiration.specified=false");
    }

    @Test
    @Transactional
    void getAllQRCodesByTransactionIsEqualToSomething() throws Exception {
        Transaction transaction;
        if (TestUtil.findAll(em, Transaction.class).isEmpty()) {
            qRCodeRepository.saveAndFlush(qRCode);
            transaction = TransactionResourceIT.createEntity();
        } else {
            transaction = TestUtil.findAll(em, Transaction.class).get(0);
        }
        em.persist(transaction);
        em.flush();
        qRCode.setTransaction(transaction);
        qRCodeRepository.saveAndFlush(qRCode);
        Long transactionId = transaction.getId();
        // Get all the qRCodeList where transaction equals to transactionId
        defaultQRCodeShouldBeFound("transactionId.equals=" + transactionId);

        // Get all the qRCodeList where transaction equals to (transactionId + 1)
        defaultQRCodeShouldNotBeFound("transactionId.equals=" + (transactionId + 1));
    }

    private void defaultQRCodeFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultQRCodeShouldBeFound(shouldBeFound);
        defaultQRCodeShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultQRCodeShouldBeFound(String filter) throws Exception {
        restQRCodeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(qRCode.getId().intValue())))
            .andExpect(jsonPath("$.[*].valeur").value(hasItem(DEFAULT_VALEUR)))
            .andExpect(jsonPath("$.[*].dateExpiration").value(hasItem(DEFAULT_DATE_EXPIRATION.toString())));

        // Check, that the count call also returns 1
        restQRCodeMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultQRCodeShouldNotBeFound(String filter) throws Exception {
        restQRCodeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restQRCodeMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingQRCode() throws Exception {
        // Get the qRCode
        restQRCodeMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingQRCode() throws Exception {
        // Initialize the database
        insertedQRCode = qRCodeRepository.saveAndFlush(qRCode);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the qRCode
        QRCode updatedQRCode = qRCodeRepository.findById(qRCode.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedQRCode are not directly saved in db
        em.detach(updatedQRCode);
        updatedQRCode.valeur(UPDATED_VALEUR).dateExpiration(UPDATED_DATE_EXPIRATION);
        QRCodeDTO qRCodeDTO = qRCodeMapper.toDto(updatedQRCode);

        restQRCodeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, qRCodeDTO.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(qRCodeDTO))
            )
            .andExpect(status().isOk());

        // Validate the QRCode in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedQRCodeToMatchAllProperties(updatedQRCode);
    }

    @Test
    @Transactional
    void putNonExistingQRCode() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        qRCode.setId(longCount.incrementAndGet());

        // Create the QRCode
        QRCodeDTO qRCodeDTO = qRCodeMapper.toDto(qRCode);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restQRCodeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, qRCodeDTO.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(qRCodeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the QRCode in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchQRCode() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        qRCode.setId(longCount.incrementAndGet());

        // Create the QRCode
        QRCodeDTO qRCodeDTO = qRCodeMapper.toDto(qRCode);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restQRCodeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(qRCodeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the QRCode in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamQRCode() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        qRCode.setId(longCount.incrementAndGet());

        // Create the QRCode
        QRCodeDTO qRCodeDTO = qRCodeMapper.toDto(qRCode);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restQRCodeMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(qRCodeDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the QRCode in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateQRCodeWithPatch() throws Exception {
        // Initialize the database
        insertedQRCode = qRCodeRepository.saveAndFlush(qRCode);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the qRCode using partial update
        QRCode partialUpdatedQRCode = new QRCode();
        partialUpdatedQRCode.setId(qRCode.getId());

        restQRCodeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedQRCode.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedQRCode))
            )
            .andExpect(status().isOk());

        // Validate the QRCode in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertQRCodeUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedQRCode, qRCode), getPersistedQRCode(qRCode));
    }

    @Test
    @Transactional
    void fullUpdateQRCodeWithPatch() throws Exception {
        // Initialize the database
        insertedQRCode = qRCodeRepository.saveAndFlush(qRCode);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the qRCode using partial update
        QRCode partialUpdatedQRCode = new QRCode();
        partialUpdatedQRCode.setId(qRCode.getId());

        partialUpdatedQRCode.valeur(UPDATED_VALEUR).dateExpiration(UPDATED_DATE_EXPIRATION);

        restQRCodeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedQRCode.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedQRCode))
            )
            .andExpect(status().isOk());

        // Validate the QRCode in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertQRCodeUpdatableFieldsEquals(partialUpdatedQRCode, getPersistedQRCode(partialUpdatedQRCode));
    }

    @Test
    @Transactional
    void patchNonExistingQRCode() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        qRCode.setId(longCount.incrementAndGet());

        // Create the QRCode
        QRCodeDTO qRCodeDTO = qRCodeMapper.toDto(qRCode);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restQRCodeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, qRCodeDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(qRCodeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the QRCode in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchQRCode() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        qRCode.setId(longCount.incrementAndGet());

        // Create the QRCode
        QRCodeDTO qRCodeDTO = qRCodeMapper.toDto(qRCode);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restQRCodeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(qRCodeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the QRCode in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamQRCode() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        qRCode.setId(longCount.incrementAndGet());

        // Create the QRCode
        QRCodeDTO qRCodeDTO = qRCodeMapper.toDto(qRCode);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restQRCodeMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(qRCodeDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the QRCode in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteQRCode() throws Exception {
        // Initialize the database
        insertedQRCode = qRCodeRepository.saveAndFlush(qRCode);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the qRCode
        restQRCodeMockMvc
            .perform(delete(ENTITY_API_URL_ID, qRCode.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return qRCodeRepository.count();
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

    protected QRCode getPersistedQRCode(QRCode qRCode) {
        return qRCodeRepository.findById(qRCode.getId()).orElseThrow();
    }

    protected void assertPersistedQRCodeToMatchAllProperties(QRCode expectedQRCode) {
        assertQRCodeAllPropertiesEquals(expectedQRCode, getPersistedQRCode(expectedQRCode));
    }

    protected void assertPersistedQRCodeToMatchUpdatableProperties(QRCode expectedQRCode) {
        assertQRCodeAllUpdatablePropertiesEquals(expectedQRCode, getPersistedQRCode(expectedQRCode));
    }
}
