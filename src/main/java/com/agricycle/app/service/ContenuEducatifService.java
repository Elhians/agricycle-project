package com.agricycle.app.service;

import com.agricycle.app.domain.ContenuEducatif;
import com.agricycle.app.repository.ContenuEducatifRepository;
import com.agricycle.app.service.dto.ContenuEducatifDTO;
import com.agricycle.app.service.mapper.ContenuEducatifMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.agricycle.app.domain.ContenuEducatif}.
 */
@Service
@Transactional
public class ContenuEducatifService {

    private static final Logger LOG = LoggerFactory.getLogger(ContenuEducatifService.class);

    private final ContenuEducatifRepository contenuEducatifRepository;

    private final ContenuEducatifMapper contenuEducatifMapper;

    public ContenuEducatifService(ContenuEducatifRepository contenuEducatifRepository, ContenuEducatifMapper contenuEducatifMapper) {
        this.contenuEducatifRepository = contenuEducatifRepository;
        this.contenuEducatifMapper = contenuEducatifMapper;
    }

    /**
     * Save a contenuEducatif.
     *
     * @param contenuEducatifDTO the entity to save.
     * @return the persisted entity.
     */
    public ContenuEducatifDTO save(ContenuEducatifDTO contenuEducatifDTO) {
        LOG.debug("Request to save ContenuEducatif : {}", contenuEducatifDTO);
        ContenuEducatif contenuEducatif = contenuEducatifMapper.toEntity(contenuEducatifDTO);
        contenuEducatif = contenuEducatifRepository.save(contenuEducatif);
        return contenuEducatifMapper.toDto(contenuEducatif);
    }

    /**
     * Update a contenuEducatif.
     *
     * @param contenuEducatifDTO the entity to save.
     * @return the persisted entity.
     */
    public ContenuEducatifDTO update(ContenuEducatifDTO contenuEducatifDTO) {
        LOG.debug("Request to update ContenuEducatif : {}", contenuEducatifDTO);
        ContenuEducatif contenuEducatif = contenuEducatifMapper.toEntity(contenuEducatifDTO);
        contenuEducatif = contenuEducatifRepository.save(contenuEducatif);
        return contenuEducatifMapper.toDto(contenuEducatif);
    }

    /**
     * Partially update a contenuEducatif.
     *
     * @param contenuEducatifDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<ContenuEducatifDTO> partialUpdate(ContenuEducatifDTO contenuEducatifDTO) {
        LOG.debug("Request to partially update ContenuEducatif : {}", contenuEducatifDTO);

        return contenuEducatifRepository
            .findById(contenuEducatifDTO.getId())
            .map(existingContenuEducatif -> {
                contenuEducatifMapper.partialUpdate(existingContenuEducatif, contenuEducatifDTO);

                return existingContenuEducatif;
            })
            .map(contenuEducatifRepository::save)
            .map(contenuEducatifMapper::toDto);
    }

    /**
     * Get one contenuEducatif by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<ContenuEducatifDTO> findOne(Long id) {
        LOG.debug("Request to get ContenuEducatif : {}", id);
        return contenuEducatifRepository.findById(id).map(contenuEducatifMapper::toDto);
    }

    /**
     * Delete the contenuEducatif by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete ContenuEducatif : {}", id);
        contenuEducatifRepository.deleteById(id);
    }
}
