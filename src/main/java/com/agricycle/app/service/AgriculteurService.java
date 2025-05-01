package com.agricycle.app.service;

import com.agricycle.app.domain.Agriculteur;
import com.agricycle.app.repository.AgriculteurRepository;
import com.agricycle.app.service.dto.AgriculteurDTO;
import com.agricycle.app.service.mapper.AgriculteurMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.agricycle.app.domain.Agriculteur}.
 */
@Service
@Transactional
public class AgriculteurService {

    private static final Logger LOG = LoggerFactory.getLogger(AgriculteurService.class);

    private final AgriculteurRepository agriculteurRepository;

    private final AgriculteurMapper agriculteurMapper;

    public AgriculteurService(AgriculteurRepository agriculteurRepository, AgriculteurMapper agriculteurMapper) {
        this.agriculteurRepository = agriculteurRepository;
        this.agriculteurMapper = agriculteurMapper;
    }

    /**
     * Save a agriculteur.
     *
     * @param agriculteurDTO the entity to save.
     * @return the persisted entity.
     */
    public AgriculteurDTO save(AgriculteurDTO agriculteurDTO) {
        LOG.debug("Request to save Agriculteur : {}", agriculteurDTO);
        Agriculteur agriculteur = agriculteurMapper.toEntity(agriculteurDTO);
        agriculteur = agriculteurRepository.save(agriculteur);
        return agriculteurMapper.toDto(agriculteur);
    }

    /**
     * Update a agriculteur.
     *
     * @param agriculteurDTO the entity to save.
     * @return the persisted entity.
     */
    public AgriculteurDTO update(AgriculteurDTO agriculteurDTO) {
        LOG.debug("Request to update Agriculteur : {}", agriculteurDTO);
        Agriculteur agriculteur = agriculteurMapper.toEntity(agriculteurDTO);
        agriculteur = agriculteurRepository.save(agriculteur);
        return agriculteurMapper.toDto(agriculteur);
    }

    /**
     * Partially update a agriculteur.
     *
     * @param agriculteurDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<AgriculteurDTO> partialUpdate(AgriculteurDTO agriculteurDTO) {
        LOG.debug("Request to partially update Agriculteur : {}", agriculteurDTO);

        return agriculteurRepository
            .findById(agriculteurDTO.getId())
            .map(existingAgriculteur -> {
                agriculteurMapper.partialUpdate(existingAgriculteur, agriculteurDTO);

                return existingAgriculteur;
            })
            .map(agriculteurRepository::save)
            .map(agriculteurMapper::toDto);
    }

    /**
     * Get one agriculteur by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<AgriculteurDTO> findOne(Long id) {
        LOG.debug("Request to get Agriculteur : {}", id);
        return agriculteurRepository.findById(id).map(agriculteurMapper::toDto);
    }

    /**
     * Delete the agriculteur by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete Agriculteur : {}", id);
        agriculteurRepository.deleteById(id);
    }
}
