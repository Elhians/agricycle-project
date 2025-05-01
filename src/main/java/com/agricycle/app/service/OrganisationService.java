package com.agricycle.app.service;

import com.agricycle.app.domain.Organisation;
import com.agricycle.app.repository.OrganisationRepository;
import com.agricycle.app.service.dto.OrganisationDTO;
import com.agricycle.app.service.mapper.OrganisationMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.agricycle.app.domain.Organisation}.
 */
@Service
@Transactional
public class OrganisationService {

    private static final Logger LOG = LoggerFactory.getLogger(OrganisationService.class);

    private final OrganisationRepository organisationRepository;

    private final OrganisationMapper organisationMapper;

    public OrganisationService(OrganisationRepository organisationRepository, OrganisationMapper organisationMapper) {
        this.organisationRepository = organisationRepository;
        this.organisationMapper = organisationMapper;
    }

    /**
     * Save a organisation.
     *
     * @param organisationDTO the entity to save.
     * @return the persisted entity.
     */
    public OrganisationDTO save(OrganisationDTO organisationDTO) {
        LOG.debug("Request to save Organisation : {}", organisationDTO);
        Organisation organisation = organisationMapper.toEntity(organisationDTO);
        organisation = organisationRepository.save(organisation);
        return organisationMapper.toDto(organisation);
    }

    /**
     * Update a organisation.
     *
     * @param organisationDTO the entity to save.
     * @return the persisted entity.
     */
    public OrganisationDTO update(OrganisationDTO organisationDTO) {
        LOG.debug("Request to update Organisation : {}", organisationDTO);
        Organisation organisation = organisationMapper.toEntity(organisationDTO);
        organisation = organisationRepository.save(organisation);
        return organisationMapper.toDto(organisation);
    }

    /**
     * Partially update a organisation.
     *
     * @param organisationDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<OrganisationDTO> partialUpdate(OrganisationDTO organisationDTO) {
        LOG.debug("Request to partially update Organisation : {}", organisationDTO);

        return organisationRepository
            .findById(organisationDTO.getId())
            .map(existingOrganisation -> {
                organisationMapper.partialUpdate(existingOrganisation, organisationDTO);

                return existingOrganisation;
            })
            .map(organisationRepository::save)
            .map(organisationMapper::toDto);
    }

    /**
     * Get one organisation by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<OrganisationDTO> findOne(Long id) {
        LOG.debug("Request to get Organisation : {}", id);
        return organisationRepository.findById(id).map(organisationMapper::toDto);
    }

    /**
     * Delete the organisation by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete Organisation : {}", id);
        organisationRepository.deleteById(id);
    }
}
