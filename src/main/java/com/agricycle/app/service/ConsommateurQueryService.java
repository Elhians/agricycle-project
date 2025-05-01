package com.agricycle.app.service;

import com.agricycle.app.domain.*; // for static metamodels
import com.agricycle.app.domain.Consommateur;
import com.agricycle.app.repository.ConsommateurRepository;
import com.agricycle.app.service.criteria.ConsommateurCriteria;
import com.agricycle.app.service.dto.ConsommateurDTO;
import com.agricycle.app.service.mapper.ConsommateurMapper;
import jakarta.persistence.criteria.JoinType;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link Consommateur} entities in the database.
 * The main input is a {@link ConsommateurCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link ConsommateurDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class ConsommateurQueryService extends QueryService<Consommateur> {

    private static final Logger LOG = LoggerFactory.getLogger(ConsommateurQueryService.class);

    private final ConsommateurRepository consommateurRepository;

    private final ConsommateurMapper consommateurMapper;

    public ConsommateurQueryService(ConsommateurRepository consommateurRepository, ConsommateurMapper consommateurMapper) {
        this.consommateurRepository = consommateurRepository;
        this.consommateurMapper = consommateurMapper;
    }

    /**
     * Return a {@link List} of {@link ConsommateurDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<ConsommateurDTO> findByCriteria(ConsommateurCriteria criteria) {
        LOG.debug("find by criteria : {}", criteria);
        final Specification<Consommateur> specification = createSpecification(criteria);
        return consommateurMapper.toDto(consommateurRepository.findAll(specification));
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(ConsommateurCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<Consommateur> specification = createSpecification(criteria);
        return consommateurRepository.count(specification);
    }

    /**
     * Function to convert {@link ConsommateurCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Consommateur> createSpecification(ConsommateurCriteria criteria) {
        Specification<Consommateur> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            specification = Specification.allOf(
                Boolean.TRUE.equals(criteria.getDistinct()) ? distinct(criteria.getDistinct()) : null,
                buildRangeSpecification(criteria.getId(), Consommateur_.id),
                buildStringSpecification(criteria.getPreferences(), Consommateur_.preferences),
                buildSpecification(criteria.getUtilisateurId(), root ->
                    root.join(Consommateur_.utilisateur, JoinType.LEFT).get(Utilisateur_.id)
                )
            );
        }
        return specification;
    }
}
