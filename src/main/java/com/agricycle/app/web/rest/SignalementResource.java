package com.agricycle.app.web.rest;

import com.agricycle.app.repository.SignalementRepository;
import com.agricycle.app.service.SignalementQueryService;
import com.agricycle.app.service.SignalementService;
import com.agricycle.app.service.criteria.SignalementCriteria;
import com.agricycle.app.service.dto.SignalementDTO;
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
 * REST controller for managing {@link com.agricycle.app.domain.Signalement}.
 */
@RestController
@RequestMapping("/api/signalements")
public class SignalementResource {

    private static final Logger LOG = LoggerFactory.getLogger(SignalementResource.class);

    private static final String ENTITY_NAME = "signalement";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final SignalementService signalementService;

    private final SignalementRepository signalementRepository;

    private final SignalementQueryService signalementQueryService;

    public SignalementResource(
        SignalementService signalementService,
        SignalementRepository signalementRepository,
        SignalementQueryService signalementQueryService
    ) {
        this.signalementService = signalementService;
        this.signalementRepository = signalementRepository;
        this.signalementQueryService = signalementQueryService;
    }

    /**
     * {@code POST  /signalements} : Create a new signalement.
     *
     * @param signalementDTO the signalementDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new signalementDTO, or with status {@code 400 (Bad Request)} if the signalement has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<SignalementDTO> createSignalement(@Valid @RequestBody SignalementDTO signalementDTO) throws URISyntaxException {
        LOG.debug("REST request to save Signalement : {}", signalementDTO);
        if (signalementDTO.getId() != null) {
            throw new BadRequestAlertException("A new signalement cannot already have an ID", ENTITY_NAME, "idexists");
        }
        signalementDTO = signalementService.save(signalementDTO);
        return ResponseEntity.created(new URI("/api/signalements/" + signalementDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, signalementDTO.getId().toString()))
            .body(signalementDTO);
    }

    /**
     * {@code PUT  /signalements/:id} : Updates an existing signalement.
     *
     * @param id the id of the signalementDTO to save.
     * @param signalementDTO the signalementDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated signalementDTO,
     * or with status {@code 400 (Bad Request)} if the signalementDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the signalementDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<SignalementDTO> updateSignalement(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody SignalementDTO signalementDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update Signalement : {}, {}", id, signalementDTO);
        if (signalementDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, signalementDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!signalementRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        signalementDTO = signalementService.update(signalementDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, signalementDTO.getId().toString()))
            .body(signalementDTO);
    }

    /**
     * {@code PATCH  /signalements/:id} : Partial updates given fields of an existing signalement, field will ignore if it is null
     *
     * @param id the id of the signalementDTO to save.
     * @param signalementDTO the signalementDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated signalementDTO,
     * or with status {@code 400 (Bad Request)} if the signalementDTO is not valid,
     * or with status {@code 404 (Not Found)} if the signalementDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the signalementDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<SignalementDTO> partialUpdateSignalement(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody SignalementDTO signalementDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update Signalement partially : {}, {}", id, signalementDTO);
        if (signalementDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, signalementDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!signalementRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<SignalementDTO> result = signalementService.partialUpdate(signalementDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, signalementDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /signalements} : get all the signalements.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of signalements in body.
     */
    @GetMapping("")
    public ResponseEntity<List<SignalementDTO>> getAllSignalements(SignalementCriteria criteria) {
        LOG.debug("REST request to get Signalements by criteria: {}", criteria);

        List<SignalementDTO> entityList = signalementQueryService.findByCriteria(criteria);
        return ResponseEntity.ok().body(entityList);
    }

    /**
     * {@code GET  /signalements/count} : count all the signalements.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countSignalements(SignalementCriteria criteria) {
        LOG.debug("REST request to count Signalements by criteria: {}", criteria);
        return ResponseEntity.ok().body(signalementQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /signalements/:id} : get the "id" signalement.
     *
     * @param id the id of the signalementDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the signalementDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<SignalementDTO> getSignalement(@PathVariable("id") Long id) {
        LOG.debug("REST request to get Signalement : {}", id);
        Optional<SignalementDTO> signalementDTO = signalementService.findOne(id);
        return ResponseUtil.wrapOrNotFound(signalementDTO);
    }

    /**
     * {@code DELETE  /signalements/:id} : delete the "id" signalement.
     *
     * @param id the id of the signalementDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSignalement(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete Signalement : {}", id);
        signalementService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
