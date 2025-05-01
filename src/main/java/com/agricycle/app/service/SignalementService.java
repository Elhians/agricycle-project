package com.agricycle.app.service;

import com.agricycle.app.domain.Signalement;
import com.agricycle.app.repository.SignalementRepository;
import com.agricycle.app.service.dto.SignalementDTO;
import com.agricycle.app.service.mapper.SignalementMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.agricycle.app.domain.Signalement}.
 */
@Service
@Transactional
public class SignalementService {

    private static final Logger LOG = LoggerFactory.getLogger(SignalementService.class);

    private final SignalementRepository signalementRepository;

    private final SignalementMapper signalementMapper;

    public SignalementService(SignalementRepository signalementRepository, SignalementMapper signalementMapper) {
        this.signalementRepository = signalementRepository;
        this.signalementMapper = signalementMapper;
    }

    /**
     * Save a signalement.
     *
     * @param signalementDTO the entity to save.
     * @return the persisted entity.
     */
    public SignalementDTO save(SignalementDTO signalementDTO) {
        LOG.debug("Request to save Signalement : {}", signalementDTO);
        Signalement signalement = signalementMapper.toEntity(signalementDTO);
        signalement = signalementRepository.save(signalement);
        return signalementMapper.toDto(signalement);
    }

    /**
     * Update a signalement.
     *
     * @param signalementDTO the entity to save.
     * @return the persisted entity.
     */
    public SignalementDTO update(SignalementDTO signalementDTO) {
        LOG.debug("Request to update Signalement : {}", signalementDTO);
        Signalement signalement = signalementMapper.toEntity(signalementDTO);
        signalement = signalementRepository.save(signalement);
        return signalementMapper.toDto(signalement);
    }

    /**
     * Partially update a signalement.
     *
     * @param signalementDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<SignalementDTO> partialUpdate(SignalementDTO signalementDTO) {
        LOG.debug("Request to partially update Signalement : {}", signalementDTO);

        return signalementRepository
            .findById(signalementDTO.getId())
            .map(existingSignalement -> {
                signalementMapper.partialUpdate(existingSignalement, signalementDTO);

                return existingSignalement;
            })
            .map(signalementRepository::save)
            .map(signalementMapper::toDto);
    }

    /**
     * Get one signalement by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<SignalementDTO> findOne(Long id) {
        LOG.debug("Request to get Signalement : {}", id);
        return signalementRepository.findById(id).map(signalementMapper::toDto);
    }

    /**
     * Delete the signalement by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete Signalement : {}", id);
        signalementRepository.deleteById(id);
    }
}
