package com.agricycle.app.web.rest;

import com.agricycle.app.repository.OrganisationRepository;
import com.agricycle.app.service.OrganisationQueryService;
import com.agricycle.app.service.OrganisationService;
import com.agricycle.app.service.criteria.OrganisationCriteria;
import com.agricycle.app.service.dto.OrganisationDTO;
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
 * REST controller for managing {@link com.agricycle.app.domain.Organisation}.
 */
@RestController
@RequestMapping("/api/organisations")
public class OrganisationResource {

    private static final Logger LOG = LoggerFactory.getLogger(OrganisationResource.class);

    private static final String ENTITY_NAME = "organisation";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final OrganisationService organisationService;

    private final OrganisationRepository organisationRepository;

    private final OrganisationQueryService organisationQueryService;

    public OrganisationResource(
        OrganisationService organisationService,
        OrganisationRepository organisationRepository,
        OrganisationQueryService organisationQueryService
    ) {
        this.organisationService = organisationService;
        this.organisationRepository = organisationRepository;
        this.organisationQueryService = organisationQueryService;
    }

    /**
     * {@code POST  /organisations} : Create a new organisation.
     *
     * @param organisationDTO the organisationDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new organisationDTO, or with status {@code 400 (Bad Request)} if the organisation has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<OrganisationDTO> createOrganisation(@RequestBody OrganisationDTO organisationDTO) throws URISyntaxException {
        LOG.debug("REST request to save Organisation : {}", organisationDTO);
        if (organisationDTO.getId() != null) {
            throw new BadRequestAlertException("A new organisation cannot already have an ID", ENTITY_NAME, "idexists");
        }
        organisationDTO = organisationService.save(organisationDTO);
        return ResponseEntity.created(new URI("/api/organisations/" + organisationDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, organisationDTO.getId().toString()))
            .body(organisationDTO);
    }

    /**
     * {@code PUT  /organisations/:id} : Updates an existing organisation.
     *
     * @param id the id of the organisationDTO to save.
     * @param organisationDTO the organisationDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated organisationDTO,
     * or with status {@code 400 (Bad Request)} if the organisationDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the organisationDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<OrganisationDTO> updateOrganisation(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody OrganisationDTO organisationDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update Organisation : {}, {}", id, organisationDTO);
        if (organisationDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, organisationDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!organisationRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        organisationDTO = organisationService.update(organisationDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, organisationDTO.getId().toString()))
            .body(organisationDTO);
    }

    /**
     * {@code PATCH  /organisations/:id} : Partial updates given fields of an existing organisation, field will ignore if it is null
     *
     * @param id the id of the organisationDTO to save.
     * @param organisationDTO the organisationDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated organisationDTO,
     * or with status {@code 400 (Bad Request)} if the organisationDTO is not valid,
     * or with status {@code 404 (Not Found)} if the organisationDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the organisationDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<OrganisationDTO> partialUpdateOrganisation(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody OrganisationDTO organisationDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update Organisation partially : {}, {}", id, organisationDTO);
        if (organisationDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, organisationDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!organisationRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<OrganisationDTO> result = organisationService.partialUpdate(organisationDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, organisationDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /organisations} : get all the organisations.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of organisations in body.
     */
    @GetMapping("")
    public ResponseEntity<List<OrganisationDTO>> getAllOrganisations(OrganisationCriteria criteria) {
        LOG.debug("REST request to get Organisations by criteria: {}", criteria);

        List<OrganisationDTO> entityList = organisationQueryService.findByCriteria(criteria);
        return ResponseEntity.ok().body(entityList);
    }

    /**
     * {@code GET  /organisations/count} : count all the organisations.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countOrganisations(OrganisationCriteria criteria) {
        LOG.debug("REST request to count Organisations by criteria: {}", criteria);
        return ResponseEntity.ok().body(organisationQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /organisations/:id} : get the "id" organisation.
     *
     * @param id the id of the organisationDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the organisationDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<OrganisationDTO> getOrganisation(@PathVariable("id") Long id) {
        LOG.debug("REST request to get Organisation : {}", id);
        Optional<OrganisationDTO> organisationDTO = organisationService.findOne(id);
        return ResponseUtil.wrapOrNotFound(organisationDTO);
    }

    /**
     * {@code DELETE  /organisations/:id} : delete the "id" organisation.
     *
     * @param id the id of the organisationDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteOrganisation(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete Organisation : {}", id);
        organisationService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
