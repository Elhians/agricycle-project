package com.agricycle.app.web.rest;

import com.agricycle.app.repository.CommentaireRepository;
import com.agricycle.app.service.CommentaireQueryService;
import com.agricycle.app.service.CommentaireService;
import com.agricycle.app.service.criteria.CommentaireCriteria;
import com.agricycle.app.service.dto.CommentaireDTO;
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
 * REST controller for managing {@link com.agricycle.app.domain.Commentaire}.
 */
@RestController
@RequestMapping("/api/commentaires")
public class CommentaireResource {

    private static final Logger LOG = LoggerFactory.getLogger(CommentaireResource.class);

    private static final String ENTITY_NAME = "commentaire";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final CommentaireService commentaireService;

    private final CommentaireRepository commentaireRepository;

    private final CommentaireQueryService commentaireQueryService;

    public CommentaireResource(
        CommentaireService commentaireService,
        CommentaireRepository commentaireRepository,
        CommentaireQueryService commentaireQueryService
    ) {
        this.commentaireService = commentaireService;
        this.commentaireRepository = commentaireRepository;
        this.commentaireQueryService = commentaireQueryService;
    }

    /**
     * {@code POST  /commentaires} : Create a new commentaire.
     *
     * @param commentaireDTO the commentaireDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new commentaireDTO, or with status {@code 400 (Bad Request)} if the commentaire has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<CommentaireDTO> createCommentaire(@Valid @RequestBody CommentaireDTO commentaireDTO) throws URISyntaxException {
        LOG.debug("REST request to save Commentaire : {}", commentaireDTO);
        if (commentaireDTO.getId() != null) {
            throw new BadRequestAlertException("A new commentaire cannot already have an ID", ENTITY_NAME, "idexists");
        }
        commentaireDTO = commentaireService.save(commentaireDTO);
        return ResponseEntity.created(new URI("/api/commentaires/" + commentaireDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, commentaireDTO.getId().toString()))
            .body(commentaireDTO);
    }

    /**
     * {@code PUT  /commentaires/:id} : Updates an existing commentaire.
     *
     * @param id the id of the commentaireDTO to save.
     * @param commentaireDTO the commentaireDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated commentaireDTO,
     * or with status {@code 400 (Bad Request)} if the commentaireDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the commentaireDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<CommentaireDTO> updateCommentaire(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody CommentaireDTO commentaireDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update Commentaire : {}, {}", id, commentaireDTO);
        if (commentaireDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, commentaireDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!commentaireRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        commentaireDTO = commentaireService.update(commentaireDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, commentaireDTO.getId().toString()))
            .body(commentaireDTO);
    }

    /**
     * {@code PATCH  /commentaires/:id} : Partial updates given fields of an existing commentaire, field will ignore if it is null
     *
     * @param id the id of the commentaireDTO to save.
     * @param commentaireDTO the commentaireDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated commentaireDTO,
     * or with status {@code 400 (Bad Request)} if the commentaireDTO is not valid,
     * or with status {@code 404 (Not Found)} if the commentaireDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the commentaireDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<CommentaireDTO> partialUpdateCommentaire(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody CommentaireDTO commentaireDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update Commentaire partially : {}, {}", id, commentaireDTO);
        if (commentaireDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, commentaireDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!commentaireRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<CommentaireDTO> result = commentaireService.partialUpdate(commentaireDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, commentaireDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /commentaires} : get all the commentaires.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of commentaires in body.
     */
    @GetMapping("")
    public ResponseEntity<List<CommentaireDTO>> getAllCommentaires(
        CommentaireCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get Commentaires by criteria: {}", criteria);

        Page<CommentaireDTO> page = commentaireQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /commentaires/count} : count all the commentaires.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countCommentaires(CommentaireCriteria criteria) {
        LOG.debug("REST request to count Commentaires by criteria: {}", criteria);
        return ResponseEntity.ok().body(commentaireQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /commentaires/:id} : get the "id" commentaire.
     *
     * @param id the id of the commentaireDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the commentaireDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<CommentaireDTO> getCommentaire(@PathVariable("id") Long id) {
        LOG.debug("REST request to get Commentaire : {}", id);
        Optional<CommentaireDTO> commentaireDTO = commentaireService.findOne(id);
        return ResponseUtil.wrapOrNotFound(commentaireDTO);
    }

    /**
     * {@code DELETE  /commentaires/:id} : delete the "id" commentaire.
     *
     * @param id the id of the commentaireDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCommentaire(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete Commentaire : {}", id);
        commentaireService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
