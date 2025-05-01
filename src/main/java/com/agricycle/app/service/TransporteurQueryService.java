package com.agricycle.app.service;

import com.agricycle.app.domain.*; // for static metamodels
import com.agricycle.app.domain.Transporteur;
import com.agricycle.app.repository.TransporteurRepository;
import com.agricycle.app.service.criteria.TransporteurCriteria;
import com.agricycle.app.service.dto.TransporteurDTO;
import com.agricycle.app.service.mapper.TransporteurMapper;
import jakarta.persistence.criteria.JoinType;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link Transporteur} entities in the database.
 * The main input is a {@link TransporteurCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link TransporteurDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class TransporteurQueryService extends QueryService<Transporteur> {

    private static final Logger LOG = LoggerFactory.getLogger(TransporteurQueryService.class);

    private final TransporteurRepository transporteurRepository;

    private final TransporteurMapper transporteurMapper;

    public TransporteurQueryService(TransporteurRepository transporteurRepository, TransporteurMapper transporteurMapper) {
        this.transporteurRepository = transporteurRepository;
        this.transporteurMapper = transporteurMapper;
    }

    /**
     * Return a {@link List} of {@link TransporteurDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<TransporteurDTO> findByCriteria(TransporteurCriteria criteria) {
        LOG.debug("find by criteria : {}", criteria);
        final Specification<Transporteur> specification = createSpecification(criteria);
        return transporteurMapper.toDto(transporteurRepository.findAll(specification));
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(TransporteurCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<Transporteur> specification = createSpecification(criteria);
        return transporteurRepository.count(specification);
    }

    /**
     * Function to convert {@link TransporteurCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Transporteur> createSpecification(TransporteurCriteria criteria) {
        Specification<Transporteur> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            specification = Specification.allOf(
                Boolean.TRUE.equals(criteria.getDistinct()) ? distinct(criteria.getDistinct()) : null,
                buildRangeSpecification(criteria.getId(), Transporteur_.id),
                buildStringSpecification(criteria.getZoneCouverture(), Transporteur_.zoneCouverture),
                buildSpecification(criteria.getTypeVehicule(), Transporteur_.typeVehicule),
                buildRangeSpecification(criteria.getCapacite(), Transporteur_.capacite),
                buildSpecification(criteria.getUtilisateurId(), root ->
                    root.join(Transporteur_.utilisateur, JoinType.LEFT).get(Utilisateur_.id)
                )
            );
        }
        return specification;
    }
}
