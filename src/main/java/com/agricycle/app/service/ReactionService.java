package com.agricycle.app.service;

import com.agricycle.app.domain.Reaction;
import com.agricycle.app.repository.ReactionRepository;
import com.agricycle.app.service.dto.ReactionDTO;
import com.agricycle.app.service.mapper.ReactionMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.agricycle.app.domain.Reaction}.
 */
@Service
@Transactional
public class ReactionService {

    private static final Logger LOG = LoggerFactory.getLogger(ReactionService.class);

    private final ReactionRepository reactionRepository;

    private final ReactionMapper reactionMapper;

    public ReactionService(ReactionRepository reactionRepository, ReactionMapper reactionMapper) {
        this.reactionRepository = reactionRepository;
        this.reactionMapper = reactionMapper;
    }

    /**
     * Save a reaction.
     *
     * @param reactionDTO the entity to save.
     * @return the persisted entity.
     */
    public ReactionDTO save(ReactionDTO reactionDTO) {
        LOG.debug("Request to save Reaction : {}", reactionDTO);
        Reaction reaction = reactionMapper.toEntity(reactionDTO);
        reaction = reactionRepository.save(reaction);
        return reactionMapper.toDto(reaction);
    }

    /**
     * Update a reaction.
     *
     * @param reactionDTO the entity to save.
     * @return the persisted entity.
     */
    public ReactionDTO update(ReactionDTO reactionDTO) {
        LOG.debug("Request to update Reaction : {}", reactionDTO);
        Reaction reaction = reactionMapper.toEntity(reactionDTO);
        reaction = reactionRepository.save(reaction);
        return reactionMapper.toDto(reaction);
    }

    /**
     * Partially update a reaction.
     *
     * @param reactionDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<ReactionDTO> partialUpdate(ReactionDTO reactionDTO) {
        LOG.debug("Request to partially update Reaction : {}", reactionDTO);

        return reactionRepository
            .findById(reactionDTO.getId())
            .map(existingReaction -> {
                reactionMapper.partialUpdate(existingReaction, reactionDTO);

                return existingReaction;
            })
            .map(reactionRepository::save)
            .map(reactionMapper::toDto);
    }

    /**
     * Get one reaction by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<ReactionDTO> findOne(Long id) {
        LOG.debug("Request to get Reaction : {}", id);
        return reactionRepository.findById(id).map(reactionMapper::toDto);
    }

    /**
     * Delete the reaction by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete Reaction : {}", id);
        reactionRepository.deleteById(id);
    }
}
