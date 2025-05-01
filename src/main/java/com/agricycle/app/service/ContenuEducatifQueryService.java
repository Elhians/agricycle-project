package com.agricycle.app.service;

import com.agricycle.app.domain.*; // for static metamodels
import com.agricycle.app.domain.ContenuEducatif;
import com.agricycle.app.repository.ContenuEducatifRepository;
import com.agricycle.app.service.criteria.ContenuEducatifCriteria;
import com.agricycle.app.service.dto.ContenuEducatifDTO;
import com.agricycle.app.service.mapper.ContenuEducatifMapper;
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
 * Service for executing complex queries for {@link ContenuEducatif} entities in the database.
 * The main input is a {@link ContenuEducatifCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link ContenuEducatifDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class ContenuEducatifQueryService extends QueryService<ContenuEducatif> {

    private static final Logger LOG = LoggerFactory.getLogger(ContenuEducatifQueryService.class);

    private final ContenuEducatifRepository contenuEducatifRepository;

    private final ContenuEducatifMapper contenuEducatifMapper;

    public ContenuEducatifQueryService(ContenuEducatifRepository contenuEducatifRepository, ContenuEducatifMapper contenuEducatifMapper) {
        this.contenuEducatifRepository = contenuEducatifRepository;
        this.contenuEducatifMapper = contenuEducatifMapper;
    }

    /**
     * Return a {@link Page} of {@link ContenuEducatifDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<ContenuEducatifDTO> findByCriteria(ContenuEducatifCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<ContenuEducatif> specification = createSpecification(criteria);
        return contenuEducatifRepository.findAll(specification, page).map(contenuEducatifMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(ContenuEducatifCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<ContenuEducatif> specification = createSpecification(criteria);
        return contenuEducatifRepository.count(specification);
    }

    /**
     * Function to convert {@link ContenuEducatifCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<ContenuEducatif> createSpecification(ContenuEducatifCriteria criteria) {
        Specification<ContenuEducatif> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            specification = Specification.allOf(
                Boolean.TRUE.equals(criteria.getDistinct()) ? distinct(criteria.getDistinct()) : null,
                buildRangeSpecification(criteria.getId(), ContenuEducatif_.id),
                buildStringSpecification(criteria.getTitre(), ContenuEducatif_.titre),
                buildStringSpecification(criteria.getDescription(), ContenuEducatif_.description),
                buildSpecification(criteria.getTypeMedia(), ContenuEducatif_.typeMedia),
                buildStringSpecification(criteria.getUrl(), ContenuEducatif_.url),
                buildRangeSpecification(criteria.getDatePublication(), ContenuEducatif_.datePublication),
                buildSpecification(criteria.getAuteurId(), root -> root.join(ContenuEducatif_.auteur, JoinType.LEFT).get(Utilisateur_.id))
            );
        }
        return specification;
    }
}
