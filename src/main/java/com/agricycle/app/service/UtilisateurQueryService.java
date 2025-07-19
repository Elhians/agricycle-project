package com.agricycle.app.service;

import com.agricycle.app.domain.*; // for static metamodels
import com.agricycle.app.domain.Utilisateur;
import com.agricycle.app.repository.UtilisateurRepository;
import com.agricycle.app.service.criteria.UtilisateurCriteria;
import com.agricycle.app.service.dto.UtilisateurDTO;
import com.agricycle.app.service.mapper.UtilisateurMapper;
import jakarta.persistence.criteria.JoinType;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link Utilisateur} entities in the database.
 * The main input is a {@link UtilisateurCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link UtilisateurDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class UtilisateurQueryService extends QueryService<Utilisateur> {

    private static final Logger LOG = LoggerFactory.getLogger(UtilisateurQueryService.class);

    private final UtilisateurRepository utilisateurRepository;

    private final UtilisateurMapper utilisateurMapper;

    public UtilisateurQueryService(UtilisateurRepository utilisateurRepository, UtilisateurMapper utilisateurMapper) {
        this.utilisateurRepository = utilisateurRepository;
        this.utilisateurMapper = utilisateurMapper;
    }

    /**
     * Return a {@link List} of {@link UtilisateurDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<UtilisateurDTO> findByCriteria(UtilisateurCriteria criteria) {
        LOG.debug("find by criteria : {}", criteria);
        final Specification<Utilisateur> specification = createSpecification(criteria);
        return utilisateurMapper.toDto(utilisateurRepository.findAll(specification));
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(UtilisateurCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<Utilisateur> specification = createSpecification(criteria);
        return utilisateurRepository.count(specification);
    }

    /**
     * Function to convert {@link UtilisateurCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Utilisateur> createSpecification(UtilisateurCriteria criteria) {
        Specification<Utilisateur> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            specification = Specification.allOf(
                Boolean.TRUE.equals(criteria.getDistinct()) ? distinct(criteria.getDistinct()) : null,
                buildRangeSpecification(criteria.getId(), Utilisateur_.id),
                buildStringSpecification(criteria.getPhone(), Utilisateur_.phone),
                buildSpecification(criteria.getRole(), Utilisateur_.role),
                buildRangeSpecification(criteria.getDateInscription(), Utilisateur_.dateInscription),
                buildSpecification(criteria.getUserId(), root -> root.join(Utilisateur_.user, JoinType.LEFT).get(User_.id)),
                buildSpecification(criteria.getAgriculteurId(), root ->
                    root.join(Utilisateur_.agriculteur, JoinType.LEFT).get(Agriculteur_.id)
                ),
                buildSpecification(criteria.getCommercantId(), root -> root.join(Utilisateur_.commercant, JoinType.LEFT).get(Commercant_.id)
                ),
                buildSpecification(criteria.getTransporteurId(), root ->
                    root.join(Utilisateur_.transporteur, JoinType.LEFT).get(Transporteur_.id)
                ),
                buildSpecification(criteria.getConsommateurId(), root ->
                    root.join(Utilisateur_.consommateur, JoinType.LEFT).get(Consommateur_.id)
                ),
                buildSpecification(criteria.getOrganisationId(), root ->
                    root.join(Utilisateur_.organisation, JoinType.LEFT).get(Organisation_.id)
                ),
                buildSpecification(criteria.getEntrepriseId(), root -> root.join(Utilisateur_.entreprise, JoinType.LEFT).get(Entreprise_.id)
                )
            );
        }
        return specification;
    }
}
