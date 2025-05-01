package com.agricycle.app.service;

import com.agricycle.app.domain.Commentaire;
import com.agricycle.app.repository.CommentaireRepository;
import com.agricycle.app.service.dto.CommentaireDTO;
import com.agricycle.app.service.mapper.CommentaireMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.agricycle.app.domain.Commentaire}.
 */
@Service
@Transactional
public class CommentaireService {

    private static final Logger LOG = LoggerFactory.getLogger(CommentaireService.class);

    private final CommentaireRepository commentaireRepository;

    private final CommentaireMapper commentaireMapper;

    public CommentaireService(CommentaireRepository commentaireRepository, CommentaireMapper commentaireMapper) {
        this.commentaireRepository = commentaireRepository;
        this.commentaireMapper = commentaireMapper;
    }

    /**
     * Save a commentaire.
     *
     * @param commentaireDTO the entity to save.
     * @return the persisted entity.
     */
    public CommentaireDTO save(CommentaireDTO commentaireDTO) {
        LOG.debug("Request to save Commentaire : {}", commentaireDTO);
        Commentaire commentaire = commentaireMapper.toEntity(commentaireDTO);
        commentaire = commentaireRepository.save(commentaire);
        return commentaireMapper.toDto(commentaire);
    }

    /**
     * Update a commentaire.
     *
     * @param commentaireDTO the entity to save.
     * @return the persisted entity.
     */
    public CommentaireDTO update(CommentaireDTO commentaireDTO) {
        LOG.debug("Request to update Commentaire : {}", commentaireDTO);
        Commentaire commentaire = commentaireMapper.toEntity(commentaireDTO);
        commentaire = commentaireRepository.save(commentaire);
        return commentaireMapper.toDto(commentaire);
    }

    /**
     * Partially update a commentaire.
     *
     * @param commentaireDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<CommentaireDTO> partialUpdate(CommentaireDTO commentaireDTO) {
        LOG.debug("Request to partially update Commentaire : {}", commentaireDTO);

        return commentaireRepository
            .findById(commentaireDTO.getId())
            .map(existingCommentaire -> {
                commentaireMapper.partialUpdate(existingCommentaire, commentaireDTO);

                return existingCommentaire;
            })
            .map(commentaireRepository::save)
            .map(commentaireMapper::toDto);
    }

    /**
     * Get one commentaire by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<CommentaireDTO> findOne(Long id) {
        LOG.debug("Request to get Commentaire : {}", id);
        return commentaireRepository.findById(id).map(commentaireMapper::toDto);
    }

    /**
     * Delete the commentaire by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete Commentaire : {}", id);
        commentaireRepository.deleteById(id);
    }
}
