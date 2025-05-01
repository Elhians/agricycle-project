package com.agricycle.app.service;

import com.agricycle.app.domain.ChatbotSession;
import com.agricycle.app.repository.ChatbotSessionRepository;
import com.agricycle.app.service.dto.ChatbotSessionDTO;
import com.agricycle.app.service.mapper.ChatbotSessionMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.agricycle.app.domain.ChatbotSession}.
 */
@Service
@Transactional
public class ChatbotSessionService {

    private static final Logger LOG = LoggerFactory.getLogger(ChatbotSessionService.class);

    private final ChatbotSessionRepository chatbotSessionRepository;

    private final ChatbotSessionMapper chatbotSessionMapper;

    public ChatbotSessionService(ChatbotSessionRepository chatbotSessionRepository, ChatbotSessionMapper chatbotSessionMapper) {
        this.chatbotSessionRepository = chatbotSessionRepository;
        this.chatbotSessionMapper = chatbotSessionMapper;
    }

    /**
     * Save a chatbotSession.
     *
     * @param chatbotSessionDTO the entity to save.
     * @return the persisted entity.
     */
    public ChatbotSessionDTO save(ChatbotSessionDTO chatbotSessionDTO) {
        LOG.debug("Request to save ChatbotSession : {}", chatbotSessionDTO);
        ChatbotSession chatbotSession = chatbotSessionMapper.toEntity(chatbotSessionDTO);
        chatbotSession = chatbotSessionRepository.save(chatbotSession);
        return chatbotSessionMapper.toDto(chatbotSession);
    }

    /**
     * Update a chatbotSession.
     *
     * @param chatbotSessionDTO the entity to save.
     * @return the persisted entity.
     */
    public ChatbotSessionDTO update(ChatbotSessionDTO chatbotSessionDTO) {
        LOG.debug("Request to update ChatbotSession : {}", chatbotSessionDTO);
        ChatbotSession chatbotSession = chatbotSessionMapper.toEntity(chatbotSessionDTO);
        chatbotSession = chatbotSessionRepository.save(chatbotSession);
        return chatbotSessionMapper.toDto(chatbotSession);
    }

    /**
     * Partially update a chatbotSession.
     *
     * @param chatbotSessionDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<ChatbotSessionDTO> partialUpdate(ChatbotSessionDTO chatbotSessionDTO) {
        LOG.debug("Request to partially update ChatbotSession : {}", chatbotSessionDTO);

        return chatbotSessionRepository
            .findById(chatbotSessionDTO.getId())
            .map(existingChatbotSession -> {
                chatbotSessionMapper.partialUpdate(existingChatbotSession, chatbotSessionDTO);

                return existingChatbotSession;
            })
            .map(chatbotSessionRepository::save)
            .map(chatbotSessionMapper::toDto);
    }

    /**
     * Get one chatbotSession by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<ChatbotSessionDTO> findOne(Long id) {
        LOG.debug("Request to get ChatbotSession : {}", id);
        return chatbotSessionRepository.findById(id).map(chatbotSessionMapper::toDto);
    }

    /**
     * Delete the chatbotSession by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete ChatbotSession : {}", id);
        chatbotSessionRepository.deleteById(id);
    }
}
