package com.agricycle.app.web.rest;

import com.agricycle.app.repository.ContenuEducatifRepository;
import com.agricycle.app.service.ContenuEducatifQueryService;
import com.agricycle.app.service.ContenuEducatifService;
import com.agricycle.app.service.criteria.ContenuEducatifCriteria;
import com.agricycle.app.service.dto.ContenuEducatifDTO;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.agricycle.app.domain.ContenuEducatif}.
 */
@RestController
@RequestMapping("/api/contenu-educatifs")
public class ContenuEducatifResource {

    private static final Logger LOG = LoggerFactory.getLogger(ContenuEducatifResource.class);

    private static final String ENTITY_NAME = "contenuEducatif";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ContenuEducatifService contenuEducatifService;

    private final ContenuEducatifRepository contenuEducatifRepository;

    private final ContenuEducatifQueryService contenuEducatifQueryService;

    public ContenuEducatifResource(
        ContenuEducatifService contenuEducatifService,
        ContenuEducatifRepository contenuEducatifRepository,
        ContenuEducatifQueryService contenuEducatifQueryService
    ) {
        this.contenuEducatifService = contenuEducatifService;
        this.contenuEducatifRepository = contenuEducatifRepository;
        this.contenuEducatifQueryService = contenuEducatifQueryService;
    }

    /**
     * {@code POST  /contenu-educatifs} : Create a new contenuEducatif.
     *
     * @param contenuEducatifDTO the contenuEducatifDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new contenuEducatifDTO, or with status {@code 400 (Bad Request)} if the contenuEducatif has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<ContenuEducatifDTO> createContenuEducatif(@Valid @RequestBody ContenuEducatifDTO contenuEducatifDTO)
        throws URISyntaxException {
        LOG.debug("REST request to save ContenuEducatif : {}", contenuEducatifDTO);
        if (contenuEducatifDTO.getId() != null) {
            throw new BadRequestAlertException("A new contenuEducatif cannot already have an ID", ENTITY_NAME, "idexists");
        }
        contenuEducatifDTO = contenuEducatifService.save(contenuEducatifDTO);
        return ResponseEntity.created(new URI("/api/contenu-educatifs/" + contenuEducatifDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, contenuEducatifDTO.getId().toString()))
            .body(contenuEducatifDTO);
    }

    /**
     * {@code PUT  /contenu-educatifs/:id} : Updates an existing contenuEducatif.
     *
     * @param id the id of the contenuEducatifDTO to save.
     * @param contenuEducatifDTO the contenuEducatifDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated contenuEducatifDTO,
     * or with status {@code 400 (Bad Request)} if the contenuEducatifDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the contenuEducatifDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<ContenuEducatifDTO> updateContenuEducatif(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody ContenuEducatifDTO contenuEducatifDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update ContenuEducatif : {}, {}", id, contenuEducatifDTO);
        if (contenuEducatifDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, contenuEducatifDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!contenuEducatifRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        contenuEducatifDTO = contenuEducatifService.update(contenuEducatifDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, contenuEducatifDTO.getId().toString()))
            .body(contenuEducatifDTO);
    }

    /**
     * {@code PATCH  /contenu-educatifs/:id} : Partial updates given fields of an existing contenuEducatif, field will ignore if it is null
     *
     * @param id the id of the contenuEducatifDTO to save.
     * @param contenuEducatifDTO the contenuEducatifDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated contenuEducatifDTO,
     * or with status {@code 400 (Bad Request)} if the contenuEducatifDTO is not valid,
     * or with status {@code 404 (Not Found)} if the contenuEducatifDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the contenuEducatifDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<ContenuEducatifDTO> partialUpdateContenuEducatif(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody ContenuEducatifDTO contenuEducatifDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update ContenuEducatif partially : {}, {}", id, contenuEducatifDTO);
        if (contenuEducatifDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, contenuEducatifDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!contenuEducatifRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ContenuEducatifDTO> result = contenuEducatifService.partialUpdate(contenuEducatifDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, contenuEducatifDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /contenu-educatifs} : get all the contenuEducatifs.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of contenuEducatifs in body.
     */
    @GetMapping("")
    public ResponseEntity<List<ContenuEducatifDTO>> getAllContenuEducatifs(
        ContenuEducatifCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get ContenuEducatifs by criteria: {}", criteria);

        Page<ContenuEducatifDTO> page = contenuEducatifQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /contenu-educatifs/count} : count all the contenuEducatifs.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countContenuEducatifs(ContenuEducatifCriteria criteria) {
        LOG.debug("REST request to count ContenuEducatifs by criteria: {}", criteria);
        return ResponseEntity.ok().body(contenuEducatifQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /contenu-educatifs/:id} : get the "id" contenuEducatif.
     *
     * @param id the id of the contenuEducatifDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the contenuEducatifDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<ContenuEducatifDTO> getContenuEducatif(@PathVariable("id") Long id) {
        LOG.debug("REST request to get ContenuEducatif : {}", id);
        Optional<ContenuEducatifDTO> contenuEducatifDTO = contenuEducatifService.findOne(id);
        return ResponseUtil.wrapOrNotFound(contenuEducatifDTO);
    }

    /**
     * {@code DELETE  /contenu-educatifs/:id} : delete the "id" contenuEducatif.
     *
     * @param id the id of the contenuEducatifDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteContenuEducatif(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete ContenuEducatif : {}", id);
        contenuEducatifService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
