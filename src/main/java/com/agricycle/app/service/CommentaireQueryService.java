package com.agricycle.app.service;

import com.agricycle.app.domain.*; // for static metamodels
import com.agricycle.app.domain.Commentaire;
import com.agricycle.app.repository.CommentaireRepository;
import com.agricycle.app.service.criteria.CommentaireCriteria;
import com.agricycle.app.service.dto.CommentaireDTO;
import com.agricycle.app.service.mapper.CommentaireMapper;
import jakarta.persistence.criteria.JoinType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link Commentaire} entities in the database.
 * The main input is a {@link CommentaireCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link CommentaireDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class CommentaireQueryService extends QueryService<Commentaire> {

    private static final Logger LOG = LoggerFactory.getLogger(CommentaireQueryService.class);

    private final CommentaireRepository commentaireRepository;

    private final CommentaireMapper commentaireMapper;

    public CommentaireQueryService(CommentaireRepository commentaireRepository, CommentaireMapper commentaireMapper) {
        this.commentaireRepository = commentaireRepository;
        this.commentaireMapper = commentaireMapper;
    }

    /**
     * Return a {@link Page} of {@link CommentaireDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<CommentaireDTO> findByCriteria(CommentaireCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Commentaire> specification = createSpecification(criteria);
        return commentaireRepository.findAll(specification, page).map(commentaireMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(CommentaireCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<Commentaire> specification = createSpecification(criteria);
        return commentaireRepository.count(specification);
    }

    /**
     * Function to convert {@link CommentaireCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Commentaire> createSpecification(CommentaireCriteria criteria) {
        Specification<Commentaire> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            specification = Specification.allOf(
                Boolean.TRUE.equals(criteria.getDistinct()) ? distinct(criteria.getDistinct()) : null,
                buildRangeSpecification(criteria.getId(), Commentaire_.id),
                buildStringSpecification(criteria.getContenu(), Commentaire_.contenu),
                buildRangeSpecification(criteria.getDate(), Commentaire_.date),
                buildSpecification(criteria.getPostId(), root -> root.join(Commentaire_.post, JoinType.LEFT).get(Post_.id)),
                buildSpecification(criteria.getAuteurId(), root -> root.join(Commentaire_.auteur, JoinType.LEFT).get(Utilisateur_.id))
            );
        }
        return specification;
    }
}
