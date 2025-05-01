package com.agricycle.app.service;

import com.agricycle.app.domain.Consommateur;
import com.agricycle.app.repository.ConsommateurRepository;
import com.agricycle.app.service.dto.ConsommateurDTO;
import com.agricycle.app.service.mapper.ConsommateurMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.agricycle.app.domain.Consommateur}.
 */
@Service
@Transactional
public class ConsommateurService {

    private static final Logger LOG = LoggerFactory.getLogger(ConsommateurService.class);

    private final ConsommateurRepository consommateurRepository;

    private final ConsommateurMapper consommateurMapper;

    public ConsommateurService(ConsommateurRepository consommateurRepository, ConsommateurMapper consommateurMapper) {
        this.consommateurRepository = consommateurRepository;
        this.consommateurMapper = consommateurMapper;
    }

    /**
     * Save a consommateur.
     *
     * @param consommateurDTO the entity to save.
     * @return the persisted entity.
     */
    public ConsommateurDTO save(ConsommateurDTO consommateurDTO) {
        LOG.debug("Request to save Consommateur : {}", consommateurDTO);
        Consommateur consommateur = consommateurMapper.toEntity(consommateurDTO);
        consommateur = consommateurRepository.save(consommateur);
        return consommateurMapper.toDto(consommateur);
    }

    /**
     * Update a consommateur.
     *
     * @param consommateurDTO the entity to save.
     * @return the persisted entity.
     */
    public ConsommateurDTO update(ConsommateurDTO consommateurDTO) {
        LOG.debug("Request to update Consommateur : {}", consommateurDTO);
        Consommateur consommateur = consommateurMapper.toEntity(consommateurDTO);
        consommateur = consommateurRepository.save(consommateur);
        return consommateurMapper.toDto(consommateur);
    }

    /**
     * Partially update a consommateur.
     *
     * @param consommateurDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<ConsommateurDTO> partialUpdate(ConsommateurDTO consommateurDTO) {
        LOG.debug("Request to partially update Consommateur : {}", consommateurDTO);

        return consommateurRepository
            .findById(consommateurDTO.getId())
            .map(existingConsommateur -> {
                consommateurMapper.partialUpdate(existingConsommateur, consommateurDTO);

                return existingConsommateur;
            })
            .map(consommateurRepository::save)
            .map(consommateurMapper::toDto);
    }

    /**
     * Get one consommateur by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<ConsommateurDTO> findOne(Long id) {
        LOG.debug("Request to get Consommateur : {}", id);
        return consommateurRepository.findById(id).map(consommateurMapper::toDto);
    }

    /**
     * Delete the consommateur by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete Consommateur : {}", id);
        consommateurRepository.deleteById(id);
    }
}
