package com.agricycle.app.web.rest;

import com.agricycle.app.repository.ChatbotSessionRepository;
import com.agricycle.app.service.ChatbotSessionQueryService;
import com.agricycle.app.service.ChatbotSessionService;
import com.agricycle.app.service.criteria.ChatbotSessionCriteria;
import com.agricycle.app.service.dto.ChatbotSessionDTO;
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
 * REST controller for managing {@link com.agricycle.app.domain.ChatbotSession}.
 */
@RestController
@RequestMapping("/api/chatbot-sessions")
public class ChatbotSessionResource {

    private static final Logger LOG = LoggerFactory.getLogger(ChatbotSessionResource.class);

    private static final String ENTITY_NAME = "chatbotSession";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ChatbotSessionService chatbotSessionService;

    private final ChatbotSessionRepository chatbotSessionRepository;

    private final ChatbotSessionQueryService chatbotSessionQueryService;

    public ChatbotSessionResource(
        ChatbotSessionService chatbotSessionService,
        ChatbotSessionRepository chatbotSessionRepository,
        ChatbotSessionQueryService chatbotSessionQueryService
    ) {
        this.chatbotSessionService = chatbotSessionService;
        this.chatbotSessionRepository = chatbotSessionRepository;
        this.chatbotSessionQueryService = chatbotSessionQueryService;
    }

    /**
     * {@code POST  /chatbot-sessions} : Create a new chatbotSession.
     *
     * @param chatbotSessionDTO the chatbotSessionDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new chatbotSessionDTO, or with status {@code 400 (Bad Request)} if the chatbotSession has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<ChatbotSessionDTO> createChatbotSession(@RequestBody ChatbotSessionDTO chatbotSessionDTO)
        throws URISyntaxException {
        LOG.debug("REST request to save ChatbotSession : {}", chatbotSessionDTO);
        if (chatbotSessionDTO.getId() != null) {
            throw new BadRequestAlertException("A new chatbotSession cannot already have an ID", ENTITY_NAME, "idexists");
        }
        chatbotSessionDTO = chatbotSessionService.save(chatbotSessionDTO);
        return ResponseEntity.created(new URI("/api/chatbot-sessions/" + chatbotSessionDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, chatbotSessionDTO.getId().toString()))
            .body(chatbotSessionDTO);
    }

    /**
     * {@code PUT  /chatbot-sessions/:id} : Updates an existing chatbotSession.
     *
     * @param id the id of the chatbotSessionDTO to save.
     * @param chatbotSessionDTO the chatbotSessionDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated chatbotSessionDTO,
     * or with status {@code 400 (Bad Request)} if the chatbotSessionDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the chatbotSessionDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<ChatbotSessionDTO> updateChatbotSession(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody ChatbotSessionDTO chatbotSessionDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update ChatbotSession : {}, {}", id, chatbotSessionDTO);
        if (chatbotSessionDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, chatbotSessionDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!chatbotSessionRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        chatbotSessionDTO = chatbotSessionService.update(chatbotSessionDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, chatbotSessionDTO.getId().toString()))
            .body(chatbotSessionDTO);
    }

    /**
     * {@code PATCH  /chatbot-sessions/:id} : Partial updates given fields of an existing chatbotSession, field will ignore if it is null
     *
     * @param id the id of the chatbotSessionDTO to save.
     * @param chatbotSessionDTO the chatbotSessionDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated chatbotSessionDTO,
     * or with status {@code 400 (Bad Request)} if the chatbotSessionDTO is not valid,
     * or with status {@code 404 (Not Found)} if the chatbotSessionDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the chatbotSessionDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<ChatbotSessionDTO> partialUpdateChatbotSession(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody ChatbotSessionDTO chatbotSessionDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update ChatbotSession partially : {}, {}", id, chatbotSessionDTO);
        if (chatbotSessionDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, chatbotSessionDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!chatbotSessionRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ChatbotSessionDTO> result = chatbotSessionService.partialUpdate(chatbotSessionDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, chatbotSessionDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /chatbot-sessions} : get all the chatbotSessions.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of chatbotSessions in body.
     */
    @GetMapping("")
    public ResponseEntity<List<ChatbotSessionDTO>> getAllChatbotSessions(ChatbotSessionCriteria criteria) {
        LOG.debug("REST request to get ChatbotSessions by criteria: {}", criteria);

        List<ChatbotSessionDTO> entityList = chatbotSessionQueryService.findByCriteria(criteria);
        return ResponseEntity.ok().body(entityList);
    }

    /**
     * {@code GET  /chatbot-sessions/count} : count all the chatbotSessions.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countChatbotSessions(ChatbotSessionCriteria criteria) {
        LOG.debug("REST request to count ChatbotSessions by criteria: {}", criteria);
        return ResponseEntity.ok().body(chatbotSessionQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /chatbot-sessions/:id} : get the "id" chatbotSession.
     *
     * @param id the id of the chatbotSessionDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the chatbotSessionDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<ChatbotSessionDTO> getChatbotSession(@PathVariable("id") Long id) {
        LOG.debug("REST request to get ChatbotSession : {}", id);
        Optional<ChatbotSessionDTO> chatbotSessionDTO = chatbotSessionService.findOne(id);
        return ResponseUtil.wrapOrNotFound(chatbotSessionDTO);
    }

    /**
     * {@code DELETE  /chatbot-sessions/:id} : delete the "id" chatbotSession.
     *
     * @param id the id of the chatbotSessionDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteChatbotSession(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete ChatbotSession : {}", id);
        chatbotSessionService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
