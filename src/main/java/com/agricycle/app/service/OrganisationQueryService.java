package com.agricycle.app.service;

import com.agricycle.app.domain.*; // for static metamodels
import com.agricycle.app.domain.Organisation;
import com.agricycle.app.repository.OrganisationRepository;
import com.agricycle.app.service.criteria.OrganisationCriteria;
import com.agricycle.app.service.dto.OrganisationDTO;
import com.agricycle.app.service.mapper.OrganisationMapper;
import jakarta.persistence.criteria.JoinType;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link Organisation} entities in the database.
 * The main input is a {@link OrganisationCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link OrganisationDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class OrganisationQueryService extends QueryService<Organisation> {

    private static final Logger LOG = LoggerFactory.getLogger(OrganisationQueryService.class);

    private final OrganisationRepository organisationRepository;

    private final OrganisationMapper organisationMapper;

    public OrganisationQueryService(OrganisationRepository organisationRepository, OrganisationMapper organisationMapper) {
        this.organisationRepository = organisationRepository;
        this.organisationMapper = organisationMapper;
    }

    /**
     * Return a {@link List} of {@link OrganisationDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<OrganisationDTO> findByCriteria(OrganisationCriteria criteria) {
        LOG.debug("find by criteria : {}", criteria);
        final Specification<Organisation> specification = createSpecification(criteria);
        return organisationMapper.toDto(organisationRepository.findAll(specification));
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(OrganisationCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<Organisation> specification = createSpecification(criteria);
        return organisationRepository.count(specification);
    }

    /**
     * Function to convert {@link OrganisationCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Organisation> createSpecification(OrganisationCriteria criteria) {
        Specification<Organisation> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            specification = Specification.allOf(
                Boolean.TRUE.equals(criteria.getDistinct()) ? distinct(criteria.getDistinct()) : null,
                buildRangeSpecification(criteria.getId(), Organisation_.id),
                buildStringSpecification(criteria.getNomOrganisation(), Organisation_.nomOrganisation),
                buildStringSpecification(criteria.getSiteWeb(), Organisation_.siteWeb),
                buildSpecification(criteria.getUtilisateurId(), root ->
                    root.join(Organisation_.utilisateur, JoinType.LEFT).get(Utilisateur_.id)
                )
            );
        }
        return specification;
    }
}
