package com.agricycle.app.web.rest;

import com.agricycle.app.repository.AgriculteurRepository;
import com.agricycle.app.service.AgriculteurQueryService;
import com.agricycle.app.service.AgriculteurService;
import com.agricycle.app.service.criteria.AgriculteurCriteria;
import com.agricycle.app.service.dto.AgriculteurDTO;
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
 * REST controller for managing {@link com.agricycle.app.domain.Agriculteur}.
 */
@RestController
@RequestMapping("/api/agriculteurs")
public class AgriculteurResource {

    private static final Logger LOG = LoggerFactory.getLogger(AgriculteurResource.class);

    private static final String ENTITY_NAME = "agriculteur";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final AgriculteurService agriculteurService;

    private final AgriculteurRepository agriculteurRepository;

    private final AgriculteurQueryService agriculteurQueryService;

    public AgriculteurResource(
        AgriculteurService agriculteurService,
        AgriculteurRepository agriculteurRepository,
        AgriculteurQueryService agriculteurQueryService
    ) {
        this.agriculteurService = agriculteurService;
        this.agriculteurRepository = agriculteurRepository;
        this.agriculteurQueryService = agriculteurQueryService;
    }

    /**
     * {@code POST  /agriculteurs} : Create a new agriculteur.
     *
     * @param agriculteurDTO the agriculteurDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new agriculteurDTO, or with status {@code 400 (Bad Request)} if the agriculteur has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<AgriculteurDTO> createAgriculteur(@RequestBody AgriculteurDTO agriculteurDTO) throws URISyntaxException {
        LOG.debug("REST request to save Agriculteur : {}", agriculteurDTO);
        if (agriculteurDTO.getId() != null) {
            throw new BadRequestAlertException("A new agriculteur cannot already have an ID", ENTITY_NAME, "idexists");
        }
        agriculteurDTO = agriculteurService.save(agriculteurDTO);
        return ResponseEntity.created(new URI("/api/agriculteurs/" + agriculteurDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, agriculteurDTO.getId().toString()))
            .body(agriculteurDTO);
    }

    /**
     * {@code PUT  /agriculteurs/:id} : Updates an existing agriculteur.
     *
     * @param id the id of the agriculteurDTO to save.
     * @param agriculteurDTO the agriculteurDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated agriculteurDTO,
     * or with status {@code 400 (Bad Request)} if the agriculteurDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the agriculteurDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<AgriculteurDTO> updateAgriculteur(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody AgriculteurDTO agriculteurDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update Agriculteur : {}, {}", id, agriculteurDTO);
        if (agriculteurDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, agriculteurDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!agriculteurRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        agriculteurDTO = agriculteurService.update(agriculteurDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, agriculteurDTO.getId().toString()))
            .body(agriculteurDTO);
    }

    /**
     * {@code PATCH  /agriculteurs/:id} : Partial updates given fields of an existing agriculteur, field will ignore if it is null
     *
     * @param id the id of the agriculteurDTO to save.
     * @param agriculteurDTO the agriculteurDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated agriculteurDTO,
     * or with status {@code 400 (Bad Request)} if the agriculteurDTO is not valid,
     * or with status {@code 404 (Not Found)} if the agriculteurDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the agriculteurDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<AgriculteurDTO> partialUpdateAgriculteur(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody AgriculteurDTO agriculteurDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update Agriculteur partially : {}, {}", id, agriculteurDTO);
        if (agriculteurDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, agriculteurDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!agriculteurRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<AgriculteurDTO> result = agriculteurService.partialUpdate(agriculteurDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, agriculteurDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /agriculteurs} : get all the agriculteurs.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of agriculteurs in body.
     */
    @GetMapping("")
    public ResponseEntity<List<AgriculteurDTO>> getAllAgriculteurs(AgriculteurCriteria criteria) {
        LOG.debug("REST request to get Agriculteurs by criteria: {}", criteria);

        List<AgriculteurDTO> entityList = agriculteurQueryService.findByCriteria(criteria);
        return ResponseEntity.ok().body(entityList);
    }

    /**
     * {@code GET  /agriculteurs/count} : count all the agriculteurs.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countAgriculteurs(AgriculteurCriteria criteria) {
        LOG.debug("REST request to count Agriculteurs by criteria: {}", criteria);
        return ResponseEntity.ok().body(agriculteurQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /agriculteurs/:id} : get the "id" agriculteur.
     *
     * @param id the id of the agriculteurDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the agriculteurDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<AgriculteurDTO> getAgriculteur(@PathVariable("id") Long id) {
        LOG.debug("REST request to get Agriculteur : {}", id);
        Optional<AgriculteurDTO> agriculteurDTO = agriculteurService.findOne(id);
        return ResponseUtil.wrapOrNotFound(agriculteurDTO);
    }

    /**
     * {@code DELETE  /agriculteurs/:id} : delete the "id" agriculteur.
     *
     * @param id the id of the agriculteurDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAgriculteur(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete Agriculteur : {}", id);
        agriculteurService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
