package com.agricycle.app.service;

import com.agricycle.app.domain.*; // for static metamodels
import com.agricycle.app.domain.ChatbotSession;
import com.agricycle.app.repository.ChatbotSessionRepository;
import com.agricycle.app.service.criteria.ChatbotSessionCriteria;
import com.agricycle.app.service.dto.ChatbotSessionDTO;
import com.agricycle.app.service.mapper.ChatbotSessionMapper;
import jakarta.persistence.criteria.JoinType;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link ChatbotSession} entities in the database.
 * The main input is a {@link ChatbotSessionCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link ChatbotSessionDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class ChatbotSessionQueryService extends QueryService<ChatbotSession> {

    private static final Logger LOG = LoggerFactory.getLogger(ChatbotSessionQueryService.class);

    private final ChatbotSessionRepository chatbotSessionRepository;

    private final ChatbotSessionMapper chatbotSessionMapper;

    public ChatbotSessionQueryService(ChatbotSessionRepository chatbotSessionRepository, ChatbotSessionMapper chatbotSessionMapper) {
        this.chatbotSessionRepository = chatbotSessionRepository;
        this.chatbotSessionMapper = chatbotSessionMapper;
    }

    /**
     * Return a {@link List} of {@link ChatbotSessionDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<ChatbotSessionDTO> findByCriteria(ChatbotSessionCriteria criteria) {
        LOG.debug("find by criteria : {}", criteria);
        final Specification<ChatbotSession> specification = createSpecification(criteria);
        return chatbotSessionMapper.toDto(chatbotSessionRepository.findAll(specification));
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(ChatbotSessionCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<ChatbotSession> specification = createSpecification(criteria);
        return chatbotSessionRepository.count(specification);
    }

    /**
     * Function to convert {@link ChatbotSessionCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<ChatbotSession> createSpecification(ChatbotSessionCriteria criteria) {
        Specification<ChatbotSession> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            specification = Specification.allOf(
                Boolean.TRUE.equals(criteria.getDistinct()) ? distinct(criteria.getDistinct()) : null,
                buildRangeSpecification(criteria.getId(), ChatbotSession_.id),
                buildRangeSpecification(criteria.getDateDebut(), ChatbotSession_.dateDebut),
                buildRangeSpecification(criteria.getDateFin(), ChatbotSession_.dateFin),
                buildSpecification(criteria.getUtilisateurId(), root ->
                    root.join(ChatbotSession_.utilisateur, JoinType.LEFT).get(Utilisateur_.id)
                )
            );
        }
        return specification;
    }
}
