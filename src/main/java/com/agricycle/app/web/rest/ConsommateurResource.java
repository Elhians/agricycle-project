package com.agricycle.app.web.rest;

import com.agricycle.app.repository.ConsommateurRepository;
import com.agricycle.app.service.ConsommateurQueryService;
import com.agricycle.app.service.ConsommateurService;
import com.agricycle.app.service.criteria.ConsommateurCriteria;
import com.agricycle.app.service.dto.ConsommateurDTO;
import com.agricycle.app.web.rest.errors.BadRequestAlertException;
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
 * REST controller for managing {@link com.agricycle.app.domain.Consommateur}.
 */
@RestController
@RequestMapping("/api/consommateurs")
public class ConsommateurResource {

    private static final Logger LOG = LoggerFactory.getLogger(ConsommateurResource.class);

    private static final String ENTITY_NAME = "consommateur";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ConsommateurService consommateurService;

    private final ConsommateurRepository consommateurRepository;

    private final ConsommateurQueryService consommateurQueryService;

    public ConsommateurResource(
        ConsommateurService consommateurService,
        ConsommateurRepository consommateurRepository,
        ConsommateurQueryService consommateurQueryService
    ) {
        this.consommateurService = consommateurService;
        this.consommateurRepository = consommateurRepository;
        this.consommateurQueryService = consommateurQueryService;
    }

    /**
     * {@code POST  /consommateurs} : Create a new consommateur.
     *
     * @param consommateurDTO the consommateurDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new consommateurDTO, or with status {@code 400 (Bad Request)} if the consommateur has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<ConsommateurDTO> createConsommateur(@RequestBody ConsommateurDTO consommateurDTO) throws URISyntaxException {
        LOG.debug("REST request to save Consommateur : {}", consommateurDTO);
        if (consommateurDTO.getId() != null) {
            throw new BadRequestAlertException("A new consommateur cannot already have an ID", ENTITY_NAME, "idexists");
        }
        consommateurDTO = consommateurService.save(consommateurDTO);
        return ResponseEntity.created(new URI("/api/consommateurs/" + consommateurDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, consommateurDTO.getId().toString()))
            .body(consommateurDTO);
    }

    /**
     * {@code PUT  /consommateurs/:id} : Updates an existing consommateur.
     *
     * @param id the id of the consommateurDTO to save.
     * @param consommateurDTO the consommateurDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated consommateurDTO,
     * or with status {@code 400 (Bad Request)} if the consommateurDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the consommateurDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<ConsommateurDTO> updateConsommateur(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody ConsommateurDTO consommateurDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update Consommateur : {}, {}", id, consommateurDTO);
        if (consommateurDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, consommateurDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!consommateurRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        consommateurDTO = consommateurService.update(consommateurDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, consommateurDTO.getId().toString()))
            .body(consommateurDTO);
    }

    /**
     * {@code PATCH  /consommateurs/:id} : Partial updates given fields of an existing consommateur, field will ignore if it is null
     *
     * @param id the id of the consommateurDTO to save.
     * @param consommateurDTO the consommateurDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated consommateurDTO,
     * or with status {@code 400 (Bad Request)} if the consommateurDTO is not valid,
     * or with status {@code 404 (Not Found)} if the consommateurDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the consommateurDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<ConsommateurDTO> partialUpdateConsommateur(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody ConsommateurDTO consommateurDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update Consommateur partially : {}, {}", id, consommateurDTO);
        if (consommateurDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, consommateurDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!consommateurRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ConsommateurDTO> result = consommateurService.partialUpdate(consommateurDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, consommateurDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /consommateurs} : get all the consommateurs.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of consommateurs in body.
     */
    @GetMapping("")
    public ResponseEntity<List<ConsommateurDTO>> getAllConsommateurs(ConsommateurCriteria criteria) {
        LOG.debug("REST request to get Consommateurs by criteria: {}", criteria);

        List<ConsommateurDTO> entityList = consommateurQueryService.findByCriteria(criteria);
        return ResponseEntity.ok().body(entityList);
    }

    /**
     * {@code GET  /consommateurs/count} : count all the consommateurs.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countConsommateurs(ConsommateurCriteria criteria) {
        LOG.debug("REST request to count Consommateurs by criteria: {}", criteria);
        return ResponseEntity.ok().body(consommateurQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /consommateurs/:id} : get the "id" consommateur.
     *
     * @param id the id of the consommateurDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the consommateurDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<ConsommateurDTO> getConsommateur(@PathVariable("id") Long id) {
        LOG.debug("REST request to get Consommateur : {}", id);
        Optional<ConsommateurDTO> consommateurDTO = consommateurService.findOne(id);
        return ResponseUtil.wrapOrNotFound(consommateurDTO);
    }

    /**
     * {@code DELETE  /consommateurs/:id} : delete the "id" consommateur.
     *
     * @param id the id of the consommateurDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteConsommateur(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete Consommateur : {}", id);
        consommateurService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
