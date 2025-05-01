package com.agricycle.app.web.rest;

import com.agricycle.app.repository.TransporteurRepository;
import com.agricycle.app.service.TransporteurQueryService;
import com.agricycle.app.service.TransporteurService;
import com.agricycle.app.service.criteria.TransporteurCriteria;
import com.agricycle.app.service.dto.TransporteurDTO;
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
 * REST controller for managing {@link com.agricycle.app.domain.Transporteur}.
 */
@RestController
@RequestMapping("/api/transporteurs")
public class TransporteurResource {

    private static final Logger LOG = LoggerFactory.getLogger(TransporteurResource.class);

    private static final String ENTITY_NAME = "transporteur";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final TransporteurService transporteurService;

    private final TransporteurRepository transporteurRepository;

    private final TransporteurQueryService transporteurQueryService;

    public TransporteurResource(
        TransporteurService transporteurService,
        TransporteurRepository transporteurRepository,
        TransporteurQueryService transporteurQueryService
    ) {
        this.transporteurService = transporteurService;
        this.transporteurRepository = transporteurRepository;
        this.transporteurQueryService = transporteurQueryService;
    }

    /**
     * {@code POST  /transporteurs} : Create a new transporteur.
     *
     * @param transporteurDTO the transporteurDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new transporteurDTO, or with status {@code 400 (Bad Request)} if the transporteur has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<TransporteurDTO> createTransporteur(@RequestBody TransporteurDTO transporteurDTO) throws URISyntaxException {
        LOG.debug("REST request to save Transporteur : {}", transporteurDTO);
        if (transporteurDTO.getId() != null) {
            throw new BadRequestAlertException("A new transporteur cannot already have an ID", ENTITY_NAME, "idexists");
        }
        transporteurDTO = transporteurService.save(transporteurDTO);
        return ResponseEntity.created(new URI("/api/transporteurs/" + transporteurDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, transporteurDTO.getId().toString()))
            .body(transporteurDTO);
    }

    /**
     * {@code PUT  /transporteurs/:id} : Updates an existing transporteur.
     *
     * @param id the id of the transporteurDTO to save.
     * @param transporteurDTO the transporteurDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated transporteurDTO,
     * or with status {@code 400 (Bad Request)} if the transporteurDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the transporteurDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<TransporteurDTO> updateTransporteur(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody TransporteurDTO transporteurDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update Transporteur : {}, {}", id, transporteurDTO);
        if (transporteurDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, transporteurDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!transporteurRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        transporteurDTO = transporteurService.update(transporteurDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, transporteurDTO.getId().toString()))
            .body(transporteurDTO);
    }

    /**
     * {@code PATCH  /transporteurs/:id} : Partial updates given fields of an existing transporteur, field will ignore if it is null
     *
     * @param id the id of the transporteurDTO to save.
     * @param transporteurDTO the transporteurDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated transporteurDTO,
     * or with status {@code 400 (Bad Request)} if the transporteurDTO is not valid,
     * or with status {@code 404 (Not Found)} if the transporteurDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the transporteurDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<TransporteurDTO> partialUpdateTransporteur(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody TransporteurDTO transporteurDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update Transporteur partially : {}, {}", id, transporteurDTO);
        if (transporteurDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, transporteurDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!transporteurRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<TransporteurDTO> result = transporteurService.partialUpdate(transporteurDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, transporteurDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /transporteurs} : get all the transporteurs.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of transporteurs in body.
     */
    @GetMapping("")
    public ResponseEntity<List<TransporteurDTO>> getAllTransporteurs(TransporteurCriteria criteria) {
        LOG.debug("REST request to get Transporteurs by criteria: {}", criteria);

        List<TransporteurDTO> entityList = transporteurQueryService.findByCriteria(criteria);
        return ResponseEntity.ok().body(entityList);
    }

    /**
     * {@code GET  /transporteurs/count} : count all the transporteurs.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countTransporteurs(TransporteurCriteria criteria) {
        LOG.debug("REST request to count Transporteurs by criteria: {}", criteria);
        return ResponseEntity.ok().body(transporteurQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /transporteurs/:id} : get the "id" transporteur.
     *
     * @param id the id of the transporteurDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the transporteurDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<TransporteurDTO> getTransporteur(@PathVariable("id") Long id) {
        LOG.debug("REST request to get Transporteur : {}", id);
        Optional<TransporteurDTO> transporteurDTO = transporteurService.findOne(id);
        return ResponseUtil.wrapOrNotFound(transporteurDTO);
    }

    /**
     * {@code DELETE  /transporteurs/:id} : delete the "id" transporteur.
     *
     * @param id the id of the transporteurDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTransporteur(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete Transporteur : {}", id);
        transporteurService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
