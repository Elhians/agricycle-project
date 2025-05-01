package com.agricycle.app.web.rest;

import com.agricycle.app.repository.QRCodeRepository;
import com.agricycle.app.service.QRCodeQueryService;
import com.agricycle.app.service.QRCodeService;
import com.agricycle.app.service.criteria.QRCodeCriteria;
import com.agricycle.app.service.dto.QRCodeDTO;
import com.agricycle.app.web.rest.errors.BadRequestAlertException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.agricycle.app.domain.QRCode}.
 */
@RestController
@RequestMapping("/api/qr-codes")
public class QRCodeResource {

    private static final Logger LOG = LoggerFactory.getLogger(QRCodeResource.class);

    private static final String ENTITY_NAME = "qRCode";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final QRCodeService qRCodeService;

    private final QRCodeRepository qRCodeRepository;

    private final QRCodeQueryService qRCodeQueryService;

    public QRCodeResource(QRCodeService qRCodeService, QRCodeRepository qRCodeRepository, QRCodeQueryService qRCodeQueryService) {
        this.qRCodeService = qRCodeService;
        this.qRCodeRepository = qRCodeRepository;
        this.qRCodeQueryService = qRCodeQueryService;
    }

    /**
     * {@code POST  /qr-codes} : Create a new qRCode.
     *
     * @param qRCodeDTO the qRCodeDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new qRCodeDTO, or with status {@code 400 (Bad Request)} if the qRCode has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<QRCodeDTO> createQRCode(@Valid @RequestBody QRCodeDTO qRCodeDTO) throws URISyntaxException {
        LOG.debug("REST request to save QRCode : {}", qRCodeDTO);
        if (qRCodeDTO.getId() != null) {
            throw new BadRequestAlertException("A new qRCode cannot already have an ID", ENTITY_NAME, "idexists");
        }
        qRCodeDTO = qRCodeService.save(qRCodeDTO);
        return ResponseEntity.created(new URI("/api/qr-codes/" + qRCodeDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, qRCodeDTO.getId().toString()))
            .body(qRCodeDTO);
    }

    /**
     * {@code PUT  /qr-codes/:id} : Updates an existing qRCode.
     *
     * @param id the id of the qRCodeDTO to save.
     * @param qRCodeDTO the qRCodeDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated qRCodeDTO,
     * or with status {@code 400 (Bad Request)} if the qRCodeDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the qRCodeDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<QRCodeDTO> updateQRCode(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody QRCodeDTO qRCodeDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update QRCode : {}, {}", id, qRCodeDTO);
        if (qRCodeDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, qRCodeDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!qRCodeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        qRCodeDTO = qRCodeService.update(qRCodeDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, qRCodeDTO.getId().toString()))
            .body(qRCodeDTO);
    }

    /**
     * {@code PATCH  /qr-codes/:id} : Partial updates given fields of an existing qRCode, field will ignore if it is null
     *
     * @param id the id of the qRCodeDTO to save.
     * @param qRCodeDTO the qRCodeDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated qRCodeDTO,
     * or with status {@code 400 (Bad Request)} if the qRCodeDTO is not valid,
     * or with status {@code 404 (Not Found)} if the qRCodeDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the qRCodeDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<QRCodeDTO> partialUpdateQRCode(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody QRCodeDTO qRCodeDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update QRCode partially : {}, {}", id, qRCodeDTO);
        if (qRCodeDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, qRCodeDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!qRCodeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<QRCodeDTO> result = qRCodeService.partialUpdate(qRCodeDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, qRCodeDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /qr-codes} : get all the qRCodes.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of qRCodes in body.
     */
    @GetMapping("")
    public ResponseEntity<List<QRCodeDTO>> getAllQRCodes(QRCodeCriteria criteria) {
        LOG.debug("REST request to get QRCodes by criteria: {}", criteria);

        List<QRCodeDTO> entityList = qRCodeQueryService.findByCriteria(criteria);
        return ResponseEntity.ok().body(entityList);
    }

    /**
     * {@code GET  /qr-codes/count} : count all the qRCodes.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countQRCodes(QRCodeCriteria criteria) {
        LOG.debug("REST request to count QRCodes by criteria: {}", criteria);
        return ResponseEntity.ok().body(qRCodeQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /qr-codes/:id} : get the "id" qRCode.
     *
     * @param id the id of the qRCodeDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the qRCodeDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<QRCodeDTO> getQRCode(@PathVariable("id") Long id) {
        LOG.debug("REST request to get QRCode : {}", id);
        Optional<QRCodeDTO> qRCodeDTO = qRCodeService.findOne(id);
        return ResponseUtil.wrapOrNotFound(qRCodeDTO);
    }

    /**
     * {@code DELETE  /qr-codes/:id} : delete the "id" qRCode.
     *
     * @param id the id of the qRCodeDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteQRCode(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete QRCode : {}", id);
        qRCodeService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
