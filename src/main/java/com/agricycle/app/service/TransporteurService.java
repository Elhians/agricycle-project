package com.agricycle.app.service;

import com.agricycle.app.domain.Transporteur;
import com.agricycle.app.repository.TransporteurRepository;
import com.agricycle.app.service.dto.TransporteurDTO;
import com.agricycle.app.service.mapper.TransporteurMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.agricycle.app.domain.Transporteur}.
 */
@Service
@Transactional
public class TransporteurService {

    private static final Logger LOG = LoggerFactory.getLogger(TransporteurService.class);

    private final TransporteurRepository transporteurRepository;

    private final TransporteurMapper transporteurMapper;

    public TransporteurService(TransporteurRepository transporteurRepository, TransporteurMapper transporteurMapper) {
        this.transporteurRepository = transporteurRepository;
        this.transporteurMapper = transporteurMapper;
    }

    /**
     * Save a transporteur.
     *
     * @param transporteurDTO the entity to save.
     * @return the persisted entity.
     */
    public TransporteurDTO save(TransporteurDTO transporteurDTO) {
        LOG.debug("Request to save Transporteur : {}", transporteurDTO);
        Transporteur transporteur = transporteurMapper.toEntity(transporteurDTO);
        transporteur = transporteurRepository.save(transporteur);
        return transporteurMapper.toDto(transporteur);
    }

    /**
     * Update a transporteur.
     *
     * @param transporteurDTO the entity to save.
     * @return the persisted entity.
     */
    public TransporteurDTO update(TransporteurDTO transporteurDTO) {
        LOG.debug("Request to update Transporteur : {}", transporteurDTO);
        Transporteur transporteur = transporteurMapper.toEntity(transporteurDTO);
        transporteur = transporteurRepository.save(transporteur);
        return transporteurMapper.toDto(transporteur);
    }

    /**
     * Partially update a transporteur.
     *
     * @param transporteurDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<TransporteurDTO> partialUpdate(TransporteurDTO transporteurDTO) {
        LOG.debug("Request to partially update Transporteur : {}", transporteurDTO);

        return transporteurRepository
            .findById(transporteurDTO.getId())
            .map(existingTransporteur -> {
                transporteurMapper.partialUpdate(existingTransporteur, transporteurDTO);

                return existingTransporteur;
            })
            .map(transporteurRepository::save)
            .map(transporteurMapper::toDto);
    }

    /**
     * Get one transporteur by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<TransporteurDTO> findOne(Long id) {
        LOG.debug("Request to get Transporteur : {}", id);
        return transporteurRepository.findById(id).map(transporteurMapper::toDto);
    }

    /**
     * Delete the transporteur by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete Transporteur : {}", id);
        transporteurRepository.deleteById(id);
    }
}
