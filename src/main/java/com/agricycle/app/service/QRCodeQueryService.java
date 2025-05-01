package com.agricycle.app.service;

import com.agricycle.app.domain.*; // for static metamodels
import com.agricycle.app.domain.QRCode;
import com.agricycle.app.repository.QRCodeRepository;
import com.agricycle.app.service.criteria.QRCodeCriteria;
import com.agricycle.app.service.dto.QRCodeDTO;
import com.agricycle.app.service.mapper.QRCodeMapper;
import jakarta.persistence.criteria.JoinType;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link QRCode} entities in the database.
 * The main input is a {@link QRCodeCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link QRCodeDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class QRCodeQueryService extends QueryService<QRCode> {

    private static final Logger LOG = LoggerFactory.getLogger(QRCodeQueryService.class);

    private final QRCodeRepository qRCodeRepository;

    private final QRCodeMapper qRCodeMapper;

    public QRCodeQueryService(QRCodeRepository qRCodeRepository, QRCodeMapper qRCodeMapper) {
        this.qRCodeRepository = qRCodeRepository;
        this.qRCodeMapper = qRCodeMapper;
    }

    /**
     * Return a {@link List} of {@link QRCodeDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<QRCodeDTO> findByCriteria(QRCodeCriteria criteria) {
        LOG.debug("find by criteria : {}", criteria);
        final Specification<QRCode> specification = createSpecification(criteria);
        return qRCodeMapper.toDto(qRCodeRepository.findAll(specification));
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(QRCodeCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<QRCode> specification = createSpecification(criteria);
        return qRCodeRepository.count(specification);
    }

    /**
     * Function to convert {@link QRCodeCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<QRCode> createSpecification(QRCodeCriteria criteria) {
        Specification<QRCode> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            specification = Specification.allOf(
                Boolean.TRUE.equals(criteria.getDistinct()) ? distinct(criteria.getDistinct()) : null,
                buildRangeSpecification(criteria.getId(), QRCode_.id),
                buildStringSpecification(criteria.getValeur(), QRCode_.valeur),
                buildRangeSpecification(criteria.getDateExpiration(), QRCode_.dateExpiration),
                buildSpecification(criteria.getTransactionId(), root -> root.join(QRCode_.transaction, JoinType.LEFT).get(Transaction_.id))
            );
        }
        return specification;
    }
}
