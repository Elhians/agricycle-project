package com.agricycle.app.service;

import com.agricycle.app.domain.*; // for static metamodels
import com.agricycle.app.domain.Reaction;
import com.agricycle.app.repository.ReactionRepository;
import com.agricycle.app.service.criteria.ReactionCriteria;
import com.agricycle.app.service.dto.ReactionDTO;
import com.agricycle.app.service.mapper.ReactionMapper;
import jakarta.persistence.criteria.JoinType;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link Reaction} entities in the database.
 * The main input is a {@link ReactionCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link ReactionDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class ReactionQueryService extends QueryService<Reaction> {

    private static final Logger LOG = LoggerFactory.getLogger(ReactionQueryService.class);

    private final ReactionRepository reactionRepository;

    private final ReactionMapper reactionMapper;

    public ReactionQueryService(ReactionRepository reactionRepository, ReactionMapper reactionMapper) {
        this.reactionRepository = reactionRepository;
        this.reactionMapper = reactionMapper;
    }

    /**
     * Return a {@link List} of {@link ReactionDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<ReactionDTO> findByCriteria(ReactionCriteria criteria) {
        LOG.debug("find by criteria : {}", criteria);
        final Specification<Reaction> specification = createSpecification(criteria);
        return reactionMapper.toDto(reactionRepository.findAll(specification));
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(ReactionCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<Reaction> specification = createSpecification(criteria);
        return reactionRepository.count(specification);
    }

    /**
     * Function to convert {@link ReactionCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Reaction> createSpecification(ReactionCriteria criteria) {
        Specification<Reaction> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            specification = Specification.allOf(
                Boolean.TRUE.equals(criteria.getDistinct()) ? distinct(criteria.getDistinct()) : null,
                buildRangeSpecification(criteria.getId(), Reaction_.id),
                buildSpecification(criteria.getType(), Reaction_.type),
                buildRangeSpecification(criteria.getDate(), Reaction_.date),
                buildSpecification(criteria.getCible(), Reaction_.cible),
                buildSpecification(criteria.getUtilisateurId(), root -> root.join(Reaction_.utilisateur, JoinType.LEFT).get(Utilisateur_.id)
                ),
                buildSpecification(criteria.getPostId(), root -> root.join(Reaction_.post, JoinType.LEFT).get(Post_.id))
            );
        }
        return specification;
    }
}
