package com.agricycle.app.service;

import com.agricycle.app.domain.Commercant;
import com.agricycle.app.repository.CommercantRepository;
import com.agricycle.app.service.dto.CommercantDTO;
import com.agricycle.app.service.mapper.CommercantMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.agricycle.app.domain.Commercant}.
 */
@Service
@Transactional
public class CommercantService {

    private static final Logger LOG = LoggerFactory.getLogger(CommercantService.class);

    private final CommercantRepository commercantRepository;

    private final CommercantMapper commercantMapper;

    public CommercantService(CommercantRepository commercantRepository, CommercantMapper commercantMapper) {
        this.commercantRepository = commercantRepository;
        this.commercantMapper = commercantMapper;
    }

    /**
     * Save a commercant.
     *
     * @param commercantDTO the entity to save.
     * @return the persisted entity.
     */
    public CommercantDTO save(CommercantDTO commercantDTO) {
        LOG.debug("Request to save Commercant : {}", commercantDTO);
        Commercant commercant = commercantMapper.toEntity(commercantDTO);
        commercant = commercantRepository.save(commercant);
        return commercantMapper.toDto(commercant);
    }

    /**
     * Update a commercant.
     *
     * @param commercantDTO the entity to save.
     * @return the persisted entity.
     */
    public CommercantDTO update(CommercantDTO commercantDTO) {
        LOG.debug("Request to update Commercant : {}", commercantDTO);
        Commercant commercant = commercantMapper.toEntity(commercantDTO);
        commercant = commercantRepository.save(commercant);
        return commercantMapper.toDto(commercant);
    }

    /**
     * Partially update a commercant.
     *
     * @param commercantDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<CommercantDTO> partialUpdate(CommercantDTO commercantDTO) {
        LOG.debug("Request to partially update Commercant : {}", commercantDTO);

        return commercantRepository
            .findById(commercantDTO.getId())
            .map(existingCommercant -> {
                commercantMapper.partialUpdate(existingCommercant, commercantDTO);

                return existingCommercant;
            })
            .map(commercantRepository::save)
            .map(commercantMapper::toDto);
    }

    /**
     * Get one commercant by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<CommercantDTO> findOne(Long id) {
        LOG.debug("Request to get Commercant : {}", id);
        return commercantRepository.findById(id).map(commercantMapper::toDto);
    }

    /**
     * Delete the commercant by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete Commercant : {}", id);
        commercantRepository.deleteById(id);
    }
}
