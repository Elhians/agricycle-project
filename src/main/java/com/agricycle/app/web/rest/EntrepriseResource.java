package com.agricycle.app.web.rest;

import com.agricycle.app.repository.EntrepriseRepository;
import com.agricycle.app.service.EntrepriseQueryService;
import com.agricycle.app.service.EntrepriseService;
import com.agricycle.app.service.criteria.EntrepriseCriteria;
import com.agricycle.app.service.dto.EntrepriseDTO;
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
 * REST controller for managing {@link com.agricycle.app.domain.Entreprise}.
 */
@RestController
@RequestMapping("/api/entreprises")
public class EntrepriseResource {

    private static final Logger LOG = LoggerFactory.getLogger(EntrepriseResource.class);

    private static final String ENTITY_NAME = "entreprise";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final EntrepriseService entrepriseService;

    private final EntrepriseRepository entrepriseRepository;

    private final EntrepriseQueryService entrepriseQueryService;

    public EntrepriseResource(
        EntrepriseService entrepriseService,
        EntrepriseRepository entrepriseRepository,
        EntrepriseQueryService entrepriseQueryService
    ) {
        this.entrepriseService = entrepriseService;
        this.entrepriseRepository = entrepriseRepository;
        this.entrepriseQueryService = entrepriseQueryService;
    }

    /**
     * {@code POST  /entreprises} : Create a new entreprise.
     *
     * @param entrepriseDTO the entrepriseDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new entrepriseDTO, or with status {@code 400 (Bad Request)} if the entreprise has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<EntrepriseDTO> createEntreprise(@RequestBody EntrepriseDTO entrepriseDTO) throws URISyntaxException {
        LOG.debug("REST request to save Entreprise : {}", entrepriseDTO);
        if (entrepriseDTO.getId() != null) {
            throw new BadRequestAlertException("A new entreprise cannot already have an ID", ENTITY_NAME, "idexists");
        }
        entrepriseDTO = entrepriseService.save(entrepriseDTO);
        return ResponseEntity.created(new URI("/api/entreprises/" + entrepriseDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, entrepriseDTO.getId().toString()))
            .body(entrepriseDTO);
    }

    /**
     * {@code PUT  /entreprises/:id} : Updates an existing entreprise.
     *
     * @param id the id of the entrepriseDTO to save.
     * @param entrepriseDTO the entrepriseDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated entrepriseDTO,
     * or with status {@code 400 (Bad Request)} if the entrepriseDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the entrepriseDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<EntrepriseDTO> updateEntreprise(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody EntrepriseDTO entrepriseDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update Entreprise : {}, {}", id, entrepriseDTO);
        if (entrepriseDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, entrepriseDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!entrepriseRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        entrepriseDTO = entrepriseService.update(entrepriseDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, entrepriseDTO.getId().toString()))
            .body(entrepriseDTO);
    }

    /**
     * {@code PATCH  /entreprises/:id} : Partial updates given fields of an existing entreprise, field will ignore if it is null
     *
     * @param id the id of the entrepriseDTO to save.
     * @param entrepriseDTO the entrepriseDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated entrepriseDTO,
     * or with status {@code 400 (Bad Request)} if the entrepriseDTO is not valid,
     * or with status {@code 404 (Not Found)} if the entrepriseDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the entrepriseDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<EntrepriseDTO> partialUpdateEntreprise(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody EntrepriseDTO entrepriseDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update Entreprise partially : {}, {}", id, entrepriseDTO);
        if (entrepriseDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, entrepriseDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!entrepriseRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<EntrepriseDTO> result = entrepriseService.partialUpdate(entrepriseDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, entrepriseDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /entreprises} : get all the entreprises.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of entreprises in body.
     */
    @GetMapping("")
    public ResponseEntity<List<EntrepriseDTO>> getAllEntreprises(EntrepriseCriteria criteria) {
        LOG.debug("REST request to get Entreprises by criteria: {}", criteria);

        List<EntrepriseDTO> entityList = entrepriseQueryService.findByCriteria(criteria);
        return ResponseEntity.ok().body(entityList);
    }

    /**
     * {@code GET  /entreprises/count} : count all the entreprises.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countEntreprises(EntrepriseCriteria criteria) {
        LOG.debug("REST request to count Entreprises by criteria: {}", criteria);
        return ResponseEntity.ok().body(entrepriseQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /entreprises/:id} : get the "id" entreprise.
     *
     * @param id the id of the entrepriseDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the entrepriseDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<EntrepriseDTO> getEntreprise(@PathVariable("id") Long id) {
        LOG.debug("REST request to get Entreprise : {}", id);
        Optional<EntrepriseDTO> entrepriseDTO = entrepriseService.findOne(id);
        return ResponseUtil.wrapOrNotFound(entrepriseDTO);
    }

    /**
     * {@code DELETE  /entreprises/:id} : delete the "id" entreprise.
     *
     * @param id the id of the entrepriseDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEntreprise(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete Entreprise : {}", id);
        entrepriseService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
