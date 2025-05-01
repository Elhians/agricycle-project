package com.agricycle.app.service;

import com.agricycle.app.domain.*; // for static metamodels
import com.agricycle.app.domain.Commercant;
import com.agricycle.app.repository.CommercantRepository;
import com.agricycle.app.service.criteria.CommercantCriteria;
import com.agricycle.app.service.dto.CommercantDTO;
import com.agricycle.app.service.mapper.CommercantMapper;
import jakarta.persistence.criteria.JoinType;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link Commercant} entities in the database.
 * The main input is a {@link CommercantCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link CommercantDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class CommercantQueryService extends QueryService<Commercant> {

    private static final Logger LOG = LoggerFactory.getLogger(CommercantQueryService.class);

    private final CommercantRepository commercantRepository;

    private final CommercantMapper commercantMapper;

    public CommercantQueryService(CommercantRepository commercantRepository, CommercantMapper commercantMapper) {
        this.commercantRepository = commercantRepository;
        this.commercantMapper = commercantMapper;
    }

    /**
     * Return a {@link List} of {@link CommercantDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<CommercantDTO> findByCriteria(CommercantCriteria criteria) {
        LOG.debug("find by criteria : {}", criteria);
        final Specification<Commercant> specification = createSpecification(criteria);
        return commercantMapper.toDto(commercantRepository.findAll(specification));
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(CommercantCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<Commercant> specification = createSpecification(criteria);
        return commercantRepository.count(specification);
    }

    /**
     * Function to convert {@link CommercantCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Commercant> createSpecification(CommercantCriteria criteria) {
        Specification<Commercant> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            specification = Specification.allOf(
                Boolean.TRUE.equals(criteria.getDistinct()) ? distinct(criteria.getDistinct()) : null,
                buildRangeSpecification(criteria.getId(), Commercant_.id),
                buildStringSpecification(criteria.getAdresseCommerce(), Commercant_.adresseCommerce),
                buildStringSpecification(criteria.getMoyenPaiement(), Commercant_.moyenPaiement),
                buildSpecification(criteria.getUtilisateurId(), root ->
                    root.join(Commercant_.utilisateur, JoinType.LEFT).get(Utilisateur_.id)
                )
            );
        }
        return specification;
    }
}
