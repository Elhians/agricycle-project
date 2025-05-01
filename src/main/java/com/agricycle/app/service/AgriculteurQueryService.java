package com.agricycle.app.service;

import com.agricycle.app.domain.*; // for static metamodels
import com.agricycle.app.domain.Agriculteur;
import com.agricycle.app.repository.AgriculteurRepository;
import com.agricycle.app.service.criteria.AgriculteurCriteria;
import com.agricycle.app.service.dto.AgriculteurDTO;
import com.agricycle.app.service.mapper.AgriculteurMapper;
import jakarta.persistence.criteria.JoinType;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link Agriculteur} entities in the database.
 * The main input is a {@link AgriculteurCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link AgriculteurDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class AgriculteurQueryService extends QueryService<Agriculteur> {

    private static final Logger LOG = LoggerFactory.getLogger(AgriculteurQueryService.class);

    private final AgriculteurRepository agriculteurRepository;

    private final AgriculteurMapper agriculteurMapper;

    public AgriculteurQueryService(AgriculteurRepository agriculteurRepository, AgriculteurMapper agriculteurMapper) {
        this.agriculteurRepository = agriculteurRepository;
        this.agriculteurMapper = agriculteurMapper;
    }

    /**
     * Return a {@link List} of {@link AgriculteurDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<AgriculteurDTO> findByCriteria(AgriculteurCriteria criteria) {
        LOG.debug("find by criteria : {}", criteria);
        final Specification<Agriculteur> specification = createSpecification(criteria);
        return agriculteurMapper.toDto(agriculteurRepository.findAll(specification));
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(AgriculteurCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<Agriculteur> specification = createSpecification(criteria);
        return agriculteurRepository.count(specification);
    }

    /**
     * Function to convert {@link AgriculteurCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Agriculteur> createSpecification(AgriculteurCriteria criteria) {
        Specification<Agriculteur> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            specification = Specification.allOf(
                Boolean.TRUE.equals(criteria.getDistinct()) ? distinct(criteria.getDistinct()) : null,
                buildRangeSpecification(criteria.getId(), Agriculteur_.id),
                buildStringSpecification(criteria.getTypeProduction(), Agriculteur_.typeProduction),
                buildRangeSpecification(criteria.getAnneeExperience(), Agriculteur_.anneeExperience),
                buildStringSpecification(criteria.getLocalisation(), Agriculteur_.localisation),
                buildSpecification(criteria.getUtilisateurId(), root ->
                    root.join(Agriculteur_.utilisateur, JoinType.LEFT).get(Utilisateur_.id)
                )
            );
        }
        return specification;
    }
}
