package com.agricycle.app.web.rest;

import com.agricycle.app.repository.CommercantRepository;
import com.agricycle.app.service.CommercantQueryService;
import com.agricycle.app.service.CommercantService;
import com.agricycle.app.service.criteria.CommercantCriteria;
import com.agricycle.app.service.dto.CommercantDTO;
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
 * REST controller for managing {@link com.agricycle.app.domain.Commercant}.
 */
@RestController
@RequestMapping("/api/commercants")
public class CommercantResource {

    private static final Logger LOG = LoggerFactory.getLogger(CommercantResource.class);

    private static final String ENTITY_NAME = "commercant";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final CommercantService commercantService;

    private final CommercantRepository commercantRepository;

    private final CommercantQueryService commercantQueryService;

    public CommercantResource(
        CommercantService commercantService,
        CommercantRepository commercantRepository,
        CommercantQueryService commercantQueryService
    ) {
        this.commercantService = commercantService;
        this.commercantRepository = commercantRepository;
        this.commercantQueryService = commercantQueryService;
    }

    /**
     * {@code POST  /commercants} : Create a new commercant.
     *
     * @param commercantDTO the commercantDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new commercantDTO, or with status {@code 400 (Bad Request)} if the commercant has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<CommercantDTO> createCommercant(@RequestBody CommercantDTO commercantDTO) throws URISyntaxException {
        LOG.debug("REST request to save Commercant : {}", commercantDTO);
        if (commercantDTO.getId() != null) {
            throw new BadRequestAlertException("A new commercant cannot already have an ID", ENTITY_NAME, "idexists");
        }
        commercantDTO = commercantService.save(commercantDTO);
        return ResponseEntity.created(new URI("/api/commercants/" + commercantDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, commercantDTO.getId().toString()))
            .body(commercantDTO);
    }

    /**
     * {@code PUT  /commercants/:id} : Updates an existing commercant.
     *
     * @param id the id of the commercantDTO to save.
     * @param commercantDTO the commercantDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated commercantDTO,
     * or with status {@code 400 (Bad Request)} if the commercantDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the commercantDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<CommercantDTO> updateCommercant(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody CommercantDTO commercantDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update Commercant : {}, {}", id, commercantDTO);
        if (commercantDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, commercantDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!commercantRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        commercantDTO = commercantService.update(commercantDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, commercantDTO.getId().toString()))
            .body(commercantDTO);
    }

    /**
     * {@code PATCH  /commercants/:id} : Partial updates given fields of an existing commercant, field will ignore if it is null
     *
     * @param id the id of the commercantDTO to save.
     * @param commercantDTO the commercantDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated commercantDTO,
     * or with status {@code 400 (Bad Request)} if the commercantDTO is not valid,
     * or with status {@code 404 (Not Found)} if the commercantDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the commercantDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<CommercantDTO> partialUpdateCommercant(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody CommercantDTO commercantDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update Commercant partially : {}, {}", id, commercantDTO);
        if (commercantDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, commercantDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!commercantRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<CommercantDTO> result = commercantService.partialUpdate(commercantDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, commercantDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /commercants} : get all the commercants.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of commercants in body.
     */
    @GetMapping("")
    public ResponseEntity<List<CommercantDTO>> getAllCommercants(CommercantCriteria criteria) {
        LOG.debug("REST request to get Commercants by criteria: {}", criteria);

        List<CommercantDTO> entityList = commercantQueryService.findByCriteria(criteria);
        return ResponseEntity.ok().body(entityList);
    }

    /**
     * {@code GET  /commercants/count} : count all the commercants.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countCommercants(CommercantCriteria criteria) {
        LOG.debug("REST request to count Commercants by criteria: {}", criteria);
        return ResponseEntity.ok().body(commercantQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /commercants/:id} : get the "id" commercant.
     *
     * @param id the id of the commercantDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the commercantDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<CommercantDTO> getCommercant(@PathVariable("id") Long id) {
        LOG.debug("REST request to get Commercant : {}", id);
        Optional<CommercantDTO> commercantDTO = commercantService.findOne(id);
        return ResponseUtil.wrapOrNotFound(commercantDTO);
    }

    /**
     * {@code DELETE  /commercants/:id} : delete the "id" commercant.
     *
     * @param id the id of the commercantDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCommercant(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete Commercant : {}", id);
        commercantService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
