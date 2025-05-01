package com.agricycle.app.service;

import com.agricycle.app.domain.*; // for static metamodels
import com.agricycle.app.domain.Entreprise;
import com.agricycle.app.repository.EntrepriseRepository;
import com.agricycle.app.service.criteria.EntrepriseCriteria;
import com.agricycle.app.service.dto.EntrepriseDTO;
import com.agricycle.app.service.mapper.EntrepriseMapper;
import jakarta.persistence.criteria.JoinType;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link Entreprise} entities in the database.
 * The main input is a {@link EntrepriseCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link EntrepriseDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class EntrepriseQueryService extends QueryService<Entreprise> {

    private static final Logger LOG = LoggerFactory.getLogger(EntrepriseQueryService.class);

    private final EntrepriseRepository entrepriseRepository;

    private final EntrepriseMapper entrepriseMapper;

    public EntrepriseQueryService(EntrepriseRepository entrepriseRepository, EntrepriseMapper entrepriseMapper) {
        this.entrepriseRepository = entrepriseRepository;
        this.entrepriseMapper = entrepriseMapper;
    }

    /**
     * Return a {@link List} of {@link EntrepriseDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<EntrepriseDTO> findByCriteria(EntrepriseCriteria criteria) {
        LOG.debug("find by criteria : {}", criteria);
        final Specification<Entreprise> specification = createSpecification(criteria);
        return entrepriseMapper.toDto(entrepriseRepository.findAll(specification));
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(EntrepriseCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<Entreprise> specification = createSpecification(criteria);
        return entrepriseRepository.count(specification);
    }

    /**
     * Function to convert {@link EntrepriseCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Entreprise> createSpecification(EntrepriseCriteria criteria) {
        Specification<Entreprise> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            specification = Specification.allOf(
                Boolean.TRUE.equals(criteria.getDistinct()) ? distinct(criteria.getDistinct()) : null,
                buildRangeSpecification(criteria.getId(), Entreprise_.id),
                buildStringSpecification(criteria.getNomEntreprise(), Entreprise_.nomEntreprise),
                buildStringSpecification(criteria.getTypeActivite(), Entreprise_.typeActivite),
                buildStringSpecification(criteria.getLicence(), Entreprise_.licence),
                buildStringSpecification(criteria.getAdressePhysique(), Entreprise_.adressePhysique),
                buildSpecification(criteria.getUtilisateurId(), root ->
                    root.join(Entreprise_.utilisateur, JoinType.LEFT).get(Utilisateur_.id)
                )
            );
        }
        return specification;
    }
}
